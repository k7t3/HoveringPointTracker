package io.github.k7t3.hpt;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.*;
import javafx.util.Duration;

import java.util.Iterator;
import java.util.LinkedList;

public class HoveringPointTrackerScene extends Scene {

    /**
     * 描画オブジェクトに関するプロパティ群。
     */
    private final SceneProperties properties;

    /**
     * このSceneのrootレイアウト。
     */
    private final Group root;

    /**
     * Sceneの境界線を表すオブジェクト。
     */
    private final Rectangle sceneBorder;

    /**
     * 描画する基オブジェクト。
     */
    private final Rectangle rectangle;

    /**
     * マウスカーソルの横軸を表すグリッド。
     */
    private final Line horizontalGrid;

    /**
     * マウスカーソルの縦軸を表すグリッド。
     */
    private final Line verticalGrid;

    /**
     * 最小X座標を表すラベル。
     */
    private final Label xLabel;

    /**
     * 最小Y座標を表すラベル。
     */
    private final Label yLabel;

    /**
     * 幅を表すラベル。
     */
    private final Label wLabel;

    /**
     * 高さを表すラベル。
     */
    private final Label hLabel;

    /**
     * 座標とサイズを表すラベルをレイアウトするコンテナ。
     */
    private final Pane rectLabelContainer;

    /**
     * マウスカーソルの座標を表すラベル。
     */
    private final Label pointLabel;

    /**
     * 画面を上方向に伸長するための上部エッジ。
     */
    private final Rectangle expandableTopEdge;

    /**
     * 画面を右方向に伸長するための右側エッジ。
     */
    private final Rectangle expandableRightEdge;

    /**
     * 画面を下方向に伸長するための下側エッジ。
     */
    private final Rectangle expandableBottomEdge;

    /**
     * 画面を左方向に伸長するための左側エッジ。
     */
    private final Rectangle expandableLeftEdge;

    /**
     * 画面を左上方向に伸長するための左上ノード。
     */
    private final Rectangle expandableTopLeftNode;

    /**
     * 画面を右上方向に伸長するための右上ノード。
     */
    private final Rectangle expandableTopRightNode;

    /**
     * 画面を右下方向に伸長するための右下ノード。
     */
    private final Rectangle expandableBottomRightNode;

    /**
     * 画面を左下方向に伸長するための左下ノード。
     */
    private final Rectangle expandableBottomLeftNode;

