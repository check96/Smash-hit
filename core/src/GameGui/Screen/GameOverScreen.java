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
import videogame.GameConfig;

public class GameOverScreen implements Screen 
{
	private GameManager game;
    private Stage stage;
    private TextButton quit;
	private TextButton retry;
	private boolean QUIT = false;
	private boolean RETRY = false;
	 
	private SpriteBatch spriteBatch;
	private Texture background;
	
	public GameOverScreen(GameManager _game)
	{
		game = _game;
		stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));

		spriteBatch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("texture/menu_background.png"));
		
        int coins = game.options.getInteger("coins");
		game.options.putInteger("coins", coins+GameConfig.COINS);
		game.options.flush();
		
		Skin skin = new Skin(Gdx.files.internal("skin/comic/skin/comic-ui.json"));
		
		quit = new TextButton("QUIT", skin);
		quit.setSize(200, 80);
		quit.setPosition(350, 150);
		quit.addListener(new InputListener(){
      		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
	            QUIT = true;
	            return true;
            }
        });
		
		retry = new TextButton("RETRY", skin);
		retry.setSize(200, 80);
		retry.setPosition(100, 150);
		retry.addListener(new InputListener(){
      		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
	            RETRY  = true;
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
    
        if(RETRY)
        {
        	RETRY = false;
        	this.dispose();
        	GameConfig.reset();
        	game.setScreen(new LoadingScreen(game));
        }
        
        if(QUIT)
        {
        	QUIT = false;
        	this.dispose();
        	game.setScreen(game.startScreen);
        }
       
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0);
        spriteBatch.end();
        
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) { }

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
