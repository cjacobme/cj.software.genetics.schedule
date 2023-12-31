package cj.software.genetics.schedule.util;

import cj.software.genetics.schedule.entity.Coordinate;
import cj.software.genetics.schedule.entity.Solution;
import cj.software.genetics.schedule.entity.Task;
import cj.software.genetics.schedule.entity.Worker;
import cj.software.genetics.schedule.entity.WorkerChain;
import cj.software.genetics.schedule.entity.setup.GeneticAlgorithm;
import cj.software.genetics.schedule.entity.setup.Priority;
import cj.software.genetics.schedule.entity.setup.SolutionSetup;
import cj.software.genetics.schedule.entity.setup.Tasks;
import cj.software.genetics.schedule.entity.setupfx.ColorPair;
import cj.software.genetics.schedule.entity.setupfx.GeneticAlgorithmFx;
import cj.software.genetics.schedule.entity.setupfx.PriorityFx;
import cj.software.genetics.schedule.entity.setupfx.SolutionSetupFx;
import cj.software.genetics.schedule.entity.setupfx.TasksFx;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

@Service
public class Converter {
    public Map<Task, Coordinate> toMapTaskCoordinate(Solution solution, Priority priority) {
        Map<Task, Coordinate> result = new HashMap<>();
        List<WorkerChain> workerChains = solution.getWorkerChains();
        int numWorkerChains = workerChains.size();
        for (int iWorkerChain = 0; iWorkerChain < numWorkerChains; iWorkerChain++) {
            WorkerChain workerChain = workerChains.get(iWorkerChain);
            Worker worker = workerChain.getWorkerForPriority(priority);
            Map<Task, Integer> slots = worker.getTasksWithSlots();
            for (Map.Entry<Task, Integer> entry : slots.entrySet()) {
                Task task = entry.getKey();
                int slotIndex = entry.getValue();
                Coordinate coordinate = Coordinate.builder().withWorkerIndex(iWorkerChain).withSlotIndex(slotIndex).build();
                result.put(task, coordinate);
            }
        }
        return result;
    }

    public GeneticAlgorithm toGeneticAlgorithm(GeneticAlgorithmFx source) {
        SolutionSetup solutionSetup = toSolutionSetup(source.getSolutionsSetup());
        List<Priority> priorities = toPriorities(source.getPriorities());
        GeneticAlgorithm result = GeneticAlgorithm.builder()
                .withSolutionSetup(solutionSetup)
                .withPriorities(priorities)
                .build();
        return result;
    }

    public List<Priority> toPriorities(ObservableList<PriorityFx> source) {
        List<Priority> result = new ArrayList<>();
        for (PriorityFx priorityFx : source) {
            Priority converted = toPriority(priorityFx);
            result.add(converted);
        }
        return result;
    }

    public Priority toPriority(PriorityFx source) {
        int value = source.getValue();
        int numSlots = source.getNumSlots();
        ColorPair colors = source.getColors();
        ObservableList<TasksFx> tasksFx = source.getTasksList();
        Color background = colors.getBackground();
        Color foreground = colors.getForeground();
        List<Tasks> tasks = toTasks(tasksFx);

        Priority result = Priority.builder()
                .withValue(value)
                .withNumSlots(numSlots)
                .withForeground(foreground)
                .withBackground(background)
                .withTasks(tasks)
                .build();
        return result;
    }

    public List<Tasks> toTasks(ObservableList<TasksFx> source) {
        List<Tasks> result = new ArrayList<>();
        for (TasksFx tasksFx : source) {
            Tasks converted = toTasks(tasksFx);
            result.add(converted);
        }
        result.sort((o1, o2) -> {
            CompareToBuilder builder = new CompareToBuilder()
                    .append(o1.getDurationSeconds(), o2.getDurationSeconds());
            int result1 = builder.build();
            return result1;
        });
        return result;
    }

    public Tasks toTasks(TasksFx source) {
        int duration = source.getDuration();
        int count = source.getCount();
        Tasks result = Tasks.builder().withDurationSeconds(duration).withNumberTasks(count).build();
        return result;
    }

    public SolutionSetup toSolutionSetup(SolutionSetupFx source) {
        int numSolutions = source.getNumSolutions();
        int numWorkers = source.getNumWorkers();
        int elitismCount = source.getElitismCount();
        int tournamentSize = source.getTournamentSize();
        SolutionSetup result = SolutionSetup.builder()
                .withNumSolutions(numSolutions)
                .withNumWorkers(numWorkers)
                .withElitismCount(elitismCount)
                .withTournamentSize(tournamentSize)
                .build();
        return result;
    }

