package com.darsan.game.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.darsan.game.Game.MyGdxGame;
import com.darsan.game.tools.ConnectDatabase;
import com.darsan.game.tools.CreateDatabase;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new MyGdxGame(), config);
		config.height= Gdx.graphics.getHeight();
		config.width=Gdx.graphics.getWidth();
		config.fullscreen = true;
	}
}
