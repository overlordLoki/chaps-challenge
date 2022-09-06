package Renderer;
import Renderer.tempDomain.*;
import Renderer.tempDomain.Tiles.Tile;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.Graphics;

/**
 * makes a jPanel that can be added to a JFrame
 * 
 * @author Loki
 */
public class Renderer extends JPanel{
    static final long serialVersionUID = 1L;
    //default texturePack
    private TexturePack texturePack = TexturePack.Cats;
    //maze array
    private Tile[][] gameArray;
    //the maze
    private Maze maze;

    public BufferedImage background;


    private static TexturePack currentTP = TexturePack.Cats;

    public enum TexturePack{
        Original, Cats, Dogs, Emoji;
    }

    public enum Images{
        Background("background"),
        
        Hero("hero"),
        Enemy("enemy"),
        
        Coin("coin"),
        BlueKey("blueKey"),
        GreenKey("greenKey"),
        OrangeKey("orangeKey"),
        YellowKey("yellowKey"),
        
        Empty_tile("empty_tile"),
        Wall("wall_tile"),
        BlueLock("blueLock"),
        GreenLock("greenLock"),
        OrangeLock("orangeLock"),
        YellowLock("yellowLock"),
        Exit("exitDoor");

        private String name;
        private BufferedImage img;

        Images(String path){
            this.img = loadImg(path);
        }

        public String getName(){
            return name;
        }

        public BufferedImage getImg(){
            return img;
        }

        public static BufferedImage getImage(Images img){
            return img.getImg();
        }

        public static BufferedImage getImage(Tile tile){
            System.out.println(tile.getImg()); 
            return switch(tile.getImg()){
                case "empty_tile" -> Images.Empty_tile.getImg();
                case "hero" -> Images.Hero.getImg();
                case "enemy" -> Images.Enemy.getImg();
                case "wall_tile" -> Images.Wall.getImg();
                case "blueKey" -> Images.BlueKey.getImg();
                case "greenKey" -> Images.GreenKey.getImg();
                case "yellowKey" -> Images.YellowKey.getImg();
                case "orangeKey" -> Images.OrangeKey.getImg();
                case "blueLock" -> Images.BlueLock.getImg();
                case "greenLock" -> Images.GreenLock.getImg();
                case "yellowLock" -> Images.YellowLock.getImg();
                case "orangeLock" -> Images.OrangeLock.getImg();
                case "exitDoor" -> Images.Exit.getImg();
                case "coin" -> Images.Coin.getImg();
                default -> throw new IllegalArgumentException("Unexpected value: " + tile.getClass().getName() + " : " + tile.getImg());
            };
        }

        public BufferedImage loadImg(String imageName){
            this.name = imageName;
            System.out.print("Loading " + imageName + "...    -> ");
            try {
                BufferedImage img = ImageIO.read(getClass().getResource("/Renderer/textures/" + currentTP + "/" + imageName + ".png"));
                System.out.println("Loaded!");
                return img;
            } catch (IOException e) {
                throw new RuntimeException(e);}
        }

        public static void reloadAllTexturepack(){
            for(Images i : Images.values()){
                i.img = i.loadImg(i.getName());
            }
        }
        
    }

    /**
     * Constructor. Takes a maze as parameters.
     * 
     * @param maze Maze to be rendered.
     */
    public Renderer(Maze maze) {
        this.maze = maze;
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
        Images.reloadAllTexturepack();
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

    public BufferedImage getImage(Images imgName) {
        return imgName.getImg();
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
        //loop through the maze array and paint the black tile as a background
        for (int i = 0; i < gameArray.length; i++) {
            for (int j = 0; j < gameArray[1].length; j++) {
                g.drawImage(Images.Empty_tile.getImg(), i * tileWidth, j * tileHeight, tileWidth, tileHeight, null);
            }
        }
        //loop through the maze array and paint the tiles
        for (int i = 0; i < gameArray.length; i++) {
            for (int j = 0; j < gameArray[1].length; j++) {
                Tile tile = gameArray[i][j];
                g.drawImage(Images.getImage(tile), i * tileWidth, j * tileHeight, tileWidth, tileHeight, null);
            }
        }
    }

}
