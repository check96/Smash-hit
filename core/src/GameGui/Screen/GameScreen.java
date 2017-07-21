package GameGui.Screen;

import java.util.ArrayList;
import java.util.List;

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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Quaternion;

import GameGui.GameManager;
import GameGui.SoundManager;
import GameGui.TimeXplosion;
import GameGui.HUD.Hud;
import videogame.AI.Dijkstra;
import videogame.Countdown;
import videogame.GameConfig;
import videogame.AI.Vertex;
import videogame.bonus.Bomb;
import videogame.World;

public class GameScreen implements Screen
{
	private GameManager game;
	private Camera cam;
	private ModelBatch batch;
	public static ModelInstance playerInstance;
	private ModelInstance bomb1Instance;
	private ModelInstance bomb2Instance;
	private ArrayList<ModelInstance> hints;
	private Environment environment;
	private World world;
	private int degrees = 90;
	private long hitTime = 0;
	private boolean hitAnimation = false;
	private Hud hud;
	private AnimationController playerController;
	private ArrayList<AnimationController> destroyedController;
	private ArrayList<AnimationController> coinController;
	private String[] state = new String[4];

	private Decal xplosion;
	private DecalBatch xplosionBatch;
	private TextureRegion xplosionRegion;
	private Texture xplosionTexture;
	
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
	private boolean keyY = false;	
	
	private Controller joystick;
	private final int B = 1;
	private final int Y = 3;
	private final int LB = 4;
	private final int RB = 5;
	private final int START = 7;

	public GameScreen() { }

