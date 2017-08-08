package repertuar.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class Chain {
    private String name;
    private String url;

    @Override
    public String toString() {
        return name;
    }
}
