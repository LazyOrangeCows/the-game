package se.doverfelt.thegame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import se.doverfelt.thegame.net.Client;
import se.doverfelt.thegame.server.ServerManager;
import se.doverfelt.thegame.Screens.LoadingScreen;

import java.util.ArrayList;

public class TheGame extends Game {
	LoadingScreen load;
	ArrayList<Screen> screens;

	public Client client;
	public ServerManager serverManager;
	public InputHandler inputHandler;
	
	@Override
	public void create () {
		serverManager = new ServerManager(true);
		client = new Client("localhost", 30916);
		inputHandler = new InputHandler(this);

		screens = new ArrayList<Screen>();
		load = new LoadingScreen();
		screens.add(load);
		setScreen(load);
	}

	@Override
	public void render () {
		for (Screen s:screens) {
			s.render(Gdx.graphics.getDeltaTime());
		}



	}
	
	@Override
	public void dispose () {
		//batch.dispose();
		//img.dispose();
	}
}
