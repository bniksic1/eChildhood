package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.dao.*;
import ba.unsa.etf.rpr.dto.*;
import ba.unsa.etf.rpr.models.*;
import com.jfoenix.controls.*;
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

public class EducatorAdminController {
    private ResourceBundle resourceBundle;
    private Educator educator;
    private String oldUsername;
    private boolean modifyAdmin;

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
    private JFXTextField fldEmail;

    @FXML
    private JFXTextField fldNumber;

    @FXML
    private Label lblTaken;

    @FXML
    private AnchorPane anchor;

    @FXML
    private JFXPasswordField fldPassword;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXRadioButton radioYes;

    @FXML
    private JFXRadioButton radioNo;

    @FXML
    private Label lblAdmin;

    public EducatorAdminController(Educator educator, ResourceBundle resourceBundle, boolean modifyAdmin){
        this.resourceBundle = resourceBundle;
        this.educator = educator;
        this.modifyAdmin = modifyAdmin;
        if(educator != null) oldUsername = educator.getUsername();
    }

    @FXML
    public void initialize(){
        Image image = new Image("https://moonvillageassociation.org/wp-content/uploads/2018/06/default-profile-picture1.jpg");
        circle.setFill(new ImagePattern(image));
        circle.setStrokeWidth(3);
        if(educator != null && educator.isAdmin()) radioYes.fire();
        else radioNo.fire();
        if(!modifyAdmin){
            radioYes.setVisible(false);
            radioNo.setVisible(false);
            lblAdmin.setVisible(false);
            btnDelete.setVisible(false);
        }

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
                    if(educator != null && educator.getUsername().equals(fldUsername.getText())) return;
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
                    else if(EducatorsDAO.getInstance().exists(fldUsername.getText()) != null){
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

        fldEmail.textProperty().addListener((obs, oldV, newV)->{
            if(newV.isEmpty())
                errorField(fldEmail);
            else {
                lblTaken.setVisible(false);
                field(fldEmail);
            }
        });

        fldNumber.textProperty().addListener((obs, oldV, newV)->{
            if(newV.isEmpty())
                errorField(fldNumber);
            else {
                lblTaken.setVisible(false);
                field(fldNumber);
            }
        });

        fldNumber.focusedProperty().addListener((obs, oldV, newV)->{
            if(!newV && !fldNumber.getText().matches("[\\d]+")){
                errorField(fldNumber);
                lblTaken.setVisible(true);
                lblTaken.setText(resourceBundle.getString("numberAllowed"));
            }
        });

        if(educator != null){
            fldUsername.setText(educator.getUsername());
            fldName.setText(educator.getName());
            fldSurname.setText(educator.getSurname());
            datePicker.setValue(educator.getDate());
            fldLocation.setText(educator.getLocation());
            fldAddress.setText(educator.getAddress());
            fldEmail.setText(educator.getEmail());
            fldNumber.setText(String.valueOf(educator.getNumber()));
            fldPassword.setText(educator.getPassword());
            if(educator.isAdmin()) radioYes.fire();
            else radioNo.fire();
            circle.setFill(new ImagePattern(new Image(educator.getAvatar())));
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
                fldEmail.getText().isEmpty() || fldNumber.getText().isEmpty()) {
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
            if (fldNumber.getText().isEmpty())
                errorField(fldNumber);
            if (fldEmail.getText().isEmpty())
                errorField(fldEmail);
            lblTaken.setText(resourceBundle.getString("emptyFields"));
            lblTaken.setVisible(true);
        }
        else if(fldUsername.getStylesheets().contains("css/childError.css") || datePicker.getStylesheets().contains("css/childError.css") ||
                fldPassword.getStylesheets().contains("css/childError.css")){
            lblTaken.setText(resourceBundle.getString("wrongData"));
        }
        else{
            if(educator != null){
                educator.setAvatar(((ImagePattern) circle.getFill()).getImage().getUrl());
                educator.setUsername(fldUsername.getText());
                educator.setPassword(fldPassword.getText());
                educator.setName(fldName.getText());
                educator.setSurname(fldSurname.getText());
                educator.setDate(datePicker.getValue());
                educator.setLocation(fldLocation.getText());
                educator.setAddress(fldAddress.getText());
                educator.setEmail(fldEmail.getText());
                educator.setNumber(Long.parseLong(fldNumber.getText()));
                if(radioYes.isSelected()) educator.setAdmin(true);
                else educator.setAdmin(false);
                EducatorsDAO.getInstance().update(educator, oldUsername);
            }
            else{
                educator = new Educator(fldUsername.getText(), fldPassword.getText(), fldName.getText(), fldSurname.getText(),
                        datePicker.getValue(), fldLocation.getText(), fldAddress.getText(),
                        Long.parseLong(fldNumber.getText()), fldEmail.getText(), ((ImagePattern) circle.getFill()).getImage().getUrl(), radioYes.isSelected());
                EducatorsDAO.getInstance().insert(educator);
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
        alert.setHeaderText(resourceBundle.getString("delEd"));
        alert.setContentText(resourceBundle.getString("delEdText"));
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initOwner(fldPassword.getScene().getWindow());
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            EducatorsDAO.removeInstance();
            ChildhoodDAO.getInstance().deleteForEducator(educator.getUsername());
            ChildhoodDAO.removeInstance();
            EducatorsDAO.getInstance().delete(educator.getUsername());
            fldPassword.getScene().getWindow().hide();
        }
    }
}
