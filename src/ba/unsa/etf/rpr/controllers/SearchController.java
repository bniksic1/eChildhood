package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.utils.ImageURL;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

public class SearchController {
    private ResourceBundle resourceBundle;
    private Circle circle;

    @FXML
    private JFXTextField fldSearch;

    @FXML
    private TilePane paneTile;

    @FXML
    private ScrollPane scrollPane;

    public SearchController(ResourceBundle resourceBundle, Circle circle) {
        this.resourceBundle = resourceBundle;
        this.circle = circle;
    }

    @FXML
    public void initialize(){
        scrollPane.widthProperty().addListener((obs, oldV, newV)->{
            paneTile.setPrefWidth((Double) newV);
        });

        scrollPane.heightProperty().addListener((obs, oldV, newV) -> {
            paneTile.setPrefHeight((Double) newV);
        });
    }

    public void searchAction(ActionEvent actionEvent) throws MalformedURLException {
        paneTile.getChildren().clear();
        URL url = new URL("http://api.giphy.com/v1/gifs/search?api_key=0yHwjKcMYyIL1fTIxsKRT6iFGJMqCMp3&q=" + fldSearch.getText().replace(" ", "+") + "&limit=25");

        new Thread(() -> {
            BufferedReader ulaz = null;
            try {
                ulaz = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
            String json = "", line = null;
            while (true) {
                try {
                    assert ulaz != null;
                    if (!((line = ulaz.readLine()) != null)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                json = json + line;
            }

            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String urlSlike = "https://i.giphy.com/media/" + object.getString("id") + "/giphy_s.gif";
                System.out.println(urlSlike);

                ImageView load = new ImageView(new Image(new File("resources/img/loading.gif").toURI().toString()));
                load.setFitWidth(128);
                load.setFitHeight(128);
                Button btnLoad = new Button("", load);
                btnLoad.setStyle("-fx-background-color: #57b0cd");
                Platform.runLater(() -> {
                    paneTile.getChildren().add(btnLoad);
                });
                ImageURL imageURL = new ImageURL(urlSlike);
                ImageView imageView = new ImageView(imageURL);
                imageView.setFitWidth(128);
                imageView.setFitHeight(128);
                Button button = new Button("", imageView);
                button.setOnAction(e->{
                    circle.setFill(new ImagePattern(new Image(imageURL.getURL())));
                    fldSearch.getScene().getWindow().hide();
                });
                Platform.runLater(()->{
                    paneTile.getChildren().remove(paneTile.getChildren().size() - 1);
                    paneTile.getChildren().add(button);
                });
            }
        }).start();
    }

    public void cancelAction(ActionEvent actionEvent){
        fldSearch.getScene().getWindow().hide();
    }

    public void defaultAction(ActionEvent actionEvent){
        circle.setFill(new ImagePattern(new Image("https://moonvillageassociation.org/wp-content/uploads/2018/06/default-profile-picture1.jpg")));
        scrollPane.getScene().getWindow().hide();
    }

    public void onEnter(ActionEvent actionEvent) throws MalformedURLException {
        searchAction(actionEvent);
    }
}
