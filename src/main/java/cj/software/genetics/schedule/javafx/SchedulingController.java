package cj.software.genetics.schedule.javafx;

import cj.software.genetics.schedule.entity.Solution;
import cj.software.genetics.schedule.entity.Task;
import cj.software.genetics.schedule.javafx.control.SolutionControl;
import cj.software.genetics.schedule.util.SolutionService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Component
@FxmlView("Scheduling.fxml")
public class SchedulingController implements Initializable {

    private SolutionControl solutionControl;

    @FXML
    private ScrollPane scrollPane;

    @Autowired
    private SolutionService solutionService;

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
}
