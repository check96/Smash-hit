package GameGui;

import java.io.IOException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import GameGui.Screen.StartScreen;
import network.MultiplayerMapGenerator;
import network.Server;
import network.Screen.MultiplayerScreen;
import network.packets.LogoutPacket;
import network.threads.ClientThread;
import network.threads.ServerThread;
import videogame.Countdown;
import videogame.GameConfig;
import videogame.MapGenerator;

public class GameManager extends Game
{
	public Countdown countdown = new Countdown();
	public MapGenerator mapGenerator;
	public MultiplayerMapGenerator multiplayerMapGenerator;
	public StartScreen startScreen;
	public Preferences options;
	public Preferences editorLevels;
	
	@Override
	public void dispose()
	{
		SoundManager.gameSoundtrack.stop();
		SoundManager.menuSoundtrack.stop();
		
		if(GameConfig.isServer)
		{
			GameConfig.server.sendData(new LogoutPacket("server"));
			for (ServerThread client : Server.clients)
			{
				try
				{
					client.disconnect();
				} catch (IOException e)
				{
					System.out.println(e.getMessage());
				}
			}
			
			try
			{
				GameConfig.server.disconnect();
			}
			catch (IOException e)
			{
				System.out.println(e.getMessage());
			}
		}
		else if(GameConfig.MULTIPLAYER)
		{
			if(MultiplayerScreen.client instanceof ClientThread)
			{
				try
				{
					MultiplayerScreen.client.disconnect();
				} catch (IOException e)
				{
					System.out.println(e.getMessage());
				}
			}
		}
	}


	@Override
	public void create()
	{			
		options = Gdx.app.getPreferences("options");
		editorLevels = Gdx.app.getPreferences("levels");
		
		GameConfig.Screen_Width = options.getInteger("screen_width",1024);
		GameConfig.Screen_Height = options.getInteger("screen_height",600);
		SoundManager.musicVolume = options.getFloat("musicVolume",1);
		SoundManager.soundVolume = options.getFloat("soundVolume",1);
		
		SoundManager.menuSoundtrack.setVolume(SoundManager.musicVolume);
		SoundManager.gameSoundtrack.setVolume(SoundManager.musicVolume);

		multiplayerMapGenerator = new MultiplayerMapGenerator();
		mapGenerator = new MapGenerator(this);
		startScreen = new StartScreen(this);
		
		this.setScreen(startScreen);
	}
}
