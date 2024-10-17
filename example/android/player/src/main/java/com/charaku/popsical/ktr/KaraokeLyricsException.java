package com.easternblu.khub.ktr;

/**
 * Exception for mostly parsing LRC exception
 * Created by pan on 14/12/17.
 */

public class KaraokeLyricsException extends Exception {

    public KaraokeLyricsException(Throwable cause) {
        super(cause);
    }

    public KaraokeLyricsException(String message) {
        super(message);
    }
}
