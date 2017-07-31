package repertuar.model;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public abstract class Cinema {

    private Integer id;
    private String name;
    protected String url;
    private List<SeanceDay> days = Collections.emptyList();

    public Cinema(Integer id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    @Override
    public String toString() {
        return name;
    }
}
