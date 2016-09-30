package se.doverfelt.thegame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import se.doverfelt.thegame.net.Client;
import se.doverfelt.thegame.server.ServerManager;

public class TheGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

	public Client client;
	public ServerManager serverManager;
	public InputHandler inputHandler;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		serverManager = new ServerManager(true);
		client = new Client("localhost", 30916);
		inputHandler = new InputHandler(this);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
