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

    @Override public boolean obstructsEnemy(Domain d){  
        return true;} //only hero can move on it
    @Override public boolean obstructsHero(Domain d){  return false;}

    @Override public void setOn(Tile t, Domain d){
        if(t.type() != TileType.Hero){ throw new IllegalArgumentException("Only the hero can move on exit door.");}
        if(d == null) throw new NullPointerException("Domain can not be null (ExitDoorOpen.setOn).");

        heroOn = true; //record that hero is now on exit door
                                        
        //let CheckGame know that the the game is won/inbetween levels
        if(d.isLastLevel()) CheckGame.state = CheckGame.GameState.WON;
        else CheckGame.state = CheckGame.GameState.BETWEENLEVELS;
            
        d.getEventListener(Domain.DomainEvent.onWin).forEach(r -> r.run()); //NEXT LEVEL/WIN
    } 
}
            
