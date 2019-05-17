package com.darsan.game.Game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.darsan.game.tools.ConnectDatabase;
import com.darsan.game.tools.CreateDatabase;

public class MyGdxGame extends Game {
	public final static float ppm=100;
	public final static float V_WIDTH=570;
	public final static float V_HEIGHT=320;
    public static final short DEFAULT_BIT =1;
    public static final short HERO_BIT =4;
	public static final short ENEMY_BIT =16;
	public static final short PROJECTILE_BIT =32;
    public static final short SENSORBIT = 64;
    public static final CreateDatabase createDatabase =new CreateDatabase();
    public static final ConnectDatabase connectDatabase =new ConnectDatabase();
    public SpriteBatch batch;
    @Override
	public void create () {
        batch=new SpriteBatch();
        connectDatabase.createTable();
        setScreen(new Menu(this));
	}

	@Override
	public void render() {
super.render();
	}

}
