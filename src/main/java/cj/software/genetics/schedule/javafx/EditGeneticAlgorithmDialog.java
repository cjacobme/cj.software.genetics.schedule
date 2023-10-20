package cj.software.genetics.schedule.javafx;

import cj.software.genetics.schedule.entity.setup.GeneticAlgorithm;
import cj.software.genetics.schedule.entity.setupfx.GeneticAlgorithmFx;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import javafx.stage.Window;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Optional;

public class EditGeneticAlgorithmDialog extends Dialog<GeneticAlgorithm> {
    public EditGeneticAlgorithmDialog(
            ConfigurableApplicationContext context,
            Window owner,
            GeneticAlgorithmFx geneticAlgorithmFx) {
        FxWeaver fxWeaver = context.getBean(FxWeaver.class);
        FxControllerAndView<EditGeneticAlgorithmController, DialogPane> controllerAndView =
                fxWeaver.load(EditGeneticAlgorithmController.class);
        Optional<DialogPane> optional = controllerAndView.getView();
        if (optional.isPresent()) {
            final EditGeneticAlgorithmController controller = controllerAndView.getController();
            controller.setData(geneticAlgorithmFx);
            initOwner(owner);
            initModality(Modality.APPLICATION_MODAL);
            DialogPane dialogPane = optional.get();
            setDialogPane(dialogPane);
            setResultConverter(buttonType -> {
                GeneticAlgorithm result;
                ButtonBar.ButtonData buttonData = buttonType.getButtonData();
                if (buttonData.isDefaultButton()) {
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
