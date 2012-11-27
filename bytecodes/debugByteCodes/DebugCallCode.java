/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.bytecodes.debugByteCodes;

import interpreter.VirtualMachine;
import interpreter.bytecodes.CallCode;
import interpreter.debugger.debugVM;
/**
 *
 * @author asauron
 */
public class DebugCallCode extends CallCode {

  /* @Override
    public void init(ArrayList<String> args) {
        throw new UnsupportedOperationException("Not supported yet.");
    }*/

    @Override
    public void execute(VirtualMachine vm) {
       // throw new UnsupportedOperationException("Not supported yet.");
        super.execute(vm);
       // debugVM dVM = (debugVM)vm;
        //dVM.addFunction();
        
    }

   
    
}
