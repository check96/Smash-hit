package GameGui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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
    private Label roomLabel;
    private Label scoreLabel;
    private Label timeLabel;
    private Label moneyLabel;
    
    private Label levelLabel;
    private Label pointsLabel;
    private Label countdownLabel;
    private Label coinsLabel;
    private Label bombLabel;
    
    private SpriteBatch spriteBatch;
    private Texture bonus;
    private Texture coin;
    
    public Hud()
    {
    	stage = new Stage(new FitViewport(800, 600), new SpriteBatch());
    	spriteBatch = new SpriteBatch();
    	
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
        
        coin = new Texture(Gdx.files.internal("Icons/coin.png"));

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
        
        parameter.size = 25;
    	BitmapFont font2 = generator.generateFont(parameter);
        
//        bonus = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Icons/hit.png")))));
      
    	bombLabel = new Label(String.format("%01d", GameConfig.numBomb1), new Label.LabelStyle(font2, Color.WHITE));
        bombLabel.setSize(2,2);
        bombLabel.setPosition(45,25);
        bombLabel.setVisible(false);
        
        //add tables to the stage
        stage.addActor(table);
    	stage.addActor(bombLabel);
    }

    public synchronized void update()
    {
    	// update labels
    	countdownLabel.setText(String.format("%02d", Countdown.getTime()));
    	pointsLabel.setText(String.format("%06d", GameConfig.SCORE));
    	levelLabel.setText(String.format("%02d", GameConfig.actualLevel));
    	coinsLabel.setText(String.format("%01d", GameConfig.COINS));
    	
    	if(GameConfig.STATE == "bomb1")
    	{
    		bombLabel.setText(String.format("%01d", GameConfig.numBomb1));
    		bombLabel.setVisible(true);
    	}
    	else if(GameConfig.STATE == "bomb2")
    	{
    		bombLabel.setText(String.format("%01d", GameConfig.numBomb2));
    		bombLabel.setVisible(true);
    	}
    	else
    		bombLabel.setVisible(false);
    	
    	// update bonus box
   		bonus = new Texture(Gdx.files.internal("Icons/"+GameConfig.STATE+".png"));
   		
    	spriteBatch.begin();
        spriteBatch.draw(bonus, 5,5);
        spriteBatch.draw(coin, 922, 524);
        spriteBatch.end();
    }

    @Override
    public void dispose() { stage.dispose(); }
}