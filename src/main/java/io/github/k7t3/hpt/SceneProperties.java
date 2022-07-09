package io.github.k7t3.hpt;

import javafx.beans.property.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;

public class SceneProperties {

    private static final Color DEFAULT_PAINT_COLOR = Color.color(0d, 0d, 0d, 0.2);

    private static final Color DEFAULT_EDGE_COLOR = ((Color) DEFAULT_PAINT_COLOR).deriveColor(
            1.0, 1.0, 1.0, 1.2);

    private static final Color DEFAULT_GRID_FILL = Color.SNOW;

    private static final Color DEFAULT_SCENE_BORDER_FILL = Color.TRANSPARENT;

    private static final Color DEFAULT_LABEL_FILL = DEFAULT_PAINT_COLOR.deriveColor(0, 1, 1, 2.4);

    private static final Color DEFAULT_CLICK_POINT_FILL = Color.SNOW;

    private static final double DEFAULT_WIDTH = 300;

    private static final double DEFAULT_HEIGHT = 300;

    private static final double DEFAULT_EDGE_WIDTH = 10;

    private static final Font DEFAULT_LABEL_FONT = Font.font("monospaced", FontWeight.BOLD, 13);

    private ObjectProperty<Screen> screen;

    private DoubleProperty minX;

    private DoubleProperty minY;

    private DoubleProperty width;

    private DoubleProperty height;

    private DoubleProperty edgeWidth;

    private ObjectProperty<Color> paintColor;

    private ObjectProperty<Color> gridColor;

    private ObjectProperty<Color> edgeColor;

    private ObjectProperty<Color> labelColor;

    private ObjectProperty<Color> clickPointColor;

    private ObjectProperty<Color> sceneBorderColor;

    private ReadOnlyDoubleWrapper currentX;

    private ReadOnlyDoubleWrapper currentY;

    private ObjectProperty<Font> labelFont;

    public Screen getScreen() {
        return screenProperty().get();
    }

    public ObjectProperty<Screen> screenProperty() {
        if (screen == null) {
            screen = new SimpleObjectProperty<>();
        }
        return screen;
    }

    public void setScreen(Screen screen) {
        this.screenProperty().set(screen);
    }

    public double getMinX() {
        return minXProperty().get();
    }

    public DoubleProperty minXProperty() {
        if (minX == null) {
            minX = new SimpleDoubleProperty(200);
        }
        return minX;
    }

    public void setMinX(double minX) {
        this.minXProperty().set(minX);
    }

    public double getMinY() {
        return minYProperty().get();
    }

    public DoubleProperty minYProperty() {
        if (minY == null) {
            minY = new SimpleDoubleProperty(200);
        }
        return minY;
    }

    public void setMinY(double minY) {
        this.minYProperty().set(minY);
    }

    public double getWidth() {
        return widthProperty().get();
    }

    public DoubleProperty widthProperty() {
        if (width == null) {
            width = new SimpleDoubleProperty(DEFAULT_WIDTH);
        }
        return width;
    }

    public void setWidth(double width) {
        this.widthProperty().set(width);
    }

    public double getHeight() {
        return heightProperty().get();
    }

    public DoubleProperty heightProperty() {
        if (height == null) {
            height = new SimpleDoubleProperty(DEFAULT_HEIGHT);
        }
        return height;
    }

    public void setHeight(double height) {
        this.heightProperty().set(height);
    }

    public double getEdgeWidth() {
        return edgeWidthProperty().get();
    }

    public DoubleProperty edgeWidthProperty() {
        if (edgeWidth == null) {
            edgeWidth = new SimpleDoubleProperty(DEFAULT_EDGE_WIDTH);
        }
        return edgeWidth;
    }

    public void setEdgeWidth(double edgeWidth) {
        this.edgeWidthProperty().set(edgeWidth);
    }

    public Color getPaintColor() {
        return paintColorProperty().get();
    }

    public ObjectProperty<Color> paintColorProperty() {
        if (paintColor == null) {
            paintColor = new SimpleObjectProperty<>(DEFAULT_PAINT_COLOR);
        }
        return paintColor;
    }

    public void setPaintColor(Color paintColor) {
        this.paintColorProperty().set(paintColor);
    }

    public Color getGridColor() {
        return gridColorProperty().get();
    }

    public ObjectProperty<Color> gridColorProperty() {
        if (gridColor == null) {
            gridColor = new SimpleObjectProperty<>(DEFAULT_GRID_FILL);
        }
        return gridColor;
    }

    public void setGridColor(Color gridColor) {
        this.gridColorProperty().set(gridColor);
    }

    public Color getSceneBorderColor() {
        return sceneBorderColorProperty().get();
    }

    public ObjectProperty<Color> sceneBorderColorProperty() {
        if (sceneBorderColor == null) {
            sceneBorderColor = new SimpleObjectProperty<>(DEFAULT_SCENE_BORDER_FILL);
        }
        return sceneBorderColor;
    }

    public void setSceneBorderColor(Color sceneBorderColor) {
        this.sceneBorderColorProperty().set(sceneBorderColor);
    }

    public Color getLabelColor() {
        return labelColorProperty().get();
    }

    public ObjectProperty<Color> labelColorProperty() {
        if (labelColor == null) {
            labelColor = new SimpleObjectProperty<>(DEFAULT_LABEL_FILL);
        }
        return labelColor;
    }

    public void setLabelColor(Color labelColor) {
        this.labelColorProperty().set(labelColor);
    }

    public Color getClickPointColor() {
        return clickPointColorProperty().get();
    }

    public ObjectProperty<Color> clickPointColorProperty() {
        if (clickPointColor == null) {
            clickPointColor = new SimpleObjectProperty<>(DEFAULT_CLICK_POINT_FILL);
        }
        return clickPointColor;
    }

    public void setClickPointColor(Color clickPointColor) {
        this.clickPointColorProperty().set(clickPointColor);
    }

    public double getCurrentX() {
        return currentXProperty().get();
    }

    ReadOnlyDoubleWrapper currentXPropertyWrapper() {
        if (currentX == null) {
            currentX = new ReadOnlyDoubleWrapper();
        }
        return currentX;
    }

    public ReadOnlyDoubleProperty currentXProperty() {
        return currentXPropertyWrapper().getReadOnlyProperty();
    }

    public double getCurrentY() {
        return currentYProperty().get();
    }

    ReadOnlyDoubleWrapper currentYPropertyWrapper() {
        if (currentY == null) {
            currentY = new ReadOnlyDoubleWrapper();
        }
        return currentY;
    }

    public ReadOnlyDoubleProperty currentYProperty() {
        return currentYPropertyWrapper().getReadOnlyProperty();
    }

    public Color getEdgeColor() {
        return edgeColorProperty().get();
    }

    public ObjectProperty<Color> edgeColorProperty() {
        if (edgeColor == null) {
            edgeColor = new SimpleObjectProperty<>(DEFAULT_EDGE_COLOR);
        }
        return edgeColor;
    }

    public void setEdgeColor(Color edgeColor) {
        this.edgeColorProperty().set(edgeColor);
    }

    public Font getLabelFont() {
        return labelFontProperty().get();
    }

    public ObjectProperty<Font> labelFontProperty() {
        if (labelFont == null) {
            labelFont = new SimpleObjectProperty<>(DEFAULT_LABEL_FONT);
        }
        return labelFont;
    }

    public void setLabelFont(Font labelFont) {
        this.labelFontProperty().set(labelFont);
    }
}
