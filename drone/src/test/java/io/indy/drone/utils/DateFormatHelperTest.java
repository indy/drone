package io.indy.drone.utils;

import java.util.Calendar;
import java.util.Date;
import org.junit.Test;
import static org.fest.assertions.api.Assertions.assertThat;


public class DateFormatHelperTest {

    @Test
    public void testParseDroneJsonDateString() {
        String jsonDate = "2002-11-03T00:00:00.000Z";
        Date d = DateFormatHelper.parseDroneJsonDateString(jsonDate);

        Calendar c = Calendar.getInstance();
        c.setTime(d);

        assertThat(c.get(Calendar.DAY_OF_MONTH)).isEqualTo(3);
        assertThat(c.get(Calendar.MONTH)).isEqualTo(Calendar.NOVEMBER);
        assertThat(c.get(Calendar.YEAR)).isEqualTo(2002);

    }
}
