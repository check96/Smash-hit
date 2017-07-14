package GameGui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import GameGui.Screen.LoadingScreen;
import videogame.GameConfig;

public class Shop implements Screen
{
	private GameManager game;
	private Stage stage;
	private Skin skin;
	
	private boolean START = false;
	private boolean BACK = false;
	
	private Label bomb1Price;
	private Label bomb2Price;
	private Label doubleCoinsPrice;
	private Label maceUpgradePrice;
	private Label tornadoUpgradePrice;
	private Label clockUpgradePrice;
	private Label coins;
	
	private Button start;
	private Button back;
	private ImageButton plus;
	private ImageButton minus;
	
	private SpriteBatch spriteBatch;
	private Texture background;
	
	public Shop(GameManager game)
	{
		this.game = game;
		stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
		
		spriteBatch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("texture/shop_background.png"));
        
        skin = new Skin(Gdx.files.internal("skin/comic/skin/comic-ui.json"));

        createButtons();
        createLabels(); 
	}
	
	private void createLabels()
	{
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/comic.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter(); 
    	
        parameter.size = 30;
    	BitmapFont font = generator.generateFont(parameter);
    	Color color = Color.WHITE;

    	float dim = 5;
    	
    	coins = new Label("0", new Label.LabelStyle(font, color));
    	coins.setText(String.format("%01d", GameConfig.COINS));
    	coins.setSize(dim, dim);
    	coins.setPosition(410, 540);
//		coins.setPosition(350, 500); 

		doubleCoinsPrice = new Label("200", new Label.LabelStyle(font, color));
		doubleCoinsPrice.setSize(dim, dim);
		doubleCoinsPrice.setPosition(120, 305);
		createTable(doubleCoinsPrice);		
		
		
		bomb1Price = new Label("30", new Label.LabelStyle(font, color));
		bomb1Price.setSize(dim, dim);
		bomb1Price.setPosition(405, 300);
		createTable(bomb1Price);		
		
		bomb2Price = new Label("60", new Label.LabelStyle(font, color));
		bomb2Price.setSize(dim, dim);
		bomb2Price.setPosition(665, 305);
		createTable(bomb2Price);
		

		maceUpgradePrice = new Label("200", new Label.LabelStyle(font, color));
		maceUpgradePrice.setSize(dim,dim);
		maceUpgradePrice.setPosition(120, 45);
		createTable(maceUpgradePrice);
		
		tornadoUpgradePrice = new Label("150", new Label.LabelStyle(font, color));
		tornadoUpgradePrice.setSize(dim,dim);
		tornadoUpgradePrice.setPosition(390, 45);
		createTable(tornadoUpgradePrice);
		
		clockUpgradePrice = new Label("200", new Label.LabelStyle(font, color));
		clockUpgradePrice.setSize(dim, dim);
		clockUpgradePrice.setPosition(645, 45);
		createTable(clockUpgradePrice);
		
		stage.addActor(coins);
	}

	private void createTable(Label label)
	{
		float x = label.getX();
		float y = label.getY();
		
		stage.addActor(label);
		
		plus.setPosition(x-100, y);
		minus.setPosition(x-100, y);
		
		stage.addActor(plus);
		stage.addActor(minus);
	}

	private void createButtons()
	{
		plus = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("skin/comic/raw/plus.png")))));
		minus = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("skin/comic/raw/minus.png")))));
		
		back = new TextButton("BACK",skin);
		back.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
	            BACK = true;
	            return true;
            }
        });
		back.setSize(150,60);
        back.setPosition(680, 500);
    
    	start = new TextButton("START",skin);
		start.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
	            START = true;
	            return true;
            }
        });
		start.setSize(150,60);
        start.setPosition(850, 500);
        
        stage.addActor(back);
        stage.addActor(start);
	}

	@Override
	public void show() {Gdx.input.setInputProcessor(stage);}

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(1, 1, 1, 1);	
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
        if(START)
        {
        	START = false;
//        	this.dispose();
        	game.setScreen(new LoadingScreen(game));
        }
        if(BACK)
        {
        	BACK = false;
//        	this.dispose();
        	game.setScreen(game.startScreen);
        }
        
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

