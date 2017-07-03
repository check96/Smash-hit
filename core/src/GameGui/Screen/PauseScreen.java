package GameGui.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import GameGui.GameManager;
import videogame.GameConfig;

public class PauseScreen implements Screen
{
	private GameManager game;
	private GameScreen screen;
	private Stage stage;

	private boolean BACK = false;
	private boolean QUIT = false;
	private boolean FULLSCREEN = false;
	
	private Slider volume;
	private TextButton back;
	private TextButton quit;
	private CheckBox on_off;
	private Label musicLabel;
	private Label fullscreen;
	

	public PauseScreen(GameManager _game, GameScreen _screen)
	{
		this.game = _game;
		this.screen = _screen;		

		stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/comic.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter(); 
    	
        parameter.size = 30;
    	BitmapFont font = generator.generateFont(parameter);    
		Skin skin = new Skin(Gdx.files.internal("skin/comic/skin/comic-ui.json"));

		Table table = new Table(skin);
		table.center();
		table.setFillParent(true);
		
		volume = new Slider(0, 1, 0.1f, false, skin);
		volume.setValue(GameConfig.volume);
		volume.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				GameConfig.volume = volume.getValue();
				
            	GameConfig.menuSoundtrack.setVolume(GameConfig.volume);
            	GameConfig.gameSoundtrack.setVolume(GameConfig.volume);
            	
            	game.options.putFloat("volume", GameConfig.volume);
				game.options.flush();
			}
		});
		FULLSCREEN = game.options.getBoolean("fullscreen");
		table = new Table(skin);
        table.setFillParent(true);
        
		musicLabel = new Label("MUSIC", new Label.LabelStyle(font, Color.BLACK));
		
		on_off = new CheckBox("", skin);
		on_off.setChecked(FULLSCREEN);
		on_off.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
			{
				FULLSCREEN = !FULLSCREEN;
				
				if(FULLSCREEN)
					Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode()); 
				else	
					Gdx.graphics.setWindowedMode(1280, 640);
				
				game.options.putBoolean("fullscreen", FULLSCREEN);
				game.options.flush();
				
				return true;
			}
		});
		
		fullscreen = new Label("FULLSCREEN", new Label.LabelStyle(font, Color.BLACK));

		back = new TextButton("BACK", skin);
		back.addListener(new InputListener()
		{
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
	            BACK = true;
	            return true;
            }
        });
		quit = new TextButton("QUIT", skin);
		quit.addListener(new InputListener(){
      		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
	            QUIT = true;
	            return true;
            }
        });
		
		table.add(musicLabel).expandX();
		table.add(volume).expandX();
		table.row();
		table.add(fullscreen).expandX();
		table.add(on_off).expandX();
		table.row();
		table.add(back).expandX();
		table.add(quit).expandX();

		stage.addActor(table);
	}
	
	@Override
	public void show()
	{
		Gdx.input.setInputProcessor(stage);	
	}

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(1, 1, 1, 1);	
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	
		if(BACK || (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)))
		{
			this.dispose();
			
			synchronized(game.countdown)
			{
				game.countdown.pause = false;
				game.countdown.notify();
			}
			game.setScreen(screen);
		}
		
		if(QUIT)
		{
			this.dispose();
			game.setScreen(new GameOverScreen(game));
		}
		
		on_off.setChecked(FULLSCREEN);

		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height)
	{
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() { }

	@Override
	public void resume() { }

	@Override
	public void hide() { }

	@Override
	public void dispose()
	{
		stage.dispose();
	}

}