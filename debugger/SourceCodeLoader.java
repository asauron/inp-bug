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
    
    public static ArrayList<String> read(String sourceFile) throws IOException {
        BufferedReader file = new BufferedReader(new FileReader(sourceFile));
        ArrayList<String> sourceLines = new ArrayList<String>();
        String nextLine = file.readLine();

       //intiating the arraylist , doesn't do anything 
        sourceLines.add(" ");
        //reading until the EOF is reached 
        while(nextLine != null) {
            sourceLines.add(nextLine);
            nextLine = file.readLine();
        }

        return sourceLines;
    }
}
