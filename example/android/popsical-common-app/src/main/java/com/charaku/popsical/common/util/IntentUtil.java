package com.easternblu.khub.common.util;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * A class to manipulate or reading Intent
 * Created by pan on 7/3/17.
 */
public class IntentUtil {

    /**
     * Convert a Intent to a readable string for logging
     *
     * @param intent An intent object
     * @return A readable string
     */
    public static String toString(Intent intent) {
        if (intent == null)
            return null;
        StringBuilder sb = new StringBuilder();
        sb.append("action=" + intent.getAction() + ",");
        if (intent.getExtras() != null) {
            sb.append(getJson(intent.getExtras()).toString() + ",");
        }

        sb.append("uri=" + intent.getData() + ",");
        sb.append("category=" + Sets.asString(intent.getCategories(), "|", true, new Converter<String, String>() {
            @Override
            public String convert(String from) {
                return from;
            }
        }) + ",");


        return sb.toString();
    }

    /**
     * Convert a JSON object to a Bundle that can be passed as the extras of
     * an Intent. It passes each number as a double, and everything else as a
     * String, arrays of those two are also supported.
     *
     * @param s the jsonObject
     * @return The Bundle
     */
    public static Bundle getBundle(JSONObject s) {
        Bundle bundle = new Bundle();
        writeToBundle(s, bundle);
        return bundle;
    }

    /**
     * @param s
     * @param bundle
     * @return
     * @see {@link #getBundle(JSONObject)}
     */
    public static void writeToBundle(JSONObject s, Bundle bundle) {
        for (Iterator<String> it = s.keys(); it.hasNext(); ) {
            String key = it.next();
            JSONArray arr = s.optJSONArray(key);
            Double num = s.optDouble(key);
            String str = s.optString(key);

            if (arr != null && arr.length() <= 0) {
                bundle.putStringArray(key, new String[]{});

            } else if (arr != null && !Double.isNaN(arr.optDouble(0))) {
                double[] newarr = new double[arr.length()];
                for (int i = 0; i < arr.length(); i++)
                    newarr[i] = arr.optDouble(i);
                bundle.putDoubleArray(key, newarr);


            } else if (arr != null && arr.optString(0) != null) {
                String[] newarr = new String[arr.length()];
                for (int i = 0; i < arr.length(); i++)
                    newarr[i] = arr.optString(i);
                bundle.putStringArray(key, newarr);


            } else if (!num.isNaN()) {
                bundle.putDouble(key, num);

            } else if (str != null) {
                bundle.putString(key, str);

            } else {
                System.err.println("unable to transform json to bundle " + key);
            }
        }
    }


    /**
     * Copy the value from bundle to a JSONObject
     * <p>
     * Not reversible.
     *
     * @param bundle
     * @return a Json object
     */
    @Nullable
    public static JSONObject getJson(final Bundle bundle) {
        if (bundle == null)
            return null;
        JSONObject jsonObject = new JSONObject();

        for (String key : bundle.keySet()) {
            Object obj = bundle.get(key);
            try {
                jsonObject.put(key, obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }


    /**
     * Internal method for {@link #getJson(Bundle)}
     *
     * @param o
     * @return Either a JSONArray or JSONObject or a JSON supported primitive
     */
    private static Object wrap(Object o) {
        if (o == null) {
            return JSONObject.NULL;
        }
        if (o instanceof JSONArray || o instanceof JSONObject) {
            return o;
        }
        if (o.equals(JSONObject.NULL)) {
            return o;
        }
        try {
            if (o instanceof Collection) {
                return new JSONArray((Collection) o);
            } else if (o.getClass().isArray()) {
                return toJSONArray(o);
            }
            if (o instanceof Map) {
                return new JSONObject((Map) o);
            }
            if (o instanceof Boolean ||
                    o instanceof Byte ||
                    o instanceof Character ||
                    o instanceof Double ||
                    o instanceof Float ||
                    o instanceof Integer ||
                    o instanceof Long ||
                    o instanceof Short ||
                    o instanceof String) {
                return o;
            }
            if (o.getClass().getPackage().getName().startsWith("java.")) {
                return o.toString();
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * Internal method for {@link #getJson(Bundle)}
     *
     * @param array array (e.g. int[] or String[])
     * @return a JSONArray object
     * @throws JSONException
     */
    private static JSONArray toJSONArray(Object array) throws JSONException {
        JSONArray result = new JSONArray();
        if (!array.getClass().isArray()) {
            throw new JSONException("Not a primitive array: " + array.getClass());
        }
        final int length = Array.getLength(array);
        for (int i = 0; i < length; ++i) {
            result.put(wrap(Array.get(array, i)));
        }
        return result;
    }


    public static Bundle createBundle(Object... nameAndValues) {
        Bundle bundle = new Bundle();
        for (int i = 0; i < nameAndValues.length; i += 2) {
            Object name = nameAndValues[i], value = nameAndValues[i + 1];
            if (name == null || value == null || !(name instanceof String)) {
                continue;
            }
            IntentUtil.put(bundle, (String) name, value);
        }
        return bundle;
    }

    public static Bundle createBundleFromMap(@Nullable Map<String, Object> nameAndValues) {
        Bundle bundle = new Bundle();
        if (nameAndValues != null) {
            for (String key : nameAndValues.keySet()) {
                Object value = nameAndValues.get(key);
                if (value == null) {
                    continue;
                }
                IntentUtil.put(bundle, key, value);
            }
        }
        return bundle;
    }


    public static boolean put(Bundle bundle, String name, Object object) {
        if (name == null || object == null || bundle == null) {
            return false;
        }

        if (object instanceof Integer) {
            bundle.putInt(name, (Integer) object);
        } else if (object instanceof Long) {
            bundle.putLong(name, (Long) object);
        } else if (object instanceof Byte) {
            bundle.putByte(name, (Byte) object);
        } else if (object instanceof Short) {
            bundle.putShort(name, (Short) object);
        } else if (object instanceof Boolean) {
            bundle.putBoolean(name, (Boolean) object);
        } else if (object instanceof String) {
            bundle.putString(name, (String) object);
        } else if (object instanceof Double) {
            bundle.putDouble(name, (Double) object);
        } else if (object instanceof Float) {
            bundle.putFloat(name, (Float) object);
        } else if (object instanceof Character) {
            bundle.putChar(name, (Character) object);
        } else {
            return false;
        }
        return true;
    }

}
