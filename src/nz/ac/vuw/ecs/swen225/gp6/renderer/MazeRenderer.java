package nz.ac.vuw.ecs.swen225.gp6.renderer;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import java.awt.Graphics;
import nz.ac.vuw.ecs.swen225.gp6.domain.DomainAccess.DomainController;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.Hero;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Direction;

/**
 * makes a jPanel that can be added to a JFrame
 * 
 * @author Loki
 */
public class MazeRenderer extends JPanel{
    static final long serialVersionUID = 1L; //serialVersionUID
    private TexturePack texturePack = TexturePack.Dogs; //default texture pack
    private Tile[][] gameArray; //the array of tiles
    public DomainController maze; //the domain controller
    public BufferedImage background; //the background image
    private int patternSize = 100; //the size of the pattern
    static TexturePack currentTP = TexturePack.Dogs; //the current texture pack
    private int renderSize = 7; //the size of the render
    private int minRenderSize = 1, maxRenderSize = 50; //the min and max render size


    /**
     * Constructor. Takes a maze as parameters.
     * 
     * @param maze Maze to be rendered.
     */
    public MazeRenderer(DomainController maze) {
        this.maze = maze;
        this.setOpaque(false);
    }

    /**
     * get a image from the image provided
     * @param TexturePack.Images
     * @return BufferedImage
     */
    public BufferedImage getImage(TexturePack.Images imgName) {return imgName.getImg();}
   
    @Override
    public void paintComponent(Graphics g) {
        //call superclass to paint background
        super.paintComponent(g);
        //get the maze array
        gameArray = maze.getGameArray();
        //viewport of the maze
        Tile[][] viewport = Viewport.getViewport(gameArray, renderSize);
        //get the width and height of the maze
        int tileWidth = (getWidth() / viewport.length);
        int tileHeight = (getHeight() / viewport[1].length);
        //loop through the maze array and paint the tiles
        for (int i = 0; i < viewport.length; i++) {
            for (int j = 0; j < viewport[1].length; j++) {
                //clear the floor
                g.drawImage(TexturePack.Images.Floor.getImg(), i * tileWidth, j * tileHeight, tileWidth, tileHeight, null);
                // if there is a item draw on top of the floor or a wall tile
                Tile tile = viewport[i][j];
                if(tile.type() == TileType.Floor) {continue;}
                //if hero tile then draw the hero depending on the direction
                if(tile.type() == TileType.Hero) {
                    Hero hero = (Hero) tile;
                    BufferedImage img = getHeroImg(hero.dir());
                }else{
                    g.drawImage(TexturePack.Images.getImage(tile), i * tileWidth, j * tileHeight, tileWidth, tileHeight, null);
                }

            }
        }
    }

    private BufferedImage getHeroImg(Direction dir) {
        System.out.println(dir);
        switch(dir) {
            case Up:
                return TexturePack.Images.HeroBack.getImg();
            case Down:
                return TexturePack.Images.HeroFront.getImg();
            case Left:
                return TexturePack.Images.HeroLeft.getImg();
            case Right:
                return TexturePack.Images.HeroRight.getImg();
            default: return null;
        }
    }

    /**
     * set the current texturePack and returns the new background image
     * 
     * @param texturePack
     */
    public void setTexturePack(TexturePack texturePack) {
        this.texturePack = texturePack;
        MazeRenderer.currentTP = texturePack;
        TexturePack.Images.reloadAllTexturepack();
        patternSize = 100;
    }

    //------------------------------------------------------------------------------------------------//
    //getters and setters

    /**
     * get min render size
     * @return minRenderSize
     */
    public int getMinRenderSize() {return minRenderSize;}
    /**
     * get maximum render size
     * @return maxRenderSize
     */
    public int getMaxRenderSize() {return maxRenderSize;}
    

    /**
     * get render size
     * @return int
     */
    public int getRenderSize() {return renderSize;}

    /**
     * set render size
     * @param renderSize
     */
    public void setRenderSize(int renderSize) {this.renderSize = renderSize;}

    /**
     * getter for patternSize
     * @return patternSize
     */
    public int getPatternSize() {return patternSize;}
    /**
     * get current texture pack
     * @return texturePack
     */
    public TexturePack getCurrentTexturePack(){return texturePack;}

    /**
     * set the maze to be rendered
     * @param maze
     */
    public void setMaze(DomainController maze) {this.maze = maze;}

}
