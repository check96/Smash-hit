package videogame;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

import GameGui.AssetHandler;
import GameGui.GameManager;
import editor.Editor;
import entity.Destroyable;
import entity.Objects;
import entity.Walls;
import entity.Wall;

public class MapGenerator extends Thread
{
	public AssetHandler assets;
	private GameManager game;
	public boolean pause = true;
	public MapGenerator(GameManager _game)
	{
		assets = new AssetHandler();
		game = _game;
	}
	
	public void run()
	{
		while(true)
		{
			if(!pause )
			{
				if(!GameConfig.EDITOR)
					createRoom();
				else			// load editor map
					loadRoom();
			}

			synchronized(this)
			{
				try 
				{
					this.wait(10000);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	public void createWalls()
	{
		float position = (GameConfig.level-1) * GameConfig.ROOM_ROW * GameConfig.CELL_HEIGHT;

		synchronized(GameConfig.walls)
		{
			//create left wall
			GameConfig.walls.add(new Wall(new Vector3((-5 +GameConfig.ROOM_ROW * GameConfig.CELL_HEIGHT/2)+position,0,-5), Walls.LEFT_WALL));
		
			//create right wall
			GameConfig.walls.add(new Wall(new Vector3((-5 + GameConfig.ROOM_ROW * GameConfig.CELL_HEIGHT/2)+position,0,-2.5f+GameConfig.ROOM_COLUMN*GameConfig.CELL_WIDTH), Walls.RIGHT_WALL));
			
			//create back wall
			GameConfig.walls.add(new Wall(new Vector3((-4.3f + GameConfig.ROOM_ROW*GameConfig.CELL_HEIGHT)+(GameConfig.level-2)*GameConfig.ROOM_ROW * GameConfig.CELL_HEIGHT,0,-2+GameConfig.ROOM_COLUMN * GameConfig.CELL_WIDTH/2), Walls.BACK_WALL));
			
			//create front wall
			GameConfig.walls.add(new Wall(new Vector3((-4 +GameConfig.ROOM_ROW*GameConfig.CELL_HEIGHT)+ position,0,-2 +GameConfig.ROOM_COLUMN * GameConfig.CELL_WIDTH/2), Walls.FOREWARD_WALL));
			
			//create ceiling
			GameConfig.walls.add(new Wall(new Vector3((GameConfig.ROOM_ROW * GameConfig.CELL_HEIGHT/2)+position, 10.6f, GameConfig.ROOM_COLUMN * GameConfig.CELL_WIDTH/2),Walls.CEILING));
			
			//create floor
			GameConfig.walls.add(new Wall(new Vector3((GameConfig.ROOM_ROW * GameConfig.CELL_HEIGHT/2)+position,-5, -2+GameConfig.ROOM_COLUMN*GameConfig.CELL_WIDTH/2),Walls.FLOOR));
		}
	}
	
	private void loadRoom()
	{
		Random rand = new Random(System.currentTimeMillis());
		GameConfig.EDITOR = false;
		for(int i = 1; i<= Editor.numLevels; i++)
		{
			int[][] points = new int[GameConfig.ROOM_ROW][GameConfig.ROOM_COLUMN];
			String line = game.editorLevels.getString("level"+i);
			 
			for(int j=0; j<points.length; j++)
			{
				String subLine = line.substring(j * GameConfig.ROOM_ROW, j * GameConfig.ROOM_ROW + GameConfig.ROOM_COLUMN);
				for(int k=0; k<points[j].length; k++)
					points[j][k] = subLine.charAt(k) - 48;
			}

			uploadTools(points);

			// create the clock
			int w = Math.abs(rand.nextInt()% (GameConfig.ROOM_COLUMN-6))+3;

				// 	choose side of clock (right or left)
			int h = rand.nextBoolean() ? 0 : GameConfig.ROOM_ROW -1;
			
			float x = w*GameConfig.CELL_HEIGHT + GameConfig.ROOM_ROW * GameConfig.CELL_HEIGHT * (GameConfig.level - 1);
			float z = h*GameConfig.CELL_WIDTH;
			
			float clockMoney = Math.abs((rand.nextInt()%3)) +1;
			GameConfig.newTools[w][h] = new Destroyable(new Vector3(x -1.5f, -3f, z), clockMoney, Objects.CLOCK); 			
			
			// 	create door
			GameConfig.newTools[0][GameConfig.ROOM_COLUMN/2] = new Destroyable(new Vector3(-3.8f + GameConfig.ROOM_ROW *GameConfig.CELL_HEIGHT,
					-5,GameConfig.ROOM_COLUMN * GameConfig.CELL_WIDTH/2), 0, Objects.DOOR);

			// load tools model
			assets.loadTools();
			
			// add new tools to tools
			upgrade();				
		}
	}

	// ONLY for editor
	public void uploadTools(int[][] map)
	{		
		float position = GameConfig.ROOM_ROW * GameConfig.CELL_HEIGHT * (GameConfig.level - 1); 
		
		// create walls
		createWalls();
		
		//		load map and create tools
		Random rand = new Random(System.currentTimeMillis());
		for(int i = 0; i < map.length; i++)
			for(int j = 0; j < map[i].length; j++)
			{
				float x = i*GameConfig.CELL_HEIGHT + position;
				float z = j*GameConfig.CELL_WIDTH;
				switch (map[i][j])
				{
					case 1:		float deskMoney = Math.abs(rand.nextInt()%4);	
								GameConfig.newTools[i][j] = new Destroyable(new Vector3(x, -4.6f, z), deskMoney, Objects.DESK);
								break;
					
					case 2: 	float printerMoney = Math.abs((rand.nextInt()%6)) +1;				
								GameConfig.newTools[i][j] = new Destroyable(new Vector3(x, -5f, z),printerMoney, Objects.PRINTER);
								break;
					
					case 3:		float plantMoney = Math.abs((rand.nextInt()%5)) +1;
								GameConfig.newTools[i][j] = new Destroyable(new Vector3(x, -4.8f, z),plantMoney, Objects.PLANT);
								break;
					
					case 4:		float lockerMoney = Math.abs((rand.nextInt()%5)) +1;
								GameConfig.newTools[i][j] = new Destroyable(new Vector3(x, -4.8f, z), lockerMoney, Objects.LOCKER);
								break;
					
					case 5:		float chairMoney = Math.abs(rand.nextInt()%2);		
								GameConfig.newTools[i][j] = new Destroyable(new Vector3(x, -4.8f, z),chairMoney, Objects.CHAIR);
								break; 
					
					default:	break;
				}
			}
		}
	
	private void createRoom()
	{
		Random rand = new Random(System.currentTimeMillis());

		// initialize the map
		GameConfig.newTools = new Destroyable[GameConfig.ROOM_ROW][GameConfig.ROOM_COLUMN];
		
		createWalls();
		
		//	 ^
		// i |	  j ->
		
		for (int i = 0; i < GameConfig.newTools.length; i+=2)
			for (int j = 1; j < GameConfig.newTools[i].length-1; j++)
			{
				float x = i*GameConfig.CELL_HEIGHT + GameConfig.ROOM_ROW * GameConfig.CELL_HEIGHT * (GameConfig.level - 1);
				float z = j*GameConfig.CELL_WIDTH;
				
				// create desk and chair in the middle of map
				if( i >= 2 && i < GameConfig.newTools.length-2 && j >= 1 && j < GameConfig.newTools.length-2 && rand.nextBoolean())
				{
					float deskMoney = Math.abs(rand.nextInt()%4);
					GameConfig.newTools[i][j] = new Destroyable(new Vector3(x, -5, z), deskMoney, Objects.DESK);
					
					float chairMoney = Math.abs(rand.nextInt()%2);
					GameConfig.newTools[i+1][j] = new Destroyable(new Vector3(-1+(i+1)*GameConfig.CELL_HEIGHT + GameConfig.ROOM_ROW * GameConfig.CELL_HEIGHT * (GameConfig.level - 1), -5, z), chairMoney, Objects.CHAIR);
				}
			
				// create printer, plant and locker on the sides
				int r = 0;
				if(i==0 || i == GameConfig.newTools.length-1 || j == GameConfig.newTools.length-1)
					r = Math.abs(rand.nextInt())%8;
				
				if(r == 1 || r == 2)
				{
					float printerMoney = Math.abs((rand.nextInt()%6)) +1;
					GameConfig.newTools[i][j] = new Destroyable(new Vector3(x , -5, z+2), printerMoney, Objects.PRINTER);
				}
				else if(r == 3 || r == 4)
				{
					float plantMoney = Math.abs((rand.nextInt()%5)) +1;
					GameConfig.newTools[i][j] = new Destroyable(new Vector3(x, -5, z), plantMoney, Objects.PLANT);
				}
				else if (r == 5 || r ==6)	
				{
					float lockerMoney = Math.abs((rand.nextInt()%5)) +1;
					GameConfig.newTools[i][j] = new Destroyable(new Vector3(x, -5, z), lockerMoney, Objects.LOCKER);			
				}
			}
	
		// clear positions near the doors
		GameConfig.newTools[0][GameConfig.ROOM_COLUMN/2 +1] = null;
		GameConfig.newTools[0][GameConfig.ROOM_COLUMN/2] = null;
		GameConfig.newTools[0][GameConfig.ROOM_COLUMN/2 -1] = null;
		GameConfig.newTools[GameConfig.ROOM_ROW -1][GameConfig.ROOM_COLUMN/2 +1] = null;
		GameConfig.newTools[GameConfig.ROOM_ROW -1][GameConfig.ROOM_COLUMN/2] = null;
		GameConfig.newTools[GameConfig.ROOM_ROW -1][GameConfig.ROOM_COLUMN/2 -1] = null;
		
		// create the clock
		int w = Math.abs(rand.nextInt()% (GameConfig.ROOM_ROW-6))+3;

			// 	choose side of clock (right or left)
		int h = rand.nextBoolean() ? 0 : GameConfig.ROOM_COLUMN -1;
		
		float x = w * GameConfig.CELL_HEIGHT + GameConfig.ROOM_ROW * GameConfig.CELL_HEIGHT * (GameConfig.level - 1);
		float z = h * GameConfig.CELL_WIDTH;
		
		float clockMoney = Math.abs((rand.nextInt()%3)) +1;
		GameConfig.newTools[w][h] = new Destroyable(new Vector3(x -1.5f, -3f, z), clockMoney, Objects.CLOCK); 			
		
		// 	create door
		GameConfig.newTools[0][GameConfig.ROOM_COLUMN/2] = new Destroyable(new Vector3(-3.8f + GameConfig.ROOM_ROW * GameConfig.CELL_HEIGHT,
				-5, 2+GameConfig.ROOM_COLUMN*GameConfig.CELL_WIDTH/2), 0, Objects.DOOR);

		// load tools model
		assets.loadTools();
		
		// add new tools to tools
		upgrade();				
	}

	
	// clone newTools and newInstances and add them to tools and toolsInstance
	@SuppressWarnings("unchecked")
	public void upgrade()
	{
		GameConfig.level++;
		synchronized(GameConfig.tools)
		{
			Destroyable[][] array = (Destroyable[][]) GameConfig.newTools.clone();
			GameConfig.tools.add(array);
			
			if(GameConfig.actualLevel >= 3)
				GameConfig.tools.remove(GameConfig.actualLevel-3);
		}
		
		synchronized(GameConfig.toolsInstance)
		{
			ArrayList<ModelInstance> array = (ArrayList<ModelInstance>) GameConfig.newInstances.clone();
			GameConfig.toolsInstance.add(array);
		
			if(GameConfig.actualLevel >= 3)
				GameConfig.toolsInstance.get(GameConfig.actualLevel-3).clear();
		}
		GameConfig.newInstances.clear();
	}
}
