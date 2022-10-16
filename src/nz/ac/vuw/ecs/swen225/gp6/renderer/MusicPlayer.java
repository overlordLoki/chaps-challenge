package nz.ac.vuw.ecs.swen225.gp6.renderer;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * This class is used to play music in the game.
 *
 * @author loki
 */
public final class MusicPlayer {

  //---------------------------------------------------------fields-------------------------------------------------------//
  private static final Clip gameMusic = initializeMusic("./res/music/gameMusic.wav");
  private static final Clip menuMusic = initializeMusic("./res/music/menuMusic.wav");
  private static Clip currentMusic = menuMusic;
//---------------------------------------------------------constructor-------------------------------------------------------//

  /**
   * private constructor.
   */
  private MusicPlayer() {
  }
//------------------------------------------------private methods-------------------------------------------------------//

  /**
   * initialize the game musicPlayer.
   *
   * @return Clip
   */
  private static Clip initializeMusic(String path) {
    try {
      File file = new File(path);
      AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
      // Get a sound clip resource.
      Clip musicClip = AudioSystem.getClip();
      // Open audio clip and load samples from the audio input stream.
      musicClip.open(audioIn);
      //System.out.println("initialized the music");
      return musicClip;
    } catch (Exception e) {
      e.printStackTrace();
    }
    //System.out.println("failed to initialize the music");
    return null;
  }
//---------------------------------------------------------public methods-------------------------------------------------------//

  /**
   * plays the game music.
   */
  public static void useGameMusic() {
    resetMusic(gameMusic);
  }

  /**
   * plays the menu music.
   */
  public static void useMenuMusic() {
    resetMusic(menuMusic);
  }

  /**
   * resumes the music.
   */
  public static void playMusic() {
    currentMusic.loop(Clip.LOOP_CONTINUOUSLY);
  }

  /**
   * pauses the music.
   */
  public static void stopMusic() {
    currentMusic.stop();
  }

  /**
   * resets the music.
   *
   * @param music the music to be played next
   */
  public static void resetMusic(Clip music) {
    currentMusic.setMicrosecondPosition(0);
    stopMusic();
    currentMusic = music;
  }
}
