package io.github.k7t3.hpt;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Collections;
import java.util.Optional;

@SuppressWarnings("ConstantConditions")
public class HPTApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        System.setProperty("javafx.animation.fullspeed", "true");

        Platform.setImplicitExit(true);

        SceneProperties properties = new SceneProperties();
        properties.setMinX(1000);
        properties.setMinY(500);
        properties.setWidth(300);
        properties.setHeight(300);
        properties.setClickPointFill(Color.LIGHTYELLOW);
        properties.setLabelFont(Font.font("monospaced", FontWeight.BOLD, 14));
        properties.setSceneBorderFill(Color.YELLOW);

//        properties.setPaintFill(Color.color(0f, 0f, 0f, 0.1));
//        properties.setGridFill(Color.SNOW);
//        properties.setEdgeFill(Color.BLACK);

        Screen screen;
        if (Screen.getScreens().size() == 1 && false) {
            screen = Screen.getPrimary();
        } else {
            ScreenSelectDialog dialog = new ScreenSelectDialog();
            Optional<Screen> screenOptional = dialog.showAndWait();
            if (!screenOptional.isPresent()) {
                System.out.println("not selected");
                primaryStage.close();
                return;
            }
            screen = screenOptional.get();
        }

        Rectangle2D screenBounds = screen.getBounds();
        primaryStage.setX(screenBounds.getMinX());
        primaryStage.setY(screenBounds.getMinY());
        primaryStage.setWidth(screenBounds.getWidth());
        primaryStage.setHeight(screenBounds.getHeight());
        primaryStage.getIcons().add(new Image(getClass().getResource("/icon/icon.png").toExternalForm()));

        RadioMenuItem showWindow = new RadioMenuItem("ウインドウ領域を表示");
        showWindow.setSelected(false);
        showWindow.setOnAction(e -> {
            if (showWindow.isSelected()) {
            } else {
            }
        });

        MenuItem closeMenuItem = new MenuItem("閉じる(_C)");
        closeMenuItem.setOnAction(e -> primaryStage.close());

        MenuItem clipPointMenuItem = new MenuItem("座標をコピー");
        clipPointMenuItem.setOnAction(e -> {
            String point = String.format("%4.0f,%4.0f", properties.getCurrentX(), properties.getCurrentY());
            Clipboard clipboard = Clipboard.getSystemClipboard();
            clipboard.setContent(Collections.singletonMap(DataFormat.PLAIN_TEXT, point));
            e.consume();
        });

        HoveringPointTrackerScene scene = new HoveringPointTrackerScene(properties);
        scene.getContextMenu().getItems().addAll(
                showWindow,
                new SeparatorMenuItem(),
                clipPointMenuItem,
                new SeparatorMenuItem(),
                closeMenuItem
        );

        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
