package me.jangroen.circuitcommander.circuit.variable;

import java.util.HashMap;

public enum Variable {
    A('a', 'A'),
    B('b', 'B'),
    C('c', 'C'),
    D('d', 'D'),
    E('e', 'E'),
    F('f', 'F'),
    G('g', 'G'),
    H('h', 'H'),
    I('i', 'I'),
    J('j', 'J'),
    K('k', 'K'),
    L('l', 'L'),
    M('m', 'M'),
    N('n', 'N'),
    O('o', 'O'),
    P('p', 'P'),
    Q('q', 'Q'),
    R('r', 'R'),
    S('s', 'S'),
    T('t', 'T'),
    U('u', 'U'),
    V('V'),
    W('w', 'W'),
    X('x', 'X'),
    Y('y', 'Y'),
    Z('z', 'Z'),

    Z1('1','1'),
    Z2('2','2'),
    Z3('3','3'),
    Z4('4','4'),
    Z5('5','5'),
    Z6('6','6'),
    Z7('7','7'),
    Z8('8','8'),
    Z9('9','9'),
    Z0('0','0');

    private static HashMap<Character, Variable> variableCharacters;

    private char[] chars;

    Variable(char... chars) {
        this.chars = chars;
    }

    public static Variable getVariable(char c) {
        return variableCharacters.getOrDefault(c, null);
    }

    static {
        variableCharacters = new HashMap<>();
        for(Variable v : values()) {
            for(char c : v.chars) {
                variableCharacters.put(c, v);
            }
        }
    }
}
