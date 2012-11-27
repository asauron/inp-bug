package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.*;
/*
 *  @author asauron
 * Get the literal value
 */


public class LitCode extends ByteCode {
    protected ArrayList<String> litArgs = new ArrayList<String>();
    protected String id;
    int i;

    @Override
    public void init(ArrayList<String> args) {
        for(String arg: args) {
            litArgs.add(arg);
        }
    }

   @Override
    public String toString() {
         return litArgs.get(0) + "   "+ litArgs.get(1);
}
    
    @Override
    public void execute(VirtualMachine vm) {
        if(litArgs.size() == 2) 
            id = litArgs.get(1);
        else 
            id = "";
        i = Integer.parseInt(litArgs.get(0));
        vm.push(i);
    }

    
}
