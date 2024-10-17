package com.easternblu.khub.common.util.cardvalidator;

import androidx.annotation.IntDef;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.easternblu.khub.common.util.Numbers;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import timber.log.Timber;

import static com.easternblu.khub.common.util.cardvalidator.CreditCardUtils.retainValidChars;


/**
 * Created by yatpanng on 29/7/17.
 */
public class CreditCardExpiryDateValidator implements TextWatcher {

    public static final int CARD_MAX_AGE_YEARS = 20;

    public static interface Listener {
        public void onCreditCardDateInputParsed(@InvalidReason int reason, String input);
    }

    public static final String TAG = CreditCardExpiryDateValidator.class.getSimpleName();

    // bitmask flags for recoverable error
    public static final int INVALID_INPUT_MONTH = 1; // ...000001
    public static final int INVALID_EXPIRED = 1 << 1; // ...000010
    public static final int INVALID_CARD_MAX_AGE_EXCEEDED = 1 << 3; // ...000100

    // // Used to specify one or more type of recoverable error
    @Retention(RetentionPolicy.SOURCE)
    @IntDef(value = {INVALID_INPUT_MONTH, INVALID_EXPIRED, INVALID_CARD_MAX_AGE_EXCEEDED}, flag = true)
    public @interface InvalidReason {
    }

    private Listener listener;
    private final String separator;
    private final Set<Character> validChars;
    private int yearLength, monthLength = 2;

    public CreditCardExpiryDateValidator() {
        this("/");
    }

    public CreditCardExpiryDateValidator(String separator) {
        this(separator, 2);
    }

    public CreditCardExpiryDateValidator(String separator, int yearLength) {
        this.separator = separator;
        this.yearLength = yearLength;
        this.validChars = new HashSet<>();
        for (char c : ("0123456789" + separator).toCharArray()) {
            validChars.add(c);
        }
    }

    private int expectedLength() {
        return monthLength + separator.length() + yearLength;
    }

    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private int beforeChangeLength = 0;

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Timber.i("beforeTextChanged: " + s + " start = " + start + " after = " + after + " count = " + count);
        beforeChangeLength = s.length();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Timber.i("onTextChanged: " + s + " start = " + start + " before = " + before + " count = " + count);
    }

    @Override
    public void afterTextChanged(Editable editable) {
        editable = retainValidChars(validChars, editable);

        Timber.i("afterTextChanged: " + beforeChangeLength + " -> " + editable.length());
        boolean adding = editable.length() > beforeChangeLength;
        if (adding) {
            Log.i(TAG,"afterTextChanged: adding");
            editable = maybeAppendSeparator(editable);
        }
        String input;

        @InvalidReason
        int invalidReason = checkInput(input = editable.toString());
        if (listener != null) {
            listener.onCreditCardDateInputParsed(invalidReason, input);
        }
    }


    @InvalidReason
    public int checkInput(String s) {
        Integer[] date = parse(s);

        int currentMonth = (Calendar.getInstance().get(Calendar.MONTH) + 1), currentYear = Calendar.getInstance().get(Calendar.YEAR);

        // must be at least 1 month before expiry
        int minMonthYearValue = (currentMonth + 1) + (12 * currentYear);
        // must be within 3 years (gov regulation)
        int maxMonthYearValue = minMonthYearValue + (12 * CARD_MAX_AGE_YEARS);
        @InvalidReason
        int error = 0;

        if (date[0] != null) {
            int m = date[0];
            if (!(1 <= m && m <= 12)) {
                error |= INVALID_INPUT_MONTH;
            }

            if (date[1] != null) {
                int y = date[1] + 2000;
                int monthYearValue = (m) + (12 * y);

                Timber.i("Months until expire: " + (monthYearValue - minMonthYearValue));
                if (monthYearValue < minMonthYearValue) {
                    error |= INVALID_EXPIRED;
                } else if (monthYearValue > maxMonthYearValue) {
                    error |= INVALID_CARD_MAX_AGE_EXCEEDED;
                }
            }
        }
        return error;
    }


    public Integer[] parse(String s) {
        Integer monthInt = null, yearInt = null;
        if (s != null) {
            if (s.length() >= monthLength) {
                monthInt = Numbers.parseInt(s.substring(0, monthLength), 0);
            }

            if (s.length() == expectedLength()) {
                yearInt = Numbers.parseInt(s.substring(expectedLength() - yearLength, expectedLength()), 0);
            }
        }
        return new Integer[]{monthInt, yearInt};
    }


    private Editable maybeAppendSeparator(Editable editable) {
        boolean allDigitSoFar = true;
        for (int i = 0; i < editable.length(); i++) {
            char c = editable.charAt(i);
            if (!Character.isDigit(c)) {
                allDigitSoFar = false;
                break;
            }
        }

        if (allDigitSoFar) {
            if (editable.length() == monthLength) {
                editable.append(separator);
            } else if (editable.length() > monthLength) {
                editable.insert(monthLength, separator);
            }
        }

        return editable;
    }


}
