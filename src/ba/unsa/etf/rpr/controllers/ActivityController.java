package ba.unsa.etf.rpr.controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ActivityController {
    private ResourceBundle resourceBundle;
    private ArrayList<String> messages, breakfast, dinner, activity;

    @FXML
    private AnchorPane anchor;

    @FXML
    private Label lblMessage;

    @FXML
    private Label lblBreakfast;

    @FXML
    private Label lblDinner;

    @FXML
    private Label lblDateTime;

    @FXML
    private Label lblActivity;

    public ActivityController(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        messages = new ArrayList<>();
        messages.add(resourceBundle.getString("message1"));
        messages.add(resourceBundle.getString("message2"));
        messages.add(resourceBundle.getString("message3"));
        messages.add(resourceBundle.getString("message4"));
        messages.add(resourceBundle.getString("message5"));
        messages.add(resourceBundle.getString("message6"));
        messages.add(resourceBundle.getString("message7"));
        breakfast = new ArrayList<>();
        breakfast.add("Pancakes and Maple Syrup");
        breakfast.add("Eggs Benedict");
        breakfast.add("Bagel with Cream Cheese");
        breakfast.add("Belgian Style Waffles");
        breakfast.add("Bacon and Eggs");
        breakfast.add("Sausage and Egg Sandwich");
        breakfast.add("Hot Oatmeal or Porridge");
        dinner = new ArrayList<>();
        dinner.add("Beef Orzo Skillet");
        dinner.add("Easy Beef Goulash");
        dinner.add("Lazy Lasagna");
        dinner.add("Homemade Vegetable Soup");
        dinner.add("Italian Pasta Toss");
        dinner.add("Bean and Sausage Stew");
        dinner.add("Chicken with Fire-Roasted Tomatoes");
        activity = new ArrayList<>();
        activity.add(resourceBundle.getString("activity1"));
        activity.add(resourceBundle.getString("activity2"));
        activity.add(resourceBundle.getString("activity3"));
        activity.add(resourceBundle.getString("activity4"));
        activity.add(resourceBundle.getString("activity5"));
        activity.add(resourceBundle.getString("activity6"));
        activity.add(resourceBundle.getString("activity7"));
    }

    @FXML
    public void initialize(){
        lblMessage.setText(messages.get(LocalDate.now().getDayOfWeek().getValue() - 1));
        lblBreakfast.setText(resourceBundle.getString("breakfast") + " " + breakfast.get(LocalDate.now().getDayOfWeek().getValue() - 1));
        lblDinner.setText(resourceBundle.getString("dinner") + " " + dinner.get(LocalDate.now().getDayOfWeek().getValue() - 1));
        lblActivity.setText(activity.get(LocalDate.now().getDayOfWeek().getValue() - 1));

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy   HH:mm:ss");
            lblDateTime.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }
}
