package com.darsan.game.charecters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.darsan.game.Game.MainGameScreen;
import com.darsan.game.Game.MyGdxGame;
import com.darsan.game.spells.Arrow;




public class Archer extends Enemy {
    private Animation archerAttack;
    private TextureRegion archerStand;
    private State currentState;
    public boolean destroyed;
    private Fixture mainFixture;
    public boolean isaimingRight;
    public boolean sawEnemy;
    private long startTime ;
    private float floattimer;
    private State previousState;
    private float fireDelay;
    private int health;
    private Music arrowEffect;

    public Archer(MainGameScreen screen, float x, float y, String region) {
        super(screen, x, y, region);
        arrowEffect= Gdx.audio.newMusic(Gdx.files.internal("sound/arrowEffect.ogg"));
        Array<TextureRegion> frames=new Array<TextureRegion>();
        for(int i=9;i<11;i++)
            frames.add(new TextureRegion(getTexture(),i*32,0,32,32));
        archerAttack=new Animation(0.6f,frames);
        archerStand=new TextureRegion(getTexture(),288,0,32,32);
        setBounds(getX(),getY(),32/screen.game.ppm,32/screen.game.ppm);

        currentState = State.SANDING;
        previousState = State.SANDING;

        health=3;
    }


    public enum State { DIEING, SANDING, ATTACKING}

    @Override
    protected void defineEnemy() {
        Fixture rightFixture;
        Fixture leftFixture;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        bodyDef.position.set(getX(), getY());
        body = world.createBody(bodyDef);
        CircleShape shape = new CircleShape();
        shape.setRadius(10f / MyGdxGame.ppm);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 11f / MyGdxGame.ppm;
       mainFixture= body.createFixture(fixtureDef);
        shape.setPosition(new Vector2(-1.8f,0));
        shape.setRadius(1.5f);
        fixtureDef.shape=shape;
        fixtureDef.isSensor=true;
        leftFixture=body.createFixture(fixtureDef);

        shape.setPosition(new Vector2(1.8f,0));
        shape.setRadius(1.5f);
        fixtureDef.shape=shape;
        fixtureDef.isSensor=true;
        rightFixture=body.createFixture(fixtureDef);
        shape.dispose();
        rightFixture.setUserData(this);
        leftFixture.setUserData(this);
        mainFixture.setUserData(this);
        Filter filter =new Filter();
        filter.categoryBits=3;
        rightFixture.setFilterData(filter);
        filter.categoryBits=5;
        leftFixture.setFilterData(filter);
        filter.categoryBits=MyGdxGame.ENEMY_BIT;
        mainFixture.setFilterData(filter);
        body.setActive(false);
    }

    @Override
    public void update(float dt) {
        fireDelay -= dt;
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2 + 0.05f);
       if(State.ATTACKING==currentState&&fireDelay < 0) {
               fireDelay += 2.5f;
shoot();

       }else if(currentState==State.DIEING){
           if(!destroyed){
               destroyed=true;
               world.destroyBody(body);
               body=null;
           }
       }
            setRegion(getFrame(dt));
        for (Arrow arrow:screen.arrows) {
arrow.update();
        }
    }

    private void shoot() {
if(currentState==State.ATTACKING) {
    arrowEffect.stop();
    arrowEffect.play();
    screen.arrows.add(new Arrow(screen, body.getPosition(), isaimingRight, this));

}
}

    private TextureRegion getFrame(float dt) {
        currentState =  getState();
        TextureRegion region;
        switch (currentState){
     case ATTACKING:
        region= (TextureRegion) archerAttack.getKeyFrame(floattimer,true);
         break;
            case DIEING:
                region=archerStand;
                fireDelay=0;
                break;
            default:
                region = archerStand;
                fireDelay=0;
                break;

 }
 if(isaimingRight&&region.isFlipX()){
            region.flip(true,false);
 }else if(!isaimingRight&&!region.isFlipX()){
            region.flip(true,false);
 }
        floattimer = currentState == previousState ? floattimer + dt : 0;
        previousState = currentState;
 return region;
    }

        public State getState() {

             if(!(health<=0)){
                 if (sawEnemy) {
                     startTime = TimeUtils.nanoTime();
                     return State.ATTACKING;
                 } else if(!sawEnemy){
                     startTime=0;
                     return State.SANDING;
                 }

    }else {
                return State.DIEING;
            }
            return null;
        }




    @Override
    public void hitByHero() {
health--;
    }

    @Override
    public void hitedHero() {

    }

    @Override
    public void draw(Batch batch) {
        if(!destroyed) {
            super.draw(batch);
        }
    }
}
