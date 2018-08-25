package repertuar.utils;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import repertuar.model.SeanceDay;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.*;

public class RepertoireUtilsTest {

    @Test
    public void shouldReturnEmptyListWhenDaysCountIsZero() {
        //given
        int daysCount = 0;

        //when
        List<SeanceDay> seanceDays = RepertoireUtils.getSeanceDays(daysCount);

        //then
        then(seanceDays).isNotNull().isEmpty();
    }

    @Test
    public void shouldReturnEmptyListWhenDaysCountIsLowerThanZero() {
        //given
        int daysCount = -1;

        //when
        List<SeanceDay> seanceDays = RepertoireUtils.getSeanceDays(daysCount);

        //then
        then(seanceDays).isNotNull().isEmpty();
    }

    @Test
    public void shouldReturnListOfSeanceDaysForDaysCountGreaterThanZero() {
        //given
        int daysCount = 3;

        //when
        List<SeanceDay> seanceDays = RepertoireUtils.getSeanceDays(daysCount);

        //then
        then(seanceDays)
                .isNotNull()
                .hasSize(3)
                .extracting("date")
                .hasOnlyElementsOfType(Date.class);

        then(seanceDays.get(0))
                .satisfies(seanceDay -> DateUtils.isSameDay(new Date(), seanceDay.getDate()));

        then(seanceDays.get(1))
                .satisfies(seanceDay -> DateUtils.isSameDay(DateUtils.addDays(new Date(), 1), seanceDay.getDate()));

        then(seanceDays.get(2))
                .satisfies(seanceDay -> DateUtils.isSameDay(DateUtils.addDays(new Date(), 2), seanceDay.getDate()));
    }
}