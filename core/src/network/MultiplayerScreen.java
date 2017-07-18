package network;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;

import GameGui.GameManager;
import GameGui.SoundManager;
import GameGui.HUD.MultiplayerHUD;
import GameGui.Screen.GameOverScreen;
import GameGui.Screen.GameScreen;
import GameGui.Screen.PauseScreen;
import videogame.Countdown;
import videogame.GameConfig;

public class MultiplayerScreen extends GameScreen
{
	private GameManager game;
	private MultiplayerHUD hud;
	private String username;
	private Socket socket;
	private ClientThread m;
	
	public MultiplayerScreen(GameManager _game, String username, String ip, int port)
	{
		try {
			socket = new Socket(ip, port);
			m = new ClientThread(socket);
			m.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.username = username;
		this.game = _game;
		this.hud = new MultiplayerHUD();
		this.batch = new ModelBatch();
		world = new MultiplayerWorld(username);
		
		SoundManager.gameSoundtrack.play();
		SoundManager.gameSoundtrack.setVolume(SoundManager.musicVolume);

		game.mapGenerator.assets.loadPlayer();
		initCamera();
		initEnvironment();
		initAnimation();
	}

	@Override
	public void handleInput()
	{
		if (Gdx.input.isKeyPressed(Input.Keys.A))
			GameConfig.LEFT = true;
		else if (Gdx.input.isKeyPressed(Input.Keys.D))
			GameConfig.RIGHT = true;
		else  if (Gdx.input.isKeyPressed(Input.Keys.W))
			GameConfig.ON = true;
		else if (Gdx.input.isKeyPressed(Input.Keys.S))
			GameConfig.BACK = true;		
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
		{
			cam.direction.rotate(4,0,1,0);
			degrees += 4;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
		{
			cam.direction.rotate(-4,0,1,0);
			degrees -= 4;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
		{
			GameConfig.HIT = true;
			hitAnimation = true;
			hitTime = System.currentTimeMillis();
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
		{
			game.countdown.pause = true;
			game.setScreen(new PauseScreen(game, this));
		}
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		handleInput();
		addAnimation();
		handleAnimation();
		world.update(delta);

		// move player instance
		playerInstance.transform.setToTranslation(GameConfig.player.getPosition());
		playerInstance.transform.rotate(0,1,0,degrees);
		
		// camera update
		cam.position.set(GameConfig.player.getPosition().cpy().add(0,7,0));
		
		cam.update();
		GameConfig.DIRECTION = cam.direction;

		batch.begin(cam);

		// render player instance
		batch.render(playerInstance, environment);
		
		
		// render walls
		synchronized (GameConfig.wallsInstance)
		{
			for (final ModelInstance wall : GameConfig.wallsInstance)
				batch.render(wall, environment);
		}
		
		// render tools instance
		synchronized (GameConfig.toolsInstance)
		{
			for(final ModelInstance mod : GameConfig.toolsInstance.get(GameConfig.actualLevel-1))
				batch.render(mod, environment);
	
			// render next room's model instances
			if(GameConfig.level > GameConfig.actualLevel+1 )
			{
				for(final ModelInstance mod : GameConfig.toolsInstance.get(GameConfig.actualLevel))
					batch.render(mod, environment);
			}
		}
		
		// render destroyed tools
		for (final ModelInstance instance : GameConfig.destroyed)
			batch.render(instance, environment);
		
		// render coins
		for (final ModelInstance instance : GameConfig.coins)
			batch.render(instance, environment);
		
		batch.end();

		// update and render hud
		hud.update();
		hud.stage.act();
		hud.stage.draw();
	
		if(GameConfig.GAME_OVER)
		{
			synchronized (game.countdown)
			{
				game.countdown.pause = true;
			}
			synchronized(game.mapGenerator)
			{
				game.mapGenerator.pause = true;
			}
			this.dispose();
			game.setScreen(new GameOverScreen(game));
		}

	}

	@Override
	protected void handleAnimation()
	{
		synchronized (game.mapGenerator.assets.clockAnimation)
		{
			for (AnimationController clock : game.mapGenerator.assets.clockAnimation)
				clock.update(Gdx.graphics.getDeltaTime());
		}
		
		if(!hitAnimation && (GameConfig.ON || GameConfig.BACK || GameConfig.RIGHT || GameConfig.LEFT))
		{
			playerController.setAnimation("Armature|ArmatureAction",-1);
			playerController.update(Gdx.graphics.getDeltaTime());
		}

		if(hitAnimation && state[GameConfig.stateIndex] == "hit")
		{
			playerController.setAnimation("Armature|hit",-1);
			playerController.update(Gdx.graphics.getDeltaTime());
		}
		
		if(hitAnimation && System.currentTimeMillis()-hitTime > 400)
		{
			playerController.setAnimation("Armature|ArmatureAction",-1);
			playerController.update(Gdx.graphics.getDeltaTime());
			hitAnimation = false;
		}
		
		for (AnimationController controller : destroyedController)
			controller.update(Gdx.graphics.getDeltaTime());
		
		for (AnimationController Controller : coinController)
			Controller.update(Gdx.graphics.getDeltaTime());
		
		if(Countdown.getTime() %10 == 0)
		{
			destroyedController.clear();
			GameConfig.destroyed.clear();
			
			coinController.clear();
			GameConfig.coins.clear();
		}

	}

	
	
}
