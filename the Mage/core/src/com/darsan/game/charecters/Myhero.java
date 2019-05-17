package com.darsan.game.charecters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.darsan.game.Game.GameOverScreen;
import com.darsan.game.Game.MainGameScreen;
import com.darsan.game.Game.Menu;
import com.darsan.game.Game.MyGdxGame;
import com.darsan.game.spells.ShadowBall;
import com.darsan.game.worldobjects.Chest;

import java.sql.SQLException;

import static com.badlogic.gdx.Gdx.input;

public class Myhero extends Sprite implements InputProcessor {
    public static int health = 6;
    public final Texture fsavepoint;
    public final Sprite fsavepointsprite;
    public final Texture ssavepoint;
    public final Sprite ssavepointsprite;
    private final Texture tsavepoint;
    public final Sprite tsavepointsprite;
    public final Sprite fosavepointsprite;
    private final Texture fosavepoint;
    public long timer;
    private Texture menu;
    public Sprite menusprite;
    private TextureRegion heroStand;
    public Vector2 movement;
    public boolean canjump;
    public boolean jumped;
    public long attackTimer;
    public boolean attacked;
    private float floattimer;
    public boolean onstairs;
    public boolean onLadder;
    private boolean isonChest;
    public Music shadowballEffect;
public boolean onsavepoint;
public Chest chest;
    public  Vector3 temp;
    public  Texture resume;
    public  Sprite resumesprite;
    public  Texture exit;
    public  Sprite exitsprite;
    private boolean saveButtonPressed;
    public static State currentState;
    public State previousState;
    private Animation heroRun;
    private TextureRegion heroAttack;
    public static boolean runningRight;
    public World world;
    public static Body body;
    private com.darsan.game.Game.MyGdxGame game;
    private Music footstep;




    public enum State {DEAD, JUMPING, SANDING, RUNNING, ATTACKING}


    public Myhero(World world, MainGameScreen screen, MyGdxGame game,float x,float y) {
        super(screen.getAtlas().findRegion("myWizard"));
        this.world = world;
        this.screen = screen;
        this.game = game;
        menu=new Texture("menu_pause.png");
        menusprite=new Sprite(menu);
        footstep= Gdx.audio.newMusic(Gdx.files.internal("sound/herofootstep.mp3"));
        currentState = State.SANDING;
        previousState = State.SANDING;
        timer = 0;
        runningRight = true;
        shadowballEffect= Gdx.audio.newMusic(Gdx.files.internal("sound/shadowballEffect.wav"));
        Array<TextureRegion> frames = new Array<TextureRegion>();

        for (int i = 4; i < 7; i++)
            frames.add(new TextureRegion(getTexture(), i * 32, 0, 32, 32));
        heroRun = new Animation(0.1f, frames);
        frames.clear();
        heroAttack = new TextureRegion(getTexture(), 224, 0, 32, 32);
        defineHero(x,y);
        heroStand = new TextureRegion(getTexture(), 128, 0, 32, 32);
        setBounds(body.getPosition().x, body.getPosition().y, 32 / game.ppm, 32 / game.ppm);
        setRegion(heroStand);
        movement = new Vector2(0, 0);
        temp = new Vector3();
        resume = new Texture("resume.png");
        resumesprite = new Sprite(resume);
        resumesprite.setSize(100, 100);
        resumesprite.setPosition(((Gdx.graphics.getWidth()- resumesprite.getWidth())/2), (Gdx.graphics.getWidth()- resumesprite.getHeight())/2);
        exit = new Texture("Exit.png");
        exitsprite = new Sprite(exit);
        exitsprite.setSize(100, 100);
        menusprite.setBounds(((Gdx.graphics.getWidth()- exitsprite.getWidth())/2),(Gdx.graphics.getWidth()- exitsprite.getHeight())/2-100,100,100);
        exitsprite.setPosition(((Gdx.graphics.getWidth()- exitsprite.getWidth())/2), (Gdx.graphics.getWidth()- exitsprite.getHeight())/2-200);
        fsavepoint = new Texture("1.png");
        fsavepointsprite = new Sprite(fsavepoint);
        fsavepointsprite.setSize(100, 100);
        fsavepointsprite.setPosition(0, (Gdx.graphics.getWidth()- fsavepointsprite.getHeight())/2-100);
        ssavepoint = new Texture("2.png");
        ssavepointsprite = new Sprite(ssavepoint);
        ssavepointsprite.setSize(100, 100);
        ssavepointsprite.setPosition(150, (Gdx.graphics.getWidth()- ssavepointsprite.getHeight())/2-100);
        tsavepoint = new Texture("3.png");
        tsavepointsprite = new Sprite(tsavepoint);
        fosavepoint = new Texture("4.png");
        fosavepointsprite = new Sprite(fosavepoint);
        tsavepointsprite.setBounds(300, (Gdx.graphics.getWidth()- ssavepointsprite.getHeight())/2-100,100,100);
        fosavepointsprite.setBounds(450,(Gdx.graphics.getWidth()- ssavepointsprite.getHeight())/2-100,100,100);
        Gdx.input.setInputProcessor(this);
    }

