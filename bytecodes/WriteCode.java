package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.*;

/*
 * @author asauron
 * 
 */
public class WriteCode extends ByteCode {
    @Override
    public void init(ArrayList<String> args) {
    }

    /**
     *
     * @param vm
     */
    @Override
    public void execute(VirtualMachine vm) {
        System.out.println(vm.peek());
    }

    @Override
    public String toString() {
        return "WRITE";
    }
}
