package com.darsan.game.spells;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.darsan.game.Game.MainGameScreen;
import com.darsan.game.Game.MyGdxGame;
import com.darsan.game.charecters.Archer;

import java.io.File;

public class Arrow extends Sprite {
    private final World world;
    private Body body;
    private MainGameScreen screen;
    private boolean colided;
    private boolean destroyed;
Archer archer;

    public Arrow(MainGameScreen screen, Vector2 start, boolean lookingright,Archer archer){
        super(new Texture("Arrow.png"));
        this.archer=archer;
        this.world=screen.getWorld();
        this.screen=screen;
        defineBody(start,lookingright);
    }

    private void defineBody(Vector2 xandy,boolean lookingright) {

        BodyDef bodyDef = new BodyDef();
        Fixture fixture;
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        xandy.x+=lookingright?0.25f:-0.25f;
        xandy.y+=0.03f;

        bodyDef.position.set(xandy);
        bodyDef.bullet=true;
        body = world.createBody(bodyDef);
        PolygonShape shape=new PolygonShape();
        shape.setAsBox((getWidth()/5)/ MyGdxGame.ppm,(getHeight()/7)/MyGdxGame.ppm);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits= MyGdxGame.PROJECTILE_BIT;
        fixtureDef.filter.maskBits=MyGdxGame.DEFAULT_BIT|MyGdxGame.HERO_BIT;
        fixture=body.createFixture(fixtureDef);
        fixture.setUserData(this);
        body.setLinearVelocity(archer.isaimingRight?new Vector2(1f,0):new Vector2(-1f,0));
        body.setGravityScale(0);
        shape.dispose();
        setPosition(xandy.x,xandy.y);
        setSize(0.15f,0.15f);
    }

    public void update(){
        if(!destroyed&&colided) {
            if(!world.isLocked()) {
                world.destroyBody(body);
                destroyed = true;
                body=null;
            }
        }else {
            if(!destroyed) {

                setPosition(body.getWorldCenter().x - 0.08f, body.getWorldCenter().y - 0.07f);
                if(archer.isaimingRight &&isFlipX()){
                    flip(true,false);
                }else if(!archer.isaimingRight&&!isFlipX()){
                    flip(true,false);
                }
            }
        }
    }

    public void setColided(boolean colided) {
        this.colided = colided;
    }
    public void draw(SpriteBatch batch){
        if(!destroyed){
            super.draw(batch);
        }
    }


}
