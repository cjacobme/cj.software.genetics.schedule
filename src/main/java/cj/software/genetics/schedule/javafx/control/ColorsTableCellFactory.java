package cj.software.genetics.schedule.javafx.control;

import cj.software.genetics.schedule.entity.setupfx.ColorPair;
import cj.software.genetics.schedule.entity.setupfx.PriorityFx;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class ColorsTableCellFactory implements Callback<TableColumn<PriorityFx, ColorPair>, TableCell<PriorityFx, ColorPair>> {
    private final ColorService colorService = new ColorService();

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
                    String style = colorService.constructStyle(retrieved);
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
}
