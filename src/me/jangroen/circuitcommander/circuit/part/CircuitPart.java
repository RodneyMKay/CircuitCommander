package me.jangroen.circuitcommander.circuit.part;

public interface CircuitPart {
    boolean calculate();

    @Override
    String toString();

    String toCircuitString();

    boolean equals(CircuitPart object);
}
