package GameGui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import videogame.Countdown;
import videogame.GameConfig;

public class Hud implements Disposable
{
    public Stage stage;
   
    //Scene2D widgets
    private Label countdownLabel;
    private Label scoreLabel;
    private Label timeLabel;
    private Label moneyLabel;
    
    private Label roomLabel;
    private Label pointsLabel;
    private Label levelLabel;
    private Label coinsLabel;
    
    public Hud()
    {
    	stage = new Stage(new FitViewport(800, 600), new SpriteBatch());

    	FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/comic.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter(); 
    	
        parameter.size = 25;
    	BitmapFont font = generator.generateFont(parameter);
    	
        //define a table used to organize our hud's label
    	
    	Table table = new Table();
        table.top();
        table.setFillParent(true);
        
        Color color = Color.BLACK;
        		
        //define our labels using the String, and a Label style consisting of a font and color
        roomLabel = new Label("ROOM", new Label.LabelStyle(font, color));
        scoreLabel = new Label("SCORE", new Label.LabelStyle(font, color));
        timeLabel = new Label("TIME", new Label.LabelStyle(font, color));
        moneyLabel = new Label("COINS", new Label.LabelStyle(font, color));
        
        countdownLabel = new Label(String.format("%03d", Countdown.getTime()), new Label.LabelStyle(font, color));
        pointsLabel = new Label(String.format("%06d", GameConfig.SCORE), new Label.LabelStyle(font, color));
        levelLabel = new Label("1", new Label.LabelStyle(font, color));
        coinsLabel = new Label("0", new Label.LabelStyle(font, color));
        
        //add our labels to our table, padding the top, and giving them all equal width with expandX
        table.add(roomLabel).expandX().padTop(10);
        table.add(scoreLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.add(moneyLabel).expandX().padTop(10);
       
        //add a second row to our table
        table.row();
        table.add(levelLabel).expandX();
        table.add(pointsLabel).expandX();
        table.add(countdownLabel).expandX();
        table.add(coinsLabel).expandX();
        
        //add our table to the stage
        stage.addActor(table);        
    }

    public synchronized void update(float dt)
    {
    	countdownLabel.setText(String.format("%02d", Countdown.getTime()));
    	pointsLabel.setText(String.format("%06d", GameConfig.SCORE));
    	levelLabel.setText(String.format("%02d", GameConfig.actualLevel));
    	coinsLabel.setText((String.format("%01d", GameConfig.COINS)));
    }

    @Override
    public void dispose() { stage.dispose(); }
}