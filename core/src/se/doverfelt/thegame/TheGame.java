package se.doverfelt.thegame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import se.doverfelt.thegame.Screens.LoadingScreen;

import java.util.ArrayList;

public class TheGame extends Game {
	LoadingScreen load;
	ArrayList<Screen> screens;

	@Override
	public void create () {
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
