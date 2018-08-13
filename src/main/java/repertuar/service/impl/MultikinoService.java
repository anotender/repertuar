package repertuar.service.impl;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import repertuar.model.Cinema;
import repertuar.model.Film;
import repertuar.model.SeanceDay;
import repertuar.service.api.ChainService;
import repertuar.service.extractor.multikino.CinemasExtractor;
import repertuar.service.extractor.multikino.FilmsExtractor;
import repertuar.utils.HttpUtils;
import repertuar.utils.RepertoireUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

public class MultikinoService implements ChainService {

    private static ChainService INSTANCE;

    private final String baseUrl = "https://multikino.pl/";
    private final Function<Document, List<Cinema>> cinemasExtractor = new CinemasExtractor();
    private final BiFunction<JSONArray, Date, List<Film>> filmsExtractor = new FilmsExtractor();

    private MultikinoService() {
    }

    public static ChainService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MultikinoService();
        }
        return INSTANCE;
    }

    @Override
    public List<Cinema> getCinemas() throws IOException {
        Document cinemasDocument = Jsoup
                .connect(baseUrl + "nasze-kina")
                .get();
        return cinemasExtractor
                .apply(cinemasDocument)
                .stream()
                .sorted(comparing(Cinema::getName))
                .collect(Collectors.toList());
    }

    @Override
    public List<SeanceDay> getSeanceDays(Integer cinemaId) {
        return RepertoireUtils.getSeanceDays(5);
    }

    @Override
    public List<Film> getFilms(Integer cinemaId, Date date) throws IOException {
        JSONArray filmsJSONArray = new JSONObject(HttpUtils
                .sendGet(baseUrl + "data/filmswithshowings/" + cinemaId))
                .getJSONArray("films");
        return filmsExtractor.apply(filmsJSONArray, date);
    }

}
