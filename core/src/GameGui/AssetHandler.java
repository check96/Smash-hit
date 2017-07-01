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
import entity.Player;
import entity.Wall;
import videogame.GameConfig;

public class AssetHandler
{
	private String player = "player/player.g3db";		// su blender scalato di 0.0015
	private String door = "door/untitled.g3db";
	private String desk = "desk/table.g3db";
	private String chair = "chair/untitled.g3db";
	private String plant = "plant/indoor plant_02_+2.g3db";
	private String printer = "printer/004.g3db";
	private String locker = "drawer/drawers of office.g3db";
	private String clock = "clock/clock.g3db";

	private Model ceiling;
	private Model wallModel;

	public Model grid;
	public  Model help;
	private  Model floorModel;
	private Material wall;
	public  ArrayList<AnimationController> animation;
	
	public AssetManager manager;
	private ModelBuilder modelBuilder;
	
	public AssetHandler()
	{
		manager = new AssetManager();
		modelBuilder = new ModelBuilder();
		animation = new ArrayList<AnimationController>();
	}
	
	public void loadWalls()
	{
		Texture wallTexture = new Texture(Gdx.files.internal("texture/wall.jpeg"));
		wall = new Material();
		wall.set(new TextureAttribute(TextureAttribute.Diffuse, wallTexture));
		wallModel = createPlaneModel(22,90, wall, 0, 0, 1, 1);
		
		ceiling = modelBuilder.createBox(10+GameConfig.ROOM_DIMENSION *5.5f, 1f, 10+GameConfig.ROOM_DIMENSION *6f,
						       new Material(ColorAttribute.createDiffuse(Color.WHITE)),Usage.Position | Usage.Normal);
		
		Texture floorTexture = new Texture(Gdx.files.internal("texture/floor.jpeg"));
		Material floor = new Material();
		floor.set(new TextureAttribute(TextureAttribute.Diffuse, floorTexture));		

		floorModel = createPlaneModel(100, 100, floor, 0, 0, 1, 1);                        
	}
	
	// load models
	public void load()
	{	
		loadWalls();
		help = modelBuilder.createCylinder(5.5f, 0.1f, 5.5f,10, new Material(ColorAttribute.createDiffuse(Color.GREEN)),
				Usage.Position | Usage.Normal);
		
		grid = modelBuilder.createLineGrid(15,15, 5.5f, 5.5f, new Material(ColorAttribute.createDiffuse(Color.WHITE)),
				Usage.Position | Usage.Normal);

		manager.load(player, Model.class);
		manager.load(chair, Model.class);
		manager.load(clock, Model.class);
		manager.load(door, Model.class);
		manager.load(desk, Model.class);
		manager.load(plant, Model.class);
		manager.load(locker, Model.class);
		manager.load(printer, Model.class);
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
	
	// load player and floor
	public void loadPlayer()	
	{
		for (Player _player : GameConfig.players)
		{
			ModelInstance mod = new ModelInstance(manager.get(player, Model.class));
			mod.transform.setToTranslation(_player.getPosition());
		
			GameScreen.playersInstance.add(mod);
		}
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
						case DESK:		float deskScale = 0.037f;
										modInst = new ModelInstance(manager.get(desk, Model.class));
										//animation.add(new AnimationController(modInst));
										//animation.get(animation.size()-1).setAnimation("Armature|ArmatureAction",-1);
										modInst.transform.setToTranslation(obj.getPosition());
										modInst.transform.scl(deskScale);
										break;
						case CHAIR:		float chairScale = 0.015f;						
										modInst = new ModelInstance(manager.get(chair, Model.class));
										modInst.transform.setToTranslation(obj.getPosition());
										modInst.transform.scl(chairScale);
										modInst.transform.rotate(0,1,0, -90);
										break;
						case PLANT:		float plantScale = 0.008f;
										modInst = new ModelInstance(manager.get(plant, Model.class));
										modInst.transform.setToTranslation(obj.getPosition());
										modInst.transform.scl(plantScale);
										break;
						case PRINTER:	float printerScale = 0.2f;
										modInst = new ModelInstance(manager.get(printer, Model.class));
										modInst.transform.setToTranslation(obj.getPosition());
										modInst.transform.scl(printerScale);
										break;
						case LOCKER:	float lockerScale = 0.05f;
										modInst = new ModelInstance(manager.get(locker, Model.class));
										modInst.transform.setToTranslation(obj.getPosition());
										modInst.transform.scl(lockerScale);
										break;
						case CLOCK:		modInst = new ModelInstance(manager.get(clock,Model.class));
										animation.add(new AnimationController(modInst));
										animation.get(animation.size()-1).setAnimation("clock|clockAction",-1);
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
										
					default:			break;
				}
				if(wallInst != null)
					GameConfig.wallsInstance.add(wallInst);
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

