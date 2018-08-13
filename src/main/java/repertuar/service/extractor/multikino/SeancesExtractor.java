package repertuar.service.extractor.multikino;

import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import repertuar.model.Seance;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SeancesExtractor implements BiFunction<JSONArray, Date, List<Seance>> {

    @Override
    public List<Seance> apply(JSONArray seancesJSONArray, Date date) {
        return IntStream
                .range(0, seancesJSONArray.length())
                .mapToObj(seancesJSONArray::getJSONObject)
                .filter(seance -> matchesDate(date, seance))
                .map(seance -> seance.getJSONArray("times"))
                .flatMap(seances -> IntStream
                        .range(0, seances.length())
                        .mapToObj(seances::getJSONObject)
                )
                .map(this::prepareSeance)
                .collect(Collectors.toList());
    }

    private boolean matchesDate(Date date, JSONObject seance) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            return DateUtils.isSameDay(date, df.parse(seance.getString("date_time")));
        } catch (ParseException e) {
            return false;
        }
    }

    private Seance prepareSeance(JSONObject seanceJSONObject) {
        return new Seance(seanceJSONObject.getString("time"), "");
    }

}
