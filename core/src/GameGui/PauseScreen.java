package GameGui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import videogame.Countdown;

public class PauseScreen implements Screen
{
	private GameManager game;
	private GameScreen screen;
	private Stage stage;
	private TextButton back;
	private TextButton quit;
	private boolean BACK = false;
	private boolean QUIT = false;
	private Countdown countdown;
	
	public PauseScreen(GameManager _game, GameScreen _screen, Countdown _countdown)
	{
		this.game = _game;
		this.screen = _screen;
		
		this.countdown = _countdown;
		
		synchronized(countdown)
		{
			try
			{
//				countdown.notify();
				countdown.wait();
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		stage = new Stage(new ScreenViewport());
		
		Skin skin = new Skin(Gdx.files.internal("skin/comic/skin/comic-ui.json"));

		back = new TextButton("BACK", skin);
		back.setSize(200, 80);
		back.setPosition(250, 250);
		back.addListener(new InputListener()
		{
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
	            BACK = true;
	            return true;
            }
        });
		quit = new TextButton("QUIT", skin);
		quit.setSize(200, 80);
		quit.setPosition(250, 150);
		quit.addListener(new InputListener(){
      		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
	            QUIT = true;
	            return true;
            }
        });
		stage.addActor(back);
		stage.addActor(quit);
	}
	
	@Override
	public void show()
	{
		Gdx.input.setInputProcessor(stage);	
	}

	@Override
	public void render(float delta)
	{
		if(BACK || (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)))
		{
			this.dispose();
			
			synchronized(countdown)
			{
				countdown.notify();
			}
			game.setScreen(screen);
		}
		
		if(QUIT)
		{
			this.dispose();
			game.setScreen(new GameOverScreen(game));
		}
		
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose()
	{
		stage.dispose();
	}

}