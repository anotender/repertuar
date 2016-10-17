package repertuar.model;

import java.util.Collections;
import java.util.List;

public abstract class Cinema {

    protected Integer id;
    protected String name;
    protected String url;
    protected List<SeanceDay> days = Collections.emptyList();

    public Cinema(Integer id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<SeanceDay> getDays() {
        return days;
    }

    public void setDays(List<SeanceDay> days) {
        this.days = days;
    }

    @Override
    public String toString() {
        return name;
    }
}
