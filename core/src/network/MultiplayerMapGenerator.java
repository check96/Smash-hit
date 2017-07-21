package network;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

import GameGui.AssetHandler;
import entity.Destroyable;
import entity.Objects;
import entity.Wall;
import entity.Walls;
import network.Screen.MultiplayerLobby;
import network.packet.LoadPacket;
import videogame.GameConfig;

public class MultiplayerMapGenerator extends Thread
{
	public AssetHandler assets;
	public boolean active = false;

	public MultiplayerMapGenerator()
	{
		assets = new AssetHandler();
		active = false;
		assets.loadModels();
	}
	
	public void run()
	{
		while(true)
		{
			if(active && GameConfig.isServer)
				createRoom();

			synchronized(this)
			{
				try 
				{
					this.wait(5000);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	private void createWalls()
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
			
			//create front upper wall
			GameConfig.walls.add(new Wall(new Vector3((-5 +GameConfig.ROOM_ROW*GameConfig.CELL_HEIGHT)+ position,17.4f,-2 +GameConfig.ROOM_COLUMN * GameConfig.CELL_WIDTH/2), Walls.FOREWARD_UPPER_WALL));
			
			//create front left wall
			GameConfig.walls.add(new Wall(new Vector3((-5 +GameConfig.ROOM_ROW*GameConfig.CELL_HEIGHT)+ position,0,-52 +GameConfig.ROOM_COLUMN * GameConfig.CELL_WIDTH/2), Walls.FOREWARD_LEFT_WALL));
			
			//create front right wall
			GameConfig.walls.add(new Wall(new Vector3((-5 +GameConfig.ROOM_ROW*GameConfig.CELL_HEIGHT)+ position,0,54 +GameConfig.ROOM_COLUMN * GameConfig.CELL_WIDTH/2), Walls.FOREWARD_RIGHT_WALL));

			//create ceiling
			GameConfig.walls.add(new Wall(new Vector3((GameConfig.ROOM_ROW * GameConfig.CELL_HEIGHT/2)+position, 10.6f, GameConfig.ROOM_COLUMN * GameConfig.CELL_WIDTH/2),Walls.CEILING));
			
			//create floor
			GameConfig.walls.add(new Wall(new Vector3((GameConfig.ROOM_ROW * GameConfig.CELL_HEIGHT/2)+position,-5, -2+GameConfig.ROOM_COLUMN*GameConfig.CELL_WIDTH/2),Walls.FLOOR));
		}
	}

	public void loadRoom(String line)
	{
		int[][] points = new int[GameConfig.ROOM_ROW][GameConfig.ROOM_COLUMN];
		
		for(int j=0; j<points.length; j++)
		{
			String subLine = line.substring(j * GameConfig.ROOM_COLUMN, j * GameConfig.ROOM_COLUMN + GameConfig.ROOM_COLUMN);
			
			for(int k=0; k < points[j].length; k++)
				points[j][k] = subLine.charAt(k) - 48;
		}

		uploadTools(points);

		// clear positions near the doors
		GameConfig.newTools[0][GameConfig.ROOM_COLUMN/2 +1] = null;
		GameConfig.newTools[0][GameConfig.ROOM_COLUMN/2] = null;
		GameConfig.newTools[0][GameConfig.ROOM_COLUMN/2 -1] = null;
		GameConfig.newTools[GameConfig.ROOM_ROW -1][GameConfig.ROOM_COLUMN/2 +1] = null;
		GameConfig.newTools[GameConfig.ROOM_ROW -1][GameConfig.ROOM_COLUMN/2] = null;
		GameConfig.newTools[GameConfig.ROOM_ROW -1][GameConfig.ROOM_COLUMN/2 -1] = null;
		
		// 	create door
		GameConfig.newTools[0][GameConfig.ROOM_COLUMN/2] = new Destroyable(new Vector3(-5f+GameConfig.ROOM_ROW * GameConfig.CELL_HEIGHT,
				-5, 1.5f+GameConfig.ROOM_COLUMN*GameConfig.CELL_WIDTH/2), 0, Objects.DOOR);

		// load tools model
		assets.loadTools();
		
		// add new tools to tools
		upgrade();				
	}

	private void uploadTools(int[][] map)
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
								GameConfig.newTools[i][j] = new Destroyable(new Vector3(x-1, -5, z+1), deskMoney, Objects.DESK);
								break;

					case 2:		float plantMoney = Math.abs((rand.nextInt()%5)) +1;
								GameConfig.newTools[i][j] = new Destroyable(new Vector3(x, -5, z), plantMoney, Objects.PLANT);
								break;
					
					case 3:		float lockerMoney = Math.abs((rand.nextInt()%6)) +1;
								GameConfig.newTools[i][j] = new Destroyable(new Vector3(x, -5, z), lockerMoney, Objects.LOCKER);
								break;
					
					case 4:		float chairMoney = Math.abs(rand.nextInt()%2) +1;
								GameConfig.newTools[i][j] = new Destroyable(new Vector3(x-2, -5, z), chairMoney, Objects.CHAIR);
								break; 
					
					default:	break;
				}
			}
	}
	
	public void createRoom()
	{
		Random rand = new Random(System.currentTimeMillis());

		// initialize the map
		GameConfig.newTools = new Destroyable[GameConfig.ROOM_ROW][GameConfig.ROOM_COLUMN];
		createWalls();

		for (int i = 0; i < GameConfig.newTools.length; i+=2)
			for (int j = 1; j < GameConfig.newTools[i].length-1; j++)
			{
				float x = i*GameConfig.CELL_HEIGHT + GameConfig.ROOM_ROW * GameConfig.CELL_HEIGHT * (GameConfig.level - 1);
				float z = j*GameConfig.CELL_WIDTH;
				
				// create desk and chair in the middle of map
				if( i >= 2 && i < GameConfig.newTools.length-2 && j >= 1 && j < GameConfig.newTools.length-2 && rand.nextBoolean())
				{
					float deskMoney = Math.abs(rand.nextInt()%4);
					GameConfig.newTools[i][j] = new Destroyable(new Vector3(x-1, -5, z+1), deskMoney, Objects.DESK);
					
					float chairMoney = Math.abs(rand.nextInt()%2);
					GameConfig.newTools[i+1][j] = new Destroyable(new Vector3(-2+(i+1)*GameConfig.CELL_HEIGHT + GameConfig.ROOM_ROW * GameConfig.CELL_HEIGHT * (GameConfig.level - 1), -5, z), chairMoney, Objects.CHAIR);
				}
			
				// create printer, plant and locker on the sides
				int r = 0;
				if(i==0 || i == GameConfig.newTools.length-1 || j == GameConfig.newTools.length-1)
					r = Math.abs(rand.nextInt())%6;
				if(r == 1 || r == 2)
				{
					float plantMoney = Math.abs((rand.nextInt()%5)) +1;
					GameConfig.newTools[i][j] = new Destroyable(new Vector3(x, -5, z), plantMoney, Objects.PLANT);
				}
				else if (r == 3 || r == 4)	
				{
					float lockerMoney = Math.abs((rand.nextInt()%6)) +1;
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
		
//	 	create door
		GameConfig.newTools[GameConfig.ROOM_ROW-1][GameConfig.ROOM_COLUMN/2] = new Destroyable(new Vector3(
		-5 + GameConfig.ROOM_ROW * GameConfig.CELL_HEIGHT* (GameConfig.level),-5,1.5f+GameConfig.ROOM_COLUMN*GameConfig.CELL_WIDTH/2),0, Objects.DOOR);	
		
		String send = "";
		for(int i = 0; i < GameConfig.newTools.length; i++)
			for(int j = 0; j < GameConfig.newTools[i].length; j++)
				if(GameConfig.newTools[i][j] == null)
					send += "0";
				else
					send += Integer.toString(GameConfig.newTools[i][j].type.id);
		
		if(GameConfig.tools.isEmpty())
			MultiplayerLobby.loadPacket = new LoadPacket(send);
		else
			GameConfig.server.sendData(new LoadPacket(send));

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
				GameConfig.tools.set(GameConfig.actualLevel-3,null);
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

