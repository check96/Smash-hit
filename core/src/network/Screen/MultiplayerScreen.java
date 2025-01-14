package network.Screen;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;

import GameGui.GameManager;
import GameGui.SoundManager;
import GameGui.Screen.PauseScreen;
import network.MultiplayerHUD;
import network.MultiplayerWorld;
import network.packets.AnimationPacket;
import network.packets.MovePacket;
import network.threads.ClientThread;
import videogame.Countdown;
import videogame.GameConfig;

public class MultiplayerScreen implements Screen 
{
	public GameManager game;
	private Camera cam;
	private ModelBatch batch;
	private Environment environment;
	private MultiplayerWorld world;
	private ArrayList<Long> hitTimes;
	private ArrayList<Boolean> hitAnimations;
	private MultiplayerHUD hud;
	private ArrayList<AnimationController> playerControllers;
	private ArrayList<AnimationController> destroyedController;
	private Socket socket;
	public static ClientThread client;

	private Controller joystick;

	//pressing button boolean
	private boolean keyB = false;
	private boolean keyLB = false;
	private boolean keyRB = false;
	private boolean keyStart = false;
	
	//movement analog boolean
	private boolean moveOn = false;
	private boolean moveBack = false;
	private boolean moveLeft = false;
	private boolean moveRight = false;
	
	private final int B = 1;
	private final int LB = 4;
	private final int RB = 5;
	private final int START = 7;

	public MultiplayerScreen() { }
	public MultiplayerWorld getWorld() {return world;}
	
	public MultiplayerScreen(GameManager _game, String ip, int port)
	{
		this.game = _game;

		for (Controller c : Controllers.getControllers()) 
		{
			if(c.getName().contains("XBOX") && c.getName().contains("360")) 
				joystick = c;
		}
		
		if(joystick != null)
		{
			initJoystick();
		}

		SoundManager.gameSoundtrack.play();
		batch = new ModelBatch();

		initCamera();
		initEnvironment();

		hud = new MultiplayerHUD();
		
		try
		{
			socket = new Socket(ip, port);
			client = new ClientThread(socket, this);
			client.start();
		}
		catch (UnknownHostException e)
		{	e.printStackTrace(); }
		catch (IOException e)
		{	e.printStackTrace(); }
		world = new MultiplayerWorld(client);
	}

	private void initJoystick() 
	{
		joystick.addListener(new ControllerAdapter() 
		{			
			@Override
			public boolean buttonDown(Controller controller, int buttonIndex) 
			{
				if (buttonIndex == B)
					keyB = true;
				else if (buttonIndex == RB)
					keyRB = true;
				else if (buttonIndex == LB)
					keyLB = true;
				else if (buttonIndex == START)
					keyStart = true;
				return true;
			}
			
			@Override
			public boolean buttonUp(Controller controller, int buttonIndex) 
			{
				if (buttonIndex == B)
					keyB = false;
				else if (buttonIndex == RB)
					keyRB = false;
				else if (buttonIndex == LB)
					keyLB = false;
				else if (buttonIndex == START)
					keyStart = false;
				return true;
			}
			@Override
			public boolean povMoved(Controller controller, int povIndex, PovDirection value)
			{
				if(value == PovDirection.north)
					moveOn = true;
				else
					moveOn = false;
				
				if(value == PovDirection.south)
					moveBack = true;
				else
					moveBack = false;
				
				if(value == PovDirection.east)
					moveRight = true;
				else 
					moveRight = false;
				
				if(value == PovDirection.west)
					moveLeft = true;
				else 
					moveLeft = false;
				
				return true;
			}
		});
	}
	
	public void initAnimation()
	{
		hitTimes = new ArrayList<Long>();
		hitAnimations = new ArrayList<Boolean>();
		destroyedController = new ArrayList<AnimationController>();
		playerControllers = new ArrayList<AnimationController>();
		for(int i = 0; i < GameConfig.playersInstance.size(); i++)
		{
			playerControllers.add(new AnimationController(GameConfig.playersInstance.get(i)));
			playerControllers.get(i).setAnimation("Armature|ArmatureAction",-1);
			hitAnimations.add(false);
			hitTimes.add(0l);
		}
	}

