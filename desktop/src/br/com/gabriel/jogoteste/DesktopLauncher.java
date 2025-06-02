package br.com.gabriel.jogoteste;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Jogo");
		config.setWindowIcon("logo.png");
		//config.setMaximized(true);
		config.setWindowedMode(JogoTeste.SCREEN_WIDTH,JogoTeste.SCREEN_HEIGHT);
		config.setResizable(true);
		config.useVsync(true);
		new Lwjgl3Application(JogoTeste.getInstance(), config);
	}
}
