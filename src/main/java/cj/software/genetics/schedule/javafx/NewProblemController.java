package cj.software.genetics.schedule.javafx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView("NewProblem.fxml")
public class NewProblemController implements Initializable {

    @FXML
    private Spinner<Integer> spNumSolutions;

    @FXML
    private Spinner<Integer> spNumWorkers;

    @FXML
    private Spinner<Integer> spNumSlots;

    @FXML
    private Spinner<Integer> spNum10;

    @FXML
    private Spinner<Integer> spNum20;

    @FXML
    private Spinner<Integer> spNum50;

    @FXML
    private Spinner<Integer> spNum100;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        spNumSolutions.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 2000, 100));
        spNumWorkers.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 20, 4));
        spNumSlots.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 10000, 100));
        spNum10.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 50));
        spNum20.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 40));
        spNum50.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 30));
        spNum100.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 20));
    }

    public int getNumSolutions() {
        return spNumSolutions.getValue();
    }

    public int getNumWorkers() {
        return spNumWorkers.getValue();
    }

    public int getNumSlots() {
        return spNumSlots.getValue();
    }

    public int getNumTasks10() {
        return spNum10.getValue();
    }

    public int getNumTasks20() {
        return spNum20.getValue();
    }

    public int getNumTasks50() {
        return spNum50.getValue();
    }

    public int getNumTasks100() {
        return spNum100.getValue();
    }
}
