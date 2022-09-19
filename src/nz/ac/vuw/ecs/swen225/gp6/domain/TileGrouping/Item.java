package nz.ac.vuw.ecs.swen225.gp6.domain.TileGrouping;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;

//ITEM GROUPS
public abstract class Item implements Tile{
    public boolean inInv = false;
    public boolean canMoveOn(Actor a, Domain d) { return a.getType() == TileType.Hero;} //hero can move on any item
}
