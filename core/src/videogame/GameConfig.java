package videogame;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

import entity.Destroyable;
import entity.Player;
import entity.Wall;

public class GameConfig
{
	public static int Screen_Width = 1024;
	public static int Screen_Height = 600;
	public static final int width = 1024;
	public static final int height = 600;
	public static final int ROOM_ROW = 15;
	public static final int ROOM_COLUMN = 10;
	public static final int CELL_HEIGHT = 6;
	public static final int CELL_WIDTH = 9;
	public static float volume = 1;
	public static Music gameSoundtrack = Gdx.audio.newMusic(Gdx.files.internal("music/Atlas_rise.ogg"));
	public static Music menuSoundtrack = Gdx.audio.newMusic(Gdx.files.internal("music/Given_up.ogg"));
	// sound massimo un mega
	
	public static int SCORE = 0;
	public static int COINS = 1000;
	public static int LOCAL_COINS = 1000;
	public static int actualLevel = 1;
	public static int level = 1;

	public static boolean EDITOR = false;
	public static boolean MULTIPLAYER = false;

	public static int maceLevel = 0;
	public static int vortexLevel = 0;
	public static int clockLevel = 0;
	public static int coinsMultiplier = 1;
	public static int numBomb1 = 20;
	public static int numBomb2 = 18;
	
	public static int destroyedDesks = 0;
	public static int destroyedChairs = 0;
	public static int destroyedDoors = 0;
	public static int destroyedPlants = 0;
	public static int destroyedLockers = 0;
	public static int destroyedObjects = 0;
	
	public static final float GRAVITY = 9.81f;
	public static Vector3 DIRECTION = new Vector3(90,0,90);
	public static String STATE = "hit";
	public static int stateIndex = 0;
	
	public static boolean GAME_OVER = false;
	public static boolean RIGHT = false;
	public static boolean LEFT  = false;
	public static boolean ON    = false;
	public static boolean BACK  = false;
	public static boolean HIT	= false;
	
	public static Player player; 
	public static ArrayList<Player> players = new ArrayList<Player>();
	public static ArrayList<Destroyable[][]> tools = new ArrayList<Destroyable[][]>();
	public static ArrayList<Wall> walls  = new ArrayList<Wall>();
	public static Destroyable[][] newTools = new Destroyable[ROOM_ROW][ROOM_COLUMN];
	
	public static ArrayList<ModelInstance> destroyed = new ArrayList<ModelInstance>();
	public static ArrayList<ModelInstance> coins = new ArrayList<ModelInstance>();
	public static ArrayList<ArrayList<ModelInstance>> toolsInstance  = new ArrayList<ArrayList<ModelInstance>>();
	public static ArrayList<ModelInstance> newInstances  = new ArrayList<ModelInstance>();
	public static ArrayList<ModelInstance> wallsInstance  = new ArrayList<ModelInstance>();
	
	public static void reset()
	{
		DIRECTION = new Vector3(90,0,90);
		level = 1;
		actualLevel = 1;
		SCORE = 0;
		LOCAL_COINS = 0;
		
		EDITOR = false;
		MULTIPLAYER = false;
		
		GAME_OVER = false;
		RIGHT = false;
		LEFT = false;
		ON = false;
		BACK  = false;
		HIT	= false;
		
		STATE = "hit";
		stateIndex = 0;

		destroyedDesks = 0;
		destroyedChairs = 0;
		destroyedDoors = 0;
		destroyedPlants = 0;
		destroyedLockers = 0;
		destroyedObjects = 0;
		
		Countdown.reset();
		tools.clear();
		tools = new ArrayList<Destroyable[][]>();
		newTools = new Destroyable[ROOM_ROW][ROOM_COLUMN];
		
		toolsInstance.clear();
		toolsInstance  = new ArrayList<ArrayList<ModelInstance>>();
		newInstances.clear();
		newInstances  = new ArrayList<ModelInstance>();
		
		walls.clear();
		walls  = new ArrayList<Wall>();
		wallsInstance.clear();
		wallsInstance  = new ArrayList<ModelInstance>();
	}
}