    public HoveringPointTrackerScene(SceneProperties properties) {
        super(new Group());
        this.root = (Group) getRoot();
        this.properties = properties;

        // nodeの初期化とプロパティバインディング

        sceneBorder = new Rectangle();
        sceneBorder.setFill(Color.TRANSPARENT);
        sceneBorder.strokeProperty().bind(properties.sceneBorderColorProperty());
        sceneBorder.widthProperty().bind(widthProperty());
        sceneBorder.heightProperty().bind(heightProperty());

        rectangle = new Rectangle();
        rectangle.translateXProperty().bind(properties.minXProperty().add(properties.edgeWidthProperty()));
        rectangle.translateYProperty().bind(properties.minYProperty().add(properties.edgeWidthProperty()));
        rectangle.widthProperty().bind(properties.widthProperty().subtract(properties.edgeWidthProperty().multiply(2)));
        rectangle.heightProperty().bind(properties.heightProperty().subtract(properties.edgeWidthProperty().multiply(2)));
        rectangle.fillProperty().bind(properties.paintColorProperty());

        horizontalGrid = new Line(0, 0, 0, 0);
        horizontalGrid.setMouseTransparent(true);
        horizontalGrid.fillProperty().bind(properties.gridColorProperty());
        horizontalGrid.strokeProperty().bind(properties.gridColorProperty());
        horizontalGrid.startXProperty().bind(properties.minXProperty());
        horizontalGrid.endXProperty().bind(properties.minXProperty().add(properties.widthProperty()));
        horizontalGrid.startYProperty().bind(properties.currentYProperty());
        horizontalGrid.endYProperty().bind(properties.currentYProperty().add(1));

        verticalGrid = new Line(0, 0, 0, 0);
        verticalGrid.setMouseTransparent(true);
        verticalGrid.fillProperty().bind(properties.gridColorProperty());
        verticalGrid.strokeProperty().bind(properties.gridColorProperty());
        verticalGrid.startXProperty().bind(properties.currentXProperty());
        verticalGrid.endXProperty().bind(properties.currentXProperty().add(1));
        verticalGrid.startYProperty().bind(properties.minYProperty());
        verticalGrid.endYProperty().bind(properties.minYProperty().add(properties.heightProperty()));

        pointLabel = new Label();
        pointLabel.setMouseTransparent(true);
        pointLabel.fontProperty().bind(properties.labelFontProperty());
        pointLabel.textFillProperty().bind(properties.labelColorProperty());
        pointLabel.setEffect(new DropShadow());
        pointLabel.translateXProperty().bind(properties.minXProperty().add(properties.widthProperty()).subtract(pointLabel.widthProperty()).subtract(properties.edgeWidthProperty()));
        pointLabel.translateYProperty().bind(properties.minYProperty().add(properties.heightProperty()).subtract(pointLabel.heightProperty()).subtract(properties.edgeWidthProperty()));
        pointLabel.textProperty().bind(properties.currentXProperty().asString("(%4.0f,").concat(properties.currentYProperty().asString("%4.0f)")));

        xLabel = new Label();
        xLabel.fontProperty().bind(properties.labelFontProperty());
        xLabel.textFillProperty().bind(properties.labelColorProperty());
        xLabel.textProperty().bind(properties.minXProperty().asString("x = %.0f"));

        yLabel = new Label();
        yLabel.fontProperty().bind(properties.labelFontProperty());
        yLabel.textFillProperty().bind(properties.labelColorProperty());
        yLabel.textProperty().bind(properties.minYProperty().asString("y = %.0f"));

        wLabel = new Label();
        wLabel.fontProperty().bind(properties.labelFontProperty());
        wLabel.textFillProperty().bind(properties.labelColorProperty());
        wLabel.textProperty().bind(properties.widthProperty().asString("w = %.0f"));

        hLabel = new Label();
        hLabel.fontProperty().bind(properties.labelFontProperty());
        hLabel.textFillProperty().bind(properties.labelColorProperty());
        hLabel.textProperty().bind(properties.heightProperty().asString("h = %.0f"));

        rectLabelContainer = new VBox(xLabel, yLabel, wLabel, hLabel);
        rectLabelContainer.setMouseTransparent(true);
        rectLabelContainer.setEffect(new DropShadow());
        rectLabelContainer.translateXProperty().bind(properties.minXProperty().add(properties.widthProperty()).subtract(rectLabelContainer.widthProperty()).subtract(10));
        rectLabelContainer.translateYProperty().bind(properties.minYProperty().add(properties.edgeWidthProperty()));


        expandableTopEdge = new Rectangle();
        expandableTopEdge.setCursor(Cursor.N_RESIZE);
        expandableTopEdge.fillProperty().bind(properties.edgeColorProperty());
        expandableTopEdge.translateXProperty().bind(properties.minXProperty().add(properties.edgeWidthProperty()));
        expandableTopEdge.translateYProperty().bind(properties.minYProperty());
        expandableTopEdge.widthProperty().bind(properties.widthProperty().subtract(properties.edgeWidthProperty().multiply(2)));
        expandableTopEdge.heightProperty().bind(properties.edgeWidthProperty());

        expandableRightEdge = new Rectangle();
        expandableRightEdge.setCursor(Cursor.E_RESIZE);
        expandableRightEdge.fillProperty().bind(properties.edgeColorProperty());
        expandableRightEdge.translateXProperty().bind(properties.minXProperty().add(properties.widthProperty()).subtract(properties.edgeWidthProperty()));
        expandableRightEdge.translateYProperty().bind(properties.minYProperty().add(properties.edgeWidthProperty()));
        expandableRightEdge.widthProperty().bind(properties.edgeWidthProperty());
        expandableRightEdge.heightProperty().bind(properties.heightProperty().subtract(properties.edgeWidthProperty().multiply(2)));

        expandableBottomEdge = new Rectangle();
        expandableBottomEdge.setCursor(Cursor.S_RESIZE);
        expandableBottomEdge.fillProperty().bind(properties.edgeColorProperty());
        expandableBottomEdge.translateXProperty().bind(properties.minXProperty().add(properties.edgeWidthProperty()));
        expandableBottomEdge.translateYProperty().bind(properties.minYProperty().add(properties.heightProperty()).subtract(properties.edgeWidthProperty()));
        expandableBottomEdge.widthProperty().bind(properties.widthProperty().subtract(properties.edgeWidthProperty().multiply(2)));
        expandableBottomEdge.heightProperty().bind(properties.edgeWidthProperty());

        expandableLeftEdge = new Rectangle();
        expandableLeftEdge.setCursor(Cursor.W_RESIZE);
        expandableLeftEdge.fillProperty().bind(properties.edgeColorProperty());
        expandableLeftEdge.translateXProperty().bind(properties.minXProperty());
        expandableLeftEdge.translateYProperty().bind(properties.minYProperty().add(properties.edgeWidthProperty()));
        expandableLeftEdge.widthProperty().bind(properties.edgeWidthProperty());
        expandableLeftEdge.heightProperty().bind(properties.heightProperty().subtract(properties.edgeWidthProperty().multiply(2)));

        expandableTopLeftNode = new Rectangle();
        expandableTopLeftNode.setCursor(Cursor.NW_RESIZE);
        expandableTopLeftNode.fillProperty().bind(properties.edgeColorProperty());
        expandableTopLeftNode.translateXProperty().bind(properties.minXProperty());
        expandableTopLeftNode.translateYProperty().bind(properties.minYProperty());
        expandableTopLeftNode.widthProperty().bind(properties.edgeWidthProperty());
        expandableTopLeftNode.heightProperty().bind(properties.edgeWidthProperty());

        expandableTopRightNode = new Rectangle();
        expandableTopRightNode.setCursor(Cursor.NE_RESIZE);
        expandableTopRightNode.fillProperty().bind(properties.edgeColorProperty());
        expandableTopRightNode.translateXProperty().bind(properties.minXProperty().add(properties.widthProperty()).subtract(properties.edgeWidthProperty()));
        expandableTopRightNode.translateYProperty().bind(properties.minYProperty());
        expandableTopRightNode.widthProperty().bind(properties.edgeWidthProperty());
        expandableTopRightNode.heightProperty().bind(properties.edgeWidthProperty());

        expandableBottomRightNode = new Rectangle();
        expandableBottomRightNode.setCursor(Cursor.SE_RESIZE);
        expandableBottomRightNode.fillProperty().bind(properties.edgeColorProperty());
        expandableBottomRightNode.translateXProperty().bind(properties.minXProperty().add(properties.widthProperty()).subtract(properties.edgeWidthProperty()));
        expandableBottomRightNode.translateYProperty().bind(properties.minYProperty().add(properties.heightProperty()).subtract(properties.edgeWidthProperty()));
        expandableBottomRightNode.widthProperty().bind(properties.edgeWidthProperty());
        expandableBottomRightNode.heightProperty().bind(properties.edgeWidthProperty());

        expandableBottomLeftNode = new Rectangle();
        expandableBottomLeftNode.setCursor(Cursor.SW_RESIZE);
        expandableBottomLeftNode.fillProperty().bind(properties.edgeColorProperty());
        expandableBottomLeftNode.translateXProperty().bind(properties.minXProperty());
        expandableBottomLeftNode.translateYProperty().bind(properties.minYProperty().add(properties.heightProperty()).subtract(properties.edgeWidthProperty()));
        expandableBottomLeftNode.widthProperty().bind(properties.edgeWidthProperty());
        expandableBottomLeftNode.heightProperty().bind(properties.edgeWidthProperty());

        // 各種イベントの付与
        addDragEvent();
        addExpandEvent();
        addEventHandler(MouseEvent.MOUSE_MOVED, this::updateCurrentCursorPositionHandler);
        addContextMenuRequestedEvent();
        addClickPointsEvent();
        addScreenSizeListener();

        // Sceneの背景は透過
        setFill(Color.TRANSPARENT);

        // Nodeの登録
        root.getChildren().addAll(
                sceneBorder,
                rectangle,
                expandableTopEdge,
                expandableRightEdge,
                expandableBottomEdge,
                expandableLeftEdge,
                expandableTopLeftNode,
                expandableTopRightNode,
                expandableBottomLeftNode,
                expandableBottomRightNode,
                horizontalGrid,
                verticalGrid,
                rectLabelContainer,
                pointLabel
        );

        // 描画キャッシュの有効化
        root.setCache(true);
        root.setCacheHint(CacheHint.SPEED);
    }

