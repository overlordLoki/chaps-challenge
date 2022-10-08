package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Door;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Key.KeyColor;

public class ExitDoor extends Door{
    
    public ExitDoor(TileInfo info){super(info);}

    @Override public TileType type(){ return TileType.ExitDoor;}
    @Override public KeyColor color(){return KeyColor.NONE;}

    @Override public boolean obstructsHero(Domain d){  return true;}//no one can go through un opened exit door
    @Override public boolean obstructsEnemy( Domain d) { return true;}
}
