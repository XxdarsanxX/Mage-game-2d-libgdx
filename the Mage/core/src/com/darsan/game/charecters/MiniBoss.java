package com.darsan.game.charecters;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;


import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.darsan.game.Game.MainGameScreen;
import com.darsan.game.Game.MyGdxGame;
import com.darsan.game.spells.GreenBall;
import com.darsan.game.worldobjects.Barrier;
import com.darsan.game.worldobjects.MovingObject;

import java.util.Random;

public class MiniBoss extends Enemy {
    private final MovingObject objectsec;
    private final Barrier barrier;
    private boolean destroyed;
 private State previousState;
 private State currentState;
    private TextureRegion miniBossStand;
 private int health;
    private boolean hitground;
private float speed;
    private float countDown;
    private int randomNumber;
    private Animation miniBossAttackingStraight;
    private float floattimer;
    private Animation miniBossAttackingTop;
    private float fireDelay;
private MovingObject object;
    private float firedelaytime;

    public enum State { DEAD, STANDING,ATTACKINGSTRAIGHT,ATTACKINGTOP}
    public MiniBoss(MainGameScreen screen, float x, float y, String region, Barrier barrier) {
        super(screen, x, y, region);
        this.barrier=barrier;
        firedelaytime=.6f;
        Array<TextureRegion> frames=new Array<TextureRegion>();
        for(int i=11;i<13;i++)
            frames.add(new TextureRegion(getTexture(),i*32,0,32,32));
        miniBossAttackingStraight=new Animation(1,frames);
        frames.clear();
        frames.add(new TextureRegion(getTexture(),352,0,32,32));
        frames.add(new TextureRegion(getTexture(),416,0,32,32));
        miniBossAttackingTop=new Animation(1,frames);

     currentState = State.STANDING;
     previousState = State.STANDING;

        miniBossStand=new TextureRegion(getTexture(),352,0,32,32);
     setBounds(getX(),getY(),32/screen.game.ppm,32/screen.game.ppm);
    health=6;
    speed=.8f;
        MapObject mapObject=screen.getMap().getLayers().get(12).getObjects().get(0);
        Rectangle rect = ((RectangleMapObject) mapObject).getRectangle();

object=new MovingObject(screen,new Vector2(rect.x/MyGdxGame.ppm,rect.y/MyGdxGame.ppm));
         mapObject=screen.getMap().getLayers().get(12).getObjects().get(1);
         rect = ((RectangleMapObject) mapObject).getRectangle();
        objectsec=new MovingObject(screen,new Vector2(rect.x/MyGdxGame.ppm,rect.y/MyGdxGame.ppm));
    }

    @Override
    public void update(float dt) {

         if(currentState==State.DEAD){

            if(!destroyed){
                destroyed=true;
                world.destroyBody(body);
                body=null;
                barrier.destroyBody();
            }
        }else {
             setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2 + 0.06f);
             setRegion(getFrame(dt));
             body.setLinearVelocity(0, hitground ? -speed : speed);
             object.update(dt);
             objectsec.update(dt);

             fireDelay -= dt;
             if (currentState == State.ATTACKINGSTRAIGHT && fireDelay < 0 && !isDestroyed()) {
                 fireDelay += firedelaytime;
                 screen.greenBalls.add(new GreenBall(screen, body.getPosition(), true, this));
             } else if (currentState == State.ATTACKINGTOP && fireDelay < 0) {
                 fireDelay += firedelaytime;
                 screen.greenBalls.add(new GreenBall(screen, object.getbodyPostion(), false, this));
                 screen.greenBalls.add(new GreenBall(screen, objectsec.getbodyPostion(), false, this));
             }
         }
    }



    @Override
    protected void defineEnemy() {
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
        fixtureDef.filter.maskBits=MyGdxGame.DEFAULT_BIT|MyGdxGame.HERO_BIT|MyGdxGame.PROJECTILE_BIT;
        fixtureDef.filter.categoryBits=MyGdxGame.ENEMY_BIT;
  Fixture fixture=body.createFixture(fixtureDef);
fixture.setUserData(this);
        shape.dispose();


    }


public boolean isHitground(){
        return hitground;
}
    private TextureRegion getFrame(float dt) {
        currentState = getState(dt);
        TextureRegion region=null;

        switch (currentState) {
            case STANDING:
            case DEAD:
                region=miniBossStand;
                break;
            case ATTACKINGSTRAIGHT:
                region= (TextureRegion)miniBossAttackingStraight.getKeyFrame(floattimer,true);
                break;
            case ATTACKINGTOP:
                region= (TextureRegion)miniBossAttackingTop.getKeyFrame(floattimer,true);
        }
        floattimer = currentState == previousState ? floattimer + dt : 0;
        previousState = currentState;
        return region;
    }
    public State getState(float dt) {
        if(!(health>0)){
            return State.DEAD;
        } else {
            countDown-=dt;
            if (countDown <= 0) {
                Random random = new Random();
                randomNumber= random.nextInt(2) ;
                countDown+=10;
                fireDelay=0;
            }
            switch (randomNumber){
                case 0:

                    return State.ATTACKINGSTRAIGHT;
                case 1:

                    return State.ATTACKINGTOP;


            }

        }
    return null;}

    @Override
    public void hitByHero() {
        health--;
    }

    @Override
    public void hitedHero() {

    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setHitground(boolean hitground) {
        this.hitground = hitground;
    }


    @Override
    public void draw(Batch batch) {
        if(!isDestroyed()) {
            super.draw(batch);
        }
        }


}
