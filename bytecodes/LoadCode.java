package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.*;

/*
 * @author asauron
 * push the value in the slot
 * 
 */
public class LoadCode extends ByteCode {
    ArrayList<String> loadArgs = new ArrayList<String>();

    @Override
    public void init(ArrayList<String> args) {
        for(String bca: args) {
            loadArgs.add(bca);
        }
    }
    
    @Override
    public String toString() {
        return  loadArgs.get(0) + " " + loadArgs.get(1); 
    }

    @Override
    public void execute(VirtualMachine vm) {
        int offset = Integer.parseInt(loadArgs.get(0));
        //offset n from the start of the frame 
        //top of the stack
       // System.out.println("the value is "+ offset);
        vm.load(offset);
    }

    
}
