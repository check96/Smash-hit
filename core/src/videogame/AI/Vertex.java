package videogame.AI;

import java.util.ArrayList;

public class Vertex implements Comparable<Vertex>
{
    public final int x, y;
    public ArrayList<Edge> adjacencies;
    public float minDistance = Float.POSITIVE_INFINITY;
    public Vertex previous;
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
