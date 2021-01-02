package me.jangroen.circuitcommander.circuit.part;

import me.jangroen.circuitcommander.circuit.variable.Variable;
import me.jangroen.circuitcommander.circuit.variable.VariableProvider;

public class CircuitPartVariable implements CircuitPart {
    private VariableProvider provider;
    private Variable variable;

    public CircuitPartVariable(VariableProvider provider, Variable variable) {
        this.provider = provider;
        this.variable = variable;
        provider.registerVariable(variable);
    }

    @Override
    public boolean calculate() {
        return provider.getVariable(variable);
    }

    @Override
    public String toString() {
        return "VAR(" + variable.name() + ")";
    }

    @Override
    public String toCircuitString() {
        return variable.name();
    }

    @Override
    public boolean equals(CircuitPart object) {
        return object instanceof CircuitPartVariable && ((CircuitPartVariable) object).variable == variable;
    }

    public Variable getVariable() {
        return variable;
    }
}
