package repertuar.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import repertuar.service.api.ChainService;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public abstract class Cinema {
    private Integer id;
    private String name;
    private String url;

    protected abstract ChainService getChainService();

    public List<SeanceDay> getSeanceDays() throws IOException {
        return getChainService().getSeanceDays(id);
    }

    public List<Film> getFilms(Date date) throws IOException {
        return getChainService().getFilms(id, date);
    }

    @Override
    public String toString() {
        return name;
    }
}
