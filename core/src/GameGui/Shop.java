package GameGui;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import entity.Weapon;
import videogame.GameConfig;

public class Shop implements Screen
{
	private GameManager game;
	private ArrayList<Weapon> items;
	private Stage stage;
	
	private SpriteBatch spriteBatch;
	private Texture background;
	
	public Shop(GameManager game)
	{
		this.game = game;
		items = new ArrayList<Weapon>();
		
		stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
		spriteBatch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("texture/shop_background.png"));
	}
	
	@Override
	public void show() { }

	@Override
	public void render(float delta)
	{
		spriteBatch.begin();
        spriteBatch.draw(background, 0, 0);

        spriteBatch.end();
        
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height)
	{
		stage.getViewport().update(width, height,true);
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

