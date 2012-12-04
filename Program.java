package interpreter;

import interpreter.bytecodes.*;
import java.util.ArrayList;
import java.util.HashMap;


/*
 *  @author asauron
 */
public class Program {
    // logic here being two-fold
    // one - map label adress to a hashmap of a string and an integer(the counter (line number)of where that label is)
    // use that to resolve addresses
    // save just the string "Falsebranch", "Goto","call"

    private HashMap<String, Integer> labelAddress;
    //private ArrayList<Integer> legalBreakPoints;
    private ArrayList<ByteCode> codeLines;
    int counter = 0;
    String path;

    public Program() {
        labelAddress = new HashMap<String, Integer>();
        codeLines = new ArrayList<ByteCode>();
    }

    // Call this method from byteCodeLoader.java 
    //getting the bytecode and adding it to the list
    public void addLabelBytecode(ByteCode bytecode) {
        codeLines.add(bytecode);

        // check for "Label" with getSimpleName to strip down the package
        if (bytecode.getClass().getSimpleName().equals("LabelCode")) {
            labelAddress.put(bytecode.labelValue(), counter);
        }

      

        //To get the line numbers
        counter++;
    }

    public void resolveAddresses() {

        //Reading through the bytecode array list 
        //and adding labels that need to be resolved
        for (ByteCode b : codeLines) {
            if (b.getClass().getSimpleName().equals("FalseBranchCode")
                    || b.getClass().getSimpleName().equals("GotoCode")
                    || b.getClass().getSimpleName().equals("CallCode")
                    ||b.getClass().getSimpleName().equals("DebugCallCode")) {
                //||b.getClass().getSimpleName().equals("DebugCallCode")) {
                // Setting the label address to the argument supported by it 
                b.setLabelAddress(labelAddress.get(b.labelValue()));
            }
        }
    }

    public void setPath(String p) {
        path = p;
    }

    public String getPath() {
        return path;
    }

    /*  public boolean isLegalBreakPoint(int offset) {
     return legalBreakPoints.contains(offset);
     }*/
    public int getSize() {
        return codeLines.size();
    }

    public ByteCode getBytecode(int pc) {
        return codeLines.get(pc);
    }
}
