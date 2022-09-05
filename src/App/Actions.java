package App;

import App.tempDomain.Game;

public class Actions {

    private static  Game game;

    public Actions(Game game){
        Actions.game = game;
    }

    public static void actionStart(){
        game.startGame();
    }

    public static void actionEnd(){
        game.endGame();
    }

    public static void actionPause(){
        game.pauseGame();
    }

    public static void actionResume(){
        game.resumeGame();
    }

    public static void actionRestart(){
        game.restartGame();
    }

    public static void actionSave(){
        game.saveGame();
    }

    public static void actionLoad(){
        game.loadGame();
    }

    public static void actionQuit(){
        game.quitGame();
    }

    public static void actionUp(){
        game.moveUp();
    }

    public static void actionDown(){
        game.moveDown();
    }

    public static void actionLeft(){
        game.moveLeft();
    }

    public static void actionRight(){
        game.moveRight();
    }

}

