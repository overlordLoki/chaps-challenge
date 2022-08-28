package Renderer;
import Persistency.*;
import Persistency.Tiles.Tile;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.Graphics;

public class Renderer extends JPanel{
    static final long serialVersionUID = 1L;
    //default texturePack
    private String texturePack = "Original";
    //maze array
    private Tile[][] gameArray;
    //the maze
    private Maze maze;

    /*
     * Constructor. Takes a maze as parameters.
     * @param maze Maze to be rendered.
     */
    public Renderer(Maze maze) {
        this.maze = maze;
    }

    /*
     * set the current texturePack and returns the new background image
     * @param texturePack
     * @return BufferedImage
     */
    public BufferedImage setTexturePack(String texturePack) {
        this.texturePack = texturePack;
        try {
            return ImageIO.read(getClass().getResource("/render/textures/" + texturePack + "/background.png"));
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    /*
     * get this Renderer
     * @return this Renderer
     */
    public Renderer getWindow(){
        return this;
    }

    /*
     * get the image of the tile
     * @param Tile
     * @return BufferedImage
     */
    public BufferedImage getImage(Tile object) {
        String name = object.getImg();
        try {
            return ImageIO.read(getClass().getResource("/render/textures/" + texturePack + "/" + name + ".png"));
        } catch (IOException e) {throw new RuntimeException(e);}
    }


}
