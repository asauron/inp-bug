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
public class LineCode extends ByteCode {
    int lineNumber;

    @Override
    public void init(ArrayList<String> args) {
        //throw new UnsupportedOperationException("Not supported yet.");
         lineNumber = Integer.parseInt(args.get(0));
        
    }

    public void execute(VirtualMachine vm) {
        debugVM dvm = (debugVM) vm;
        dvm.setStartedLine(lineNumber);
    }
    
    @Override
     public String toString() {
        return "" + lineNumber;
    }
    
}
