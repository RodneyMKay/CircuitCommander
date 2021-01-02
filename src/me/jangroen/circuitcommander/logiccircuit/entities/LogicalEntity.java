package me.jangroen.circuitcommander.logiccircuit.entities;

import me.jangroen.circuitcommander.logiccircuit.LogicalCircuit;
import me.jangroen.circuitcommander.logiccircuit.LogicalComponent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.UUID;

public abstract class LogicalEntity extends LogicalComponent {
    LogicalCircuit parent;
    int x;
    int y;
    private UUID circuitUUID;

    LogicalEntity(UUID uuid, LogicalCircuit parent, int x, int y, UUID circuitUUID) {
        super(uuid);
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.circuitUUID = circuitUUID;
    }

    public abstract Element getEntity(Document document);
    public Element getEntitySymbol(Document document) {
        if(circuitUUID == null) return null;

        Element symbol = document.createElement("lc:CircuitSymbol");

        Element circuitSymbolId = document.createElement("lc:CircuitSymbolId");
        circuitSymbolId.appendChild(document.createTextNode(circuitUUID.toString()));
        symbol.appendChild(circuitSymbolId);

        Element circuitId = document.createElement("lc:CircuitId");
        circuitId.appendChild(document.createTextNode(uuid.toString()));
        symbol.appendChild(circuitId);

        Element logicalCircuitId = document.createElement("lc:LogicalCircuitId");
        logicalCircuitId.appendChild(document.createTextNode(parent.getUuid().toString()));
        symbol.appendChild(logicalCircuitId);

        Element x = document.createElement("lc:X");
        x.appendChild(document.createTextNode("" + this.x));
        symbol.appendChild(x);

        Element y = document.createElement("lc:Y");
        y.appendChild(document.createTextNode("" + this.y));
        symbol.appendChild(y);

        return symbol;
    }
}
