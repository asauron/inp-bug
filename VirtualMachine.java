package interpreter;

import interpreter.bytecodes.ByteCode;
import java.util.*;

/*
 * @author asauron
 *
 */
public class VirtualMachine {
    //

    protected RunTimeStack runTimeStack = new RunTimeStack();
    public int pc = 0;
    /*
     * returnAddress stack stores the bytecode index (PC) that the virtual machine should execute 
     * when the current function exits. Each time a function is entered,
     * the PC should be pushed on to the returnAddrs stack.
     * When a function exits the PC should be restored to the value 
     * that is popped from the top of the returnAddrs stack.
     */
    protected Stack returnAddress = new Stack();
    //State of the virtual machine
    protected boolean isRunning = true;
    protected Program program;
    //Status of the run stack dump
    public boolean dumpValue;

    public VirtualMachine(Program program) {
        this.program = program;
    }

    //From the reader
    public void executeProgram() {
        pc = 0;
        runTimeStack = new RunTimeStack();
        returnAddress = new Stack();
        isRunning = true;
        while (isRunning) {

            ByteCode code = program.getBytecode(pc);
            code.execute(this);
        //    System.out.println(code.toString()); label does get called here!
            //Dump the values in the stack when dump is turned on
          // if (dumpValue) {
                prettyDump(code);
          //  }
         pc++;  //incrementing the program counter 
        }

    }
    
    //From the reader, to print out the output in the required manner
     public void prettyDump(ByteCode code) {
      //  System.out.println(code.toString());
         //getting the "simple name", i.e remove interpreter.bytecodes and convert it to upper case
        String codeSimpleName = code.getName();
          String output = "";
      //  System.out.println(code);
        if (!codeSimpleName.matches("DUMP")) {
            //do not output dump on or off
            String[] argList = code.toString().split("\\s");
            output += codeSimpleName+" "+argList[0]+" ";

            // To format the code with special instructions
            if (codeSimpleName.matches("LIT|LOAD|STORE|RETURN|CALL|LABEL")) {
              //checking for the code simple names 
                if (codeSimpleName.matches("LIT")) {
                    if (argList.length > 1) {
                        output += argList[1]+"\t"+"int "+argList[1];
                    }
                    //   System.out.println(output);
                } else if (codeSimpleName.matches("LOAD")) {
                  output += argList[1]+"\t"+"<load "+argList[1]+">";
                   //  System.out.println(output);
                } else if (codeSimpleName.matches("STORE")) {
                    //the top most value in the runtime stack
                    output += argList[1]+"\t"+argList[1]+"="+runTimeStack.peek();
                     //  System.out.println(output);

                } else if (codeSimpleName.matches("RETURN")) {
                    String arg = argList[0].split("<<")[0];
                    output += "\t"+"exit "+arg+": "+runTimeStack.peek();
                      // System.out.println(output);
            
                } else if (codeSimpleName.matches("LABEL")){
                    //String arg = argList[0].split(" ")[0];
                    output += "\t";
                   //    System.out.println(output);
                }
                
                //doing it here so as to not break encapsulation
                else if (codeSimpleName.matches("CALL")) {
                    // call "the arg" with call "(the arg)"
                    String arg = argList[0].split("<<")[0];
                    String funcArgs = "";
                    for (int n = runTimeStack.peekFrame(); n < runTimeStack.size(); n++) {
                        funcArgs += runTimeStack.get(n);
                        if (n!= runTimeStack.size()-1) {
                            funcArgs += ",";
                        }
                    }
                  
             output += "\t"+arg+"("+funcArgs+")";//+"\n"+"LABEL"+arg+"\n";// + runTimeStack.dump();
               //System.out.println(""+codeSimpleName+" "+argList[0]+" "+spacing+arg+"("+funcArgs+")");
             // runTimeStack.dump();
              // System.out.println(""+"LABEL" + " " + argList[0]);
               //runTimeStack.dump();
                }
            }
            
           System.out.println(output);
            runTimeStack.dump();
        }
    }

    
    /*All the methods from the runtime stack are implemented from here so as to not 
     break encapsulation*/

    public void setNArgs(int numArgs) {
        runTimeStack.setNArgs(numArgs);
    }

    public int getNumArgs() {
        return runTimeStack.getNumArgs();
    }

    public int peek() {
        return runTimeStack.peek();
    }

    public int pop() {
        return runTimeStack.pop();
    }

    public int push(int i) {
        return runTimeStack.push(i);
    }

    public void newFrameAt(int offset) {
        runTimeStack.newFrameAt(offset);
    }

    public void popFrame() {
        runTimeStack.popFrame();
    }
    
    //new method
    public int peekFrame(){
       return runTimeStack.peekFrame();
    }

    public int store(int offset) {
        return runTimeStack.store(offset);
    }

    public int load(int offset) {
        return runTimeStack.load(offset);
    }

    public Integer push(Integer i) {
        return runTimeStack.push(i);
    }

    public int runStackSize() {
        return runTimeStack.size();
    }

    public void setReturnAddress() {
        returnAddress.push(new Integer(pc));
    }

    public int getReturnAddress() {
        //Type casted to an object
        return (Integer) returnAddress.pop();

    }

    public void setProgramCounter(int newpc) {
        this.pc = newpc;
    }

    public int getProgramCounter() {
        return pc;
    }

    public void isRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public void dumpRunStack(boolean dump) {
        dumpValue = dump;
    }

    
    
     public void stopRunning() {
        isRunning = false;
    }
     
     public int runStackGet(int i) {
        return runTimeStack.get(i);
     }

}