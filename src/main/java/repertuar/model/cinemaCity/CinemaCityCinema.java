package repertuar.model.cinemaCity;

import repertuar.model.Cinema;

/**
 * Created by mateu on 09.12.2015.
 */
public class CinemaCityCinema extends Cinema {

    public CinemaCityCinema(String name, String city, String website) {
        super(name, city, website);
    }

    @Override
    public void loadDays() {
        System.out.println(website);
    }

    @Override
    public void loadFilms(int day, String date) {

    }
}
