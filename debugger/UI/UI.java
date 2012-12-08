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

        System.out.println(dvm.displaySourceCode());

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
        helpMenu += "cv             - change values of local variables\n";
        helpMenu += "wv             - watch variables <line> <var>      \n";
        helpMenu += "is             - insert statement <line> <value>    \n";
        helpMenu += "lr             - replace line   <line> <value>       \n";
        helpMenu += "new            - preview changed source code      \n";
        helpMenu += "df             - display function source           \n";
        helpMenu += "q              - quits execution                   \n";
        helpMenu += "halt           - quit execution                    \n";
        System.out.println(helpMenu);
    }
    //reads in the user command and executes it

    private static void executeCommand(String command) throws IOException {
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
            System.out.println(dvm.displaySourceCode());
        } else if (command.matches("var")) {
            displayVariables();
        } else if (command.startsWith("q") || command.startsWith("halt")) {
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
            watchVariables(arg);
        } else if (command.startsWith("is")) {
            insertStatement(arg);
        } else if (command.startsWith("lr")) {
            replaceLine(arg);

        } else if (command.startsWith("new")) {
            previewChanges();
        } else if (command.startsWith("df")) {
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
                System.out.println(dvm.removeBP(lineNum));
            } else {
                System.out.println("Sorry, line " + lineNum + " does not exist");
            }
        }


    }

    //Continues execution
    private static void contExecution() {
        dvm.setBP(false);

    }

    //Displays the current variables in the environment stack
    private static void displayVariables() {
        dvm.showVariables();
    }

    //stops execution
    private static void quit() throws IOException {
        if (dvm.hasChanged()) {
            System.out.println("Changes were made to the program, do you want to save the changes? (y/n)");
            System.out.println(">> ");
            BufferedReader news = new BufferedReader(new InputStreamReader(System.in));
            String answer = news.readLine().toLowerCase();
            if (answer.startsWith("y")) {
                System.out.println("Enter a filename");
                System.out.println(">> ");
                BufferedReader file;
                file = new BufferedReader(new InputStreamReader(System.in));
                String filename = file.readLine().toLowerCase();
                dvm.saveChanges(filename);

                System.out.println("your changes were successfully saved");

            } else {
                System.out.println("Exiting program");
            }
        }
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
        dvm.tempN = dvm.n - 1;
        dvm.stepOut();

    }

    private static void stepIn() {
        // throw new UnsupportedOperationException("Not yet implemented");
        dvm.stepIn();
        // dvm.displayFunctionSource();
    }

    private static void stepOver() {
        //  throw new UnsupportedOperationException("Not yet implemented");
        // int line =  dvm.getCurrentLine();
        dvm.lineNumber = dvm.getCurrentLine();
        dvm.stepOver();
        // dvm.stepOver();
        //dvm.displayFunctionSource();
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

    private static void watchVariables(String arg) {

        // throw new UnsupportedOperationException("Not yet implemented");
        String line = arg.split(" ")[0];
        String variable = arg.split(" ")[1];
        int lineNum = Integer.parseInt(line);

      System.out.println(dvm.watchVariables(variable, lineNum));

    }

    private static void insertStatement(String arg) {

        String id = arg.split(" ")[0];

        int line = Integer.parseInt(id);
        String news = arg.split(" ", 2)[1];

        System.out.println(dvm.insertStatement(line, news));


    }

    private static void replaceLine(String arg) {
        //throw new UnsupportedOperationException("Not yet implemented");
        String id = arg.split(" ")[0];
        String value = arg.split(" ", 2)[1];
        int line = Integer.parseInt(id);
        System.out.println(dvm.replaceStatement(line, value));
    
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