/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.debugger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author asauron
 * Loads the source code onto a 
 */
public class SourceCodeLoader {
    
    public static ArrayList<BreakPointList> read(String sourceFile) throws IOException {
        BufferedReader file = new BufferedReader(new FileReader(sourceFile));
        ArrayList<BreakPointList> sourceLines = new ArrayList<BreakPointList>();
        String nextLine = file.readLine();

       //intiating the arraylist , doesn't do anything 
        sourceLines.add(new BreakPointList(" ", false));
        //reading until the EOF is reached 
        while(nextLine != null) {
            sourceLines.add(new BreakPointList(nextLine, false));
            nextLine = file.readLine();
        }

        return sourceLines;
    }
}
