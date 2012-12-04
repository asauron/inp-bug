/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.bytecodes.debugByteCodes;

import interpreter.VirtualMachine;
import interpreter.bytecodes.ReturnCode;
import interpreter.debugger.debugVM;

/**
 *
 * @author asauron
 */
public class DebugReturnCode extends ReturnCode {

   /* @Override
    public void init(ArrayList<String> args) {
        throw new UnsupportedOperationException("Not supported yet.");
    }*/


    @Override
     public void execute(VirtualMachine vm) {
      
        debugVM dvm = (debugVM) vm;
        dvm.popFER();
        //System.out.println("popped in return");
          super.execute(vm);
    }
    
}
