package me.jangroen.circuitcommander.logiccircuit.entities;

import me.jangroen.circuitcommander.logiccircuit.LogicalCircuit;
import me.jangroen.circuitcommander.logiccircuit.parameter.PinSide;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.UUID;

public class LogicalConstant extends LogicalEntity {
    private boolean value;
    private PinSide pinSide;

    public LogicalConstant(LogicalCircuit parent, int x, int y, boolean value, PinSide pinSide) {
        super(UUID.randomUUID(), parent, x, y, UUID.randomUUID());
        this.value = value;
        this.pinSide = pinSide;
    }

    @Override
    public Element getEntity(Document document) {
        Element constant = document.createElement("lc:Constant");

        Element constantId = document.createElement("lc:ConstantId");
        constantId.appendChild(document.createTextNode(uuid.toString()));
        constant.appendChild(constantId);

        if(value) {
            Element value = document.createElement("lc:Value");
            value.appendChild(document.createTextNode("1"));
            constant.appendChild(value);
        }

        Element pinSide = document.createElement("lc:PinSide");
        pinSide.appendChild(document.createTextNode(this.pinSide.name));
        constant.appendChild(pinSide);

        return constant;
    }
}
