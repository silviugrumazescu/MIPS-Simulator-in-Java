package hardware.MEM;

import hardware.interfaces.AsyncComponent;
import hardware.interfaces.SyncComponent;
import hardware.registers.RegisterEXMEM;
import hardware.registers.RegisterMEMWB;
import hardware.utility.ControlRepo;
import ui.MainView;

import java.util.BitSet;

public class MEM implements AsyncComponent, SyncComponent {

    private RegisterEXMEM registerEXMEM;
    private RegisterMEMWB registerMEMWB;

    private DataMemory dataMemory;

    public BitSet PCSrc;

    public MEM(RegisterEXMEM registerEXMEM, RegisterMEMWB registerMEMWB, MainView mainView) {
        this.registerEXMEM = registerEXMEM;
        this.registerMEMWB = registerMEMWB;

        dataMemory = new DataMemory(registerEXMEM.get(RegisterEXMEM.AluRes),
                                    registerEXMEM.get(RegisterEXMEM.RD2),
                                    registerEXMEM.get(RegisterEXMEM.ControlSignals),
                                    mainView);

        PCSrc = new BitSet(1);
    }

    @Override
    public void compute() {
        // Data memory
        dataMemory.compute();

        // Calculating PCSrc
        Boolean branch = registerEXMEM.get(RegisterEXMEM.ControlSignals).get(ControlRepo.Branch);
        Boolean zero = registerEXMEM.get(RegisterEXMEM.ZeroSig).get(0);
        Boolean branchGreater = registerEXMEM.get(RegisterEXMEM.ControlSignals).get(ControlRepo.BranchGreater);
        Boolean greater = registerEXMEM.get(RegisterEXMEM.GreaterSig).get(0);

        PCSrc.clear();
        if((branch == true && zero == true) || (branchGreater == true && greater)) {
            PCSrc.set(0);
        }

        // Queueing values in Register
        registerMEMWB.store(RegisterMEMWB.ControlSignals, registerEXMEM.get(RegisterEXMEM.ControlSignals));
        registerMEMWB.store(RegisterMEMWB.ReadData, dataMemory.readData);
        registerMEMWB.store(RegisterMEMWB.ALURes, registerEXMEM.get(RegisterEXMEM.AluRes));
        registerMEMWB.store(RegisterMEMWB.WBAddr, registerEXMEM.get(RegisterEXMEM.WBAddr));
    }

    @Override
    public void clock() {
        dataMemory.clock();
    }

    public void printMemory() {
        dataMemory.print();
    }

    public BitSet[] getMemoryData() {
        return dataMemory.memory;
    }

    public void setMemory(Integer index, BitSet value) {
        dataMemory.memory[index].clear();
        for(int j = 0; j < 32; j++) {
            dataMemory.memory[index].set(j, value.get(j));
        }
    }
}
