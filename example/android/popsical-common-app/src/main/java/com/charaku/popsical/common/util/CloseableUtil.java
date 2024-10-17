package com.easternblu.khub.common.util;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.util.Collection;

/**
 * Created by yatpanng on 1/3/17.
 */
public class CloseableUtil {
    public static final String TAG = CloseableUtil.class.getSimpleName();


    /**
     * @param closeables
     * @see {@link #close(Object...)}
     */
    public static void close(Collection<?> closeables) {
        if (closeables != null) {
            close(closeables.toArray(new Object[0]));
        }
    }


    /**
     * A generic method to close an object if it is {@link Closeable} or {@link Socket}
     * <p>
     * throws {@link IllegalArgumentException} if it is neither class
     * <p>
     * @param closeables
     * @throws
     * @see <a href="https://stackoverflow.com/questions/29825330/why-a-socket-is-not-instanceof-closeable-at-runtime">Note: works for Socket object</a>
     */
    public static void close(Object... closeables) {
        if (closeables != null) {
            for (Object object : closeables) {
                if (object == null)
                    continue;

                try {
                    if (object instanceof Closeable) {
                        ((Closeable) object).close();

                    } else if (object instanceof Socket) {
                        ((Socket) object).close();

                    } else {
                        throw new IllegalArgumentException(object.getClass().getSimpleName() + " is neither " + Closeable.class.getSimpleName() + " nor a " + Socket.class.getSimpleName());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
