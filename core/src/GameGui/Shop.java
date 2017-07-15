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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import GameGui.Screen.LoadingScreen;
import videogame.GameConfig;

public class Shop implements Screen
{
	private GameManager game;
	private Stage stage;
	private Skin skin;
	
	private int numBomb1 = 0;
	private int numBomb2 = 0;
	private int spentMoney = 1000;

	private int maceUpgradeLevel = 2;
	private int tornadoUpgradeLevel = 6;
	private int clockUpgradeLevel = 10;

	
	private int bomb1Price = 100;
	private int bomb2Price = 300;
	private int doubleCoinsPrice = 500;
	private int maceUpgradePrice = 500;
	private int tornadoUpgradePrice = 1000;
	private int clockUpgradePrice = 500;
	
	private Label bomb1Label;
	private Label bomb2Label;
	private Label doubleCoinsLabel;
	private Label maceUpgradeLabel;
	private Label tornadoUpgradeLabel;
	private Label clockUpgradeLabel;
	
	private Label coins;
	private Label money;
	private Label bomb1;
	private Label bomb2;
	
	private Button start;
	private Button back;
	private Button buy;
	private ImageButton plusBomb1;
	private ImageButton minusBomb1;
	private ImageButton plusBomb2;
	private ImageButton minusBomb2;
	
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
    	
    	bomb1 = new Label("0", new Label.LabelStyle(font, color));
        bomb1.setPosition(500, 340);
 		bomb1.setSize(40, 80);
 		
 		bomb2 = new Label("0", new Label.LabelStyle(font, color));
     	bomb2.setPosition(750, 340);
 		bomb2.setSize(40, 80);
 		
 		stage.addActor(bomb1);
        stage.addActor(bomb2);
    	
    	coins = new Label("0", new Label.LabelStyle(font, color));
    	coins.setText(String.format("%01d", GameConfig.COINS));
    	coins.setSize(dim, dim);
    	coins.setPosition(410, 530);
    	
    	money = new Label("0", new Label.LabelStyle(font, color));
    	money.setSize(dim, dim);
    	money.setPosition(410, 490); 

		doubleCoinsLabel = new Label(Integer.toString(doubleCoinsPrice), new Label.LabelStyle(font, color));
		doubleCoinsLabel.setSize(dim, dim);
		doubleCoinsLabel.setPosition(120, 305);
		stage.addActor(doubleCoinsLabel);		
		
		bomb1Label = new Label(Integer.toString(bomb1Price), new Label.LabelStyle(font, color));
		bomb1Label.setSize(dim, dim);
		bomb1Label.setPosition(390, 305);
		stage.addActor(bomb1Label);		
		
		bomb2Label = new Label(Integer.toString(bomb2Price), new Label.LabelStyle(font, color));
		bomb2Label.setSize(dim, dim);
		bomb2Label.setPosition(650, 305);
		stage.addActor(bomb2Label);
		
		tornadoUpgradeLabel = new Label(Integer.toString(tornadoUpgradePrice), new Label.LabelStyle(font, color));
		tornadoUpgradeLabel.setSize(dim,dim);
		tornadoUpgradeLabel.setPosition(100, 45);
		stage.addActor(tornadoUpgradeLabel);
		
		clockUpgradeLabel = new Label(Integer.toString(clockUpgradePrice), new Label.LabelStyle(font, color));
		clockUpgradeLabel.setSize(dim, dim);
		clockUpgradeLabel.setPosition(365, 45);
		stage.addActor(clockUpgradeLabel);
		
		maceUpgradeLabel = new Label(Integer.toString(maceUpgradePrice), new Label.LabelStyle(font, color));
		maceUpgradeLabel.setSize(dim,dim);
		maceUpgradeLabel.setPosition(635, 45); 
		stage.addActor(maceUpgradeLabel);
		
		stage.addActor(money);
		stage.addActor(coins);
	}

	private void createButtons()
	{
		float dim = 80;
		
		plusBomb1 = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("skin/comic/raw/plus.png")))));
		plusBomb1.addListener(new ClickListener() {              
            public void clicked(InputEvent event, float x, float y)
            {
            	numBomb1++;
            }
        });
		plusBomb1.setSize(dim,dim);
		plusBomb1.setPosition(470, 375);
		stage.addActor(plusBomb1);
		
		minusBomb1 = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("skin/comic/raw/minus.png")))));
		minusBomb1.addListener(new ClickListener() {              
            public void clicked(InputEvent event, float x, float y)
            {
            	if(numBomb1 > 0)
            		numBomb1--;
            }
        });
		minusBomb1.setSize(dim,dim);
		minusBomb1.setPosition(470, 305);
		
		stage.addActor(minusBomb1);
		
		plusBomb2 = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("skin/comic/raw/plus.png")))));
		plusBomb2.setSize(dim,dim);
		plusBomb2.setPosition(730, 375);
        plusBomb2.addListener(new ClickListener() {              
            public void clicked(InputEvent event, float x, float y)
            {
                numBomb2++;
            }
        });
        
        stage.addActor(plusBomb2);
        
		minusBomb2 = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("skin/comic/raw/minus.png")))));
		minusBomb2.setSize(dim,dim);
		minusBomb2.setPosition(730, 305);
        minusBomb2.addListener(new ClickListener() {              
            public void clicked(InputEvent event, float x, float y)
            {
            	if(numBomb2 > 0)
            		numBomb2--;
            }
        });
        stage.addActor(minusBomb2);
        
		back = new TextButton("BACK",skin);
		back.setSize(150,60);
        back.setPosition(680, 500);
		back.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
            	game.setScreen(game.startScreen);
	            return true;
            }
        });
		
    	start = new TextButton("START",skin);
    	start.setSize(150,60);
        start.setPosition(850, 500);
    	start.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
            	game.setScreen(new LoadingScreen(game));
	            return true;
            }
        });
        
    	
    	
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
		
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0);
        spriteBatch.end();

        stage.act();
        bomb1.setText(String.format("%01d", numBomb1));
        bomb2.setText(String.format("%01d", numBomb2));
        
        maceUpgradeLabel.setText(String.format("%01d", maceUpgradePrice * maceUpgradeLevel));
        tornadoUpgradeLabel.setText(String.format("%01d", tornadoUpgradePrice * tornadoUpgradeLevel));
        clockUpgradeLabel.setText(String.format("%01d", clockUpgradePrice * clockUpgradeLevel));
        
        
        spentMoney = 0;
        spentMoney += numBomb1 * bomb1Price + numBomb2 * bomb2Price;
       
        money.setText(String.format("%01d", spentMoney));
        
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