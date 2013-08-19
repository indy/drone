package io.indy.drone.model;

import org.junit.Test;
import org.json.JSONException;
import org.json.JSONObject;

import static org.fest.assertions.api.Assertions.assertThat;

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
            assertThat(Strike.hasValidMinMax(s)).isTrue();
        }

        String[] invalid = {"", 
                            "Unknown",
                            "Possibly",
                            "Possible",
                            "Some'",
                            "At least 1",
                            "'Many' (2 named)"};
        for(String s : invalid) {
            assertThat(Strike.hasValidMinMax(s)).isFalse();
        }
    }

    @Test
    public void testParseMinMax() {
        try {
            assertThat(Strike.parseMinMax("2-5")).isEqualTo(new int[]{2,5});
            assertThat(Strike.parseMinMax("80-82")).isEqualTo(new int[]{80,82});
            assertThat(Strike.parseMinMax("0")).isEqualTo(new int[]{0,0});
            assertThat(Strike.parseMinMax("11")).isEqualTo(new int[]{11,11});
            assertThat(Strike.parseMinMax("123")).isEqualTo(new int[]{123,123});

            assertThat(Strike.parseMinMax(" 0")).isEqualTo(new int[]{0,0});
            assertThat(Strike.parseMinMax(" 2 - 5 ")).isEqualTo(new int[]{2,5});
            assertThat(Strike.parseMinMax(" 8-82")).isEqualTo(new int[]{8,82});
            assertThat(Strike.parseMinMax(" 11 ")).isEqualTo(new int[]{11,11});
            assertThat(Strike.parseMinMax(" 123")).isEqualTo(new int[]{123,123});
        } catch(Strike.ParseException e) {

        }
    }

    //    @Test
    public void testFromJson() {
        String jsonString = "{" + 
            "\"_id\": \"51a65578e0932c0e1eb4199f\"," + 
            "\"number\": 1," + 
            "\"country\": \"Yemen\"," + 
            "\"date\": \"2002-11-03T00:00:00.000Z\"," + 
            "\"town\": \"\"," + 
            "\"location\": \"Marib Province\"," + 
            "\"deaths\": \"2\"," + 
            "\"deaths_min\": \"3\"," + 
            "\"deaths_max\": \"4\"," + 
            "\"civilians\": \"5-6\"," + 
            "\"injuries\": \"\"," + 
            "\"children\": \"\"," + 
            "\"tweet_id\": \"278544689483890688\"," + 
            "\"bureau_id\": \"YEM001\"," + 
            "\"bij_summary_short\": \"In the first known US targeted assassination using a drone, a CIA Predator struck a car killing six al Qaeda suspects.\"," + 
            "\"bij_link\": \"http://www.thebureauinvestigates.com/2012/03/29/yemen-reported-us-covert-actions-since-2001/\"," + 
            "\"target\": \"\"," + 
            "\"lat\": \"15.47467\"," + 
            "\"lon\": \"45.322755\"," + 
            "\"articles\": []," + 
            "\"names\": [" + 
            "\"Qa’id Salim Sinan al-Harithi, Abu Ahmad al-Hijazi, Salih Hussain Ali al-Nunu, Awsan Ahmad al-Tarihi, Munir Ahmad Abdallah al-Sauda, Adil Nasir al-Sauda’\"" + 
            "]" + 
            "}";

        try {
            String myString = new JSONObject().put("JSON", "Hello, World!").toString();
            JSONObject jsonObject = new JSONObject(myString);
            //            Strike strike = Strike.fromJson(jsonObject);
            //            assertThat(strike.getNumber()).isEqualTo(1);
        } catch(JSONException e) {

        }
    }
}
