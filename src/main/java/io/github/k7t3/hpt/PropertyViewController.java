package io.github.k7t3.hpt;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@SuppressWarnings("ConstantConditions")
public class PropertyViewController extends Stage implements Initializable {

    @FXML
    private Pane root;

    @FXML
    private ComboBox<SelectableScreen> displayComboBox;

    @FXML
    private ColorPicker paintColor;

    @FXML
    private ColorPicker gridColor;

    @FXML
    private ColorPicker edgeColor;

    @FXML
    private ColorPicker labelColor;

    @FXML
    private ColorPicker clickPointColor;

    @FXML
    private ColorPicker sceneBorderColor;

    private record SelectableScreen(Screen screen, int screenNumber) { }

    private final SceneProperties properties;

    public PropertyViewController(SceneProperties properties) {
        this.properties = properties;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PropertiesView.fxml"));
        loader.setController(this);
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // ComboBoxで選んだスクリーンを変更したときにプレビューを表示するリスナを登録
        displayComboBox.getSelectionModel().selectedItemProperty().addListener((ob, o, n) -> showStage(n));
        // ComboBoxをドロップダウンしたときに表示されるセルのファクトリ
        displayComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(SelectableScreen item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {

                    setText(null);

                } else {

                    Rectangle2D bounds = item.screen.getBounds();

                    setText(getScreenName(item.screenNumber, bounds.getWidth(), bounds.getHeight()));

                }
            }
        });
        // コンボボックスの見出し
        displayComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(SelectableScreen object) {

                Rectangle2D bounds = object.screen.getBounds();
                return getScreenName(object.screenNumber, bounds.getWidth(), bounds.getHeight());

            }

            @Override
            public SelectableScreen fromString(String string) {
                return null;
            }
        });

        // コンボボックスに表示するディスプレイの設定
        int number = 1;
        ObservableList<SelectableScreen> screens = FXCollections.observableArrayList();
        for (Screen screen : Screen.getScreens()) {
            screens.add(new SelectableScreen(screen, number++));
        }
        displayComboBox.setItems(screens);

        displayComboBox.getSelectionModel().select(0);
        showStage(screens.get(0));

        paintColor.setValue(properties.getPaintColor());
        gridColor.setValue(properties.getGridColor());
        edgeColor.setValue(properties.getEdgeColor());
        labelColor.setValue(properties.getLabelColor());
        clickPointColor.setValue(properties.getClickPointColor());
        sceneBorderColor.setValue(properties.getSceneBorderColor());

        // この画面を閉じたときにプレビューが表示されていたら同時に閉じる。
        showingProperty().addListener((ob, o, n) -> {
            if (o && !n) {
                if (stage != null) {
                    stage.close();
                }
            }
        });

        getIcons().add(new Image(getClass().getResource("/icon/icon.png").toExternalForm()));

        Scene scene = new Scene(root);
        setScene(scene);
    }

    private static final String SCREEN_FORMAT = "%s %d (%4.0f x %4.0f)";

    private String getScreenName(int number, double width, double height) {
        return String.format(SCREEN_FORMAT, "ディスプレイ", number, width, height);
    }

    private Stage stage;

    private void showStage(SelectableScreen screen) {
        if (stage != null) {
            stage.close();
        }
        if (screen == null) {
            return;
        }

        stage = new ScreenSelectionStage(screen.screen, screen.screenNumber);
        stage.initOwner(this);
        stage.show();
        toFront();
    }

    @FXML
    private void enter(ActionEvent e) {
        e.consume();
        properties.setScreen(displayComboBox.getValue().screen());
        properties.setPaintColor(paintColor.getValue());
        properties.setGridColor(gridColor.getValue());
        properties.setGridColor(edgeColor.getValue());
        properties.setLabelColor(labelColor.getValue());
        properties.setClickPointColor(clickPointColor.getValue());
        properties.setSceneBorderColor(sceneBorderColor.getValue());
        close();
    }

    private boolean isCancelled = false;

    public boolean isCancelled() {
        return isCancelled;
    }

    @FXML
    private void cancel(ActionEvent e) {
        isCancelled = true;
        e.consume();
        close();
    }

}
