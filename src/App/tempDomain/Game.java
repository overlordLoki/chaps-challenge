package App.tempDomain;

public class Game {

    public void startGame(){
        System.out.println("Game started");
    }

    public void endGame(){
        System.out.println("Game ended");
    }

    public void pauseGame(){
        System.out.println("Game paused");
    }

    public void resumeGame(){
        System.out.println("Game resumed");
    }

    public void restartGame(){
        System.out.println("Game restarted");
    }

    public void saveGame(){
        System.out.println("Game saved");
    }

    public void loadGame(){
        System.out.println("Game loaded");
    }

    public void quitGame(){
        System.out.println("Game quit");
    }

    public void moveUp(){
        System.out.println("Player moved up");
    }

    public void moveDown(){
        System.out.println("Player moved down");
    }

    public void moveLeft(){
        System.out.println("Player moved left");
    }

    public void moveRight(){
        System.out.println("Player moved right");
    }

    public int getCurrentLevel() {
        return 1;
    }

    public int getTreasuresLeft() {
        return 12;
    }
}
