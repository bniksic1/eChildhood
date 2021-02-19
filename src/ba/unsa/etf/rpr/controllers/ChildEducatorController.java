package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.dao.*;
import ba.unsa.etf.rpr.dto.*;
import ba.unsa.etf.rpr.models.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

import static javafx.scene.control.PopupControl.USE_PREF_SIZE;

public class ChildEducatorController {
    private ResourceBundle resourceBundle;
    private Child child;
    private String oldUsername;
    private EducatorModel educatorModel;

    @FXML
    private JFXTextField fldUsername;
    @FXML
    private Circle circle;
    @FXML
    private JFXTextField fldName;

    @FXML
    private JFXTextField fldSurname;

    @FXML
    private DatePicker datePicker;

    @FXML
    private JFXTextField fldAddress;

    @FXML
    private JFXTextField fldLocation;

    @FXML
    private JFXTextField fldPEmail;

    @FXML
    private JFXTextField fldPNumber;

    @FXML
    private JFXTextField fldPName;

    @FXML
    private Label lblTaken;

    @FXML
    private AnchorPane anchor;

    @FXML
    private JFXPasswordField fldPassword;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private ChoiceBox<Educator> choiceEducator;

    public ChildEducatorController(Child child, ResourceBundle resourceBundle){
        this.resourceBundle = resourceBundle;
        this.child = child;
        if(child != null) oldUsername = child.getUsername();
        educatorModel = new EducatorModel(EducatorsDAO.getInstance().getEducators());
        educatorModel.setCurrentEducator(educatorModel.getEducators().get(0));
    }

