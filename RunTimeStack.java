package interpreter;

import java.util.*;

/*
 *  @author asauron
 * 
 * Records and processes the stack of active frames
 * The RunTimeStack class maintains the stack ofactive frames; 
 * when we call a function we'll push a new frame on the stack; 
 * when we retum from a function we'll pop the top frame
 */
public class RunTimeStack {
    //Using deque to implement the stacks functionality,
    //pushing and popping objects made easy 

    private ArrayDeque<Integer> framePointers;
    //Using an array list instead of a vector
    private ArrayList<Integer> runTimeStack;
    private int nArgs;

    public RunTimeStack() {
        
        framePointers = new ArrayDeque<Integer>();
        runTimeStack = new ArrayList<Integer>();
        framePointers.add(0);
    }

    //The runStack dump,printed out when dump is on
    public void dump() {
        System.out.print("[");
        for (int i = 0; i < runTimeStack.size(); i++) {
            if (i != 0 && framePointers.contains(i)) {
                System.out.print("] [");
            }
        if (!framePointers.contains(i)) {
                System.out.print(",");
            }
           System.out.print(runTimeStack.get(i));
        }
        System.out.println("]");
    }

    //Implemented so as to not break encapsulation
    public int size() {
        return runTimeStack.size();
    }
    

    /*
     * Used in the ArgsCode method
     */
    public void setNArgs(int numOfArgs) {
        this.nArgs = numOfArgs;
    }

    public int getNumArgs() {
        return nArgs;
    }
    
    //the top item on the runtime stack
    public int peek() {
        return runTimeStack.get(size() - 1);
    }

    //pop the top item from the runtime stack
    public int pop() {
        int temp = runTimeStack.get(size() - 1);
        runTimeStack.remove(size() - 1);
        return temp;
    }

    //i-push this item on the runtime stack
    public int push(int i) {
        runTimeStack.add(i);
        return i;
    }
    
    //save the value, pop the
    //top frame and 
    //then push the return value
    public void popFrame() {
        int temp = peek();
        int pop = framePointers.pop();
        while (size() - 1 >= pop) {
            if (!runTimeStack.isEmpty()) { 
          //Error checking so as to avaoid Empty Stack Exception
                pop();
            }
        }

        push(temp);
    }

    //start new frame
    public void newFrameAt(int offset) {
        framePointers.push(offset);
    }

    

    //Used to store into variables
    public int store(int offset) {
        int temp = pop();
        runTimeStack.set(offset, temp);
         return offset;
    }

    //Used to load variables onto the stack
    public int load(int offset) {
        int temp = runTimeStack.get(framePointers.peek() + offset);
        runTimeStack.add(temp);
        return offset;
        //  return runStack.get(runStack.size() - 1);
    }
    
   //Returning object i
    public Integer push(Integer i) {
        runTimeStack.add(i);
         return i;
    }

    int peekFrame() {
        return framePointers.peek();
        //throw new UnsupportedOperationException("Not yet implemented");
    }
   public int get(int i)
   {
       return runTimeStack.get(i);
   }
   
   public int frameSize() {
        return framePointers.size();
    }
    
}
