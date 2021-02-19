package ba.unsa.etf.rpr.utils;

import javafx.scene.image.Image;

import java.io.InputStream;

public class ImageURL extends Image {
    private String url;
    public ImageURL(String s) {
        super(s);
        url = s;
    }

    public ImageURL(InputStream inputStream) {
        super(inputStream);
    }

    public String getURL() {
        return url;
    }
}
