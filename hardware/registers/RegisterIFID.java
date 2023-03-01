package hardware.registers;

import hardware.utility.BitUtility;
import ui.MainView;

import java.util.BitSet;
import java.util.HashMap;

public class RegisterIFID extends Register{

    public static String INSTRUCTION = "instruction";
    public static String PCp1 = "pcplus1";

    public RegisterIFID(MainView mainView) {
        super(mainView, "registerIFID");

        this.values.put(INSTRUCTION, new BitSet(32));
        this.values.put(PCp1, new BitSet(32));

        for(HashMap.Entry<String, BitSet> set : values.entrySet()) {
            waitingValues.put(set.getKey(), new BitSet(set.getValue().length()));
        }
    }

    public void print() {
        System.out.println("RegisterIFID");
        for(HashMap.Entry<String, BitSet> set : values.entrySet()) {
            System.out.print("\t");
            System.out.println(set.getKey() + " : " + Integer.toString(BitUtility.BinToIntSigned(set.getValue()), 2));
        }
    }

}
