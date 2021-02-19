package ba.unsa.etf.rpr.controllers;

import com.jfoenix.controls.JFXPopup;

import ba.unsa.etf.rpr.dao.*;
import ba.unsa.etf.rpr.dto.*;
import ba.unsa.etf.rpr.models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.io.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import static javafx.scene.control.PopupControl.USE_PREF_SIZE;

public class MenuParentController {
    private Child child;
    private ResourceBundle resourceBundle;

    @FXML
    private Label lblWelcome;

    @FXML
    private Circle circle;

    @FXML
    private VBox vboxPopUp;

    @FXML
    private AnchorPane paneStage;

    public MenuParentController(Child person, ResourceBundle resourceBundle) {
        child = person;
        this.resourceBundle = resourceBundle;
    }

    @FXML
    public void initialize(){
        lblWelcome.setText(lblWelcome.getText() + " " + child.getUsername());
        circle.setStroke(Paint.valueOf("#00141b"));
        circle.setStrokeWidth(2);
        Image image = new Image(child.getAvatar());
        circle.setFill(new ImagePattern(image));
    }

    public void popUpAction(ActionEvent actionEvent){
        JFXPopup popup = new JFXPopup(vboxPopUp);
        popup.show(paneStage ,JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT, -20, 101);
        vboxPopUp.setVisible(true);
    }

    public void logOutAction(ActionEvent actionEvent) throws IOException {
        paneStage.getScene().getWindow().hide();
        Stage stage = new Stage();
        LoginController loginController = new LoginController(resourceBundle);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"), resourceBundle);
        fxmlLoader.setController(loginController);
        Parent root = fxmlLoader.load();
        stage.setTitle(resourceBundle.getString("loginForm"));
        stage.setScene(new Scene(root, USE_PREF_SIZE, USE_PREF_SIZE));
        stage.setResizable(false);
        stage.show();
    }

    public void myProfileAction(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        ChildParentController childParentController = new ChildParentController(child);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/childParent.fxml"), resourceBundle);
        fxmlLoader.setController(childParentController);
        Parent root = fxmlLoader.load();
        stage.setTitle(resourceBundle.getString("myProfile"));
        stage.setScene(new Scene(root, USE_PREF_SIZE, USE_PREF_SIZE));
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(lblWelcome.getScene().getWindow());
        stage.show();
    }

