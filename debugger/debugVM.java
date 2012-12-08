
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.debugger;

import interpreter.Program;
import interpreter.RunTimeStack;
import interpreter.VirtualMachine;
import interpreter.bytecodes.ByteCode;
import interpreter.bytecodes.ReadCode;
import interpreter.bytecodes.WriteCode;
import interpreter.bytecodes.debugByteCodes.*;
import interpreter.debugger.UI.UI;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;

/*
 * 
 * @author asauron
 *The debugVM inplements encapsulation so that the function environment Methods are only 
 * called from here
 */
public class debugVM extends VirtualMachine {

    private ArrayList<String> sourceCode;
    private Stack<FunctionEnvironmentRecord> envStack;
    private ByteCode currentByteCode;
    private boolean waitingForBreakpoint;
    private HashMap<Integer, Integer> breakpoints;
    private UI ui;
    private boolean bp;
    private HashMap<Integer, Set<String>> onWatchList;
    private boolean stepout;
    private boolean stepover;
    public boolean stepin;
    public boolean step;
    //for watch variables 
    private String onWatch;
    private ArrayList<String> newSourceCode;
    int tempBp = -2;
    boolean change = false;
    boolean steppingover = false;
    boolean readvisited = false;
    boolean writevisited = false;
    public int n = 0;
    public int lineNumber = -1;
    public int tempN = 0;

    public debugVM(Program program, ArrayList<String> sourceCode) {
        super(program);
        waitingForBreakpoint = true;
        this.isRunning = true;
        this.pc = 0;
        this.currentByteCode = null;
        this.runTimeStack = new RunTimeStack();
        this.returnAddress = new Stack<Integer>();
        this.sourceCode = sourceCode;

        this.envStack = new Stack<FunctionEnvironmentRecord>();
        breakpoints = new HashMap<Integer, Integer>();
        this.breakpoints.put(new Integer(0), new Integer(0));
        this.newSourceCode = new ArrayList<String>();
        //so that inserting and replacing can be done 
        newSourceCode = (ArrayList<String>) sourceCode.clone(); 
        newSourceCode.remove(0);
        this.change = false;
        onWatchList = new HashMap<Integer, Set<String>>();



    }

    public void intiliaze() {
        addFunction();
        setStartedLine(0);
        super.isRunning(true);
    }

    @Override
    public void executeProgram() {
        int envsize = envStack.size();

        onWatch = null;

        while (!bp) {
            currentByteCode = program.getBytecode(pc);

            if (stepin && ((currentByteCode instanceof ReadCode) || (currentByteCode instanceof WriteCode))) {

                bp = true;


                return;
            }



            if (stepover) {
                stepOver();
            }

            currentByteCode.execute(this);


            pc++;

            if (stepin && currentByteCode instanceof FormalCode) {
                bp = true;
                return;
            }

            if (stepout) {
                stepOut();
            }



        }



    }

    public void endIT() {
        super.isRunning(false);



    }

    @Override
    public void isRunning(boolean value) {
        if (value == false) {
            endIT();
        }
        this.isRunning = value;

    }

    //==============//
    /*
     * The methods that the debugger bytecodes call. 
     * Encapsulation has been maintained here
     
     */

    /* bytecode methods */
      //the debug return bytecode 
    public void popFER() {
        envStack.pop();
        n--;
        if (stepout) {
            bp = true;
        }
    }

    // adds a single record entry at the given offset
    //Lit code 
    public void enterRecord(String id, int offsetvalue) {
        //   envStack.peek().enterSymbol(id, offset);
        //  envStack.peek().enterSymbol(id, runTimeStack.get((runTimeStack.size() - offset) - 1));

        boolean isThere = envStack.peek().containsString(id);

        envStack.peek().enterSymbol(id, offsetvalue);
      //if there is no varaible to watch, then dont do anything 
        if (!isThere) {
            return;
        }

        //If the variable is on the watch list, display the changes 

        int startLine = envStack.peek().getStartLine();
        int endLine = envStack.peek().getEndline();

        for (Integer i : onWatchList.keySet()) {
            int value = i.intValue();
            //Integer.parseInt(i
            if (value >= startLine && value <= endLine) {
                Set<String> set = onWatchList.get(i);

             for (String k : set) {
               if (id.equals(k)) {
              onWatch = "Watched variable " + id + " changed to " + offsetvalue;
             System.out.println(onWatch);

                    }
             }
            }
        }
    }

