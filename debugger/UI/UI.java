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

        // dvm.displaySource();
        //printSourceLines();
        displaySourceCode();
        System.out.println("Debugger: type '?' for commands.");

        while (!stop && dvm.isRunning()) {
            try {
                System.out.print(">> ");
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                command = input.readLine().toLowerCase();
                executeCommand(command);
            } catch (IOException ex) {
            }
        }
        System.out.println("****Quit Execution: stoping Debugger****");
    }

    private static void displaySourceCode() {
        int startLine = dvm.getFirstFunc();
        int endLine = dvm.getLastFunc();
        int currentLine = dvm.getCurrentLine();

        String output = "";

       /*  if(startLine == 0 || endLine == 0){
            currentLine = 1;
            startLine = 1;}*/
       if (startLine < 0 || endLine < 0 || currentLine < 0 ) {
            output = "****" + dvm.getCurrentFunc() + "****";
        } else {
            for (int line = startLine; line <= endLine; line++) {
                if (dvm.isBPSet(line)) {
                    output += "*";
                } else {
                    output += " ";
                }

             
                output += line + ". " + dvm.getSourceLine(line);

                if (line == currentLine) {
                    output += " <----";
                }



                output += "\n";
            }
        }
        System.out.println(output);
       /*  if(startLine == 0 || endLine == 0){
            currentLine = -1;
            startLine = 1;
           
        }*/
        
        
    }
    
    //prints out the help menu

    private static void help() {
        String helpMenu = "";
        helpMenu += "? or help      - display the command menu\n";
        helpMenu += "continue       - continue execution \n";
        helpMenu += "brkpointlst    - lists all breakpoints \n";
        helpMenu += "breakpoint  N   -sets breakpoints at N(mulitple accepted too)\n";
        helpMenu += "clear N         -Clears the breakpoint at N(multiple accepted too)\n";
        helpMenu += "source          - displays the source code \n";
        helpMenu += "variables       - displays varaibles in the stack \n";
        helpMenu += "q               - quits execution\n";

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
        } else if (command.matches("continue")) {
            dvm.setWaitingForBreakpoint(true);
            contExecution();
        } else if (command.matches("brkpointlst")) {
            listAllBreakPoints();
            // the argument should match the integer, regular expression
        } else if (command.matches("breakpoint") && arg.matches("[\\d+\\s*]+")) {
            setBPs(arg);
        } else if (command.matches("clear") && arg.matches("[\\d+\\s*]+")) {
            clearBreakPoints(arg);
        } else if (command.matches("source")) {
            displaySourceCode();
        } else if (command.matches("variables")) {
            displayVariables();
        } else if (command.startsWith("q")) {
            quit();
        } else {
            System.out.println("Command does not exist; type '?' to get a list of avalible commands.");
        }
    }
    
    
    /*
     * Break point methods
     */

    private static void setBPs(String lineNumbers) {
        String[] lines = lineNumbers.split(" ");
        String brkpts = "";
        for (String line : lines) {
            int lineNum = Integer.parseInt(line);

            if (lineNum <= dvm.getSize()) {
                //checking if a breakpoint can be set at the particular line of code 
                boolean success = dvm.setBP(lineNum - 1, true); //lineNumber - 1
                if (success) {
                    brkpts += lineNum + " ";
                } else {
                    System.out.println("Breakpoint cannot be set on this line : number : " + lineNum);
                }
            } else {
                System.out.println("Sorry, line " + lineNum + " does not exist");
            }
        }

        if (!brkpts.isEmpty()) {
            System.out.println("Break Points were set at : " + brkpts);
        }
    }

    private static void clearBreakPoints(String lineNumbers) {
        String[] lines = lineNumbers.split(" ");
        if (lines == null) {
            System.out.println("enter a breakpoint");
        }
        String brkpts = "";
        for (String line : lines) {
            int lineNum = Integer.parseInt(line);

            if (lineNum <= dvm.getSize()) {
                //checking if a breakpoint has been set at that line
                boolean breakpointSet = dvm.setBP(lineNum - 1, false);//lineNumber - 1
                if (breakpointSet) {
                    brkpts += lineNum + " ";
                }
            } else {
                System.out.println("Sorry, line " + lineNum + " does not exist");
            }
        }

        if (!brkpts.isEmpty()) {
            System.out.println("Break Points were  cleared at : " + brkpts);
        } else {
            System.out.println("There were no break points to be cleared");
        }
    }

    private static void listAllBreakPoints() {
        String breakpoints = "";
        for (int line = 1; line <= dvm.getSize(); line++) {
            if (dvm.isBPSet(line)) {
                breakpoints += line + " ";
            }
        }

        System.out.println("Current Break Points: " + breakpoints);
    }
    
    
    //Continues execution

    private static void contExecution() {
        dvm.executeProgram();
        displaySourceCode();

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
}