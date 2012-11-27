/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.debugger;

import interpreter.ByteCodeLoader;
import interpreter.CodeTable;
import interpreter.Program;
import interpreter.debugger.UI.UI;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author asauron
 */
public class DebugBCL  {
    private ArrayList<BreakPointList> sourceCode;
    private ByteCodeLoader bcl;
 

    /**
     *
     * @param codeFile
     */
    public DebugBCL(String codeFile) {
      
        CodeTable.dInit();

        try {
            String sourceFile = codeFile + ".x";
            codeFile += ".x.cod";
            bcl = new ByteCodeLoader(codeFile);
            sourceCode = SourceCodeLoader.read(sourceFile);
            System.out.println("****Debugging " + sourceFile + "****");
        
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }

    public void run() throws IOException {
        Program program = bcl.loadCodes();
        debugVM dvm = new debugVM(program, sourceCode);
        UI.display(dvm);
       // dvm.displayInterface(dvm);
        
    }
}