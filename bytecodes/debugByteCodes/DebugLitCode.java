/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.bytecodes.debugByteCodes;

import interpreter.VirtualMachine;
import interpreter.bytecodes.LitCode;
import interpreter.debugger.debugVM;
import java.util.ArrayList;

/**
 *
 * @author asauron
 */
public class DebugLitCode extends LitCode {

    private String value;
    private String id;

    public void init(ArrayList<String> args) {
        super.init(args);

        value = args.get(0);

        if (args.size() > 1) {
            id = args.get(1);
        }
    }

    @Override
    public void execute(VirtualMachine vm) {
        super.execute(vm);
        debugVM dvm = (debugVM) vm;
      // System.out.println("this "+id+" is going into my table"+ value);
        if (id != null && id.length() > 0) {
            (dvm).enterRecord(id, Integer.parseInt(value));
          //  dvm.enterRecord(id,(Integer)(vm.runStackSize()-1-(vm.peekFrame())));
        } /*else {
            (dvm).enterRecord("", Integer.parseInt(offset));
        }*/

    }

    @Override
    public String toString() {
        if (litArgs.size() > 1) {
            return litArgs.get(0) + "   " + litArgs.get(1);
        }
        return litArgs.get(0);
    }
}
