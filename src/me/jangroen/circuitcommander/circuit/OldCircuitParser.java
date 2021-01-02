package me.jangroen.circuitcommander.circuit;

import me.jangroen.circuitcommander.circuit.parse.CircuitLanguage;
import me.jangroen.circuitcommander.circuit.parse.ParseException;
import me.jangroen.circuitcommander.circuit.part.*;
import me.jangroen.circuitcommander.circuit.part.operation.CircuitOperation;
import me.jangroen.circuitcommander.circuit.part.operation.CircuitPartOperation;
import me.jangroen.circuitcommander.circuit.variable.Variable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class OldCircuitParser {
    public static Circuit parse(String parseString) {
        parseString = parseString.replace(' ', '\n');
        parseString = parseString.replace("\n", "");
        OldCircuitParser oldCircuitParser = new OldCircuitParser(parseString);
        oldCircuitParser.circuit.circuitPart = oldCircuitParser.parsePart(0, oldCircuitParser.chars.length);
        return oldCircuitParser.circuit;
    }

    private Circuit circuit;
    private String parseString;
    private char[] chars;
    private Map<Integer, Map<Integer, CircuitOperation>> operators;
    private int currentLayer;

    private OldCircuitParser(String parseString) {
        // Assign Local variables
        this.circuit = new Circuit();
        this.parseString = parseString;
        this.chars = parseString.toCharArray();
        this.operators = new HashMap<>();
        this.currentLayer = 0;

        // Check for empty input
        if(chars.length == 0) throw new ParseException("Circuit cannot be empty!");

        // Loop through all characters
        for(int i = 0; i < chars.length; i++) {
            char c = chars[i];

            // Count brackets
            if(c == CircuitLanguage.START_BRACKET) currentLayer++;
            else if(c == CircuitLanguage.STOP_BRACKET) currentLayer--;

            // Search for operators and save them
            else {
                CircuitOperation operator = CircuitOperation.getOperator(c);
                if(operator != null) {
                    operators.computeIfAbsent(currentLayer, k -> new TreeMap<>()).put(i, operator);
                }
            }
        }

        // Check for valid bracket count
        if(currentLayer != 0) throw new ParseException("Input String has invalid backets: " + parseString);
    }

    private CircuitPart parseValueOrVariable(char c) {
        if(c == CircuitLanguage.TRUE_SYMBOL) return new CircuitPartValue(true);
        else if(c == CircuitLanguage.FALSE_SYMBOL) return new CircuitPartValue(false);
        else {
            Variable variable = Variable.getVariable(c);
            if(variable == null) throw new ParseException("Char cannot be identified as variable or value: " + c);
            return new CircuitPartVariable(circuit, variable);
        }
    }

    private CircuitPart parsePartWithoutOperation(int startIndex, int endIndex) {
        // Check for Invertion
        boolean inverted = chars[startIndex] == CircuitLanguage.NOT_OPERATOR;
        if(inverted) startIndex++;

        // Checking size
        int size = endIndex - startIndex;
        if(size == 0) throw new ParseException("Error while parsing part: " + parseString.substring(0, startIndex + 1) + "<-- Here");
        CircuitPart part;
        if(size == 1) {
            part = parseValueOrVariable(chars[startIndex]);
        } else if(size > 2) {
            if(chars[startIndex] == CircuitLanguage.START_BRACKET && chars[endIndex - 1] == CircuitLanguage.STOP_BRACKET) {
                startIndex++;
                endIndex--;
                currentLayer++;
                part = parsePart(startIndex, endIndex);
                currentLayer--;
            } else {
                throw new ParseException("Error while parsing part: " + parseString.substring(0, startIndex + 2) + "<-- Here");
            }
        } else {
            throw new ParseException("Error while parsing part: " + parseString.substring(0, startIndex + 2) + "<-- Here");
        }

        // Return and invert if needed
        if(inverted) return new CircuitPartInvert(part);
        return part;
    }

    private CircuitPart parsePart(int startIndex, int endIndex) {
        // Check for Operators in Layer
        Map<Integer, CircuitOperation> operatorsInLayer = operators.get(currentLayer);
        if(operatorsInLayer == null) {
            return parsePartWithoutOperation(startIndex, endIndex);
        } else {
            // Get iterator of operations matching this CircuitPart
            Iterator<Map.Entry<Integer, CircuitOperation>> entryIterator = operatorsInLayer.entrySet().stream().filter(e -> e.getKey() < endIndex && e.getKey() > startIndex).iterator();

            // Parse first part
            Map.Entry<Integer, CircuitOperation> entry = entryIterator.next();
            CircuitPart part = parsePartWithoutOperation(startIndex, entry.getKey());

            // Parse parts between first and last if existing
            while(entryIterator.hasNext()) {
                Map.Entry<Integer, CircuitOperation> newEntry = entryIterator.next();
                part = new CircuitPartOperation(entry.getValue(), part, parsePartWithoutOperation(entry.getKey() + 1, newEntry.getKey()));
                entry = newEntry;
            }

            // Parse last part
            part = new CircuitPartOperation(entry.getValue(), part, parsePartWithoutOperation(entry.getKey() + 1, endIndex));

            // Return everything
            return part;
        }
    }
}
