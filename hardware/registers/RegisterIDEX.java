package hardware.registers;

import hardware.utility.BitUtility;
import ui.MainView;

import java.util.BitSet;
import java.util.HashMap;

public class RegisterIDEX extends Register{

    public static String ControlSignals = "Control Signals";
    public static String PCp1 = "PCp1";
    public static String RD1 = "ReadData1";
    public static String RD2 = "ReadData2";
    public static String Immediate = "Immediate";
    public static String Function = "Alu function";
    public static String RT = "RT";
    public static String RD = "RS";
    public static String SA = "Shift Amount";

    public RegisterIDEX(MainView mainView) {
        super(mainView, "registerIDEX");

        this.values.put(ControlSignals, new BitSet(11));
        this.values.put(PCp1, new BitSet(32));
        this.values.put(RD1, new BitSet(32));
        this.values.put(RD2, new BitSet(32));
        this.values.put(Immediate, new BitSet(32));
        this.values.put(Function, new BitSet(6));
        this.values.put(RT, new BitSet(5));
        this.values.put(RD, new BitSet(5));
        this.values.put(SA, new BitSet(5));

        for(HashMap.Entry<String, BitSet> set : values.entrySet()) {
            waitingValues.put(set.getKey(), new BitSet(set.getValue().length()));
        }
    }

    public void print() {
        System.out.println("RegisterIDEX");
        for(HashMap.Entry<String, BitSet> set : values.entrySet()) {
            System.out.print("\t");
            System.out.println(set.getKey() + " : " + Integer.toString(BitUtility.BinToIntSigned(set.getValue()), 2));
        }
    }
}
