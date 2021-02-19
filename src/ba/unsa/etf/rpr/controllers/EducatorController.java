package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.dto.*;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;


public class EducatorController {
    private Educator educator;

    @FXML
    private JFXTextField fldUsername;
    @FXML
    private Circle circle;
    @FXML
    private JFXTextField fldName;

    @FXML
    private JFXTextField fldSurname;

    @FXML
    private JFXTextField fldLocation;

    @FXML
    private JFXTextField fldEmail;

    @FXML
    private AnchorPane anchor;

    @FXML
    private JFXTextField fldNumber;


    public EducatorController(Educator educator){
        this.educator = educator;
    }

    @FXML
    public void initialize(){
        circle.setFill(new ImagePattern(new Image(educator.getAvatar())));
        circle.setStrokeWidth(3);
        fldUsername.setText(educator.getUsername());
        fldName.setText(educator.getName());
        fldSurname.setText(educator.getSurname());
        fldLocation.setText(educator.getLocation());
        fldEmail.setText(educator.getEmail());
        fldNumber.setText(String.valueOf(educator.getNumber()));
    }

}
