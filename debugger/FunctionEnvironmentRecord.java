/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.debugger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;


/**
 * Use , store and retrieve function informations
 *
 * @author asauron
 */
public class FunctionEnvironmentRecord {

    private SymbolTable table;
    private int startLine, endLine, currentLine;
    private String functionName;
   // private String name;

    public FunctionEnvironmentRecord() {
        table = new SymbolTable();
        // table.beginScope();
               startLine = new Integer(-1);
		endLine = new Integer(-1);
		currentLine = new Integer(-1);
    }

   /* public static void main(String[] args) throws FileNotFoundException, IOException {
         FunctionEnvironmentRecord fctEnvRecord = new FunctionEnvironmentRecord();
        if (args.length == 0) {
            System.out.println("***Incorrect usage, try: java - jar interpreter.jar <file name>");
            System.exit(1);
        }
      
       BufferedReader programFile = new BufferedReader(new FileReader(args[0]));
       while (programFile.ready()){
        String test = programFile.readLine();
        fctEnvRecord.processFER(test);
       }

    }*/

    private void processFER(String test) {
        //throw new UnsupportedOperationException("Not yet implemented");
        String[] input = test.split("\\n");
        for (int i = 0; i < input.length; i++) {
            String[] token = input[i].split("\\s");
            //begin scope, to set the table
            if (token[0].equals("BS")) {
                //begin scope of the process
                table.beginScope();
            } else if (token[0].equals("Function")) {
                //setting the start, end , current lines and function names
                setFunctionName(token[1]);
                setStartLine(Integer.parseInt(token[2]));
                setEndLine(Integer.parseInt(token[3]));
                setCurrentLine(0);
            } else if (token[0].equals("Line")) {
                //setting the current line number
                setCurrentLine(Integer.parseInt(token[1]));
            } else if (token[0].equals("Enter")) {
                table.put(token[1], Integer.parseInt(token[2]));

            } else if (token[0].equals("Pop")) {
                //popping
                table.endScope(Integer.parseInt(token[1]));
            }
           
            showDump();
            System.out.println();
        }
    }

    /**
     * getters and setters for line numbers and function names
     */
    public void setStartLine(int n) {
        startLine = n;
    }

    public int getStartLine() {
        return startLine;
    }

    public void setEndLine(int n) {
        endLine = n;
    }

    public int getEndline() {
        return endLine;
    }

    public void setCurrentLine(int n) {
        currentLine = n;
    }

    public int getCurrentLine() {
        return currentLine;
    }

    public void setFunctionName(String name) {
        functionName = name;
    }

    public String getFunctionName() {
        return functionName;
    }

    private void showDump() {
        // throw new UnsupportedOperationException("Not yet implemented");
        String output =  new String();
        output += "(" +"<" + table.getName() + ">,";
         //if function name is set, get it, else add the "-"
        if (functionName != null) {
            output += functionName + ",";
        } else {
            output += "- ,";
        }
        //if endline set, add endLine, else add "-"
        if (startLine > 0) {
            output += startLine + ",";
        } else {
            output += "- ,";
        }
        //if endline set, add endLine, else add "-"
        if (endLine > 0) {
            output += endLine + "," ;
        } else {
            output += "- ,";
        }
         //if currentline set, add currentLine, else add "-"
        if (currentLine > 0) {
             output += currentLine;
        } else {
            output += "-";
        }
        output += ")";
        System.out.println(output);
    }

    void add(String formalCode, int offset) {
        //throw new UnsupportedOperationException("Not yet implemented");
        table.put(formalCode,offset);
    }

    int getVariableOffset(String var) {
       // throw new UnsupportedOperationException("Not yet implemented");
          return table.get(var);
    }

    Set<String> getVariables() {
       // throw new UnsupportedOperationException("Not yet implemented");
            return table.keys();
    }

    void pop(int numberOfPops) {
        //throw new UnsupportedOperationException("Not yet implemented");
           table.popValues(numberOfPops);
    }
    
    public String  vars() {
		String variables = "";
              //  System.out.println("in FER");

		for (String symbol : table.keys()) {
			variables += symbol + ": " + table.get(symbol) + "\n";
		}

		return variables;
	}

