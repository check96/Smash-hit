package network.Screen;

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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import GameGui.GameManager;
import network.Server;
import videogame.GameConfig;

public class NetworkScreen implements Screen 
{
	private GameManager game;
	private Stage stage;
	private SpriteBatch spriteBatch;
	private Texture background;

	private Button join;
	private Button create;
	private Button login;
	private Button back;
	private Button createBack;
	private Button joinBack;
	private Button createServer;

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

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/comic.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

		parameter.size = 25;
		BitmapFont font = generator.generateFont(parameter);

		Skin skin = new Skin(Gdx.files.internal("skin/comic/skin/comic-ui.json"));
		Skin mySkin = new Skin(Gdx.files.internal("skin/holo/skin/Holo-dark-hdpi.json"));

		Color color = Color.WHITE;

		ipLabel = new Label("IP ADDRESS", new Label.LabelStyle(font, color));
		ipLabel.setPosition(310, 370);

		usernameLabel = new Label("Your username", new Label.LabelStyle(font, color));
		usernameLabel.setPosition(270, 305);

		portLabel = new Label("PORT", new Label.LabelStyle(font, color));
		portLabel.setPosition(335, 240);

		numPlayersLabel = new Label("number of players", new Label.LabelStyle(font, color));
		numPlayersLabel.setPosition(250, 305);

		ip = new TextField("169.254.116.179", mySkin);	//169.254.116.179
		ip.setPosition(550, 370);
		ip.setSize(220, 60);

		username = new TextField("dgfgd", mySkin);
		username.setPosition(550, 305);
		username.setSize(150, 60);

		port = new TextField("12345", mySkin);
		port.setPosition(550, 235);
		port.setSize(150, 60);

		numPlayers = new TextField("2", mySkin);
		numPlayers.setAlignment(Align.center);
		numPlayers.setPosition(550, 305);
		numPlayers.setSize(150, 60);

		back = new TextButton("back", skin);
		back.setSize(150, 80);
		back.setPosition(50, 50);
		back.addListener(new InputListener()
		{
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) 
			{
				game.setScreen(game.startScreen);
				return true;
			}
		});

		createBack = new TextButton("back", skin);
		createBack.setSize(150, 80);
		createBack.setPosition(50, 50);
		createBack.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) 
			{
				stage.clear();
				stage.addActor(create);
				stage.addActor(join);
				stage.addActor(back);
				return true;
			}
		});

		joinBack = new TextButton("back", skin);
		joinBack.setSize(150, 80);
		joinBack.setPosition(50, 50);
		joinBack.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) 
			{
				stage.clear();
				stage.addActor(create);
				stage.addActor(join);
				stage.addActor(back);
				return true;
			}
		});

		join = new TextButton("join", skin);
		join.setSize(150, 80);
		join.setPosition(400, 170);
		join.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) 
			{
				stage.clear();
				stage.addActor(username);
				stage.addActor(usernameLabel);
				stage.addActor(ip);
				stage.addActor(ipLabel);
				stage.addActor(port);
				stage.addActor(portLabel);
				stage.addActor(login);
				stage.addActor(joinBack);
				return true;
			}
		});

		create = new TextButton("create server", skin);
		create.setSize(300, 100);
		create.setPosition(330, 260);
		create.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) 
			{
				stage.clear();
				stage.addActor(numPlayers);
				stage.addActor(numPlayersLabel);
				stage.addActor(createBack);
				stage.addActor(createServer);
				stage.addActor(ipLabel);
				stage.addActor(ip);
				stage.addActor(port);
				stage.addActor(portLabel);
				return true;
			}
		});

		createServer = new TextButton("create", skin);
		createServer.setSize(200, 80);
		createServer.setPosition(510, 120);
		createServer.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) 
			{ 
				GameConfig.server = new Server(Integer.parseInt(port.getText()), Integer.parseInt(numPlayers.getText()));
				GameConfig.isServer = true;
				synchronized (GameConfig.tools)
				{
					GameConfig.tools.clear();
				}
				synchronized (GameConfig.toolsInstance)
				{
					GameConfig.toolsInstance.clear();
				}
				
				game.multiplayerMapGenerator.createRoom();
				
				stage.clear();
				stage.addActor(username);
				stage.addActor(usernameLabel);
				stage.addActor(ip);
				stage.addActor(ipLabel);
				stage.addActor(port);
				stage.addActor(portLabel);
				stage.addActor(login);
				stage.addActor(joinBack);
				return true;
			}
		});

		login = new TextButton("LOGIN", skin);
		login.setSize(200, 80);
		login.setPosition(510, 120);
		login.addListener(new InputListener()
		{
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) 
			{
				GameConfig.username = username.getText();
				game.setScreen(new MultiplayerLobby(game, ip.getText(), Integer.parseInt(port.getText())));
				return true;
			}
		});

		stage.addActor(join);
		stage.addActor(create);
		stage.addActor(back);
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
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
	}
}
