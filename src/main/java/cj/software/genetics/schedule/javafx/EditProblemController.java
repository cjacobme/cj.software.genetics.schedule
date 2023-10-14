package cj.software.genetics.schedule.javafx;

import cj.software.genetics.schedule.entity.setupfx.ColorPair;
import cj.software.genetics.schedule.entity.setupfx.PriorityFx;
import cj.software.genetics.schedule.entity.setupfx.TasksFx;
import cj.software.genetics.schedule.javafx.control.ColorsTableCellFactory;
import cj.software.genetics.schedule.javafx.control.TasksSubTableCellFactory;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView("EditProblem.fxml")
public class EditProblemController implements Initializable {

    @FXML
    private Button btnAdd;

    @FXML
    private TableView<PriorityFx> tblPriorities;

    @FXML
    private TableColumn<PriorityFx, Integer> tcolPriority;

    @FXML
    private TableColumn<PriorityFx, ColorPair> tcolColor;

    @FXML
    private TableColumn<PriorityFx, ObservableList<TasksFx>> tcolTasks;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnEdit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tcolPriority.setCellValueFactory(new PropertyValueFactory<>("value"));
        tcolColor.setCellFactory(new ColorsTableCellFactory());
        tcolTasks.setCellFactory(new TasksSubTableCellFactory());
        btnDelete.disableProperty().bind(Bindings.isEmpty(tblPriorities.getSelectionModel().getSelectedItems()));
        btnEdit.disableProperty().bind(Bindings.isEmpty(tblPriorities.getSelectionModel().getSelectedItems()));
    }

    public void setData(ObservableList<PriorityFx> tableData) {
        tblPriorities.setItems(tableData);
    }
}
