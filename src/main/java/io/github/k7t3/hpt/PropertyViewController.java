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
import java.util.function.Consumer;

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
    private Spinner<Double> xSpinner;

    @FXML
    private Spinner<Double> ySpinner;

    @FXML
    private Spinner<Double> wSpinner;

    @FXML
    private Spinner<Double> hSpinner;

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
        displayComboBox.getSelectionModel().selectedItemProperty().addListener((ob, o, n) -> showPreviewStage(n));
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

        // 初期表示するスクリーン
        SelectableScreen screen;

        if (properties.getScreen() != null) {

            // プロパティで指定されていた場合
            screen = screens.stream()
                    .filter(s -> s.screen.hashCode() == properties.getScreen().hashCode())
                    .findFirst()
                    .orElse(screens.get(0));

        } else {

            // プロパティで指定されていない場合は先頭のスクリーン
            screen = screens.get(0);

        }

        // 初期選択項目
        displayComboBox.getSelectionModel().select(screen);
        showPreviewStage(screen);
        setSpinnerProperties(screen.screen);

        // ディスプレイを変更したらスピナの値を変更する。
        displayComboBox.getSelectionModel().selectedItemProperty().addListener((ob, o, n) -> {
            if (n != null) {
                setSpinnerProperties(n.screen);
            }
        });

        Consumer<Spinner<Double>> assignStringConverter = (spinner) -> {
            spinner.getValueFactory().setConverter(new StringConverter<>() {
                @Override
                public String toString(Double object) {
                    return String.format("%.0f", object);
                }

                @Override
                public Double fromString(String string) {
                    try {

                        return Double.parseDouble(string);

                    } catch (Exception e) {

                        // Double型にパースできないときは元に戻す
                        spinner.getEditor().setText(toString(spinner.getValue()));
                        return spinner.getValue();
                    }

                }
            });
        };
        assignStringConverter.accept(xSpinner);
        assignStringConverter.accept(ySpinner);
        assignStringConverter.accept(wSpinner);
        assignStringConverter.accept(hSpinner);

        paintColor.setValue(properties.getPaintColor());
        gridColor.setValue(properties.getGridColor());
        edgeColor.setValue(properties.getEdgeColor());
        labelColor.setValue(properties.getLabelColor());
        clickPointColor.setValue(properties.getClickPointColor());

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

    /**
     * 選択されたディスプレイに応じて座標・サイズのスピナーのプロパティを更新する。
     * @param screen 選択されたディスプレイ
     */
    private void setSpinnerProperties(Screen screen) {
        Rectangle2D screenBounds = screen.getBounds();

        xSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(
                screenBounds.getMinX(),
                screenBounds.getMaxX(),
                Math.min(screenBounds.getMaxX(), Math.max(screenBounds.getMinX(), properties.getMinX()))
        ));
        ySpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(
                screenBounds.getMinY(),
                screenBounds.getMaxY(),
                Math.min(screenBounds.getMaxY(), Math.max(screenBounds.getMinY(), properties.getMinY()))
        ));
        // 幅と高さの下限値はラベルサイズに影響されるがここでは不明のためてきとうに設定。
        wSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(
                properties.getEdgeWidth() * 4,
                screenBounds.getMaxX(),
                Math.max(properties.getEdgeWidth() * 4, Math.min(properties.getWidth(), screenBounds.getMaxX()))
        ));
        hSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(
                properties.getEdgeWidth() * 4,
                screenBounds.getMaxY(),
                Math.max(properties.getEdgeWidth() * 4, Math.min(properties.getHeight(), screenBounds.getMaxY()))
        ));
    }

    private static final String SCREEN_FORMAT = "%s %d (%4.0f x %4.0f)";

    private String getScreenName(int number, double width, double height) {
        return String.format(SCREEN_FORMAT, "ディスプレイ", number, width, height);
    }

    /**
     * 選択しているディスプレイを表す透過Stage
     */
    private Stage stage;

    /**
     * 選択しているディスプレイを判別するための透過Stageを表示する。
     * @param screen 選択したディスプレイ
     */
    private void showPreviewStage(SelectableScreen screen) {
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
        isCancelled = false;
        properties.setScreen(displayComboBox.getValue().screen());
        properties.setPaintColor(paintColor.getValue());
        properties.setGridColor(gridColor.getValue());
        properties.setEdgeColor(edgeColor.getValue());
        properties.setLabelColor(labelColor.getValue());
        properties.setClickPointColor(clickPointColor.getValue());
        properties.setMinX(xSpinner.getValue());
        properties.setMinY(ySpinner.getValue());
        properties.setWidth(wSpinner.getValue());
        properties.setHeight(hSpinner.getValue());
        close();
    }

    private boolean isCancelled = true;

    public boolean isCancelled() {
        return isCancelled;
    }

    @FXML
    private void cancel(ActionEvent e) {
        e.consume();
        close();
    }

}
