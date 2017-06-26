package GameGui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import editor.Editor;
import network.SelectScreen;
import videogame.GameConfig;

public class StartScreen implements Screen 
{
	private GameManager game;
    private Stage stage;
    public static EditorScreen editorScreen;	
	public static OptionScreen optionScreen;

    public boolean SINGLE_PLAYER = false;
    private boolean MULTIPLAYER = false;
    private boolean OPTIONS = false;
    private boolean EDITOR = false;
    
    public StartScreen(GameManager _game)
    {
    	game = _game;
        stage = new Stage(new ScreenViewport());
        
        editorScreen = new EditorScreen(game);
		optionScreen = new OptionScreen(game);
		
        int row_height = Gdx.graphics.getWidth();
        int col_width = Gdx.graphics.getWidth();
 
        Skin mySkin = new Skin(Gdx.files.internal("skin/comic/skin/comic-ui.json"));

        Table table = new Table(mySkin);
        table.center();
        table.setFillParent(true);
      
        // single player
        Button singlePlayer = new TextButton("Single Player",mySkin);
        singlePlayer.setSize(col_width/5,row_height/16);
        singlePlayer.setPosition(col_width/2,row_height/2);
        singlePlayer.addListener(new InputListener(){
        	          @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
            	SINGLE_PLAYER = true;
            	return true;
            }
            
        });
 
        // multi player
        Button multiPlayer = new TextButton("multiplayer",mySkin);
        multiPlayer.setSize(col_width/8,row_height/8);
        multiPlayer.setPosition(col_width/2,row_height);
        multiPlayer.addListener(new InputListener()
        {    
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
            	MULTIPLAYER = true;
                return true;
            }
        });
 
        // editor
        Button editor = new TextButton("Editor",mySkin);
        editor.setSize(col_width/4,row_height/4);
        editor.setPosition(col_width,row_height);
        editor.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
            	EDITOR = true;
                return true;
            }
         
        });
 
        // options
        Button options = new TextButton("Options",mySkin);
        options.setSize(col_width/4,row_height/4);
        options.setPosition(col_width,row_height*0.5f);
        options.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
            	OPTIONS = true;
                return true;
            }
        });
        
        table.add(singlePlayer).expandX().padTop(10);
        table.row();
        table.add(multiPlayer).expandX().padTop(10);
        table.row();
        table.add(editor).expandX().padTop(10);
        table.row();
        table.add(options).expandX().padTop(10);
        stage.addActor(table);
    }
 
    public void render(float delta)
    {
    	if(EDITOR)
     	{
     		EDITOR = false;
     		game.setScreen(editorScreen);
     	}
    	if(SINGLE_PLAYER)
    	{
    		SINGLE_PLAYER = false;
    		this.dispose();
    		game.setScreen(new LoadingScreen(game));
    	}
    	else if(MULTIPLAYER)
    	{
    		GameConfig.MULTIPLAYER = true;
    		MULTIPLAYER = false;
    		game.setScreen(new SelectScreen(game));
    	}
    	else if(OPTIONS)
    	{
    		OPTIONS = false;
    		game.setScreen(optionScreen);
    	}
    	
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

	@Override
	public void show()
	{
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void resize(int width, int height)
	{
		stage.getViewport().update(width, height, true);
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
		editorScreen.dispose();
		optionScreen.dispose();
	}
}