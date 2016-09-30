package se.doverfelt.thegame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import se.doverfelt.thegame.Screens.CScreen;
import se.doverfelt.thegame.Screens.MainMenu;
import se.doverfelt.thegame.server.ServerManager;
import se.doverfelt.thegame.Screens.LoadingScreen;

import java.util.ArrayList;
import java.util.HashMap;

public class TheGame extends Game {

	public HashMap<String, Screen> screens;

	public Assets assets;
	SpriteBatch batch;



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


		ServerManager manager = new ServerManager(true);
	}

	@Override
	public void render () {


		if (screen != null&& (screen instanceof CScreen)){
			((CScreen)screen).render(Gdx.graphics.getDeltaTime(), batch);
		}else {
			super.render();
		}



	}
	
	@Override
	public void dispose () {
		//batch.dispose();
		//img.dispose();
	}
}
