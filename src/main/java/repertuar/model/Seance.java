package repertuar.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Seance {
    private String hour;
    private String url;

    @Override
    public String toString() {
        return hour;
    }
}
