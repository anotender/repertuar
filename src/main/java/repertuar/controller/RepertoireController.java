package repertuar.controller;

import repertuar.model.Cinema;
import repertuar.model.Film;
import repertuar.model.SeanceDay;

import java.util.Date;
import java.util.List;

public class RepertoireController {

    public List<SeanceDay> getSeanceDays(Cinema cinema) throws Exception {
        return cinema.getService().getSeanceDays(cinema.getId());
    }

    public List<Film> getFilms(Cinema cinema, Date date) throws Exception {
        return cinema.getService().getFilms(cinema.getId(), date);
    }
}
