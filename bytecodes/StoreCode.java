package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.*;

/* @author asauron
 * pop the top of the stack
 * 
 */

public class StoreCode extends ByteCode {
    private ArrayList<String> storeCodeArgs = new ArrayList<String>();
    int top;
    int value;

    @Override
    public void init(ArrayList<String> args) {
        for(String arg: args) {
            storeCodeArgs.add(arg);
        }
    }

   @Override
    public String toString() {
        return  storeCodeArgs.get(0) + " " + storeCodeArgs.get(1); 
    }
    
    
    @Override
    public void execute(VirtualMachine vm) {
        int offset = Integer.parseInt(storeCodeArgs.get(0));

        vm.store(offset);
        top = vm.peek();
        value = vm.runStackGet(offset);
        
        
    }
    
    public String getThisName(){
       String name = storeCodeArgs.get(1);
        return name;
    }
    
    /**
     *
     * @return
     */
    public int getThisValue() {
		return value;
	}

    
}
