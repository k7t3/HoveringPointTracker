package io.github.k7t3.hpt;

import javafx.scene.paint.Color;
import javafx.stage.Screen;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Properties;

/**
 * プロパティの永続化管理
 */
class PropertyManager {

    public static final String PROPERTY_DISPLAY = "display";
    public static final String PROPERTY_MIN_X = "minX";
    public static final String PROPERTY_MIN_Y = "minY";
    public static final String PROPERTY_WIDTH = "width";
    public static final String PROPERTY_HEIGHT = "height";
    public static final String PROPERTY_EDGE_WIDTH = "edgeWidth";
    public static final String PROPERTY_PAINT_COLOR = "paintColor";
    public static final String PROPERTY_GRID_COLOR = "gridColor";
    public static final String PROPERTY_EDGE_COLOR = "edgeColor";
    public static final String PROPERTY_LABEL_COLOR = "labelColor";
    public static final String PROPERTY_CLICK_POINT_COLOR = "clickPointColor";
    public static final String PROPERTY_SCENE_BORDER_COLOR = "sceneBorderColor";

    private final SceneProperties properties;

    private final boolean isSupported;

    public PropertyManager() {
        this(new SceneProperties());
    }

    public PropertyManager(SceneProperties properties) {
        this.properties = properties;
        isSupported = System.getProperty("os.name").toLowerCase().startsWith("win");
    }

    public SceneProperties getProperties() {
        return properties;
    }

    /**
     * プロパティファイルのパス。Windowsのみサポート。
     */
    public static final Path PROPERTY_FILE_PATH = Paths.get(
            System.getProperty("user.home"),
            "AppData",
            "Local",
            "io.github.k7t3.hpt",
            "properties.xml");

    public void save() {
        if (!isSupported) {
            System.out.println("property manager unsupported.");
            return;
        }

        Properties p = new Properties();

        int screenHashCode = properties.getScreen() != null ? properties.getScreen().hashCode() : -1;

        p.setProperty(PROPERTY_DISPLAY, String.valueOf(screenHashCode));
        p.setProperty(PROPERTY_MIN_X, String.valueOf(properties.getMinX()));
        p.setProperty(PROPERTY_MIN_Y, String.valueOf(properties.getMinY()));
        p.setProperty(PROPERTY_WIDTH, String.valueOf(properties.getWidth()));
        p.setProperty(PROPERTY_HEIGHT, String.valueOf(properties.getHeight()));
        p.setProperty(PROPERTY_EDGE_WIDTH, String.valueOf(properties.getEdgeWidth()));
        p.setProperty(PROPERTY_PAINT_COLOR, properties.getPaintColor().toString());
        p.setProperty(PROPERTY_GRID_COLOR, properties.getGridColor().toString());
        p.setProperty(PROPERTY_EDGE_COLOR, properties.getEdgeColor().toString());
        p.setProperty(PROPERTY_LABEL_COLOR, properties.getLabelColor().toString());
        p.setProperty(PROPERTY_CLICK_POINT_COLOR, properties.getClickPointColor().toString());
        p.setProperty(PROPERTY_SCENE_BORDER_COLOR, properties.getSceneBorderColor().toString());

        if (!Files.exists(PROPERTY_FILE_PATH.getParent())) {

            try {

                Files.createDirectories(PROPERTY_FILE_PATH.getParent());

            } catch (IOException e) {

                e.printStackTrace();
                return;

            }

        }

        try (OutputStream stream = Files.newOutputStream(PROPERTY_FILE_PATH)) {

            p.storeToXML(stream, "", StandardCharsets.UTF_8);

        } catch (IOException e) {

            e.printStackTrace();

        }
    }

    public void load() {
        if (!isSupported) {
            System.out.println("property manager unsupported.");
            return;
        }

        if (!Files.exists(PROPERTY_FILE_PATH)) {
            return;
        }

        Properties p = new Properties();

        try (InputStream stream = Files.newInputStream(PROPERTY_FILE_PATH)) {

            p.loadFromXML(stream);

            int displayHashCode = Integer.parseInt(p.getProperty(PROPERTY_DISPLAY));
            double minX = Double.parseDouble(p.getProperty(PROPERTY_MIN_X));
            double minY = Double.parseDouble(p.getProperty(PROPERTY_MIN_Y));
            double width = Double.parseDouble(p.getProperty(PROPERTY_WIDTH));
            double height = Double.parseDouble(p.getProperty(PROPERTY_HEIGHT));
            double edgeWidth = Double.parseDouble(p.getProperty(PROPERTY_EDGE_WIDTH));
            Color paintColor = Color.valueOf(p.getProperty(PROPERTY_PAINT_COLOR));
            Color gridColor = Color.valueOf(p.getProperty(PROPERTY_GRID_COLOR));
            Color edgeColor = Color.valueOf(p.getProperty(PROPERTY_EDGE_COLOR));
            Color labelColor = Color.valueOf(p.getProperty(PROPERTY_LABEL_COLOR));
            Color clickPointColor = Color.valueOf(p.getProperty(PROPERTY_CLICK_POINT_COLOR));
            Color sceneBorderColor = Color.valueOf(p.getProperty(PROPERTY_SCENE_BORDER_COLOR));

            Screen.getScreens().stream()
                    .filter(s -> s.hashCode() == displayHashCode)
                    .findFirst()
                    .ifPresent(properties::setScreen);
            properties.setMinX(minX);
            properties.setMinY(minY);
            properties.setWidth(width);
            properties.setHeight(height);
            properties.setEdgeWidth(edgeWidth);
            properties.setPaintColor(paintColor);
            properties.setGridColor(gridColor);
            properties.setEdgeColor(edgeColor);
            properties.setLabelColor(labelColor);
            properties.setClickPointColor(clickPointColor);
            properties.setSceneBorderColor(sceneBorderColor);

        } catch (IOException e) {

            throw new RuntimeException(e);

        }
    }

}
