package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import java.util.function.*;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.Maze;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Direction;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Loc;
//TODO: FINISH AND SHORTEN SOME METHODS
//TODO: testing and ensuring some states wont be reached

/*
 * Each tile object will hold a reference to one TileState enum, 
 * which determines the behaviour of a number of the tiles important methods.
 * 
 * Each enum must override a number of methods e.g: getSymbol and isObstruction
 * as well as choose to override or use the default version of some others  e.g: setOn and ping
 */
public enum TileType {
    //ACTORS:
    Hero('H'){
        @Override public boolean isObstruction(Tile t, Domain d) { return t.type() != TileType.Enemy;} //enemy can move on actor
        @Override public void setOn(Tile self, Tile a, Domain d){}//TODO: LOSE 
        @Override public void ping(Tile self, Domain d) {
            Maze m = d.getCurrentMaze();
            Loc l1 = self.info().loc();
            //find new location of hero if it moves
            Direction dir = self.info().dir();
            Loc l2 = dir.transformLoc(l1);

            if(dir == Direction.None) return; //if hero is not moving, do nothing

            //if tile at new location is obstruction return
            if(m.getTileAt(l2).type().isObstruction(self, d)) return;
            
            //otherwise move self to new location and set previous location to empty
            m.getTileAt(l2).setOn(self, d);
            m.getTileAt(l1).setOn(new Tile(TileType.Empty, new TileInfo(l1, a->{})), d);

            self.info().dir(m.getDirection()); //set heros direction of facing
            m.makeHeroStep(Direction.None); //make hero stop moving
        }
    },

    Enemy('E'){
        @Override public void setOn(Tile self, Tile t, Domain d){}//TODO: LOSE
        @Override public void ping(Tile self, Domain d){ self.info().consumer().accept(d);}
    },

    //STATIC TERRAINS:
    Empty(' '){
        @Override public boolean isObstruction(Tile t, Domain d) { return false;} //anyone can move on empty terrain
        @Override public void setOn(Tile self, Tile t, Domain d){d.getCurrentMaze().setTileAt(self.info().loc(), t);}
    },

    Floor('_'){
        @Override public boolean isObstruction(Tile t, Domain d) { return false;} //anyone can move on floor
        @Override public void setOn(Tile self, Tile t, Domain d){d.getCurrentMaze().setTileAt(self.info().loc(), t);}
    }, 

    Wall('|'){
        @Override public boolean isObstruction(Tile t, Domain d) { return true;} //no one can move on wall
    },


    //INTERACTIVE TERRAINS:
    ExitDoor('X'){
        @Override public boolean isObstruction(Tile t, Domain d) { return true;}//no one can move on exit door
        @Override public void ping(Tile self, Domain d) {
            //if all treasures collected replace exitdoor with open exit door
            if(d.getTreasuresLeft() == 0){
                d.getCurrentMaze().setTileAt(self.info().loc(), TileType.ExitDoorOpen, a->{}); 
            }
        }
    },

    ExitDoorOpen('Z'){
        @Override public void setOn(Tile self, Tile t, Domain d){}//TODO: WIN
    },

    BlueLock('B'){
        @Override public boolean isObstruction(Tile t, Domain d){ 
            return !(t.type() == TileType.Hero && d.getInv().hasItem(BlueKey));
        }
        @Override public void setOn(Tile self, Tile t, Domain d){ 
            d.getInv().removeItem(BlueKey);
            d.getCurrentMaze().setTileAt(self.info().loc(), t);
        }
    },

    GreenLock('G'){
        @Override public boolean isObstruction(Tile t, Domain d){ 
            return !(t.type() == TileType.Hero && d.getInv().hasItem(GreenKey));
        }
        @Override public void setOn(Tile self, Tile t, Domain d){ 
            d.getInv().removeItem(GreenKey);
            d.getCurrentMaze().setTileAt(self.info().loc(), t);
        }
    },

    OrangeLock('O'){
        @Override public boolean isObstruction(Tile t, Domain d){ 
            return !(t.type() == TileType.Hero && d.getInv().hasItem(OrangeKey));
        }
        @Override public void setOn(Tile self, Tile t, Domain d){ 
            d.getInv().removeItem(OrangeKey);
            d.getCurrentMaze().setTileAt(self.info().loc(), t);
        }
    },

    YellowLock('Y'){
        @Override public boolean isObstruction(Tile t, Domain d){ 
            return !(t.type() == TileType.Hero && d.getInv().hasItem(YellowKey));
        }
        @Override public void setOn(Tile self, Tile t, Domain d){ 
            d.getInv().removeItem(YellowKey);
            d.getCurrentMaze().setTileAt(self.info().loc(), t);
        }
    },

    //PICKABLES(ITEMS):
    BlueKey('b'){
        @Override public void setOn(Tile self, Tile t, Domain d){ 
            d.getInv().addItem(self);
            d.getCurrentMaze().setTileAt(self.info().loc(), t);
        }
    },

    GreenKey('g'){
        @Override public void setOn(Tile self, Tile t, Domain d){ 
            d.getInv().addItem(self);
            d.getCurrentMaze().setTileAt(self.info().loc(), t);
        }
    },

    OrangeKey('o'){
        @Override public void setOn(Tile self, Tile t, Domain d){ 
            d.getInv().addItem(self);
            d.getCurrentMaze().setTileAt(self.info().loc(), t);
        }

    },

    YellowKey('y'){
        @Override public void setOn(Tile self,Tile t, Domain d){ 
            d.getInv().addItem(self);
            d.getCurrentMaze().setTileAt(self.info().loc(), t);
        }
    },

    Coin('C'){
        @Override public void setOn(Tile self, Tile t, Domain d){ 
            d.getInv().addCoin();
            d.getCurrentMaze().setTileAt(self.info().loc(), t);
        }
    }, 

    //SPECIAL:
    Null(Character.MIN_VALUE){ //this state won't be in game anywhere, its here just for aid in code
        @Override public boolean isObstruction(Tile t, Domain d) { return false;}
    };

    //CONSTRUCTOR
    TileType(char symbol){this.symbol = symbol;}
    
    //FIELDS:
    private char symbol;

    //METHODS:
    /**
     * returns character symbol of tiletype associated with enum.
     */
    public char getSymbol(){ return symbol;}
    /**
     * Checks wether the associated tile to this type is an obstruction for another given tile t, 
     * in a given domain.
     * NOTE: does not alter the tile, maze or actor in anyway.
     */
    public boolean isObstruction(Tile t, Domain d){return t.type() != TileType.Hero;}
    /**
     * Sets the given tile t instead of the associated tile to this type on maze, changing the domain to do so.
     * NOTE: should not check wether it's possible for tile t to move on this tile!
     */
    public void setOn(Tile self, Tile t, Domain d){}
    /**
     * Calculates the next state of the tile in the domain(maze/inventory).
     * Based on the tile and domain state, this method may alter the state of the tile and given domain object.
     */
    public void ping(Tile self, Domain d){}
}

