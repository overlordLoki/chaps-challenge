package nz.ac.vuw.ecs.swen225.gp6.domain.TileGrouping;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.Maze;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Direction;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Loc;

/**
 * Enum for tile names but also more importantly where the specific tile types are implemented with 
 * dynamic dispatching.
 * 
 * TODO: THINK OF BEST STRUCTURE FOR SPECIFIC TILE IMPLEMENTATIONS
 */
public enum TileType{
    //ACTORS
    Hero{
        public Tile makeTileObject(TileInfo info){
            return new Actor(){
                @Override public TileType getType(){ return Hero;}
                @Override public char getSymbol(){return 'A';}
                @Override public TileInfo getInfo(){return info;}
                @Override public boolean canMoveOn(Actor a, Domain d) { return a.getType() == TileType.Enemy;} //enemy can move on actor
                @Override public void setOn(Actor a, Domain d){}//TODO: LOSE 
                @Override public void ping(Domain d) {
                    Maze m = d.getCurrentMaze();
                    Loc newLoc = m.getDirection().transformLoc(info.loc()); //new loc to move
                    Tile newTil = m.getTileAt(newLoc); //tile at new loc

                    if(newTil.canMoveOn(this, d)){
                        m.setTileAt(info.loc(), Floor, a -> {}); //set previous tile to floor
                        newTil.setOn(this, d);//if movable, move
                    }
                    
                
                    this.staticDirection = m.getDirection(); //set heros direction of facing
                    m.makeHeroStep(Direction.None); //make hero stop moving

                }
            };
        }
    },
    Enemy{
        public Tile makeTileObject(TileInfo info){
            return new Actor(){
                @Override public TileType getType(){ return Enemy;}
                @Override public char getSymbol(){return 'E';}
                @Override public TileInfo getInfo(){return info;}
                @Override public boolean canMoveOn(Actor a, Domain d) { return a.getType() == TileType.Hero;} //hero can move on enemy
                @Override public void setOn(Actor a, Domain d){}//TODO: LOSE
                @Override public void ping(Domain d){ info.consumer().accept(d);}
                
            };
        }
    },

    //TERRAINS
    Empty{
        public Tile makeTileObject(TileInfo info){
            return new Tile(){
                @Override public TileType getType(){ return Empty;}
                @Override public char getSymbol(){return ' ';}
                @Override public TileInfo getInfo(){return info;}
                @Override public boolean canMoveOn(Actor a, Domain d) { return true;} //anyone can move on empty terrain
                @Override public void setOn(Actor a, Domain d){d.getCurrentMaze().setTileAt(info.loc(), a);}
            };
        }
    },
    Floor{
        public Tile makeTileObject(TileInfo info){
            return new Tile(){
                @Override public TileType getType(){ return Floor;}
                @Override public char getSymbol(){return '_';}
                @Override public TileInfo getInfo(){return info;}
                @Override public boolean canMoveOn(Actor a, Domain d) { return true;} //anyone can move on floor
                @Override public void setOn(Actor a, Domain d){d.getCurrentMaze().setTileAt(info.loc(), a);}
            };
        }
    },
    Wall{
        public Tile makeTileObject(TileInfo info){
            return new Tile(){
                @Override public TileType getType(){ return Wall;}
                @Override public char getSymbol(){return '|';}
                @Override public TileInfo getInfo(){return info;}
                @Override public boolean canMoveOn(Actor a, Domain d) { return false;} //no one can move on wall
            };
        }
    },

    //INTERACTIVE TERRAINS
    ExitDoor{
        public Tile makeTileObject(TileInfo info){
            return new Tile(){
                @Override public TileType getType(){ return ExitDoor;}
                @Override public char getSymbol(){return 'X';}
                @Override public TileInfo getInfo(){return info;}
                @Override public boolean canMoveOn(Actor a, Domain d) { return false;}
                @Override public void ping(Domain d) {
                    //if all treasures collected replace exitdoor with open exit door
                    if(d.getCurrentMaze().getTileCount(TileType.Coin) - d.getInv().coins() == 0){
                        d.getCurrentMaze().setTileAt(info.loc(), TileType.ExitDoorOpen, a->{}); 
                    }
                }
            };
        }
    },
    ExitDoorOpen{
        public Tile makeTileObject(TileInfo info){
            return new Tile(){
                @Override public TileType getType(){ return ExitDoorOpen;}
                @Override public char getSymbol(){return 'Z';}
                @Override public TileInfo getInfo(){return info;}
                @Override public boolean canMoveOn(Actor a, Domain d) { return a.getType() == TileType.Hero;}
                @Override public void setOn(Actor a, Domain d){}//TODO: WIN
            };
        }
    },

