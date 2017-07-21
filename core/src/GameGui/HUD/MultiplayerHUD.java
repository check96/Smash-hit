package GameGui.HUD;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import videogame.Countdown;
import videogame.GameConfig;

public class MultiplayerHUD implements Disposable
{
    public Stage stage;
   
    //Scene2D widgets
    private Label scoreLabel;
    private Label timeLabel;
    
    private Label pointsLabel;
    private Label countdownLabel;
    
    public SpriteBatch spriteBatch;
    
    public MultiplayerHUD()
    {
    	stage = new Stage(new ExtendViewport(GameConfig.width, GameConfig.height), new SpriteBatch());
    	spriteBatch = new SpriteBatch();
    	
    	FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/comic.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter(); 
    	
        parameter.size = 25;
    	BitmapFont font = generator.generateFont(parameter);
    	
        //define a table used to organize our hud's label
    	Table table = new Table();
    	table.top();
    	table.setFillParent(true);
        
        Color color = Color.NAVY;
        		
        //define our labels using the String, and a Label style consisting of a font and color
        scoreLabel = new Label("SCORE", new Label.LabelStyle(font, color));
        timeLabel = new Label("TIME", new Label.LabelStyle(font, color));
        
        countdownLabel = new Label(String.format("%03d", Countdown.getTime()), new Label.LabelStyle(font, color));
        pointsLabel = new Label(String.format("%06d", GameConfig.SCORE), new Label.LabelStyle(font, color));
        
        //add our labels to our table, padding the top, and giving them all equal width with expandX
        table.add(scoreLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
       
        //add a second row to our table
        table.row();
        table.add(pointsLabel).expandX();
        table.add(countdownLabel).expandX();
        
        //add tables to the stage
        stage.addActor(table);
    }

    public synchronized void update()
    {
    	// update labels
    	countdownLabel.setText(String.format("%02d", Countdown.getTime()));
    	pointsLabel.setText(String.format("%06d", GameConfig.SCORE));
   		
    	spriteBatch.begin();
    	spriteBatch.enableBlending();
        spriteBatch.end(); 
    }

    @Override
    public void dispose() { stage.dispose(); }

    public void resize(int width, int height)
	{
    	stage.getViewport().update(width, height, true);
		GameConfig.Screen_Height = height;
		GameConfig.Screen_Width = width;
	}
}