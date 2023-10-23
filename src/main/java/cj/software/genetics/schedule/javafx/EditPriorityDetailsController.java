package cj.software.genetics.schedule.javafx;

import cj.software.genetics.schedule.entity.setup.Tasks;
import cj.software.genetics.schedule.entity.setupfx.ColorPair;
import cj.software.genetics.schedule.entity.setupfx.PriorityFx;
import cj.software.genetics.schedule.entity.setupfx.TasksFx;
import cj.software.genetics.schedule.javafx.control.ColorService;
import cj.software.genetics.schedule.util.Converter;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
@FxmlView("EditPriorityDetails.fxml")
public class EditPriorityDetailsController implements Initializable {

    private PriorityFx original;

    private PriorityFx priorityFx;

    @FXML
    private TextField tfPriority;

    @FXML
    private TextField tfNumSlots;

    @FXML
    private Button btnSample;

    @FXML
    private TableView<TasksFx> tblTasks;

    @FXML
    private TableColumn<TasksFx, Integer> tcolDuration;

    @FXML
    private TableColumn<TasksFx, Integer> tcolCount;

    @FXML
    private ColorPicker cpBackground;

    @FXML
    private ColorPicker cpForeground;

    @FXML
    private Button btnDelTaskLine;

    @Autowired
    private ColorService colorService;

    @Autowired
    private Converter converter;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TextFormatter<Integer> integerTextFormatter = new TextFormatter<>(new IntegerStringConverter());
        tfNumSlots.setTextFormatter(integerTextFormatter);
        tcolDuration.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        tcolDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        tcolDuration.setOnEditCommit(event -> {
            TasksFx tasksFx = event.getTableView().getItems().get(event.getTablePosition().getRow());
            Integer newValue = event.getNewValue();
            tasksFx.setDuration(newValue);
        });

        tcolCount.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        tcolCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        tcolCount.setOnEditCommit(event -> {
            TasksFx tasksFx = event.getTableView().getItems().get(event.getTablePosition().getRow());
            Integer newValue = event.getNewValue();
            tasksFx.setCount(newValue);
        });

        btnDelTaskLine.disableProperty().bind(Bindings.isEmpty(tblTasks.getSelectionModel().getSelectedItems()));
    }

    public void setData(PriorityFx priorityFx) {
        NumberStringConverter numberStringConverter = new NumberStringConverter();
        this.priorityFx = priorityFx;
        this.original = converter.copy(priorityFx);
        Bindings.bindBidirectional(tfNumSlots.textProperty(), priorityFx.numSlotsProperty(), numberStringConverter);
        Bindings.bindBidirectional(tfPriority.textProperty(), priorityFx.valueProperty(), numberStringConverter);
        ColorPair colorPair = priorityFx.getColors();
        if (colorPair != null) {
            String style = colorService.constructStyle(colorPair);
            btnSample.setStyle(style);
            cpBackground.setValue(colorPair.getBackground());
            cpForeground.setValue(colorPair.getForeground());
        }
        tblTasks.setItems(priorityFx.getTasksList());
    }

    @FXML
    public void colorsChanged() {
        Color background = cpBackground.getValue();
        Color foreground = cpForeground.getValue();
        ColorPair colorPair = new ColorPair(foreground, background);
        String style = colorService.constructStyle(colorPair);
        btnSample.setStyle(style);
    }

    @FXML
    public void addTasksLine() {
        Tasks source = Tasks.builder().withNumberTasks(0).withDurationSeconds(0).build();
        TasksFx tasksFx = converter.toTasksFx(source);
        ObservableList<TasksFx> tasksList = this.tblTasks.getItems();
        tasksList.add(tasksFx);
        int numTasks = tasksList.size();
        this.tblTasks.getSelectionModel().select(numTasks - 1);
    }

    @FXML
    public void deleteTaskLine() {
        TasksFx selected = this.tblTasks.getSelectionModel().getSelectedItem();
        String question = String.format("Do you want to delete this setting? duration=%d, count=%d", selected.getDuration(), selected.getCount());
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, question, ButtonType.YES, ButtonType.NO);
        Optional<?> optional = alert.showAndWait();
        if (optional.isPresent()) {
            ButtonType response = alert.getResult();
            if (ButtonType.YES.equals(response)) {
                tblTasks.getItems().remove(selected);
            }
        }
    }

    public PriorityFx getModifications() {
        int index = this.priorityFx.getValue();
        Color foreground = cpForeground.getValue();
        Color background = cpBackground.getValue();
        ColorPair colorPair = new ColorPair(foreground, background);
        ObservableList<TasksFx> tasks = tblTasks.getItems();
        int numSlots = this.priorityFx.getNumSlots();
        PriorityFx result = new PriorityFx(index, numSlots, colorPair, tasks);
        return result;
    }

    public PriorityFx getOriginal() {
        return original;
    }
}
