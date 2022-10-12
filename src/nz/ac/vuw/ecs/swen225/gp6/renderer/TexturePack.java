package nz.ac.vuw.ecs.swen225.gp6.renderer;

import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
public class TexturePack {
    private final String texturePackName;
    private final Font titleFont;
    private final Font subtitleFont;
    private final Font textFont;
    private final Color colorDefault = Color.BLACK;
    private final Color colorHover;
    private final Color colorSelected;
    private  Color dynamicColor;


    public String getName()         {return texturePackName;}
    public Font getTitleFont()      {return titleFont;}
    public Font getSubtitleFont()   {return subtitleFont;}
    public Font getTextFont()       {return textFont;}
    public Color getColorDefault()  {return colorDefault;}
    public Color getColorHover()    {return colorHover;}
    public Color getColorSelected() {return colorSelected;}
    public Color getDynamicColor() {return dynamicColor;}
    public void setDynamicColor(Color color) {dynamicColor = color;}

    /**
     * Constructor for texture packs
     * @param tile
     * @param subtitle
     * @param text
     * @param colorDefault
     * @param colorHover
     * @param colorSelected
     */
    TexturePack(String name,Font title,Font subtitle, Font text, Color colorHover, Color colorSelected){
        this.texturePackName = name;
        this.titleFont = title;
        this.subtitleFont = subtitle;
        this.textFont = text;
        this.colorHover = colorHover;
        this.colorSelected = colorSelected;
        this.dynamicColor = colorDefault;
    }

    //get image enum
    public BufferedImage getImage(Tile tile) {
        return Images.getImage(tile);
    }
    //get image from string
    public BufferedImage getImage(String imgName) {
        return Images.getImage(imgName);
    }

    public enum Images{

        /**
         * The background image for the game.
         */
        Background("background"),
    
        /**
         * The image for the repeatable pattern background.
         */
        Pattern("pattern"),
        /**
         * The image for the repeatable pattern background.
         */
        Pattern_2("pattern2"),
    
        /**
         * The image for the floor.
         */
        Floor("floor"),
        /**
         * The image for the hero.
         */
        Hero("hero"),
        /**
         * The image for the enemy.
         */
        Enemy("enemy"),
        
        /**
         * The image for the coin.
         */
        Coin("coin"),
        /**
         * The image for the blue key.
         */
        BlueKey("blueKey"),
        /**
         * The image for the green key.
         */
        GreenKey("greenKey"),
        /**
         * The image for the orange key.
         */
        OrangeKey("orangeKey"),
        /**
         * The image for the yellow key.
         */
        YellowKey("yellowKey"),
        /**
         * The image for the empty tile.
         */
        Empty_tile("empty_tile"),
        /**
         * The image for the wall.
         */
        Wall("wall_tile"),
        /**
         * The image for the blueLock
         */
        BlueLock("blueLock"),
        /**
         * The image for the greenLock
         */
        GreenLock("greenLock"),
        /**
         * The image for the orangeLock
         */
        OrangeLock("orangeLock"),
        /**
         * The image for the yellowLock
         */
        YellowLock("yellowLock"),
        /**
         * The image for the exit
         */
        Exit("exitDoor"),
        /**
         * The image for the win screen
         */
        WinScreen("winScreen"),
        /**
         * The image for the lose screen
         */
        LoseScreen("loseScreen"),

        /**
         * The image for the help tile
         */
        InfoTile("help"),
    
        HeroBack("heroBack"),
        HeroFront("heroFront"),
        HeroLeft("heroSide"),
        HeroRight("hero");
        
        //name of the image
        private String name;
        //the image we store in ram.
        private BufferedImage img;
        
        /*constructor for the enum, loads the image and keeps it as a veraible so we dont need to 
          reload the file from disk everytime we redraw */
        Images(String path){this.img = loadImg(path);}
        /**
         * get the name
         * @return String
         */
        public String getName(){return name;}
        /**
         * get the image that is cashed
         * @return BufferedImage
         */
        public BufferedImage getImg(){return img;}
        
        /**
         * get the image.
         * @param path
         * @return BufferedImage
         */
        public static BufferedImage getImage(Images img){return img.getImg();}
        
        /**
         * get the image for the Tile provided.
         * @param tile
         * @return BufferedImage
         */
        public static BufferedImage getImage(Tile tile){
            return switch(tile.type()){
                case Floor -> getImage(Empty_tile);
                case Empty -> Images.Empty_tile.getImg();
                case Wall -> Images.Wall.getImg();
                case BlueKey -> Images.BlueKey.getImg();
                case GreenKey -> Images.GreenKey.getImg();
                case YellowKey -> Images.YellowKey.getImg();
                case OrangeKey -> Images.OrangeKey.getImg();
                case BlueLock -> Images.BlueLock.getImg();
                case GreenLock -> Images.GreenLock.getImg();
                case YellowLock -> Images.YellowLock.getImg();
                case OrangeLock -> Images.OrangeLock.getImg();
                case ExitDoor -> Images.Exit.getImg();
                case ExitDoorOpen -> Images.Exit.getImg();
                case Info -> Images.Empty_tile.getImg();
                case Coin -> Images.Coin.getImg();
                case Periphery -> Images.Pattern.getImg();
                default -> Images.loadCustom(tile.info().getImageName());
            };
        }

        /**
         * get the image for the String provided.
         * @param String
         * @return BufferedImage
         */
        public static BufferedImage getImage(String imgName){
            for (Images img : Images.values()) {
                if (img.getName().equals(imgName)) {
                    return img.getImg();
                }
            }
            System.out.println("Image not found: " + imgName);
            return null;
        }
        
        /**
         * load the image from the file.
         * @param path
         * @return BufferedImage
         */
        public static BufferedImage loadCustom(String path){
            for (Images img : Images.values()) {
                if (img.getName().equals(path)) {
                    return img.getImg();
                }
            }
            try {
                return ImageIO.read(new File("res/customTextures/"+path+".png"));
            } catch (IOException e) {
                try{
                    return ImageIO.read(new File("res/customTextures/default.png"));
                }catch (IOException|IllegalArgumentException ex){
                    System.out.println("Error loading image: " + path); return null;}}
        }
        
        /**
         * load the image from the disk
         * @param path
         * @return BufferedImage
         */
        public BufferedImage loadImg(String imageName){
            this.name = imageName;
            //System.out.print("Loading " + imageName + "...    -> ");
            try {
                File file = new File("res/textures/" + MazeRenderer.getTexturePack().getName() + "/" + imageName + ".png");
                BufferedImage img = ImageIO.read(file);
                //System.out.println("Loaded!");
                return img;
            } catch (IOException e) {
                throw new RuntimeException(e);}
        }
        
        /**
         * reload all the images and cashs them when changing texture packs.
         */
        public static void reloadAllTexturepack(){
            for(Images i : Images.values()){
                i.img = i.loadImg(i.getName());
            }
        }
        
    }
    
}
