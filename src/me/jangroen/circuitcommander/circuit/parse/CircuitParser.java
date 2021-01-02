package me.jangroen.circuitcommander.circuit.parse;

import me.jangroen.circuitcommander.circuit.Circuit;
import me.jangroen.circuitcommander.circuit.part.CircuitPart;
import me.jangroen.circuitcommander.circuit.part.CircuitPartInvert;
import me.jangroen.circuitcommander.circuit.part.CircuitPartValue;
import me.jangroen.circuitcommander.circuit.part.CircuitPartVariable;
import me.jangroen.circuitcommander.circuit.part.operation.CircuitOperation;
import me.jangroen.circuitcommander.circuit.variable.Variable;
import me.jangroen.circuitcommander.circuit.variable.VariableProvider;

import java.util.*;

public class CircuitParser {
    /**
     * Parses a String of circuit language into a {@link Circuit} object.
     *
     * Examples:
     * - A^B
     * - !(A^B)vC
     *
     * @param parseString String to parse
     * @return Parsed object
     * @throws ParseException When a malformed parse string is given
     */
    public static Circuit parse(String parseString) throws ParseException {
        CircuitParser parser = new CircuitParser(parseString);
        return parser.parse();
    }

    /**
     * Stores all the characters of the String to parse.
     */
    private char[] chars;

    /**
     * Stores a {@link OperationMap} per bracket layer.
     * Is filled by the constructor.
     */
    private Map<Integer, OperationMap> operationMaps;

    /**
     * VariableProvider in which all Variables that are registered.
     */
    private VariableProvider variableProvider;

    private CircuitParser(String parseString) throws ParseException {
        this.chars = parseString.toCharArray();
        this.operationMaps = new HashMap<>();
        this.variableProvider = new VariableProvider();

        // Prepares a maps of all operation characters associated to their bracket depth
        int level = 0;
        for (int i = 0; i < this.chars.length; i++) {
            char c = this.chars[i];
            if (c == CircuitLanguage.START_BRACKET) {
                level++;
            } else if (c == CircuitLanguage.STOP_BRACKET) {
                level--;
            } else {
                for (CircuitOperation operation : CircuitOperation.values()) {
                    if (c == operation.symbol) {
                        if (this.operationMaps.containsKey(level)) {
                            this.operationMaps.get(level).put(i, operation);
                        } else {
                            OperationMap map = new OperationMap();
                            map.put(i, operation);
                            this.operationMaps.put(level, map);
                        }
                        break;
                    }
                }
            }
        }

        if (level > 0) throwParseException("Missing closing bracket", chars.length);
        if (level < 0) throwParseException("Missing opening bracket", 0);
    }

    private Circuit parse() throws ParseException {
        CircuitPart part = parsePart(0, 0, this.chars.length);
        return new Circuit(part, variableProvider);
    }

    private CircuitPart parsePart(int bracketLevel, int from, int to) throws ParseException {
        if (this.operationMaps.get(bracketLevel) == null) {
            // (x)
            if (this.chars[from] == CircuitLanguage.START_BRACKET) {
                if (this.chars[to] != CircuitLanguage.STOP_BRACKET) {
                    throwParseException("Expected closing bracket", to - 1);
                }
                return parsePart(bracketLevel + 1, from + 1, to - 1);
            // !(x)
            } else if (this.chars[from + 1] != CircuitLanguage.START_BRACKET) {
                if (this.chars[from] != CircuitLanguage.NOT_OPERATOR) {
                    throwParseException("Char expected to be a not operatior", from);
                }

                if (this.chars[to] != CircuitLanguage.STOP_BRACKET) {
                    throwParseException("Expected closing bracket", to - 1);
                }

                return new CircuitPartInvert(parsePart(bracketLevel + 1, from + 2, to - 1));
            } else {
                // x
                if (to - from == 1) {
                    return parseStaticPart(from, false);
                // !x
                } else if (to - from == 2) {
                    if (this.chars[from] == CircuitLanguage.NOT_OPERATOR) {
                        throwParseException("Char expected to be a not operatior", from);
                    }

                    return new CircuitPartInvert(parseStaticPart(from + 1, true));
                } else {
                    throwParseException("", to - 1);
                }
            }
        // x ^ y ^ z
        } else {
            return parseOperation(bracketLevel, from, to);
        }

        throw new Error("Return statement was skipped or throwParseException didn't throw a parse exception!");
    }

    // 1 || !x
    private CircuitPart parseStaticPart(int position, boolean invert) throws ParseException {
        CircuitPart part;

        if (this.chars[position] == CircuitLanguage.TRUE_SYMBOL) {
            part = new CircuitPartValue(true);
        } else if (this.chars[position] == CircuitLanguage.FALSE_SYMBOL) {
            part = new CircuitPartValue(false);
        } else {
            Variable variable = Variable.getVariable(this.chars[position]);

            if (variable == null) {
                throwParseException("Invalid variable name", position);
            }

            part = new CircuitPartVariable(variableProvider, variable);
        }

        if (invert) {
            return new CircuitPartInvert(part);
        }

        return part;
    }

    // x ^ y ^ z
    private CircuitPart parseOperation(int bracketLevel, int from, int to) {
        CircuitOperation operation;
        List<ParseArea> areas = new ArrayList<>();
        Map.Entry<Integer, CircuitOperation> operationMapEntry;
        Iterator<Map.Entry<Integer, CircuitOperation>> operationsIterator;
        operationsIterator = this.operationMaps.get(bracketLevel).entrySet().iterator();

        operation = operationsIterator.next().getValue();

        while (operationsIterator.hasNext()) {
            operationMapEntry = operationsIterator.next();

            if (operationMapEntry.getValue() == operation) {
                areas.add(new ParseArea(from, to));
            }
        }
    }

    private static class OperationMap extends HashMap<Integer, CircuitOperation> {
        private CircuitOperation operation;

        private OperationMap() {
            super();
            // TODO: Priorities
        }

        @Override
        public CircuitOperation put(Integer key, CircuitOperation value) {
            if (this.operation.hasHigherPriority(value)) {
                this.operation = value;
            }

            return super.put(key, value);
        }

        public CircuitOperation getOperation() {
            return operation;
        }
    }

    private static class ParseArea {
        private final int from;
        private final int to;

        private ParseArea(int from, int to) {
            this.from = from;
            this.to = to;
        }
    }

    private void throwParseException(String message, int position) throws ParseException {
        // TODO: Make proper parse error message
        message += ' ' + position;
        throw new ParseException(message);
    }
}
