package com.easternblu.khub.common.util;

import android.util.Log;

import com.easternblu.khub.common.CommonTest;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by pan on 31/3/17.
 */

public class GSONTest extends CommonTest {

    public static final String AGE = "age";
    public static final String WEIGHT = "weight";
    public static final String NAME = "name";

    static class Person {
        private int tempQueueId;

        @Expose
        @SerializedName(AGE)
        private int age;

        @Expose
        @SerializedName(WEIGHT)
        protected double weightInKg;

        @Expose
        @SerializedName(NAME)
        public String name;

        public String toJSONString() {
            return Strings.format("{" +
                    "\"%1$s\":%2$s," +
                    "\"%3$s\":%4$s," +
                    "\"%5$s\":\"%6$s\"" +
                    "}", AGE, age, WEIGHT, weightInKg, NAME, name);

        }

    }

    private static Person randomPerson() {
        Person person = new Person();
        person.tempQueueId = Maths.randomInt(Integer.MAX_VALUE / 10, Integer.MAX_VALUE);
        person.name = "Mr " + Maths.randomInt(1, 1000);
        person.age = Maths.randomInt(18, 90);
        person.weightInKg = Maths.roundHalfDown(Maths.randomInt(4500, 9000) / 100f, 2);
        return person;
    }

    @Test
    public void testOthers() throws JSONException {
        Person person = randomPerson();
        assertEquals("Should be same", person.toJSONString(), GSON.toString(person, true));


        Map<String, Object> valueObject = new HashMap<>();
        valueObject.put(NAME, "pan");
        valueObject.put(WEIGHT, 60d);
        valueObject.put(AGE, 30);

        Timber.d(GSON.toString(valueObject, false));
    }


}
