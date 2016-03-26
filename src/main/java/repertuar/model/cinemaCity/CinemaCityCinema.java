package repertuar.model.cinemaCity;

import repertuar.model.Cinema;

public class CinemaCityCinema extends Cinema {

    public CinemaCityCinema(String name, String city, String website) {
        super(name, city, website);
    }

    @Override
    public void loadDays() {
        System.out.println(url.get());
    }

    @Override
    public void loadFilms(int day, String date) {

    }
}
