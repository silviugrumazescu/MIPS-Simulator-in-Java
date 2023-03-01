package hardware.MEM;

import hardware.interfaces.AsyncComponent;
import hardware.interfaces.SyncComponent;
import hardware.utility.BitUtility;
import hardware.utility.ControlRepo;
import ui.MainView;

import java.util.BitSet;

public class DataMemory implements AsyncComponent, SyncComponent {

    private MainView mainView;

    public BitSet[] memory;
    private BitSet address, writeData;
    private BitSet controlSignals;

    public BitSet readData;

    public DataMemory(BitSet address, BitSet writeData, BitSet controlSignals, MainView mainView) {
        this.address = address;
        this.writeData = writeData;
        this.controlSignals = controlSignals;

        this.mainView = mainView;

        memory = new BitSet[1024];
        for(int i = 0; i < 1024; i++) {
            memory[i] = new BitSet(32);
        }

        memory[0] = BitUtility.IntToBin(69);

        readData = new BitSet(32);
    }

    @Override
    public void compute() {
        try{
            readData = memory[BitUtility.BinToInt(address)];
        }
        catch(ArrayIndexOutOfBoundsException ex) {
        }
    }

    @Override
    public void clock() {
        if(controlSignals.get(ControlRepo.MemWrite) == true) {
            BitSet toWrite = memory[BitUtility.BinToInt(address)];
            toWrite.clear();
            toWrite.or(writeData);
        }

        mainView.updateMemoryTable(memory);

    }

    public void print() {
        System.out.println("DataMemory: ");
        for(int i = 0; i < 10; i++) {
            System.out.print("\t");
            System.out.println("ADDR" + Integer.toString(i) + ": " + Integer.toString(BitUtility.BinToInt(memory[i])));
        }
    }

}
