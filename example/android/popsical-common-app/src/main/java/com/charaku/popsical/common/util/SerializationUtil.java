package com.easternblu.khub.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import timber.log.Timber;

/**
 * Created by pan on 21/9/17.
 */

public class SerializationUtil {
    public static final String TAG = SerializationUtil.class.getSimpleName();


    public static <O extends Serializable> O clone(O object) {
        O clone = null;
        try {
            clone = SerializationUtil.deserialize(object.getClass(), SerializationUtil.serialize(object));
        } catch (Exception e) {
            Timber.e(e);
        }
        return clone;
    }

    public static byte[] serialize(Serializable yourObject) throws IOException {
        if (yourObject == null)
            return null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(yourObject);
            return bos.toByteArray();

        } finally {
            CloseableUtil.close(bos);
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
    }

    public static <K> K deserialize(Class<?> clazz, byte[] serializedData) throws IOException, ClassNotFoundException {
        if (serializedData == null) {
            throw new NullPointerException("No serialized data");
        }
        ByteArrayInputStream bis = new ByteArrayInputStream(serializedData);
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(bis);

            Object obj = in.readObject();
            if (obj != null)
                return (K) obj;

        } finally {
            CloseableUtil.close(bis);
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return null;
    }
}
