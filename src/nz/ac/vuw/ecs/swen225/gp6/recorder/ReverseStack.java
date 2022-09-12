package nz.ac.vuw.ecs.swen225.gp6.recorder;

import java.util.List;
import java.util.Stack;


/**
 * Old code, not currently used.
 * Keeping it for potential future use.
 */
public class ReverseStack {
    Stack<List<String>> stack;

    public ReverseStack(Stack<List<String>> stack) {
        this.stack = stack;
    }

    public Stack<List<String>> execute(){
        Stack<List<String>> reversed = new Stack<>();
        while(!stack.isEmpty()){
            reversed.push(stack.pop());
        }
        return reversed;
    }
}
