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
            Loc newLoc = m.getDirection().transformLoc(self.info().loc()); //new loc to move
            Tile newTil = m.getTileAt(newLoc); //tile at new loc

            if(newTil.isObstruction(self, d) == false){
                m.setTileAt(self.info().loc(), Floor, l -> {}); //set previous tile to floor
                newTil.setOn(self, d);//if movable, move
            }
        
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
            if(d.getCurrentMaze().getTileCount(TileType.Coin) - d.getInv().coins() == 0){
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
    public char getSymbol(){ return symbol;}
    public boolean isObstruction(Tile t, Domain d){return t.type() != TileType.Hero;}
    public void setOn(Tile self, Tile t, Domain d){}
    public void ping(Tile self, Domain d){}
}

