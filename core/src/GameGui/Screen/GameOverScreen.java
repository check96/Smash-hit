package GameGui.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import GameGui.GameManager;
import GameGui.Shop;
import GameGui.SoundManager;
import videogame.GameConfig;

public class GameOverScreen implements Screen 
{
	private GameManager game;
    private Stage stage;
    private TextButton quit;
	private TextButton retry;
	 
	private SpriteBatch spriteBatch;
	private Texture background;
	
	public GameOverScreen(GameManager _game)
	{
		game = _game;
		stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
		GameConfig.reset();
		GameConfig.coinsMultiplier = 1;
		
		spriteBatch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("texture/game_over_background.png"));
		
		GameConfig.COINS += GameConfig.LOCAL_COINS;

		game.options.putInteger("coins", GameConfig.COINS);
		game.options.flush();
		
		Skin skin = new Skin(Gdx.files.internal("skin/comic/skin/comic-ui.json"));
		
		quit = new TextButton("QUIT", skin);
		quit.setSize(200, 80);
		quit.setPosition(GameConfig.Screen_Width*350/GameConfig.width, GameConfig.Screen_Height*150/GameConfig.height);
		quit.addListener(new InputListener(){
      		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
      			SoundManager.gameSoundtrack.stop();
      			SoundManager.menuSoundtrack.play();
            	game.setScreen(game.startScreen);
	            return true;
            }
        });
		
		retry = new TextButton("RETRY", skin);
		retry.setSize(200, 80);
		retry.setPosition(GameConfig.Screen_Width*100/GameConfig.width, GameConfig.Screen_Height*150/GameConfig.height);
		retry.addListener(new InputListener(){
      		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
      			
      			SoundManager.gameSoundtrack.stop();
            	game.setScreen(new Shop(game));
	            return true;
            }
        });
		Table table = new Table();
        table.center();
        table.setFillParent(true);
        
		table.add(quit).expandX().padTop(1);
		table.add(retry).expandX().padTop(1);
		
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
