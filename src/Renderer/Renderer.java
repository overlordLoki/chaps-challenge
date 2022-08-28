package Renderer;
import Domain.*;
import Persistency.*;
import Persistency.Tiles.Tile;
import javax.swing.JPanel;

public class Renderer extends JPanel{
    static final long serialVersionUID = 1L;
    //default texturePack
    private String texturePack = "Original";
    //maze array
    private Tile[][] gameArray;
    //maze
    private Maze maze;
    
}
