package com.blackberry.jwteditor;

import com.blackberry.jwteditor.model.config.HighlightColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HighlightColorTest {

    @Test
    void testConstructionOfHighlightColorWithValidBurpColor() {
        HighlightColor highlightColor = HighlightColor.from("cyan");

        assertEquals(highlightColor, HighlightColor.CYAN);
    }

    @Test
    void testConstructionOfHighlightColorWithInvalidBurpColor() {
        HighlightColor highlightColor = HighlightColor.from("gunmetal");

        assertNull(highlightColor);
    }
}