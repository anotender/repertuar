package repertuar.model;

import lombok.Data;
import lombok.ToString;
import repertuar.utils.Website;

import java.util.List;

@Data
public class Film {
    private String title;
    private Website website;
    private List<Seance> seances;

    public Film(String title, String url, List<Seance> seances) {
        this.title = title;
        this.website = new Website(url);
        this.seances = seances;
    }

    @Override
    public String toString() {
        return title;
    }
}
