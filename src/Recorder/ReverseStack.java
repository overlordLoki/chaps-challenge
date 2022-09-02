package Recorder;

import java.util.List;
import java.util.Stack;

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
