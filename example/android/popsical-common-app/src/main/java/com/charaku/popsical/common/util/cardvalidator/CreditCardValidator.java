
package com.easternblu.khub.common.util.cardvalidator;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;

import com.easternblu.khub.common.util.Strings;
import com.easternblu.khub.common.util.cardvalidator.strategy.DefaultFormatterStrategy;
import com.easternblu.khub.common.util.cardvalidator.strategy.FormatStrategy;

import java.util.List;
import java.util.Set;


/**
 * Created by tyln on 28.04.16.
 */

public class CreditCardValidator implements TextWatcher {
    public static final String TAG = CreditCardValidator.class.getSimpleName();
    private List<Validator> mValidators;
    private final InputFilter[] mFilterArray = new InputFilter[1];

    private CreditCardValidationChangeListener mValidationChangeListener;
    private CreditCardTypeChangeListener mTypeChangeListener;

    private Validator mDefaultValidator;

    private boolean isPatternValid;
    private boolean isCardValid;
    private boolean isFormatable;

    private int mValidatePoint;

    public CreditCardValidator(List<Validator> validators,
                               CreditCardValidationChangeListener validationChangeListener,
                               CreditCardTypeChangeListener typeChangeListener,
                               boolean isFormatable,
                               int validatePotint) {
        this.mValidators = validators;
        this.mValidationChangeListener = validationChangeListener;
        this.mTypeChangeListener = typeChangeListener;
        this.isFormatable = isFormatable;
        this.isCardValid = false;
        this.isPatternValid = false;
        this.mValidatePoint = validatePotint;

    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        final String s = CreditCardUtils.cleanCardNumber(editable.toString());
        if (s.length() >= 0) {
            if (isValidatorChanged(s)) {
                mFilterArray[0] = new InputFilter.LengthFilter(getMaxLenForFormatter());
                editable.setFilters(mFilterArray);
                if (this.mTypeChangeListener != null) {
                    this.mTypeChangeListener.onCreditCardTypeChanged(mDefaultValidator.getType());
                }
            }
        }

        isPatternValid = (s.length() >= mDefaultValidator.getLen()) && s.matches(mDefaultValidator.getFullPattern());

        final boolean tempValid = validateCreditCard(s, isPatternValid);
        if (isCardValid != tempValid) {
            isCardValid = tempValid;
            if (mValidationChangeListener != null) {
                mValidationChangeListener.onValidationChanged(isCardValid);
            }
        }

        if (isFormatable) {
            mDefaultValidator.getFormatStrategy().formatCardNumber(editable);
        }
    }


    /**
     * Attempt to find the credit card type by just looking at the first 1~6 numbers
     *
     * @param cardNumber
     * @return
     * @throws Exception if it cannot tell the type yet
     */
    public int getType(String cardNumber) throws Exception {
        return getType(cardNumber, 6);
    }

    /**
     * Attempt to find the credit card type
     *
     * @param cardNumber
     * @return
     * @throws Exception if it cannot tell the type yet
     */
    public int getType(String cardNumber, int maxLenToCheck) throws Exception {
        if (cardNumber != null) {
            cardNumber = Strings.retainChars(cardNumber, CreditCardConstants.DIGITS_ONLY_CHAR_ARRAY);
        }
        if (cardNumber.length() >= 0) {
            StringBuilder buffer = new StringBuilder();
            for (int i = 0; i < Math.min(maxLenToCheck, cardNumber.length()); i++) {
                buffer.append(cardNumber.charAt(i));
                String s = buffer.toString();
                for (Validator v : mValidators) {
                    if (s.matches(v.getTypePattern())) {
                        return v.getType();
                    }
                }
            }
        }
        throw new Exception("Unable to find matching card type");
    }





    private boolean isValidatorChanged(String s) {
        boolean isChanged = false;

        if (mDefaultValidator == null) {
            mDefaultValidator = mValidators.get(0);
            isChanged = true;
        }

        for (Validator v : mValidators) {
            if (s.matches(v.getTypePattern()) && this.mDefaultValidator.getType() != v.getType()) {
                this.mDefaultValidator = v;
                isChanged = true;
            }
        }

        return isChanged;
    }

    private int getMaxLenForFormatter() {
        if (!isFormatable) {
            if (mDefaultValidator.getType() == CreditCardConstants.CARD_VISA) {
                return mDefaultValidator.getLen() + 3;
            } else {
                return mDefaultValidator.getLen();
            }
        } else {
            final FormatStrategy formatStrategy = mDefaultValidator.getFormatStrategy();
            if (formatStrategy instanceof DefaultFormatterStrategy) {
                if (mDefaultValidator.getType() == CreditCardConstants.CARD_VISA) {
                    return mDefaultValidator.getLen() + 6;
                } else {
                    return mDefaultValidator.getLen() + 3;
                }
            } else {
                return mDefaultValidator.getLen() + 2;
            }
        }
    }

    public static boolean validateCreditCard(String carNumber, boolean isPatternValid) {
        return isPatternValid && CreditCardUtils.luhm(carNumber);
    }


    public interface CreditCardValidationChangeListener {
        void onValidationChanged(boolean isValid);
    }

    public interface CreditCardTypeChangeListener {
        void onCreditCardTypeChanged(int type);
    }
}