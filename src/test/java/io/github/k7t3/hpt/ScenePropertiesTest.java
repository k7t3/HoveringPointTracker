package io.github.k7t3.hpt;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScenePropertiesTest {

    private SceneProperties properties;

    @BeforeEach
    void setUp() {
        properties = new SceneProperties();
    }

    @Test
    void testClone() {
        System.out.println("testClone");
        var width = 200d;
        var color = Color.YELLOW;

        properties.setWidth(width);
        properties.setPaintColor(color);

        var copied = properties.clone();

        assertEquals(width, copied.getWidth());
        assertEquals(color, copied.getPaintColor());

        copied.setWidth(100);
        assertNotEquals(width, copied.getWidth());

        System.out.println("done testClone");
    }
}