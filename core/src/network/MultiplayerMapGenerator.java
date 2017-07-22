package network;

import java.util.Random;
import com.badlogic.gdx.math.Vector3;

import entity.Destroyable;
import entity.Objects;
import entity.Wall;
import entity.Walls;
import network.Screen.MultiplayerLobby;
import network.packets.LoadPacket;
import videogame.GameConfig;

public class MultiplayerMapGenerator
{
	public MultiplayerAssetsHandler assets;
	
	public MultiplayerMapGenerator()
	{
		assets = new MultiplayerAssetsHandler();
		assets.loadModels();
	}
	
		
	private void createWalls()
	{
		//create left wall
		GameConfig.walls.add(new Wall(new Vector3(-5 + GameConfig.ROOM_ROW * GameConfig.CELL_HEIGHT/2,0,-5), Walls.LEFT_WALL));
	
		//create right wall
		GameConfig.walls.add(new Wall(new Vector3(-5 + GameConfig.ROOM_ROW * GameConfig.CELL_HEIGHT/2, 0, -2.5f+GameConfig.ROOM_COLUMN*GameConfig.CELL_WIDTH), Walls.RIGHT_WALL));
		
		//create back wall
		GameConfig.walls.add(new Wall(new Vector3(-4.3f , 0 , -2 + GameConfig.ROOM_COLUMN * GameConfig.CELL_WIDTH/2), Walls.BACK_WALL));
		
		//create front upper wall
		GameConfig.walls.add(new Wall(new Vector3(-5 + GameConfig.ROOM_ROW * GameConfig.CELL_HEIGHT, 0,-2 +GameConfig.ROOM_COLUMN * GameConfig.CELL_WIDTH/2), Walls.FOREWARD_UPPER_WALL));
		
		//create ceiling
		GameConfig.walls.add(new Wall(new Vector3(GameConfig.ROOM_ROW * GameConfig.CELL_HEIGHT/2, 10.6f, GameConfig.ROOM_COLUMN * GameConfig.CELL_WIDTH/2),Walls.CEILING));
		
		//create floor
		GameConfig.walls.add(new Wall(new Vector3((GameConfig.ROOM_ROW * GameConfig.CELL_HEIGHT/2), -5, -2+GameConfig.ROOM_COLUMN*GameConfig.CELL_WIDTH/2),Walls.FLOOR));
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

		// load tools model
		assets.loadTools();
	}

	private void uploadTools(int[][] map)
	{		
		// create walls
		createWalls();
		
		//	load map and create tools
		for(int i = 0; i < map.length; i++)
			for(int j = 0; j < map[i].length; j++)
			{
				float x = i*GameConfig.CELL_HEIGHT;
				float z = j*GameConfig.CELL_WIDTH;
				switch (map[i][j])
				{
					case 1:		GameConfig.multiplayerMap[i][j] = new Destroyable(new Vector3(x-1, -5, z+1), 0, Objects.DESK);
								break;

					case 2:		GameConfig.multiplayerMap[i][j] = new Destroyable(new Vector3(x, -5, z), 0, Objects.PLANT);
								break;
					
					case 3:		GameConfig.multiplayerMap[i][j] = new Destroyable(new Vector3(x, -5, z), 0, Objects.LOCKER);
								break;
					
					case 4:		GameConfig.multiplayerMap[i][j] = new Destroyable(new Vector3(x-2, -5, z), 0, Objects.CHAIR);
								break; 
					
					default:	break;
				}
			}
	}
	
	public void createRoom()
	{
		Random rand = new Random(System.currentTimeMillis());

		// initialize the map
		createWalls();

		for (int i = 0; i < GameConfig.multiplayerMap.length; i+=2)
			for (int j = 1; j < GameConfig.multiplayerMap[i].length-1; j++)
			{
				float x = i*GameConfig.CELL_HEIGHT;
				float z = j*GameConfig.CELL_WIDTH;
				
				// create desk and chair in the middle of map
				if( i >= 2 && i < GameConfig.multiplayerMap.length-2 && j >= 1 && j < GameConfig.multiplayerMap.length-2 && rand.nextBoolean())
				{
					GameConfig.multiplayerMap[i][j] = new Destroyable(new Vector3(x-1, -5, z+1), 0, Objects.DESK);
					
					GameConfig.multiplayerMap[i+1][j] = new Destroyable(new Vector3(-2+(i+1)*GameConfig.CELL_HEIGHT, -5, z), 0, Objects.CHAIR);
				}
			
				// create printer, plant and locker on the sides
				int r = 0;
				if(i==0 || i == GameConfig.multiplayerMap.length-1 || j == GameConfig.multiplayerMap.length-1)
					r = Math.abs(rand.nextInt())%6;
				if(r == 1 || r == 2)
					GameConfig.multiplayerMap[i][j] = new Destroyable(new Vector3(x, -5, z), 0, Objects.PLANT);
				else if (r == 3 || r == 4)	
					GameConfig.multiplayerMap[i][j] = new Destroyable(new Vector3(x, -5, z), 0, Objects.LOCKER);			
			}
	
		// load tools model
		assets.loadTools();
		
		String send = "";
		
		for(int i = 0; i < GameConfig.ROOM_ROW; i++)
			for(int j = 0; j < GameConfig.ROOM_COLUMN; j++)
			{
				if(GameConfig.multiplayerMap[i][j] instanceof Destroyable)
					send += Integer.toString(GameConfig.multiplayerMap[i][j].type.id);
				else
					send += "0";
			}
		
		MultiplayerLobby.loadPacket = new LoadPacket(send);
	}
}