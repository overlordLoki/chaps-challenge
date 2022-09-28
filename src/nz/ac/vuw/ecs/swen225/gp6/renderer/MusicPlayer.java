package nz.ac.vuw.ecs.swen225.gp6.renderer;
import javax.sound.sampled.*;
import java.io.File;

public final class MusicPlayer {

    private static Clip gameMusic = initializeGameMusic();//initialize the game musicPlayer
    //music for menu
    private static Clip menuMusic = initializeMenuMusic();//initialize the menu musicPlayer
    //private musicPlayer constructor
    /**
     * private constructor
     */
    private MusicPlayer() {}

    //initialize the music
    /**
     * initialize the game musicPlayer
     * @return Clip
     */
    public static Clip initializeGameMusic() {
        try {
            // Open an audio input stream.
            String path = "./res/music/gameMusic.wav";
            File file = new File(path);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
            // Get a sound clip resource.
            Clip gameMusic = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            gameMusic.open(audioIn);
            //System.out.println("initialized the music");
            return gameMusic;
        } catch (Exception e) {e.printStackTrace();}
        //System.out.println("failed to initialize the music");
        return null;
    }

    /**
     * play the game music
     */
    public static void playGameMusic() {
        //start music from the start
        gameMusic.setFramePosition(0);
        //play the music
        gameMusic.start();
        //loop the music
        gameMusic.loop(Clip.LOOP_CONTINUOUSLY);
        //System.out.println("playing music");
    }
    /**
     * stop playing the game music
     */
    public static void stopGameMusic() {
        //stop the music
        gameMusic.stop();
        //System.out.println("stop playing music");
    }

    /**
     * initialize the menu musicPlayer
     * @return Clip
     */
    public static Clip initializeMenuMusic() {
        try {
            // Open an audio input stream.
            String path = "./res/music/menuMusic.wav";
            File file = new File(path);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
            // Get a sound clip resource.
            Clip menuMusic = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            menuMusic.open(audioIn);
            //System.out.println("initialized the music");
            return menuMusic;
        } catch (Exception e) {e.printStackTrace();}
        //System.out.println("failed to initialize the music");
        return null;
    }

    /**
     * play the menu music
     */
    public static void playMenuMusic() {
        //start music from the start
        menuMusic.setFramePosition(0);
        //play the music
        menuMusic.start();
        //loop the music
        menuMusic.loop(Clip.LOOP_CONTINUOUSLY);
        //System.out.println("playing music");
    }
    /**
     * stop playing the menu music
     */
    public static void stopMenuMusic() {
        //stop the music
        menuMusic.stop();
        //System.out.println("stop playing music");
    }


    
}
