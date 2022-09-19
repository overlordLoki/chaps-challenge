package nz.ac.vuw.ecs.swen225.gp6.domain.TileGrouping;

import java.util.function.*;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.Maze;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Loc;

public enum TileState {
    Hero(
        ()->'H',
        (t, d) -> t.getState() == TileTypeTwo.Enemy,
        (t, d) ->{},//TODO: LOSE 
        d -> {
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
    ),

    Enemy(
        ()->'E',
        (t, d) -> t.getType() == TileTypeTwo.Hero,
        (t, d) ->{},//TODO: LOSE 
        d -> info.consumer().accept(d)
    ),

    Empty(
        ()->' ',
        (t, d) -> true,
        (t, d) ->d.getCurrentMaze().setTileAt(info.loc(), a),
        d ->{}
    ),
    Floor(
        ()->'_',
        (t, d)->true,
        (t, d) ->d.getCurrentMaze().setTileAt(info.loc(), a),
        d ->{}
    ), 

    Wall(
        ()->'|',
        (t, d) -> false,
        (t, d) ->{},
        d ->{}
    ),
    ExitDoor(
        ()->'X',
        (t, d) -> false,
        (t, d) -> {},
        d ->{
            //if all treasures collected replace exitdoor with open exit door
            if(d.getCurrentMaze().getTileCount(TileType.Coin) - d.getInv().coins() == 0){
                d.getCurrentMaze().setTileAt(info.loc(), TileType.ExitDoorOpen, a->{}); 
            }
        }
    ),

    ExitDoorOpen(
        ()->'Z',
        (t, d) -> t.getState() == TileType.Hero,
        (t, d) -> {},//TODO: WIN
        d -> {}
    ),

    BlueLock(
        ()->'B',
        (t, d) ->a.getType() == TileTypeTwo.Hero && d.getInv().hasItem(TileTypeTwo.BlueKey),
        (t, d) ->{
            d.getInv().removeItem(TileTypeTwo.BlueKey);
            d.getCurrentMaze().setTileAt(info.loc(), a);
        }, 
        d -> {}
    ),
    GreenLock(
        ()->'G',
        (t, d) ->a.getType() == TileTypeTwo.Hero && d.getInv().hasItem(TileTypeTwo.GreenKey),
        (t, d) ->{
            d.getInv().removeItem(TileTypeTwo.GreenKey);
            d.getCurrentMaze().setTileAt(info.loc(), a);
        }, 
        d -> {}
    ),
    OrangeLock(
        ()->'O',
        (t, d) ->a.getType() == TileTypeTwo.Hero && d.getInv().hasItem(TileTypeTwo.OrangeLock),
        (t, d) ->{
            d.getInv().removeItem(TileTypeTwo.OrangeLock);
            d.getCurrentMaze().setTileAt(info.loc(), a);
        },
        d ->{}
    ),
    YellowLock(
        ()->'Y',
        (t, d) ->a.getType() == TileTypeTwo.Hero && d.getInv().hasItem(TileTypeTwo.YellowLock),
        (t, d) ->{
            d.getInv().removeItem(TileTypeTwo.YellowLock);
            d.getCurrentMaze().setTileAt(info.loc(), a);
        },
        d ->{}
    ),

    BlueKey(
        ()->'b',
        (t, d) ->a.getType() == TileTypeTwo.Hero,
        (t, d) ->{
            d.getInv().addItem(this);
            d.getCurrentMaze().setTileAt(info.loc(), a);
        },
        d ->{}
    ),
    GreenKey(
        ()->'g',
        (t, d) ->a.getType() == TileTypeTwo.Hero,
        (t, d) ->{
            d.getInv().addItem(this);
            d.getCurrentMaze().setTileAt(info.loc(), a);
        },
        d ->{}
    ),
    OrangeKey(
        ()->'o',
        (t, d) ->a.getType() == TileTypeTwo.Hero,
        (t, d) ->{
            d.getInv().addItem(this);
            d.getCurrentMaze().setTileAt(info.loc(), a);
        },
        d ->{}
    ),
    YellowKey(
        ()->'y',
        (t, d) ->a.getType() == TileTypeTwo.Hero,
        (t, d) ->{
            d.getInv().addItem(this);
            d.getCurrentMaze().setTileAt(info.loc(), a);
        },
        d ->{}
    ),

    Coin(
        ()->'C',
        (t, d) ->a.getType() == TileTypeTwo.Hero,
        (t, d) ->{
            d.getInv().addCoin();
                    d.getCurrentMaze().setTileAt(info.loc(), a);
        },
        d ->{}
    ), 

    Null(
        ()->Character.MIN_VALUE,
        (t, d) -> false,
        (t, d) ->{},
        d ->{}
    );

    //informing methods
    Supplier<Character> getSymbol;
    BiPredicate<TileTwo, Domain> isObstruction;

    //action methods
    BiConsumer<TileTwo, Domain> setOn;
    Consumer<Domain> ping;

    private TileState(
        Supplier<Character> getSymbol,
        BiPredicate<TileTypeTwo, Domain> isObstruction,
        BiConsumer<TileTypeTwo, Domain> setOn,
        Consumer<Domain> ping
    ){
        this.getSymbol = getSymbol;
        this.isObstruction = isObstruction;
        this.setOn = setOn;
        this.ping = ping;
    }
}
