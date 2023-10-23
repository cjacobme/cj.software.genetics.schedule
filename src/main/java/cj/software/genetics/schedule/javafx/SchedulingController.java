package cj.software.genetics.schedule.javafx;

import cj.software.genetics.schedule.entity.BreedingStepEvent;
import cj.software.genetics.schedule.entity.Solution;
import cj.software.genetics.schedule.entity.Task;
import cj.software.genetics.schedule.entity.setup.GeneticAlgorithm;
import cj.software.genetics.schedule.entity.setup.Priority;
import cj.software.genetics.schedule.entity.setup.SolutionSetup;
import cj.software.genetics.schedule.entity.setupfx.GeneticAlgorithmFx;
import cj.software.genetics.schedule.javafx.control.ColorService;
import cj.software.genetics.schedule.javafx.control.SolutionControl;
import cj.software.genetics.schedule.util.Breeder;
import cj.software.genetics.schedule.util.Converter;
import cj.software.genetics.schedule.util.GeneticAlgorithmService;
import cj.software.genetics.schedule.util.SolutionService;
import cj.software.genetics.schedule.util.TaskService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Window;
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.SortedMap;

@Component
@FxmlView("Scheduling.fxml")
public class SchedulingController implements Initializable, ApplicationListener<BreedingStepEvent> {

    private final Logger logger = LogManager.getFormatterLogger();

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @Autowired
    private SolutionService solutionService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private Breeder breeder;

    @Autowired
    private GeneticAlgorithmService geneticAlgorithmService;

    @Autowired
    private Converter converter;

    @Autowired
    private ColorService colorService;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TableView<Solution> tblSolutions;

    @FXML
    private TableColumn<Solution, Integer> tcolCycle;

    @FXML
    private TableColumn<Solution, String> tcolDuration;

    @FXML
    private TextField tfCycleNo;

    @FXML
    private Spinner<Integer> spNumCycles;

    @FXML
    private Slider spScale;

    @FXML
    private Label lbScale;

    @FXML
    private TextField tfStatus;

    private GeneticAlgorithm geneticAlgorithm;

    private List<Solution> population;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SolutionControl solutionControl = new SolutionControl(colorService);
        scrollPane.setContent(solutionControl);
        tfStatus.textProperty().bind(solutionControl.statusProperty());

        tcolCycle.setCellValueFactory(new PropertyValueFactory<>("cycleCounter"));
        tcolDuration.setCellValueFactory(new PropertyValueFactory<>("durationInSeconds"));
        tblSolutions.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) ->
                solutionControl.setSolution(newValue));
        spNumCycles.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 30));
        int scaleValue = spScale.valueProperty().intValue();
        lbScale.setText(String.format("%d", scaleValue));
        spScale.valueProperty().addListener((observableValue, numbOldValue, numbNewValue) -> {
            if (numbNewValue != null) {
                int newValue = numbNewValue.intValue();
                lbScale.setText(String.format("%d", newValue));
                solutionControl.setScale(newValue);
                solutionControl.setSolution(null);
                if (population != null) {
                    int selectedIndex = tblSolutions.getSelectionModel().getSelectedIndex();
                    if (selectedIndex >= 0) {
                        Solution solution = population.get(selectedIndex);
                        solutionControl.setSolution(solution);
                    }
                }
            }
        });
    }

    @FXML
    public void newProblem2() {
        Window owner = Window.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
        GeneticAlgorithm source = geneticAlgorithmService.createDefault();
        GeneticAlgorithmFx geneticAlgorithmFx = converter.toGeneticAlgorithmFx(source);
        EditGeneticAlgorithmDialog editGeneticAlgorithmDialog = new EditGeneticAlgorithmDialog(
                applicationContext, owner, geneticAlgorithmFx);
        Optional<GeneticAlgorithmFx> optional = editGeneticAlgorithmDialog.showAndWait();
        if (optional.isPresent()) {
            GeneticAlgorithmFx editedFx = optional.get();
            this.geneticAlgorithm = converter.toGeneticAlgorithm(editedFx);
            SortedMap<Priority, List<Task>> allTasks = taskService.createTasks(this.geneticAlgorithm);
            SolutionSetup solutionSetup = this.geneticAlgorithm.getSolutionSetup();
            List<Solution> allSolutions = solutionService.createInitialPopulation(solutionSetup, allTasks);
            setPopulation(allSolutions, this.geneticAlgorithm.getCycleCounter());
        } else {
            logger.info("dialog was cancelled");
        }
    }

    private void setPopulation(List<Solution> solutions, int cycleCounter) {
        population = solutions;
        ObservableList<Solution> tableData = FXCollections.observableList(solutions);
        tblSolutions.setItems(tableData);
        if (!solutions.isEmpty()) {
            tblSolutions.getSelectionModel().select(0);
        }
        String formatted = String.format("%d", cycleCounter);
        tfCycleNo.setText(formatted);
    }

    @FXML
    public void exitApplication() {
        logger.info("exiting now...");
        Platform.exit();
    }

    @FXML
    public void singleStep() {
        int cycleCounter = geneticAlgorithm.getCycleCounter();
        SolutionSetup solutionSetup = geneticAlgorithm.getSolutionSetup();
        int elitismCount = solutionSetup.getElitismCount();
        int tournamentSize = solutionSetup.getTournamentSize();
        List<Solution> newPopulation =
                breeder.step(cycleCounter, population, elitismCount, tournamentSize);
        setPopulation(newPopulation, cycleCounter);
    }

    @FXML
    public void multipleSteps() {
        int numSteps = spNumCycles.getValue();
        SolutionSetup solutionSetup = geneticAlgorithm.getSolutionSetup();
        int elitismCount = solutionSetup.getElitismCount();
        int tournamentSize = solutionSetup.getTournamentSize();
        Thread thread = new Thread(() -> breeder.multipleSteps(
                geneticAlgorithm,
                numSteps,
                SchedulingController.this.population,
                elitismCount,
                tournamentSize), "multiple steps");
        thread.start();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onApplicationEvent(BreedingStepEvent event) {
        Platform.runLater(() -> {
            int cycleCounter = event.getCounter();
            List<Solution> solutions = (List) event.getSource();
            setPopulation(solutions, cycleCounter);
        });
    }
}
