package repertuar.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SeanceDay {
    private Date date;
    private List<Film> films;

    public SeanceDay(Date date, List<Film> films) {
        this.date = date;
        this.films = films;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Film> getFilms() {
        return films;
    }

    public void setFilms(List<Film> films) {
        this.films = films;
    }

    @Override
    public String toString() {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
}
