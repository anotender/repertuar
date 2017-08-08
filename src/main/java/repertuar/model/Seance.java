package repertuar.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Seance {
    private String hour;
    private String url;

    public Seance(String hour) {
        this(hour, null);
    }

    @Override
    public String toString() {
        return hour;
    }
}
