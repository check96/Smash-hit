package GameGui.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import GameGui.SoundManager;
import network.MultiplayerScreen;
import videogame.GameConfig;

public class PauseScreen implements Screen
{
	private GameManager game;
	private GameScreen gameScreen;
	private MultiplayerScreen multiplayerScreen;
	private Stage stage;

	private boolean BACK = false;
	private boolean FULLSCREEN = false;
	
	private Slider musicVolume;
	private Slider soundVolume;
	private TextButton back;
	private TextButton quit;
	private CheckBox on_off;
	private Label musicLabel;
	private Label soundLabel;
	private Label fullscreen;

	private SpriteBatch spriteBatch;
	private Texture background;
	
	public PauseScreen(GameManager _game, MultiplayerScreen _screen)
	{
		this.game = _game;
		this.multiplayerScreen = _screen;
		init();
	}
	
	public PauseScreen(GameManager _game, GameScreen _screen)
	{
		this.game = _game;
		this.gameScreen = _screen;
		init();
	}
		
	public void init()
	{
		stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
		spriteBatch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("texture/pause_background.png"));
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/comic.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter(); 
    	
        parameter.size = 30;
    	BitmapFont font = generator.generateFont(parameter);   
    	
		Skin skin = new Skin(Gdx.files.internal("skin/comic/skin/comic-ui.json"));
		
		musicVolume = new Slider(0, 1, 0.1f, false, skin);
		musicVolume.setPosition(500, 250);
		musicVolume.setValue(SoundManager.musicVolume);
		musicVolume.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				SoundManager.musicVolume = musicVolume.getValue();
				
            	SoundManager.menuSoundtrack.setVolume(SoundManager.musicVolume);
            	SoundManager.gameSoundtrack.setVolume(SoundManager.musicVolume);
            	
            	game.options.putFloat("musicVolume", SoundManager.musicVolume);
				game.options.flush();
			}
		});
		
		soundVolume = new Slider(0, 1, 0.1f, false, skin);
		soundVolume.setPosition(500, 200);
		soundVolume.setValue(SoundManager.soundVolume);
		soundVolume.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				SoundManager.musicVolume = musicVolume.getValue();
				
            	game.options.putFloat("soundVolume", SoundManager.soundVolume);
				game.options.flush();
			}
		});

		
		FULLSCREEN = game.options.getBoolean("fullscreen",false);
		
        
        soundLabel = new Label("SOUND", new Label.LabelStyle(font, Color.WHITE));
        soundLabel.setPosition(320, 200);
		musicLabel = new Label("MUSIC", new Label.LabelStyle(font, Color.WHITE));
		musicLabel.setPosition(320, 250);
		
		on_off = new CheckBox("", skin);
		on_off.setPosition(570, 150);
		on_off.setChecked(FULLSCREEN);
		on_off.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
			{
				FULLSCREEN = !FULLSCREEN;
				
				if(FULLSCREEN)
					Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode()); 
				else	
					Gdx.graphics.setWindowedMode(GameConfig.width, GameConfig.height);
				
				game.options.putBoolean("fullscreen", FULLSCREEN);
				game.options.flush();
				
				return true;
			}
		});
		
		fullscreen = new Label("FULLSCREEN", new Label.LabelStyle(font, Color.WHITE));
		fullscreen.setPosition(320, 150);

		back = new TextButton("BACK", skin);
		back.setPosition(320, 50);
		back.addListener(new InputListener()
		{
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
	            BACK = true;
	            return true;
            }
        });
		
		quit = new TextButton("QUIT", skin);
		quit.setPosition(520, 50);
		quit.addListener(new InputListener(){
      		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
      			GameConfig.SCORE = 0;
      			GameConfig.LOCAL_COINS = 0;
      			game.setScreen(new GameOverScreen(game));
	            return true;
            }
        });

		stage.addActor(musicLabel);
		stage.addActor(musicVolume);
		stage.addActor(soundVolume);
		stage.addActor(soundLabel);
		stage.addActor(fullscreen);
		stage.addActor(on_off);
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
		Gdx.gl.glClearColor(1, 1, 1, 1);	
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	
		if(BACK || (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)))
		{
			this.dispose();
			
			synchronized(game.countdown)
			{
				game.countdown.active = true;
				game.countdown.notify();
			}
			if(gameScreen instanceof GameScreen)
				game.setScreen(gameScreen);
			else if(multiplayerScreen instanceof MultiplayerScreen)
				game.setScreen(multiplayerScreen);
		}
		
		on_off.setChecked(FULLSCREEN);

	    spriteBatch.begin();
        spriteBatch.draw(background, 0, 0);
        spriteBatch.end();
		
        stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height)
	{
		stage.getViewport().update(width, height, true);
		
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
	public void dispose()
	{
		stage.dispose();
	}
}