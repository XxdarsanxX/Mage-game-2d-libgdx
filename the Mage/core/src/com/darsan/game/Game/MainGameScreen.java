package com.darsan.game.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.darsan.game.charecters.Archer;
import com.darsan.game.charecters.MiniBoss;
import com.darsan.game.charecters.Myhero;
import com.darsan.game.charecters.Warrior;
import com.darsan.game.detecters.WorldContactListner;
import com.darsan.game.huds.Hud;
import com.darsan.game.spells.Arrow;
import com.darsan.game.spells.GreenBall;
import com.darsan.game.spells.ShadowBall;
import com.darsan.game.tools.Creater;
import com.darsan.game.worldobjects.Barrier;
import com.darsan.game.worldobjects.Castle;
import com.darsan.game.worldobjects.Chest;
import com.darsan.game.worldobjects.End;
import com.darsan.game.worldobjects.Ground;
import com.darsan.game.worldobjects.Ladder;
import com.darsan.game.worldobjects.Lava;
import com.darsan.game.worldobjects.Save;
import com.darsan.game.worldobjects.Stairs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class MainGameScreen implements Screen {


    public ArrayList<ShadowBall>shadowBalls;
    public ArrayList<Arrow>arrows;
    public ArrayList<GreenBall> greenBalls;
    private Creater creator;
    private TextureAtlas atlas;
    private World world;
    //private Box2DDebugRenderer debugRenderer;
    //mapstuff
    private TiledMap map;
    private AssetManager manager;
    // Camera and render
    public OrthographicCamera camera;
    private OrthogonalTiledMapRenderer renderer;
    //end of mapstuff
    public Myhero hero;
    public MyGdxGame game;
    public SpriteBatch batch;
public Music music;

    public Hud hud;
    private MiniBoss miniBoss;
    private  boolean paused;
    private FitViewport camport;


    MainGameScreen(MyGdxGame game, ResultSet resultSet) {
        this.game = game;
        this.batch=game.batch;
        manager = new AssetManager();
        atlas = new TextureAtlas("charectersprites/myAsset.pack");
        manager.setLoader(TiledMap.class, new TmxMapLoader());
        manager.load("maps/mymap.tmx", TiledMap.class);
        manager.finishLoading();
        map = manager.get("maps/mymap.tmx", TiledMap.class);
        camera = new OrthographicCamera();

        // Instantiation of the render for the map object

        camport =  new FitViewport(MyGdxGame.V_WIDTH/MyGdxGame.ppm,MyGdxGame.V_HEIGHT/MyGdxGame.ppm,camera);
        camport.apply();
        camera.position.set(camport.getWorldWidth() / 2f, camport.getWorldHeight() / 2, 0);

        renderer = new OrthogonalTiledMapRenderer(map, 1f/ game.ppm);
        worldManger();
        MapObject mapObject=map.getLayers().get(10).getObjects().get(0);
        Rectangle rect = ((RectangleMapObject) mapObject).getRectangle();
        hero = new Myhero(world, this, game,rect.x/MyGdxGame.ppm,rect.y/MyGdxGame.ppm);
        mapObject=map.getLayers().get(11).getObjects().get(0);
        rect = ((RectangleMapObject) mapObject).getRectangle();
        music =Gdx.audio.newMusic(Gdx.files.internal("sound/music.ogg"));
        music.setLooping(true);
        music.setVolume(0.2f);
        music.play();
        miniBoss = new MiniBoss(this,rect.x/MyGdxGame.ppm,rect.y/MyGdxGame.ppm,
                "myWizardStudent",new Barrier(this,map.getLayers().get(13).getObjects().get(0)));

        world.setContactListener(new WorldContactListner());
        shadowBalls=new ArrayList<ShadowBall>();
        arrows=new ArrayList<Arrow>();
        greenBalls=new ArrayList<GreenBall>();
        creator=new Creater(this);
        hud=new Hud(batch,this);
        try {
            //s_id INTEGER PRIMARY KEY, health TEXT NOT NULL,  x TEXT NOT NULL,  y TEXT NOT NULL)
            System.out.println(resultSet==null);
            if(resultSet!=null)
            hero.load(resultSet.getInt(2), resultSet.getFloat(3), resultSet.getFloat(4));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void show() {
        //debugRenderer = new Box2DDebugRenderer();


    }

    @Override
    public void render(float delta) {
        if (!paused) {
            music.play();
            // Update the camera and render
            update(delta);
            doGravity();

        }
            else {
            batch.setProjectionMatrix(hud.stage.getCamera().combined);
            music.pause();
            batch.begin();
            if(hero.isSaveButtonPressed()) {
        hero.fsavepointsprite.draw(batch);
        hero.ssavepointsprite.draw(batch);
        hero.tsavepointsprite.draw(batch);
        hero.fosavepointsprite.draw(batch);
            }else {
                hero.resumesprite.draw(batch);
                hero.menusprite.draw(batch);
                hero.exitsprite.draw(batch);
            }
batch.end();
        }
            batch.setProjectionMatrix(hud.stage.getCamera().combined);

            batch.begin();
            for (int i = 0; i < Myhero.getHealth(); i++)
                batch.draw(new Texture("heart.png"), i * 32, 700, 32, 32);
            batch.end();
            hud.stage.draw();
            hero.drawMyhero(batch, camera);

            batch.begin();

            for (Warrior warrior : creator.getWarrior())
                warrior.draw(batch);
            for (Archer archer : creator.getArcher())
                archer.draw(batch);
            for (Arrow arrow : arrows
                    ) {
                arrow.draw(batch);
            }
            for (GreenBall greenBall : greenBalls) {
                greenBall.draw(batch);

            }
            miniBoss.draw(batch);
            batch.end();
    }
    private void update(float dt) {

        Gdx.gl.glClearColor(.5f, .7f, .9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.setView(camera);
        renderer.render();

        camera.position.x = hero.body.getPosition().x;
        camera.position.y = hero.body.getPosition().y;
        for(Warrior warrior : creator.getWarrior()) {
                if(!warrior.destroyed) {
                    warrior.update(dt);
            }
        }

hero.update(dt);
miniBoss.update(dt);
            for(Archer archer : creator.getArcher()) {
  if(!archer.destroyed) {

            if(archer.getX()<(hero.getX()+MyGdxGame.V_WIDTH/MyGdxGame.ppm)&&archer.getX()>hero.getX()-MyGdxGame.V_WIDTH/MyGdxGame.ppm) {
                archer.body.setActive(true);
                archer.update(dt);
            }else {
                archer.body.setActive(false);
            }
                }
//entity.update(dt);
        }
        camera.update();
        //debugRenderer.render(world, camera.combined);
        for (GreenBall greenBall : greenBalls) {
            greenBall.update();
        }


}


    private void doGravity() {
        float WORLDSTEP = 0.016666668f;
        world.step(WORLDSTEP, 6, 2);
    }


    private void worldManger() {
        world = new World(new Vector2(0, -9.81f), true);
        //MapObject x = map.getLayers().get(1).getObjects().get(0);
        new End(this,map.getLayers().get(14).getObjects().get(0));
            for (MapObject object:map.getLayers().get(2).getObjects()
                 ) {
           new Ground(this,object);
            }
            for (MapObject object:map.getLayers().get(3).getObjects()
                    ) {
               new Castle(this,object);            }
        for (MapObject object:map.getLayers().get(6).getObjects()
                ) {
            new Stairs(this,object);
        }
        for (MapObject object:map.getLayers().get(5).getObjects()
                ) {
            new Lava(this,object);
        }
        for (MapObject object:map.getLayers().get(8).getObjects()
                ) {
            new Ladder(this,object);
        }
        for (MapObject object:map.getLayers().get(8).getObjects()
        ) {
            new Ladder(this,object);
        }
        for (MapObject object:map.getLayers().get(15).getObjects()
        ) {
            new Save(this,object);
        }
        int i=0;
        for(MapObject object:map.getLayers().get(9).getObjects()){
new Chest(this,object);
        }




    }



    @Override
    public void resize(int width, int height) {
camport.update(width,height);
camport.apply();
camera.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        manager.dispose();

    }


    public World getWorld() {
        return world;
    }

    public TiledMap getMap() {
        return map;
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }


    public void setpaused(boolean paused) {

        this.paused =paused;

    }

    public boolean getIsPaused() {
        return paused;
    }
}
