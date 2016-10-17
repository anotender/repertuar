package repertuar.model.multikino;

import repertuar.model.Cinema;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MultikinoCinema extends Cinema {

    private Integer cityID;
    private DateFormat dateFormat = new SimpleDateFormat();

    public MultikinoCinema(Integer id, String name, String url, Integer cityID) {
        super(id, name, url);
        this.cityID = cityID;
    }
}