    // debug pop
    public void popFERLiterals(int n) {
        envStack.peek().popSymbol(n);
    }
    
    //call bytecode

    public void addFunction() {
        envStack.push(new FunctionEnvironmentRecord());
        n++;  //this is later used by the step out method

    }

    //Function Bytecodes
    public void setFuncName(String funcName) {
    envStack.peek().setFunctionName(funcName);
      }

    public void setLines(int start, int end) {
        envStack.peek().setStartLine(start);
        envStack.peek().setEndLine(end);
      
    }

    //Formal Byte Code
    public void pushRecord(String formalCode, int offset) {
       envStack.peek().enterSymbol(formalCode, runTimeStack.get((runTimeStack.size() - offset) - 1));
        checkBP();
    }

    ////Line Byte code
    public void setStartedLine(int lineNumber) {
        //  stepover = false;
        envStack.peek().setCurrentLine(lineNumber);
        checkBP();
    }
//--------------------------------------------------------------------------------------//

    //a boolean for the breakpoint, for the continue method                                      
    public void setWaitingForBreakpoint(boolean waiting) {
        waitingForBreakpoint = waiting;
    }

    public debugVM(ArrayList<String> sourceCode, Stack<FunctionEnvironmentRecord> envStack, ByteCode currentByteCode, boolean waitingForBreakpoint, HashMap<Integer, Integer> breakpoints, UI ui, boolean bp, Program program) {
        super(program);
        this.sourceCode = sourceCode;
        this.envStack = envStack;
        this.currentByteCode = currentByteCode;
        this.waitingForBreakpoint = waitingForBreakpoint;
        this.breakpoints = breakpoints;
        this.ui = ui;
        this.bp = bp;
    }

    public debugVM(Program program) {
        super(program);
    }
    
    //removing the break point

    public String removeBP(Integer breakPoint) {
        if (breakpoints.containsValue(breakPoint)) {
            breakpoints.remove(breakPoint);
            return ("Break Points were  cleared at : " + breakPoint);

        } else {
            return ("No bp set at that line :" + breakPoint);
        }

    }

    //getting the line number of that breakpoint, and checking whether it is true 
    public boolean setBP(Integer breakPoint) {
      String sourceLine = sourceCode.get(breakPoint.intValue());
        if (sourceLine.matches(".*(\\{|int|void|boolean|if|while|return|=).*")) {
            breakpoints.put(breakPoint, breakPoint);
            return true;
        } else {
            return false;
        }


    }

    // the FER methods to be calles, encapsulated 
    public int getSize() {
        return sourceCode.size();
    }

    public int getFirstFunc() {
        return envStack.peek().getStartLine();
    }

    public int getLastFunc() {
        return envStack.peek().getEndline();
    }

    public String getCurrentFunc() {
        return envStack.peek().getFunctionName().split("<<")[0];
    }

    //Displays the variables 
    public void showVariables() {
        // System.out.println("empty stack here");
        System.out.println(envStack.peek().vars());


    }

    public void checkBP() {

        if (step && (!stepin && !stepover && !stepout)) {
            step = false;
            bp = true;
        }

        Integer line = new Integer(envStack.peek().getCurrentLine());


        if (breakpoints.containsKey(line)) {
            bp = true;

        } else {
            bp = false;
        }

    }

    public String displaySourceCode() {

        String output = "";
        int currentLine = 0;
        int startLine = envStack.peek().getStartLine();
        int endLine = envStack.peek().getEndline();

        if ((envStack.size() == 0)) {
            currentLine = 0;


        } else {
            currentLine = envStack.peek().getCurrentLine();
        }
        if (envStack.peek().getCurrentLine() == 0 || envStack.peek().getStartLine() == -2) {
            startLine = 1;
            endLine = sourceCode.size() - 1;
        }

        if (startLine == -1) {
            return "*******" + envStack.peek().getFunctionName() + "********";
        }

        for (int i = 1; i < sourceCode.size(); i++) {

            if (bp && breakpoints.containsKey(new Integer(i))) {

                output += "*";
            } else {
                output += " ";
            }

            output += String.format("%02d. %s", i, sourceCode.get(i));

            if (i > 0 && i == currentLine) {
                output += " <=======";
            }

            output += "\n";
        }
        return output;


    }

