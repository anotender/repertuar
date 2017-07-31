package repertuar.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class SeanceDay {
    private Date date;
    private List<Film> films;

    @Override
    public String toString() {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
}
