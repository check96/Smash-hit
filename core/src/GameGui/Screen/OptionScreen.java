package GameGui.Screen;

import com.badlogic.gdx.Gdx;
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
import videogame.GameConfig;

public class OptionScreen implements Screen 
{	
	private GameManager game;
	private boolean BACK = false;
	private Stage stage;
	private Slider volume;
	private TextButton back;
	private Label musicLabel;
	private Label fullscreen;
	private Table table;
	private CheckBox on_off;
	private boolean FULLSCREEN = true;
	
	private SpriteBatch spriteBatch;
	private Texture background;
	
	public OptionScreen(GameManager _game)
	{
		this.game = _game;
		this.stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
		
//		FULLSCREEN = game.options.getBoolean("fullscreen");
		
		spriteBatch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("texture/options_background.png"));

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/comic.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter(); 
    	
        parameter.size = 30;
    	BitmapFont font = generator.generateFont(parameter);
    	
		Skin skin = new Skin(Gdx.files.internal("skin/comic/skin/comic-ui.json"));
		
		volume = new Slider(0, 1, 0.1f, false, skin);
		volume.setValue(GameConfig.volume);
		volume.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				GameConfig.volume = volume.getValue();
				
            	GameConfig.menuSoundtrack.setVolume(GameConfig.volume);
            	GameConfig.gameSoundtrack.setVolume(GameConfig.volume);
            	
//            	game.options.putFloat("volume", GameConfig.volume);
//				game.options.flush();
			}
		});
		
		table = new Table(skin);
        table.setFillParent(true);
        
		musicLabel = new Label("MUSIC", new Label.LabelStyle(font, Color.WHITE));
		
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
				
//				game.options.putBoolean("fullscreen", FULLSCREEN);
//				game.options.flush();
				
				return true;
			}
		});
		
		fullscreen = new Label("FULLSCREEN", new Label.LabelStyle(font, Color.WHITE));

		back = new TextButton("BACK",skin);
		back.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
	            BACK = true;
	            return true;
            }
        });
		back.setSize(200,80);
        back.setPosition(Gdx.graphics.getWidth()/3.5f, 100);
        
		table.add(musicLabel).expandX();
		table.add(volume).expandX();
		table.row();
		table.add(fullscreen).expandX();
		table.add(on_off).expandX();
		
		stage.addActor(table);
		stage.addActor(back);
	}
	
	@Override
	public void render(float delta)
	{		
		Gdx.gl.glClearColor(1, 1, 1, 1);	
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(BACK)
		{
			BACK = false;
			game.setScreen(game.startScreen);
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
		
//		game.options.putInteger("screen_width", width);
//		game.options.putInteger("screen_height", height);
//		game.options.flush();
		
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

	@Override
	public void show() 
	{
		Gdx.input.setInputProcessor(stage);
	}
}
