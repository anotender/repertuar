package repertuar.service.api;

import repertuar.model.Cinema;
import repertuar.model.Film;
import repertuar.model.SeanceDay;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface ChainService {

    List<Cinema> getCinemas() throws IOException;

    List<SeanceDay> getSeanceDays(Integer cinemaId) throws IOException;

    List<Film> getFilms(Integer cinemaID, Date date) throws IOException;

}
