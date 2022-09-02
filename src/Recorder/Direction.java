package Recorder;



/*
 * Enum used to step in a direction
 * Not currently implemented, for potential future use.
 */
public enum Direction{
    FORWARD(1), BACKWARD(-1);

    private int value;

    Direction(int i){
        this.value = i;
    };
    
    public int value(){
        return this.value;
    }
}
