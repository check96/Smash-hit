package videogame;

public class Countdown extends Thread
{
	private static int initial_time = 10;
	private static int time;
	public boolean active = false;
	
	public Countdown()
	{ 
		time = initial_time;
	}
	
	public static int getTime() {return time;}
	
	public void run()
	{
		while(true)
		{
			if(active)
				time--;
			
			synchronized(this)
			{
				try 
				{
					this.wait(1000);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	public static synchronized void increment(int _time)
	{
		time += _time; 
	}
	
	public static synchronized void reset() 
	{
		time = initial_time;
	}
}
