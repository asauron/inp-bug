package interpreter;

import java.util.HashMap;
/*
 * @author asauron
 * Initiate all the labels
 */
 
public class CodeTable {
    // From the Reader
    
    public static HashMap<String, String> byteCode = new HashMap<String, String>();
    
    public CodeTable() {
        init();
    }

    public static void init() {
        byteCode.put("HALT", "HaltCode");
        byteCode.put("POP", "PopCode");
        byteCode.put("FALSEBRANCH", "FalseBranchCode");
        byteCode.put("GOTO", "GotoCode");
        byteCode.put("STORE", "StoreCode");
        byteCode.put("LOAD", "LoadCode");
        byteCode.put("LIT", "LitCode");
        byteCode.put("ARGS", "ArgsCode");
        byteCode.put("CALL", "CallCode");
        byteCode.put("RETURN", "ReturnCode");
        byteCode.put("BOP", "BopCode");
        byteCode.put("READ", "ReadCode");
        byteCode.put("WRITE", "WriteCode");
        byteCode.put("LABEL", "LabelCode");
        byteCode.put("DUMP", "DumpCode");
    }
     
     public static void dInit(){ 
        byteCode.put("FORMAL", "debugByteCodes.FormalCode");
        byteCode.put("FUNCTION", "debugByteCodes.FunctionCode");
        byteCode.put("LINE", "debugByteCodes.LineCode");
        byteCode.put("HALT", "HaltCode");
        byteCode.put("POP", "debugByteCodes.DebugPopCode");
        byteCode.put("FALSEBRANCH", "FalseBranchCode");
        byteCode.put("GOTO", "GotoCode");
        byteCode.put("STORE", "StoreCode");
        byteCode.put("LOAD", "LoadCode");
        byteCode.put("LIT", "debugByteCodes.DebugLitCode");
        byteCode.put("ARGS", "ArgsCode");
        byteCode.put("CALL", "debugByteCodes.DebugCallCode");
        byteCode.put("RETURN", "debugByteCodes.DebugReturnCode");
        byteCode.put("BOP", "BopCode");
        byteCode.put("READ", "ReadCode");
        byteCode.put("WRITE", "WriteCode");
        byteCode.put("LABEL", "LabelCode");
        byteCode.put("DUMP", "DumpCode");
    
     }
     
    
     public static String get(String code) {
        return byteCode.get(code);
    }
}

    
