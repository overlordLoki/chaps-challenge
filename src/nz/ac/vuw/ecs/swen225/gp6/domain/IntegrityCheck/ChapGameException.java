package nz.ac.vuw.ecs.swen225.gp6.domain.IntegrityCheck;
/**
 * A ChapGameException will be thrown when the game violates the desired constraints , 
 * This is a exception class that all custom made exceptions for this game should extend.
 * 
 * Note: Some already existing exception classes such asIllegalArgumentException are also used 
 * to enforce desired game constraints which does not extend ChapGameException.
 */
public class ChapGameException extends RuntimeException{
    /**
     * chap game exceptions require an error message.
     * 
     * @param m the error message
     */
    public ChapGameException(String m){
        super(m);
    }

}

/**
 * A IllegalStateException will be thrown when the game state is inconsistent 
 */
class IllegalStateException extends RuntimeException {
    /**
     * Illegal state exceptions require an error message.
     * 
     * @param m the error message
     */
    public IllegalStateException(String m){
        super(m);
    }
}
