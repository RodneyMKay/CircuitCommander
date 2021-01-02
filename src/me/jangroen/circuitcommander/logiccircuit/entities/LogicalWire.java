package me.jangroen.circuitcommander.logiccircuit.entities;

import me.jangroen.circuitcommander.logiccircuit.LogicalCircuit;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.UUID;

public class LogicalWire extends LogicalEntity {
    private int dx;
    private int dy;

    public LogicalWire(LogicalCircuit parent, int x, int y, int dx, int dy) {
        super(UUID.randomUUID(), parent, x, y, null);
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public Element getEntity(Document document) {
        Element wire = document.createElement("lc:Wire");

        Element wireId = document.createElement("lc:WireId");
        wireId.appendChild(document.createTextNode(uuid.toString()));
        wire.appendChild(wireId);

        Element logicalCircuitId = document.createElement("lc:LogicalCircuitId");
        logicalCircuitId.appendChild(document.createTextNode(parent.getUuid().toString()));
        wire.appendChild(logicalCircuitId);

        if(x != 0) {
            Element x1 = document.createElement("lc:X1");
            x1.appendChild(document.createTextNode("" + x));
            wire.appendChild(x1);
        }

        if(y != 0) {
            Element y1 = document.createElement("lc:Y1");
            y1.appendChild(document.createTextNode("" + y));
            wire.appendChild(y1);
        }

        if(dx != 0) {
            Element x2 = document.createElement("lc:X2");
            x2.appendChild(document.createTextNode("" + dx));
            wire.appendChild(x2);
        }

        if(dy != 0) {
            Element y2 = document.createElement("lc:Y2");
            y2.appendChild(document.createTextNode("" + dy));
            wire.appendChild(y2);
        }

        return wire;
    }
}
