package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.*;
/*
 * @author asauron
 * <binary op>
 * 
 */

public class BopCode extends ByteCode {

    private String bopArg;

    public void init(ArrayList<String> args) {
        bopArg = args.get(0);
    }

    public String toString() {
        return bopArg;
    }

    public void execute(VirtualMachine vm) {
        int first = vm.pop(); //the first   
        int second = vm.pop();// once the first has been popped, the next one in line
        int result = 0;

        if (bopArg.equals("+")) {
            result = second + first;
            vm.push(result);
        } else if (bopArg.equals("-")) {
            result = second - first;
            vm.push(result);
        } else if (bopArg.equals("/")) {
            result = second / first;
            vm.push(result);
        } else if (bopArg.equals("*")) {
            result = second * first;
            vm.push(result);
        } else if (bopArg.equals("&")) {
            if (second > 0 && first > 0) {
                vm.push(1);
            } else {
                vm.push(0);
            }
        } else if (bopArg.equals("|")) {
            if (second > 0 || first > 0) {
                vm.push(1);
            } else {
                vm.push(0);
            }
        } else if (bopArg.equals("==")) {
            if (second == first) {
                vm.push(1);
            } else {
                vm.push(0);
            }
        } else if (bopArg.equals("!=")) {
            if (second != first) {
                vm.push(1);
            } else {
                vm.push(0);
            }
        } else if (bopArg.equals("<=")) {
            if (second <= first) {
                vm.push(1);
            } else {
                vm.push(0);
            }
        } else if (bopArg.equals(">=")) {
            if (second >= first) {
                vm.push(1);
            } else {
                vm.push(0);
            }
        } else if (bopArg.equals(">")) {
            if (second > first) {
                vm.push(1);
            } else {
                vm.push(0);
            }


        } else if (bopArg.equals("<")) {
            if (second < first) {
                vm.push(1);
            } else {
                vm.push(0);
            }
        }



    }
}
