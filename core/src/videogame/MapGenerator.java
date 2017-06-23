package videogame;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

import GameGui.AssetHandler;
import editor.PreviewPanel;
import entity.Destroyable;
import entity.Objects;

public class MapGenerator extends Thread
{
	private AssetHandler assets;
	
	public MapGenerator(AssetHandler _assets)
	{
		assets = _assets;
		
		GameConfig.walls.add(new Destroyable(new Vector3(-5f,0,15),0,Objects.TOP_WALL));
		GameConfig.walls.add(new Destroyable(new Vector3(-5f,0,GameConfig.ROOM_DIMENSION*4.5f),0,Objects.TOP_WALL));
		GameConfig.walls.add(new Destroyable(new Vector3(-5f,0,GameConfig.ROOM_DIMENSION*2.5f),0,Objects.TOP_WALL));
		GameConfig.walls.add(new Destroyable(new Vector3(-5f,7.5f, GameConfig.ROOM_DIMENSION *2.6f),0,Objects.HIGH_WALL));
	}
	
	public void run()
	{
		while(true)
		{
			Random rand = new Random(System.currentTimeMillis());

			// initialize the map
			GameConfig.newTools = new Destroyable[GameConfig.ROOM_DIMENSION][GameConfig.ROOM_DIMENSION];
			
			if(!GameConfig.EDITOR)
			{ 
				float dimension = GameConfig.ROOM_DIMENSION * 5.5f;		//82.5
				float position = dimension * (GameConfig.level - 1); 
				
				// create walls
				GameConfig.walls.add(new Destroyable(new Vector3(position + 35f, 0,-4),0,Objects.VERTICAL_WALL));
				GameConfig.walls.add(new Destroyable(new Vector3(position + 35f, 0,GameConfig.ROOM_DIMENSION *5.5f),0, Objects.VERTICAL_WALL));
				GameConfig.walls.add(new Destroyable(new Vector3(-4f + dimension * GameConfig.level,0,-5+dimension*0.261f),0, Objects.TOP_WALL));
				GameConfig.walls.add(new Destroyable(new Vector3(-4f + dimension * GameConfig.level, 0,3+dimension*0.737f),0, Objects.TOP_WALL));
				GameConfig.walls.add(new Destroyable(new Vector3(-4f + dimension* GameConfig.level, 7.6f, GameConfig.ROOM_DIMENSION *2.6f),0,Objects.HIGH_WALL));
				GameConfig.walls.add(new Destroyable(new Vector3(40+position, 10.6f, GameConfig.ROOM_DIMENSION *2.6f),0, Objects.CEILING));
//					 ^
				// i |	  j ->
				
				for (int i = 0; i < GameConfig.newTools.length; i+=2)
					for (int j = 1; j < GameConfig.newTools[i].length-1; j+=2)
					{
						float x = i*5.5f + position;
						float z = j*5.5f;
						
						// create desk and chair in the middle of map
						if( i >= 2 && i < GameConfig.newTools.length-2 && j >= 1 && j < GameConfig.newTools.length-2 && rand.nextBoolean())
						{
							float deskMoney = Math.abs(rand.nextInt()%4);
							GameConfig.newTools[i][j] = new Destroyable(new Vector3(x, -4f, z), deskMoney, Objects.DESK);
							
							float chairMoney = Math.abs(rand.nextInt()%2);
							GameConfig.newTools[i+1][j] = new Destroyable(new Vector3(x, -3.5f, z), chairMoney, Objects.CHAIR);
						}
					
						// create printer, plant and locker on the sides
						int r = 0;
						if(i==0 || i == GameConfig.newTools.length-1 || j == GameConfig.newTools.length-1)
							r = Math.abs(rand.nextInt())%8;
						
						if(r == 1 || r == 2)
						{
							float printerMoney = Math.abs((rand.nextInt()%6)) +1;
							GameConfig.newTools[i][j] = new Destroyable(new Vector3(x -0.5f, -4f, z +0.3f), printerMoney, Objects.PRINTER);
						}
						else if(r == 3 || r == 4)
						{
							float plantMoney = Math.abs((rand.nextInt()%5)) +1;
							GameConfig.newTools[i][j] = new Destroyable(new Vector3(x - 2f, -10f, z - 0.5f), plantMoney, Objects.PLANT);
						}
						else if (r == 5 || r ==6)	
						{
							float lockerMoney = Math.abs((rand.nextInt()%5)) +1;
							GameConfig.newTools[i][j] = new Destroyable(new Vector3(x - 2f, -3f, z), lockerMoney, Objects.LOCKER);			
						}
					}
//					*/				
			
				// clear positions near the doors
				GameConfig.newTools[0][GameConfig.ROOM_DIMENSION/2 +1] = null;
				GameConfig.newTools[0][GameConfig.ROOM_DIMENSION/2 -1] = null;
				GameConfig.newTools[GameConfig.ROOM_DIMENSION -1][GameConfig.ROOM_DIMENSION/2 +1] = null;
				GameConfig.newTools[GameConfig.ROOM_DIMENSION -1][GameConfig.ROOM_DIMENSION/2 -1] = null;
			}
		
			// load editor map
			if(GameConfig.EDITOR)
			{
				GameConfig.EDITOR = false;
				uploadTools(PreviewPanel.points);
			}
			
			// create the clock
			int w = Math.abs(rand.nextInt()% (GameConfig.ROOM_DIMENSION-6))+3;

				// 	choose side of clock (right or left)
			int h = rand.nextBoolean() ? 0 : GameConfig.ROOM_DIMENSION -1;
			
			float x = w*5.5f + GameConfig.ROOM_DIMENSION * 5.5f * (GameConfig.level - 1);
			float z = h*5.5f;
			
			float clockMoney = Math.abs((rand.nextInt()%3)) +1;
			GameConfig.newTools[w][h] = new Destroyable(new Vector3(x -1.5f, -3f, z), clockMoney, Objects.CLOCK); 			
			
			// 	create door
			GameConfig.newTools[0][GameConfig.ROOM_DIMENSION/2] = new Destroyable(new Vector3(-3.8f + GameConfig.ROOM_DIMENSION *5.5f,
					-5,GameConfig.ROOM_DIMENSION*2.746f), 0, Objects.DOOR);

			// load tools model
			assets.loadTools();
			
			// add new tools to tools
			upgrade();
	
			try
			{
				//GameConfig.timing = System.currentTimeMillis();
				sleep(10000);
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	// ONLY for editor
	public void uploadTools(int[][] map)
	{		
		float dimension = GameConfig.ROOM_DIMENSION * 5.5f;		//82.5
		float position = dimension * (GameConfig.level - 1); 
		
		// create walls
		GameConfig.walls.add(new Destroyable(new Vector3(position + 35f, 0,-4),0,Objects.VERTICAL_WALL));
		GameConfig.walls.add(new Destroyable(new Vector3(position + 35f, 0,GameConfig.ROOM_DIMENSION *5.5f),0, Objects.VERTICAL_WALL));
		GameConfig.walls.add(new Destroyable(new Vector3(-4f + dimension * GameConfig.level,0,-5+dimension*0.261f),0, Objects.TOP_WALL));
		GameConfig.walls.add(new Destroyable(new Vector3(-4f + dimension * GameConfig.level, 0,3+dimension*0.737f),0, Objects.TOP_WALL));
		GameConfig.walls.add(new Destroyable(new Vector3(-4f + dimension* GameConfig.level, 7.6f, GameConfig.ROOM_DIMENSION *2.6f),0,Objects.HIGH_WALL));
		GameConfig.walls.add(new Destroyable(new Vector3(40+position, 10.6f, GameConfig.ROOM_DIMENSION *2.6f),0, Objects.CEILING));

//		load map and create tools
		Random rand = new Random(System.currentTimeMillis());
		for(int i = 0; i < map.length; i++)
			for(int j = 0; j < map[i].length; j++)
			{
				float x = i*5.5f + position;
				float z = j*5.5f;
				switch (map[i][j])
				{
					case 1:		float deskMoney = Math.abs(rand.nextInt()%4);	
								GameConfig.newTools[i][j] = new Destroyable(new Vector3(x, -4f, z), deskMoney, Objects.DESK);
								break;
					
					case 2: 	float printerMoney = Math.abs((rand.nextInt()%6)) +1;				
								GameConfig.newTools[i][j] = new Destroyable(new Vector3(x-0.5f, -4f, z+0.3f),printerMoney, Objects.PRINTER);
								break;
					
					case 3:		float plantMoney = Math.abs((rand.nextInt()%5)) +1;
								GameConfig.newTools[i][j] = new Destroyable(new Vector3(x - 2f, -4.5f, z - 0.5f),plantMoney, Objects.PLANT);
								break;
					
					case 4:		float lockerMoney = Math.abs((rand.nextInt()%5)) +1;
								GameConfig.newTools[i][j] = new Destroyable(new Vector3(x - 2f, -3f, z), lockerMoney, Objects.LOCKER);
								break;
					
					case 5:		float chairMoney = Math.abs(rand.nextInt()%2);		
								GameConfig.newTools[i][j] = new Destroyable(new Vector3(x + 1f, -3f, z+4),chairMoney, Objects.CHAIR);
								break; 
					
					default:	break;
				}
			}
		}
		
	@SuppressWarnings("unchecked")
	
	// clone newTools and newInstances and add them to tools and toolsInstance
	public void upgrade()
	{
		GameConfig.level++;
	/*
		for(int k = 0; k<GameConfig.newTools.length; k++)
		{	for(int h = 0; h<GameConfig.newTools[k].length; h++)
				if(GameConfig.tools.get(GameConfig.actualLevel-1)[k][h] != null)
					System.out.print(GameConfig.tools.get(GameConfig.actualLevel-1)[k][h].type+ " ");
				else
					System.out.print(0+ " ");
				
			System.out.println();
		}
	*/
		int cont = 0;
		for(int k = 0; k<GameConfig.newTools.length; k++)
			for(int h = 0; h<GameConfig.newTools[k].length; h++)
				if(GameConfig.newTools[k][h] != null)
					cont++;
		
		System.out.println("newTools size: "+ cont);
		System.out.println("newInstance size: "+GameConfig.newInstances.size());
		
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
