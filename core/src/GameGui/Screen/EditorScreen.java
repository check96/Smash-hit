package GameGui.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import GameGui.GameManager;
import GameGui.Shop;
import editor.Editor;
import videogame.GameConfig;

public class EditorScreen implements Screen
{
	private GameManager game;
	private Stage stage;
	private Editor editor;
	
	private Label levels;
	private TextField numLevels;
	private TextButton play;
	private TextButton back;
	private TextButton create;
	private Table table;
	private Table levelsTable;
	private boolean BACK = false;
	public static boolean CREATED = false;
	private boolean EDITOR = false;
	
    private SpriteBatch spriteBatch;
	private Texture background;
	
	public EditorScreen(GameManager _game) 
	{
		this.game = _game;
		stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));		

 	    spriteBatch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("texture/editor_background.png"));
		
        Skin skin = new Skin(Gdx.files.internal("skin/comic/skin/comic-ui.json"));
        Skin mySkin = new Skin(Gdx.files.internal("skin/holo/skin/Holo-dark-hdpi.json"));
        
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/comic.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter(); 
        parameter.size = 30;
    	BitmapFont Font = generator.generateFont(parameter);
    	   	
    	levelsTable = new Table(skin);
    	levelsTable.center();
    	levelsTable.setFillParent(true);
    	
		table = new Table(skin);
		table.bottom();
        table.setFillParent(true);        
        
        levels = new Label("NUMBER OF LEVELS", new Label.LabelStyle(Font, Color.WHITE));
        
        numLevels = new TextField("1", mySkin);
        numLevels.setAlignment(Align.center);
		numLevels.setSize(200, 70);        
		
		back = new TextButton("BACK", skin);
		back.addListener(new InputListener()
		{
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
	        {
	            BACK = true;
	            return true;
	        }
	    });
		
		play = new TextButton("PLAY", skin);
		play.addListener(new InputListener(){
	  		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
	        {
	  			GameConfig.EDITOR = true;
				game.setScreen(new Shop(game));
	            return true;
	        }
	    });
		
		create = new TextButton("CREATE LEVELS", skin);
		create.setPosition(GameConfig.Screen_Width*300/GameConfig.width, GameConfig.Screen_Height*200/GameConfig.height);
		create.addListener(new InputListener(){
	  		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
	        {
	            EDITOR = true;
	            return true;
	        }
	    });
		levelsTable.add(levels).expandX().padTop(1);
		levelsTable.add(numLevels).expandX().padTop(1);
		
//		table.row();
		table.add(back).expandX().padTop(1);
		
		stage.addActor(levelsTable);
		stage.addActor(create);
		stage.addActor(table);
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
		
		if(BACK || (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)))
		{
			BACK = false;
			game.setScreen(game.startScreen);
		}
		
		if(EDITOR)
		{
			EDITOR = false;
			editor = new Editor(Integer.parseInt(numLevels.getText()),game);
			table.add(play).expandX().padLeft(1);
		}
		
		if(CREATED)
		{
			CREATED = false;
			editor.setVisible(false);
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