    public void changeAvatarAction(ActionEvent actionEvent) throws IOException{
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
        stage.initOwner(lblWelcome.getScene().getWindow());
        stage.show();
        stage.setOnHidden(e->{
            child.setAvatar(((ImagePattern) circle.getFill()).getImage().getUrl());
            try {
                EducatorsDAO.removeInstance();
                ChildhoodDAO.getInstance().update(child, child.getUsername());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }

    public void myChildAction(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        ChildEducatorController childEducatorController = new ChildEducatorController(child, resourceBundle);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/childEducator.fxml"), resourceBundle);
        fxmlLoader.setController(childEducatorController);
        Parent root = fxmlLoader.load();
        stage.setTitle(resourceBundle.getString("myChild"));
        stage.setScene(new Scene(root, USE_PREF_SIZE, USE_PREF_SIZE));
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(lblWelcome.getScene().getWindow());
        stage.show();
        stage.setOnHidden(e->{
            lblWelcome.setText(resourceBundle.getString("welcome") + " " + child.getUsername());
            circle.setFill(new ImagePattern(new Image(child.getAvatar())));
        });
    }

    public void educatorsAction(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();

        EducatorsListController educatorsListController = new EducatorsListController(child, resourceBundle);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/list.fxml"), resourceBundle);
        fxmlLoader.setController(educatorsListController);
        Parent root = fxmlLoader.load();
        stage.setTitle(resourceBundle.getString("educators"));
        stage.setScene(new Scene(root, USE_PREF_SIZE, USE_PREF_SIZE));
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(lblWelcome.getScene().getWindow());
        stage.show();
    }

    public void childhoodAction(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();

        ChildhoodListController childhoodListController = new ChildhoodListController(child, resourceBundle);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/list.fxml"), resourceBundle);
        fxmlLoader.setController(childhoodListController);
        Parent root = fxmlLoader.load();
        stage.setTitle("Childhood");
        stage.setScene(new Scene(root, USE_PREF_SIZE, USE_PREF_SIZE));
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(lblWelcome.getScene().getWindow());
        stage.show();
    }

    public void activityAction(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();

        ActivityController activityController = new ActivityController(resourceBundle);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/activity.fxml"), resourceBundle);
        fxmlLoader.setController(activityController);
        Parent root = fxmlLoader.load();
        stage.setTitle(resourceBundle.getString("dayActivity"));
        stage.setScene(new Scene(root, USE_PREF_SIZE, USE_PREF_SIZE));
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(lblWelcome.getScene().getWindow());
        stage.show();
    }

    public void growedAction(ActionEvent actionEvent) throws IOException, SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(resourceBundle.getString("confirmDialog"));
        File file = new File("resources/img/proud.png");
        Image image = new Image(file.toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        alert.setGraphic(imageView);
        alert.setHeaderText(resourceBundle.getString("proud"));
        alert.setContentText(resourceBundle.getString("proudText"));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            EducatorsDAO.removeInstance();
            ChildhoodDAO.getInstance().delete(child.getUsername());

            paneStage.getScene().getWindow().hide();
            Stage stage = new Stage();
            LoginController loginController = new LoginController(resourceBundle);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"), resourceBundle);
            fxmlLoader.setController(loginController);
            Parent root = fxmlLoader.load();
            stage.setTitle(resourceBundle.getString("loginForm"));
            stage.setScene(new Scene(root, USE_PREF_SIZE, USE_PREF_SIZE));
            stage.setResizable(false);
            stage.show();
        }
    }

    public void closeAction(ActionEvent actionEvent){
        lblWelcome.getScene().getWindow().hide();
    }

    private void writeFile(File file) throws IOException {
        if(file == null) return;
        ArrayList<Child> childhood = ChildhoodDAO.getInstance().getChildhood();
        ChildhoodDAO.removeInstance();
        ArrayList<Educator> educators = EducatorsDAO.getInstance().getEducators();
        EducatorsDAO.removeInstance();

        if(childhood.isEmpty() && educators.isEmpty()){
            Writer writer = new FileWriter(file);
            writer.write(resourceBundle.getString("dbEmpty"));
            writer.close();
            return;
        }
        Writer writer = new FileWriter(file);
        writer.write("----- Childhood -----\n\n");
        for(Child c : childhood)
            writer.write(resourceBundle.getString("username") + ": " + c.getUsername() + " | " + resourceBundle.getString("name") + " " + c.getName() + " | " + resourceBundle.getString("surname") + " " + c.getSurname() + " | E-mail: " + c.getEmail() + " | " + resourceBundle.getString("educator2") + " " + c.getEducator() + "\n");
        writer.write("\n\n----- " + resourceBundle.getString("educators") + " -----\n\n");
        for(Educator e : educators)
            writer.write(resourceBundle.getString("username") + ": " + e.getUsername() + " | " + resourceBundle.getString("name") + " " + e.getName() + " | " + resourceBundle.getString("surname") + " " + e.getSurname() + " | E-mail: " + e.getEmail() + " | " + resourceBundle.getString("number")+ " " + e.getNumber() + "\n");
        writer.close();
        System.out.println("Successfully written");
    }

    public void aboutAction(ActionEvent actionEvent) throws FileNotFoundException {
        System.out.println("s");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(resourceBundle.getString("about"));
        File file = new File("resources/img/eChildhood 1.png");
        Image image = new Image(file.toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        alert.setGraphic(imageView);
        alert.setHeaderText(resourceBundle.getString("aboutInformationHeader"));
        alert.setContentText(resourceBundle.getString("aboutInformation"));
        alert.showAndWait();
    }

    private void switchLanguage(String lang){
        System.out.println( "Restarting app!" );
        lblWelcome.getScene().getWindow().hide();

        Stage stage1 = new Stage();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("translate_" + lang);
        MenuParentController menuParentController = new MenuParentController(child, resourceBundle);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menuParent.fxml"), resourceBundle);
        fxmlLoader.setController(menuParentController);
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage1.setTitle(resourceBundle.getString("menu"));
        stage1.setScene(new Scene(root, USE_PREF_SIZE, USE_PREF_SIZE));
        stage1.setResizable(false);
        stage1.show();
    }

    public void enAction(ActionEvent actionEvent) {
        switchLanguage("en");
    }

    public void frAction(ActionEvent actionEvent) {
        switchLanguage("fr");
    }

    public void deAction(ActionEvent actionEvent) {
        switchLanguage("de");
    }

    public void bsAction(ActionEvent actionEvent) {
        switchLanguage("bs");
    }

    public void saveAction(ActionEvent actionEvent) throws IOException {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter("txt file", "txt");
        jfc.setAcceptAllFileFilterUsed(false);
        jfc.addChoosableFileFilter(fileNameExtensionFilter);
        if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            writeFile(new File(jfc.getSelectedFile().getAbsolutePath()));
        }
    }

    public void printChildhoodAction(ActionEvent actionEvent){
        try {
            JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResource("/jrxml/childhood.jrxml").getFile());
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, ChildhoodDAO.getInstance().getConn());
            JasperViewer.viewReport(jasperPrint, false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void printEducatorsAction(ActionEvent actionEvent){
        try {
            JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResource("/jrxml/educators.jrxml").getFile());
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, EducatorsDAO.getInstance().getConn());
            JasperViewer.viewReport(jasperPrint, false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
}
