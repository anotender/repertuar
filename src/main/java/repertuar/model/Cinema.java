package repertuar.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class Cinema {
    private Integer id;
    private String name;
    protected String url;

    @Override
    public String toString() {
        return name;
    }
}