    @FXML
    public void initialize(){
        Image image = new Image("https://moonvillageassociation.org/wp-content/uploads/2018/06/default-profile-picture1.jpg");
        circle.setFill(new ImagePattern(image));
        circle.setStrokeWidth(3);
        choiceEducator.setItems(educatorModel.getEducators());
        choiceEducator.getSelectionModel().select(0);

        choiceEducator.selectionModelProperty().addListener((obs, oldV, newV)->{
            educatorModel.setCurrentEducator(newV.getSelectedItem());
        });

        fldUsername.textProperty().addListener((obs, oldV, newV)->{
            if(newV.isEmpty()) {
                errorField(fldUsername);
            }
            else {
                lblTaken.setVisible(false);
                field(fldUsername);
            }
        });

        fldUsername.focusedProperty().addListener((obs, oldV, newV)->{
            if(!newV && !fldUsername.getText().isEmpty()){
                try {
                    if(child != null && child.getUsername().equals(fldUsername.getText())) return;
                    else if(fldUsername.getText().length() < 5){
                        errorField(fldUsername);
                        lblTaken.setVisible(true);
                        lblTaken.setText(resourceBundle.getString("usernameContain"));
                    }
                    else if(fldUsername.getText().equals("admin")){
                        errorField(fldUsername);
                        lblTaken.setVisible(true);
                        lblTaken.setText(resourceBundle.getString("usernameDenied"));
                    }
                    else if(ChildhoodDAO.getInstance().exists(fldUsername.getText()) != null){
                        errorField(fldUsername);
                        lblTaken.setVisible(true);
                        lblTaken.setText(resourceBundle.getString("usernameTaken"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        fldPassword.textProperty().addListener((obs, oldV, newV)->{
            if(newV.isEmpty()) {
                fldPassword.getStylesheets().clear();
                fldPassword.getStylesheets().add("css/childError.css");
            }
            else {
                lblTaken.setVisible(false);
                fldPassword.getStylesheets().clear();
                fldPassword.getStylesheets().add("css/child.css");
            }
        });

        fldPassword.focusedProperty().addListener((obs, oldV, newV)->{
            if(!newV && fldPassword.getText().length() < 8){
                fldPassword.getStylesheets().clear();
                fldPassword.getStylesheets().add("css/childError.css");
                lblTaken.setVisible(true);
                lblTaken.setText(resourceBundle.getString("passwordContain"));
            }
        });

        fldName.textProperty().addListener((obs, oldV, newV)->{
            if(newV.isEmpty())
                errorField(fldName);
            else {
                lblTaken.setVisible(false);
                field(fldName);
            }
        });

        fldSurname.textProperty().addListener((obs, oldV, newV)->{
            if(newV.isEmpty())
                errorField(fldSurname);
            else {
                lblTaken.setVisible(false);
                field(fldSurname);
            }
        });

        datePicker.valueProperty().addListener((obs, oldV, newV)->{
            if(LocalDate.now().isBefore(newV)) {
                datePicker.getStylesheets().clear();
                datePicker.getStylesheets().add("css/childError.css");
            }
            else {
                lblTaken.setVisible(false);
                datePicker.getStylesheets().clear();
                datePicker.getStylesheets().add("css/child.css");
            }
        });

        fldLocation.textProperty().addListener((obs, oldV, newV)->{
            if(newV.isEmpty())
                errorField(fldLocation);
            else{
                lblTaken.setVisible(false);
                field(fldLocation);
            }
        });

        fldAddress.textProperty().addListener((obs, oldV, newV)->{
            if(newV.isEmpty())
                errorField(fldAddress);
            else {
                lblTaken.setVisible(false);
                field(fldAddress);
            }
        });

        fldPName.textProperty().addListener((obs, oldV, newV)->{
            if(newV.isEmpty())
                errorField(fldPName);
            else {
                lblTaken.setVisible(false);
                field(fldPName);
            }
        });

        fldPEmail.textProperty().addListener((obs, oldV, newV)->{
            if(newV.isEmpty())
                errorField(fldPEmail);
            else {
                lblTaken.setVisible(false);
                field(fldPEmail);
            }
        });

        fldPNumber.textProperty().addListener((obs, oldV, newV)->{
            if(newV.isEmpty())
                errorField(fldPNumber);
            else {
                lblTaken.setVisible(false);
                field(fldPNumber);
            }
        });

        fldPNumber.focusedProperty().addListener((obs, oldV, newV)->{
            if(!newV && !fldPNumber.getText().matches("[\\d]+")){
                errorField(fldPNumber);
                lblTaken.setVisible(true);
                lblTaken.setText(resourceBundle.getString("numberAllowed"));
            }
        });

        if(child != null){
            fldUsername.setText(child.getUsername());
            fldName.setText(child.getName());
            fldSurname.setText(child.getSurname());
            datePicker.setValue(child.getDate());
            fldLocation.setText(child.getLocation());
            fldAddress.setText(child.getAddress());
            fldPName.setText(child.getParentName());
            fldPEmail.setText(child.getEmail());
            fldPNumber.setText(String.valueOf(child.getParentNumber()));
            choiceEducator.getSelectionModel().select(child.getEducator());
            fldPassword.setText(child.getPassword());
            circle.setFill(new ImagePattern(new Image(child.getAvatar())));
        }
        else btnDelete.setVisible(false);
    }

    private void errorField(JFXTextField textField){
        textField.getStylesheets().clear();
        textField.getStylesheets().add("css/childError.css");
    }

    private void field(JFXTextField textField){
        textField.getStylesheets().clear();
        textField.getStylesheets().add("css/child.css");
    }

    public void cancelAction(ActionEvent actionEvent){
        circle.getScene().getWindow().hide();
    }

    public void saveAction(ActionEvent actionEvent) throws SQLException, ParseException {
        if(fldUsername.getText().isEmpty() || fldName.getText().isEmpty() || fldSurname.getText().isEmpty() ||
                fldLocation.getText().isEmpty() || fldAddress.getText().isEmpty() ||
                fldPEmail.getText().isEmpty() || fldPName.getText().isEmpty() || fldPNumber.getText().isEmpty()) {
            if (fldUsername.getText().isEmpty())
                errorField(fldUsername);
            if (fldName.getText().isEmpty())
                errorField(fldName);
            if (fldSurname.getText().isEmpty())
                errorField(fldSurname);
            if (datePicker.getValue() == null) {
                datePicker.getStylesheets().clear();
                datePicker.getStylesheets().add("css/childError.css");
            }
            if (fldLocation.getText().isEmpty())
                errorField(fldLocation);
            if (fldAddress.getText().isEmpty())
                errorField(fldAddress);
            if (fldPNumber.getText().isEmpty())
                errorField(fldPNumber);
            if (fldPName.getText().isEmpty())
                errorField(fldPName);
            if (fldPEmail.getText().isEmpty())
                errorField(fldPEmail);
            lblTaken.setText(resourceBundle.getString("emptyFields"));
            lblTaken.setVisible(true);
        }
        else if(fldUsername.getStylesheets().contains("css/childError.css") || datePicker.getStylesheets().contains("css/childError.css") ||
                fldPassword.getStylesheets().contains("css/childError.css")){
            lblTaken.setText(resourceBundle.getString("wrongData"));
        }
        else{
            if(child != null){
                child.setAvatar(((ImagePattern) circle.getFill()).getImage().getUrl());
                child.setUsername(fldUsername.getText());
                child.setPassword(fldPassword.getText());
                child.setName(fldName.getText());
                child.setSurname(fldSurname.getText());
                child.setDate(datePicker.getValue());
                child.setLocation(fldLocation.getText());
                child.setAddress(fldAddress.getText());
                child.setParentName(fldPName.getText());
                child.setEmail(fldPEmail.getText());
                child.setParentNumber(Long.parseLong(fldPNumber.getText()));
                child.setEducator((Educator) EducatorsDAO.getInstance().getEducator(choiceEducator.getSelectionModel().getSelectedItem().getUsername()));
                EducatorsDAO.removeInstance();
                ChildhoodDAO.getInstance().update(child, oldUsername);
            }
            else{
                child = new Child(fldUsername.getText(), fldPassword.getText(), fldName.getText(), fldSurname.getText(),
                        datePicker.getValue(), fldLocation.getText(), fldAddress.getText(), fldPName.getText(),
                        fldPEmail.getText(), Long.parseLong(fldPNumber.getText()), ((ImagePattern) circle.getFill()).getImage().getUrl(), null);
                child.setEducator((Educator) EducatorsDAO.getInstance().getEducator(choiceEducator.getSelectionModel().getSelectedItem().getUsername()));
                EducatorsDAO.removeInstance();
                ChildhoodDAO.getInstance().insert(child);
            }
            fldPassword.getScene().getWindow().hide();
        }
    }

    public void changeAvatarAction(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();

        SearchController searchController = new SearchController(resourceBundle, circle);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/search.fxml"), resourceBundle);
        fxmlLoader.setController(searchController);
        Parent root = fxmlLoader.load();
        stage.setTitle(resourceBundle.getString("search"));
        stage.setScene(new Scene(root, USE_PREF_SIZE, USE_PREF_SIZE));
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(fldPassword.getScene().getWindow());
        stage.show();
    }

    public void deleteAction(ActionEvent actionEvent) throws SQLException {
        System.out.println("del");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(resourceBundle.getString("confirmDialog"));
        alert.setHeaderText(resourceBundle.getString("delConfirm"));
        alert.setContentText(resourceBundle.getString("okThis"));
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initOwner(fldPassword.getScene().getWindow());
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            EducatorsDAO.removeInstance();
            ChildhoodDAO.getInstance().delete(child.getUsername());
            fldPassword.getScene().getWindow().hide();
        }
    }
}
