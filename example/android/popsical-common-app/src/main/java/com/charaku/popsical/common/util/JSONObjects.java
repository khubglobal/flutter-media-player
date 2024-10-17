package com.easternblu.khub.common.util;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Collection;

/**
 * Created by yatpanng on 16/2/17.
 */

public class JSONObjects {

    public static String asString(JSONArray jsonArray, String delim, boolean skipNull, Converter<Object, String> converter) throws JSONException {
        if (jsonArray != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < jsonArray.length(); i++) {
                Object element = jsonArray.get(i);
                if (element == null && skipNull)
                    continue;
                if (i > 0) {
                    sb.append(delim);
                } else {
                    sb.append(converter.convert(element));
                }
                i++;
            }
            return sb.toString();
        } else {
            return null;
        }
    }
}