	private void initCamera() 
	{
		cam = new PerspectiveCamera(67, GameConfig.Screen_Width, GameConfig.Screen_Height);
		cam.lookAt(GameConfig.DIRECTION);
		cam.near = 1f;
		cam.far = 1500f;
		cam.direction.x += 0.4f;
		cam.direction.y -= 0.5f;
		cam.direction.z -= 0.4f;
		cam.update();
	}

	private void initEnvironment() 
	{
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
	}

	public void handleInput() 
	{
		if (Gdx.input.isKeyPressed(Input.Keys.A) || moveLeft)
			GameConfig.LEFT = true;
		else if (Gdx.input.isKeyPressed(Input.Keys.D) || moveRight)
			GameConfig.RIGHT = true;
		else if (Gdx.input.isKeyPressed(Input.Keys.W) || moveOn)
			GameConfig.ON = true;
		else if (Gdx.input.isKeyPressed(Input.Keys.S) || moveBack)
			GameConfig.BACK = true;		
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || keyLB)
		{
			cam.direction.rotate(4,0,1,0);
			GameConfig.players.get(GameConfig.ID).angle += 4;
			client.out.println(new MovePacket(GameConfig.players.get(GameConfig.ID).getPosition(), GameConfig.players.get(GameConfig.ID).angle));
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || keyRB)
		{
			cam.direction.rotate(-4,0,1,0);
			GameConfig.players.get(GameConfig.ID).angle -= 4;
			client.out.println(new MovePacket(GameConfig.players.get(GameConfig.ID).getPosition(),GameConfig.players.get(GameConfig.ID).angle));
		}
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || keyB)
		{
			GameConfig.HIT = true;
			synchronized (hitAnimations)
			{
				hitAnimations.set(GameConfig.ID,true);
			}
			synchronized(hitTimes)
			{
				hitTimes.set(GameConfig.ID, System.currentTimeMillis());
			}
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || keyStart)
		{
			keyStart = false;
			game.setScreen(new PauseScreen(game, this));
		}
	}
	
	public void render(float delta) 
	{
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		
		handleInput();

		addAnimation();
		handleAnimation();
		world.update(delta);

		// render players instance
		synchronized(GameConfig.playersInstance)
		{
			for(int i = 0; i < GameConfig.playersInstance.size(); i++)
				GameConfig.playersInstance.get(i).transform.setToTranslation(GameConfig.players.get(i).getPosition());

			for(int i = 0; i < GameConfig.playersInstance.size(); i++)
				GameConfig.playersInstance.get(i).transform.rotate(0,1,0,90+GameConfig.players.get(i).angle);
			for(int i = 0; i < GameConfig.playersInstance.size(); i++)
				batch.render(GameConfig.playersInstance.get(i), environment);
		}
		// camera update
		cam.position.set(GameConfig.players.get(GameConfig.ID).getPosition().cpy().add(0,7,0));
		
		cam.update();
		GameConfig.DIRECTION = cam.direction;

		batch.begin(cam);

		// render walls
		synchronized (GameConfig.wallsInstance)
		{
			for(int i = 0; i<GameConfig.wallsInstance.size(); i++)
				batch.render(GameConfig.wallsInstance.get(i), environment);
		}
		
		// render tools instance
		synchronized(GameConfig.multiplayerInstances)
		{
			for(int i = 0; i < GameConfig.multiplayerInstances.size(); i++)
				batch.render(GameConfig.multiplayerInstances.get(i), environment);
		}

//		render destroyed tools
		synchronized(GameConfig.destroyed)
		{
			for (int i = 0; i<GameConfig.destroyed.size(); i++)
				batch.render(GameConfig.destroyed.get(i), environment);
		}
		
		batch.end();

		// update and render hud
		hud.update();
		hud.stage.act();
		hud.stage.draw();
	
		if(GameConfig.GAME_OVER)
		{
			synchronized (game.countdown)
			{
				game.countdown.active = false;
			}

			this.dispose();
			game.setScreen(new MultiplayerOverScreen(game));
		}
	}

	private void addAnimation()
	{
		for(int i = destroyedController.size(); i < GameConfig.destroyed.size(); i++)
		{
			destroyedController.add(new AnimationController(GameConfig.destroyed.get(i)));
			destroyedController.get(i).setAnimation("Armature|ArmatureAction");
		}
	}

	private synchronized void handleAnimation()
	{
		synchronized(hitAnimations)
		{
			if(!hitAnimations.get(GameConfig.ID) && (GameConfig.ON || GameConfig.BACK || GameConfig.RIGHT || GameConfig.LEFT))
			{
				client.out.println(new AnimationPacket("move", 0l));
				synchronized(hitAnimations)
				{
					playerControllers.get(GameConfig.ID).setAnimation("Armature|ArmatureAction",-1);
					playerControllers.get(GameConfig.ID).update(Gdx.graphics.getDeltaTime()/2);
				}
			}
		}
		synchronized(hitAnimations)
		{
			if(hitAnimations.get(GameConfig.ID))
			{
				client.out.println(new AnimationPacket("hit", hitTimes.get(GameConfig.ID)));
				synchronized(playerControllers)
				{
					playerControllers.get(GameConfig.ID).setAnimation("Armature|hit",-1);
					playerControllers.get(GameConfig.ID).update(Gdx.graphics.getDeltaTime()/2);
				}
			}
		}
		synchronized(hitTimes)
		{
			synchronized(hitAnimations)
			{
				if(hitAnimations.get(GameConfig.ID) && System.currentTimeMillis() - hitTimes.get(GameConfig.ID) > 400)
				{
					playerControllers.get(GameConfig.ID).setAnimation("Armature|ArmatureAction",-1);
					playerControllers.get(GameConfig.ID).update(Gdx.graphics.getDeltaTime()/2);
					hitAnimations.set(GameConfig.ID, false);
				}
			}
		}
		
		synchronized(hitAnimations)
		{
			for(int i = 0; i < hitAnimations.size(); i++)
			{
				synchronized(hitTimes)
				{
					if(hitAnimations.get(i) && System.currentTimeMillis() - hitTimes.get(i) > 400)
					{
						client.out.println(new AnimationPacket("endHit",0l));
						synchronized(playerControllers)
						{
							playerControllers.get(i).setAnimation("Armature|ArmatureAction",-1);
							playerControllers.get(i).update(Gdx.graphics.getDeltaTime()/2);
						}
						hitAnimations.set(i, false);
					}
				}
			}
		}
		
		for(int i = 0; i < playerControllers.size(); i++)
		{
			if(hitAnimations.get(i))
				playerControllers.get(i).update(Gdx.graphics.getDeltaTime()/2);
		}
		
		for (AnimationController controller : destroyedController)
			controller.update(Gdx.graphics.getDeltaTime());
		
		if(Countdown.getTime() %10 == 0)
		{
			destroyedController.clear();
			GameConfig.destroyed.clear();
		}
	}

	public synchronized void handlePlayerAnimations(int id, String type, long time)
	{
		int j = id;
		if(type.equals("hit"))
		{
			synchronized(playerControllers)
			{
				playerControllers.get(j).setAnimation("Armature|hit",-1);
			}
			synchronized(hitAnimations)
			{
				hitAnimations.set(j, true);
			}
			
			synchronized(hitTimes)
			{
				hitTimes.set(j, time);
			}
		}
		else if(type.equals("move"))
		{
			synchronized(playerControllers)
			{
				playerControllers.get(j).setAnimation("Armature|ArmatureAction",-1);
			}
		}
		else if(type.equals("endHit"))
		{
			synchronized(playerControllers)
			{
				playerControllers.get(j).setAnimation("Armature|ArmatureAction",-1);
			}
			synchronized(hitAnimations)
			{
				hitAnimations.set(j, false);
			}
		}
	}
	
	public void dispose()
	{
		GameConfig.tools.clear();
		GameConfig.toolsInstance.clear();
		batch.dispose();
		hud.dispose();
	}
	
	@Override
	public void show() { }

	@Override
	public void resize(int width, int height)
	{
		hud.resize(width,height);
		game.options.putInteger("screen_width", width);
		game.options.putInteger("screen_height", height);
		game.options.flush();
		
		GameConfig.Screen_Height = height;
		GameConfig.Screen_Width = width;
	}

	@Override
	public void pause() { }

	@Override
	public void resume() { }

	@Override
	public void hide() { }
}