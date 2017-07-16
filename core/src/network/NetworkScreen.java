package network;

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
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import GameGui.GameManager;
import videogame.GameConfig;

public class NetworkScreen implements Screen
{
	private GameManager game;
	private Stage stage;
	private Server server;
	private SpriteBatch spriteBatch;
	private Texture background;
	
	private Button join;
	private Button create;
	
	private Label ipLabel;
	private Label usernameLabel;
	private Label portLabel;
	private Label numPlayersLabel;
	
	private TextField ip;
	private TextField username;
	private TextField port;
	private TextField numPlayers;
	
	public NetworkScreen(GameManager _game)
	{
		this.game = _game;
		stage = new Stage(new ScreenViewport());
		spriteBatch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("texture/multiplayer_background.png"));
        
		Skin mySkin = new Skin(Gdx.files.internal("skin/comic/skin/comic-ui.json"));
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/comic.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter(); 
    	
        parameter.size = 25;
    	BitmapFont font = generator.generateFont(parameter);
    	
    	Color color = Color.WHITE;
    	
    	ipLabel = new Label("IP ADDRESS", new Label.LabelStyle(font, color));
    	ipLabel.setPosition(180, 380);
    	    	
    	usernameLabel = new Label("Your username", new Label.LabelStyle(font, color));
		usernameLabel.setPosition(180, 310);
		
		portLabel = new Label("PORT", new Label.LabelStyle(font, color));
		portLabel.setPosition(180, 240);
    	
		numPlayersLabel = new Label("num players", new Label.LabelStyle(font, color));
		numPlayersLabel.setPosition(180, 170);
		
    	ip = new TextField("127.0.0.1", mySkin);
    	ip.setPosition(400, 350);
		ip.setSize(150, 60);
		
		username = new TextField("", mySkin);
		username.setPosition(400, 285);
		username.setSize(150, 60);
		
		port = new TextField("", mySkin);
		port.setPosition(400, 220);
		port.setSize(150, 60);
		
		numPlayers = new TextField("2", mySkin);
		numPlayers.setPosition(400, 155);
		numPlayers.setSize(150, 60);
		
		join = new TextButton("LOGIN",mySkin);
	    join.setSize(150, 80);
	    join.setPosition(380,60);
	    join.addListener(new InputListener(){
	           @Override
	           public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
	           {
	        	   if(server instanceof Server)
	        		   new Client(game,ip.getText(),username.getText(), Integer.parseInt(port.getText()), Integer.parseInt(numPlayers.getText()));
	             	
	        	   return true;
	           }
	       });
	    
	    create = new TextButton("create server",mySkin);
	    create.setSize(300, 100);
	    create.setPosition(50,50);
	    create.addListener(new InputListener(){
	           @Override
	           public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
	           {
	        	   	server = new Server(Integer.parseInt(port.getText()), Integer.parseInt(numPlayers.getText()));
	             	return true;
	           }
	       });
	    
	       stage.addActor(join);
	       stage.addActor(create);
	       stage.addActor(username);
	       stage.addActor(usernameLabel);
	       stage.addActor(ip);
	       stage.addActor(ipLabel);
	       stage.addActor(port);
	       stage.addActor(portLabel);
	       stage.addActor(numPlayers);
	       stage.addActor(numPlayersLabel);
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
	public void dispose() {	}
}
