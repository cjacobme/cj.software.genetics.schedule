package cj.software.genetics.schedule.javafx;

import cj.software.genetics.schedule.entity.setupfx.ColorPair;
import cj.software.genetics.schedule.entity.setupfx.PriorityFx;
import cj.software.genetics.schedule.entity.setupfx.TasksFx;
import cj.software.genetics.schedule.javafx.control.ColorService;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView("EditPriorityDetails.fxml")
public class EditPriorityDetailsController implements Initializable {
    private PriorityFx priorityFx;

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
        tcolDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        tcolCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        btnDelTaskLine.disableProperty().bind(Bindings.isEmpty(tblTasks.getSelectionModel().getSelectedItems()));
    }

    public void setData(PriorityFx priorityFx) {
        this.priorityFx = priorityFx;
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

    }

    @FXML
    public void deleteTaskLine() {

    }
}
