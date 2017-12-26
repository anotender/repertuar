package repertuar.model.multikino;

import repertuar.model.Cinema;
import repertuar.service.api.ChainService;
import repertuar.service.factory.ServiceFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MultikinoCinema extends Cinema {

    public MultikinoCinema(String name, String url) {
        super(null, name, url);
    }

    @Override
    public ChainService getService() {
        return ServiceFactory.getMultikinoService();
    }
}
