package GameGui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import GameGui.Screen.StartScreen;
import videogame.Countdown;
import videogame.GameConfig;
import videogame.MapGenerator;

public class GameManager extends Game
{
	public Countdown countdown = new Countdown();
	public MapGenerator mapGenerator;
	public StartScreen startScreen;
	public Preferences options;
	public Preferences editorLevels;
	
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
	
		mapGenerator = new MapGenerator(this);
		startScreen = new StartScreen(this);
		
		this.setScreen(startScreen);
	}
}
