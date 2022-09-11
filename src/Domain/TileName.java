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
                public boolean canMoveOn(Actor a, Maze m) { return true;} //anyone can move on empty terrain
            };
        }
    },
    Floor{
        public Tile getTileObject(){
            return new Tile(){
                public TileName getName(){ return Floor;}
                public boolean canMoveOn(Actor a, Maze m) { return true;} //anyone can move on floor
            };
        }
    },
    Wall{
        public Tile getTileObject(){
            return new Tile(){
                public TileName getName(){ return Wall;}
                public boolean canMoveOn(Actor a, Maze m) { return false;} //no one can move on wall
            };
        }
    },

    //interactive terrains
    ExitDoor{
        public Tile getTileObject(){
            return new Tile(){
                public TileName getName(){ return ExitDoor;}
                public boolean canMoveOn(Actor a, Maze m) { return false;}
            };
        }
    },
    ExitDoorOpen{
        public Tile getTileObject(){
            return new Tile(){
                public TileName getName(){ return ExitDoorOpen;}
                public boolean canMoveOn(Actor a, Maze m) { return true;}
            };
        }
    },

    BlueLock{
        public Tile getTileObject(){
            return new Lock(){
                public TileName getName(){ return BlueKey;}
                public boolean canMoveOn(Actor a, Maze m){ return m.getInv().hasItem(BlueKey);}
            };
        }
    },
    GreenLock{
        public Tile getTileObject(){
            return new Lock(){
                public TileName getName(){ return GreenLock;}
                public boolean canMoveOn(Actor a, Maze m){ return m.getInv().hasItem(GreenKey);}
            };
        }
    },
    OrangeLock{
        public Tile getTileObject(){
            return new Lock(){
                public TileName getName(){ return OrangeKey;}
                public boolean canMoveOn(Actor a, Maze m){ return m.getInv().hasItem(OrangeKey);}
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
