package network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import GameGui.GameManager;
import GameGui.Screen.LoadingScreen;
import network.packet.LoadPacket;
import videogame.GameConfig;

public class MultiplayerLobby implements Screen
{
	private GameManager game;
	private SpriteBatch spriteBatch;
	private Texture background;
	private MultiplayerScreen multiplayerScreen;
	
	public static boolean ready = false;
	public static LoadPacket loadPacket;
	
	public MultiplayerLobby(GameManager _game, String ip, int port)
	{
		this.game = _game;
		spriteBatch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("texture/multiplayer_background.png"));
        multiplayerScreen = new MultiplayerScreen(game, ip, port);
       
        if(GameConfig.server != null)
        	new ServerLaunchThread().start();
	}

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(1, 1, 1, 1);	
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0);
        spriteBatch.end();
        
        if(ready)
        	game.setScreen(new LoadingScreen(game, multiplayerScreen));
	}

	public void loadMap()
	{
		game.mapGenerator.active = true;
	}
	
	@Override
	public void show() { Gdx.input.setInputProcessor(null);}

	@Override
	public void resize(int width, int height) 
	{
		game.options.putInteger("screen_width", width);
		game.options.putInteger("screen_height", height);
		game.options.flush();
		
		GameConfig.Screen_Height = height;
		GameConfig.Screen_Width = width;
	}

	@Override
	public void pause() { }

	@Override
	public void resume() { }

	@Override
	public void hide() { }

	@Override
	public void dispose() {	}

}
