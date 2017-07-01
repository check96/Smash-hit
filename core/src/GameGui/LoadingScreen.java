package GameGui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class LoadingScreen implements Screen
{
	private GameManager game;
	private ShapeRenderer shapeRenderer;
	private OrthographicCamera camera;
	private Stage stage;
	
	private float progress;
	
	public LoadingScreen(GameManager _game)
	{
		game = _game;
		
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

		shapeRenderer = new ShapeRenderer();
	}

	@Override
	public void show()
	{
	    shapeRenderer.setProjectionMatrix(camera.combined);
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
        
        if (game.mapGenerator.assets.manager.update() && progress >= game.mapGenerator.assets.manager.getProgress() - 0.001f) 
        {
        	game.setScreen(new GameScreen(game));
        }
 
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(camera.viewportWidth/3 +10, camera.viewportHeight / 2 -8, camera.viewportWidth/3 - 64, 16);

        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(camera.viewportWidth/3 +10, camera.viewportHeight / 2 -8, progress * (camera.viewportWidth/3 - 64), 16);
       
        shapeRenderer.end();
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
        shapeRenderer.dispose();
    }
}