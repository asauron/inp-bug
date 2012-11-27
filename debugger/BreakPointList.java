/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.debugger;

/**
 *
 * @author asauron
 */
public class BreakPointList {
    private String sourceLine;
    private boolean isBPSet;

    // to be used by the debugVM
    
    public BreakPointList(String sourceLine, boolean isBPSet) {
        //reads "that" source line
        this.isBPSet = isBPSet;
        this.sourceLine = sourceLine;
    }

   //return the source line
    public String getSourceLine() {
        return sourceLine;
    }
    
    //checks whether or not a breakpoint is set
    public boolean isBPSet() {
        return isBPSet;
    }

   //sets the boolean value of that line for a breakpoint
    public void setBP(boolean breakPoint) {
        isBPSet = breakPoint;
    }
}