    public void setOnLadder(boolean onLadder) {
        this.onLadder = onLadder;

    }

    public void load(int health,float x,float y) {
        this.health=health;
        body.setTransform(x,y,0);
    }

    public void setChest(Chest chest) {
        this.chest = chest;
    }

    public boolean isSaveButtonPressed() {
        return saveButtonPressed;
    }
    public State getState() {
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            return State.ATTACKING;
        } else if (input.isKeyPressed(Input.Keys.LEFT) || input.isKeyPressed(Input.Keys.RIGHT)|| input.isKeyPressed(Input.Keys.D)|| input.isKeyPressed(Input.Keys.A)) {
            footstep.play();
            return State.RUNNING;
        }else if(health<=0){
            footstep.stop();
            return State.DEAD;
        } else {
            footstep.stop();
            return State.SANDING;
        }
    }

    public static int getHealth() {
        return health;
    }



    public void setcanjump(boolean x) {
        canjump = x;
    }


    public static void damage(int i) {
        System.out.println(health);
        health =health- i;
        System.out.println(health);

    }


    MainGameScreen screen;

    public void update(float dt) {
       if(currentState== State.DEAD){
           screen.music.stop();
           screen.game.setScreen(new GameOverScreen(game,"GAME OVER"));
           health=6;
           currentState=State.SANDING;
       }

        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2 + 0.06f);
        setRegion(getFrame(dt));
       if(onstairs){

           movement.y=movement.x>0?2:0;
       }else if(!jumped&&!onLadder){
           movement.y=0;
       }else
        if (TimeUtils.timeSinceNanos(timer) > 200000000 && jumped) {
            movement.y = 0;
            jumped = false;
            timer = TimeUtils.nanoTime();
        }
        body.setLinearVelocity(movement);
       if(!onLadder) {
           body.setGravityScale(-world.getGravity().y);
       }if (attacked) {
            for (ShadowBall s : screen.shadowBalls
                    ) {
                screen.batch.begin();
                s.draw(screen.batch);
                screen.batch.end();
                s.update();
            }
        }
        if(health>6){
            health=6;
        }


    }

    private TextureRegion getFrame(float dt) {
        currentState = getState();
        TextureRegion region;
        switch (currentState) {
            case RUNNING:
                region = (TextureRegion) heroRun.getKeyFrame(floattimer, true);
                break;
            case ATTACKING:
                region = heroAttack;
                break;
            case JUMPING:
                // region=(TextureRegion) heroJump.getKeyFrame(timer);
                // break;
            case SANDING:
            case DEAD:
            default:
                region = heroStand;
                break;
        }
        if ((movement.x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        }

        //if hero is running right and the texture isnt facing right... flip it.

        else if ((movement.x > 0 || runningRight) && region.isFlipX()) {

            region.flip(true, false);

            runningRight = true;

        }
        floattimer = currentState == previousState ? floattimer + dt : 0;
        previousState = currentState;

        return region;
    }


    private void defineHero(float x,float y) {
//hero
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        bodyDef.position.set(x, y);
        body = world.createBody(bodyDef);
        CircleShape shape = new CircleShape();
        shape.setRadius(10f / MyGdxGame.ppm);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits=MyGdxGame.HERO_BIT;
        fixtureDef.filter.maskBits=MyGdxGame.DEFAULT_BIT|MyGdxGame.ENEMY_BIT|MyGdxGame.PROJECTILE_BIT|MyGdxGame.SENSORBIT;
        fixtureDef.density = 11f / game.ppm;
        body.createFixture(fixtureDef).setUserData(this);

        shape.dispose();
        //endof hero

    }

    public void drawMyhero(SpriteBatch batch, OrthographicCamera camera) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        draw(batch);
        batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.RIGHT||keycode == Input.Keys.D) {
            movement.x = 3f;
            currentState = Myhero.State.RUNNING;
            runningRight = true;
        } else if (keycode == Input.Keys.LEFT||keycode == Input.Keys.A) {
            movement.x = -3f;
            currentState = Myhero.State.RUNNING;
            runningRight = false;
        }
        if((keycode== Input.Keys.UP||keycode == Input.Keys.W)&&onLadder){
movement.y=2.5f;
        }else if(keycode== Input.Keys.DOWN||keycode == Input.Keys.S&&onLadder){
            movement.y=0.5f;
        }else if(keycode== Input.Keys.S||keycode == Input.Keys.S&&onLadder){

        } else if(keycode== Input.Keys.ESCAPE){
            screen.setpaused(screen.getIsPaused()?false:true);

        } else if(keycode== Input.Keys.T&&onsavepoint){
            screen.setpaused(screen.getIsPaused()?false:true);
            saveButtonPressed=saveButtonPressed?false:true;
        }

        if (keycode == Input.Keys.E && TimeUtils.timeSinceNanos(attackTimer) > 900000000) {
            shadowballEffect.stop();
            shadowballEffect.play();
            screen.shadowBalls.add(new ShadowBall(screen, body.getPosition(),runningRight));
            attacked = true;
            attackTimer = TimeUtils.nanoTime();
        } if (keycode == Input.Keys.SPACE && !jumped && canjump) {
            System.out.println("jumped");
            timer = TimeUtils.nanoTime();
            jumped = true;
            movement.y = 4;

        }

        if (Input.Keys.SHIFT_LEFT == keycode) {
            movement.x *= 2;
        }
        if(isonChest &&keycode== Input.Keys.F){
      if(!chest.isopen()) {
          givehealth(chest.setOpen(true));

      }
      }

        return true;
    }

    public void givehealth(int i) {
    health+=i;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.LEFT|| keycode == Input.Keys.D||keycode == Input.Keys.A) {
            movement.x = 0;

        }
        if (keycode == Input.Keys.SHIFT_LEFT) {
            movement.x /= 2;
        }if((keycode== Input.Keys.DOWN||keycode== Input.Keys.UP||keycode == Input.Keys.W||keycode == Input.Keys.S)&&onLadder){
            movement.y=1.607f;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
if(screenX==Gdx.graphics.getWidth()/2&&screenY==Gdx.graphics.getHeight()/2+250){

}

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
       try {


        temp.set(screenX, screenY, 0);
        screen.hud.stage.getCamera().unproject(temp);
        if (resumesprite.getBoundingRectangle().contains(temp.x, temp.y)) {

            screen.setpaused(false);

        }else if(exitsprite.getBoundingRectangle().contains(temp.x,temp.y)&&screen.getIsPaused()){
            Gdx.app.exit();
        }else if(menusprite.getBoundingRectangle().contains(temp.x,temp.y)&&screen.getIsPaused()){
            game.setScreen(new Menu(game));

        }else if(fsavepointsprite.getBoundingRectangle().contains(temp.x,temp.y)&&isSaveButtonPressed()){
            if(MyGdxGame.connectDatabase.selectbyId(1).isClosed()){
                MyGdxGame.connectDatabase.insert(1,body.getPosition().x,body.getPosition().y,health);
            }else {

                MyGdxGame.connectDatabase.update(1,health,body.getPosition().x,body.getPosition().y);
            }
        }else if(ssavepointsprite.getBoundingRectangle().contains(temp.x,temp.y)&&isSaveButtonPressed()){
            if( MyGdxGame.connectDatabase.selectbyId(2).isClosed()){
                MyGdxGame.connectDatabase.insert(2,body.getPosition().x,body.getPosition().y,health);
            }else {
                MyGdxGame.connectDatabase.update(2,health,body.getPosition().x,body.getPosition().y);
            }
        }else if(tsavepointsprite.getBoundingRectangle().contains(temp.x,temp.y)&&isSaveButtonPressed()){
            if( MyGdxGame.connectDatabase.selectbyId(3).isClosed()){
                MyGdxGame.connectDatabase.insert(3,body.getPosition().x,body.getPosition().y,health);
            }else {
                MyGdxGame.connectDatabase.update(3,health,body.getPosition().x,body.getPosition().y);
            }
        }else if(fosavepointsprite.getBoundingRectangle().contains(temp.x,temp.y)&&isSaveButtonPressed()){
            if(MyGdxGame.connectDatabase.selectbyId(4).isClosed()){
                MyGdxGame.connectDatabase.insert(4,body.getPosition().x,body.getPosition().y,health);
            }else {
                MyGdxGame.connectDatabase.update(4,health,body.getPosition().x,body.getPosition().y);
            }
        }}catch (SQLException ex){
           System.out.println(ex.getMessage());
       }
        return true;
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
    public void setIsonChest(boolean isonChest) {
        this.isonChest = isonChest;
    }

}
