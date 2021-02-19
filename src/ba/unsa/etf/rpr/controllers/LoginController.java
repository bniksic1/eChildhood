package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.dao.*;
import ba.unsa.etf.rpr.dto.*;
import ba.unsa.etf.rpr.models.*;
import ba.unsa.etf.rpr.dao.ChildhoodDAO;
import ba.unsa.etf.rpr.dao.EducatorsDAO;
import ba.unsa.etf.rpr.dto.Child;
import ba.unsa.etf.rpr.dto.Educator;
import ba.unsa.etf.rpr.dto.Person;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

import static javafx.scene.control.PopupControl.USE_PREF_SIZE;

public class LoginController {
    private ResourceBundle resourceBundle;
    private ChildhoodDAO childhoodDAO;
    private EducatorsDAO educatorsDAO;

    public LoginController(ResourceBundle resourceBundle){
        this.resourceBundle = resourceBundle;
        childhoodDAO = ChildhoodDAO.getInstance();
        educatorsDAO = EducatorsDAO.getInstance();
    }

    @FXML
    private JFXRadioButton radioParent;

    @FXML
    private JFXButton btnForgot;

    @FXML
    private JFXPasswordField fldPassword;

    @FXML
    private JFXButton btnLogin;

    @FXML
    private ImageView imgLoading;

    @FXML
    private JFXRadioButton radioEducator;

    @FXML
    private JFXTextField fldUsername;

    @FXML
    private ImageView imgFlag;

    @FXML
    private Label lblWrong;

    @FXML
    public void initialize(){
        System.out.println(resourceBundle.getString("lang"));

        if(resourceBundle.getString("lang").equals("en"))
            imgFlag.setImage(new Image(new File("resources/img/united-kingdom.png").toURI().toString()));
        else if(resourceBundle.getString("lang").equals("fr"))
            imgFlag.setImage(new Image(new File("resources/img/france.png").toURI().toString()));
        else if(resourceBundle.getString("lang").equals("de"))
            imgFlag.setImage(new Image(new File("resources/img/germany.png").toURI().toString()));
        else if(resourceBundle.getString("lang").equals("bs"))
            imgFlag.setImage(new Image(new File("resources/img/bosnia.png").toURI().toString()));


        fldUsername.textProperty().addListener((obs, oldV, newV)->{
            if(newV.isEmpty()){
                fldUsername.setPromptText(resourceBundle.getString("emptyField"));
                fldUsername.getStylesheets().clear();
                fldUsername.getStylesheets().add("css/loginError.css");
            }
            else{
                lblWrong.setVisible(false);
                fldUsername.setPromptText(resourceBundle.getString("enterUsername"));
                fldUsername.getStylesheets().clear();
                fldUsername.getStylesheets().add("css/login.css");
            }
        });

        fldPassword.textProperty().addListener((obs, oldV, newV)->{
            if(newV.isEmpty()){
                fldPassword.setPromptText(resourceBundle.getString("emptyField"));
                fldPassword.getStylesheets().clear();
                fldPassword.getStylesheets().add("css/loginError.css");
            }
            else{
                lblWrong.setVisible(false);
                fldPassword.setPromptText(resourceBundle.getString("enterPassword"));
                fldPassword.getStylesheets().clear();
                fldPassword.getStylesheets().add("css/login.css");
            }
        });

    }

