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


public class ChildhoodListController {
    private ResourceBundle resourceBundle;
    private ChildModel childModel;
    private Person person;

    public ChildhoodListController(Person person, ResourceBundle resourceBundle){
        this.resourceBundle = resourceBundle;
        this.person = person;
        childModel = new ChildModel(ChildhoodDAO.getInstance().getChildhood());
    }

    @FXML
    private ListView<Child> listView;

    @FXML
    public void initialize(){
        listView.getItems().addAll(childModel.getChildren());
        childModel.currentChildProperty().addListener((obs, oldV, newV)->{
            if(newV != null){
                if(person instanceof Educator) {
                    listView.getScene().getWindow().hide();

                    Stage stage = new Stage();
                    ChildEducatorController childEducatorController = new ChildEducatorController(newV, resourceBundle);
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/childEducator.fxml"), resourceBundle);
                    fxmlLoader.setController(childEducatorController);
                    Parent root = null;
                    try {
                        root = fxmlLoader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    stage.setTitle(resourceBundle.getString("childModify"));
                    stage.setScene(new Scene(root, USE_PREF_SIZE, USE_PREF_SIZE));
                    stage.setResizable(false);
                    stage.initModality(Modality.WINDOW_MODAL);
                    stage.initOwner(listView.getScene().getWindow());
                    stage.show();
                    stage.setOnHidden(e -> {
                        childModel = new ChildModel(ChildhoodDAO.getInstance().getChildhood());
                        listView.getItems().clear();
                        listView.getItems().addAll(childModel.getChildren());
                    });
                }
                else{
                    listView.getScene().getWindow().hide();

                    Stage stage = new Stage();
                    ChildParentController childParentController = new ChildParentController(newV);
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/childParent.fxml"), resourceBundle);
                    fxmlLoader.setController(childParentController);
                    Parent root = null;
                    try {
                        root = fxmlLoader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    stage.setTitle(resourceBundle.getString("childInfo"));
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
        childModel.setCurrentChild(listView.getSelectionModel().getSelectedItem());
    }
}
