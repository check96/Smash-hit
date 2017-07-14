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
	public Preferences scores;
	public Preferences editorLevels;
	
	@Override
	public void create()
	{			
		options = Gdx.app.getPreferences("options");
		scores = Gdx.app.getPreferences("Scores");
		editorLevels = Gdx.app.getPreferences("levels");
		
		GameConfig.Screen_Width = options.getInteger("screen_width",1024);
		GameConfig.Screen_Height = options.getInteger("screen_height",600);
		GameConfig.volume = options.getFloat("volume",1);
		
		GameConfig.menuSoundtrack.setVolume(GameConfig.volume);
		GameConfig.gameSoundtrack.setVolume(GameConfig.volume);
		
		mapGenerator = new MapGenerator(this);
		startScreen = new StartScreen(this);
		
		this.setScreen(startScreen);
//		this.setScreen(new Shop(this));		
	}
}
