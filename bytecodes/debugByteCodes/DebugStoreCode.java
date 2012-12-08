/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.bytecodes.debugByteCodes;

/**
 *
 * @author asauron
 */
import interpreter.debugger.*;
import interpreter.bytecodes.*;
import interpreter.*;
import java.util.*;

public class DebugStoreCode extends StoreCode {
	@Override
	public void execute(VirtualMachine vm) {
		super.execute(vm);
                
                  debugVM dvm = (debugVM) vm;

		dvm.enterRecord(this.getThisName(), this.getThisValue());
	}

	@Override
	public String getName() {
		return "STORE";
	}
}