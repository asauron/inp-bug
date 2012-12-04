/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.bytecodes.debugByteCodes;

import interpreter.VirtualMachine;
import interpreter.bytecodes.CallCode;
import interpreter.debugger.debugVM;
import java.util.ArrayList;
/**
 *
 * @author asauron
 */
public class DebugCallCode extends CallCode {

  /* @Override
    public void init(ArrayList<String> args) {
        throw new UnsupportedOperationException("Not supported yet.");
    }*/

    public void init(ArrayList<String> args) {
        callArg = args.get(0);

    }
    
    @Override
    public void execute(VirtualMachine vm) {
       // throw new UnsupportedOperationException("Not supported yet.");
          
       debugVM dVM = (debugVM)vm;
     //  System.out.println("pushing function");
        dVM.addFunction();
         super.execute(vm);
    
    }
      @Override
    public String toString() {
      return callArg;

    }
    
       @Override
    public void setLabelAddress(int address) {
        labelAddress = address;
    }

    @Override
    public String labelValue() {
        return callArg;
    }

   
    
}
