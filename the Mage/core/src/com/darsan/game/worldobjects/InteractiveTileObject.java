package com.darsan.game.worldobjects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.darsan.game.Game.MainGameScreen;
import com.darsan.game.Game.MyGdxGame;

public abstract class InteractiveTileObject {
    protected World world;
    protected Rectangle bounds;
    protected Body body;
    protected MainGameScreen screen;
    protected MapObject object;
    protected Fixture fixture;

    public InteractiveTileObject(MainGameScreen screen, MapObject object,boolean issensor,boolean dynamic){
        this.screen = screen;
        this.world = screen.getWorld();
BodyDef bdef=new BodyDef();
FixtureDef fdef =new FixtureDef();
        Shape shape = null;

            if (object instanceof RectangleMapObject) {
                shape = getRectangle((RectangleMapObject)object);
            }
        else if (object instanceof PolygonMapObject) {
            shape = getPolygon((PolygonMapObject)object);
        }
        else if (object instanceof PolylineMapObject) {
            shape = getPolyline((PolylineMapObject)object);
        }
        else if (object instanceof CircleMapObject) {
            shape = getCircle((CircleMapObject)object);
        }


            bdef.type = dynamic? BodyDef.BodyType.DynamicBody:BodyDef.BodyType.StaticBody;
        body = world.createBody(bdef);
        fdef.shape = shape;
        fdef.density=1;
        fdef.friction=3;
        fdef.isSensor=issensor;
        fdef.filter.categoryBits=MyGdxGame.DEFAULT_BIT;
       fixture =body.createFixture(fdef);
    }



    private static PolygonShape getRectangle(RectangleMapObject rectangleObject) {
        Rectangle rectangle = rectangleObject.getRectangle();
        PolygonShape polygon = new PolygonShape();
        Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) /MyGdxGame.ppm,
                (rectangle.y + rectangle.height * 0.5f ) / MyGdxGame.ppm);
        polygon.setAsBox(rectangle.width * 0.5f /MyGdxGame.ppm,
                rectangle.height * 0.5f /MyGdxGame.ppm,
                size,
                0.0f);
        return polygon;
    }

    private static CircleShape getCircle(CircleMapObject circleObject) {
        Circle circle = circleObject.getCircle();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(circle.radius / MyGdxGame.ppm);
        circleShape.setPosition(new Vector2(circle.x /MyGdxGame.ppm, circle.y /MyGdxGame.ppm));
        return circleShape;
    }

    private static PolygonShape getPolygon(PolygonMapObject polygonObject) {
        PolygonShape polygon = new PolygonShape();
        float[] vertices = polygonObject.getPolygon().getTransformedVertices();

        float[] worldVertices = new float[vertices.length];

        for (int i = 0; i < vertices.length; ++i) {
            System.out.println(vertices[i]);
            worldVertices[i] = vertices[i] /MyGdxGame.ppm;
        }

        polygon.set(worldVertices);
        return polygon;
    }

    private static ChainShape getPolyline(PolylineMapObject polylineObject) {
        float[] vertices = polylineObject.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < vertices.length / 2; ++i) {
            worldVertices[i] = new Vector2();
            worldVertices[i].x = vertices[i * 2] / MyGdxGame.ppm;
            worldVertices[i].y = vertices[i * 2 + 1] /MyGdxGame.ppm;
        }

        ChainShape chain = new ChainShape();
        chain.createChain(worldVertices);
        return chain;
    }
}
