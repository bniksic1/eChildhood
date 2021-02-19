package ba.unsa.etf.rpr.models;



import ba.unsa.etf.rpr.dto.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class EducatorModel {
    private ObservableList<Educator> educators;
    private SimpleObjectProperty<Educator> currentEducator = new SimpleObjectProperty<>();

    public EducatorModel(){
        educators = FXCollections.observableArrayList();
    }

    public EducatorModel(ArrayList<Educator> arrayList){
        educators = FXCollections.observableArrayList(arrayList);
    }

    public Educator getCurrentEducator() {
        return currentEducator.get();
    }

    public SimpleObjectProperty<Educator> currentEducatorProperty() {
        return currentEducator;
    }

    public void setCurrentEducator(Educator currentEducator) {
        this.currentEducator.set(currentEducator);
    }

    public ObservableList<Educator> getEducators() {
        return educators;
    }

    public void setEducators(ObservableList<Educator> educators) {
        this.educators = educators;
    }
}
