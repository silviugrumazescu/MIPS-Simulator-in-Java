package hardware.WB;

import hardware.interfaces.AsyncComponent;
import hardware.registers.RegisterMEMWB;
import hardware.utility.ControlRepo;

import java.util.BitSet;

public class WB implements AsyncComponent {

    private RegisterMEMWB registerMEMWB;
    public BitSet writeData;

    public WB(RegisterMEMWB registerMEMWB) {
        this.registerMEMWB = registerMEMWB;
        writeData = new BitSet(32);
        writeData.or(registerMEMWB.get(RegisterMEMWB.ALURes));
    }

    @Override
    public void compute() {
        writeData.clear();

        Boolean memToReg = registerMEMWB.get(RegisterMEMWB.ControlSignals).get(ControlRepo.MemToReg);

        if (memToReg == true) {
            writeData.or(registerMEMWB.get(RegisterMEMWB.ReadData));
        }
        else {
            writeData.or(registerMEMWB.get(RegisterMEMWB.ALURes));
        }
    }
}
