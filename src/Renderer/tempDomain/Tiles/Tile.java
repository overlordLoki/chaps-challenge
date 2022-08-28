package Renderer.tempDomain.Tiles;

public interface Tile {
    public String img = "";
    public default String getImg(){
        return img;
    }

}