    public void loginAction(ActionEvent actionEvent) throws SQLException, ParseException, IOException, InterruptedException {
        if(fldUsername.getText().isEmpty() && fldPassword.getText().isEmpty()){
            fldUsername.getStylesheets().clear();
            fldPassword.getStylesheets().clear();
            fldUsername.getStylesheets().add("css/loginError.css");
            fldPassword.getStylesheets().add("css/loginError.css");
            fldUsername.setPromptText(resourceBundle.getString("emptyField"));
            fldPassword.setPromptText(resourceBundle.getString("emptyField"));
        }
        else{
            Person person;
            if(radioParent.isSelected()) person = childhoodDAO.exists(fldUsername.getText(), fldPassword.getText());
            else person = educatorsDAO.getEducator(fldUsername.getText(), fldPassword.getText());

            if(person == null) lblWrong.setVisible(true);
            else {
                System.out.println("Name: " + person.getName() + ", surname: " + person.getSurname());

                Stage stage = new Stage();
                Parent root;
                FXMLLoader fxmlLoader;
                if(person instanceof Child){
                    MenuParentController menuParentController = new MenuParentController((Child) person, resourceBundle);
                    fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menuParent.fxml"), resourceBundle);
                    fxmlLoader.setController(menuParentController);
                }
                else{
                    MenuEducatorController menuEducatorController = new MenuEducatorController((Educator) person, resourceBundle);
                    fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menuEducator.fxml"), resourceBundle);
                    fxmlLoader.setController(menuEducatorController);
                }
                root = fxmlLoader.load();
                stage.setTitle(resourceBundle.getString("menu"));
                stage.setScene(new Scene(root, USE_PREF_SIZE, USE_PREF_SIZE));
                stage.setResizable(false);
                btnLogin.getScene().getWindow().hide();
                stage.show();
            }
        }
    }

    private void switchLanguage(String lang){
        System.out.println( "Restarting app!" );
        fldPassword.getScene().getWindow().hide();

        Stage stage1 = new Stage();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("translate_" + lang);
        LoginController loginController = new LoginController(resourceBundle);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"), resourceBundle);
        fxmlLoader.setController(loginController);
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage1.setTitle(resourceBundle.getString("loginForm"));
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

    private Person forgotten() throws SQLException, ParseException {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(resourceBundle.getString("usernameInput"));
        dialog.setHeaderText(resourceBundle.getString("headerInput"));
        dialog.setContentText(resourceBundle.getString("enterUsername2"));
        Optional<String> result = dialog.showAndWait();
        Person person = null;
        if(result.isPresent()) {
            String username = result.get();
            person = childhoodDAO.exists(username);
            if (person == null)
                person = educatorsDAO.getEducator(username);
            if(person == null){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle(resourceBundle.getString("errorDialog"));
                alert.setHeaderText(resourceBundle.getString("usernameError"));
                alert.setContentText(resourceBundle.getString("usernameErrorText"));
                alert.showAndWait();
            }
        }
        return person;
    }

    private void sendEmail(Person forgottenPerson){
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        //get Session
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("e.childhood00@gmail.com","echildhoodBB00");
                    }
                });
        //compose message
        try {
            MimeMessage message = new MimeMessage(session);

            message.addRecipient(Message.RecipientType.TO,new InternetAddress(forgottenPerson.getEmail()));

            message.setSubject(resourceBundle.getString("loginInformation"));

            message.setText(resourceBundle.getString("username") + ": " + forgottenPerson.getUsername() + "\n" +
                    resourceBundle.getString("password") + ": " + forgottenPerson.getPassword());
            //send message
            Transport.send(message);
            System.out.println("message sent successfully");
        } catch (MessagingException e) {throw new RuntimeException(e);}
    }

    public void forgotAction(ActionEvent actionEvent) throws SQLException, ParseException {
        Person forgottenPerson = forgotten();
        if(forgottenPerson == null) return;
        if(forgottenPerson.getUsername().equals("admin")){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle(resourceBundle.getString("errorDialog"));
            alert.setHeaderText(resourceBundle.getString("adminError"));
            alert.setContentText(resourceBundle.getString("adminErrorText"));
            alert.showAndWait();
        }
        else {
            sendEmail(forgottenPerson);
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle(resourceBundle.getString("informationDialog"));
            alert.setHeaderText(null);
            alert.setContentText(resourceBundle.getString("messageSent"));

            alert.showAndWait();
        }
    }

    public void onEnter(ActionEvent actionEvent) throws ParseException, SQLException, IOException, InterruptedException {
        loginAction(actionEvent);
    }


}
