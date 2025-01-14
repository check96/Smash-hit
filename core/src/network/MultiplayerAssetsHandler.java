package network;

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
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import entity.Destroyable;
import entity.Wall;
import videogame.GameConfig;

public class MultiplayerAssetsHandler
{
	private String player = "player/player.g3db";		// scalato di 0.0095
	private String desk = "desk/desk.g3db";				// scalato di 0.05       
	private String chair = "chair/chair.g3db";			// scalato di 0.055
	private String plant = "plant/plant.g3db";			// scalato di 0.01
	private String locker = "drawer/drawer.g3db";		// scalato di 0.055
	private Model ceiling;
	private Model wallModel;
	private Model floorModel;
	private Material wall;
	
	public AssetManager manager;
	private ModelBuilder modelBuilder;
	
	public MultiplayerAssetsHandler()
	{
		manager = new AssetManager();
		modelBuilder = new ModelBuilder();
	}
	
	public void loadWalls()
	{
		Texture wallTexture = new Texture(Gdx.files.internal("texture/wall.jpeg"));
		wall = new Material();
		wall.set(new TextureAttribute(TextureAttribute.Diffuse, wallTexture));
		wallModel = createPlaneModel(22,GameConfig.ROOM_COLUMN*GameConfig.CELL_WIDTH+10, wall, 0, 0, 1, 1);
		
		ceiling = modelBuilder.createBox(10+GameConfig.ROOM_ROW * GameConfig.CELL_HEIGHT, 1f, 10 + GameConfig.ROOM_COLUMN * GameConfig.CELL_WIDTH,
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

		manager.load(player, Model.class);
		manager.load(chair, Model.class);
		manager.load(desk, Model.class);
		manager.load(plant, Model.class);
		manager.load(locker, Model.class);
		manager.finishLoading();		
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
	
	// load player 
	public void loadPlayer()	
	{
		for(int i = 0; i < GameConfig.players.size(); i++)
		{
			ModelInstance instance = new ModelInstance(manager.get(player, Model.class)); 
			instance.transform.setToTranslation(GameConfig.players.get(i).getPosition());
			GameConfig.playersInstance.add(instance);
		}
	}

	// load model instances
	public void loadTools()			
	{
		// for each matrix cell, load its modelInstance
		for (int i = 0; i < GameConfig.multiplayerMap.length; i++)
			for (int j = 0; j < GameConfig.multiplayerMap[i].length; j++)
			{
				Destroyable obj = GameConfig.multiplayerMap[i][j];
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
										
						default:        break;
					}
					
					if(modInst != null)
						GameConfig.multiplayerInstances.add(modInst);	
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
					case FOREWARD_UPPER_WALL:	wallInst = new ModelInstance(wallModel);
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
	}
}
