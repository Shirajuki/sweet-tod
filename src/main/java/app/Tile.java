package app;

import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Tile extends StackPane {
    private final int TILE_SIZE = 18;
    private Text text = new Text();
    private Rectangle border = new Rectangle(TILE_SIZE, TILE_SIZE);

    public Tile(String tileType, Color c, Color bg) {
        // border.setStroke(Color.WHITE);
        this.createTile(tileType, c, bg);
        text.setFont(Font.font(TILE_SIZE));
        // text.getStyleClass().add("monospace");
    }

    public Tile(String tileType, Color c, Color bg, boolean monospaced) {
        this.createTile(tileType, c, bg);
        text.getStyleClass().add("monospace");
    }

    public void createTile(String tileType, Color c, Color bg) {
        border.setFill(bg);
        setAlignment(Pos.CENTER);
        getChildren().addAll(border, text);
        text.setFill(c);
        text.setText(tileType);
    }

    private boolean visited = false;

    public boolean visited() {
        return visited;
    }

    public void setVisited(boolean bool) {
        visited = bool;
    }

    public void changeSize(int width, int height) {
        border.setWidth(width);
        border.setHeight(height);
    }

    public void colorUpdate(Color c, Color bg) {
        text.setFill(c);
        border.setFill(bg);
    }

    public void updateTile(String tiletype, Color c, Color bg) {
        border.setFill(bg);
        text.setFill(c);
        text.setText(tiletype);
    }

    public void updateText(List<String> textListed) {
        String txt = "";
        for (int i = 0; i < textListed.size(); i++) {
            String s = textListed.get(i);
            txt += s + "\n";
        }
        text.setText(txt);
    }

    public void updateTextString(String txt) {
        text.setText(txt);
    }

    public void updateTilePos(int translateX, int translateY, Pos alignment) {
        setTranslateX(translateX);
        setTranslateY(translateY);
        setAlignment(alignment);
    }

}
