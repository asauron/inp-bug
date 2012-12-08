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
public class FunctionCode extends ByteCode {
    String funcName;
    int start;
    int end;

    @Override
    public void init(ArrayList<String> args) {
        funcName = args.get(0);
        start= Integer.parseInt(args.get(1));
        end= Integer.parseInt(args.get(2));
    }

    

    @Override
    public void execute(VirtualMachine vm) {
        debugVM dvm = (debugVM) vm;
       // System.out.println("function bytecode");
        dvm.setLines(start, end);
        dvm.setFuncName(funcName);
    }
    
    @Override
    public String toString(){
        return funcName + " " + start + " " + end;
    }
}
