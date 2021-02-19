package ba.unsa.etf.rpr.models;

import ba.unsa.etf.rpr.dto.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class ChildModel {
    private ObservableList<Child> children;
    private SimpleObjectProperty<Child> currentChild = new SimpleObjectProperty<>();

    public ChildModel(){
        children = FXCollections.observableArrayList();
    }
    public ChildModel(ArrayList<Child> arrayList){
        children = FXCollections.observableArrayList(arrayList);
    }

    public ObservableList<Child> getChildren() {
        return children;
    }

    public void setChildren(ObservableList<Child> children) {
        this.children = children;
    }

    public Child getCurrentChild() {
        return currentChild.get();
    }

    public SimpleObjectProperty<Child> currentChildProperty() {
        return currentChild;
    }

    public void setCurrentChild(Child currentChild) {
        this.currentChild.set(currentChild);
    }
}