    BlueLock{ 
        public Tile makeTileObject(TileInfo info){
            return new Tile(){
                @Override public TileType getType(){ return BlueLock;}
                @Override public char getSymbol(){return 'B';}
                @Override public TileInfo getInfo(){return info;}
                @Override public boolean canMoveOn(Actor a, Domain d){ return a.getType() == TileType.Hero && d.getInv().hasItem(BlueKey);}
                @Override public void setOn(Actor a, Domain d){ 
                    d.getInv().removeItem(BlueKey);
                    d.getCurrentMaze().setTileAt(info.loc(), a);
                }
            };
        }
    },
    GreenLock{
        public Tile makeTileObject(TileInfo info){
            return new Tile(){
                @Override public TileType getType(){ return GreenLock;}
                @Override public char getSymbol(){return 'G';}
                @Override public TileInfo getInfo(){return info;}
                @Override public boolean canMoveOn(Actor a, Domain d){ return a.getType() == TileType.Hero && d.getInv().hasItem(GreenKey);}
                @Override public void setOn(Actor a, Domain d){ 
                    d.getInv().removeItem(GreenLock);
                    d.getCurrentMaze().setTileAt(info.loc(), a);
                }
            };
        }
    },
    OrangeLock{
        public Tile makeTileObject(TileInfo info){
            return new Tile(){
                @Override public TileType getType(){ return OrangeLock;}
                @Override public char getSymbol(){return 'O';}
                @Override public TileInfo getInfo(){return info;}
                @Override public boolean canMoveOn(Actor a, Domain d){ return a.getType() == TileType.Hero && d.getInv().hasItem(OrangeKey);}
                @Override public void setOn(Actor a, Domain d){ 
                    d.getInv().removeItem(OrangeKey);
                    d.getCurrentMaze().setTileAt(info.loc(), a);
                }
            };
        }
    },
    YellowLock{
        public Tile makeTileObject(TileInfo info){
            return new Tile(){
                @Override public TileType getType(){ return YellowLock;}
                @Override public char getSymbol(){return 'Y';}
                @Override public TileInfo getInfo(){return info;}
                @Override public boolean canMoveOn(Actor a, Domain d){ return a.getType() == TileType.Hero && d.getInv().hasItem(YellowKey);}
                @Override public void setOn(Actor a, Domain d){ 
                    d.getInv().removeItem(YellowKey);
                    d.getCurrentMaze().setTileAt(info.loc(), a);
                }
            };
        }
    },



    //ITEMS
    BlueKey{
        public Tile makeTileObject(TileInfo info){
            return new Item(){
                @Override public TileType getType(){ return BlueKey;}
                @Override public char getSymbol(){return 'b';}
                @Override public TileInfo getInfo(){return info;}
                @Override public void setOn(Actor a, Domain d){ 
                    d.getInv().addItem(this);
                    d.getCurrentMaze().setTileAt(info.loc(), a);
                }
            };
        }
    },
    GreenKey{
        public Tile makeTileObject(TileInfo info){
            return new Item(){
                @Override public TileType getType(){ return GreenKey;}
                @Override public char getSymbol(){return 'g';}
                @Override public TileInfo getInfo(){return info;}
                @Override public void setOn(Actor a, Domain d){ 
                    d.getInv().addItem(this);
                    d.getCurrentMaze().setTileAt(info.loc(), a);
                }
            };
        }
    },
    OrangeKey{
        public Tile makeTileObject(TileInfo info){
            return new Item(){
                @Override public TileType getType(){ return OrangeKey;}
                @Override public char getSymbol(){return 'o';}
                @Override public TileInfo getInfo(){return info;}
                @Override public void setOn(Actor a, Domain d){ 
                    d.getInv().addItem(this);
                    d.getCurrentMaze().setTileAt(info.loc(), a);
                }
            };
        }
    },
    YellowKey{
        public Tile makeTileObject(TileInfo info){
            return new Item(){
                @Override public TileType getType(){ return YellowKey;}
                @Override public char getSymbol(){return 'y';}
                @Override public TileInfo getInfo(){return info;}
                @Override public void setOn(Actor a, Domain d){ 
                    d.getInv().addItem(this);
                    d.getCurrentMaze().setTileAt(info.loc(), a);
                }
            };
        }
    },

    Coin{
        public Tile makeTileObject(TileInfo info){
            return new Item(){
                @Override public TileType getType(){ return Coin;}
                @Override public char getSymbol(){return 'C';}
                @Override public TileInfo getInfo(){return info;}
                @Override public boolean canMoveOn(Actor a, Domain d) { return true;}
                @Override public void setOn(Actor a, Domain d){ 
                    d.getInv().addCoin();
                    d.getCurrentMaze().setTileAt(info.loc(), a);
                }
            };
        }
    },

    //SPECIAL TILES
    Null{
        public Tile makeTileObject(TileInfo info){
            return new Tile(){
                @Override public TileType getType(){ return Null;}
                @Override public TileInfo getInfo(){return info;}
                @Override public boolean canMoveOn(Actor a, Domain d) { return false;}
                @Override public char getSymbol(){return Character.MIN_VALUE;}
            };
        }
    };

    /**
     * @return a NEW tile object associated with the enum
     * 
     */
    public abstract Tile makeTileObject(TileInfo info);
}



enum type{
    etx(()->{System.out.println("floor");}){
        public void printlosbeofib(){
            System.out.println("oiboeib");
        }
    };



    Runnable onPing;
    type(Runnable onPing){
        this.onPing = onPing;
    }
    public void ping(){
        onPing.run();
    }
    public void printlosbeofib(){
        System.out.println("sef");
    }
}

