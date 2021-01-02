package me.jangroen.circuitcommander.circuit.part.operation;

import me.jangroen.circuitcommander.circuit.parse.CircuitLanguage;
import me.jangroen.circuitcommander.circuit.part.CircuitPart;

import java.util.Arrays;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public abstract class CircuitPartOperation implements CircuitPart {
    private CircuitPart[] parts;

    public CircuitPartOperation(CircuitPart[] parts) {
        this.parts = parts;
    }

    public CircuitPart[] getParts() {
        return this.parts;
    }

    @Override
    public String toString() {
        String partString = Arrays.stream(parts).map(CircuitPart::toString).collect(Collectors.joining(","));
        return getCircuitOperation().name() + "(" + partString + ")";
    }

    @Override
    public String toCircuitString() {
        Collector<CharSequence, ?, String> collector = Collectors.joining(getCircuitOperation().symbol + "");
        String partString = Arrays.stream(parts).map(CircuitPart::toCircuitString).collect(collector);
        return CircuitLanguage.START_BRACKET + partString + CircuitLanguage.STOP_BRACKET;
    }

    @Override
    public boolean equals(CircuitPart object) {
        if (!(object instanceof CircuitPartOperation)) return false;
        if (((CircuitPartOperation) object).getCircuitOperation() != getCircuitOperation()) return false;
        for (CircuitPart partOther : ((CircuitPartOperation) object).parts) {
            boolean contains = false;
            for (CircuitPart part : parts) {
                if (partOther.equals(part)) {
                    contains = true;
                    break;
                }
            }
            if (!contains) return false;
        }
        return true;
    }

    public abstract CircuitOperation getCircuitOperation();
}
