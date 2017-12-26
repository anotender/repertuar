package repertuar.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import repertuar.service.api.ChainService;

@Data
@AllArgsConstructor
public abstract class Cinema {
    private Integer id;
    private String name;
    protected String url;

    public abstract ChainService getService();

    @Override
    public String toString() {
        return name;
    }
}
