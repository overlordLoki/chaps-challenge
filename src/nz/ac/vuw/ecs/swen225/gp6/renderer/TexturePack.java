package nz.ac.vuw.ecs.swen225.gp6.renderer;

import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.IOException;

import javax.imageio.ImageIO;

import nz.ac.vuw.ecs.swen225.gp6.renderer.tempDomain.Tiles.Tile;

public enum TexturePack{
    Original(new Font("Arial", Font.BOLD, 80),
            new Font("Arial", Font.BOLD, 40),
            new Font("Arial", Font.BOLD, 30),
            Color.BLACK, Color.ORANGE, Color.RED),
    Cats(new Font("Agency FB", Font.BOLD, 80),
            new Font("Agency FB", Font.BOLD, 40),
            new Font("Agency FB", Font.BOLD, 30),
            Color.BLACK, Color.ORANGE, Color.RED),
    Dogs(new Font("Agency FB", Font.BOLD, 80),
            new Font("Agency FB", Font.BOLD, 40),
            new Font("Agency FB", Font.BOLD, 30),
            Color.BLACK, Color.ORANGE, Color.RED),
    Emoji(new Font("Comic Sans MS", Font.BOLD, 80),
            new Font("Comic Sans MS", Font.BOLD, 40),
            new Font("Comic Sans MS", Font.BOLD, 30),
            Color.BLACK, Color.ORANGE, Color.RED);

    private final Font titleFont;
    private final Font subtitleFont;
    private final Font textFont;
    private final Color colorDefault;
    private final Color colorHover;
    private final Color colorSelected;

    public Font getTitleFont()      {return titleFont;}
    public Font getSubtitleFont()   {return subtitleFont;}
    public Font getTextFont()       {return textFont;}
    public Color getColorDefault()  {return colorDefault;}
    public Color getColorHover()    {return colorHover;}
    public Color getColorSelected() {return colorSelected;}

    TexturePack(Font title,Font subtitle, Font text, Color colorDefault, Color colorHover, Color colorSelected){
        this.titleFont = title;
        this.subtitleFont = subtitle;
        this.textFont = text;
        this.colorDefault = colorDefault;
        this.colorHover = colorHover;
        this.colorSelected = colorSelected;
    }

    public enum Images{
        Background("background"),
        Pattern("pattern"),
        Pattern_2("pattern2"),
        Floor("floor"),
        
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
            return switch(tile.getImg()){
                case "floor" -> Images.Floor.getImg();
                case "empty_tile" -> Images.Empty_tile.getImg();
                case "hero" -> Images.Hero.getImg();
                case "pattern" -> Images.Pattern.getImg();
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
                BufferedImage img = ImageIO.read(getClass().getResource("/nz/ac/vuw/ecs/swen225/gp6/renderer/textures/" + Renderer.currentTP + "/" + imageName + ".png"));
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
}