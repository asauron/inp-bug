
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.debugger;

import interpreter.Program;
import interpreter.RunTimeStack;
import interpreter.VirtualMachine;
import interpreter.bytecodes.ByteCode;
import java.util.ArrayList;
import java.util.Stack;

/*
 *The debugVM inplements encapsulation so that the function environment Methods are only 
 * called from here
 */

public class debugVM extends VirtualMachine {

    private ArrayList<BreakPointList> sourceCode;
    private Stack<FunctionEnvironmentRecord> envStack;
    private ByteCode currentByteCode;
    private String command;
    private boolean waitingForBreakpoint;
    private boolean lineChanged;


    public debugVM(Program program, ArrayList<BreakPointList> sourceCode) {
        super(program);
	waitingForBreakpoint = true;
        this.isRunning = true;
        this.pc = 0;
        this.currentByteCode = null;
        this.runTimeStack = new RunTimeStack();
        this.returnAddress = new Stack<Integer>();
        this.sourceCode = sourceCode;
        this.lineChanged = false; 
        this.envStack = new Stack<FunctionEnvironmentRecord>();
        FunctionEnvironmentRecord dummy = new FunctionEnvironmentRecord();
        //The first line of every program is set back by one line because of the numerous null
        //pointer exceptions  and empty stack exceptions that i got ,
        //this does not affect the funcionality of the debugger
        dummy.setFunctionName("empty record");
        dummy.setStartLine(1);
        dummy.setEndLine(sourceCode.size());
        dummy.setCurrentLine(1);
        this.envStack.add(dummy);

    
    }

    @Override
    
    public void executeProgram() {
  
		while (waitingForBreakpoint ) {
            currentByteCode = program.getBytecode(pc);
	//System.out.println("executing: " + currentByteCode.getName() + " " + currentByteCode.toString());
          //  if (currentByteCode.getName().matches("READ"))
            //    System.out.print(readPrompt);

            currentByteCode.execute(this);
            pc++;
        }
           command = null;

    }
    
    /* bytecode methods */
// the function byte code adds a record to the FER stack
   public void addFER(String name, int startLine, int endLine) {
        FunctionEnvironmentRecord record = new FunctionEnvironmentRecord();
        record.setFunctionName(name);
        record.setStartLine(startLine);
        record.setEndLine(endLine);
        record.setCurrentLine(getCurrentLine());
        envStack.add(record);


    }

//the debug return bytecode pops the top, similar to the interpreter's return
    public void popFER() {
      envStack.pop();
    }

// adds a single record entry at the given offset, like the lit code 
    public void enterRecord(String id, int offset) {
        FunctionEnvironmentRecord record = envStack.pop();
        record.add(id, offset);
        envStack.add(record);
    }

    // pops n number of entries from the stack , like the pop code 
    public void popFERLiterals(int n) {
        FunctionEnvironmentRecord record = envStack.pop();
        record.pop(n);
        envStack.add(record);
    }


      //a boolean for the breakpoint, for the continue method                                      
    public void setWaitingForBreakpoint(boolean waiting) {
		waitingForBreakpoint = waiting;
	}
  
    //return's the vm' status
    @Override
    public boolean isRunning() {
        return isRunning;
    }
    
    //returns the source code 
     public ArrayList<BreakPointList> getSourceLines() {
        return sourceCode;
    }

    // FER methods 
    public String getSourceLine(int lineNumber) {
        return sourceCode.get(lineNumber - 1).getSourceLine(); //lineNumber - 1- added a dummy record at 1
    }

    
     public int getCurrentLine() {
         return  envStack.peek().getCurrentLine();
    }

   
    public void setCurrentLine(int lineNumber) {
       if(lineNumber >= 0 && envStack.size() == runTimeStack.frameSize() +1  ) { //+1 , added an extra dummy record
            FunctionEnvironmentRecord record = envStack.pop();
            record.setCurrentLine(lineNumber);
            envStack.add(record);
            lineChanged = true;

			if (isBPSet(lineNumber)) {
				waitingForBreakpoint = false;
			}
		}
    }
    
    public boolean setBP(int lineNumber, boolean breakPoint) {
        BreakPointList sourceLine = sourceCode.get(lineNumber);
        String line = sourceLine.getSourceLine();
        if (line.matches(".*(\\{|int|void|boolean|if|while|return|=).*")) {
            sourceLine.setBP(breakPoint);
            return true;
        } else {
            return false;
        }
    }


    

    public int getSize() {
        return sourceCode.size();
    }
    
   
    
    public boolean isBPSet(int line) {
        if (line > 0) {
                return sourceCode.get(line -1).isBPSet(); //line -1
        }
        else {
            return false;
        }
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


    public void showVariables() {
	System.out.println(envStack.peek().vars());
	}


    
    
   

}


