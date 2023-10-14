package cj.software.genetics.schedule.javafx;

import cj.software.genetics.schedule.entity.setup.GeneticAlgorithm;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import javafx.stage.Window;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Optional;

public class EditProblemDialog extends Dialog<GeneticAlgorithm> {
    public EditProblemDialog(ConfigurableApplicationContext context, Window owner) {
        FxWeaver fxWeaver = context.getBean(FxWeaver.class);
        FxControllerAndView<EditProblemController, DialogPane> controllerAndView =
                fxWeaver.load(EditProblemController.class);
        Optional<DialogPane> optional = controllerAndView.getView();
        if (optional.isPresent()) {
            initOwner(owner);
            initModality(Modality.APPLICATION_MODAL);
            DialogPane dialogPane = optional.get();
            setDialogPane(dialogPane);
            setResultConverter(buttonType -> {
                GeneticAlgorithm result;
                ButtonBar.ButtonData buttonData = buttonType.getButtonData();
                if (buttonData.isDefaultButton()) {
                    final EditProblemController controller = controllerAndView.getController();
                    //TODO obtain values from controller
                    result = GeneticAlgorithm.builder().build();
                } else {
                    result = null;
                }
                return result;
            });
        }
    }
}