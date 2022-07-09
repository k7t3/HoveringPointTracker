module HoveringPointTracker {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;

    opens io.github.k7t3.hpt to javafx.graphics;
}