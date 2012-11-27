package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.*;

/*
 * @author asauron
 * Transfer control to the
 * indicated function
 */
public class CallCode extends ByteCode {

    private int labelAddress;
    private String callArg;
    int numArgs;
    String arg;

    public void init(ArrayList<String> args) {
        callArg = args.get(0);

    }

    @Override
    public String toString() {
      return callArg;

    }

    /*
     * Setting the return address and the 
     * program counter
     */
    @Override
    public void execute(VirtualMachine vm) {
        vm.setReturnAddress();
        vm.setProgramCounter(labelAddress - 1);
        numArgs = vm.getNumArgs();
    }

    
    /*
     * Setting the label address and giving the call "arg"
     * to the label for the hash table so that call "that arg" will call
     * label "that arg"
     */
    @Override
    public void setLabelAddress(int address) {
        labelAddress = address;
    }

    @Override
    public String labelValue() {
        return callArg;
    }
}
