package GameGui;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import GameGui.Screen.GameScreen;
import entity.Destroyable;
import entity.Wall;
import videogame.GameConfig;

public class AssetHandler
{
	private String player = "player/player.g3db";		// scalato di 0.0095
	private String door = "door/door.g3db";				// scalato di 0.005
	private String desk = "desk/desk.g3db";				// scalato di 0.05       
	private String chair = "chair/chair.g3db";			// scalato di 0.055
	private String plant = "plant/plant.g3db";			// scalato di 0.01
	private String locker = "drawer/drawer.g3db";		// scalato di 0.055
	private String clock = "clock/clock.g3db";
	private String coin = "coin/coin.g3db"; 
			
	private Model ceiling;
	private Model wallModel;
	private Model floorModel;
	private Material wall;
	
	public static Model coinModel;
	public Model bomb1;
	public Model bomb2;
	public Model grid;
	public Model help;

	public ArrayList<AnimationController> clockAnimation;
	public AssetManager manager;
	private ModelBuilder modelBuilder;
	
	public AssetHandler()
	{
		manager = new AssetManager();
		modelBuilder = new ModelBuilder();
		clockAnimation = new ArrayList<AnimationController>();
	}
	
	public void loadWalls()
	{
		Texture wallTexture = new Texture(Gdx.files.internal("texture/wall.jpeg"));
		wall = new Material();
		wall.set(new TextureAttribute(TextureAttribute.Diffuse, wallTexture));
		wallModel = createPlaneModel(22,GameConfig.ROOM_COLUMN*GameConfig.CELL_WIDTH+10, wall, 0, 0, 1, 1);
		
		ceiling = modelBuilder.createBox(10+GameConfig.ROOM_ROW * GameConfig.CELL_HEIGHT, 1f, 10+GameConfig.ROOM_COLUMN * GameConfig.CELL_WIDTH,
						       new Material(ColorAttribute.createDiffuse(Color.WHITE)),Usage.Position | Usage.Normal);
		
		Texture floorTexture = new Texture(Gdx.files.internal("texture/floor.jpeg"));
		Material floor = new Material();
		floor.set(new TextureAttribute(TextureAttribute.Diffuse, floorTexture));		

		floorModel = createPlaneModel(GameConfig.ROOM_ROW*GameConfig.CELL_HEIGHT +10, GameConfig.ROOM_COLUMN*GameConfig.CELL_WIDTH+10, floor, 0, 0, 1, 1);
	}
	
	// load models
	public void loadModels()
	{	
		loadWalls();

		help = modelBuilder.createCylinder(GameConfig.CELL_HEIGHT*0.9f, 0.1f, GameConfig.CELL_WIDTH*0.9f, 10, new Material(ColorAttribute.createDiffuse(Color.GREEN)),
				Usage.Position | Usage.Normal);
		
		grid = modelBuilder.createLineGrid(GameConfig.ROOM_ROW,GameConfig.ROOM_COLUMN, GameConfig.CELL_HEIGHT, GameConfig.CELL_WIDTH,
				new Material(ColorAttribute.createDiffuse(Color.WHITE)), Usage.Position | Usage.Normal);

		bomb1 = modelBuilder.createSphere(0.6f,0.6f,0.6f,100,100, new Material(ColorAttribute.createDiffuse(Color.BLACK)),
				Usage.Position | Usage.Normal);
		
		bomb2 = modelBuilder.createSphere(0.6f,0.6f,0.6f,100,100, new Material(ColorAttribute.createDiffuse(Color.RED)),
				Usage.Position | Usage.Normal);
		
		manager.load(player, Model.class);
		manager.load(chair, Model.class);
		manager.load(clock, Model.class);
		manager.load(door, Model.class);
		manager.load(desk, Model.class);
		manager.load(plant, Model.class);
		manager.load(locker, Model.class);
		manager.load(coin, Model.class);
		manager.finishLoading();		
		
		coinModel = manager.get(coin, Model.class);
	}
	
	private Model createPlaneModel(final float width, final float height, final Material material, final float u1,
			final float v1, final float u2, final float v2)
	{
		modelBuilder.begin();
		MeshPartBuilder bPartBuilder = modelBuilder.part("rect", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal | Usage.TextureCoordinates, material);
		// NOTE ON TEXTURE REGION, MAY FILL OTHER REGIONS, USE GET region.getU() and so on
		bPartBuilder.setUVRange(u1, v1, u2, v2);
		bPartBuilder.rect(-(width * 0.5f), -(height * 0.5f), 0, (width * 0.5f), -(height * 0.5f), 0, (width * 0.5f),
				(height * 0.5f), 0, -(width * 0.5f), (height * 0.5f), 0, 0, 0, -1);

		return (modelBuilder.end());
	}                        
	
