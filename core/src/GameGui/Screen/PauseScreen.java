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
import videogame.GameConfig;

public class PauseScreen implements Screen
{
	private GameManager game;
	private GameScreen screen;
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
	

	public PauseScreen(GameManager _game, GameScreen _screen)
	{
		this.game = _game;
		this.screen = _screen;		

		stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
		spriteBatch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("texture/pause_background.png"));
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/comic.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter(); 
    	
        parameter.size = 30;
    	BitmapFont font = generator.generateFont(parameter);    
		Skin skin = new Skin(Gdx.files.internal("skin/comic/skin/comic-ui.json"));

		Table table = new Table(skin);
		table.center();
		table.setFillParent(true);
		
		musicVolume = new Slider(0, 1, 0.1f, false, skin);
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
		table = new Table(skin);
        table.setFillParent(true);
        
        soundLabel = new Label("SOUND", new Label.LabelStyle(font, Color.BLACK));
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
					Gdx.graphics.setWindowedMode(GameConfig.width, GameConfig.height);
				
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
      			game.setScreen(new GameOverScreen(game));
	            return true;
            }
        });
		
		table.add(musicLabel).expandX();
		table.add(musicVolume).expandX();
		table.row();
		table.add(soundLabel).expandX();
		table.add(soundVolume).expandX();
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