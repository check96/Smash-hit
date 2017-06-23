package GameGui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;

import videogame.Countdown;
import videogame.AI.Dijkstra;
import videogame.GameConfig;
import videogame.MapGenerator;
import videogame.AI.Vertex;
import videogame.World;

public class GameScreen implements Screen 
{
	private GameManager game;
	private Camera cam;
	private ModelBatch batch;
	public static ArrayList<ModelInstance> playersInstance;
	private ArrayList<ModelInstance> hints;
	private AssetHandler assets;
	private Environment environment;
	private World world;
	private int degrees = 90;
	private Hud hud;
	private MapGenerator mapGenerator;
	private AnimationController animationController;
	private int id;
	private static Countdown countdown = new Countdown();
	
	public GameScreen(GameManager _game, int _id, AssetHandler _assets, MapGenerator mg)
	{
		game = _game;
		this.id = _id;
		this.assets = _assets;
		this.mapGenerator = mg;
		
		playersInstance = new ArrayList<ModelInstance>();
		world = new World(id);
		batch = new ModelBatch();
		initCamera();
		initEnvironment();
		assets.loadPlayer();
		
		animationController = new AnimationController(playersInstance.get(0));
		animationController.setAnimation("Armature|ArmatureAction",-1);
		
		hud = new Hud();
	}

	private void initCamera() 
	{
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(GameConfig.players.get(id).getPosition());
		cam.lookAt(GameConfig.DIRECTION);
		cam.near = 1f;
		cam.far = 300f;
		cam.direction.x += 0.4f;
		cam.direction.z -= 0.6f;
		cam.update();
	}

	private void initEnvironment() 
	{
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
		hints = new ArrayList<ModelInstance>();
	}

	public void handleInput() 
	{
		if (Gdx.input.isKeyPressed(Input.Keys.A))
			GameConfig.LEFT = true;
		if (Gdx.input.isKeyPressed(Input.Keys.D))
			GameConfig.RIGHT = true;
		if (Gdx.input.isKeyPressed(Input.Keys.W))
			GameConfig.ON = true;
		if (Gdx.input.isKeyPressed(Input.Keys.S))
			GameConfig.BACK = true;		
		if (Gdx.input.isKeyJustPressed(Input.Keys.F))
			help();
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
		{
			cam.direction.rotate(2,0,1,0);
			degrees += 2;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
		{
			cam.direction.rotate(-2,0,1,0);
			degrees -= 2;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
		{
//			animationController.setAnimation("Armature|hit",-1);
			GameConfig.HIT = true;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
		{
			game.setScreen(new PauseScreen(game, this, countdown));
		}
			
	}
		
	public void render(float delta) 
	{
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
//		Gdx.gl30.enabl
		handleInput();
		handleAnimation();
		world.update();

		// move player instance
		playersInstance.get(id).transform.setToTranslation(GameConfig.players.get(id).getPosition());
		playersInstance.get(id).transform.rotate(0,1,0,degrees);
		
		// camera update
		Vector3 pos = new Vector3(GameConfig.players.get(id).getPosition().x,GameConfig.players.get(id).getPosition().y+6.5f, GameConfig.players.get(id).getPosition().z);
		cam.position.set(pos);
		
		cam.update();
		GameConfig.DIRECTION = cam.direction;

		batch.begin(cam);
		
		// render floor
		batch.render(assets.floorInstance, environment);

		// render player instances
		for(final ModelInstance playerInstance : playersInstance)			
			batch.render(playerInstance, environment);
		
		// render hints
		for(final ModelInstance mod : hints)
			batch.render(mod, environment);
		
		// render tools instance
		synchronized (GameConfig.toolsInstance)
		{
			for(final ModelInstance mod : GameConfig.toolsInstance.get(GameConfig.actualLevel-1))
				batch.render(mod, environment);
			
			// render previous room's model instances
//			if(GameConfig.actualLevel > 1)
//				for(final ModelInstance mod : GameConfig.toolsInstance.get(GameConfig.actualLevel-2))
//					batch.render(mod, environment);
//			
//			// render next room's model instances
//			if(GameConfig.level >= GameConfig.actualLevel+2)
//				for(final ModelInstance mod : GameConfig.toolsInstance.get(GameConfig.actualLevel))
//					batch.render(mod, environment);
		}
		
		
		// render walls
		synchronized (GameConfig.toolsInstance)
		{
			for(final ModelInstance mod : GameConfig.wallsInstance)
				batch.render(mod, environment);
		}
		
		batch.end();

		// update and render hud
		hud.update(delta);
		hud.stage.act();
		hud.stage.draw();
		
		if(GameConfig.GAME_OVER)
		{
			synchronized (countdown)
			{
				try
				{
					countdown.wait();
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			mapGenerator.interrupt();
			this.dispose();
			game.setScreen(new GameOverScreen(game));
		}
	}

	private void handleAnimation()
	{
		synchronized(GameConfig.toolsInstance)
		{
			for (AnimationController clock : assets.animationClock)
				clock.update(Gdx.graphics.getDeltaTime());
		}
		if(GameConfig.ON || GameConfig.BACK || GameConfig.RIGHT || GameConfig.LEFT)
		{
//			animationController.setAnimation("Armature|ArmatureAction",-1);
			animationController.update(Gdx.graphics.getDeltaTime());
		}
//		else if(GameConfig.HIT)
//		{
//			
//			animationController.update(Gdx.graphics.getDeltaTime());
//		}
	}

	public void dispose()
	{
		GameConfig.tools.clear();
		GameConfig.toolsInstance.clear();
		batch.dispose();
		assets.dispose();
		hud.dispose();
	}

	private void help()
	{
		hints.clear();
		
		// apply dijkstra
        List<Vertex> path = Dijkstra.getShortestPath();
        
        //	create hints model
        float position = (GameConfig.actualLevel-1) * 5.5f * GameConfig.ROOM_DIMENSION;
        for (Vertex vertex : path)
        {
        	ModelInstance mod = new ModelInstance(assets.help);
        	mod.transform.setToTranslation(vertex.x*5.5f +position, -4.1f, vertex.y*5.5f);
        	hints.add(mod);
		}
     }
	
	@Override
	public void show() { }

	@Override
	public void resize(int width, int height) { }

	@Override
	public void pause() { }

	@Override
	public void resume() { }

	@Override
	public void hide() { }
}
