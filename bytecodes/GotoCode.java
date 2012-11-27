package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.*;

/* 
 * @author asauron
 * GOTO <label>
 */

public class GotoCode extends ByteCode {
    private String gotoCodeArg = new String();
    private int labelAddress;

    @Override
    public void init(ArrayList<String> args) {
        gotoCodeArg = args.get(0);
    }

    @Override
    public String toString() {
      return  gotoCodeArg;
    }
    
    
    /**
     *
     * @param vm
     */
    @Override
    public void execute(VirtualMachine vm) {
        vm.setProgramCounter(labelAddress);
    }

    @Override
    public void setLabelAddress(int address) {
        labelAddress = address;
    }

    @Override
    public String labelValue() {
        return gotoCodeArg;
    }

    
}
