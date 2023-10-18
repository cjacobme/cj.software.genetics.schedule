package cj.software.genetics.schedule.javafx;

import cj.software.genetics.schedule.entity.setup.Priority;
import cj.software.genetics.schedule.entity.setupfx.ColorPair;
import cj.software.genetics.schedule.entity.setupfx.PriorityFx;
import cj.software.genetics.schedule.entity.setupfx.SolutionSetupFx;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Window;
import javafx.util.converter.NumberStringConverter;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Optional;
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
    private TableColumn<PriorityFx, Integer> tcolNumSlots;

    @FXML
    private TextField tfNumSolutions;

    @FXML
    private TextField tfNumWorkers;

    @FXML
    private TextField tfElitismCount;

    @FXML
    private TextField tfTournamentSize;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnEdit;

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    private SolutionSetupFx solutionSetupFx;

    private SolutionSetupFx originalSolutionsSetupFx;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tcolPriority.setCellValueFactory(new PropertyValueFactory<>("value"));
        tcolColor.setCellFactory(new ColorsTableCellFactory());
        tcolTasks.setCellFactory(new TasksSubTableCellFactory());
        tcolNumSlots.setCellValueFactory(new PropertyValueFactory<>("numSlots"));
        btnDelete.disableProperty().bind(Bindings.isEmpty(tblPriorities.getSelectionModel().getSelectedItems()));
        btnEdit.disableProperty().bind(Bindings.isEmpty(tblPriorities.getSelectionModel().getSelectedItems()));
    }

    public void setData(
            ObservableList<PriorityFx> tableData,
            SolutionSetupFx solutionSetupFx) {
        this.solutionSetupFx = solutionSetupFx;
        this.originalSolutionsSetupFx = new SolutionSetupFx(solutionSetupFx);
        NumberStringConverter numberStringConverter = new NumberStringConverter();
        Bindings.bindBidirectional(tfNumSolutions.textProperty(), solutionSetupFx.numSolutionsProperty(), numberStringConverter);
        Bindings.bindBidirectional(tfNumWorkers.textProperty(), solutionSetupFx.numWorkersProperty(), numberStringConverter);
        Bindings.bindBidirectional(tfElitismCount.textProperty(), solutionSetupFx.elitismCountProperty(), numberStringConverter);
        Bindings.bindBidirectional(tfTournamentSize.textProperty(), solutionSetupFx.tournamentSizeProperty(), numberStringConverter);
        tblPriorities.setItems(tableData);
    }

    @FXML
    public void addPriority() {
        int numRows = this.tblPriorities.getItems().size();
        Priority priority = Priority.builder().withValue(numRows).withBackground(Color.DARKGRAY).withForeground(Color.BLACK).build();
        PriorityFx priorityFx = new PriorityFx(priority);
        Window owner = Window.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
        EditPriorityDetailsDialog dialog = new EditPriorityDetailsDialog(applicationContext, owner, priorityFx);
        dialog.showAndWait();
    }

    @FXML
    public void editPriority() {
        TableView.TableViewSelectionModel<PriorityFx> selectionModel = this.tblPriorities.getSelectionModel();
        int index = selectionModel.getSelectedIndex();
        PriorityFx selectedValue = selectionModel.getSelectedItem();
        Window owner = Window.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
        EditPriorityDetailsDialog dialog = new EditPriorityDetailsDialog(applicationContext, owner, selectedValue);
        Optional<?> optional = dialog.showAndWait();
        if (optional.isPresent()) {
            PriorityFx editedValue = dialog.getResult();
            this.tblPriorities.getItems().set(index, editedValue);
        }
    }

    public SolutionSetupFx getSolutionSetupFx() {
        return solutionSetupFx;
    }

    public SolutionSetupFx getOriginalSolutionsSetupFx() {
        return originalSolutionsSetupFx;
    }
}
