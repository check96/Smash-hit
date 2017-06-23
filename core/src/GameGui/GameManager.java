package GameGui;

import com.badlogic.gdx.Game;

public class GameManager extends Game
{

	@Override
	public void create()
	{	
		this.setScreen(new StartScreen(this));
	}
}
