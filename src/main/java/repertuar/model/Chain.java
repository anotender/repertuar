package repertuar.model;

import lombok.Data;
import lombok.ToString;
import repertuar.utils.Website;

import java.util.Collections;
import java.util.List;

@Data
public abstract class Chain {
    private String name;
    private Website website;
    private List<Cinema> cinemas = Collections.emptyList();

    public Chain(String name, String url) {
        this.name = name;
        this.website = new Website(url);
    }

    @Override
    public String toString() {
        return name;
    }
}
