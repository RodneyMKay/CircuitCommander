package me.jangroen.circuitcommander.circuit.variable;

public class VariableNotAssignedException extends RuntimeException {
    public VariableNotAssignedException(Variable variable) {
        super(variable.name());
    }
}
