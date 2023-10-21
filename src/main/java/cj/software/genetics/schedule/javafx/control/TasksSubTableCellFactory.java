package cj.software.genetics.schedule.javafx.control;

import cj.software.genetics.schedule.entity.setupfx.PriorityFx;
import cj.software.genetics.schedule.entity.setupfx.TasksFx;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class TasksSubTableCellFactory implements Callback<
        TableColumn<PriorityFx, ObservableList<TasksFx>>,
        TableCell<PriorityFx, ObservableList<TasksFx>>> {
    @Override
    public TableCell<PriorityFx, ObservableList<TasksFx>> call(
            TableColumn<PriorityFx, ObservableList<TasksFx>> priorityFxObservableListTableColumn) {
        TableCell<PriorityFx, ObservableList<TasksFx>> result = new TableCell<>() {
            @Override
            protected void updateItem(ObservableList<TasksFx> tasksFxes, boolean empty) {
                super.updateItem(tasksFxes, empty);
                if (!empty) {
                    TableView<TasksFx> tableView = new TableView<>();
                    tableView.setMaxHeight(150.0);
                    tableView.setPrefHeight(150.0);
                    ObservableList<TasksFx> entries = getTableView().getItems().get(getIndex()).getTasksList();
                    TableColumn<TasksFx, Integer> tcolDuration = new TableColumn<>("Duration / s");
                    tcolDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
                    tableView.getColumns().add(tcolDuration);
                    TableColumn<TasksFx, Integer> tcolCount = new TableColumn<>("Count");
                    tcolCount.setCellValueFactory(new PropertyValueFactory<>("count"));
                    tableView.getColumns().add(tcolCount);
                    tableView.setItems(entries);
                    setGraphic(tableView);
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
