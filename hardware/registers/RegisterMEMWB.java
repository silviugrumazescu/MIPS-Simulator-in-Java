package hardware.registers;

import hardware.utility.BitUtility;
import ui.MainView;

import java.util.BitSet;
import java.util.HashMap;

public class RegisterMEMWB extends Register{

    public static String ControlSignals = "Control Signals";
    public static String ReadData = "Read Data";
    public static String ALURes = "Alu Result";
    public static String WBAddr = "WBAddr";

    public RegisterMEMWB(MainView mainView) {
        super(mainView, "registerMEMWB");

        this.values.put(ControlSignals, new BitSet(9));
        this.values.put(ReadData, new BitSet(32));
        this.values.put(ALURes, new BitSet(32));
        this.values.put(WBAddr, new BitSet(5));

        for(HashMap.Entry<String, BitSet> set : values.entrySet()) {
            waitingValues.put(set.getKey(), new BitSet(set.getValue().length()));
        }

    }

    public void print() {
        System.out.println("RegisterMEMWB");
        for(HashMap.Entry<String, BitSet> set : values.entrySet()) {
            System.out.print("\t");
            System.out.println(set.getKey() + " : " + Integer.toString(BitUtility.BinToIntSigned(set.getValue()), 2));
        }
    }

}
