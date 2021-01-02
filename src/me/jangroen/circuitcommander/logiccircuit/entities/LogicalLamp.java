package me.jangroen.circuitcommander.logiccircuit.entities;

import me.jangroen.circuitcommander.logiccircuit.LogicalCircuit;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.UUID;

public class LogicalLamp extends LogicalEntity {
    private static final String lampUuid = "00000000-0000-0000-0000-000000080100";

    public LogicalLamp(LogicalCircuit parent, int x, int y) {
        super(UUID.fromString(lampUuid), parent, x, y, UUID.randomUUID());
    }

    @Override
    public Element getEntity(Document document) {
        return null;
    }
}
