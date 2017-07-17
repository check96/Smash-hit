package GameGui;

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
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import GameGui.Screen.LoadingScreen;
import videogame.GameConfig;

public class Shop implements Screen
{
	private GameManager game;
	private Stage stage;
	private Skin skin;
	private Skin mySkin;
	
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
	
	private Button start;
	private Button back;
	private Button bomb1;
	private Button bomb2;
	private Button doubleCoins;
	private Button mace;
	private Button vortex;
	private Button time;

	private TextField bomb1Field;
	private TextField bomb2Field;
	
	private SpriteBatch spriteBatch;
	private Texture background;
	
	public Shop(GameManager game)
	{
		this.game = game;
		stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
		
		spriteBatch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("texture/shop_background.png"));
        
        mySkin = new Skin(Gdx.files.internal("skin/holo/skin/Holo-dark-hdpi.json"));
        skin = new Skin(Gdx.files.internal("skin/comic/skin/comic-ui.json"));

        bomb1Field = new TextField("", mySkin);
        bomb1Field.setSize(70, 50);
        bomb1Field.setPosition(495, 380);
        
        bomb2Field = new TextField("", mySkin);
        bomb2Field.setSize(70, 50);
        bomb2Field.setPosition(755, 380);
        
        stage.addActor(bomb1Field);
        stage.addActor(bomb2Field);
		createButtons();
        createLabels(); 
	}
	
	private void createLabels()
	{
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/comic.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter(); 
    	
        parameter.size = 30;
    	BitmapFont fontBuyCoin = generator.generateFont(parameter);
    	Color color = Color.WHITE;
    	
    	parameter.size = 20;
    	BitmapFont fontUpgradeCoin = generator.generateFont(parameter);


    	float dim = 10;
    	
    	coins = new Label("0", new Label.LabelStyle(fontBuyCoin, color));
    	coins.setText(String.format("%01d", GameConfig.COINS));
    	coins.setSize(dim, dim);
    	coins.setPosition(GameConfig.Screen_Width*410/GameConfig.width, GameConfig.Screen_Height*530/GameConfig.height);
    	

    	doubleCoinsLabel = new Label(Integer.toString(doubleCoinsPrice), new Label.LabelStyle(fontBuyCoin, color));
		doubleCoinsLabel.setSize(dim, dim);
		doubleCoinsLabel.setPosition(GameConfig.Screen_Width*220/GameConfig.width, GameConfig.Screen_Height*355/GameConfig.height);
		stage.addActor(doubleCoinsLabel);
		
		
		bomb1Label = new Label(Integer.toString(bomb1Price), new Label.LabelStyle(fontBuyCoin, color));
		bomb1Label.setSize(dim, dim);
		bomb1Label.setPosition(GameConfig.Screen_Width*490/GameConfig.width, GameConfig.Screen_Height*355/GameConfig.height);
		stage.addActor(bomb1Label);	
		
		
		bomb2Label = new Label(Integer.toString(bomb2Price), new Label.LabelStyle(fontBuyCoin, color));
		bomb2Label.setSize(dim, dim);
		bomb2Label.setPosition(GameConfig.Screen_Width*750/GameConfig.width, GameConfig.Screen_Height*355/GameConfig.height);
		stage.addActor(bomb2Label);
		
		
		tornadoUpgradeLabel = new Label(Integer.toString(tornadoUpgradePrice), new Label.LabelStyle(fontUpgradeCoin, color));
		tornadoUpgradeLabel.setSize(dim,dim);
		tornadoUpgradeLabel.setPosition(GameConfig.Screen_Width*230/GameConfig.width, GameConfig.Screen_Height*92/GameConfig.height);
		stage.addActor(tornadoUpgradeLabel);
		
		
		clockUpgradeLabel = new Label(Integer.toString(clockUpgradePrice), new Label.LabelStyle(fontUpgradeCoin, color));
		clockUpgradeLabel.setSize(dim, dim);
		clockUpgradeLabel.setPosition(GameConfig.Screen_Width*495/GameConfig.width, GameConfig.Screen_Height*92/GameConfig.height);
		stage.addActor(clockUpgradeLabel);
		
		
		maceUpgradeLabel = new Label(Integer.toString(maceUpgradePrice), new Label.LabelStyle(fontUpgradeCoin, color));
		maceUpgradeLabel.setSize(dim,dim);
		maceUpgradeLabel.setPosition(GameConfig.Screen_Width*765/GameConfig.width, GameConfig.Screen_Height*92/GameConfig.height); 
		stage.addActor(maceUpgradeLabel);
		
		
		stage.addActor(coins);
	}

	private void createButtons()
	{
		back = new TextButton("BACK",skin);
		back.setSize(150,60);
        back.setPosition(GameConfig.Screen_Width*680/GameConfig.width, GameConfig.Screen_Height*500/GameConfig.height);
		back.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
            	game.setScreen(game.startScreen);
	            return true;
            }
        });
		
    	start = new TextButton("START",skin);
    	start.setSize(150,60);
        start.setPosition(GameConfig.Screen_Width*850/GameConfig.width, GameConfig.Screen_Height*500/GameConfig.height);
    	start.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
            	game.setScreen(new LoadingScreen(game));
	            return true;
            }
        });
    	
    	FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/comic.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter(); 
    	
        parameter.size = 25;
    	BitmapFont fontBuy = generator.generateFont(parameter);
    	TextButton.TextButtonStyle buyStyle = new TextButton.TextButtonStyle();
    	buyStyle.font = fontBuy;
    	
    	parameter.size = 20;
    	BitmapFont fontUpgrade = generator.generateFont(parameter);
    	TextButton.TextButtonStyle upgradeStyle = new TextButton.TextButtonStyle();
    	upgradeStyle.font = fontUpgrade;
    	
    	doubleCoins = new TextButton("buy", buyStyle);
    	doubleCoins.setSize(50,30);
    	doubleCoins.setPosition(GameConfig.Screen_Width*130/GameConfig.width, GameConfig.Screen_Height*300/GameConfig.height);
    	doubleCoins.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
            	if(GameConfig.coinsMultiplier == 1 && GameConfig.COINS >= doubleCoinsPrice)
            	{
            		GameConfig.coinsMultiplier = 2;
            		GameConfig.COINS -= doubleCoinsPrice;
            		coins.setText(String.format("%01d", GameConfig.COINS));
            	}
	            return true;
            	
            }
        });
    	stage.addActor(doubleCoins);
    	
    	bomb1 = new TextButton("buy", buyStyle);
    	bomb1.setSize(50,30);
    	bomb1.setPosition(GameConfig.Screen_Width*395/GameConfig.width, GameConfig.Screen_Height*300/GameConfig.height);
    	bomb1.addListener(new InputListener(){
    		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {	
    			int number = 0;
    			if(!bomb1Field.getText().equals(""))
    				number = Integer.parseInt(bomb1Field.getText());
            	
            	if(number*bomb1Price <= GameConfig.COINS )
            	{
            		GameConfig.numBomb1 += number;
            		GameConfig.COINS -= number*bomb1Price;
            		coins.setText(String.format("%01d", GameConfig.COINS));
            		bomb1Field.setText("");
            	}
            	
	            return true;
            }
        });
    	stage.addActor(bomb1);
    	
    	bomb2 = new TextButton("buy", buyStyle);
    	bomb2.setSize(50,30);
    	bomb2.setPosition(GameConfig.Screen_Width*665/GameConfig.width, GameConfig.Screen_Height*300/GameConfig.height);
    	bomb2.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
            	int number = 0;
            	if(!bomb2Field.getText().equals(""))
    				number = Integer.parseInt(bomb2Field.getText());
            	
            	if(number*bomb1Price <= GameConfig.COINS )
            	{
            		GameConfig.numBomb2 += number;
            		GameConfig.COINS -= number*bomb2Price;
            		coins.setText(String.format("%01d", GameConfig.COINS));
            		bomb2Field.setText("");
            	}
            	
	            return true;
            }
        });
    	stage.addActor(bomb2);
    	

    	
    	mace = new TextButton("upgrade", upgradeStyle);
    	mace.setSize(50,30);
    	mace.setPosition(GameConfig.Screen_Width*665/GameConfig.width, GameConfig.Screen_Height*40/GameConfig.height);
    	mace.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
            	if(maceUpgradePrice <= GameConfig.COINS)
            	{
            		maceUpgradePrice *=2;
            		GameConfig.maceLevel++;
            		GameConfig.COINS -= maceUpgradePrice;
            		coins.setText(String.format("%01d", GameConfig.COINS));
            	}
	            return true;
            }
        });
    	stage.addActor(mace);
    	
    	time = new TextButton("upgrade", upgradeStyle);
    	time.setSize(50,30);
    	time.setPosition(GameConfig.Screen_Width*400/GameConfig.width, GameConfig.Screen_Height*40/GameConfig.height);
    	time.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
            	if(clockUpgradePrice <= GameConfig.COINS)
            	{
            		clockUpgradePrice *=2;
            		GameConfig.clockLevel++;
            		GameConfig.COINS -= clockUpgradePrice;
            		coins.setText(String.format("%01d", GameConfig.COINS));
            	}
	            return true;
            }
        });
    	stage.addActor(time);
    	
    	vortex = new TextButton("upgrade", upgradeStyle);
    	vortex.setSize(50,30);
    	vortex.setPosition(GameConfig.Screen_Width*135/GameConfig.width, GameConfig.Screen_Height*40/GameConfig.height);
    	vortex.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
            	if(tornadoUpgradePrice <= GameConfig.COINS)
            	{
            		tornadoUpgradePrice *=2;
            		GameConfig.vortexLevel++;
            		GameConfig.COINS -= tornadoUpgradePrice;
            		coins.setText(String.format("%01d", GameConfig.COINS));
            	}
	            return true;
            }
        });
    	stage.addActor(vortex);
    	
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


        maceUpgradeLabel.setText(String.format("%01d", maceUpgradePrice * maceUpgradeLevel));
        tornadoUpgradeLabel.setText(String.format("%01d", tornadoUpgradePrice * tornadoUpgradeLevel));
        clockUpgradeLabel.setText(String.format("%01d", clockUpgradePrice * clockUpgradeLevel));
        
        stage.act();
        stage.draw();
	}

	@Override
	public void resize(int width, int height)
	{
		stage.getViewport().update(width, height,true);
		
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