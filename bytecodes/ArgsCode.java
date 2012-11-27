package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.*;
/*
 * @author asauron
 */
//Used prior to calling a function
public class ArgsCode extends ByteCode {

    private int n;

    @Override
    public void init(ArrayList<String> args) {
        n = Integer.parseInt(args.get(0));
    }

    @Override
    public String toString() {
        return Integer.toString(n);
    }
    //Execute method makes sure that CALL execution has n args

    @Override
    public void execute(VirtualMachine vm) {
        //Setting the args to be called later on
        vm.setNArgs(n);
        //arg (n) down from the top
        int offset = vm.runStackSize() - n;
        //Set up a new frame 
        vm.newFrameAt(offset);


    }
}
