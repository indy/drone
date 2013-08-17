package io.indy.drone.model;

import org.junit.Test;

import static org.junit.Assert.*;
//import static org.fest.assertions.api.Assertions.assertThat;

public class StrikeTest {

    @Test
    public void testHasValidMinMax() {

        String[] valid = {"2-5",
                          "80-82",
                          "0",
                          "11",
                          "123",
                          " 0",
                          " 2 - 5 ",
                          " 80-82",
                          " 11 ",
                          " 123"};

        for(String s : valid) {
            assertEquals(true, Strike.hasValidMinMax(s));
        }

        String[] invalid = {"", 
                            "Unknown",
                            "Possibly",
                            "Possible",
                            "Some'",
                            "At least 1",
                            "'Many' (2 named)"};
        for(String s : invalid) {
            assertEquals(false, Strike.hasValidMinMax(s));
        }
    }

    @Test
    public void testParseMinMax() {
        try {
            assertArrayEquals(new int[]{2, 5}, Strike.parseMinMax("2-5"));
            assertArrayEquals(new int[]{80, 82}, Strike.parseMinMax("80-82"));
            assertArrayEquals(new int[]{0, 0}, Strike.parseMinMax("0"));
            assertArrayEquals(new int[]{11, 11}, Strike.parseMinMax("11"));
            assertArrayEquals(new int[]{123, 123}, Strike.parseMinMax("123"));

            assertArrayEquals(new int[]{0, 0}, Strike.parseMinMax(" 0"));
            assertArrayEquals(new int[]{2, 5}, Strike.parseMinMax(" 2 - 5 "));
            assertArrayEquals(new int[]{80, 82}, Strike.parseMinMax(" 80-82"));
            assertArrayEquals(new int[]{11, 11}, Strike.parseMinMax(" 11 "));
            assertArrayEquals(new int[]{123, 123}, Strike.parseMinMax(" 123"));
        } catch(Strike.ParseException e) {

        }
    }
}
