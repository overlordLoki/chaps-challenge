package nz.ac.vuw.ecs.swen225.gp6.renderer;
import javax.sound.sampled.*;
import java.io.File;

public final class MusicPlayer {
    private static final  Clip gameMusic = initializeMusic("./res/music/gameMusic.wav");//initialize the game musicPlayer
    //music for menu
    private static final Clip menuMusic = initializeMusic("./res/music/menuMusic.wav");//initialize the menu musicPlayer

    private static Clip currentMusic = menuMusic;//initialize the current musicPlayer
    //private musicPlayer constructor
    /**
     * private constructor
     */
    private MusicPlayer() {}


    /**
     * initialize the game musicPlayer
     * @return Clip
     */
    public static Clip initializeMusic(String path) {
        try {
            File file = new File(path);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
            // Get a sound clip resource.
            Clip musicClip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            musicClip.open(audioIn);
            //System.out.println("initialized the music");
            return musicClip;
        } catch (Exception e) {e.printStackTrace();}
        //System.out.println("failed to initialize the music");
        return null;
    }

    public static void useGameMusic(){resetMusic(gameMusic);}

    public static void useMenuMusic(){resetMusic(menuMusic);}
    
    public static void playMusic() {currentMusic.loop(Clip.LOOP_CONTINUOUSLY);}

    public static void stopMusic() {currentMusic.stop();}
    
    public static void resetMusic(Clip music) {
        currentMusic.setMicrosecondPosition(0);
        stopMusic();
        currentMusic = music;
    }
}
