package com.blackberry.jwteditor;

import com.blackberry.jwteditor.model.config.HighlightColor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HighlightColorTest {
    @Test
    void testConstructionOfHighlightColorWithValidBurpColor() {
        HighlightColor highlightColor = HighlightColor.from("cyan");

        assertThat(highlightColor).isEqualTo(HighlightColor.CYAN);
    }

    @Test
    void testConstructionOfHighlightColorWithInvalidBurpColor() {
        HighlightColor highlightColor = HighlightColor.from("gunmetal");

        assertThat(highlightColor).isNull();
    }
}