package nz.ac.vuw.ecs.swen225.gp6.domain;

/**
 * Enum for tile names but also more importantly where the specific tile types are implemented with 
 * dynamic dispatching.
 */
public enum TileType{
    //actors
    Hero{
        public Tile getTileObject(TileInfo info){
            return new Actor(){
                public TileType getType(){ return Hero;}
                public boolean canMoveOn(Actor a, Maze m) { return a.getType() == TileType.Enemy;} //enemy can move on hero
                public void ping(Maze m) {
                    Loc newl = m.getDirection().transformLoc(info.loc);
                    if(Loc.checkInBound(newl, m) && m.getTileAt(newl).canMoveOn(this, m)){
                        
                    }
                    
                };
            };
        }
    },
    Enemy{
        public Tile getTileObject(TileInfo info){
            return new Actor(){
                public TileType getType(){ return Enemy;}
                public boolean canMoveOn(Actor a, Maze m) { return a.getType() == TileType.Hero;} //hero can move on enemy
                public void ping(Maze m){
                    // TODO: 
                }
            };
        }
    },

    //terrains
    Empty{
        public Tile getTileObject(TileInfo info){
            return new EmptyTile(){
                public TileType getType(){ return Empty;}
                public boolean canMoveOn(Actor a, Maze m) { return true;} //anyone can move on empty terrain
            };
        }
    },
    Floor{
        public Tile getTileObject(TileInfo info){
            return new EmptyTile(){
                public TileType getType(){ return Floor;}
                public boolean canMoveOn(Actor a, Maze m) { return true;} //anyone can move on floor
            };
        }
    },
    Wall{
        public Tile getTileObject(TileInfo info){
            return new Tile(){
                public TileType getType(){ return Wall;}
                public boolean canMoveOn(Actor a, Maze m) { return false;} //no one can move on wall
            };
        }
    },

    //interactive terrains
    ExitDoor{
        public Tile getTileObject(TileInfo info){
            return new Tile(){
                public TileType getType(){ return ExitDoor;}
                public boolean canMoveOn(Actor a, Maze m) { return false;}
                public void ping(Maze m) {
                    // TODO: 
                };
            };
        }
    },
    ExitDoorOpen{
        public Tile getTileObject(TileInfo info){
            return new Tile(){
                public TileType getType(){ return ExitDoorOpen;}
                public boolean canMoveOn(Actor a, Maze m) { return a.getType() == TileType.Hero;}
            };
        }
    },

    BlueLock{
        public Tile getTileObject(TileInfo info){
            return new Lock(){
                public TileType getType(){ return BlueKey;}
                public boolean canMoveOn(Actor a, Maze m){ return a.getType() == TileType.Hero && m.getInv().hasItem(BlueKey);}
            };
        }
    },
    GreenLock{
        public Tile getTileObject(TileInfo info){
            return new Lock(){
                public TileType getType(){ return GreenLock;}
                public boolean canMoveOn(Actor a, Maze m){ return a.getType() == TileType.Hero && m.getInv().hasItem(GreenKey);}
            };
        }
    },
    OrangeLock{
        public Tile getTileObject(TileInfo info){
            return new Lock(){
                public TileType getType(){ return OrangeKey;}
                public boolean canMoveOn(Actor a, Maze m){ return a.getType() == TileType.Hero && m.getInv().hasItem(OrangeKey);}
            };
        }
    },
    YellowLock{
        public Tile getTileObject(TileInfo info){
            return new Lock(){
                public TileType getType(){ return YellowLock;}
                public boolean canMoveOn(Actor a, Maze m){ return a.getType() == TileType.Hero && m.getInv().hasItem(YellowKey);}
            };
        }
    },



    //items
    BlueKey{
        public Tile getTileObject(TileInfo info){
            return new Key(){
                public TileType getType(){ return BlueKey;}
            };
        }
    },
    GreenKey{
        public Tile getTileObject(TileInfo info){
            return new Key(){
                public TileType getType(){ return GreenKey;}
            };
        }
    },
    OrangeKey{
        public Tile getTileObject(TileInfo info){
            return new Key(){
                public TileType getType(){ return OrangeKey;}
            };
        }
    },
    YellowKey{
        public Tile getTileObject(TileInfo info){
            return new Key(){
                public TileType getType(){ return YellowKey;}
            };
        }
    },

    Coin{
        public Tile getTileObject(TileInfo info){
            return new Tile(){
                public TileType getType(){ return Coin;}
                public boolean canMoveOn(Actor a, Maze m) { return true;}
            };
        }
    };

    /**
     * @return a NEW tile object associated with the enum
     * 
     */
    abstract Tile getTileObject(TileInfo info);
}


