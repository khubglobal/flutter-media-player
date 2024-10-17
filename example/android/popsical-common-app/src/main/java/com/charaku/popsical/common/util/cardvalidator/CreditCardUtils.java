
package com.easternblu.khub.common.util.cardvalidator;

import android.text.Editable;
import android.text.TextUtils;

import com.easternblu.khub.common.util.Strings;

import java.util.Set;


/**
 * Created by tyln on 28.04.16.
 */

public final class CreditCardUtils {

    /**
     * Internal method use for credit card validation
     *
     * @param cardNumber
     * @return
     */
    public static boolean luhm(String cardNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

    /**
     * Keep all the validChars (chars in the set) in the editable, rest all remove. Preserve the order of the text
     *
     * @param validChars
     * @param editable
     * @return
     */
    public static Editable retainValidChars(Set<Character> validChars, Editable editable) {
        int pos = 0;
        while (0 <= pos && pos < editable.length()) {
            char c = editable.charAt(pos);
            if (validChars.contains(c)) {
                pos++;
                continue;
            } else {
                editable = editable.delete(pos, pos + 1);
            }
        }
        return editable;
    }

    /**
     * @param cardNumber
     * @return
     */
    public static String cleanCardNumber(String cardNumber) {
        if (!TextUtils.isEmpty(cardNumber)) {
            return Strings.retainChars(cardNumber, CreditCardConstants.DIGITS_ONLY_CHAR_ARRAY);
        }
        return cardNumber;
    }

    /**
     * Get the default english name of the credit card type
     * <p>
     * TODO: put it in Strings.xml?
     *
     * @param type
     * @return
     */
    public static String getDefaultCardName(int type) {
        switch (type) {
            case CreditCardConstants.CARD_VISA:
                return "VISA";

            case CreditCardConstants.CARD_MASTER:
                return "MASTER CARD";

            case CreditCardConstants.CARD_AMEX:
                return "American Express";

            case CreditCardConstants.CARD_DIN_CLUB:
                return "Diners Club";

            case CreditCardConstants.CARD_DISCOVER:
                return "Discover";

            case CreditCardConstants.CARD_JCB:
            case CreditCardConstants.CARD_JCB_15_DIGITS:
                return "JCB";

            default:
                return "";
        }
    }


    /**
     * Format a credit card number based on result from {@link #getFormat(int)}
     *
     * @param cardNumber
     * @param delim
     * @param partLength
     * @return
     */
    public static String formatCardNumber(String cardNumber, String delim, int... partLength) {
        String digits = cleanCardNumber(cardNumber);
        StringBuilder formattedNumber = new StringBuilder();
        int sectionSize = 0, sectionIndex = 0;
        for (char c : digits.toCharArray()) {
            formattedNumber.append(c);
            sectionSize++;
            if (sectionIndex < partLength.length - 1 && sectionSize == partLength[sectionIndex]) {
                formattedNumber.append(delim);
                sectionSize = 0;
                sectionIndex++;
            }

        }
        return formattedNumber.toString();
    }


    /**
     * Get the length of each section of the credit card number once formatted
     * <p>
     * Ideally need to match the {@link com.easternblu.khub.common.util.cardvalidator.strategy.FormatStrategy} implementations
     *
     * @param type
     * @return
     */
    public static int[] getFormat(int type) {
        switch (type) {
            case CreditCardConstants.CARD_AMEX:
                return new int[]{4, 5, 6};

            case CreditCardConstants.CARD_DIN_CLUB:
                return new int[]{4, 6, 4};

            case CreditCardConstants.CARD_VISA:
            case CreditCardConstants.CARD_MASTER:
            case CreditCardConstants.CARD_DISCOVER:
            case CreditCardConstants.CARD_JCB:
            case CreditCardConstants.CARD_JCB_15_DIGITS:
            default:
                return new int[]{4, 4, 4, 4};
        }
    }


}
