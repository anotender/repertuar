package repertuar.model;

import repertuar.utils.Website;

import java.util.List;

public class Film {
    private String title;
    private Website website;
    private List<Seance> seances;

    public Film(String title, String url, List<Seance> seances) {
        this.title = title;
        this.website = new Website(url);
        this.seances = seances;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    public List<Seance> getSeances() {
        return seances;
    }

    public void setSeances(List<Seance> seances) {
        this.seances = seances;
    }

    @Override
    public String toString() {
        return title;
    }
}
