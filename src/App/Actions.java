package App;

import App.tempDomain.Game;

public class Actions {

    private Game game;

    public Actions(Game game){
        this.game = game;
    }

    public void actionPause(){
        game.pauseGame();
    }

    public void actionResume(){
        game.resumeGame();
    }

    public void actionSave(){
        game.saveGame();
    }

    public void actionLoad(){
        game.loadGame();
    }

    public void actionQuit(){
        game.quitGame();
    }

    public void actionUp(){
        game.moveUp();
    }

    public void actionDown(){
        game.moveDown();
    }

    public void actionLeft(){
        game.moveLeft();
    }

    public void actionRight(){
        game.moveRight();
    }

}

