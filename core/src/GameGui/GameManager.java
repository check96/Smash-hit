package GameGui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import videogame.Countdown;
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
		
		editorLevels.putString("levels", "");
		editorLevels.flush();
		
		mapGenerator = new MapGenerator(this);
		startScreen = new StartScreen(this);
		this.setScreen(startScreen);		
	}
}
