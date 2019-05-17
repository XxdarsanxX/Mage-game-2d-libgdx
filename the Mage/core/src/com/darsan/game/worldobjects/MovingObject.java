package com.darsan.game.worldobjects;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.darsan.game.Game.MainGameScreen;
import com.darsan.game.Game.MyGdxGame;

public class MovingObject  {

    private boolean hitSensor;
private Body body;
    public MovingObject(MainGameScreen screen, Vector2 location) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        bodyDef.position.set(location.x,location.y);
        body = screen.getWorld().createBody(bodyDef);
        CircleShape shape = new CircleShape();
        shape.setRadius(10/MyGdxGame.ppm);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 11f / MyGdxGame.ppm;
        fixtureDef.filter.categoryBits=MyGdxGame.SENSORBIT;
        fixtureDef.filter.maskBits=MyGdxGame.DEFAULT_BIT;
        body.createFixture(fixtureDef).setUserData(this);
body.setGravityScale(0);
    }

    public void setHitSensor(boolean hitSensor) {
        this.hitSensor = hitSensor;
    }
    public Vector2 getbodyPostion(){
        return body.getPosition();

    }
   public void update(float dt){
        body.setLinearVelocity(hitSensor?1:-1,0);

    }

    public boolean gethitSensor() {
        return hitSensor;
    }
}