    public void setBP(boolean value) {
        bp = value;

    }

    public boolean isBP() {
        return bp;

    }

    public String listBPs() {
        String output = "Breakpoints set are at lines :  ";
        for (Integer i : breakpoints.keySet()) {
            if (i > 0) {
                output += i.toString() + " ";
            }
        }

        return output;

    }

    public String displayFunctionSource() {

        String out = new String();

        if (envStack.peek().getFunctionName() == null) {
            out += "No function entered yet.";
        } else {

            out += "Function: ";
            out += envStack.peek().getFunctionName();
            out += "\n--------------------\n";

            for (int i = envStack.peek().getStartLine(); i <= envStack.peek().getEndline(); i++) {
                out += String.format("%3d. %s", i, sourceCode.get(i));
                if (i == envStack.peek().getCurrentLine()) {
                    out += "\t\t<=====";
                }
                out += "\n";
            }
        }
        return out;

    }

    public String displayChanges() {
        String output = "";
        for (int i = 0; i < newSourceCode.size(); i++) {
            output += String.format("[%02d]. %s\n", (i + 1), newSourceCode.get(i));
        }

        return output;
    }

    public String changeValue(String id, int value) {

        String out = "";
        Integer old = envStack.peek().getValue(id);

        if (old == null) {

            out += ("Unable to set " + value + " to " + id);
            return out;
        }
        envStack.peek().enterSymbol(id, value);

        out += ("set  " + value + " to " + id);
        return out;


    }

    //Modifying the source code methods
    public String insertStatement(int line, String value) {
        newSourceCode.add(line, value);
        displayChanges();
        change = true;
        return ("Statement was inserted after "+line );
    }

    public String replaceStatement(int line, String value) {
        newSourceCode.set(line - 1, value);
        displayChanges();
        change = true;
        return ("Line "+ line + "was replaced");
    }

    public boolean hasChanged() {
        return change;
    }

    //Saving the changes 
    public void saveChanges(String filename) {
        try {
            PrintWriter output = new PrintWriter(filename);
            String out = "";

            for (String line : newSourceCode) {
                out += line + "\n";
            }

            output.print(out);
            output.close();

            change = false;
        } catch (Exception e) {
            change = true;
        }


    }
    //Watching the variables , adding it to the watch list , so as to get back the value in the variable 

    public String watchVariables(String variable, int line) {
        Integer lineInteger = new Integer(line);
        Set<String> watchVaribleList = onWatchList.get(lineInteger);
        if (watchVaribleList == null) {
            watchVaribleList = new HashSet<String>();
            onWatchList.put(lineInteger, watchVaribleList);
        }
        watchVaribleList.add(variable);
        return ("Watching "+variable+ " at line "+ line );
    }

    //The stepping methods - step in, step out and step over
    //Stepping into the function, line
    public void stepIn() {
        stepin = true;
        this.bp = false;
        step = true;


    }

    //Stepping over to the next line
    public void stepOver() {

        int current = envStack.peek().getCurrentLine();
        if (lineNumber != current && lineNumber != -1 && current != -1) {
            this.bp = true;
            stepover = false;

        } else {
            stepover = true;
            stepin = false;
            step = true;
            this.bp = false;
        }

        if (current != -1) {
            lineNumber = current;
        }

    }

    //Stepping out until you are out of that current function 
    public void stepOut() {

        if (tempN == n) {
            stepout = false;
            bp = true;

        } else {
            stepout = true;
            stepin = false;
            bp = false;
            step = true;
        }


    }
    // used by the step over method to get the current line 

    public int getCurrentLine() {
        return envStack.peek().getCurrentLine();
    }
}