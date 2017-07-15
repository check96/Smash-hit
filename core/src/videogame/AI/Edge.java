package videogame.AI;

public class Edge
{
    public final Vertex target;
    public final float weight;
    
    public Edge(Vertex _target, float argWeight)
    {
    	target = _target;
    	weight = argWeight;
    }
    
    @Override
    public String toString()
    {
    	return ("(" + target.x + ","+target.y + ")  "+weight);
    }
}
