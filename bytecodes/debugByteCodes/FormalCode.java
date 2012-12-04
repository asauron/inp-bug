/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.bytecodes.debugByteCodes;

import interpreter.VirtualMachine;
import interpreter.bytecodes.ByteCode;
import interpreter.debugger.debugVM;
import java.util.ArrayList;

/**
 *
 * @author asauron
 */
public class FormalCode extends ByteCode  {
    String formalCode = new String();
    int offset;

    @Override
    public void init(ArrayList<String> args) {
       // throw new UnsupportedOperationException("Not supported yet.");
        formalCode = args.get(0);
        offset = Integer.parseInt(args.get(1));
        
    }

    public void execute(VirtualMachine vm) {
        debugVM dvm = (debugVM) vm;
        dvm.pushRecord(formalCode, offset);
    }
    
    @Override
    public String toString(){
        return formalCode + " " + offset;
    
    }
    
}
