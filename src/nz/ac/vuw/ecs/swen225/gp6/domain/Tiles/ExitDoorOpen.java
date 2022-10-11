package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.IntegrityCheck.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Key.KeyColor;

/**
 * A class representing a exit door that represents the exit door when all coins/treasures 
 * are collected an the player can pass through it to pass the level.
 */
public class ExitDoorOpen extends Door{
    private static boolean heroOn = false;

    /**
     * Create an open exit door open tile
     * @param info tile information
     */
    public ExitDoorOpen (TileInfo info){super(info);}

    @Override public TileType type(){ return TileType.ExitDoorOpen;}
    @Override public KeyColor color(){return KeyColor.NONE;}
    /**
     * @return true if hero is on the tile, false otherwise
     */
    public boolean heroOn(){return heroOn;}

    @Override public boolean obstructsEnemy(Domain d){  return true;} //only hero can move on it
    @Override public boolean obstructsHero(Domain d){  return false;}

    @Override public void setOn(Tile t, Domain d){
        //if the tile is hero, WIN
        if(t.type() == TileType.Hero){
            heroOn = true; 
                                           
            //let CheckGame know that the the game is won/inbetween levels
            if(d.getMazes().size() == d.getCurrentLevel()){ 
                CheckGame.state = CheckGame.GameState.WON;
            } else{
                CheckGame.state = CheckGame.GameState.BETWEENLEVELS;
            }
            
            d.getEventListener(Domain.DomainEvent.onWin).forEach(r -> r.run());
        } 
    }
            
}
