package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.*;
import javax.swing.JOptionPane;

/*
 * @author asauron
 * read an integer
 */
public class ReadCode extends ByteCode {
    @Override
    public void init(ArrayList<String> args) {
    }

    @Override
    public String toString() {
        return "READ";
    }
    
    @Override
    public void execute(VirtualMachine vm) { //number input as illustrated in the SviewApp
        String input = JOptionPane.showInputDialog(null, "Enter a number:");
        //Debugging output
        //System.out.println(input);
        vm.push(Integer.parseInt(input));
    }

    
}
