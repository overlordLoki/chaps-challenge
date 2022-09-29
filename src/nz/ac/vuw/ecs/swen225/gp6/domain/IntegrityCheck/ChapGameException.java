package nz.ac.vuw.ecs.swen225.gp6.domain.IntegrityCheck;

public class ChapGameException extends RuntimeException{
    public ChapGameException(String m){
        super(m);
    }

}

class IllegalStateException extends ChapGameException {
    public IllegalStateException(String m){
        super(m);
    }
}

class IllegalActionException extends ChapGameException{
    public IllegalActionException(String m){
        super(m);
    }
}
