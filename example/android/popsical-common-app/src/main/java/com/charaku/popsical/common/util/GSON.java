package com.easternblu.khub.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

import timber.log.Timber;

/**
 * Created by yatpanng on 15/2/17.
 */

public class GSON {

    private static final String TAG = GSON.class.getSimpleName();

    /**
     * Convert the object to JSON format For exposedOnly, set this to true,
     * Otherwise all the objects member variables (public/protected/private)
     * will be part of the json and it can get dangerous result json can also be
     * very big.
     *
     * @param obj
     * @param exposedOnly if true then only @Expose variables will be used (ideally this
     *                    should always be true)
     * @return
     */
    public static String toString(Object obj, boolean exposedOnly) {
        return toString(obj, exposedOnly, false);
    }

    public static String toString(Object obj, boolean exposedOnly, boolean prettyPrint) {
        return toString(obj, new DefaultModifier(exposedOnly, prettyPrint));
    }


    public static interface GsonBuilderModifier {
        public GsonBuilder modify(GsonBuilder gsBuilder);
    }

    public static class DefaultModifier implements GsonBuilderModifier {

        final boolean exposedOnly, prettyPrint;

        public DefaultModifier(boolean exposedOnly) {
            this(exposedOnly, false);
        }

        public DefaultModifier(boolean exposedOnly, boolean prettyPrint) {
            super();
            this.exposedOnly = exposedOnly;
            this.prettyPrint = prettyPrint;
        }

        @Override
        public GsonBuilder modify(GsonBuilder gsBuilder) {
            if (exposedOnly)
                gsBuilder = gsBuilder.excludeFieldsWithoutExposeAnnotation();


            if (prettyPrint)
                gsBuilder = gsBuilder.setPrettyPrinting();
            return gsBuilder;
        }
    }

    public static String toString(Object obj, GsonBuilderModifier builderModifier) {

        if (obj == null)
            return null;

        GsonBuilder gsBuilder = new GsonBuilder();
        if (builderModifier != null)
            gsBuilder = builderModifier.modify(gsBuilder);

        Gson gs = gsBuilder.create();
        String json = gs.toJson(obj);
        return json;
    }


    /**
     * Convert a json string to a object of given class
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <K> K toObject(String json, Class<K> clazz) {
        return toObject(json, clazz, true);
    }

    /**
     * Convert a json string to a object of given class
     *
     * @param json       JSON string
     * @param clazz      Intended class to be converted
     * @param exposeOnly False if expose all parameters by default
     * @param <K>        Class
     * @return Result in class clazz
     */
    public static <K> K toObject(String json, Class<K> clazz, boolean exposeOnly) {
        return toObject(json, clazz, new DefaultModifier(exposeOnly, false));
    }

    public static <K> K toObject(String json, Class<K> clazz, GsonBuilderModifier builderModifier) {
        if (json == null)
            return null;
        GsonBuilder gsBuilder = new GsonBuilder();
        if (builderModifier != null)
            gsBuilder = builderModifier.modify(gsBuilder);
        Gson gs = gsBuilder.create();
        try {
            return gs.fromJson(json, clazz);
        } catch (JsonSyntaxException e) {
            Timber.e(e);
            Timber.e("Unable to convert: " + json + " to class " + clazz.getSimpleName());
            throw e;
        }
    }

    /**
     * Convert a json string to a object of given type this is used for generics
     *
     * @param json
     * @param type (use TypeToken to get the Type object)
     * @return
     */
    public static <K> K toObject(String json, Type type) {
        if (json == null)
            return null;
        GsonBuilder gsBuilder = new GsonBuilder();
        Gson gs = gsBuilder.create();
        return gs.fromJson(json, type);
    }
}
