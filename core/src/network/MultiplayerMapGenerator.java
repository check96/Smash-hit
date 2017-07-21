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
	private final int ROW;
	private final int COLUMN;
	
	public MultiplayerMapGenerator()
	{
		ROW = GameConfig.ROW;
		COLUMN = GameConfig.COLUMN;
		
		assets = new MultiplayerAssetsHandler();
		assets.loadModels();
	}
	
	private void createWalls()
	{
		synchronized(GameConfig.walls)
		{
			//create left wall
			GameConfig.walls.add(new Wall(new Vector3((-5 + ROW * GameConfig.CELL_HEIGHT/2),0,-5), Walls.LEFT_WALL));
		
			//create right wall
			GameConfig.walls.add(new Wall(new Vector3(-5 + ROW * GameConfig.CELL_HEIGHT/2, 0, -2.5f  + COLUMN*GameConfig.CELL_WIDTH), Walls.RIGHT_WALL));
			
			//create back wall
			GameConfig.walls.add(new Wall(new Vector3(-4.3f + ROW * GameConfig.CELL_HEIGHT + ROW * GameConfig.CELL_HEIGHT,0, -2 + COLUMN * GameConfig.CELL_WIDTH/2), Walls.BACK_WALL));
			
			//create front upper wall
			GameConfig.walls.add(new Wall(new Vector3(-5 + ROW * GameConfig.CELL_HEIGHT, 17.4f, -2 + COLUMN * GameConfig.CELL_WIDTH/2), Walls.FOREWARD_UPPER_WALL));
			
			//create front left wall
			GameConfig.walls.add(new Wall(new Vector3(-5 + ROW*GameConfig.CELL_HEIGHT, 0, -52 + COLUMN * GameConfig.CELL_WIDTH/2), Walls.FOREWARD_LEFT_WALL));
			
			//create front right wall
			GameConfig.walls.add(new Wall(new Vector3(-5 + ROW*GameConfig.CELL_HEIGHT,0,54 + COLUMN * GameConfig.CELL_WIDTH/2), Walls.FOREWARD_RIGHT_WALL));

			//create ceiling
			GameConfig.walls.add(new Wall(new Vector3(GameConfig.ROOM_ROW * GameConfig.CELL_HEIGHT/2, 10.6f, COLUMN * GameConfig.CELL_WIDTH/2),Walls.CEILING));
			
			//create floor
			GameConfig.walls.add(new Wall(new Vector3(ROW * GameConfig.CELL_HEIGHT/2, -5, -2 + COLUMN*GameConfig.CELL_WIDTH/2),Walls.FLOOR));
		}
	}

	public void loadRoom(String line)
	{
		int[][] points = new int[GameConfig.ROW][GameConfig.COLUMN];

		for(int j=0; j < ROW; j++)
		{
			String subLine = line.substring(j * COLUMN, j * COLUMN + COLUMN);

			for(int k = 0; k < COLUMN; k++)
				points[j][k] = subLine.charAt(k) - 48;
		}
		
		uploadTools(points);

		// clear positions near the doors
		GameConfig.multiplayerMap[0][COLUMN/2 +1] = null;
		GameConfig.multiplayerMap[0][COLUMN/2] = null;
		GameConfig.multiplayerMap[0][COLUMN/2 -1] = null;
		GameConfig.multiplayerMap[ROW -1][COLUMN/2 +1] = null;
		GameConfig.multiplayerMap[ROW -1][COLUMN/2] = null;
		GameConfig.multiplayerMap[ROW -1][COLUMN/2 -1] = null;
		
		// load tools model
		assets.loadTools();
	}

	private void uploadTools(int[][] map)
	{		
		// create walls
		createWalls();
		
		//		load map and create tools
		Random rand = new Random(System.currentTimeMillis());
		
		for(int i = 0; i < map.length; i++)
			for(int j = 0; j < map[i].length; j++)
			{
				float x = i*GameConfig.CELL_HEIGHT;
				float z = j*GameConfig.CELL_WIDTH;
				switch (map[i][j])
				{
					case 1:		float deskMoney = Math.abs(rand.nextInt()%4);
								GameConfig.multiplayerMap[i][j] = new Destroyable(new Vector3(x-1, -5, z+1), deskMoney, Objects.DESK);
								break;

					case 2:		float plantMoney = Math.abs((rand.nextInt()%5)) +1;
								GameConfig.multiplayerMap[i][j] = new Destroyable(new Vector3(x, -5, z), plantMoney, Objects.PLANT);
								break;
					
					case 3:		float lockerMoney = Math.abs((rand.nextInt()%6)) +1;
								GameConfig.multiplayerMap[i][j] = new Destroyable(new Vector3(x, -5, z), lockerMoney, Objects.LOCKER);
								break;
					
					case 4:		float chairMoney = Math.abs(rand.nextInt()%2) +1;
								GameConfig.multiplayerMap[i][j] = new Destroyable(new Vector3(x-2, -5, z), chairMoney, Objects.CHAIR);
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

		for (int i = 0; i < GameConfig.multiplayerMap.length; i+=3)
			for (int j = 2 ; j < GameConfig.multiplayerMap[i].length-2; j++)
			{
				float x = i * GameConfig.CELL_HEIGHT;
				float z = j * GameConfig.CELL_WIDTH;
				
				// create desk and chair in the middle of map
				if( i >= 4 && i < GameConfig.multiplayerMap.length-4 && j >= 3 && j < GameConfig.multiplayerMap.length-4 && rand.nextBoolean())
				{
					float deskMoney = Math.abs(rand.nextInt()%4);
					GameConfig.multiplayerMap[i][j] = new Destroyable(new Vector3(x-1, -5, z+1), deskMoney, Objects.DESK);
					
					float chairMoney = Math.abs(rand.nextInt()%2);
					GameConfig.multiplayerMap[i+1][j] = new Destroyable(new Vector3(-2+(i+1)*GameConfig.CELL_HEIGHT + ROW * GameConfig.CELL_HEIGHT, -5, z), chairMoney, Objects.CHAIR);
				}
			
				// create printer, plant and locker on the sides
				int r = 0;
				if(i==0 || i == GameConfig.multiplayerMap.length-1 || j == GameConfig.multiplayerMap.length-1)
					r = Math.abs(rand.nextInt())%6;
				if(r == 1 || r == 2)
				{
					float plantMoney = Math.abs((rand.nextInt()%5)) +1;
					GameConfig.multiplayerMap[i][j] = new Destroyable(new Vector3(x, -5, z), plantMoney, Objects.PLANT);
				}
				else if (r == 3 || r == 4)	
				{
					float lockerMoney = Math.abs((rand.nextInt()%6)) +1;
					GameConfig.multiplayerMap[i][j] = new Destroyable(new Vector3(x, -5, z), lockerMoney, Objects.LOCKER);			
				}
			}
	
		// clear positions near the doors
		GameConfig.multiplayerMap[0][COLUMN/2 +1] = null;
		GameConfig.multiplayerMap[0][COLUMN/2] = null;
		GameConfig.multiplayerMap[0][COLUMN/2 -1] = null;
		GameConfig.multiplayerMap[ROW -1][COLUMN/2 +1] = null;
		GameConfig.multiplayerMap[ROW -1][COLUMN/2] = null;
		GameConfig.multiplayerMap[ROW -1][COLUMN/2 -1] = null;
		
//	 	create door
		GameConfig.multiplayerMap[ROW-1][COLUMN/2] = new Destroyable(new Vector3( -5 + ROW * GameConfig.CELL_HEIGHT, -5 ,
				1.5f + COLUMN * GameConfig.CELL_WIDTH/2),0, Objects.DOOR);	
		
		String send = "";
		for(int i = 0; i < GameConfig.multiplayerMap.length; i++)
			for(int j = 0; j < GameConfig.multiplayerMap[i].length; j++)
				if(GameConfig.multiplayerMap[i][j] == null)
					send += "0";
				else
					send += Integer.toString(GameConfig.multiplayerMap[i][j].type.id);
		
		MultiplayerLobby.loadPacket = new LoadPacket(send);

		// load tools model
		assets.loadTools();
	}
}

