package repertuar.controller;

import repertuar.model.Chain;
import repertuar.model.Cinema;
import repertuar.model.Film;
import repertuar.model.SeanceDay;
import repertuar.model.cinemaCity.CinemaCity;
import repertuar.model.helios.Helios;
import repertuar.model.multikino.Multikino;
import repertuar.service.factory.ServiceFactory;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class RepertuarController {

    private ServiceFactory serviceFactory = new ServiceFactory();

    public List<Chain> getChains() {
        List<Chain> chains = new LinkedList<>();

        chains.add(new Helios("Helios", "http://helios.pl"));
        chains.add(new Multikino("Multikino", "https://multikino.pl"));
        chains.add(new CinemaCity("Cinema City", "https://cinema-city.pl/"));

        return chains;
    }

    public List<Cinema> getCinemas(Chain chain) throws Exception {
        return Optional
                .of(serviceFactory.getService(chain))
                .orElseThrow(Exception::new)
                .getCinemas();
    }

    public List<SeanceDay> getSeanceDays(Cinema cinema) throws Exception {
        return Optional
                .of(serviceFactory.getService(cinema))
                .orElseThrow(Exception::new)
                .getSeanceDays(cinema.getId());
    }

    public List<Film> getFilms(Cinema cinema, Date date) throws Exception {
        return Optional
                .of(serviceFactory.getService(cinema))
                .orElseThrow(Exception::new)
                .getFilms(cinema.getId(), date);
    }
}
