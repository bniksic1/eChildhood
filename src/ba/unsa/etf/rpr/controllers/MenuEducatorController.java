package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.dao.*;
import ba.unsa.etf.rpr.dto.*;
import ba.unsa.etf.rpr.models.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
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
import java.util.ResourceBundle;

import static javafx.scene.control.PopupControl.USE_PREF_SIZE;

public class MenuEducatorController {
    private Educator educator;
    private ResourceBundle resourceBundle;
    private ChildhoodDAO childhoodDAO;
    private EducatorsDAO educatorsDAO;

    @FXML
    private Label lblWelcome;
    @FXML
    private VBox vboxPopUp;
    @FXML
    private JFXButton btnPopUp;
    @FXML
    private AnchorPane paneStage;
    @FXML
    private Circle circle;
    @FXML
    private Pane panePopUp;

    public MenuEducatorController(Educator person, ResourceBundle resourceBundle) {
        educator = person;
        this.resourceBundle = resourceBundle;
        childhoodDAO = ChildhoodDAO.getInstance();
        educatorsDAO = EducatorsDAO.getInstance();
    }

    @FXML
    public void initialize(){
        lblWelcome.setText(lblWelcome.getText() + " " + educator.getUsername());
        circle.setStroke(Paint.valueOf("#00141b"));
        circle.setStrokeWidth(2);
        Image image = new Image(educator.getAvatar());
        circle.setFill(new ImagePattern(image));

    }

    public void closeAction(ActionEvent actionEvent){
        lblWelcome.getScene().getWindow().hide();
    }

    private void switchLanguage(String lang){
        System.out.println( "Restarting app!" );
        lblWelcome.getScene().getWindow().hide();

        Stage stage1 = new Stage();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("translate_" + lang);
        MenuEducatorController menuEducatorController = new MenuEducatorController(educator, resourceBundle);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menuEducator.fxml"), resourceBundle);
        fxmlLoader.setController(menuEducatorController);
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

    public void saveAction(ActionEvent actionEvent) throws IOException {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter("txt file", "txt");
        jfc.setAcceptAllFileFilterUsed(false);
        jfc.addChoosableFileFilter(fileNameExtensionFilter);
        if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            writeFile(new File(jfc.getSelectedFile().getAbsolutePath()));
        }
    }

    private void writeFile(File file) throws IOException {
        if(file == null) return;
        ArrayList<Child> childhood = childhoodDAO.getChildhood();
        ArrayList<Educator> educators = educatorsDAO.getEducators();
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

    public void childhoodPrintAction(ActionEvent actionEvent){
        try {
            JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResource("/jrxml/childhood.jrxml").getFile());
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, childhoodDAO.getConn());
            JasperViewer.viewReport(jasperPrint, false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void educatorsPrintAction(ActionEvent actionEvent){
        try {
            JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResource("/jrxml/educators.jrxml").getFile());
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, educatorsDAO.getConn());
            JasperViewer.viewReport(jasperPrint, false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void childUpAction(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        ChildEducatorController childEducatorController = new ChildEducatorController(null, resourceBundle);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/childEducator.fxml"), resourceBundle);
        fxmlLoader.setController(childEducatorController);
        Parent root = fxmlLoader.load();
        stage.setTitle(resourceBundle.getString("childUp"));
        stage.setScene(new Scene(root, USE_PREF_SIZE, USE_PREF_SIZE));
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(btnPopUp.getScene().getWindow());
        stage.show();
    }

    public void childhoodAction(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();

        ChildhoodListController childhoodListController = new ChildhoodListController(educator, resourceBundle);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/list.fxml"), resourceBundle);
        fxmlLoader.setController(childhoodListController);
        Parent root = fxmlLoader.load();
        stage.setTitle("Childhood");
        stage.setScene(new Scene(root, USE_PREF_SIZE, USE_PREF_SIZE));
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(btnPopUp.getScene().getWindow());
        stage.show();
    }

    public void educatorsAction(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();

        EducatorsListController educatorsListController = new EducatorsListController(educator, resourceBundle);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/list.fxml"), resourceBundle);
        fxmlLoader.setController(educatorsListController);
        Parent root = fxmlLoader.load();
        stage.setTitle(resourceBundle.getString("educators"));
        stage.setScene(new Scene(root, USE_PREF_SIZE, USE_PREF_SIZE));
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(btnPopUp.getScene().getWindow());
        stage.show();
    }

    public void educatorUpAction(ActionEvent actionEvent) throws IOException {
        if(!educator.isAdmin()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(resourceBundle.getString("errorDialog"));
            alert.setHeaderText(resourceBundle.getString("attemptDenied"));
            alert.setContentText(resourceBundle.getString("noPerm"));
            alert.showAndWait();
            return;
        }
        Stage stage = new Stage();
        EducatorAdminController educatorAdminController = new EducatorAdminController(null, resourceBundle, true);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/educatorAdmin.fxml"), resourceBundle);
        fxmlLoader.setController(educatorAdminController);
        Parent root = fxmlLoader.load();
        stage.setTitle(resourceBundle.getString("educator"));
        stage.setScene(new Scene(root, USE_PREF_SIZE, USE_PREF_SIZE));
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(btnPopUp.getScene().getWindow());
        stage.show();
    }

    public void myProfileAction(ActionEvent actionEvent) throws IOException {
        if(educator.getUsername().equals("admin")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(resourceBundle.getString("errorDialog"));
            alert.setHeaderText(resourceBundle.getString("attemptDenied"));
            alert.setContentText(resourceBundle.getString("noModAdmin"));
            alert.showAndWait();
            return;
        }
        Stage stage = new Stage();
        EducatorAdminController educatorAdminController = new EducatorAdminController(educator, resourceBundle, false);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/educatorAdmin.fxml"), resourceBundle);
        fxmlLoader.setController(educatorAdminController);
        Parent root = fxmlLoader.load();
        stage.setTitle(resourceBundle.getString("myProfile"));
        stage.setScene(new Scene(root, USE_PREF_SIZE, USE_PREF_SIZE));
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(btnPopUp.getScene().getWindow());
        stage.show();
        stage.setOnHidden(e->{
            lblWelcome.setText(resourceBundle.getString("welcome") + " " + educator.getUsername());
            circle.setFill(new ImagePattern(new Image(educator.getAvatar())));
        });
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
            educator.setAvatar(((ImagePattern) circle.getFill()).getImage().getUrl());
            try {
                EducatorsDAO.getInstance().update(educator, educator.getUsername());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
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
}
