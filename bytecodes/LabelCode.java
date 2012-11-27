package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.*;
/*
 * @author asauron
 * This is the target for branches
 */

public class LabelCode extends ByteCode {
    private String labelArg = new String();
    int labelAddress;

    public void init(ArrayList<String> args) {
        labelArg = args.get(0);
    }

    @Override
   public String toString() {
   //System.out.println("LABEL");
        return  labelArg;
    }
    
    @Override
    public void execute(VirtualMachine vm) {
      //System.out.println("in label");
    }

    @Override
    public void setLabelAddress(int address) {
        labelAddress = address;
    }

    @Override
    public String labelValue() {
        return labelArg;
    }

    
}
