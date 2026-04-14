package vue.commun;

import dao.impl.SalleDAO;
import javafx.application.Application;
import javafx.stage.Stage;
import modele.Salle;
import vue.auth.LoginVue;

import java.util.List;

public class MainVue extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        new LoginVue().start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
