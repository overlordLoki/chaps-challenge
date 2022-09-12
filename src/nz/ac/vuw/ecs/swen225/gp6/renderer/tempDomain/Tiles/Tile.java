package nz.ac.vuw.ecs.swen225.gp6.renderer.tempDomain.Tiles;

public interface Tile {
    public String img = "";
    public default String getImg(){
        return img;
    }

}
