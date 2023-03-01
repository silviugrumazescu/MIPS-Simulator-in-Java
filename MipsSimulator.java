import hardware.MIPS;
import hardware.utility.BitUtility;
import ui.MainView;

import java.util.BitSet;
import java.util.Scanner;

public class MipsSimulator {
    public static void main(String[] args) {

        /*MIPS mips = new MIPS();

        System.out.println("Initialized MIPS");
        mips.printRegisters();
        Scanner in = new Scanner(System.in);
        while (true) {
            String s = in.nextLine();
            if(s.substring(0,4).equals("next")) {
                mips.step();
                mips.printRegisters();
            }
            else if(s.substring(0, 11).equals("printMemory")) {
                mips.printMemory();
            }
            else if(s.substring(0,14). equals("printRegisters")) {
                mips.printRegFile();
            }
        }
        */

        //MainView mainView = new MainView();
        MainViewController mainViewController = new MainViewController();




    }

}
