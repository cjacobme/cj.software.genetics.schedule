package cj.software.genetics.schedule.javafx;

import cj.software.genetics.schedule.entity.setup.Tasks;
import cj.software.genetics.schedule.entity.setupfx.ColorPair;
import cj.software.genetics.schedule.entity.setupfx.PriorityFx;
import cj.software.genetics.schedule.entity.setupfx.TasksFx;
import cj.software.genetics.schedule.javafx.control.ColorService;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import javafx.util.converter.IntegerStringConverter;
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

    @FXML
    private TextField tfPriority;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
        this.original = new PriorityFx(priorityFx);
        this.tfPriority.setText(String.format("%d", priorityFx.getValue()));
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
        TasksFx tasksFx = new TasksFx(source);
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
        int index = Integer.parseInt(tfPriority.getText());
        Color foreground = cpForeground.getValue();
        Color background = cpBackground.getValue();
        ColorPair colorPair = new ColorPair(foreground, background);
        ObservableList<TasksFx> tasks = tblTasks.getItems();
        PriorityFx result = new PriorityFx(index, colorPair, tasks);
        return result;
    }

    public PriorityFx getOriginal() {
        return original;
    }
}
