package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.*;
/*
 * @author asauron
 * Dumps the value at that moment in the runtime stack
 */

public class DumpCode extends ByteCode {
    private String dumpArg; 


    @Override
    public void init(ArrayList<String> args) {
        dumpArg = args.get(0);
    }

    @Override
    public void execute(VirtualMachine vm) {
        if(dumpArg.equals("ON")) {
            vm.dumpRunStack(true);
        }
        
        else {
            vm.dumpRunStack(false);
        }
        
    }
    
   
}
