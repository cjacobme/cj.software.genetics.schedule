package cj.software.genetics.schedule.javafx;

import cj.software.genetics.schedule.entity.ProblemSetup;
import cj.software.genetics.schedule.entity.Solution;
import cj.software.genetics.schedule.entity.Task;
import cj.software.genetics.schedule.javafx.control.SolutionControl;
import cj.software.genetics.schedule.util.SolutionService;
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

    private ProblemSetup problemSetup;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        solutionControl = new SolutionControl();
        scrollPane.setContent(solutionControl);
    }

    @FXML
    public void createSolution() {
        List<Task> tasks = createTasks();
        Solution solution = solutionService.createInitialSoluation(4, 50, tasks);
        solutionControl.setSolution(solution);
    }

    private List<Task> createTasks() {
        List<Task> result = new ArrayList<>();
        result.addAll(createTasks(10, 20, 0));
        result.addAll(createTasks(20, 5, 10));
        result.addAll(createTasks(2, 100, 30));
        return result;
    }

    private List<Task> createTasks(int count, int duration, int startingIndex) {
        int index = startingIndex;
        List<Task> result = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            Task task = Task.builder().withIdentifier(index++).withDurationSeconds(duration).build();
            result.add(task);
        }
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
