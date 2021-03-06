package repertuar.service.impl;

import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import repertuar.model.Cinema;
import repertuar.model.Film;
import repertuar.model.SeanceDay;
import repertuar.service.api.ChainService;
import repertuar.service.extractor.helios.CinemasExtractor;
import repertuar.service.extractor.helios.FilmsExtractor;
import repertuar.utils.RepertoireUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import static repertuar.model.Chain.HELIOS;

public class HeliosService implements ChainService {

    private static ChainService INSTANCE;

    private final Function<Document, List<Cinema>> cinemasExtractor = new CinemasExtractor();
    private final Function<Document, List<Film>> filmsExtractor = new FilmsExtractor();

    private HeliosService() {
    }

    public static ChainService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HeliosService();
        }
        return INSTANCE;
    }

    @Override
    public List<Cinema> getCinemas() throws IOException {
        Document cinemasDocument = Jsoup.connect(HELIOS.getBaseUrl()).get();
        return cinemasExtractor.apply(cinemasDocument);
    }

    @Override
    public List<SeanceDay> getSeanceDays(Integer cinemaID) {
        return RepertoireUtils.getSeanceDays(7);
    }

    @Override
    public List<Film> getFilms(Integer cinemaID, Date date) throws IOException {
        Document filmsDocument = Jsoup
                .connect(HELIOS.getBaseUrl() + "/" + cinemaID + "/Repertuar/index/dzien/" + daysDifference(new Date(), date))
                .get();

        return filmsExtractor.apply(filmsDocument);
    }

    private int daysDifference(Date d1, Date d2) {
        Date earlierDate = d1.before(d2) ? d1 : d2;
        Date laterDate = d1.before(d2) ? d2 : d1;

        int diff = 0;

        while (!DateUtils.isSameDay(earlierDate, laterDate)) {
            earlierDate = DateUtils.addDays(earlierDate, 1);
            diff++;
        }

        return diff;
    }

}
