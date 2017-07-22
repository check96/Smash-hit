package GameGui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;


public class SoundManager
{
	public static Music hitSound = Gdx.audio.newMusic(Gdx.files.internal("music/mace_hit.ogg"));
	public static Sound destructionSound = Gdx.audio.newSound(Gdx.files.internal("music/crash.mp3"));
	public static Sound clockSound = Gdx.audio.newSound(Gdx.files.internal("music/Timer.mp3"));
	public static Sound explosionSound = Gdx.audio.newSound(Gdx.files.internal("music/explosion.mp3"));
	public static Sound vortexSound = Gdx.audio.newSound(Gdx.files.internal("music/tornado.mp3"));
	public static Sound coinsSound = Gdx.audio.newSound(Gdx.files.internal("music/final_money.ogg"));
	public static Sound shopSound = Gdx.audio.newSound(Gdx.files.internal("music/money.mp3"));
	public static Music gameSoundtrack = Gdx.audio.newMusic(Gdx.files.internal("music/Atlas_rise.ogg"));
	public static Music menuSoundtrack = Gdx.audio.newMusic(Gdx.files.internal("music/Given_up.ogg"));
	public static float musicVolume = 1;
	public static float soundVolume = 1;
}
