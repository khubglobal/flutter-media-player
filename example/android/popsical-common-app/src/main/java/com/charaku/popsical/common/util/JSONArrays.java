package com.easternblu.khub.common.util;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by yatpanng on 16/2/17.
 */

public class JSONArrays {


    /**
     * Extract elements of a given type inside an JSONArray
     *
     * @param jsonArray
     * @param type
     * @param output
     * @param <A>
     * @return
     * @throws JSONException
     */
    public static <A> List<A> getElementOfType(@NonNull JSONArray jsonArray, Class<A> type, List<A> output) throws JSONException {
        if (jsonArray == null) {
            return output;
        }
        for (int i = 0; i < jsonArray.length(); i++) {
            Object object = jsonArray.get(i);
            if (object != null && type.isInstance(object)) {
                output.add((A) object);
            }
        }
        return output;
    }

    /**
     * A convience way to create {@link JSONArray}
     *
     * @param elements
     * @return
     * @throws JSONException
     */
    public static JSONArray create(Object... elements) throws JSONException {
        return addAll(new JSONArray(), true, elements);
    }


    /**
     * A convience way to add elements to {@link JSONArray}
     *
     * @param jsonArray
     * @param skipNull
     * @param elements
     * @return
     * @throws JSONException
     */
    public static JSONArray addAll(@NonNull JSONArray jsonArray, boolean skipNull, @NonNull Object... elements) throws JSONException {
        for (Object element : elements) {
            if (element == null) {
                if (!skipNull) {
                    jsonArray.put(null);
                }
            } else if (element instanceof Integer ||
                    element instanceof Long ||
                    element instanceof Float ||
                    element instanceof Double ||
                    element instanceof String ||
                    element instanceof Boolean ||
                    element instanceof JSONObject) {
                jsonArray.put(element);
            } else {
                throw new JSONException("Unable to add element " + element);
            }
        }
        return jsonArray;
    }
}
