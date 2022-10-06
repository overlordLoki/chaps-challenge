package nz.ac.vuw.ecs.swen225.gp6.app.utilities;

/**
 * Generic Pair record class that has a key and value defined by the user.
 * @param <T> the type of the key
 * @param <E> the type of the value
 * 
 * @author Jeff Lin
 */
record Pair<T,E>(T key, E value) {
    @Override
    public String toString() {
        return key + " : " + value;
    }
}
