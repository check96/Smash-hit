package network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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

public class NetworkScreen implements Screen
{
	private GameManager game;
	private Stage stage;
	private Button join;
	private String ip;
	private Label ipLabel;
	private Label usernameLabel;
	private TextField ipField;
	private TextField username;
	private boolean JOIN = false;
	
	public NetworkScreen(GameManager _game)
	{
		this.game = _game;
		stage = new Stage(new ScreenViewport());
		
		Skin mySkin = new Skin(Gdx.files.internal("skin/comic/skin/comic-ui.json"));
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/comic.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter(); 
    	
        parameter.size = 25;
    	BitmapFont font = generator.generateFont(parameter);
    	
    	ipLabel = new Label("IP ADDRESS", new Label.LabelStyle(font, Color.BLACK));
    	ipLabel.setPosition(80, 370);
    	
    	usernameLabel = new Label("Your username", new Label.LabelStyle(font, Color.BLACK));
		usernameLabel.setPosition(80, 270);
    	
    	ipField = new TextField("", mySkin);
    	ipField.setPosition(300, 350);
		ipField.setSize(200, 70);
		
		username = new TextField("", mySkin);
		username.setPosition(300, 250);
		username.setSize(200, 70);
		
		join = new TextButton("LOGIN",mySkin);
	    join.setSize(300, 100);
	    join.setPosition(150,100);
	    join.addListener(new InputListener(){
	           @Override
	           public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
	           {
	        	   JOIN = true;
	             	return true;
	           }
	           
	       });
	       stage.addActor(join);
	       stage.addActor(username);
	       stage.addActor(usernameLabel);
	       stage.addActor(ipField);
	       stage.addActor(ipLabel);
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
        
        if(JOIN)
        {
        	join.setVisible(false);
        	JOIN = false;
        	new Client(game);
        }
        stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
