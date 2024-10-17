package com.easternblu.khub.common;


import com.easternblu.khub.common.util.Dates;

import org.junit.Test;

import java.util.Arrays;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by pan on 31/3/17.
 */

public class DatesTest extends CommonTest {


    @Test
    public void testDuration() {
        assertEquals("check milliseconds", 1000, new Dates.Duration().addSeconds(1).getMilliseconds());
        assertEquals("check 2 second", 2000, new Dates.Duration().addSeconds(2).getMilliseconds());
        assertEquals("check 2 minutes", 2 * 60 * 1000, new Dates.Duration().addMinutes(2).getMilliseconds());
        assertEquals("check 1 day (24 hrs)", 24 * 60 * 60 * 1000, new Dates.Duration().addHours(24).getMilliseconds());
        assertEquals("check 2 minutes and 30sec", 2 * 60 * 1000 + (30 * 1000), new Dates.Duration().addMinutes(2).addSeconds(30).getMilliseconds());


        Dates.Duration temp = new Dates.Duration().addMinutes(2).addSeconds(30);
        long[] result = temp.getTimeValues(Calendar.HOUR, Calendar.MINUTE, Calendar.SECOND);
        long[] expected = new long[]{0, 2, 30};
        log("result = " + Arrays.toString(result));
        assertTrue("Same array", Arrays.equals(result, expected));
    }


    @Test
    public void testTimestamp() {

        long timestamp = 3 * 60000 + 55000;
        assertEquals("test toTimeText", "03:55.000", Dates.toTimeText(timestamp));
        assertEquals("test toTimeText with hour", "01:03:55.000", Dates.toTimeText(timestamp + 60000 * 60, true, true));

        assertEquals("test toTimeText 1 digit min", "03:55", Dates.toTimeText(timestamp, false));
        assertEquals("test toTimestamp with ms", timestamp, Dates.toTimestamp("03:55.000", -1));
        assertEquals("test toTimestamp 1 digit min", timestamp, Dates.toTimestamp("3:55", -1));
        assertEquals("test toTimestamp (with ms)", timestamp + 123, Dates.toTimestamp("03:55.123", -1));
        assertEquals("test toTimestamp (with hour)", timestamp + 60000 * 60, Dates.toTimestamp("01:03:55", -1));


    }


}
