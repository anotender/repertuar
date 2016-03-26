package repertuar.model.helios;

import javafx.beans.property.SimpleStringProperty;
import javafx.util.Pair;
import repertuar.model.Film;
import repertuar.model.Website;

import java.util.LinkedList;

public class HeliosFilm extends Film {

    public HeliosFilm(String title, String url, LinkedList<Pair<SimpleStringProperty, Website>> hours) {
        super(title, url, hours);
    }

    @Override
    public void loadHours() {
//        PrintWriter out = null;
//        try {
//            out = new PrintWriter("content.html");
//
//            website.loadContent(false);
//            for (String line : website.getContent()) {
//                out.println(line);
//            }
//        } catch (FileNotFoundException e) {
//        } finally {
//            if (out != null) {
//                out.close();
//            }
//        }
    }


}
