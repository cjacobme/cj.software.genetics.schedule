package cj.software.genetics.schedule.javafx;

import cj.software.genetics.schedule.entity.BreedingStepEvent;
import cj.software.genetics.schedule.entity.ProblemSetup;
import cj.software.genetics.schedule.entity.Solution;
import cj.software.genetics.schedule.entity.Task;
import cj.software.genetics.schedule.javafx.control.SolutionControl;
import cj.software.genetics.schedule.util.Breeder;
import cj.software.genetics.schedule.util.SolutionService;
import cj.software.genetics.schedule.util.TaskService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

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

    private ProblemSetup problemSetup;

    private List<Solution> population;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SolutionControl solutionControl = new SolutionControl();
        scrollPane.setContent(solutionControl);

        tcolCycle.setCellValueFactory(new PropertyValueFactory<>("cycleCounter"));
        tcolDuration.setCellValueFactory(new PropertyValueFactory<>("durationInSeconds"));
        tblSolutions.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) ->
                solutionControl.setSolution(newValue));
        spNumCycles.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000, 100));
    }

    private List<Task> createAllTasks(ProblemSetup problemSetup) {
        int num10 = problemSetup.getNumTasks10();
        int num20 = problemSetup.getNumTasks20();
        int num50 = problemSetup.getNumTasks50();
        int num100 = problemSetup.getNumTasks100();
        int total = num10 + num20 + num50 + num100;
        List<Task> result = new ArrayList<>(total);
        int startIndex = 0;
        result.addAll(taskService.createTasks(startIndex, 10, num10));
        startIndex += num10;
        result.addAll(taskService.createTasks(startIndex, 20, num20));
        startIndex += num20;
        result.addAll(taskService.createTasks(startIndex, 50, num50));
        startIndex += num50;
        result.addAll(taskService.createTasks(startIndex, 100, num100));
        return result;
    }

    @FXML
    public void newProblem() {
        Window owner = Window.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
        NewProblemDialog newProblemDialog = new NewProblemDialog(applicationContext, owner);
        Optional<ProblemSetup> optProblemSetup = newProblemDialog.showAndWait();
        if (optProblemSetup.isPresent()) {
            logger.info("a new problem was defined");
            problemSetup = optProblemSetup.get();
            List<Task> allTasks = createAllTasks(problemSetup);
            List<Solution> allSolutions = solutionService.createInitialPopulation(
                    problemSetup.getNumSolutions(),
                    problemSetup.getNumWorkers(),
                    problemSetup.getNumSlots(),
                    allTasks);
            setPopulation(allSolutions);
        } else {
            logger.info("dialog was cancelled");
        }
    }

    private void setPopulation(List<Solution> solutions) {
        population = solutions;
        ObservableList<Solution> tableData = FXCollections.observableList(solutions);
        tblSolutions.setItems(tableData);
        if (!solutions.isEmpty()) {
            tblSolutions.getSelectionModel().select(0);
        }
        int cycleCounter = problemSetup.getCycleCounter();
        String formatted = String.format("%d", cycleCounter);
        tfCycleNo.setText(formatted);
    }

    private void setPopulation(List<Solution> solutions, int cycleCounter) {
        problemSetup.setCycleCounter(cycleCounter);
        Platform.runLater(() -> setPopulation(solutions));
    }

    @FXML
    public void exitApplication() {
        logger.info("exiting now...");
        Platform.exit();
    }

    @FXML
    public void singleStep() {
        int cycleCounter = problemSetup.incCycleCounter();
        int elitismCount = problemSetup.getElitismCount();
        int tournamentSize = problemSetup.getTournamentSize();
        int numWorkers = problemSetup.getNumWorkers();
        int numSlots = problemSetup.getNumSlots();
        List<Solution> newPopulation =
                breeder.step(cycleCounter, population, elitismCount, tournamentSize, numWorkers, numSlots);
        setPopulation(newPopulation);
    }

    @FXML
    public void multipleSteps() {
        int initialCycleCounter = problemSetup.getCycleCounter();
        int numSteps = spNumCycles.getValue();
        int elitismCount = problemSetup.getElitismCount();
        int tournamentSize = problemSetup.getTournamentSize();
        int numWorkers = problemSetup.getNumWorkers();
        int numSlots = problemSetup.getNumSlots();
        Thread thread = new Thread(() -> breeder.multipleSteps(
                initialCycleCounter,
                numSteps,
                SchedulingController.this.population,
                elitismCount,
                tournamentSize,
                numWorkers,
                numSlots), "multiple steps");
        thread.start();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onApplicationEvent(BreedingStepEvent event) {
        int cycleCounter = event.getCounter();
        List<Solution> solutions = (List) event.getSource();
        setPopulation(solutions, cycleCounter);
    }
}
