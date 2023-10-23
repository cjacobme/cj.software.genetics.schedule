package cj.software.genetics.schedule.javafx;

import cj.software.genetics.schedule.entity.setup.Priority;
import cj.software.genetics.schedule.entity.setupfx.ColorPair;
import cj.software.genetics.schedule.entity.setupfx.GeneticAlgorithmFx;
import cj.software.genetics.schedule.entity.setupfx.PriorityFx;
import cj.software.genetics.schedule.entity.setupfx.SolutionSetupFx;
import cj.software.genetics.schedule.entity.setupfx.TasksFx;
import cj.software.genetics.schedule.javafx.control.ColorsTableCellFactory;
import cj.software.genetics.schedule.javafx.control.TasksSubTableCellFactory;
import cj.software.genetics.schedule.util.Converter;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
@FxmlView("EditGeneticAlgorithm.fxml")
public class EditGeneticAlgorithmController implements Initializable {

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

    @Autowired
    private Converter converter;

    private GeneticAlgorithmFx geneticAlgorithmFx;

    private GeneticAlgorithmFx originalGeneticsAlgorithmFx;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tcolPriority.setCellValueFactory(new PropertyValueFactory<>("value"));
        tcolColor.setCellFactory(new ColorsTableCellFactory());
        tcolTasks.setCellFactory(new TasksSubTableCellFactory());
        tcolNumSlots.setCellValueFactory(new PropertyValueFactory<>("numSlots"));
        btnDelete.disableProperty().bind(Bindings.isEmpty(tblPriorities.getSelectionModel().getSelectedItems()));
        btnEdit.disableProperty().bind(Bindings.isEmpty(tblPriorities.getSelectionModel().getSelectedItems()));
    }

    public void setData(GeneticAlgorithmFx geneticAlgorithmFx) {
        this.geneticAlgorithmFx = geneticAlgorithmFx;
        this.originalGeneticsAlgorithmFx = converter.copy(geneticAlgorithmFx);
        NumberStringConverter numberStringConverter = new NumberStringConverter();
        SolutionSetupFx solutionSetupFx = geneticAlgorithmFx.getSolutionsSetup();
        Bindings.bindBidirectional(tfNumSolutions.textProperty(), solutionSetupFx.numSolutionsProperty(), numberStringConverter);
        Bindings.bindBidirectional(tfNumWorkers.textProperty(), solutionSetupFx.numWorkersProperty(), numberStringConverter);
        Bindings.bindBidirectional(tfElitismCount.textProperty(), solutionSetupFx.elitismCountProperty(), numberStringConverter);
        Bindings.bindBidirectional(tfTournamentSize.textProperty(), solutionSetupFx.tournamentSizeProperty(), numberStringConverter);
        ObservableList<PriorityFx> priorities = geneticAlgorithmFx.getPriorities();
        tblPriorities.setItems(priorities);
    }

    @FXML
    public void addPriority() {
        int numRows = this.tblPriorities.getItems().size();
        Priority priority = Priority.builder()
                .withValue(numRows)
                .withBackground(Color.DARKGRAY)
                .withForeground(Color.BLACK)
                .build();
        PriorityFx priorityFx = converter.toPriorityFx(priority);
        Window owner = Window.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
        EditPriorityDetailsDialog dialog = new EditPriorityDetailsDialog(applicationContext, owner, priorityFx);
        Optional<?> optional = dialog.showAndWait();
        if (optional.isPresent()) {
            PriorityFx editedValue = dialog.getResult();
            this.tblPriorities.getItems().add(editedValue);
        }
    }

    @FXML
    public void editPriority() {
        PriorityFx selectedValue = determineSelectedPriority();
        Window owner = Window.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
        EditPriorityDetailsDialog dialog = new EditPriorityDetailsDialog(applicationContext, owner, selectedValue);
        Optional<?> optional = dialog.showAndWait();
        if (optional.isPresent()) {
            TableView.TableViewSelectionModel<PriorityFx> selectionModel = this.tblPriorities.getSelectionModel();
            int index = selectionModel.getSelectedIndex();
            PriorityFx editedValue = dialog.getResult();
            this.tblPriorities.getItems().set(index, editedValue);
        }
    }

    private PriorityFx determineSelectedPriority() {
        TableView.TableViewSelectionModel<PriorityFx> selectionModel = this.tblPriorities.getSelectionModel();
        PriorityFx result = selectionModel.getSelectedItem();
        return result;
    }

    @FXML
    public void deletePriority() {
        PriorityFx selectedPriority = determineSelectedPriority();
        String question = String.format("Do you want to delete this priority with value %d?", selectedPriority.getValue());
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, question, ButtonType.YES, ButtonType.NO);
        Optional<?> optional = alert.showAndWait();
        if (optional.isPresent()) {
            ButtonType response = alert.getResult();
            if (ButtonType.YES.equals(response)) {
                ObservableList<PriorityFx> items = tblPriorities.getItems();
                items.remove(selectedPriority);
                int priority = 0;
                for (PriorityFx priorityFx : items) {
                    priorityFx.seValue(priority);
                    priority++;
                }
            }
        }
    }

    public GeneticAlgorithmFx getGeneticAlgorithmFx() {
        return geneticAlgorithmFx;
    }

    public GeneticAlgorithmFx getOriginalGeneticsAlgorithmFx() {
        return originalGeneticsAlgorithmFx;
    }
}
