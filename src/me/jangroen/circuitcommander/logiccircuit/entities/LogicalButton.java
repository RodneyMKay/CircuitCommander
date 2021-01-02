package me.jangroen.circuitcommander.logiccircuit.entities;

import me.jangroen.circuitcommander.logiccircuit.LogicalCircuit;
import me.jangroen.circuitcommander.logiccircuit.parameter.PinSide;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.UUID;

public class LogicalButton extends LogicalEntity {
    private String title;
    private PinSide pinSide;

    public LogicalButton(LogicalCircuit parent, int x, int y, String title, PinSide pinSide) {
        super(UUID.randomUUID(), parent, x, y, UUID.randomUUID());
        this.title = title;
        this.pinSide = pinSide;
    }

    @Override
    public Element getEntity(Document document) {
        Element button = document.createElement("lc:CircuitButton");

        Element circuitButtonId = document.createElement("lc:CircuitButtonId");
        circuitButtonId.appendChild(document.createTextNode(uuid.toString()));
        button.appendChild(circuitButtonId);

        Element notation = document.createElement("lc:Notation");
        notation.appendChild(document.createTextNode(this.title));
        button.appendChild(notation);

        Element pinSide = document.createElement("lc:PinSide");
        pinSide.appendChild(document.createTextNode(this.pinSide.toString()));
        button.appendChild(pinSide);

        Element isToggle = document.createElement("lc:IsToggle");
        isToggle.appendChild(document.createTextNode("True"));
        button.appendChild(isToggle);

        return button;
    }
}
