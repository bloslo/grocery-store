package com.grocerystop.onlinegrocerystore.action;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActionParserTest {

    @Test
    void parseSimpleAction() {
        String action = "2 + 2";
        BigDecimal result = ActionParser.fromString(action);

        assertEquals(BigDecimal.valueOf(4), result);
    }

    @Test
    void parseActionContainingBrackets() {
        String action = "( 1 + 2 ) * 3";
        BigDecimal result = ActionParser.fromString(action);

        assertEquals(BigDecimal.valueOf(9), result);
    }

    @Test
    void parseActionContainingDivision() {
        String action = "2.00 * ( 7 / 100 )";
        BigDecimal result = ActionParser.fromString(action);

        assertEquals(BigDecimal.valueOf(0.14), result.setScale(2));
    }

    @Test
    void parseActionContainingOnlyNumber() {
        String action = "1";
        BigDecimal result = ActionParser.fromString(action);

        assertEquals(BigDecimal.valueOf(1), result);
    }
}
