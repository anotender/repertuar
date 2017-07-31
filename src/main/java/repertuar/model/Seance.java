package repertuar.model;

import lombok.Data;
import repertuar.utils.Website;

@Data
public class Seance {
    private String hour;
    private Website website;

    public Seance(String hour, String url) {
        this.hour = hour;
        this.website = new Website(url);
    }

    @Override
    public String toString() {
        return hour;
    }
}
