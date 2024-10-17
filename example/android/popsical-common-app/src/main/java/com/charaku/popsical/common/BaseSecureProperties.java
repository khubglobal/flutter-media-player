package com.easternblu.khub.common;

import android.app.Application;
import android.content.Context;
import androidx.annotation.Nullable;

import com.easternblu.khub.common.util.Base64;
import com.easternblu.khub.common.util.CipherHelper;
import com.easternblu.khub.common.util.CloseableUtil;
import com.easternblu.khub.common.util.Strings;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import timber.log.Timber;

/**
 * A class to read the encrypted properties file in assets folder
 * <p>
 * See build.gradle for SecureFileHelper usage
 * <p>
 * Based on the properties file in popsical.common.properties and how it is copied to strings.xml in build.gradle. It is deemded too insecure
 * if we jsut let api key and id exposed plainly in strings.xml. So this class is used in conjuction with SecureFileHelper
 * which will encrypt the properties file during compile time using a symmetric key and initial-vector
 * <p>
 * Created by yatpanng on 13/6/17.
 */
public class BaseSecureProperties extends Properties {
    public static final String TAG = BaseSecureProperties.class.getSimpleName();


    protected BaseSecureProperties() {
    }

    /**
     * 1) read the popsical.tv.properties<p>
     * 2) wrap it in a decrypting stream<p>
     * 3) load it into {@link Properties}<p>
     * 5) This method MUST be called in {@link Application#onCreate()}<p>
     *
     * @param ctx
     * @param assetsFilePath
     * @param cipherOptions
     * @throws IOException
     * @throws CipherHelper.CipherHelperException
     */
    public void setup(Context ctx, String assetsFilePath, String cipherOptions) throws IOException, CipherHelper.CipherHelperException {
        InputStream input = null;
        try {

            Timber.v("assetsFilePath="+assetsFilePath);
            Timber.v("cipherOptions="+cipherOptions);
            input = ctx.getResources().getAssets().open(assetsFilePath);
            input = CipherHelper.createDecryptingInputStream(input, cipherOptions);
            load(input);

            for (Object key : new ArrayList<>(this.keySet())) {
                Object value = get(key);
                if (isValidKey(key) && isValidValue(value)) {
                    // Timber.v("Put " + key + " = " + value);
                    this.put(key, value);
                } else {
                    Timber.w("Cannot put " + key + " = " + value);
                }
            }

        } finally {
            CloseableUtil.close(input);
        }
    }

    private boolean isValidKey(@Nullable Object key) {
        return key instanceof String && !((String) key).isEmpty();
    }

    private boolean isValidValue(@Nullable Object value) {
        return value instanceof String && !((String) value).isEmpty();
    }


}
