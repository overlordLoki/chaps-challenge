package nz.ac.vuw.ecs.swen225.gp6.renderer;
import javax.sound.sampled.*;
import java.io.File;

public final class MusicPlayer {

    private static Clip gameMusic = initializeGameMusic();

    //private musicPlayer constructor
    private MusicPlayer() {}

    //initialize the music
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
            System.out.println("initialized the music");
            return gameMusic;
        } catch (Exception e) {e.printStackTrace();}
        System.out.println("failed to initialize the music");
        return null;
    }

    //play the music
    public static void playGameMusic() {
        //start music from the start
        gameMusic.setFramePosition(0);
        //play the music
        gameMusic.start();
        //loop the music
        gameMusic.loop(Clip.LOOP_CONTINUOUSLY);
        System.out.println("playing music");
    }
    //stop playing the music
    public static void stopGameMusic() {
        //stop the music
        gameMusic.stop();
        System.out.println("stop playing music");
    }


    
}
