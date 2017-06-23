package network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import GameGui.GameManager;

public class SelectScreen implements Screen 
{
	private GameManager game;
	private Stage stage;
	
	private Server server;
	private boolean CREATE = false;
	private boolean selected = false;
	
	public SelectScreen(GameManager _game)
	{
		this.game = _game;
		stage = new Stage(new ScreenViewport());
		
	   Skin mySkin = new Skin(Gdx.files.internal("skin/comic/skin/comic-ui.json"));
	     
       Button create = new TextButton("CREATE A SERVER",mySkin);
       create.setSize(500, 100);
       create.setPosition(50,300);
       create.addListener(new InputListener(){
           @Override
           public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
           {
        	   CREATE = true;
               return true;
           }
           
       });
       stage.addActor(create);
       
       Button join = new TextButton("CONNECT TO A SERVER",mySkin);
       join.setSize(500, 100);
       join.setPosition(50,100);
       join.addListener(new InputListener(){
           @Override
           public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
           {
        	   selected = true;
               return true;
           }
           
       });
       stage.addActor(join);
       
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
    
        if(CREATE)
        {
        	CREATE = false;
        	selected = true;
        	server = new Server();
        	server.start();
        }
        if(selected)
        {
        	selected = false;
        	this.dispose();
        	game.setScreen(new NetworkScreen(game));
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
	public void dispose()
	{
		stage.dispose();
	}

	
}
