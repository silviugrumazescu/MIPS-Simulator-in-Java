package hardware.ID;

import hardware.interfaces.AsyncComponent;
import hardware.interfaces.SyncComponent;
import hardware.utility.BitUtility;
import hardware.utility.ControlRepo;
import ui.MainView;

import java.util.BitSet;

public class RegisterFile implements SyncComponent, AsyncComponent {

    public BitSet[] registers;

    private BitSet instr, writeAddress, writeData, controlSignals;

    public BitSet RD1, RD2;

    public RegisterFile(BitSet instr, BitSet writeAddress, BitSet writeData, BitSet controlSignals) {
        this.instr = instr;
        this.writeAddress = writeAddress;
        this.writeData = writeData;
        this.controlSignals = controlSignals;

        registers = new BitSet[32];
        for(int i = 0 ; i < 32; i++) {
            registers[i] = new BitSet(32);
        }
        registers[1] = BitUtility.IntToBin(21);

        RD1 = registers[0];
        RD2 = registers[0];
    }

    @Override
    public void compute() {
        Integer RA1 = BitUtility.BinToInt(instr.get(21,26));
        Integer RA2 = BitUtility.BinToInt(instr.get(16,21));

        RD1 = registers[RA1];
        RD2 = registers[RA2];
    }
    @Override
    public void clock() {
        Boolean RegWrite = controlSignals.get(ControlRepo.RegWrite);
        if(RegWrite == true) {
            Integer address = BitUtility.BinToInt(writeAddress);
            registers[address].clear();
            registers[address].or(writeData);
        }
    }

    public void print() {
        System.out.println("RegFile: ");
        for(int i = 0; i < 10; i++) {
            System.out.print("\t");
            System.out.println("RD" + Integer.toString(i) + ": " + Integer.toString(BitUtility.BinToIntSigned(registers[i])));
        }
    }

}
