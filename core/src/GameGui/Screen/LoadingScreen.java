package GameGui.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import GameGui.GameManager;

public class LoadingScreen implements Screen
{
	private GameManager game;
	private OrthographicCamera camera;
	private Stage stage;
	private SpriteBatch spriteBatch;
	private Texture background;
	
	private float progress;
	
	public LoadingScreen(GameManager _game)
	{
		game = _game;
		
		spriteBatch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("texture/menu_background.png"));

		camera = new OrthographicCamera();
        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),camera));
       
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/comic.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter(); 
    	
        parameter.size = 25;
    	BitmapFont font = generator.generateFont(parameter);
    	
        Label loading = new Label("LOADING...", new Label.LabelStyle(font, Color.BLACK));
        loading.setPosition(camera.viewportWidth/2.5f -6, camera.viewportHeight/2 +10);
        stage.addActor(loading);
		
        synchronized(game.mapGenerator)
		{
        	game.mapGenerator.pause = false;
        	game.mapGenerator.notify();
		}
	}

	@Override
	public void show()
	{
	    this.progress = 0f;
	}

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
        progress = MathUtils.lerp(progress, game.mapGenerator.assets.manager.getProgress(), 0.1f);
        
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0);
        spriteBatch.end();
        
        if (game.mapGenerator.assets.manager.update() && progress >= game.mapGenerator.assets.manager.getProgress() - 0.001f) 
        {
        	game.setScreen(new GameScreen(game));
        }
    }

    @Override
    public void resize(int width, int height)
    {
    	stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose()
    {
    	stage.dispose();
    }
}