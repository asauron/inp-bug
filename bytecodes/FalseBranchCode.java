package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.*;
/* 
 *@author asauron
 * 
 * Pops the top of the stack. 
 *
 */

public class FalseBranchCode extends ByteCode {

    private String falseBranchArg = new String();
    private int labelAddress;

    @Override
    public void init(ArrayList<String> args) {
        falseBranchArg = args.get(0);
    }

    @Override
    public String toString() {
        return falseBranchArg;
    }

    @Override
    public void execute(VirtualMachine vm) {
        int branch = vm.pop();
        /*
         * if it'sfalse (0) then branch to <label> 
         * else execute the next bytecode
         */
        if (branch == 0) {
            vm.setProgramCounter(labelAddress);
        }
    }

     /*
     * Setting the label address and giving the falsebranch "arg"
     * to the label for the hash table so that falsebranch "that arg" will call
     * label "that arg"
     */
    
    @Override
    public void setLabelAddress(int address) {
        labelAddress = address;
    }

    @Override
    public String labelValue() {
        return falseBranchArg;
    }
}
