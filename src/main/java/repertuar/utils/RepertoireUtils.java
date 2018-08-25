package repertuar.utils;

import org.apache.commons.lang3.time.DateUtils;
import repertuar.model.SeanceDay;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RepertoireUtils {

    public static List<SeanceDay> getSeanceDays(int daysCount) {
        return IntStream
                .range(0, daysCount)
                .mapToObj(i -> new SeanceDay(
                                DateUtils.addDays(new Date(), i),
                                Collections.emptyList()
                        )
                )
                .collect(Collectors.toList());
    }

}
