package cj.software.genetics.schedule.javafx;

import cj.software.genetics.schedule.entity.setupfx.ColorPair;
import cj.software.genetics.schedule.entity.setupfx.PriorityFx;
import cj.software.genetics.schedule.javafx.control.ColorsTableCellFactory;
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
    private Button addNewPriority;

    @FXML
    private TableView<PriorityFx> tblPriorities;

    @FXML
    private TableColumn<PriorityFx, Integer> tcolPriority;

    @FXML
    private TableColumn<PriorityFx, ColorPair> tcolColor;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tcolPriority.setCellValueFactory(new PropertyValueFactory<>("value"));
        tcolColor.setCellFactory(new ColorsTableCellFactory());
    }

    public void setData(ObservableList<PriorityFx> tableData) {
        tblPriorities.setItems(tableData);
    }
}
