package br.com.gabriel.chefboom;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
	public static void main(String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		config.setForegroundFPS(Config.FPS);
		config.setTitle(Config.GAME_TITLE);
		config.setWindowIcon(Config.GAME_ICON);

		if (Config.FULLSCREEN) {
			config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
		} else if (Config.MAXIMIZED){
			Graphics.DisplayMode displayMode = Lwjgl3ApplicationConfiguration.getDisplayMode();

			float aspectRatio = (float) Config.SCREEN_WIDTH / Config.SCREEN_HEIGHT;
			int height = displayMode.height;
			int width = (int) (height * aspectRatio);

			config.setWindowedMode(width, height);
			config.setDecorated(false); // Remove bordas para parecer fullscreen
		} else {
			config.setWindowedMode(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
		}

		config.setResizable(true);
		config.useVsync(true);
		new Lwjgl3Application(ChefBoom.getInstance(), config);
	}
}
