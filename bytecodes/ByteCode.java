package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.*;

/*
 * @author asauron
 * Implemented as an abstract class so that all other bytecodes can inherit from it
 */
public abstract class ByteCode {
    private String ByteCodearg = new String();

    public abstract void init(ArrayList<String> args);
    public abstract void execute(VirtualMachine vm);

    public String getName() {
        String codeName = this.getClass().getName().replaceFirst("interpreter.bytecodes.", "").replaceAll("Code", "");
            return codeName.toUpperCase();
        // throw new UnsupportedOperationException("Not yet implemented");
    }
    public void setLabelAddress(int address) {}

    public String labelValue() {
        return ByteCodearg;
    }

    

    
  
}
