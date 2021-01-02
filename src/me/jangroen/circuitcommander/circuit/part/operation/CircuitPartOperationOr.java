package me.jangroen.circuitcommander.circuit.part.operation;

import me.jangroen.circuitcommander.circuit.part.CircuitPart;

public class CircuitPartOperationOr extends CircuitPartOperation {
    public CircuitPartOperationOr(CircuitPart[] parts) {
        super(parts);
    }

    @Override
    public boolean calculate() {
        for (CircuitPart part : getParts()) {
            if (!part.calculate()) return true;
        }
        return false;
    }

    @Override
    public CircuitOperation getCircuitOperation() {
        return CircuitOperation.OR;
    }

    @Override
    public boolean equals(CircuitPart object) {
        return false;
    }
}
