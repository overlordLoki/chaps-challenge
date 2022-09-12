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
                public TileType getName(){ return Hero;}
                public boolean canMoveOn(Actor a, Maze m) { return a.getName() == TileType.Enemy;} //enemy can move on hero
                public void ping(Maze m) {
                    // TODO: 
                };
            };
        }
    },
    Enemy{
        public Tile getTileObject(TileInfo info){
            return new Actor(){
                public TileType getName(){ return Enemy;}
                public boolean canMoveOn(Actor a, Maze m) { return a.getName() == TileType.Hero;} //hero can move on enemy
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
                public TileType getName(){ return Empty;}
                public boolean canMoveOn(Actor a, Maze m) { return true;} //anyone can move on empty terrain
            };
        }
    },
    Floor{
        public Tile getTileObject(TileInfo info){
            return new EmptyTile(){
                public TileType getName(){ return Floor;}
                public boolean canMoveOn(Actor a, Maze m) { return true;} //anyone can move on floor
            };
        }
    },
    Wall{
        public Tile getTileObject(TileInfo info){
            return new Tile(){
                public TileType getName(){ return Wall;}
                public boolean canMoveOn(Actor a, Maze m) { return false;} //no one can move on wall
            };
        }
    },

    //interactive terrains
    ExitDoor{
        public Tile getTileObject(TileInfo info){
            return new Tile(){
                public TileType getName(){ return ExitDoor;}
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
                public TileType getName(){ return ExitDoorOpen;}
                public boolean canMoveOn(Actor a, Maze m) { return a.getName() == TileType.Hero;}
            };
        }
    },

    BlueLock{
        public Tile getTileObject(TileInfo info){
            return new Lock(){
                public TileType getName(){ return BlueKey;}
                public boolean canMoveOn(Actor a, Maze m){ return a.getName() == TileType.Hero && m.getInv().hasItem(BlueKey);}
            };
        }
    },
    GreenLock{
        public Tile getTileObject(TileInfo info){
            return new Lock(){
                public TileType getName(){ return GreenLock;}
                public boolean canMoveOn(Actor a, Maze m){ return a.getName() == TileType.Hero && m.getInv().hasItem(GreenKey);}
            };
        }
    },
    OrangeLock{
        public Tile getTileObject(TileInfo info){
            return new Lock(){
                public TileType getName(){ return OrangeKey;}
                public boolean canMoveOn(Actor a, Maze m){ return a.getName() == TileType.Hero && m.getInv().hasItem(OrangeKey);}
            };
        }
    },
    YellowLock{
        public Tile getTileObject(TileInfo info){
            return new Lock(){
                public TileType getName(){ return YellowLock;}
                public boolean canMoveOn(Actor a, Maze m){ return a.getName() == TileType.Hero && m.getInv().hasItem(YellowKey);}
            };
        }
    },



    //items
    BlueKey{
        public Tile getTileObject(TileInfo info){
            return new Key(){
                public TileType getName(){ return BlueKey;}
            };
        }
    },
    GreenKey{
        public Tile getTileObject(TileInfo info){
            return new Key(){
                public TileType getName(){ return GreenKey;}
            };
        }
    },
    OrangeKey{
        public Tile getTileObject(TileInfo info){
            return new Key(){
                public TileType getName(){ return OrangeKey;}
            };
        }
    },
    YellowKey{
        public Tile getTileObject(TileInfo info){
            return new Key(){
                public TileType getName(){ return YellowKey;}
            };
        }
    },

    Coin{
        public Tile getTileObject(TileInfo info){
            return new Tile(){
                public TileType getName(){ return Coin;}
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