    public List<Task> toTaskList(Solution solution, Priority priority) {
        List<Task> result = new ArrayList<>();
        List<WorkerChain> workerChains = solution.getWorkerChains();
        for (WorkerChain workerChain : workerChains) {
            Worker worker = workerChain.getWorkerForPriority(priority);
            List<Task> workerTasks = worker.getTasks();
            result.addAll(workerTasks);
        }
        result.sort((task1, task2) -> {
            CompareToBuilder builder = new CompareToBuilder()
                    .append(task1.getIdentifier(), task2.getIdentifier());
            int result1 = builder.build();
            return result1;
        });
        return result;
    }

    public ObservableList<PriorityFx> toPriorityFx(SortedSet<Priority> source) {
        ObservableList<PriorityFx> result = FXCollections.observableArrayList();
        for (Priority priority : source) {
            PriorityFx converted = toPriorityFx(priority);
            result.add(converted);
        }
        return result;
    }

    public PriorityFx toPriorityFx(Priority source) {
        int priorityValue = source.getValue();
        Integer numSlotsWrapper = source.getNumSlots();
        int numSlots = (numSlotsWrapper != null ? numSlotsWrapper : 0);
        Color foreground = source.getForeground();
        Color background = source.getBackground();
        ColorPair colorPair = new ColorPair(foreground, background);
        ObservableList<TasksFx> tasksList = FXCollections.observableArrayList();
        for (Tasks tasks : source.getTasks()) {
            TasksFx tasksFx = toTasksFx(tasks);
            tasksList.add(tasksFx);
        }

        PriorityFx result = new PriorityFx(priorityValue, numSlots, colorPair, tasksList);
        return result;
    }

    public TasksFx toTasksFx(Tasks source) {
        int duration = source.getDurationSeconds();
        int count = source.getNumberTasks();
        TasksFx result = new TasksFx(duration, count);
        return result;
    }

    public SolutionSetupFx toSolutionSetupFx(SolutionSetup source) {
        int numWorkers = source.getNumWorkers();
        int numSolutions = source.getNumSolutions();
        int elitismCount = source.getElitismCount();
        int tournamentSize = source.getTournamentSize();
        SolutionSetupFx result = new SolutionSetupFx(numSolutions, numWorkers, elitismCount, tournamentSize);
        return result;
    }

    public GeneticAlgorithmFx toGeneticAlgorithmFx(GeneticAlgorithm source) {
        SolutionSetupFx solutionSetupFx = toSolutionSetupFx(source.getSolutionSetup());
        ObservableList<PriorityFx> priorities = toPriorityFx(source.getPriorities());
        GeneticAlgorithmFx result = new GeneticAlgorithmFx(solutionSetupFx, priorities);
        return result;
    }

    public GeneticAlgorithmFx copy(GeneticAlgorithmFx source) {
        SolutionSetupFx solutionSetupFx = copy(source.getSolutionsSetup());
        ObservableList<PriorityFx> priorities = copy(source.getPriorities());
        GeneticAlgorithmFx result = new GeneticAlgorithmFx(solutionSetupFx, priorities);
        return result;
    }

    public ObservableList<PriorityFx> copy(ObservableList<PriorityFx> source) {
        ObservableList<PriorityFx> result = FXCollections.observableArrayList();
        for (PriorityFx priorityFx : source) {
            PriorityFx copied = copy(priorityFx);
            result.add(copied);
        }
        return result;
    }

    public SolutionSetupFx copy(SolutionSetupFx source) {
        int numSolutions = source.getNumSolutions();
        int numWorkers = source.getNumWorkers();
        int elitismCount = source.getElitismCount();
        int tournamentSize = source.getTournamentSize();

        SolutionSetupFx result = new SolutionSetupFx(numSolutions, numWorkers, elitismCount, tournamentSize);
        return result;
    }

    public PriorityFx copy(PriorityFx source) {
        int value = source.getValue();
        int numSlots = source.getNumSlots();
        ColorPair colorPair = source.getColors();
        ObservableList<TasksFx> tasks = source.getTasksList();

        ColorPair copyColorPair = copy(colorPair);
        ObservableList<TasksFx> copyTasks = FXCollections.observableArrayList();
        for (TasksFx tasksSource : tasks) {
            TasksFx copied = copy(tasksSource);
            copyTasks.add(copied);
        }

        PriorityFx result = new PriorityFx(value, numSlots, copyColorPair, copyTasks);
        return result;
    }

    public ColorPair copy(ColorPair source) {
        Color foreground = source.getForeground();
        Color background = source.getBackground();
        ColorPair result = new ColorPair(foreground, background);
        return result;
    }

    public TasksFx copy(TasksFx source) {
        int duration = source.getDuration();
        int count = source.getCount();
        TasksFx result = new TasksFx(duration, count);
        return result;
    }
}
