package me.jangroen.circuitcommander.circuit.part.operation;

/**
 * Describes the operations that can be used in a
 * {@link me.jangroen.circuitcommander.circuit.part.operation.CircuitPartOperation}.
 */
public enum CircuitOperation {
    /**
     * Binary AND operation represented by '^'.
     *
     * Examples:
     * - A^B
     * - C^!D
     */
    AND('^', 20),

    /**
     * Binary OR operation represented by 'v'.
     *
     * Examples:
     * - AvB
     * - Cv!D
     */
    OR('v', 10);

    /**
     * Symbol used in the circuit language.
     *
     * @see me.jangroen.circuitcommander.circuit.parse.CircuitParser
     */
    public final char symbol;

    /**
     * Priority of the operation.
     */
    public final int priority;

    CircuitOperation(char symbol, int priority) {
        this.symbol = symbol;
        this.priority = priority;
    }

    /**
     * Fetches the CircuitOperation matching to a character
     * used in the circuit language.
     *
     * @param c symbol character
     * @return the operation corresponding to c
     *
     * @see me.jangroen.circuitcommander.circuit.parse.CircuitParser
     */
    public static CircuitOperation getOperator(char c) {
        if (c == AND.symbol) return AND;
        else if (c == OR.symbol) return OR;
        return null;
    }

    /**
     * Checks if the specified operation has a higher priority than
     * the current object.
     *
     * @param operation the object that should be compared
     * @return if the comparison returned <code>true</code>
     */
    public boolean hasHigherPriority(CircuitOperation operation) {
        return operation.priority > priority;
    }
}
