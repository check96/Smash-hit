package GameGui.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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
	private Label destroyedDesks;
	private Label destroyedChairs;
	private Label destroyedDoors;
	private Label destroyedPlants;
	private Label destroyedLockers;
	private Label destroyedObjects;
	private Label score;
	private Label coins;
	private SpriteBatch money;
	private SpriteBatch spriteBatch;
	private Texture background;
	private Texture moneyTexture;
	public GameOverScreen(GameManager _game)
	{
		game = _game;
		stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
		
		GameConfig.coinsMultiplier = 1;
		
		money = new SpriteBatch();
		spriteBatch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("texture/game_over_background.png"));
        moneyTexture = new Texture(Gdx.files.internal("Icons/money.png"));
		GameConfig.COINS += GameConfig.LOCAL_COINS;

		game.options.putInteger("coins", GameConfig.COINS);
		game.options.flush();
		
		Skin skin = new Skin(Gdx.files.internal("skin/comic/skin/comic-ui.json"));
		
		quit = new TextButton("QUIT", skin);
		quit.setSize(200, 80);
		quit.setPosition(GameConfig.Screen_Width*280/GameConfig.width, GameConfig.Screen_Height*50/GameConfig.height);
		quit.addListener(new InputListener(){
      		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
      			GameConfig.reset();
      			SoundManager.gameSoundtrack.stop();
      			SoundManager.menuSoundtrack.play();
            	game.setScreen(game.startScreen);
	            return true;
            }
        });
		
		retry = new TextButton("RETRY", skin);
		retry.setSize(200, 80);
		retry.setPosition(GameConfig.Screen_Width*520/GameConfig.width, GameConfig.Screen_Height*50/GameConfig.height);
		retry.addListener(new InputListener(){
      		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
      			GameConfig.reset();
      			SoundManager.gameSoundtrack.stop();
            	game.setScreen(new Shop(game));
	            return true;
            }
        });
		Table table = new Table();
		table.center();
        table.setFillParent(true);
        
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/comic.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

		parameter.size = 25;
		BitmapFont font = generator.generateFont(parameter);
		Color color = Color.WHITE;

		coins = new Label(""+GameConfig.COINS, new Label.LabelStyle(font, color));
		coins.setPosition(200, 360);
		
		int highscore = game.options.getInteger("hishscore", 0);
		
		if(GameConfig.SCORE > highscore)
		{
			score = new Label("NEW Highscore: "+ GameConfig.SCORE, new Label.LabelStyle(font, color));
			
			game.options.putInteger("highscore", GameConfig.SCORE);
			game.options.flush();
		}
		else
			score = new Label("Score: "+ GameConfig.SCORE, new Label.LabelStyle(font, color));
		
		
		destroyedDesks = new Label("Desks Destroyed: "+ GameConfig.destroyedDesks, new Label.LabelStyle(font, color));
        destroyedChairs = new Label("Chairs Destroyed: "+ GameConfig.destroyedChairs, new Label.LabelStyle(font, color));
        destroyedDoors = new Label("Doors Destroyed: "+ GameConfig.destroyedDoors, new Label.LabelStyle(font, color));
        destroyedPlants = new Label("Plants Destroyed: "+ GameConfig.destroyedPlants, new Label.LabelStyle(font, color));
        destroyedLockers = new Label("Lockers Destroyed: "+ GameConfig.destroyedLockers, new Label.LabelStyle(font, color));
        destroyedObjects = new Label("Total Objects Destroyed: "+ GameConfig.destroyedObjects, new Label.LabelStyle(font, color));
        
        table.add(score).expandX();
        table.row();
        table.add(destroyedDesks).expandX();
        table.row();
        table.add(destroyedChairs).expandX();
        table.row();
        table.add(destroyedDoors).expandX();
        table.row();
    	table.add(destroyedPlants).expandX();
    	table.row();
    	table.add(destroyedLockers).expandX();
    	table.row();
    	table.add(destroyedObjects).expandX();
    	table.row();

    	stage.addActor(coins);
    	stage.addActor(table);
		stage.addActor(retry);
		stage.addActor(quit);
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
        
        money.begin();
        money.draw(moneyTexture,260,350);
        money.end();
        
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
