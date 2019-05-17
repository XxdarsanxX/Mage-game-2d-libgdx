package com.darsan.game.spells;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.darsan.game.Game.MainGameScreen;
import com.darsan.game.Game.MyGdxGame;
import com.darsan.game.charecters.MiniBoss;

public class GreenBall extends Sprite{
    private final World world;
    private Body body;
    private MainGameScreen screen;
    private boolean colided;
    private boolean destroyed;
    MiniBoss miniBoss;
    private Vector2 projectile;

    public GreenBall(MainGameScreen screen, Vector2 start, boolean isMiniboss,MiniBoss miniBoss){
        super(new Texture("greenball.png"));
        this.miniBoss=miniBoss;
        this.world=screen.getWorld();
        this.screen=screen;
        projectile=new Vector2(isMiniboss?1:0,isMiniboss?0:-1);
        defineBody(start);

    }

    private void defineBody(Vector2 xandy) {

        BodyDef bodyDef = new BodyDef();
        Fixture fixture;
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        xandy.x+=0.25f;
        xandy.y+=0.03f;

        bodyDef.position.set(xandy);
        bodyDef.bullet=true;
        body = world.createBody(bodyDef);
        CircleShape shape = new CircleShape();
        shape.setRadius(7 / screen.game.ppm);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits= MyGdxGame.PROJECTILE_BIT;
        fixtureDef.filter.maskBits=MyGdxGame.DEFAULT_BIT|MyGdxGame.HERO_BIT;
        fixture=body.createFixture(fixtureDef);
        fixture.setUserData(this);
        body.setLinearVelocity(projectile);
        body.setGravityScale(0);
        shape.dispose();
        setPosition(xandy.x,xandy.y);
        setSize(getWidth()/MyGdxGame.ppm,getHeight()/MyGdxGame.ppm);
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

                setPosition(body.getWorldCenter().x - 0.14f, body.getWorldCenter().y - 0.154f);

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
