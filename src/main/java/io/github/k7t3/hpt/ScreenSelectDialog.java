package io.github.k7t3.hpt;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.StringConverter;

@SuppressWarnings("ConstantConditions")
public class ScreenSelectDialog extends Dialog<Screen> {

    private final ComboBox<SelectableScreen> screenComboBox = new ComboBox<>();

    public ScreenSelectDialog() {
        init();
    }

    private static final String SCREEN_FORMAT = "%s %d (%4.0f x %4.0f)";

    private record SelectableScreen(Screen screen, int screenNumber) { }

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
        stage.show();
    }

    private void init() {
        screenComboBox.getSelectionModel().selectedItemProperty().addListener((ob, o, n) -> showStage(n));

        screenComboBox.setCellFactory(param -> new ListCell<>() {
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

        screenComboBox.setConverter(new StringConverter<>() {
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

        int number = 1;
        ObservableList<SelectableScreen> screens = FXCollections.observableArrayList();
        for (Screen screen : Screen.getScreens()) {
            screens.add(new SelectableScreen(screen, number++));
        }

        screenComboBox.setItems(screens);

        screenComboBox.getSelectionModel().select(0);

        showStage(screenComboBox.getValue());

        DialogPane dialogPane = getDialogPane();
        dialogPane.setHeaderText("ディスプレイの選択");

        StackPane container = new StackPane(screenComboBox);
        container.setPadding(new Insets(10));
        dialogPane.setContent(container);
        dialogPane.setExpanded(false);
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        ((Stage)dialogPane.getScene().getWindow()).getIcons().add(
                new Image(getClass().getResource("/icon/icon.png").toExternalForm())
        );

        setOnCloseRequest(e -> {
            if (stage != null) {
                stage.close();
            }
        });

        setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {

                return screenComboBox.getValue().screen;

            } else {

                return null;

            }
        });
    }
}
