package se.doverfelt.thegame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import se.doverfelt.thegame.Screens.MapTestScreen;
import se.doverfelt.thegame.Screens.CScreen;
import se.doverfelt.thegame.Screens.MainMenu;
import se.doverfelt.thegame.server.ServerWorld;
import se.doverfelt.thegame.server.ServerManager;
import se.doverfelt.thegame.net.Client;
import se.doverfelt.thegame.server.ServerManager;
import se.doverfelt.thegame.Screens.LoadingScreen;
import java.util.ArrayList;
import java.util.HashMap;

public class TheGame extends Game {

	public HashMap<String, Screen> screens;

	public Assets assets;
	SpriteBatch batch;

	public Client client;
	public ServerManager serverManager;
	
	@Override
	public void create () {
		//Initiation variables
		assets = new Assets();
		screens = new HashMap<String, Screen>();
		LoadingScreen load = new LoadingScreen(this);
		MainMenu main = new MainMenu(this);
		batch = new SpriteBatch();

		screens.put("load", load);
		screens.put("main", main);
		setScreen(load);

		new ServerWorld();

		serverManager = new ServerManager(true);
		client = new Client("localhost", 30916);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (screen != null&& (screen instanceof CScreen)){
			((CScreen)screen).render(Gdx.graphics.getDeltaTime(), batch);
		}else {
			super.render();
		}


	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
