package hardware;

import hardware.interfaces.AsyncComponent;
import hardware.utility.BitUtility;
import hardware.utility.ControlRepo;
import hardware.utility.InstructionsRepo;

import java.util.BitSet;

public class ControlUnit implements AsyncComponent {

    private BitSet Instr;
    public BitSet ControlSignals;

    public ControlUnit(BitSet Instr) {
        this.Instr = Instr;

        ControlSignals = new BitSet(11);
    }

    @Override
    public void compute() {

        ControlSignals.clear();
        byte instructionType = BitUtility.BinToByte(Instr.get(26,32));
        byte function = BitUtility.BinToByte(Instr.get(0,6));

        System.out.println("instruction type: " + instructionType);

        switch(instructionType) {
            case InstructionsRepo.R:
                ControlSignals.set(ControlRepo.RegWrite);
                ControlSignals.set(ControlRepo.RegDst);
                setFunctionBits(function);
                break;
            case InstructionsRepo.LW:
                ControlSignals.set(ControlRepo.MemToReg);
                ControlSignals.set(ControlRepo.RegWrite);
                ControlSignals.set(ControlRepo.AluSrc);
                setFunctionBits(InstructionsRepo.ADD);
                break;
            case InstructionsRepo.SW:
                ControlSignals.set(ControlRepo.MemWrite);
                ControlSignals.set(ControlRepo.AluSrc);
                setFunctionBits(InstructionsRepo.ADD);
                break;
            case InstructionsRepo.BEQ:
                ControlSignals.set(ControlRepo.Branch);
                setFunctionBits(InstructionsRepo.SUB);
                break;
            case InstructionsRepo.BGEZ:
                ControlSignals.set(ControlRepo.BranchGreater);
                setFunctionBits(InstructionsRepo.SUB);
                break;
            case InstructionsRepo.ADDI:
                ControlSignals.set(ControlRepo.RegWrite);
                ControlSignals.set(ControlRepo.AluSrc);
                setFunctionBits(InstructionsRepo.ADD);
                break;
            case InstructionsRepo.J:
                ControlSignals.set(ControlRepo.JUMP);
                setFunctionBits(InstructionsRepo.ADD);
                System.out.println("Changed signals for J");
                break;
        }
    }

    private void setFunctionBits(byte function) {
        long[] value = {function << ControlRepo.ALUOpLower};
        ControlSignals.or(BitSet.valueOf(value));
    }

}
