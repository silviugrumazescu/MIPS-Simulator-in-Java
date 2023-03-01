package hardware.ID;

import hardware.interfaces.AsyncComponent;
import hardware.interfaces.SyncComponent;
import hardware.registers.RegisterIDEX;
import hardware.registers.RegisterIFID;
import hardware.registers.RegisterMEMWB;
import ui.MainView;

import java.util.BitSet;

public class ID implements AsyncComponent, SyncComponent {

    RegisterIFID registerIFID;
    RegisterMEMWB registerMEMWB;
    RegisterIDEX registerIDEX;
    BitSet writeData;
    BitSet mainControlSignals;

    RegisterFile registerFile;

    public ID(RegisterIFID registerIFID, RegisterMEMWB registerMEMWB, RegisterIDEX registerIDEX, BitSet writeData, BitSet mainControlSignals) {
        this.registerIFID = registerIFID;
        this.registerMEMWB = registerMEMWB;
        this.registerIDEX = registerIDEX;
        this.writeData = writeData;
        this.mainControlSignals = mainControlSignals;

        BitSet instr = registerIFID.get(RegisterIFID.INSTRUCTION);
        BitSet writeAddress = registerMEMWB.get(RegisterMEMWB.WBAddr);
        BitSet controlSignals = registerMEMWB.get(RegisterMEMWB.ControlSignals);

        registerFile = new RegisterFile(instr, writeAddress, writeData, controlSignals);
    }

    @Override
    public void compute() {
        registerFile.compute();

        registerIDEX.store(RegisterIDEX.ControlSignals, mainControlSignals);
        registerIDEX.store(RegisterIDEX.PCp1, registerIFID.get(RegisterIFID.PCp1));

        registerIDEX.store(RegisterIDEX.RD1, registerFile.RD1);
        registerIDEX.store(RegisterIDEX.RD2, registerFile.RD2);

        BitSet instr = registerIFID.get(RegisterIFID.INSTRUCTION);

        registerIDEX.store(RegisterIDEX.SA, instr.get(6, 11));

        registerIDEX.store(RegisterIDEX.Function, instr.get(0,6));
        registerIDEX.store(RegisterIDEX.RT, instr.get(16,21));
        registerIDEX.store(RegisterIDEX.RD, instr.get(11,16));

        // sign extend Immediate
        BitSet immediate = new BitSet(32);
        for(int i = 0; i <= 15; i++) {
            immediate.set(i, instr.get(i));
        }
        if (immediate.get(15)) { // if most significant bit set, the number is negative
            immediate.set(16,32);
        }
        registerIDEX.store(RegisterIDEX.Immediate, immediate);
    }

    @Override
    public void clock() {
        registerFile.clock();
    }

    public void printRegFile() {
        registerFile.print();
    }

    public BitSet[] getRegisterMemoryData() {
        return registerFile.registers;
    }

    public void setRegister(Integer index, BitSet value) {
        registerFile.registers[index].clear();
        for(int j = 0; j < 32; j++) {
            registerFile.registers[index].set(j, value.get(j));
        }
    }
}
