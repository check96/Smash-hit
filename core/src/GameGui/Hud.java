package GameGui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import videogame.Countdown;
import videogame.GameConfig;

public class Hud implements Disposable
{
    public Stage stage;
    public Stage vortexStage;
   
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
    
    public SpriteBatch spriteBatch;
    private Texture bonus;
    private Texture coin;
    private Texture vortex;
    private Image vortexImage;
    private float x1 = 1;
    
    public Hud()
    {
    	stage = new Stage(new ExtendViewport(GameConfig.width, GameConfig.height), new SpriteBatch());
    	spriteBatch = new SpriteBatch();
    	
    	vortexStage = new Stage(new ExtendViewport(GameConfig.width, GameConfig.height), new SpriteBatch());
    	
    	vortex = new Texture("Icons/Smoke_down.png");
    	
    	vortexImage = new Image(vortex);

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
        bombLabel.setPosition(GameConfig.Screen_Width*45/GameConfig.width, GameConfig.Screen_Height*25/GameConfig.height);
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
    	coinsLabel.setText(String.format("%01d", GameConfig.LOCAL_COINS));
    	
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
    	spriteBatch.enableBlending();
        spriteBatch.draw(bonus,5,5);
        spriteBatch.draw(coin, 922, 524);
        
        if(GameConfig.STATE == "tornado")
        {
        	move();
        	vortexStage.addActor(vortexImage);
        	vortexStage.act();
        	vortexStage.draw();
        	vortexStage.clear();
        }
        spriteBatch.end(); 
    }
    public void move()
    {
    	vortexImage.setPosition(x1%GameConfig.Screen_Width, 0);
    	x1 -= 100; 	
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