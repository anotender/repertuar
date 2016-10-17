package repertuar.model;

import repertuar.utils.Website;

public class Seance {
    private String hour;
    private Website website;

    public Seance(String hour, String url) {
        this.hour = hour;
        this.website = new Website(url);
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    @Override
    public String toString() {
        return hour;
    }
}
