package io.github.k7t3.hpt;

import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.*;

class ScreenSelectionStage extends Stage {

    private final Screen screen;

    private final int screenNumber;

    ScreenSelectionStage(Screen screen, int screenNumber) {
        super(StageStyle.TRANSPARENT);
        this.screen = screen;
        this.screenNumber = screenNumber;
        init();
    }

    private static final double EDGE_SIZE = 10;

    private static final Color EDGE_FILL = Color.LIGHTSKYBLUE;

    private static final double FONT_SIZE = 64;

    private void init() {
        Group group = new Group();
        Scene scene = new Scene(group);

        Rectangle2D bounds = screen.getBounds();
        double x = bounds.getMinX();
        double y = bounds.getMinY();
        double width = bounds.getWidth();
        double height = bounds.getHeight();

        Rectangle edgeLeft = new Rectangle();
        edgeLeft.setWidth(EDGE_SIZE);
        edgeLeft.setHeight(height);
        edgeLeft.setFill(EDGE_FILL);

        Rectangle edgeTop = new Rectangle();
        edgeTop.setWidth(width);
        edgeTop.setHeight(EDGE_SIZE);
        edgeTop.setFill(EDGE_FILL);

        Rectangle edgeRight = new Rectangle();
        edgeRight.setWidth(EDGE_SIZE);
        edgeRight.setHeight(height);
        edgeRight.setFill(EDGE_FILL);
        edgeRight.setTranslateX(width - EDGE_SIZE);

        Rectangle edgeBottom = new Rectangle();
        edgeBottom.setWidth(width);
        edgeBottom.setHeight(EDGE_SIZE);
        edgeBottom.setFill(EDGE_FILL);
        edgeBottom.setTranslateY(height - EDGE_SIZE);

        Text numberText = new Text("ディスプレイ " + screenNumber);
        numberText.setTranslateX(200);
        numberText.setTranslateY(200);
        numberText.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, FONT_SIZE));
        numberText.setFill(Color.WHITE);
        numberText.setStroke(Color.BLACK);
        numberText.setEffect(new DropShadow());

        group.getChildren().addAll(
                edgeTop,
                edgeRight,
                edgeBottom,
                edgeLeft,
                numberText
        );
        group.getChildren().forEach(n -> n.setMouseTransparent(true));
        group.setMouseTransparent(true);

        scene.setFill(Color.TRANSPARENT);

        setScene(scene);
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
    }

}
