package network.threads;

import videogame.GameConfig;

public class ServerLaunchThread extends Thread
{
	
	public ServerLaunchThread()	{ }
	
	@Override
	public void run()
	{
		GameConfig.server.launch();
	}
}
