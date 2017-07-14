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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
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
	
	private boolean START = false;
	private boolean BACK = false;
	
	private int numBomb1 = 0;
	private int numBomb2 = 0;
	
	private Label bomb1Price;
	private Label bomb2Price;
	private Label doubleCoinsPrice;
	private Label maceUpgradePrice;
	private Label tornadoUpgradePrice;
	private Label clockUpgradePrice;
	private Label coins;
	private Label bomb1;
	private Label bomb2;
	
	private Button start;
	private Button back;
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
    	coins.setPosition(410, 540);
//		coins.setPosition(350, 500);  per soldi spesi

		doubleCoinsPrice = new Label("200", new Label.LabelStyle(font, color));
		doubleCoinsPrice.setSize(dim, dim);
		doubleCoinsPrice.setPosition(120, 310);
		stage.addActor(doubleCoinsPrice);		
		
		bomb1Price = new Label("30", new Label.LabelStyle(font, color));
		bomb1Price.setSize(dim, dim);
		bomb1Price.setPosition(405, 310);
		stage.addActor(bomb1Price);		
		
		bomb2Price = new Label("60", new Label.LabelStyle(font, color));
		bomb2Price.setSize(dim, dim);
		bomb2Price.setPosition(665, 310);
		stage.addActor(bomb2Price);
		
		maceUpgradePrice = new Label("200", new Label.LabelStyle(font, color));
		maceUpgradePrice.setSize(dim,dim);
		maceUpgradePrice.setPosition(120, 45);
		stage.addActor(maceUpgradePrice);
		
		tornadoUpgradePrice = new Label("150", new Label.LabelStyle(font, color));
		tornadoUpgradePrice.setSize(dim,dim);
		tornadoUpgradePrice.setPosition(390, 45);
		stage.addActor(tornadoUpgradePrice);
		
		clockUpgradePrice = new Label("200", new Label.LabelStyle(font, color));
		clockUpgradePrice.setSize(dim, dim);
		clockUpgradePrice.setPosition(645, 45);
		stage.addActor(clockUpgradePrice);
		
		stage.addActor(coins);
	}

	private void createButtons()
	{
		float dim = 50;
		
		plusBomb1 = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("skin/comic/raw/plus.png")))));
		plusBomb1.setSize(dim,dim);
		plusBomb1.setPosition(490, 390);
		plusBomb1.addListener(new ClickListener() {              
            public void clicked(InputEvent event, float x, float y)
            {
            	numBomb1++;
            }
        });
		
		minusBomb1 = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("skin/comic/raw/minus.png")))));
		minusBomb1.setSize(dim,dim);
		minusBomb1.setPosition(490, 320);
		minusBomb1.addListener(new ClickListener() {              
            public void clicked(InputEvent event, float x, float y)
            {
            	if(numBomb1 > 0)
            		numBomb1--;
            }
        });
		
		plusBomb2 = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("skin/comic/raw/plus.png")))));
		plusBomb2.setSize(dim,dim);
		plusBomb2.setPosition(740, 390);
        plusBomb2.addListener(new ClickListener() {              
            public void clicked(InputEvent event, float x, float y)
            {
                numBomb2++;
            }
        });
        
		minusBomb2 = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("skin/comic/raw/minus.png")))));
		minusBomb2.setSize(dim,dim);
		minusBomb2.setPosition(740, 320);
        minusBomb2.addListener(new ClickListener() {              
            public void clicked(InputEvent event, float x, float y)
            {
            	if(numBomb2 > 0)
            		numBomb2--;
            }
        });
        
		back = new TextButton("BACK",skin);
		back.setSize(150,60);
        back.setPosition(680, 500);
		back.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
	            BACK = true;
	            return true;
            }
        });
		
    	start = new TextButton("START",skin);
    	start.setSize(150,60);
        start.setPosition(850, 500);
    	start.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
	            START = true;
	            return true;
            }
        });
        
        stage.addActor(back);
        stage.addActor(start);
        stage.addActor(plusBomb1);
        stage.addActor(minusBomb1);
        stage.addActor(plusBomb2);
        stage.addActor(minusBomb2);
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
        	game.setScreen(new LoadingScreen(game));
        }
        if(BACK)
        {
        	BACK = false;
        	game.setScreen(game.startScreen);
        }
        
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0);
        spriteBatch.end();

        stage.act();
        bomb1.setText(String.format("%01d", numBomb1));
        bomb2.setText(String.format("%01d", numBomb2));
		
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