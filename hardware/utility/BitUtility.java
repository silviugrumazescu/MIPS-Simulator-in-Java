package hardware.utility;

import java.util.BitSet;

public class BitUtility {

    public static Integer BinToInt(BitSet value) {
        BitSet trimmed = ((BitSet)value.clone()).get(0, 31);
        long[] arr = trimmed.toLongArray();
        if (arr.length == 0) {
            return 0;
        }
        else {
            return Math.toIntExact(arr[0]);
        }
    }

    public static Integer BinToIntSigned(BitSet value) {
        Integer number;
        BitSet toCompute = ((BitSet)value.clone()).get(0, 32);
        if(toCompute.get(31) == true) {
            // negative number
            toCompute.flip(0, 32);
            long[] arr = toCompute.toLongArray();
            if (arr.length == 0) {
                number = -1;   // daca dam flip si arr e 0 inseamna ca avem reprezentarea lui -1
            }
            else {
                number = Math.toIntExact(arr[0]);
                number += 1; // Fiind in complement fata de 2
                number = number * (-1);
            }
        }
        else {
            number = BinToInt(toCompute);
        }
        return number;
    }

    public static Byte BinToByte(BitSet value) {
        byte[] arr = value.toByteArray();
        if(arr.length == 0) {
            return 0;
        }
        else {
            return arr[0];
        }
    }

    public static BitSet IntToBin(Integer value) {
        long[] longValue = {Long.valueOf(value)};
        return BitSet.valueOf(longValue).get(0,32);
    }

    public static BitSet AddBitSet(BitSet op1, BitSet op2) {
        long[] value = {BinToIntSigned(op1) + BinToIntSigned(op2)};
        return BitSet.valueOf(value).get(0, 32);
    }

    public static BitSet StringToBin(String number) {
        BitSet result = new BitSet(32);
        for(int i = 0; i < number.length(); i++) {
            if(number.charAt(i) == '1') {
                result.set(number.length() - 1 - i);
            }
        }
        return result;
    }


}
