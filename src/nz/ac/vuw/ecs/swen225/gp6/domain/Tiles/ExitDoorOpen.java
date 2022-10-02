package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.IntegrityCheck.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Key.KeyColor;

public class ExitDoorOpen extends Door{

    public ExitDoorOpen (TileInfo info){super(info);}

    @Override public TileType type(){ return TileType.ExitDoorOpen;}
    @Override public KeyColor color(){return KeyColor.NONE;}

    @Override public boolean obstructsEnemy(Domain d){  return true;} //only hero can move on it
    @Override public boolean obstructsHero(Domain d){  return false;}

    @Override public void setOn(Tile t, Domain d){
        //if the tile is hero, WIN
        if(t.type() == TileType.Hero){
            d.getEventListener(Domain.DomainEvent.onWin).forEach(r -> r.run());
            CheckGame.gameHasEnded = true; //let the integrity checker know the game has ended
        } 
    }
            
}
