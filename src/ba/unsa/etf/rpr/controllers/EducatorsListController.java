package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.dao.*;
import ba.unsa.etf.rpr.dto.*;
import ba.unsa.etf.rpr.models.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

import static javafx.scene.control.PopupControl.USE_PREF_SIZE;


public class EducatorsListController {
    private ResourceBundle resourceBundle;
    private EducatorModel educatorModel;
    private Person person;

    public EducatorsListController(Person educator, ResourceBundle resourceBundle){
        this.resourceBundle = resourceBundle;
        this.person = educator;
        educatorModel = new EducatorModel(EducatorsDAO.getInstance().getEducators());
    }

    @FXML
    private ListView<Educator> listView;

    @FXML
    public void initialize(){
        listView.getItems().addAll(educatorModel.getEducators());
        educatorModel.currentEducatorProperty().addListener((obs, oldV, newV)->{
            if(newV != null){
                listView.getScene().getWindow().hide();

                if(person instanceof Educator && ((Educator) person).isAdmin()) {
                    Stage stage = new Stage();
                    EducatorAdminController educatorAdminController = new EducatorAdminController(newV, resourceBundle, true);
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/educatorAdmin.fxml"), resourceBundle);
                    fxmlLoader.setController(educatorAdminController);
                    Parent root = null;
                    try {
                        root = fxmlLoader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    stage.setTitle(resourceBundle.getString("educatorModify"));
                    stage.setScene(new Scene(root, USE_PREF_SIZE, USE_PREF_SIZE));
                    stage.setResizable(false);
                    stage.initModality(Modality.WINDOW_MODAL);
                    stage.initOwner(listView.getScene().getWindow());
                    stage.show();
                    stage.setOnHidden(e -> {
                        educatorModel = new EducatorModel(EducatorsDAO.getInstance().getEducators());
                        listView.getItems().clear();
                        listView.getItems().addAll(educatorModel.getEducators());
                    });
                }
                else{
                    Stage stage = new Stage();
                    EducatorController educatorController = new EducatorController(newV);
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/educator.fxml"), resourceBundle);
                    fxmlLoader.setController(educatorController);
                    Parent root = null;
                    try {
                        root = fxmlLoader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    stage.setTitle(resourceBundle.getString("educatorInfo"));
                    stage.setScene(new Scene(root, USE_PREF_SIZE, USE_PREF_SIZE));
                    stage.setResizable(false);
                    stage.initModality(Modality.WINDOW_MODAL);
                    stage.initOwner(listView.getScene().getWindow());
                    stage.show();
                }
            }
        });
    }

    public void switchChildAction(MouseEvent mouseEvent){
        educatorModel.setCurrentEducator(listView.getSelectionModel().getSelectedItem());
    }
}
