package hardware.registers;

import hardware.utility.BitUtility;
import ui.MainView;

import java.util.BitSet;
import java.util.HashMap;

public class RegisterEXMEM extends Register{

    public static String ControlSignals = "Control Signals";
    public static String BranchAddress = "Branch Address";
    public static String ZeroSig = "Zero Signal";
    public static String GreaterSig = "Greater Signal";
    public static String AluRes = "Alu Result";
    public static String RD2 = "RD2";
    public static String WBAddr = "WBAddr";

    public RegisterEXMEM(MainView mainView) {
        super(mainView, "registerEXMEM");
        this.values.put(ControlSignals, new BitSet(11));
        this.values.put(BranchAddress, new BitSet(32));
        this.values.put(ZeroSig, new BitSet(1));
        this.values.put(GreaterSig, new BitSet(1));
        this.values.put(AluRes, new BitSet(32));
        this.values.put(RD2, new BitSet(32));
        this.values.put(WBAddr, new BitSet(5));

        for(HashMap.Entry<String, BitSet> set : values.entrySet()) {
            waitingValues.put(set.getKey(), new BitSet(set.getValue().length()));
        }

    }

    public void print() {
        System.out.println("RegisterEXMEM");
        for(HashMap.Entry<String, BitSet> set : values.entrySet()) {
            System.out.print("\t");
            System.out.println(set.getKey() + " : " + Integer.toString(BitUtility.BinToIntSigned(set.getValue()), 2));
        }
    }

}
