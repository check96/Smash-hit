package videogame.AI;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import entity.Objects;
import videogame.GameConfig;

public class Dijkstra
{
	private static int startX = GameConfig.ROOM_ROW-1;
	private static int startY = GameConfig.ROOM_COLUMN/2;
	private static int clockX = -1;
	private static int clockY = -1;
	private static int endX;
	private static int endY; 
	private static Vertex start = null;
	private static Vertex end = null;
	private static Vertex clock = null;
	private static ArrayList<Vertex> vertex;
	
    public static void computePaths(Vertex source)
    {
        source.minDistance = 0;
        PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
	    vertexQueue.add(source);
	
	    while (!vertexQueue.isEmpty())
	    {
	        Vertex u = vertexQueue.poll();
	        
            // Visit each edge exiting u
            for (Edge e : u.adjacencies)
            {
                Vertex v = e.target;
                float distanceThroughU = u.minDistance + e.weight;
		        if (distanceThroughU < v.minDistance)
		        {
		            vertexQueue.remove(v);
		
		            v.minDistance = distanceThroughU ;
		            v.previous = u;
		            vertexQueue.add(v);
		        }
            }
        }
    }

    public static List<Vertex> getShortestPath()
    {
    	createMap();
    	List<Vertex> path = new ArrayList<Vertex>();

    	computePaths(start);
    	if(clock != null)
        {
    	
    		for (Vertex vertex = clock; vertex != null; vertex = vertex.previous)
	            path.add(vertex);
    		
            path.remove(path.size()-1);
	        computePaths(clock);
	        
	        double door_clock = Math.sqrt(Math.pow((clock.x-start.x),2)+Math.pow((clock.y-start.y),2));
	        double player_clock = Math.sqrt(Math.pow((clock.x-end.x),2)+Math.pow((clock.y-end.y),2));
	        double door_player = Math.sqrt(Math.pow((end.x-start.x),2)+Math.pow((end.y-start.y),2));
	        
//	        if(door_clock + player_clock > door_player*3.5) 
//	        	path.clear();
        }
    	
        for (Vertex vertex = end; vertex != null; vertex = vertex.previous)
            path.add(vertex);
       
//        System.out.println("Distance from "+ start.toString() + " to " + end.toString() + " is: "+ end.minDistance);
//        System.out.println("Path: " + path);
//        System.out.println();
       return path;
    }
    
    private static void createMap()
    {
		if(GameConfig.player.getX() < 0)
			endX = 0;
		else
			endX = (int) ((GameConfig.player.getX() / GameConfig.CELL_HEIGHT) +1 )% GameConfig.ROOM_ROW;
		
		endY = (int) (GameConfig.player.getZ() / GameConfig.CELL_WIDTH) % GameConfig.ROOM_COLUMN;
    	
        // mark all the vertices
		float[][] map = new float[GameConfig.ROOM_ROW][GameConfig.ROOM_COLUMN];
		
		for (int i = 0; i < map.length; i++)
			for (int j = 0; j < map[i].length; j++)
			{
				map[i][j] = 2;
				if(GameConfig.tools.get(GameConfig.actualLevel-1)[i][j] != null)
				{
					if(GameConfig.tools.get(GameConfig.actualLevel-1)[i][j].getMoneyReward() != 0)
						map[i][j] = 1 / GameConfig.tools.get(GameConfig.actualLevel-1)[i][j].getMoneyReward();
					
					if(GameConfig.tools.get(GameConfig.actualLevel-1)[i][j].type == Objects.CLOCK)
					{
						clockX = (int) ((GameConfig.tools.get(GameConfig.actualLevel-1)[i][j].getX() + 4.5f)/ GameConfig.CELL_HEIGHT) % GameConfig.ROOM_ROW;
			    		clockY = (int) ((GameConfig.tools.get(GameConfig.actualLevel-1)[i][j].getZ() + 3.5f) / GameConfig.CELL_WIDTH) % GameConfig.ROOM_COLUMN;
					}
				}
			}
	
		vertex = new ArrayList<Vertex>();

		for (int i = 0; i < map.length; i++)
			for (int j = 0; j < map[i].length; j++)
				vertex.add(new Vertex(i,j));
		
		// create adjacencies
		for (int i = 0; i < vertex.size(); i++)
		{
			Vertex v = vertex.get(i);
			for (int j = 0; j < vertex.size(); j++)
			{
				Vertex u = vertex.get(j);
				
				if(u.y == v.y-1 && u.x == v.x)
					v.adjacencies.add(new Edge(u, map[u.x][u.y]));
				if(u.x == v.x-1 && u.y == v.y-1)
					v.adjacencies.add(new Edge(u,map[u.x][u.y]));
				if(u.x == v.x-1 && u.y == v.y)
					v.adjacencies.add(new Edge(u,map[u.x][u.y]));
				if(u.x == v.x-1 && u.y == v.y+1)
					v.adjacencies.add(new Edge(u,map[u.x][u.y]));
				if(u.x == v.x && u.y == v.y+1)
					v.adjacencies.add(new Edge(u,map[u.x][u.y]));
			}	
			
			if(v.x == startX && v.y == startY)
				start = v;
			else if(v.x == endX && v.y == endY)
				end = v;
			else if(v.x == clockX && v.y == clockY)
				clock = v;
		}
    }
    
    
}