package hardware.EX;

import hardware.interfaces.AsyncComponent;
import hardware.utility.BitUtility;
import hardware.utility.ControlRepo;
import hardware.utility.InstructionsRepo;

import java.util.BitSet;

public class ALU implements AsyncComponent {
    private BitSet controlSignals;
    private BitSet RD1, RD2, immediate, shiftAmount;

    public BitSet ALUResult;
    public BitSet Zero;
    public BitSet Greater;

    public ALU(BitSet controlSignals, BitSet RD1,  BitSet RD2, BitSet immediate, BitSet shiftAmount) {
        this.controlSignals = controlSignals;
        this.RD1 = RD1;
        this.RD2 = RD2;
        this.immediate = immediate;
        this.shiftAmount = shiftAmount;

        ALUResult = new BitSet(32);
        Zero = new BitSet(1);
        Greater = new BitSet(1);
    }

    @Override
    public void compute() {

        // ------------ ALUSrc mux ---------------
        BitSet operand;
        if(controlSignals.get(ControlRepo.AluSrc) == false) {
            operand = RD2;
        }
        else {
            operand = immediate;
        }

        byte function = BitUtility.BinToByte(controlSignals.get(ControlRepo.ALUOpLower, ControlRepo.ALUOpUpper+1));

        ALUResult.clear();
        switch(function) {
            case InstructionsRepo.ADD:
                Integer addResult = BitUtility.BinToIntSigned(RD1) + BitUtility.BinToIntSigned(operand);
                ALUResult.or(BitUtility.IntToBin(addResult));
                break;
            case InstructionsRepo.SUB:
                Integer subResult = BitUtility.BinToIntSigned(RD1) - BitUtility.BinToIntSigned(operand);
                ALUResult.or(BitUtility.IntToBin(subResult));
                break;
            case InstructionsRepo.AND:
                ALUResult.or(RD1);
                ALUResult.and(operand);
                break;
            case InstructionsRepo.OR:
                ALUResult.or(RD1);
                ALUResult.or(operand);
                break;
            case InstructionsRepo.XOR:
                ALUResult.or(RD1);
                ALUResult.xor(operand);
                break;
            case InstructionsRepo.SLL:
                Integer toBeShiftedLeft = BitUtility.BinToInt(operand);
                toBeShiftedLeft = toBeShiftedLeft << BitUtility.BinToInt(shiftAmount);
                ALUResult.or(BitUtility.IntToBin(toBeShiftedLeft));
                break;
            case InstructionsRepo.SRL:
                Integer toBeShiftedRight = BitUtility.BinToInt(operand);
                toBeShiftedRight = toBeShiftedRight >> BitUtility.BinToInt(shiftAmount);
                ALUResult.or(BitUtility.IntToBin(toBeShiftedRight));
                break;
            case InstructionsRepo.SLT:
                if (BitUtility.BinToIntSigned(RD1) < BitUtility.BinToIntSigned(operand)) {
                    ALUResult.set(0);
                }
                break;
        }

        // setting greater and zero
        Zero.clear();
        Greater.clear();
        if(BitUtility.BinToIntSigned(RD1) == BitUtility.BinToIntSigned(operand)) {
            Zero.set(0);
        }
        else if(BitUtility.BinToIntSigned(RD1) > BitUtility.BinToIntSigned(operand)) {
            Greater.set(0);
        }
    }
}
