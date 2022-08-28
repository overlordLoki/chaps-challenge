package Renderer;
import Renderer.tempDomain.*;
import Renderer.tempDomain.Tiles.Tile;
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
    private BufferedImage getImage(Tile object) {
        String name = object.getImg();
        try {
            return ImageIO.read(getClass().getResource("/Renderer/textures/" + texturePack + "/" + name + ".png"));
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    /* 
    * get the image of EmptyTile
    * @return BufferedImage
    */
    private BufferedImage getEmptyTile() {
        try {
            return ImageIO.read(getClass().getResource("/Renderer/textures/" + texturePack + "/empty_tile.png"));
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    /*
     * paint the maze
     * @param Graphics
     */
    @Override
    public void paintComponent(Graphics g) {
        //call superclass to paint background
        super.paintComponent(g);
        //get the maze array
        gameArray = maze.getGameArray();
        //get the width and height of the maze
        int tileWidth = (getWidth() / gameArray.length);
        int tileHeight = (getHeight() / gameArray[1].length);
        //loop through the maze array and paint the black tile as a background
        for (int i = 0; i < gameArray.length; i++) {
            for (int j = 0; j < gameArray[1].length; j++) {
                g.drawImage(getEmptyTile(), i * tileWidth, j * tileHeight, tileWidth, tileHeight, null);
            }
        }
        //loop through the maze array and paint the tiles
        for (int i = 0; i < gameArray.length; i++) {
            for (int j = 0; j < gameArray[1].length; j++) {
                Tile tile = gameArray[i][j];
                g.drawImage(getImage(tile), i * tileWidth, j * tileHeight, tileWidth, tileHeight, null);
            }
        }
    }

}
