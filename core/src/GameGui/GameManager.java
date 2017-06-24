package GameGui;

import com.badlogic.gdx.Game;

import videogame.Countdown;
import videogame.MapGenerator;

public class GameManager extends Game
{
	public Countdown countdown = new Countdown();
	public MapGenerator mapGenerator = new MapGenerator();
	public StartScreen startScreen;
	
	@Override
	public void create()
	{	
		startScreen = new StartScreen(this);
		this.setScreen(startScreen);		
	}
}
