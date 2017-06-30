package GameGui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import videogame.Countdown;
import videogame.MapGenerator;

public class GameManager extends Game
{
	public Countdown countdown = new Countdown();
	public MapGenerator mapGenerator = new MapGenerator();
	public StartScreen startScreen;
	public Preferences options;
	public Preferences scores;
	
	@Override
	public void create()
	{			

		options = Gdx.app.getPreferences("options");
		scores = Gdx.app.getPreferences("Scores");
		
		startScreen = new StartScreen(this);
		this.setScreen(startScreen);		
	}
}
