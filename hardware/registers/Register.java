package hardware.registers;

import hardware.interfaces.SyncComponent;
import ui.MainView;

import java.util.BitSet;
import java.util.HashMap;

public class Register implements SyncComponent {

    protected HashMap<String, BitSet> waitingValues;
    protected HashMap<String, BitSet> values;

    protected MainView mainView;

    protected String registerName;

    public Register(MainView mainView, String registerName) {
        waitingValues = new HashMap<String, BitSet>();
        values = new HashMap<String, BitSet>();

        this.mainView = mainView;
        this.registerName = registerName;
    }

    @Override
    public void clock() {
        for (HashMap.Entry<String,BitSet> set : waitingValues.entrySet()) {
            BitSet valueToUpdate = values.get(set.getKey());

            if (valueToUpdate != null) {
                valueToUpdate.clear();
                valueToUpdate.or(set.getValue());
            }
        }
        mainView.updateRegisterTable(registerName, values);
    }

    public BitSet get(String key) {
        BitSet value = values.get(key);
        if(value == null) {
            return new BitSet(32);
        }
        else return value;
    }

    public void store(String key, BitSet value) {
        BitSet valueToUpdate = waitingValues.get(key);
        valueToUpdate.clear();
        valueToUpdate.or(value);
    }

    public HashMap<String, BitSet> getRegisterData() {
        return values;
    }

    public void reinitRegister() {
        for(HashMap.Entry<String, BitSet> set : values.entrySet()) {
            values.get(set.getKey()).clear();
        }
        for(HashMap.Entry<String, BitSet> set : values.entrySet()) {
            waitingValues.get(set.getKey()).clear();
        }
    }

}
