package me.jangroen.circuitcommander.circuit.variable;

import me.jangroen.circuitcommander.circuit.parse.CircuitLanguage;

import java.util.*;

public class VariableProvider {
    private static final boolean DEFAULT = false;
    private Map<Variable, Boolean> values = new TreeMap<>();

    public void setVariable(Variable variable, boolean value) {
        values.put(variable, value);
    }

    public void registerVariable(Variable... variable) {
        for(Variable v : variable) values.put(v, DEFAULT);
    }

    public boolean getVariable(Variable variable) {
        if(values.containsKey(variable)) return values.get(variable);
        throw new VariableNotAssignedException(variable);
    }

    public List<Variable> getVariables() {
        return new ArrayList<>(values.keySet());
    }

    public Map<Variable, Boolean> getValues() {
        return values;
    }

    public boolean countBinary() {
        for(Map.Entry<Variable, Boolean> entry : values.entrySet()) {
            if(entry.getValue()) {
                values.replace(entry.getKey(), false);
            } else {
                values.replace(entry.getKey(), true);
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        final String[] ret = {""};
        values.forEach((v, b) -> ret[0] += ',' + v.name() + '=' + (b ? CircuitLanguage.TRUE_SYMBOL : CircuitLanguage.FALSE_SYMBOL));
        return ret[0].substring(1);
    }
}
