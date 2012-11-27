package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.*;

/*
 * @author asauron
 * Pop 
 */
public class PopCode extends ByteCode {
    protected int popArg;

    @Override
    public void init(ArrayList<String> args) {
        popArg = Integer.parseInt(args.get(0));
    }

    @Override
    public String toString() {
        return Integer.toString(popArg);
    }
    
    
    @Override
    public void execute(VirtualMachine vm) {
        for(int n = 0; n < popArg; n++) {
             //pop the number n levels of stack
            vm.pop();
        }
    }

    
}
