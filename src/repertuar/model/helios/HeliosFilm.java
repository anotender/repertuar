package repertuar.model.helios;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.util.Pair;
import repertuar.model.Film;
import repertuar.model.Website;
import sun.java2d.pipe.SpanShapeRenderer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by mateu on 22.09.2015.
 */
public class HeliosFilm extends Film {

    public HeliosFilm(String title, String url) {
        super(title, url);
    }

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
