package Renderer;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import Renderer.tempDomain.Tiles.Tile;

public enum TexturePack{
    Original, Cats, Dogs, Emoji;

    public enum Images{
        Background("background"),
        Pattern("pattern"),
        
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