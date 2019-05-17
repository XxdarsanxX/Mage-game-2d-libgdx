package com.darsan.game.spells;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.darsan.game.Game.MainGameScreen;
import com.darsan.game.Game.MyGdxGame;
import com.darsan.game.charecters.Myhero;

public class ShadowBall  extends Sprite{
    private final World world;
    private Body body;
    private MainGameScreen screen;
    private boolean colided;
    private boolean destroyed;

    public ShadowBall(MainGameScreen screen, Vector2 start,boolean lookingright){
        super(new Texture("shadowBall.png"));
        this.world=screen.getWorld();
        this.screen=screen;
        defineBody(start,lookingright);
    }

    private void defineBody(Vector2 xandy,boolean lookingright) {

        BodyDef bodyDef = new BodyDef();
        Fixture fixture;
        bodyDef.type = BodyDef.BodyType.DynamicBody;
       xandy.x+=lookingright?0.2f:-0.2f;
       xandy.y+=0.03f;
        bodyDef.position.set(xandy);
        bodyDef.bullet=true;
        body = world.createBody(bodyDef);
        CircleShape shape = new CircleShape();
        shape.setRadius(7 / screen.game.ppm);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits= MyGdxGame.PROJECTILE_BIT;
        fixtureDef.filter.maskBits=MyGdxGame.DEFAULT_BIT|MyGdxGame.HERO_BIT|MyGdxGame.ENEMY_BIT;
        fixtureDef.density=0.001f;
        fixture=body.createFixture(fixtureDef);
        fixture.setUserData(this);
        body.setLinearVelocity(screen.hero.runningRight?new Vector2(2f,0):new Vector2(-2f,0));
        shape.dispose();
        setPosition(xandy.x,xandy.y);
        body.setGravityScale(0);
        setSize(0.15f,0.15f);body.setGravityScale(0);
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
               // archer.getX()<(hero.getX()+MyGdxGame.V_WIDTH/MyGdxGame.ppm)&&archer.getX()>hero.getX()-MyGdxGame.V_WIDTH/MyGdxGame.ppm
                if(!(getX()>screen.hero.getX()-(MyGdxGame.V_WIDTH-300)/MyGdxGame.ppm)||!(getX()<(screen.hero.getX()+(MyGdxGame.V_WIDTH-300)/MyGdxGame.ppm))){
                    System.out.println("destroyed");
                    this.setColided(true);
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


