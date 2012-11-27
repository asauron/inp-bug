package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.*;
/*
 * @author asauron
 * Halt the virtual machine
 */

public class HaltCode extends ByteCode {
    public void init(ArrayList<String> args) {
    }

    @Override
    public String toString() {
        return "HALT";
        
    }
    //Set is running to false
    //from the reader
    public void execute(VirtualMachine vm) {
        vm.isRunning(false);
    }

    
}
