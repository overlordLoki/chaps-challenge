package Renderer;
import Domain.*;
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
    //maze
    private Maze maze;

    public Renderer(Maze maze) {
        this.maze = maze;
    }

    public BufferedImage setTexturePack(String texturePack) {
        this.texturePack = texturePack;
        try {
            return ImageIO.read(getClass().getResource("/render/textures/" + texturePack + "/background.png"));
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    public Renderer getWindow(){
        return this;
    }

}
