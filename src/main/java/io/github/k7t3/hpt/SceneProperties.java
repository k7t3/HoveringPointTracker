package io.github.k7t3.hpt;

import javafx.beans.property.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class SceneProperties {

    private static final Paint DEFAULT_PAINT_FILL = Color.color(0d, 0d, 0d, 0.2);

    private static final Paint DEFAULT_EDGE_FILL = ((Color) DEFAULT_PAINT_FILL).deriveColor(
            1.0, 1.0, 1.0, 1.2);

    private static final Paint DEFAULT_GRID_FILL = Color.SNOW;

    private static final Paint DEFAULT_SCENE_BORDER_FILL = Color.TRANSPARENT;

    private static final Paint DEFAULT_LABEL_FILL = ((Color)DEFAULT_PAINT_FILL).deriveColor(0, 1, 1, 2.4);

    private static final Paint DEFAULT_CLICK_POINT_FILL = Color.SNOW;

    private static final double DEFAULT_WIDTH = 200;

    private static final double DEFAULT_HEIGHT = 200;

    private static final double DEFAULT_EDGE_WIDTH = 10;

    private static final Font DEFAULT_LABEL_FONT = Font.font("monospaced", FontWeight.BOLD, 13);

    private DoubleProperty minX;

    private DoubleProperty minY;

    private DoubleProperty width;

    private DoubleProperty height;

    private DoubleProperty edgeWidth;

    private ObjectProperty<Paint> paintFill;

    private ObjectProperty<Paint> gridFill;

    private ObjectProperty<Paint> edgeFill;

    private ObjectProperty<Paint> labelFill;

    private ObjectProperty<Paint> clickPointFill;

    private ObjectProperty<Paint> sceneBorderFill;

    private ReadOnlyDoubleWrapper currentX;

    private ReadOnlyDoubleWrapper currentY;

    private ObjectProperty<Font> labelFont;

    public double getMinX() {
        return minXProperty().get();
    }

    public DoubleProperty minXProperty() {
        if (minX == null) {
            minX = new SimpleDoubleProperty(0);
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
            minY = new SimpleDoubleProperty(0);
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

    public Paint getPaintFill() {
        return paintFillProperty().get();
    }

    public ObjectProperty<Paint> paintFillProperty() {
        if (paintFill == null) {
            paintFill = new SimpleObjectProperty<>(DEFAULT_PAINT_FILL);
        }
        return paintFill;
    }

    public void setPaintFill(Paint paintFill) {
        this.paintFillProperty().set(paintFill);
    }

    public Paint getGridFill() {
        return gridFillProperty().get();
    }

    public ObjectProperty<Paint> gridFillProperty() {
        if (gridFill == null) {
            gridFill = new SimpleObjectProperty<>(DEFAULT_GRID_FILL);
        }
        return gridFill;
    }

    public void setGridFill(Paint gridFill) {
        this.gridFillProperty().set(gridFill);
    }

    public Paint getSceneBorderFill() {
        return sceneBorderFillProperty().get();
    }

    public ObjectProperty<Paint> sceneBorderFillProperty() {
        if (sceneBorderFill == null) {
            sceneBorderFill = new SimpleObjectProperty<>(DEFAULT_SCENE_BORDER_FILL);
        }
        return sceneBorderFill;
    }

    public void setSceneBorderFill(Paint sceneBorderFill) {
        this.sceneBorderFillProperty().set(sceneBorderFill);
    }

    public Paint getLabelFill() {
        return labelFillProperty().get();
    }

    public ObjectProperty<Paint> labelFillProperty() {
        if (labelFill == null) {
            labelFill = new SimpleObjectProperty<>(DEFAULT_LABEL_FILL);
        }
        return labelFill;
    }

    public void setLabelFill(Paint labelFill) {
        this.labelFillProperty().set(labelFill);
    }

    public Paint getClickPointFill() {
        return clickPointFillProperty().get();
    }

    public ObjectProperty<Paint> clickPointFillProperty() {
        if (clickPointFill == null) {
            clickPointFill = new SimpleObjectProperty<>(DEFAULT_CLICK_POINT_FILL);
        }
        return clickPointFill;
    }

    public void setClickPointFill(Paint clickPointFill) {
        this.clickPointFillProperty().set(clickPointFill);
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

    public Paint getEdgeFill() {
        return edgeFillProperty().get();
    }

    public ObjectProperty<Paint> edgeFillProperty() {
        if (edgeFill == null) {
            edgeFill = new SimpleObjectProperty<>(DEFAULT_EDGE_FILL);
        }
        return edgeFill;
    }

    public void setEdgeFill(Paint edgeFill) {
        this.edgeFillProperty().set(edgeFill);
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
