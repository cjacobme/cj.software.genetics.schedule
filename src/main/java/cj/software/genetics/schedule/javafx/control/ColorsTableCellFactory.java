package cj.software.genetics.schedule.javafx.control;

import cj.software.genetics.schedule.entity.setupfx.ColorPair;
import cj.software.genetics.schedule.entity.setupfx.PriorityFx;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class ColorsTableCellFactory implements Callback<TableColumn<PriorityFx, ColorPair>, TableCell<PriorityFx, ColorPair>> {

    public ColorsTableCellFactory() {
        // default constructor
    }

    @Override
    public TableCell<PriorityFx, ColorPair> call(TableColumn<PriorityFx, ColorPair> priorityFxColorPairTableColumn) {
        TableCell<PriorityFx, ColorPair> result = new TableCell<>() {
            @Override
            protected void updateItem(ColorPair colorPair, boolean empty) {
                super.updateItem(colorPair, empty);
                if (!empty) {
                    Button button = new Button("sample");
                    ColorPair retrieved = getTableView().getItems().get(getIndex()).getColors();
                    Color background = retrieved.getBackground();
                    Color foreground = retrieved.getForeground();
                    String foregroundStr = ColorsTableCellFactory.this.normalize(foreground);
                    String backgroundStr = ColorsTableCellFactory.this.normalize(background);
                    String style = String.format("-fx-background-color:%s;-fx-text-fill:%s;", backgroundStr, foregroundStr);
                    button.setStyle(style);
                    setGraphic(button);
                    setText(null);
                } else {
                    setGraphic(null);
                    setText(null);
                }
            }
        };
        return result;
    }

    private String normalize(Color source) {
        String result = source.toString();
        if (result.startsWith("0x")) {
            result = result.substring("0x".length());
        }
        if (!result.startsWith("#")) {
            result = "#" + result;
        }
        return result;
    }
}
