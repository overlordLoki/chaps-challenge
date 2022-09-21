package nz.ac.vuw.ecs.swen225.gp6.renderer;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.Graphics;
import javax.sound.sampled.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.DomainAccess.DomainController;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.Tile;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.TileType;

/**
 * makes a jPanel that can be added to a JFrame
 * 
 * @author Loki
 */
public class MazeRenderer extends JPanel{
    static final long serialVersionUID = 1L;
    private TexturePack texturePack = TexturePack.Cats;
    private Tile[][] gameArray;
    private DomainController maze;
    public BufferedImage background;
    private int patternSize = 100;
    static TexturePack currentTP = TexturePack.Cats;
    private Clip clip;
    public int getPatternSize() {return patternSize;}
    public TexturePack getCurrentTexturePack(){return texturePack;}
    /**
     * Constructor. Takes a maze as parameters.
     * 
     * @param maze Maze to be rendered.
     */
    public MazeRenderer(DomainController maze) {
        this.maze = maze;
        PlaySound();
    }
    /**
     * set the current texturePack and returns the new background image
     * 
     * @param texturePack
     * @return BufferedImage
     */
    public void setTexturePack(TexturePack texturePack) {
        this.texturePack = texturePack;
        this.currentTP = texturePack;
        TexturePack.Images.reloadAllTexturepack();
        patternSize = 100;
    }

    /**
     * get this Renderer
     * 
     * @return this Renderer
     */
    public BufferedImage getImage(String image){
        try {
            return ImageIO.read(getClass().getResource("/Renderer/textures/" + texturePack + "/" + image + ".png"));
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    public BufferedImage getImage(TexturePack.Images imgName) {return imgName.getImg();}

    //initialize the music
    public void PlaySound() {
        try {
           // Open an audio input stream.
           String path = "src/nz/ac/vuw/ecs/swen225/gp6/renderer/music/gameMusic.wav";
           //C:\Users\tymon\Desktop\UNI\swen 225\chaps-challenge\src\nz\ac\vuw\ecs\swen225\gp6\renderer\music\gameMusic.wav
           File file = new File(path);
           AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
           // Get a sound clip resource.
           clip = AudioSystem.getClip();
           // Open audio clip and load samples from the audio input stream.
           clip.open(audioIn);
        } catch (Exception e) {e.printStackTrace();}
        System.out.println("initialize the music");
    }

    //play the music
    public void playMusic() {
        //start music from the start
        clip.setFramePosition(0);
        //play the music
        clip.start();
        //loop the music
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        System.out.println("playing music");
    }
    //stop playing the music
    public void stopMusic() {
        //stop the music
        clip.stop();
        System.out.println("stop playing music");
    }
    
   
    @Override
    public void paintComponent(Graphics g) {
        //call superclass to paint background
        super.paintComponent(g);
        //get the maze array
        gameArray = maze.getGameArray();
        //get the width and height of the maze
        int tileWidth = (getWidth() / gameArray.length);
        int tileHeight = (getHeight() / gameArray[1].length);
        //loop through the maze array and paint the tiles
        for (int i = 0; i < gameArray.length; i++) {
            for (int j = 0; j < gameArray[1].length; j++) {
                //clear the floor
                g.drawImage(TexturePack.Images.Floor.getImg(), i * tileWidth, j * tileHeight, tileWidth, tileHeight, null);
                // if there is a item draw on top of the floor or a wall tile
                Tile tile = gameArray[i][j];
                if(tile.type() == TileType.Floor) {continue;}
                g.drawImage(TexturePack.Images.getImage(tile), i * tileWidth, j * tileHeight, tileWidth, tileHeight, null);
            }
        }
    }

}
