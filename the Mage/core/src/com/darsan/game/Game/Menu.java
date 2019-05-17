package com.darsan.game.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.darsan.game.tools.ConnectDatabase;

import java.sql.SQLException;

public class Menu implements Screen, InputProcessor {
    private Texture newGame;
    private Texture loadGame;
    private Texture endGame;
    private Texture fSave;
    private Texture sSave;
    private Sprite newsprite;
    private Sprite loadsprite;
    private Sprite endsprite;
    private Sprite fSaveSprite;
    private Sprite sSaveSprite;
    private SpriteBatch batch;
    private Vector3 temp;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    final static float screenx = Gdx.graphics.getWidth();
    final static float screeny = Gdx.graphics.getHeight();
    final static float newWidth = Math.round(screenx / 5);
    final static float newHeight = Math.round(screeny / 7);
    final static private float newx = Math.round(100);
    final static private float newy = Math.round(screeny - 200);
    MyGdxGame game;
    private boolean loadpressed;
    private ConnectDatabase db;
    private Texture tSave;
    private Sprite tSaveSprite;
    private Texture foSave;
    private Sprite foSaveSprite;
    public Menu(MyGdxGame game) {
        this.game = game;
        this.batch=game.batch;
    }

    @Override
    public void show() {

        temp = new Vector3();
        newGame = new Texture("newgame.png");
        loadGame = new Texture("load.png");
        endGame = new Texture("endgame.png");
        fSave=new Texture("1.png");
        sSave=new Texture("2.png");
        tSave=new Texture("3.png");
        foSave=new Texture("4.png");
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        newsprite = new Sprite(newGame);
        loadsprite = new Sprite(loadGame);
        endsprite = new Sprite(endGame);
        fSaveSprite=new Sprite(fSave);
        sSaveSprite=new Sprite(sSave);
        tSaveSprite=new Sprite(tSave);
        foSaveSprite=new Sprite(foSave);
        newsprite.setSize(newWidth, newHeight);
        endsprite.setSize(newWidth, newHeight);
        loadsprite.setSize(newWidth, newHeight);
        newsprite.setPosition(newx, newy);
        loadsprite.setPosition(newx, newy - 100);
        endsprite.setPosition(newx, newy - 200);
        fSaveSprite.setBounds(newx+250,newy-100,150,100);
        sSaveSprite.setBounds(newx+430,newy-100,150,100);
        tSaveSprite.setBounds(newx+610,newy-100,150,100);
        foSaveSprite.setBounds(newx+790,newy-100,150,100);
        db =new ConnectDatabase();
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        newsprite.draw(batch);
        loadsprite.draw(batch);
        endsprite.draw(batch);
        if(loadpressed){
fSaveSprite.draw(batch);
sSaveSprite.draw(batch);
tSaveSprite.draw(batch);
foSaveSprite.draw(batch);
        }
        batch.end();


    }

    @Override
    public void resize(int width, int height) {

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
        shapeRenderer.dispose();
        newGame.dispose();
        loadGame.dispose();
        endGame.dispose();
        batch.dispose();
        shapeRenderer.dispose();

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        temp.set(screenX, screenY, 0);
        camera.unproject(temp);
        if (newsprite.getBoundingRectangle().contains(temp.x, temp.y)) {

            game.setScreen(new MainGameScreen(game,null));

        } else if (endsprite.getBoundingRectangle().contains(temp.x, temp.y)) {
            Gdx.app.exit();
        } else if (loadsprite.getBoundingRectangle().contains(temp.x, temp.y)){

            loadpressed = !loadpressed;
    }else if(sSaveSprite.getBoundingRectangle().contains(temp.x,temp.y)&&loadpressed){
            try {
                if(!db.selectbyId(2).isClosed())
                game.setScreen(new MainGameScreen(game, MyGdxGame.connectDatabase.selectbyId(2)));
                else {
                    System.out.println("rs null");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else if(fSaveSprite.getBoundingRectangle().contains(temp.x,temp.y)&&loadpressed){
            try {
                if(!db.selectbyId(1).isClosed()) {
                    game.setScreen(new MainGameScreen(game,  MyGdxGame.connectDatabase.selectbyId(1)));
                }else {
                    System.out.println("rs null");
                }
    } catch (SQLException e) {
        e.printStackTrace();
    }

        }else if(tSaveSprite.getBoundingRectangle().contains(temp.x,temp.y)&&loadpressed){
            try {
                if(!db.selectbyId(3).isClosed()) {
                    System.out.println("first");
                    game.setScreen(new MainGameScreen(game,  MyGdxGame.connectDatabase.selectbyId(3)));
                }else {
                    System.out.println("rs is closed");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }else if(tSaveSprite.getBoundingRectangle().contains(temp.x,temp.y)&&loadpressed){
            try {
                if(!db.selectbyId(4).isClosed()) {
                    game.setScreen(new MainGameScreen(game,  MyGdxGame.connectDatabase.selectbyId(4)));
                }else {
                    System.out.println("rs null");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
