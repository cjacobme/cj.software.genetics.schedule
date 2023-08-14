package cj.software.genetics.schedule.javafx;

import cj.software.genetics.schedule.entity.ProblemSetup;
import cj.software.genetics.schedule.entity.Solution;
import cj.software.genetics.schedule.entity.Task;
import cj.software.genetics.schedule.javafx.control.SolutionControl;
import cj.software.genetics.schedule.util.SolutionService;
import cj.software.genetics.schedule.util.TaskService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.stage.Window;
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
@FxmlView("Scheduling.fxml")
public class SchedulingController implements Initializable {

    private final Logger logger = LogManager.getFormatterLogger();

    private SolutionControl solutionControl;

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @FXML
    private ScrollPane scrollPane;

    @Autowired
    private SolutionService solutionService;

    @Autowired
    private TaskService taskService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        solutionControl = new SolutionControl();
        scrollPane.setContent(solutionControl);
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
            ProblemSetup problemSetup = optProblemSetup.get();
            List<Task> allTasks = createAllTasks(problemSetup);
            List<Solution> allSolutions = solutionService.createInitialPopulation(
                    problemSetup.getNumSolutions(),
                    problemSetup.getNumWorkers(),
                    problemSetup.getNumSlots(),
                    allTasks);
        } else {
            logger.info("dialog was cancelled");
        }
    }

    @FXML
    public void exitApplication() {
        logger.info("exiting now...");
        Platform.exit();
    }
}
