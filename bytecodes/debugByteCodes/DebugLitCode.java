/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.bytecodes.debugByteCodes;

import interpreter.VirtualMachine;
import interpreter.bytecodes.LitCode;
import interpreter.debugger.debugVM;

/**
 *
 * @author asauron
 */
public class DebugLitCode extends LitCode {

   

    @Override
    public void execute(VirtualMachine vm) {
        super.execute(vm);
        debugVM dvm = (debugVM) vm;

        if (!id.isEmpty()) {
            int offset = vm.runStackSize() - 1;
            dvm.enterRecord(id, offset);
    }
    }
    
  
    @Override
   public String toString() {
	if (litArgs.size() > 1) {
           return litArgs.get(0) + "   "+ litArgs.get(1);
       }
	return litArgs.get(0);
}
    
    
}