    void finishedLine(int lineNumber) {
        //throw new UnsupportedOperationException("Not yet implemented");
        currentLine = lineNumber;
    }

    void enterSymbol(String id, int offset) {
        //throw new UnsupportedOperationException("Not yet implemented");
        table.put(id,offset);
    }

    void popSymbol(int n) {
        //throw new UnsupportedOperationException("Not yet implemented");
        table.endScope(n);  
    }
    
    public Integer getValue(String symbol){
        return (Integer)table.get(symbol);
        
    }
    


   
}
class Binder {

    private Object value;
    private String prevTop;
    Binder tail;

    Binder(Object v, String p, Binder t) {
        value = v;
        prevTop = p;
        tail = t;
    }

    Object getValue() {
        return value;
    }

    String getPrevTop() {
        return prevTop;
    }

    Binder getTail() {
        return tail;
    }
}

/**
 * 1
 ** <pre>
 * The Table class is similar to java.utiI.Dictionary, except that
 * each key must be a Symbol and therc is a scope mechanism.
 * -
 * - -
 * -
 * -
 *
 * Consider the following sequcnce of events for table t:
 * t.put(Symbol("a"),5) * t.beginScopeO
 * Lput(Symbol(,'b"),7) * t.put(Symbol("a"),9)
 *
 * symbols will have the key/value pairs for Symbols "a" and "b" as:
 *
 * Symbol("a") ->
 * Binder(9. Symbol("b") , Binder(5, null, nUll) )
 * (the second field has a reference to the prior Symbol added in this * scope: the third field refers to the Binder for the Symbol("a")
 * included in the prior scope)
 * Binder has 2 linked lists - the second field contains list of symbols * added to the current scope: the third field contains the list of *Binders for the Symbols with the same string id - in this case. "a"
 *
 * Symbol("b") ->
 * Binder(7. null, null)
 * (the second field is null sinee there are no other symbols to link *in this scope: the third field is null since there is no Symbol("b")
 *
 *
 * top has a reference to Symbol("a") which was the last symbol added * to current scope
 *
 * Note: What happens if a symbol is deflned twice in the same scope?? .~
 * </pre>
 *
 *
 */

class SymbolTable {

    private  HashMap<String, Binder> symbols = new HashMap<String,Binder>();
    private String top;
    //private Binder marks;

    /**
     * Gets the object associated with the specified symbol in the Table.
     */
  /* public Object get(String Key) {
       Binder e = symbols.get(Key);
        return e.getValue();
    }*/
    
     public int get(String key) {
	int e = (Integer) symbols.get(key).getValue();
	return e;
         //Binder e = symbols.get(key);
         //return e.getValue();
  }

    /**
     * Puts the specified value into the Table, bound to the specified
     * Symbol.<br> * Maintain the list o f symbols in the current scope
     * (top);<br> Add to list of symbols in prior scope with the same string
     * identifier
     */
    public void put(String key, Object value) {
        symbols.put(key, new Binder(value, top, symbols.get(key)));
        top = key;
    }

    /*Remembers the current state ofthe Table; 
     * push new mark on mark stack */
    public void beginScope() {
        // marks = new Binder(null,top,marks);
        top = null;
        // throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Restores the table to what it was at the most recent beginScope * that
     * has not already been ended.
     */
    //public void endScope(){}
    public void endScope(int pop) {
        for (int i = 0; i < pop; i++) // while(top!= null)
        {
            Binder e = symbols.get(top);
            if (e.getTail() != null) {
                symbols.put(top, e.getTail());
            } else {
                symbols.remove(top);
            }
            top = e.getPrevTop();
        }
        // throw new UnsupportedOperationException("Not yet implemented");
    }

    public java.util.Set<String> keys() {
        return symbols.keySet();
    }
public void popValues(int numOfPops) {
	for (int i = 0; i < numOfPops; i++) {
            Binder e = symbols.get(top);
            if (e.getTail()!=null)
               symbols.put(top,e.getTail());
	   else
               symbols.remove(top);
	   top = e.getPrevTop();
        }
  }
  
    public String getName() {
        String output = "";
        for (String key : this.keys()) {
            output += key + "/" + get(key) + ",";
        }
        if (output.length() > 0) {
            output = output.substring(0, output.length() - 1);
        }
        return output;
    }

}

