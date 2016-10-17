package repertuar.model;

import repertuar.utils.Website;

import java.util.Collections;
import java.util.List;

public abstract class Chain {

    protected String name;
    protected Website website;
    protected List<Cinema> cinemas = Collections.emptyList();

    public Chain(String name, String url) {
        this.name = name;
        this.website = new Website(url);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    public List<Cinema> getCinemas() {
        return cinemas;
    }

    public void setCinemas(List<Cinema> cinemas) {
        this.cinemas = cinemas;
    }

    @Override
    public String toString() {
        return name;
    }
}
