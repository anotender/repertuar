package repertuar.service.impl;

import org.json.JSONObject;
import repertuar.model.Cinema;
import repertuar.model.Film;
import repertuar.model.SeanceDay;
import repertuar.service.api.ChainService;
import repertuar.service.extractor.cinemacity.CinemasExtractor;
import repertuar.service.extractor.cinemacity.FilmsExtractor;
import repertuar.utils.HttpUtils;
import repertuar.utils.RepertoireUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class CinemaCityService implements ChainService {

    private static ChainService INSTANCE;

    private final String baseUrl = "https://www.cinema-city.pl/";
    private final Function<JSONObject, List<Cinema>> cinemasExtractor = new CinemasExtractor();
    private final BiFunction<JSONObject, Date, List<Film>> filmsExtractor = new FilmsExtractor();

    private CinemaCityService() {
    }

    public static ChainService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CinemaCityService();
        }
        return INSTANCE;
    }

    @Override
    public List<Cinema> getCinemas() throws IOException {
        JSONObject cinemasJSONObject = new JSONObject(HttpUtils.sendGet(baseUrl + "pl/data-api-service/v1/quickbook/10103/cinemas/with-event/until/2019-07-22?attr=&lang=pl_PL"));
        return cinemasExtractor.apply(cinemasJSONObject);
    }

    @Override
    public List<SeanceDay> getSeanceDays(Integer cinemaID) {
        return RepertoireUtils.getSeanceDays(7);
    }

    @Override
    public List<Film> getFilms(Integer cinemaID, Date date) throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String url = String.format(
                "%spl/data-api-service/v1/quickbook/10103/film-events/in-cinema/%d/at-date/%s",
                baseUrl, cinemaID, dateFormat.format(date)
        );
        JSONObject filmsJSONObject = new JSONObject(HttpUtils.sendGet(url));

        return filmsExtractor.apply(filmsJSONObject, date);
    }

}
