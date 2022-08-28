package Persistency.Tiles;

public interface Tile {
    public String img = "";
    public default String getImg(){
        return img;
    }

}
