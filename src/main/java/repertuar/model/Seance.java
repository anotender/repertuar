package repertuar.model;

import javafx.beans.property.SimpleStringProperty;

public class Seance {
    private SimpleStringProperty stringToShow;
    private Website website;

    public Seance(String stringToShow, String url) {
        this.stringToShow = new SimpleStringProperty(stringToShow);
        this.website = new Website(url);
    }

    public String getStringToShow() {
        return stringToShow.get();
    }

    public SimpleStringProperty stringToShowProperty() {
        return stringToShow;
    }

    public void setStringToShow(String stringToShow) {
        this.stringToShow.set(stringToShow);
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }
}
