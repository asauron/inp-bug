package interpreter;

import interpreter.bytecodes.ByteCode;
import java.io.*;
import java.util.*;

/*
 *  @author asauron 
 * ByteCode Loader class will bytecodes from the file "program file"
 * into the virtual machine
 * 
 */
public class ByteCodeLoader {

    private String programFile;

    public ByteCodeLoader(String ByteCodeFile) throws IOException {
        programFile = ByteCodeFile;
    }
    /*The loadCodes method loades all the codes from the file
     and asks the program to resolve addresses.*/

    public Program loadCodes() throws IOException {
        String nextLine;
        BufferedReader codeFile = null;
        
        try {
            codeFile = new BufferedReader(new FileReader(programFile));
        } catch (Exception e) {
            System.out.println(e);
        }

        Program program = new Program();

        try {
            while ((nextLine = codeFile.readLine()) != null) {
                ArrayList<String> byteCodeArgs = new ArrayList();
                StringTokenizer tokens = new StringTokenizer(nextLine);
                String codeClass = CodeTable.get(tokens.nextToken()); 
                
                //From the reader
               ByteCode bc = (ByteCode) (Class.forName("interpreter.bytecodes." + codeClass).newInstance());
                //System.out.println(bc.toString());//debugging output
                while (tokens.hasMoreTokens()) {
                    byteCodeArgs.add(tokens.nextToken());
                }

                bc.init(byteCodeArgs);
                program.addLabelBytecode(bc);
            }
            codeFile.close();
        } catch (Exception e) {
            System.out.println("In the bytecode loader " + e);
        }
       
        //Requesting the program class to resolve the addresses
        program.resolveAddresses();

        return program;
    }
    
    protected String getCodeClass(String code) {
        return CodeTable.get(code);
    }

}