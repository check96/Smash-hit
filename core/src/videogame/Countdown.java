package videogame;

public class Countdown extends Thread
{
	private static int initial_time = 500;
	private static int time;
	private static boolean isRunning = true;
	
	public Countdown()
	{ 
		time = initial_time;
	}
	
	public static int getTime() {return time;}
	
	public void run()
	{
		while(true)
		{
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
	
//	public synchronized void restart()	{isRunning = true;}	
//	public statisynchronized void pause()  { isRunning = false;}

	public static synchronized void reset() 
	{
		time = initial_time+1;
//		isRunning = true;
	}
}
