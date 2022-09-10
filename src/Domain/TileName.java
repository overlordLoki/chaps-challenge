package Domain;

/**
 * Enum for tile names but also more importantly where the specific tile types are implemented with 
 * dynamic dispatching.
 */
public enum TileName{
    //actors
    Hero{
        public Tile getTileObject(){
            return new Actor(){
                public TileName getName(){ return Hero;}
                public boolean canMoveOn(Actor a, Maze m) { return a.getName() == TileName.Enemy;} //enemy can move on hero
            };
        }
    },
    Enemy{
        public Tile getTileObject(){
            return new Actor(){
                public TileName getName(){ return Enemy;}
                public boolean canMoveOn(Actor a, Maze m) { return a.getName() == TileName.Hero;} //hero can move on enemy
            };
        }
    },

    //terrains
    Empty{
        public Tile getTileObject(){
            return new Tile(){
                public TileName getName(){ return Empty;}
                public boolean canMoveOn(Actor a, Maze m) { return true;}
            };
        }
    },
    Floor{
        public Tile getTileObject(){
            return new Tile(){
                public TileName getName(){ return Floor;}
                public boolean canMoveOn(Actor a, Maze m) { return true;}
            };
        }
    },
    Wall{
        public Tile getTileObject(){
            return new Tile(){
                public TileName getName(){ return Wall;}
                public boolean canMoveOn(Actor a, Maze m) { return false;}
            };
        }
    },

    //interactive terrains
    ExitDoor{
        public Tile getTileObject(){
            return new Tile(){
                public TileName getName(){ return ExitDoor;}
                public boolean canMoveOn(Actor a, Maze m) { }
            };
        }
    },

    BlueLock{
        public Tile getTileObject(){
            return new Lock(){
                public TileName getName(){ return BlueKey;}
            };
        }
    },
    GreenLock{
        public Tile getTileObject(){
            return new Lock(){
                public TileName getName(){ return GreenLock;}
            };
        }
    },
    OrangeLock{
        public Tile getTileObject(){
            return new Lock(){
                public TileName getName(){ return OrangeKey;}
            };
        }
    },


    //items
    BlueKey{
        public Tile getTileObject(){
            return new Key(){
                public TileName getName(){ return BlueKey;}
            };
        }
    },
    GreenKey{
        public Tile getTileObject(){
            return new Key(){
                public TileName getName(){ return GreenKey;}
            };
        }
    },
    OrangeKey{
        public Tile getTileObject(){
            return new Key(){
                public TileName getName(){ return OrangeKey;}
            };
        }
    },

    Coin{
        public Tile getTileObject(){
            return new Tile(){
                public TileName getName(){ return Coin;}
                public boolean canMoveOn(Actor a, Maze m) { return true;}
            };
        }
    };

    abstract Tile getTileObject();
}
