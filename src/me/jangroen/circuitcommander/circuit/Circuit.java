package me.jangroen.circuitcommander.circuit;

import me.jangroen.circuitcommander.circuit.part.CircuitPart;
import me.jangroen.circuitcommander.circuit.variable.VariableProvider;

public class Circuit {
    private CircuitPart circuitPart;
    private VariableProvider variableProvider;

    public Circuit(CircuitPart circuitPart, VariableProvider variableProvider) {
        this.circuitPart = circuitPart;
        this.variableProvider = variableProvider;
    }

    public VariableProvider getVariableProvider() {
        return variableProvider;
    }

    public void setVariableProvider(VariableProvider variableProvider) {
        this.variableProvider = variableProvider;
    }

    public boolean calculate() {
        return circuitPart.calculate();
    }

    @Override
    public String toString() {
        return circuitPart.toString();
    }

    public String toCircuitString() {
        return circuitPart.toCircuitString();
    }

    public CircuitPart getMainPart() {
        return circuitPart;
    }
}
