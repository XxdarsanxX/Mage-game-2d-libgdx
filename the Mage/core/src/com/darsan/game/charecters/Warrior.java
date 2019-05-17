package com.darsan.game.charecters;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.darsan.game.Game.MainGameScreen;
import com.darsan.game.Game.MyGdxGame;

public class Warrior extends Enemy {
public int health=3;
private Fixture fixtureright;
private Fixture fixtureleft;
    private  Animation warriorRun;
    private Animation warriorAttack;
    private  TextureRegion warriorStand;
    public boolean destroyed;
    public boolean right;
    public boolean left;
    private Vector2 movment;
    private Fixture fixturemain;
    private State currentState;
    private float floattimer;
    private boolean runningRight;
    private State previousState;
private Music footstep;
    private boolean isAtacking;
    private float attackTimer;


    public Warrior(MainGameScreen screen, float x, float y,String region) {
        super(screen, x, y,region);
        currentState = Warrior.State.SANDING;
        previousState = Warrior.State.SANDING;
        Array<TextureRegion> frames=new Array<TextureRegion>();
runningRight=true;
footstep= Gdx.audio.newMusic(Gdx.files.internal("sound/herofootstep.mp3"));
      for(int i=1;i<3;i++)
            frames.add(new TextureRegion(getTexture(),i*32,0,32,32));
       warriorRun=new Animation(0.1f,frames);
        frames.clear();
        frames.add(new TextureRegion(getTexture(),1*32,0,32,32));
        frames.add(new TextureRegion(getTexture(),3*32,0,32,32));
        warriorAttack=new Animation(0.2f,frames);
        frames.clear();
     movment=new Vector2();
        warriorStand=new TextureRegion(getTexture(),0,0,32,32);
        setBounds(getX(),getY(),32/screen.game.ppm,32/screen.game.ppm);
    }

    public void setAtacking(boolean atacking) {
        isAtacking = atacking;
    }


    public enum State { Dieing, SANDING, RUNNING, ATTACKING}

    @Override
    protected void defineEnemy() {
        Filter filter=new Filter();
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX(),getY());
        bodyDef.fixedRotation=true;
        body = world.createBody(bodyDef);
        CircleShape shape = new CircleShape();
        shape.setRadius(10/MyGdxGame.ppm);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density=1f/screen.game.ppm;
      fixturemain=  body.createFixture(fixtureDef);
        shape.setRadius(0.5f);
        shape.setPosition(new Vector2(1,0));
        fixtureDef.isSensor=true;
        fixtureDef.shape=shape;

      fixtureright=  body.createFixture(fixtureDef);

        shape.setRadius(0.5f);
        shape.setPosition(new Vector2(-1,0));
        fixtureDef.isSensor=true;
        fixtureDef.shape=shape;

 fixtureleft= body.createFixture(fixtureDef);
fixtureright.setUserData(this);
fixtureleft.setUserData(this);
filter.categoryBits=3;
fixtureright.setFilterData(filter);
filter.categoryBits=5;
fixtureleft.setFilterData(filter);
fixturemain.setUserData(this);
filter.categoryBits=MyGdxGame.ENEMY_BIT;
filter.maskBits=MyGdxGame.DEFAULT_BIT|MyGdxGame.HERO_BIT|MyGdxGame.PROJECTILE_BIT;
fixturemain.setFilterData(filter);

        shape.dispose();
    }

    @Override
    public void update(float dt) {

if(!destroyed) {
    setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2 + 0.05f);
    setRegion(getFrame(dt));
    if (right) {
        movment.x = 0.15f;

    }
    if (left) {
        movment.x = -0.15f;
    }
    body.applyForceToCenter(movment, true);
}
if(health<=0&&!destroyed){
    destroyed=true;
    footstep.stop();
    world.destroyBody(body);
    body=null;
}
if(isAtacking){
    attackTimer-=dt;
    if(attackTimer<0){
        Myhero.damage(1);
        attackTimer+=1f;
    }
}else {
    attackTimer=0;
}
    }



    @Override
    public void hitByHero() {
body.applyForceToCenter(.02f,0,true);
--health;
    }

    @Override
    public void hitedHero() {

    }
    public void draw(SpriteBatch batch){
        if(!destroyed){
            super.draw(batch);
        }
    }
    private TextureRegion getFrame(float dt) {
        currentState =  getState();
        TextureRegion region;
        switch (currentState) {
            case RUNNING:

                footstep.play();
                region = (TextureRegion) warriorRun.getKeyFrame(floattimer, true);
                break;
            case ATTACKING:
                region = (TextureRegion) warriorAttack.getKeyFrame(floattimer,true);
                hitedHero();
                break;
            default:
                footstep.stop();
                region = warriorStand;
                break;
        }
        if ((movment.x< 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        }  else if ((movment.x> 0 || runningRight) && region.isFlipX()) {

            region.flip(true, false);

            runningRight = true;

        }
        floattimer = currentState == previousState ? floattimer + dt : 0;
        previousState = currentState;
        return region;

    }

    private State getState() {
        if (isAtacking) {
            return Warrior.State.ATTACKING;
        } else {
        }
        if (movment.x!=0) {
            return Warrior.State.RUNNING;
        } else {
            return Warrior.State.SANDING;
        }

    }

    }
