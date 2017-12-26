package repertuar.controller;

import repertuar.model.Chain;
import repertuar.model.Cinema;
import repertuar.model.Film;
import repertuar.model.SeanceDay;
import repertuar.model.cinemaCity.CinemaCity;
import repertuar.model.helios.Helios;
import repertuar.model.multikino.Multikino;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class RepertuarController {

    public List<Chain> getChains() {
        List<Chain> chains = new LinkedList<>();

        chains.add(new Helios("Helios", "http://helios.pl"));
        chains.add(new Multikino("Multikino", "https://multikino.pl"));
        chains.add(new CinemaCity("Cinema City", "https://cinema-city.pl/"));

        return chains;
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
