package com.easternblu.khub.common.util;

import androidx.annotation.Nullable;

import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A very simple class to shortcut methods in {@link java.util.Currency}
 * Created by pan on 22/2/18.
 */

public class CurrencyUtil {
    public static class CurrencyOverride {
        private Integer fractionDigits = null;
        private String symbol;
        private int lowestSubunit = 100;
    }

    private static Map<Currency, CurrencyOverride> overrideMap;
    private static final CurrencyOverride DEFAULT_CURRENCY_OVERRIDE = new CurrencyOverride();

    static {
        overrideMap = new HashMap<>();
        CurrencyOverride override;


        override = new CurrencyOverride();
        override.symbol = "S$"; // default java implementation from Currency class is '$', so we want to change that
        overrideMap.put(Currency.getInstance("SGD"), override);


        override = new CurrencyOverride();
        override.symbol = "Rp";
        override.fractionDigits = 0;
        overrideMap.put(Currency.getInstance("IDR"), override);

        override = new CurrencyOverride();
        override.symbol = "RM";
        overrideMap.put(Currency.getInstance("MYR"), override);


        override = new CurrencyOverride();
        override.symbol = "₩";
        override.fractionDigits = 0;
        override.lowestSubunit = 1;
        overrideMap.put(Currency.getInstance("KRW"), override);

        override = new CurrencyOverride();
        override.symbol = "¥";
        override.fractionDigits = 0;
        override.lowestSubunit = 1;
        overrideMap.put(Currency.getInstance("JPY"), override);

        override = new CurrencyOverride();
        override.lowestSubunit = 1;
        overrideMap.put(Currency.getInstance("XAF"), override);

        override = new CurrencyOverride();
        override.lowestSubunit = 1;
        overrideMap.put(Currency.getInstance("XOF"), override);

    }


    /**
     * The amount of getSubunit() = 1 unit of currency
     * <p>
     * e.g.<br/>
     * 100 cents = 1 SGD<br/>
     * returns 100<br/>
     * <p>
     * 1 yen = 1 JPY<br/>
     * returns 1 (there is nothing lower then 1 yen in JPY)<br/>
     *
     * @param currency
     * @return
     */
    public static int getSubunit(Currency currency) {
        CurrencyOverride currencyOverride = overrideMap.get(currency);
        if (currencyOverride == null) {
            currencyOverride = DEFAULT_CURRENCY_OVERRIDE;
        }
        return currencyOverride.lowestSubunit;
    }

    /**
     * Get a string starting with currency symbol then the amount
     *
     * @param currencyCode
     * @param amountInCents The amount that is in currency unit * 10^fraction digit
     * @param locale
     * @return
     */
    public static String getCurrencySymbolWithAmountFromCents(String currencyCode, int amountInCents, @Nullable Locale locale) {
        Currency currency = Currency.getInstance(currencyCode);
        int subunit = getSubunit(currency);
        return getCurrencySymbolWithAmount(currencyCode, 1f * amountInCents / subunit, locale);
    }

    public static String getCurrencySymbolWithAmountFromCents(String currencyCode, int amountCents) {
        return getCurrencySymbolWithAmountFromCents(currencyCode, amountCents, null);
    }


    /**
     * Get a string starting with currency symbol then the amount.
     *
     * @param currencyCode
     * @param amount       The amount that is in currency unit and any decimal places
     * @param locale
     * @return
     */
    public static String getCurrencySymbolWithAmount(String currencyCode, float amount, @Nullable Locale locale) {
        Currency currency = Currency.getInstance(currencyCode);
        int decimalPlaces = currency.getDefaultFractionDigits();
        return Strings.format("%s%." + currency.getDefaultFractionDigits() + "f",
                getCurrencySymbol(currency, locale),
                amount, decimalPlaces);
    }

    public static String getCurrencySymbolWithAmount(String currencyCode, float amount) {
        return getCurrencySymbolWithAmount(currencyCode, amount, null);
    }


    /**
     * Get the rounding fraction digit of the currency but with the option for a global override by
     * using {@link #addCurrencyOverride(Currency, CurrencyOverride)}
     *
     * @param currency
     * @return
     */
    public static int getCurrencyFractionDigits(@Nullable Currency currency) {
        CurrencyOverride currencyOverride;
        if (currency != null) {
            if ((currencyOverride = overrideMap.get(currency)) != null && currencyOverride.fractionDigits != null) {
                return currencyOverride.fractionDigits.intValue();
            } else {
                return currency.getDefaultFractionDigits();
            }
        } else {
            return 2;
        }
    }

    /**
     * Return the currency symbol of a currency object but with the option for a global override by
     * using {@link #addCurrencyOverride(Currency, CurrencyOverride)}
     *
     * @param currency
     * @param locale
     * @return
     */
    public static String getCurrencySymbol(@Nullable Currency currency, @Nullable Locale locale) {
        CurrencyOverride currencyOverride;
        if (currency != null) {
            if ((currencyOverride = overrideMap.get(currency)) != null && currencyOverride.symbol != null) {
                return currencyOverride.symbol;
            } else {
                return locale == null ? currency.getSymbol() : currency.getSymbol(locale);
            }
        } else {
            return "$";
        }
    }

    /**
     * Allows you to override all fields provided in {@link CurrencyOverride} param, so that
     * All method in this class {@link CurrencyUtil} will return overriden values
     *
     * @param currency
     * @param currencyOverride
     */
    public static void addCurrencyOverride(Currency currency, CurrencyOverride currencyOverride) {
        overrideMap.put(currency, currencyOverride);
    }
}
