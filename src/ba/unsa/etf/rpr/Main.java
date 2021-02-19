package ba.unsa.etf.rpr;

import ba.unsa.etf.rpr.controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ResourceBundle;

import static javafx.scene.control.PopupControl.USE_PREF_SIZE;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        ResourceBundle resourceBundle = ResourceBundle.getBundle("translate");
        LoginController loginController = new LoginController(resourceBundle);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"), resourceBundle);
        fxmlLoader.setController(loginController);
        Parent root = fxmlLoader.load();
        primaryStage.setTitle(resourceBundle.getString("loginForm"));
        primaryStage.setScene(new Scene(root, USE_PREF_SIZE, USE_PREF_SIZE));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
