package com.darsan.game.charecters;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.darsan.game.Game.MainGameScreen;
import com.darsan.game.Game.MyGdxGame;

public abstract class Enemy extends Sprite {


    protected World world;

    protected MainGameScreen screen;

    public Body body;

    public Vector2 velocity;


    public Enemy(MainGameScreen screen, float x, float y, String region) {
        super(screen.getAtlas().findRegion(region));
        this.world = screen.getWorld();

        this.screen = screen;

        setPosition(x, y);

        defineEnemy();

        velocity = new Vector2(-1, -2);

        body.setActive(true);

    }


    protected abstract void defineEnemy();

    public abstract void update(float dt);

    public abstract void hitByHero();

    public abstract void hitedHero();
}