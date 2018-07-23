package repertuar.controller;

import repertuar.model.Chain;
import repertuar.model.Cinema;
import repertuar.model.Film;
import repertuar.model.SeanceDay;
import repertuar.model.cinemacity.CinemaCity;
import repertuar.model.helios.Helios;
import repertuar.model.multikino.Multikino;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class RepertoireController {

    public List<Chain> getChains() {
        return Arrays.asList(
                new Helios("Helios", "http://helios.pl"),
                new Multikino("Multikino", "https://multikino.pl"),
                new CinemaCity("Cinema City", "https://cinema-city.pl/")
        );
    }

    public List<Cinema> getCinemas(Chain chain) throws Exception {
        return chain.getService().getCinemas();
    }

    public List<SeanceDay> getSeanceDays(Cinema cinema) throws Exception {
        return cinema.getService().getSeanceDays(cinema.getId());
    }

    public List<Film> getFilms(Cinema cinema, Date date) throws Exception {
        return cinema.getService().getFilms(cinema.getId(), date);
    }
}
