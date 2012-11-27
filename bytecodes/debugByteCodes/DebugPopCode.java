/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.bytecodes.debugByteCodes;

import interpreter.VirtualMachine;
import interpreter.bytecodes.PopCode;
import interpreter.debugger.debugVM;

/**
 *
 * @author asauron
 */
public class DebugPopCode extends PopCode{

   /* @Override
    public void init(ArrayList<String> args) {
        throw new UnsupportedOperationException("Not supported yet.");
    }*/

    @Override
    public void execute(VirtualMachine vm) {
        super.execute(vm);
        debugVM dvm = (debugVM) vm;
        dvm.popFERLiterals(popArg);
    }
    
}
