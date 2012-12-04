
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.debugger;

import interpreter.Program;
import interpreter.RunTimeStack;
import interpreter.VirtualMachine;
import interpreter.bytecodes.ByteCode;
import interpreter.debugger.UI.UI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

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
    private boolean stepout;
    private ArrayList <String> newSourceCode;

    public debugVM(Program program, ArrayList<String> sourceCode) {
        super(program);
        waitingForBreakpoint = true;
        this.isRunning = true;
        this.pc = 0;
        this.currentByteCode = null;
        this.runTimeStack = new RunTimeStack();
        this.returnAddress = new Stack<Integer>();
        this.sourceCode = sourceCode;
        //this.lineChanged = false;
        this.envStack = new Stack<FunctionEnvironmentRecord>();
        breakpoints = new HashMap<Integer, Integer>();
        this.breakpoints.put(new Integer(0), new Integer(0));
        this.newSourceCode = new ArrayList<String>();
        newSourceCode = (ArrayList <String>)sourceCode.clone();
	newSourceCode.remove(0); // empty newline at the top that we insert



    }

    public void intiliaze() {
        addFunction();
        setStartedLine(0);
        super.isRunning(true);
    }

    @Override
    public void executeProgram() {
        int envsize = envStack.size();

        while (!bp) {
            currentByteCode = program.getBytecode(pc);
         //   System.out.println("executing: " + currentByteCode.getName() + " " + currentByteCode.toString());
            currentByteCode.execute(this);
            pc++;

        }



    }

    public void endIT() {
        super.isRunning(false);
       popFER();


    }

    @Override
    public void isRunning(boolean value) {
       if (value == false) {
            endIT();
        }
        this.isRunning = value;
        
    }

    //==============//

    /* bytecode methods */
//the debug return bytecode 
    public void popFER() {
       envStack.pop();
      if (stepout) {
           bp = true;
           stepout=false;
        }
    }

// adds a single record entry at the given offsetLit code 
    public void enterRecord(String id, int offset) {
        envStack.peek().enterSymbol(id, offset);
    }

    // debug pop
    public void popFERLiterals(int n) {
        envStack.peek().popSymbol(n);
    }
    //call bytecode

    public void addFunction() {
        envStack.push(new FunctionEnvironmentRecord());
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
    public void pushRecord(String formalCode, int offset) {//enterStrinf

        envStack.peek().enterSymbol(formalCode, (runTimeStack.size() - offset) - 1);
    }

    ////Line Byte code
    public void setStartedLine(int lineNumber) {
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

    public void removeBP(Integer breakPoint) {
        if(breakpoints.containsValue(breakPoint)){
        breakpoints.remove(breakPoint);
        System.out.println("Break Points were  cleared at : " + breakPoint);
        displaySourceCode();}
        else
            System.out.println("No bp set at that line :"+ breakPoint);

    }

    //getting the line number of that breakpoint, and checking whether it is true 
    public boolean setBP(Integer breakPoint) {
        // System.out.println(breakPoint);
        String sourceLine = sourceCode.get(breakPoint.intValue());
        if (sourceLine.matches(".*(\\{|int|void|boolean|if|while|return|=).*")) {
            breakpoints.put(breakPoint, breakPoint);
            return true;
        } else {
            return false;
        }


    }

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

    //quit command
    public void showVariables() {
        // System.out.println("empty stack here");
        System.out.println(envStack.peek().vars());
    }

    public void checkBP() {
        
        Integer line = new Integer(envStack.peek().getCurrentLine());


        if (breakpoints.containsKey(line)) {
            bp = true;

        } else {
            bp = false;
        }

    }

    public void displaySourceCode() {

        String output = "";
        int currentLine = 0;

        if ((envStack.size() == 0)) {
            currentLine = 0;


        } else {
            currentLine = envStack.peek().getCurrentLine();
        }

        for (int i = 1; i < sourceCode.size(); i++) {

            if (bp && breakpoints.containsKey(new Integer(i))) {
                // System.out.println("in diisplay");
                output += "*";
            } else {
                output += " ";
            }

            output += String.format("%02d. %s", i, sourceCode.get(i));

            if (i > 0 && i == currentLine) {
                output += " <----";
            }

            output += "\n";
        }
        System.out.println(output);


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

    public void stepOut() {
        //throw new UnsupportedOperationException("Not yet implemented");
     
       stepout = true;
       bp = false;
      // popFER();
       executeProgram();

      
      
    }

    public String displayFunctionSource() {
        //throw new UnsupportedOperationException("Not yet implemented");
        String out = new String();

        if( envStack.peek().getFunctionName() == null ) {
            out+="No function entered yet.";
        } else {

            out+="Function: ";
            out+= envStack.peek().getFunctionName();
            out+="\n--------------------\n";

            for( int i = envStack.peek().getStartLine(); i <= envStack.peek().getEndline(); i++) {
                 System.out.print( String.format("%3d. %s", i, sourceCode.get(i) ));
                 if( i == envStack.peek().getCurrentLine())
                     System.out.print("\t\t<---");
                 System.out.println();
            }
        }
            return out;
        
    }
    
    public String displayChanges() {
		String output = "";
		for (int i = 0; i < newSourceCode.size(); i++) {
			output += String.format("%02d. %s\n", (i + 1), newSourceCode.get(i));
		}

		return output;
	}

   public String changeValue(String id, int value) {
     //  throw new UnsupportedOperationException("Not yet implemented");
          //   System.out.println(id+value);
       String out = "";
             Integer old = envStack.peek().getValue(id);
         
		if (old == null) {
			
                        out+=("Unable to set "+ value  + " to "+ id) ;
                        return out;
		}
                envStack.peek().enterSymbol(id, value);
           
                 out+=("set  "+ value + " to "+ id) ;
                 return out;
            
                 
    }
   
   

    public void insertStatement(int line, String value) {
        //throw new UnsupportedOperationException("Not yet implemented");
       // System.out.println(line+value);
        newSourceCode.add(line,value);
        }
    public void replaceStatement(int line,String value){
        newSourceCode.set(line,value);
    }
}
