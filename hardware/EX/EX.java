package hardware.EX;

import hardware.interfaces.AsyncComponent;
import hardware.registers.RegisterEXMEM;
import hardware.registers.RegisterIDEX;
import hardware.utility.BitUtility;
import hardware.utility.ControlRepo;

import java.util.BitSet;

public class EX implements AsyncComponent {

    RegisterIDEX registerIDEX;
    RegisterEXMEM registerEXMEM;

    ALU alu;

    public EX(RegisterIDEX registerIDEX, RegisterEXMEM registerEXMEM) {
        this.registerIDEX = registerIDEX;
        this.registerEXMEM = registerEXMEM;

        alu = new ALU(registerIDEX.get(RegisterIDEX.ControlSignals),
                     registerIDEX.get(RegisterIDEX.RD1),
                     registerIDEX.get(RegisterIDEX.RD2),
                     registerIDEX.get(RegisterIDEX.Immediate),
                     registerIDEX.get(RegisterIDEX.SA));
    }

    @Override
    public void compute() {

        alu.compute();

        // Control signals
        registerEXMEM.store(RegisterEXMEM.ControlSignals, registerIDEX.get(RegisterIDEX.ControlSignals));

        //branch address
        BitSet branchAddress = BitUtility.AddBitSet(registerIDEX.get(RegisterIDEX.PCp1), registerIDEX.get(RegisterIDEX.Immediate)); // (PC + 1) + Immediate
        registerEXMEM.store(RegisterEXMEM.BranchAddress, branchAddress);

        // ALU
        registerEXMEM.store(RegisterEXMEM.ZeroSig, alu.Zero);
        registerEXMEM.store(RegisterEXMEM.GreaterSig, alu.Greater);
        registerEXMEM.store(RegisterEXMEM.AluRes, alu.ALUResult);
        //RD2
        registerEXMEM.store(RegisterEXMEM.RD2, registerIDEX.get(RegisterIDEX.RD2));

        // Write back mux
        BitSet WBAddr;
        if(registerIDEX.get(RegisterIDEX.ControlSignals).get(ControlRepo.RegDst) == true) {
            WBAddr = registerIDEX.get(RegisterIDEX.RD);
        }
        else {
            WBAddr = registerIDEX.get(RegisterIDEX.RT);
        }
        registerEXMEM.store(RegisterEXMEM.WBAddr, WBAddr);
    }
}
