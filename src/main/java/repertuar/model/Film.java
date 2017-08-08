package repertuar.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Film {
    private String title;
    private String url;
    private List<Seance> seances;

    public Film(String title, List<Seance> seances) {
        this(title, null, seances);
    }

    @Override
    public String toString() {
        return title;
    }
}
