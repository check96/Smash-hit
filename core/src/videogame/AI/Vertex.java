package videogame.AI;

import java.util.ArrayList;

public class Vertex implements Comparable<Vertex>
{
    public int x, y;
    public float X,Z;
    public ArrayList<Edge> adjacencies;
    public float minDistance = Float.POSITIVE_INFINITY;
    public Vertex previous;
    
    public Vertex(float x, float z)
    {
    	adjacencies = null;
    	this.X = x;
    	this.Z = z;
    }
    
    public Vertex(int x, int y)
    {
    	adjacencies = new ArrayList<Edge>();
    	this.x = x;
    	this.y = y;
    }
    
    public String toString() { return ("(" + x + "," + y +") "); }
    
    public int compareTo(Vertex other)
    {
        return Float.compare(minDistance, other.minDistance);
    }
}
