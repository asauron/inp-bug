/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.debugger.UI;

import interpreter.debugger.debugVM;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author asauron
 */
public class UI {

    private static debugVM dvm;
    private static boolean stop;

    public static void display(debugVM virtualMachine) {

        stop = false;
        dvm = virtualMachine;
        String command = null;

        //dvm.displaySourceCode();
        System.out.println("Debugger: type '?' for commands.");

        dvm.intiliaze();
        while (dvm.isRunning()) {
            readInput();
            // System.out.println("Done reading");
            dvm.executeProgram();
        }
        System.out.println("****Quit Execution: stoping Debugger****");
    }

    public static void readInput() {
        String command = null;
        dvm.displaySourceCode();
        //  dvm.displayFunctionSource();
        while (dvm.isBP() && dvm.isRunning()) {
            // System.out.println("breakpoint: " + dvm.isBP());
            try {
                System.out.print(">> ");
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                command = input.readLine().toLowerCase();
                executeCommand(command);

            } catch (IOException ex) {
            }
        }

    }

    private static void help() {
        String helpMenu = "";
        helpMenu += "? or help      - display the command menu\n";
        helpMenu += "c              - continue execution \n";
        helpMenu += "brklst         - lists all breakpoints \n";
        helpMenu += "bp  N          -sets breakpoints at N(mulitple accepted too)\n";
        helpMenu += "clr N          -Clears the breakpoint at N(multiple accepted too)\n";
        helpMenu += "src            - displays the source code \n";
        helpMenu += "var            - displays variables in the stack \n";
        helpMenu += "sof            - step out of current activation of frame\n";
        helpMenu += "si             - step into                      \n";
        helpMenu += "so             - step over                       \n";
        helpMenu += "cv             - change values of local variables \n";
        helpMenu += "wv             - watch variables                   \n";
        helpMenu += "is             - insert statement                   \n";
        helpMenu += "lr             - replace line                       \n";
        helpMenu += "new            - preview changed source code        \n";
        helpMenu += "df             - display function source             \n";
        helpMenu += "q              - quits execution                     \n";
        helpMenu += "halt           - quit execution                    \n";
        System.out.println(helpMenu);
    }
    //reads in the user command and executes it

    private static void executeCommand(String command) {
        String arg = "";
        if (command.split(" ").length > 1) {
            arg = command.split(" ", 2)[1];
        }
        command = command.split(" ")[0];

        if (command.startsWith("help") || command.startsWith("?")) {
            help();
        } else if (command.matches("c")) {
            //dvm.setWaitingForBreakpoint(true);
            contExecution();
            //dvm.continueExec();
        } else if (command.matches("brklst")) {
            listAllBreakPoints();
            // the argument should match the integer, regular expression
        } else if (command.matches("bp") && arg.matches("[\\d+\\s*]+")) {
            setBPs(arg);
        } else if (command.matches("clr") && arg.matches("[\\d+\\s*]+")) {
            clearBreakPoints(arg);
        } else if (command.matches("src")) {
            dvm.displaySourceCode();
        } else if (command.matches("var")) {
            displayVariables();
        } else if (command.startsWith("q")) {
            quit();
        } else if (command.startsWith("sof")) {
            stepOut();
        } else if (command.startsWith("si")) {
            stepIn();
        } else if (command.startsWith("so")) {
            stepOver();
        } else if (command.startsWith("cv")) {
            changeVariables(arg);
        } else if (command.startsWith("wv")) {
            watchVariables();
        } else if (command.startsWith("is")) {
            insertStatement(arg);
        } else if (command.startsWith("lr")) {
            replaceLine(arg);
            
        } else if (command.startsWith("new")) {
            previewChanges();
        }
        else if (command.startsWith("df")) {
            displayFunction();
        } else {
            System.out.println("Command does not exist; type '?' to get a list of avalible commands.");
        }
    }

    //Command Methods
    private static void clearBreakPoints(String lineNumbers) {
        String[] lines = lineNumbers.split(" ");
        for (String line : lines) {
            int lineNum = Integer.parseInt(line);
            //checking if a breakpoint has been set at that line
            if (lineNum <= dvm.getSize()) {
                dvm.removeBP(lineNum);
            } else {
                System.out.println("Sorry, line " + lineNum + " does not exist");
            }
        }


    }

    //Continues execution
    private static void contExecution() {
        dvm.setBP(false);
        //dvm.displayFunctionSource();
        //dvm.setWaitingForBreakpoint(true);
    }

    //Displays the current variables in the environment stack
    private static void displayVariables() {
        dvm.showVariables();
    }

    //stops execution
    private static void quit() {
        dvm.stopRunning();
        stop = true;
    }

    private static void setBPs(String lineNumbers) {

        String[] lines = lineNumbers.split(" ");

        for (String line : lines) {
            int lineNum = Integer.parseInt(line);

            if (lineNum <= dvm.getSize()) {

                if (dvm.setBP(lineNum)) {
                    System.out.println("Break Points were set at : " + lineNum);

                } else {
                    System.out.println("Breakpoint cannot be set on this line : number : " + lineNum);
                }
            } else {
                System.out.println("Sorry, line " + lineNum + " does not exist");
            }
        }



    }

    private static void listAllBreakPoints() {
        System.out.println(dvm.listBPs());
    }

    private static void stepOut() {
        //throw new UnsupportedOperationException("Not yet implemented");
        dvm.stepOut();
        dvm.displaySourceCode();
    }

    private static void stepIn() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private static void stepOver() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private static void changeVariables(String arg) {
      String id = arg.split(" ")[0];
      String value = arg.split(" ")[1];
      int number = Integer.parseInt(value);
      String change = dvm.changeValue(id, number);
      System.out.println(change);
      System.out.println("The variables now are");
      dvm.showVariables();

    }

    private static void watchVariables() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private static void insertStatement(String arg) {
       // System.out.println(arg);
        String id = arg.split(" ")[0];
        String value = arg.split(" ")[1];
        int line = Integer.parseInt(id);
        //System.out.println(line+" "+ value);
        dvm.insertStatement(line,value);
      //  String news = arg[3];
       // System.out.println(id+" "+value);
        
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    private static void replaceLine(String arg) {
        //throw new UnsupportedOperationException("Not yet implemented");
          String id = arg.split(" ")[0];
        String value = arg.split(" ")[1];
        int line = Integer.parseInt(id);
        dvm.replaceStatement(line,value);
    }

    private static void displayFunction() {
        // throw new UnsupportedOperationException("Not yet implemented");
        System.out.println(dvm.displayFunctionSource());
    }

    private static void previewChanges() {
       // throw new UnsupportedOperationException("Not yet implemented");
        System.out.println(dvm.displayChanges());
    }
    
    
}