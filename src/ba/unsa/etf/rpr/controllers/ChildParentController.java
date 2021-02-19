package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.dto.*;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class ChildParentController {
    private Child child;

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
    private JFXTextField fldPEmail;

    @FXML
    private JFXTextField fldPName;

    @FXML
    private Label lblTaken;

    @FXML
    private AnchorPane anchor;

    @FXML
    private JFXTextField fldPNumber;

    @FXML
    private JFXTextField fldEducator;

    public ChildParentController(Child child){
        this.child = child;
    }

    @FXML
    public void initialize(){
        circle.setFill(new ImagePattern(new Image(child.getAvatar())));
        circle.setStrokeWidth(3);
        fldUsername.setText(child.getUsername());
        fldName.setText(child.getName());
        fldSurname.setText(child.getSurname());
        fldLocation.setText(child.getLocation());
        fldPName.setText(child.getParentName());
        fldPEmail.setText(child.getEmail());
        fldPNumber.setText(String.valueOf(child.getParentNumber()));
        fldEducator.setText(child.getEducator().toString());
    }

}
