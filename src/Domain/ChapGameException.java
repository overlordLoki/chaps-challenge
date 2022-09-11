package Domain;

public class ChapGameException extends RuntimeException{
    public ChapGameException(String m){
        super(m);
    }

}

class IllegalStateException extends RuntimeException {
    public IllegalStateException(String m){
        super(m);
    }
}

class IllegalActionException extends ChapGameException{
    public IllegalActionException(String m){
        super(m);
    }
}
