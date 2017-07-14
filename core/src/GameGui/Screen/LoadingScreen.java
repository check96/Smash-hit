package GameGui.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import GameGui.GameManager;
import videogame.GameConfig;

public class LoadingScreen implements Screen
{
	private GameManager game;
	private SpriteBatch spriteBatch;
	private Texture background;
	private Texture loadingBar; 
	
	private float progress;
	
	public LoadingScreen(GameManager _game)
	{
		GameConfig.menuSoundtrack.stop();

		game = _game;
		spriteBatch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("texture/background.png"));
        
        loadingBar = new Texture(Gdx.files.internal("loading_bar/bate_0.png"));
		
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

        progress = MathUtils.lerp(progress, game.mapGenerator.assets.manager.getProgress(), 0.1f);
        
//        System.out.println((int)(progress*10));
        loadingBar = new Texture(Gdx.files.internal("loading_bar/bate_"+(int)(progress*11)+".png"));
        
        spriteBatch.begin();
        spriteBatch.draw(background,0,0);
        spriteBatch.draw(loadingBar, 250, 0);
        spriteBatch.end();
        
        if (game.mapGenerator.assets.manager.update() && progress >= game.mapGenerator.assets.manager.getProgress() - 0.001f) 
        {
        	game.setScreen(new GameScreen(game));
        }
    }

    @Override
    public void resize(int width, int height)
    {
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
    public void dispose() { }
}