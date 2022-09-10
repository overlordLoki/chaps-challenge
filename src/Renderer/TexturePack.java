package Renderer;

import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.IOException;

import javax.imageio.ImageIO;

import Renderer.tempDomain.Tiles.Tile;

public enum TexturePack{
    Original("Comic Sans MS",Font.BOLD, Color.BLACK, Color.ORANGE, Color.RED, 80, 40, 30),
    Cats("Agency FB", Font.BOLD,  Color.BLACK, Color.ORANGE, Color.RED, 80, 40, 30),
    Dogs("Agency FB", Font.BOLD,  Color.BLACK, Color.ORANGE, Color.RED, 80, 40, 30),
    Emoji("Agency FB", Font.BOLD, Color.BLACK, Color.ORANGE, Color.RED, 80, 40, 30);
    
    private final String font;
    private final int style;
    private final Color colorDefault;
    private final Color colorHover;
    private final Color colorSelected;
    private final int TitleSize;
    private final int SubtitleSize;
    private final int TextSize;

    public String getFont() {return font;}
    public int getStyle() {return style;}
    public Color getColorDefault() {return colorDefault;}
    public Color getColorHover() {return colorHover;}
    public Color getColorSelected() {return colorSelected;}
    public int getTitleSize() {return TitleSize;}
    public int getSubtitleSize() {return SubtitleSize;}
    public int getTextSize() {return TextSize;}

    TexturePack(String font, int style, Color colorDefault, Color colorHover, Color colorSelected, int TitleSize, int SubtitleSize, int TextSize){
        this.font = font;
        this.style = style;
        this.colorDefault = colorDefault;
        this.colorHover = colorHover;
        this.colorSelected = colorSelected;
        this.TitleSize = TitleSize;
        this.SubtitleSize = SubtitleSize;
        this.TextSize = TextSize;
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
                BufferedImage img = ImageIO.read(getClass().getResource("/Renderer/textures/" + Renderer.currentTP + "/" + imageName + ".png"));
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