package GameGui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import videogame.GameConfig;

public class OptionScreen implements Screen 
{	
	private GameManager game;
	public static boolean MUSIC = true;
	public static boolean BACK = false;
	private Stage stage;
	private ImageButton volume;
	private TextButton back;
	private Label musicLabel;
	private Label fullscreen;
	private Table table;
	private Slider slider;
	private ImageButton on_off;
	public boolean FULLSCREEN = true;
	
	public OptionScreen(GameManager _game)
	{
		this.game = _game;
		this.stage = new Stage(new ScreenViewport());
		
		FULLSCREEN = game.options.getBoolean("fullscreen");
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/comic.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter(); 
    	
        parameter.size = 30;
    	BitmapFont font = generator.generateFont(parameter);
    	
		Skin skin = new Skin(Gdx.files.internal("skin/comic/skin/comic-ui.json"));
		
		slider = new Slider(0, 1, 0.1f, false, skin);
		
		table = new Table(skin);
		table.center();
        table.setFillParent(true);
        
		musicLabel = new Label("MUSIC", new Label.LabelStyle(font, Color.BLACK));
		
		on_off = new ImageButton(skin);
		on_off.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
			{
				FULLSCREEN = !FULLSCREEN;
				
				game.options.putBoolean("fullscreen", FULLSCREEN);
				game.options.flush();
				
				return true;
			}
		});
		
		fullscreen = new Label("FULLSCREEN", new Label.LabelStyle(font, Color.BLACK));
		
		volume = new ImageButton(skin);
		volume.addListener(new InputListener()
		{
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
            	float volume = slider.getValue();
            	GameConfig.soundtrack.setVolume(volume);
            	
//            	game.preferences.putFloat("volume", volume);
//				game.preferences.flush();
            	
                return true;
            }
        });
    
        back = new TextButton("BACK",skin);
		back.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
	            BACK = true;
	            return true;
            }
        });
		back.setSize(400,0);
        back.setPosition(Gdx.graphics.getWidth()/3.5f, 100);
        
		table.add(musicLabel).expandX().padLeft(1);
		table.add(slider).expandX().padRight(1);
		table.row();
		table.add(fullscreen).expandX().padRight(1);
		table.add(on_off).expandX().padLeft(1);
		
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
        if(FULLSCREEN )
    		on_off.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("skin/comic/raw/checkbox-on.png"))));
    	else
    		on_off.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("skin/comic/raw/checkbox.png"))));
    
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

	@Override
	public void show() 
	{
		Gdx.input.setInputProcessor(stage);
		slider.setValue(GameConfig.volume);
	}
}
