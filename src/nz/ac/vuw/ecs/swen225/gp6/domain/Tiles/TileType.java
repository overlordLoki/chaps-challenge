package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.Maze;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Direction;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Loc;
//TODO:MAKE METHODS CHECK OTHER TYPES LESS (less type check)
//TODO: testing and ensuring some states wont be reached

/*
 * Each tile object will hold a reference to one TileTypeInterface instance, 
 * which determines the behaviour of a number of the tiles important methods.
 * 
 * Each enum must override a number of methods e.g: getSymbol
 * as well as choose to override or use the default version of some others  e.g: setOn, ping and isObstruction
 * 
 * Not all tile types will have an enum associated with them,
 * but all tiles must have a tileTypeInterface instance associated with them.
 * The enums tiletypes are the default tiles that are used in the game. 
 * 
 * Any tile types that are to be created in run time should be added 
 * with a tileTypeInterface instance that is not an enum.
 * This is through overriding tileTypeInterface interface dynamically.
 */
public enum TileType implements TileTypeInterface{
    //ACTORS:
    Hero('H'){
        @Override public boolean isObstruction(Tile t, Domain d) { return false;} 
        @Override public void setOn(Tile self, Tile t, Domain d){
            d.getCurrentMaze().setTileAt(self.info().loc(), t);
            d.getEventListener(Domain.DomainEvent.onLose).forEach(r -> r.run()); //LOSE (since only enemy can move on actor)
        }
        @Override public void ping(Tile self, Domain d) {
            Maze m = d.getCurrentMaze();
            Loc l1 = self.info().loc();
            //find new location of hero if it moves
            Direction dir = d.getCurrentMaze().getDirection();
            Loc l2 = dir.transformLoc(l1);

            //if hero hasnt moved or tile at new location is obstruction return
            if(dir == Direction.None || m.getTileAt(l2).type().isObstruction(self, d)) return;
            
            //otherwise set previous location to empty and move self to new location (order matters here) 
            m.getTileAt(l1).setOn(new Tile(TileType.Floor, new TileInfo(l1)), d);
            m.getTileAt(l2).setOn(self, d);

            //TODO remove
            //System.out.println( "Location x: " + self.info().loc().x() + " y: " + self.info().loc().y());
            //System.out.println( d.getCurrentMaze().toString());
            
            self.info().dir(m.getDirection()); //set heros direction of facing
            m.makeHeroStep(Direction.None); //make hero stop moving
        }
    },

    Enemy('E'){//TODO: delete this tile type??
        @Override public void setOn(Tile self, Tile t, Domain d){
            d.getCurrentMaze().setTileAt(self.info().loc(), t);
            d.getEventListener(Domain.DomainEvent.onLose).forEach(r -> r.run()); //LOSE (since only hero can move on enemy)
        }
        @Override public void ping(Tile self, Domain d){ }
    },

    //STATIC TERRAINS:
    Empty(' '){ //this is just used for the empty inventory tile
        @Override public boolean isObstruction(Tile t, Domain d) { return false;} //anyone can move on empty terrain
    },

    Floor('_'){
        @Override public boolean isObstruction(Tile t, Domain d) { return false;} //anyone can move on floor
        @Override public void setOn(Tile self, Tile t, Domain d){d.getCurrentMaze().setTileAt(self.info().loc(), t);}
    }, 

    Wall('/'){
        @Override public boolean isObstruction(Tile t, Domain d) { return true;} //no one can move on wall
    },


    //INTERACTIVE TERRAINS:
    Info('i'){ //future idea: not disappear after once usage
        @Override public void setOn(Tile self, Tile t, Domain d){d.getCurrentMaze().setTileAt(self.info().loc(), t);} 
        @Override public void ping(Tile self, Domain d){}//TODO: display info
    },

    ExitDoor('X'){
        @Override public boolean isObstruction(Tile t, Domain d) { return true;}//no one can move on exit door
        @Override public void ping(Tile self, Domain d) {
            //if all treasures collected replace exitdoor with open exit door
            if(d.getTreasuresLeft() == 0){
                d.getCurrentMaze().setTileAt(self.info().loc(), TileType.ExitDoorOpen); 
            }
        }
    },

    ExitDoorOpen('Z'){
        @Override public void setOn(Tile self, Tile t, Domain d){ 
            d.getEventListener(Domain.DomainEvent.onWin).forEach(r -> r.run());
        } //WIN
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
    @Override public char getSymbol() {return symbol;}
}