    /**
     * 掴んだ座標の最小Xからの距離
     */
    private double distanceFromMinX = 0;
    /**
     * 掴んだ座標の最小Yからの距離
     */
    private double distanceFromMinY = 0;

    /**
     * ドラッグを開始するまでの遊び
     */
    private static final double PLAY_DRAG_START = 20;

    /**
     * ドラッグを開始するときに掴んだX座標
     */
    private double captureSceneX = 0;

    /**
     * ドラッグを開始するときに掴んだY座標
     */
    private double captureSceneY = 0;

    /**
     * ドラッグを実行しているか
     */
    private boolean dragging = false;

    /**
     * ドラッグ移動処理が実施されたか
     */
    private boolean isDragDone = false;

    /**
     * 図形を掴んで移動させるイベントを付与する。
     */
    private void addDragEvent() {
        rectangle.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            if (e.getButton() != MouseButton.PRIMARY) {
                return;
            }

            isDragDone = false;

            captureSceneX = e.getSceneX();
            captureSceneY = e.getSceneY();

            distanceFromMinX = e.getSceneX() - rectangle.getTranslateX();
            distanceFromMinY = e.getSceneY() - rectangle.getTranslateY();

            e.consume();
        });
        rectangle.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            if (e.getButton() != MouseButton.PRIMARY) {
                return;
            }

            if (dragging) {

                // 上下限付きの座標
                var x = Math.max(
                        distanceFromMinX,
                        Math.min(e.getSceneX(), this.getWidth() - (properties.getWidth() - distanceFromMinX))
                );
                var y = Math.max(
                        distanceFromMinY,
                        Math.min(e.getSceneY(), this.getHeight() - (properties.getHeight() - distanceFromMinY))
                );

                // ドラッグ処理
                properties.setMinX(x - distanceFromMinX);
                properties.setMinY(y - distanceFromMinY);
                // ドラッグ中も座標を追いかけるように
                updateCurrentCursorPositionHandler(e);

            } else {

                // 動かした距離が遊び範囲を超えたときにドラッグを有効化
                dragging = (PLAY_DRAG_START < Math.abs(e.getSceneX() - captureSceneX)) ||
                        (PLAY_DRAG_START < Math.abs(e.getSceneY() - captureSceneY));

            }

            e.consume();
        });
        rectangle.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            isDragDone = dragging;
            dragging = false;
        });
    }

    /**
     * エッジ・ノードをドラッグしたときに対応する方向へ伸長するイベントを付与する。
     * 加えて、伸長時にカーソル座標も更新する。
     */
    private void addExpandEvent() {
        expandableTopEdge.setOnMouseDragged(e -> {
            if (e.getButton() != MouseButton.PRIMARY) {
                return;
            }

            stretch(-1, e.getSceneY(), -1, -1);
            updateCurrentCursorPositionHandler(e);
            e.consume();
        });
        expandableRightEdge.setOnMouseDragged(e -> {
            if (e.getButton() != MouseButton.PRIMARY) {
                return;
            }

            stretch(-1, -1, e.getSceneX(), -1);
            updateCurrentCursorPositionHandler(e);
            e.consume();
        });
        expandableBottomEdge.setOnMouseDragged(e -> {
            if (e.getButton() != MouseButton.PRIMARY) {
                return;
            }

            stretch(-1, -1, -1, e.getSceneY());
            updateCurrentCursorPositionHandler(e);
            e.consume();
        });
        expandableLeftEdge.setOnMouseDragged(e -> {
            if (e.getButton() != MouseButton.PRIMARY) {
                return;
            }

            stretch(e.getSceneX(), -1, -1, -1);
            updateCurrentCursorPositionHandler(e);
            e.consume();
        });
        expandableTopLeftNode.setOnMouseDragged(e -> {
            if (e.getButton() != MouseButton.PRIMARY) {
                return;
            }

            stretch(e.getSceneX(), e.getSceneY(), -1, -1);
            updateCurrentCursorPositionHandler(e);
            e.consume();
        });
        expandableTopRightNode.setOnMouseDragged(e -> {
            if (e.getButton() != MouseButton.PRIMARY) {
                return;
            }

            stretch(-1, e.getSceneY(), e.getSceneX(), -1);
            updateCurrentCursorPositionHandler(e);
            e.consume();
        });
        expandableBottomRightNode.setOnMouseDragged(e -> {
            if (e.getButton() != MouseButton.PRIMARY) {
                return;
            }

            stretch(-1, -1, e.getSceneX(), e.getSceneY());
            updateCurrentCursorPositionHandler(e);
            e.consume();
        });
        expandableBottomLeftNode.setOnMouseDragged(e -> {
            if (e.getButton() != MouseButton.PRIMARY) {
                return;
            }

            stretch(e.getSceneX(), -1, -1, e.getSceneY());
            updateCurrentCursorPositionHandler(e);
            e.consume();
        });
    }

    /**
     * 引数の{@link MouseEvent}に基づいて現在のポインタの座標を更新する。
     * @param event マウスイベント
     */
    private void updateCurrentCursorPositionHandler(MouseEvent event) {
        // 描画範囲内でのみ座標を割り当てる
        double x = Math.max(properties.getMinX(), Math.min(event.getSceneX(), properties.getMinX() + properties.getWidth()));
        double y = Math.max(properties.getMinY(), Math.min(event.getSceneY(), properties.getMinY() + properties.getHeight()));
        properties.currentXPropertyWrapper().set(x);
        properties.currentYPropertyWrapper().set(y);
    }

    private DoubleExpression minWidthLimit;

    private DoubleExpression minHeightLimit;

    /**
     * それぞれの入力パラメータが負数でない場合、指定方向の値を更新する。
     * @param minX 最小X
     * @param minY 最小Y
     * @param maxX 最大X
     * @param maxY 最大Y
     */
    private void stretch(double minX, double minY, double maxX, double maxY) {
        if (minWidthLimit == null) {
            minWidthLimit = properties.edgeWidthProperty().multiply(2).add(pointLabel.widthProperty());
            minHeightLimit = properties.edgeWidthProperty().multiply(2).add(pointLabel.heightProperty()).add(rectLabelContainer.heightProperty());
        }
        double widthLimit = minWidthLimit.get();
        double heightLimit = minHeightLimit.get();

        if (0d <= minX) {
            minX = Math.max(0, Math.min(properties.getMinX() + properties.getWidth() - widthLimit, minX));
            double deltaX = properties.getMinX() - minX;
            properties.setMinX(minX);
            properties.setWidth(properties.getWidth() + deltaX);
        }
        if (0d <= minY) {
            minY = Math.max(0, Math.min(properties.getMinY() + properties.getHeight() - heightLimit, minY));
            double deltaY = properties.getMinY() - minY;
            properties.setMinY(minY);
            properties.setHeight(properties.getHeight() + deltaY);
        }
        if (0d <= maxX) {
            maxX = Math.max(properties.getMinX() + widthLimit, Math.min(maxX, getWidth()));
            double deltaX = maxX - (properties.getMinX() + properties.getWidth());
            properties.setWidth(properties.getWidth() + deltaX);
        }
        if (0d <= maxY) {
            maxY = Math.max(properties.getMinY() + heightLimit, Math.min(maxY, getHeight()));
            double deltaY = maxY - (properties.getMinY() + properties.getHeight());
            properties.setHeight(properties.getHeight() + deltaY);
        }
    }

    private ObjectProperty<ContextMenu> contextMenu;

    public ContextMenu getContextMenu() {
        return contextMenuProperty().get();
    }

    public ObjectProperty<ContextMenu> contextMenuProperty() {
        if (contextMenu == null) {
            ContextMenu cm =  new ContextMenu();
            cm.setHideOnEscape(true);
            cm.setAutoHide(true);
            contextMenu = new SimpleObjectProperty<>(cm);
        }
        return contextMenu;
    }

    public void setContextMenu(ContextMenu contextMenu) {
        this.contextMenuProperty().set(contextMenu);
    }

    private void addContextMenuRequestedEvent() {
        rectangle.setOnContextMenuRequested(e -> {
            if (contextMenu == null) {
                return;
            }

            ContextMenu contextMenu = getContextMenu();

            if (contextMenu.getItems().isEmpty()) {
                return;
            }

            if (contextMenu.isShowing()) {
                return;
            }

            contextMenu.show(rectangle, e.getScreenX(), e.getScreenY());
            e.consume();
        });
        addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            if (contextMenu == null) {
                return;
            }

            ContextMenu contextMenu = getContextMenu();

            if (!contextMenu.isShowing()) {
                return;
            }

            contextMenu.hide();
            e.consume();
        });
    }

    private static final double CLICK_POINT_CIRCLE_RADIUS = 3;

    private LinkedList<Circle> clickPoints;

    private Tooltip clickPointTooltip;

    /**
     * 描画範囲をクリックするとその座標をポイントする円を描画するイベントを付与する。
     */
    private void addClickPointsEvent() {
        rectangle.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (e.getButton() != MouseButton.PRIMARY) {
                return;
            }
            // 移動ドラッグ解放のクリックのときは無視
            if (isDragDone) {
                return;
            }

            // Controlを押しながらクリックすると直近のクリックポイントを削除
            if (e.isControlDown()) {

                if (clickPoints == null || clickPoints.isEmpty()) {
                    return;
                }

                root.getChildren().remove(clickPoints.pollLast());

            } else {

                Circle circle = new Circle(CLICK_POINT_CIRCLE_RADIUS);
                circle.fillProperty().bind(properties.clickPointColorProperty());
                circle.setTranslateX(e.getSceneX());
                circle.setTranslateY(e.getSceneY());
                // クリックポイントにカーソルを合わせると座標をポップアップするように。
                circle.setOnMouseEntered(e2 -> {
                    if (getWindow() == null) {
                        return;
                    }
                    clickPointTooltip.setText(String.format("(%4.0f,%4.0f)", e.getSceneX(), e.getSceneY()));
                    clickPointTooltip.show(getWindow(), e.getSceneX(), e.getSceneY() + 10);
                });
                circle.setOnMouseExited(e2 -> clickPointTooltip.hide());

                root.getChildren().add(circle);

                if (clickPoints == null) {
                    clickPoints = new LinkedList<>();
                    clickPointTooltip = new Tooltip();
                    clickPointTooltip.setFont(Font.font("monospaced", 13));
                }

                clickPoints.add(circle);

                // 波紋エフェクトの表示
                Circle ripple = new Circle(CLICK_POINT_CIRCLE_RADIUS);
                ripple.setMouseTransparent(true);
                ripple.setFill(Color.TRANSPARENT);
                ripple.strokeProperty().bind(properties.clickPointColorProperty());
                ripple.setTranslateX(e.getSceneX());
                ripple.setTranslateY(e.getSceneY());
                root.getChildren().add(ripple);

                // アニメーションの実行
                KeyValue scaleKeyValue = new KeyValue(ripple.radiusProperty(), ripple.getRadius() * 8);
                KeyValue fadeKeyValue = new KeyValue(ripple.opacityProperty(), 0);
                KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), scaleKeyValue, fadeKeyValue);
                Timeline timeline = new Timeline(keyFrame);
                timeline.setOnFinished(e2 -> root.getChildren().remove(ripple));
                timeline.play();

            }

            e.consume();
        });

        // 描画範囲からはみ出したクリックポイントを削除する
        properties.minXProperty().addListener((ob, o, n) -> removeNotIntersectClickPoints());
        properties.minYProperty().addListener((ob, o, n) -> removeNotIntersectClickPoints());
        properties.widthProperty().addListener((ob, o, n) -> removeNotIntersectClickPoints());
        properties.heightProperty().addListener((ob, o, n) -> removeNotIntersectClickPoints());
    }

    /**
     * 描画範囲から出たクリックポイントを削除する
     */
    private void removeNotIntersectClickPoints() {
        if (clickPoints == null || clickPoints.isEmpty()) {
            return;
        }

        Rectangle2D rectangle = new Rectangle2D (
                properties.getMinX(),
                properties.getMinY(),
                properties.getWidth(),
                properties.getHeight()
        );

        Iterator<Circle> iterator = clickPoints.iterator();
        while (iterator.hasNext()) {
            Circle circle = iterator.next();

            Rectangle2D circleRectangle = new Rectangle2D(
                    circle.getTranslateX(),
                    circle.getTranslateY(),
                    CLICK_POINT_CIRCLE_RADIUS,
                    CLICK_POINT_CIRCLE_RADIUS
            );

            if (!rectangle.intersects(circleRectangle)) {
                root.getChildren().remove(circle);
                iterator.remove();
            }

        }

    }

    public void clearClickPoints() {
        if (clickPoints == null) {
            return;
        }

        root.getChildren().removeAll(clickPoints);
        clickPoints.clear();
    }

    private void addScreenSizeListener() {
        // プロパティのスクリーン設定が変更されたときにウインドウのサイズを変更するリスナを登録
        properties.screenProperty().addListener((ob, o, n) -> {
            if (n == null) {
                return;
            }

            Rectangle2D bounds = n.getBounds();

            Window window = getWindow();
            window.setX(bounds.getMinX());
            window.setY(bounds.getMinY());
            window.setWidth(bounds.getWidth());
            window.setHeight(bounds.getHeight());
        });
        // 所属する親ウインドウが変更されたときにサイズを変更するリスナを登録
        windowProperty().addListener((ob, o, n) -> {
            if (n == null) {
                return;
            }
            if (properties.getScreen() == null) {
                return;
            }

            Rectangle2D bounds = properties.getScreen().getBounds();

            n.setX(bounds.getMinX());
            n.setY(bounds.getMinY());
            n.setWidth(bounds.getWidth());
            n.setHeight(bounds.getHeight());
        });
    }

}