	// load player and floor
	public void loadPlayer()	
	{
			GameScreen.playerInstance = new ModelInstance(manager.get(player, Model.class));
			GameScreen.playerInstance.transform.setToTranslation(GameConfig.player.getPosition());
	}

	// load model instances
	public void loadTools()			
	{
		// for each matrix cell, load its modelInstance
		for (int i = 0; i < GameConfig.newTools.length; i++)
			for (int j = 0; j < GameConfig.newTools[i].length; j++)
			{
				Destroyable obj = GameConfig.newTools[i][j];
				ModelInstance modInst = null;
				if(obj instanceof Destroyable)
				{
					switch (obj.type)
					{
						case DESK:		modInst = new ModelInstance(manager.get(desk, Model.class));
										modInst.transform.setToTranslation(obj.getPosition());
										break;
										
						case CHAIR:		modInst = new ModelInstance(manager.get(chair, Model.class));
										modInst.transform.setToTranslation(obj.getPosition());
										modInst.transform.rotate(0,1,0, -90);
										break;
										
						case PLANT:		modInst = new ModelInstance(manager.get(plant, Model.class));
										modInst.transform.setToTranslation(obj.getPosition());
										break;

						case LOCKER:	modInst = new ModelInstance(manager.get(locker, Model.class));
										modInst.transform.setToTranslation(obj.getPosition());
										break;
										
						case CLOCK:		modInst = new ModelInstance(manager.get(clock,Model.class));
										synchronized(clockAnimation)
										{
											clockAnimation.add(new AnimationController(modInst));
											clockAnimation.get(clockAnimation.size()-1).setAnimation("clock|clockAction",-1);
										}
										modInst.transform.setToTranslation(obj.getPosition());
										break;
						case DOOR:		modInst = new ModelInstance(manager.get(door,Model.class));
										modInst.transform.setToTranslation(obj.getPosition());
										modInst.transform.rotate(0,1,0, 90);
										break;
						
						default:        break;
					}
					
					if(modInst != null)
						GameConfig.newInstances.add(modInst);	
				}
			}
		
			// load walls and door instances
		synchronized (GameConfig.walls)
		{
			for (Wall obj : GameConfig.walls)
			{	
				ModelInstance wallInst = null;
				switch (obj.type)
				{
					case FOREWARD_WALL:	wallInst = new ModelInstance(wallModel);
										wallInst.transform.setToTranslation(obj.getPosition());
										wallInst.transform.rotate(0,1,0,-90);
										wallInst.transform.rotate(0,0,1,90);
									break;
					case LEFT_WALL:		wallInst = new ModelInstance(wallModel);
										wallInst.transform.setToTranslation(obj.getPosition());
										wallInst.transform.rotate(0,0,1,90);
									break;
					case CEILING:		wallInst = new ModelInstance(ceiling);
										wallInst.transform.setToTranslation(obj.getPosition());
									break;
					case RIGHT_WALL:	wallInst = new ModelInstance(wallModel);
										wallInst.transform.setToTranslation(obj.getPosition());
										wallInst.transform.rotate(0,1,0,180);
										wallInst.transform.rotate(0,0,1,90);
									break;
					case BACK_WALL:		wallInst = new ModelInstance(wallModel);
										wallInst.transform.setToTranslation(obj.getPosition());
										wallInst.transform.rotate(0,1,0,90);
										wallInst.transform.rotate(0,0,1,90);
									break;
					case FLOOR:			wallInst = new ModelInstance(floorModel);
										wallInst.transform.setToTranslation(obj.getPosition());
										wallInst.transform.rotate(0,1,1,180);
									break;
					default:	break;
				}
				synchronized (GameConfig.wallsInstance)
				{
					if(wallInst != null)
						GameConfig.wallsInstance.add(wallInst);
				}
			}
		}	
	}

	public void dispose()
	{
		floorModel.dispose();
		wallModel.dispose();
		ceiling.dispose();
		manager.dispose();
		help.dispose();
	}
}