package io.github.k7t3.hpt;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Collections;
import java.util.Optional;

@SuppressWarnings("ConstantConditions")
public class HPTApplication extends Application {
    
    private Exception propertyReadError;

    private SceneProperties properties;

    @Override
    public void init() throws Exception {
        super.init();

        Platform.setImplicitExit(true);

        properties = new SceneProperties();

        try {

            PropertyManager manager = new PropertyManager(properties);
            manager.load();

        } catch (Exception e) {
            
            propertyReadError = e;

        }
        
    }

    private boolean isInterruptIfPropertyReadError() {
        if (propertyReadError == null) {
            return false;
        }

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("WARNING");
        alert.setHeaderText("設定の読み込みエラー");
        alert.setContentText("設定ファイルの読み込みに失敗しました。\n" + propertyReadError.getMessage());
        alert.getButtonTypes().setAll(ButtonType.OK);
        alert.showAndWait();

        String format = """
                設定を初期化して実行しますか？
                
                手動で修正する場合は以下のファイルを修正してください。
                %s
                """;

        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("CONFIRMATION");
        alert.setHeaderText("設定の読み込みエラー");
        alert.setContentText(String.format(format, PropertyManager.PROPERTY_FILE_PATH));
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

        Optional<ButtonType> chosen = alert.showAndWait();

        if (chosen.isEmpty()) {
            return true;
        }

        if (chosen.get() == ButtonType.NO) {
            return true;
        }

        return false;
    }

    @Override
    public void stop() throws Exception {
        super.stop();

        if (properties == null) {
            return;
        }

        PropertyManager manager = new PropertyManager(properties);
        manager.save();

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

//        System.setProperty("javafx.animation.fullspeed", "true");

        // プロパティ読み込みエラーに失敗していた場合は警告を出す
        if (isInterruptIfPropertyReadError()) {
            Platform.exit();
            return;
        }

        // プロパティ設定画面を表示
        PropertyViewController controller = new PropertyViewController(properties);
        controller.showAndWait();

        // キャンセルされていたら終了
        if (controller.isCancelled()) {
            Platform.exit();
            return;
        }

        HoveringPointTrackerScene scene = new HoveringPointTrackerScene(properties);

        MenuItem clipPointMenuItem = new MenuItem("座標をコピー");
        clipPointMenuItem.setOnAction(e -> {
            String point = String.format("%4.0f,%4.0f", properties.getCurrentX(), properties.getCurrentY());
            Clipboard clipboard = Clipboard.getSystemClipboard();
            clipboard.setContent(Collections.singletonMap(DataFormat.PLAIN_TEXT, point));
            e.consume();
        });

        MenuItem clearSavedPointsMenuItem = new MenuItem("座標をすべて削除");
        clearSavedPointsMenuItem.setOnAction(e -> {
            scene.clearClickPoints();
            e.consume();
        });

        MenuItem showPropertyMenuItem = new MenuItem("プロパティ");
        showPropertyMenuItem.setOnAction(e -> {
            PropertyViewController propertyView = new PropertyViewController(properties);
            propertyView.initOwner(primaryStage);
            propertyView.initModality(Modality.WINDOW_MODAL);
            propertyView.showAndWait();
        });

        MenuItem closeMenuItem = new MenuItem("閉じる(_C)");
        closeMenuItem.setOnAction(e -> primaryStage.close());

        scene.getContextMenu().getItems().addAll(
                clearSavedPointsMenuItem,
                new SeparatorMenuItem(),
                clipPointMenuItem,
                new SeparatorMenuItem(),
                showPropertyMenuItem,
                new SeparatorMenuItem(),
                closeMenuItem
        );

        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(getClass().getResource("/icon/icon.png").toExternalForm()));
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();
    }
}
