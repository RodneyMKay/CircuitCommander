package me.jangroen.circuitcommander.circuit.part;

import me.jangroen.circuitcommander.circuit.parse.CircuitLanguage;

public class CircuitPartValue implements CircuitPart {
    private boolean value;

    public CircuitPartValue(boolean value) {
        this.value = value;
    }

    @Override
    public boolean calculate() {
        return value;
    }

    @Override
    public String toString() {
        return "VAL(" + value + ")";
    }

    @Override
    public String toCircuitString() {
        return Character.toString(value ? CircuitLanguage.TRUE_SYMBOL : CircuitLanguage.FALSE_SYMBOL);
    }

    @Override
    public boolean equals(CircuitPart object) {
        return object instanceof CircuitPartValue && value == ((CircuitPartValue) object).value;
    }
}
