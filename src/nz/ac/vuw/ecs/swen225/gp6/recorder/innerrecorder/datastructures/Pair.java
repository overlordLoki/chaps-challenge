package nz.ac.vuw.ecs.swen225.gp6.recorder.innerrecorder.datastructures;

/**
 * Generic Pair class that has a key and value defined by the user.
 */
public class Pair<T,E> {
    private T key;
    private E value;

    /**
     * Construct a new Pair of time to actions
     * @param time the time of the actions
     * @param actions the recorded actions executed
     */
    public Pair(T key, E value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Get the time portion of this pair.
     * @return
     */
    public T getKey() {
        return key;
    }

    /**
     * Get the actions portion of this pair.
     * @return
     */
    public E getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return key + " : " + value;
    }
}
