package GameGui;

import java.util.ListIterator;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

import videogame.GameConfig;

public class Deleter
{
	public static void remove(boolean clock, boolean vortex, Vector3 objPosition, float money)
	{
		synchronized (GameConfig.toolsInstance.get(GameConfig.actualLevel-1))
		{
			ListIterator<ModelInstance> iterator = GameConfig.toolsInstance.get(GameConfig.actualLevel-1).listIterator();
			while(iterator.hasNext())
			{
				ModelInstance instance = iterator.next();
				if(instance.transform.getTranslation(new Vector3()).equals(objPosition))
				{
					if(!clock && !vortex)
					{
						GameConfig.destroyed.add(instance);
						addCoinsAnimation(objPosition, money);
					}
					
					iterator.remove();
					break;
				}
			}
		}
	}

	public static void remove(Vector3 objPosition)
	{
		synchronized(GameConfig.multiplayerInstances)
		{
			ListIterator<ModelInstance> iterator = GameConfig.multiplayerInstances.listIterator();
		
			while(iterator.hasNext())
			{
				ModelInstance instance = iterator.next();
				if(instance.transform.getTranslation(new Vector3()).equals(objPosition))
				{
					synchronized(GameConfig.destroyed)
					{
						GameConfig.destroyed.add(instance);
					}
					
					iterator.remove();
					break;
				}
			}
		}
	}

	
	private static void addCoinsAnimation(Vector3 objPosition, float money)
	{
		for(int i = 0; i<money; i++)
		{
			ModelInstance instance = new ModelInstance(AssetHandler.coinModel);
			instance.transform.setToTranslation(objPosition);
			instance.transform.rotate(0,1,0, 90*i);
			
			GameConfig.coins.add(instance);
		}
	}
	
}