	public GameScreen(GameManager _game) 
	{
		game = _game;
		for (Controller c : Controllers.getControllers()) 
		{
			if (c.getName().contains("XBOX") && c.getName().contains("360"))
				joystick = c;
		}

		if (joystick != null)
			initJoystick();

		state[0] = "hit";
		state[1] = "bomb1";
		state[2] = "bomb2";
		state[3] = "tornado";

		SoundManager.gameSoundtrack.play();

		world = new World();
		batch = new ModelBatch();

		game.mapGenerator.assets.loadPlayer();
		initCamera();
		initEnvironment();
		initAnimation();

		xplosionTexture = new Texture("Icons/explosion1.png");
		xplosionRegion = new TextureRegion(xplosionTexture);
		xplosion = Decal.newDecal(xplosionRegion, true);
		xplosionBatch = new DecalBatch(new CameraGroupStrategy(cam));

		bomb1Instance = new ModelInstance(game.mapGenerator.assets.bomb1);
		bomb2Instance = new ModelInstance(game.mapGenerator.assets.bomb2);

		game.countdown.active = true;
		hud = new Hud();
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
				else if(buttonIndex == Y)
					keyY = true;
				else if (buttonIndex == RB) // && GameConfig.STATE !="tornado"
					keyRB = true;
				else if (buttonIndex == LB) // && GameConfig.STATE !="tornado"
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
				else if(buttonIndex == Y)
					keyY = false;
				else if (buttonIndex == RB) // && GameConfig.STATE !="tornado"
					keyRB = false;
				else if (buttonIndex == LB) // && GameConfig.STATE !="tornado"
					keyLB = false;
				else if (buttonIndex == START) // START
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

	private void initAnimation()
	{
		destroyedController = new ArrayList<AnimationController>();
		coinController = new ArrayList<AnimationController>();

		playerController = new AnimationController(playerInstance);
		playerController.setAnimation("Armature|ArmatureAction", -1);
	}

	private void initCamera()
	{
		cam = new PerspectiveCamera(67, GameConfig.Screen_Width, GameConfig.Screen_Height);
		cam.position.set(GameConfig.player.getPosition());
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
		hints = new ArrayList<ModelInstance>();
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
		if (Gdx.input.isKeyPressed(Input.Keys.F))
			help();
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || keyLB)
		{
			cam.direction.rotate(4, 0, 1, 0);
			degrees += 4;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || keyRB)
		{
			cam.direction.rotate(-4, 0, 1, 0);
			degrees -= 4;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE )|| keyB)
		{
			if(GameConfig.STATE.equals("bomb1") || GameConfig.STATE.equals("bomb2"))
				keyB = false;
			
			GameConfig.HIT = true;
			hitAnimation = true;
			hitTime = System.currentTimeMillis();
				
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || keyStart) 
		{
			game.countdown.active = false;
			game.setScreen(new PauseScreen(game, this));
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_RIGHT) || keyY)
		{
			keyY = false;
			GameConfig.stateIndex++;
			GameConfig.stateIndex %= 3;

			GameConfig.STATE = state[GameConfig.stateIndex];
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT))
		{
			GameConfig.stateIndex--;

			if (GameConfig.stateIndex < 0)
				GameConfig.stateIndex = 2;

			GameConfig.STATE = state[GameConfig.stateIndex];
		}
	}

	public void drawBonus()
	{
		if (GameConfig.bombXplosion != null)
		{
			new TimeXplosion();
			if (GameConfig.xplosion1)
			{
				xplosion.setPosition(GameConfig.bombXplosion.getPosition());
				xplosion.setRotation(playerInstance.transform.getRotation(new Quaternion()));
				xplosion.setScale(0.01f);
				xplosionBatch.add(xplosion);
			}
			else if (GameConfig.xplosion2)
			{
				xplosion.setPosition(GameConfig.bombXplosion.getPosition());
				xplosion.setRotation(playerInstance.transform.getRotation(new Quaternion()));
				xplosion.setScale(0.05f);
				xplosionBatch.add(xplosion);
			}
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
		handleSound();
		world.update(delta);

		// move player instance
		playerInstance.transform.setToTranslation(GameConfig.player.getPosition());
		playerInstance.transform.rotate(0, 1, 0, degrees);

		// camera update
		cam.position.set(GameConfig.player.getPosition().cpy().add(0, 7, 0));

		cam.update();
		GameConfig.DIRECTION = cam.direction;

		batch.begin(cam);

		// render player instance
		batch.render(playerInstance, environment);

		// render hints
		for (final ModelInstance mod : hints)
			batch.render(mod, environment);

		// render walls
		synchronized (GameConfig.wallsInstance) {
			for (final ModelInstance wall : GameConfig.wallsInstance)
				batch.render(wall, environment);
		}

		if (world.getBomb() instanceof Bomb) {
			if (world.getBomb().shooted()) {
				if (!world.getBomb().isUpgrade()) {
					bomb1Instance.transform.setToTranslation(world.getBomb().getPosition());
					batch.render(bomb1Instance, environment);
				} else {
					bomb2Instance.transform.setToTranslation(world.getBomb().getPosition());
					batch.render(bomb2Instance, environment);
				}
			}
		}

		// render tools instance
		synchronized (GameConfig.toolsInstance) {
			for (final ModelInstance mod : GameConfig.toolsInstance.get(GameConfig.actualLevel - 1))
				batch.render(mod, environment);

			// render next room's model instances
			if (GameConfig.actualLevel < GameConfig.toolsInstance.size()) {
				for (final ModelInstance mod : GameConfig.toolsInstance.get(GameConfig.actualLevel))
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

		if (GameConfig.GAME_OVER) {
			synchronized (game.countdown) {
				game.countdown.active = false;
			}
			synchronized (game.mapGenerator) {
				game.mapGenerator.active = false;
			}
			this.dispose();
			game.setScreen(new GameOverScreen(game));
		}
		drawBonus();
		xplosionBatch.flush();
	}

	private void addAnimation() {
		for (int i = destroyedController.size(); i < GameConfig.destroyed.size(); i++) {
			destroyedController.add(new AnimationController(GameConfig.destroyed.get(i)));
			destroyedController.get(i).setAnimation("Armature|ArmatureAction");
		}

		for (int i = coinController.size(); i < GameConfig.coins.size(); i++) {
			coinController.add(new AnimationController(GameConfig.coins.get(i)));
			coinController.get(i).setAnimation("Armature|ArmatureAction");
		}
	}

	private void handleSound() {
		if (GameConfig.tornadoSound) {
			SoundManager.vortexSound.loop(SoundManager.soundVolume);
			GameConfig.tornadoSound = false;
		} else if (GameConfig.tornadoSoundStop) {
			SoundManager.vortexSound.stop();
			GameConfig.tornadoSoundStop = false;
		}
		if (hitAnimation) {
			if (state[GameConfig.stateIndex] == "hit") {
				if (GameConfig.HIT && GameConfig.hitted) {
					SoundManager.hitSound.play();
				} else if (!GameConfig.HIT) {
					SoundManager.hitSound.stop();
					GameConfig.hitted = false;
				}

			} else if (state[GameConfig.stateIndex] == "bomb1" || state[GameConfig.stateIndex] == "bomb2") {
				SoundManager.explosionSound.play(SoundManager.soundVolume);
			}

		}
	}

	private void handleAnimation() {
		synchronized (game.mapGenerator.assets.clockAnimation) {
			for (AnimationController clock : game.mapGenerator.assets.clockAnimation)
				clock.update(Gdx.graphics.getDeltaTime());
		}

		synchronized (game.mapGenerator.assets.vortexAnimation) {
			for (AnimationController vortex : game.mapGenerator.assets.vortexAnimation)
				vortex.update(Gdx.graphics.getDeltaTime());
		}

		if (state[GameConfig.stateIndex] == "tornado") {
			playerController.setAnimation("Armature|bonus", -1);
			playerController.update(Gdx.graphics.getDeltaTime());
		}

		if (!hitAnimation && (GameConfig.ON || GameConfig.BACK || GameConfig.RIGHT || GameConfig.LEFT)) {
			if (state[GameConfig.stateIndex] != "tornado") {
				playerController.setAnimation("Armature|ArmatureAction", -1);
				playerController.update(Gdx.graphics.getDeltaTime());
			}
		}

		if (hitAnimation) {
			if (state[GameConfig.stateIndex] == "hit") {
				playerController.setAnimation("Armature|hit", -1);
				playerController.update(Gdx.graphics.getDeltaTime());
			} else if (state[GameConfig.stateIndex] == "bomb1" || state[GameConfig.stateIndex] == "bomb2") {

				playerController.setAnimation("Armature|bomb", -1);
				playerController.update(Gdx.graphics.getDeltaTime());
			}
		}

		if (hitAnimation && System.currentTimeMillis() - hitTime > 400) {
			playerController.setAnimation("Armature|ArmatureAction", -1);
			playerController.update(Gdx.graphics.getDeltaTime());
			hitAnimation = false;
		}

		for (AnimationController controller : destroyedController)
			controller.update(Gdx.graphics.getDeltaTime());

		for (AnimationController Controller : coinController)
			Controller.update(Gdx.graphics.getDeltaTime());

		if (Countdown.getTime() % 10 == 0) {
			destroyedController.clear();
			GameConfig.destroyed.clear();

			coinController.clear();
			GameConfig.coins.clear();
		}
	}

	public void dispose() {
		GameConfig.tools.clear();
		GameConfig.toolsInstance.clear();
		batch.dispose();
		// game.mapGenerator.assets.dispose();
		hud.dispose();
	}

	private void help() {
		hints.clear();

		// apply dijkstra
		List<Vertex> path = Dijkstra.getShortestPath();

		// create hints model
		float position = (GameConfig.actualLevel - 1) * GameConfig.CELL_HEIGHT * GameConfig.ROOM_ROW;
		for (Vertex vertex : path) {
			ModelInstance mod = new ModelInstance(game.mapGenerator.assets.help);
			mod.transform.setToTranslation(-2 + vertex.x * GameConfig.CELL_HEIGHT + position, -4.7f,
					1 + vertex.y * GameConfig.CELL_WIDTH);
			hints.add(mod);
		}
	}

	@Override
	public void show() {
	}

	@Override
	public void resize(int width, int height) {
		hud.resize(width, height);
		game.options.putInteger("screen_width", width);
		game.options.putInteger("screen_height", height);
		game.options.flush();

		GameConfig.Screen_Height = height;
		GameConfig.Screen_Width = width;
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}
}