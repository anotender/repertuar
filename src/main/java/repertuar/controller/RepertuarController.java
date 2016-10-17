package repertuar.controller;

import repertuar.model.Chain;
import repertuar.model.Cinema;
import repertuar.model.Film;
import repertuar.model.SeanceDay;
import repertuar.model.cinemaCity.CinemaCity;
import repertuar.model.helios.Helios;
import repertuar.model.multikino.Multikino;
import repertuar.model.multikino.MultikinoCinema;
import repertuar.service.impl.CinemaCityService;
import repertuar.service.impl.MultikinoService;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class RepertuarController {

    private MultikinoService multikinoService = new MultikinoService();
    private CinemaCityService cinemaCityService = new CinemaCityService();

    public List<Chain> getChains() {
        List<Chain> chains = new LinkedList<>();

        chains.add(new Helios("Helios", "http://helios.pl"));
        chains.add(new Multikino("Multikino", "https://multikino.pl"));
        chains.add(new CinemaCity("Cinema City", "https://cinema-city.pl/"));

        return chains;
    }

    public List<Cinema> getCinemas(Chain chain) throws IOException {
        if (chain instanceof Multikino) {
            return multikinoService.getCinemas();
        } else if (chain instanceof CinemaCity) {
            return cinemaCityService.getCinemas();
        }
        return Collections.emptyList();
    }

    public List<SeanceDay> getSeanceDays(Cinema cinema) throws IOException {
        if (cinema instanceof MultikinoCinema) {
            return multikinoService.getSeanceDays(cinema.getId());
        }
        return Collections.emptyList();
    }

    public List<Film> getFilms(Cinema cinema, Date date) throws IOException {
        if (cinema instanceof MultikinoCinema) {
            return multikinoService.getFilms(cinema.getId(), date);
        }
        return Collections.emptyList();
    }
}
