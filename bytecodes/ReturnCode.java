package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.*;

/* 
 * @author asauron
 * return from the current function
 */
public class ReturnCode extends ByteCode {

    private String returnArg = new String();

    @Override
    public void init(ArrayList<String> args) {
        if (!args.isEmpty()) {
            returnArg = args.get(0);
        }
    }

    @Override
    public String toString() {
        return returnArg.toString();
    }

    @Override
    public void execute(VirtualMachine vm) {
        int returnAddress = vm.getReturnAddress();
        //System.out.println("in the return block ");
        vm.popFrame();
        //  System.out.println("popped frame");
        vm.setProgramCounter(returnAddress);

    }
}
