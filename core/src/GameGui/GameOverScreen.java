package GameGui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import videogame.GameConfig;

public class GameOverScreen implements Screen 
{
	private GameManager game;
    private Stage stage;
    private TextButton quit;
	private TextButton retry;
	private Label gameOver;
	private boolean QUIT = false;
	private boolean RETRY = false;
	
	public GameOverScreen(GameManager _game)
	{
		game = _game;
		stage = new Stage(new ScreenViewport());
		Skin skin = new Skin(Gdx.files.internal("skin/comic/skin/comic-ui.json"));
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/comic.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter(); 
    	
        parameter.size = 55;
    	BitmapFont font = generator.generateFont(parameter);
    	
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
        table.top();
        table.setFillParent(true);
        
		gameOver = new Label("GAME OVER", new Label.LabelStyle(font, Color.BLACK));
		
		table.add(gameOver).expandX().padTop(10);
		
		stage.addActor(table);
		stage.addActor(quit);
		stage.addActor(retry);
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
