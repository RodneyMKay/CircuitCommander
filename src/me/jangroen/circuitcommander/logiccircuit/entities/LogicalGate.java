package me.jangroen.circuitcommander.logiccircuit.entities;

import me.jangroen.circuitcommander.logiccircuit.LogicalCircuit;
import me.jangroen.circuitcommander.logiccircuit.parameter.GateType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.UUID;

public class LogicalGate extends LogicalEntity {
    public LogicalGate(LogicalCircuit parent, int x, int y, GateType gateType) {
        super(UUID.fromString(gateType.getUuid()), parent, x, y, UUID.randomUUID());
    }

    @Override
    public Element getEntity(Document document) {
        return null;
    }
}
