package com.mahmoud.practice.outbreak.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mahmoud.practice.outbreak.core.OutbreakGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.resizable = false;
		config.height = 480;
		config.width = 480;
		new LwjglApplication(new OutbreakGame(), config);
	}
}
