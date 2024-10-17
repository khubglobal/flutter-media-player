package com.easternblu.khub.common.util;

import com.easternblu.khub.common.CommonTest;
import com.easternblu.khub.common.model.Track;

import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by pan on 31/3/17.
 */

public class StringsTest extends CommonTest {

    @Test
    public void testFormat() {
        float number = 3.3333f;
        String numberFormat = "%2.2f";
        assertEquals("Basic number format", Strings.format(numberFormat, number), "3.33");
        assertEquals("Basic number format in foreign language", Strings.format(Locale.GERMANY, numberFormat, number), "3,33");
    }

    final String nullString = null;
    final String emptyString = "";
    final String spaceString = " ";
    final String nonEmptyString = "apple";
    final String a = "a", A = "A", b = "b", B = "B", lowerA = "a";

    @Test
    public void testNulls() {


        assertTrue("Should be null", Strings.isNullOrEmpty(nullString));
        assertTrue("Should be empty", Strings.isNullOrEmpty(emptyString));
        assertFalse("Should be non-empty", Strings.isNullOrEmpty(spaceString));
        assertFalse("Should be non-empty", Strings.isNullOrEmpty(nonEmptyString));


        assertFalse("Should be null", Strings.isNotEmpty(nullString));
        assertFalse("Should be empty", Strings.isNotEmpty(emptyString));
        assertTrue("Should be non-empty", Strings.isNotEmpty(spaceString));
        assertTrue("Should be non-empty", Strings.isNotEmpty(nonEmptyString));
    }

    @Test
    public void testSame() {

        assertTrue("Should be same", Strings.isNotNullAndEquals(a, lowerA));
        assertFalse("Should not be same", Strings.isNotNullAndEquals(a, A));
        assertFalse("Should not be same", Strings.isNotNullAndEquals(a, b));
        assertFalse("Should not be same", Strings.isNotNullAndEquals(nullString, nullString));
        assertFalse("Should not be same", Strings.isNotNullAndEquals(nullString, emptyString));
        assertFalse("Should not be same", Strings.isNotNullAndEquals(nullString, a));

        assertTrue("Should be same", Strings.isNotNullAndEqualsIgnoreCase(a, A));

    }


    @Test
    public void testJoin() {
        assertEquals("Should be same", "1,2,3,4", Strings.join(",", "1", "2", "3", "4"));
        assertEquals("Should be same", "1,2,4", Strings.join(",", true, true, "1", "", "2", null, "4"));
        assertEquals("Should be same", "1,,2,4", Strings.join(",", true, false, "1", "", "2", null, "4"));
        assertEquals("Should be same", "1,,2,null,4", Strings.join(",", false, false, "1", "", "2", null, "4"));

    }

    @Test
    public void testOthers() throws NoSuchAlgorithmException {

        assertEquals("Should be same", "1,2,3,4", Strings.toString(new Integer[]{1, 2, null, 3, 4}, ",", true, Strings.getToStringConverter(Integer.class)));

        assertEquals("Should be same", "+", Strings.urlEncodeUTF8(" ")); // could also be %20

        assertEquals("Should be same", a, Strings.getFirstNotNullOrEmpty(nullString, emptyString, a, b, A, B));

        assertEquals("Should be same", a, Strings.STRING_CONVERTER.convert(a));

        assertEquals("Should be same", "1", Strings.getToStringConverter(Integer.class).convert(new Integer(1)));

        assertEquals("Should be same", "01", Strings.bytesToHex(new byte[]{0x01}));
        assertEquals("Should be same", "79", Strings.bytesToHex(new byte[]{0x79}));
        assertEquals("Should be same", "FF", Strings.bytesToHex(new byte[]{-1}));
        assertEquals("Should be same", "80", Strings.bytesToHex(new byte[]{-128}));

        String plainText = "Hello";
        // assertEquals("Should be same", Strings.getHex(plainText.getBytes()), Strings.getHex2(plainText.getBytes()));
        assertEquals("Should be same", plainText, new String(Strings.getBytesFromHex(Strings.bytesToHex(plainText.getBytes()))));

        assertEquals("Should be same", Strings.getHex(plainText.getBytes()), Strings.bytesToHex(plainText.getBytes()));


        assertEquals("Should be same", "532eaabd9574880dbf76b9b8cc00832c20a6ec113d682299550d7a6e0f345e25", Strings.bytesToHex(Strings.getSHA256("Test")).toLowerCase());

        assertEquals("Should be same", "Apple", Strings.capitalize("apple"));

        assertEquals("Should be same", "aaa", Strings.repeat(a, 3));


        String abcd = "a,b,c, d";
        List<String> splitResult = Strings.splitByString(abcd, ',', true);

        assertEquals("Should be same", 4, splitResult.size());
        assertEquals("Should be same", "a", splitResult.get(0));
        assertEquals("Should be same", "c", splitResult.get(2));
        assertEquals("Should be same", "d", splitResult.get(3));
        abcd = "a+b+ c+ d";
        splitResult = Strings.splitByString(abcd, '+', true);
        assertEquals("Should be same", 4, splitResult.size());
        assertEquals("Should be same", "a", splitResult.get(0));
        assertEquals("Should be same", "c", splitResult.get(2));
        assertEquals("Should be same", "d", splitResult.get(3));


        abcd = "a+a-c,+s,b+ c+ d";
        splitResult = Strings.splitByString(abcd, "+-, ".toCharArray(), true);
        assertEquals("Should be same", 7, splitResult.size());

        assertEquals("Should be same", "a", splitResult.get(0));
        assertEquals("Should be same", "a", splitResult.get(1));
        assertEquals("Should be same", "c", splitResult.get(2));
        assertEquals("Should be same", "s", splitResult.get(3));
        assertEquals("Should be same", "b", splitResult.get(4));
        assertEquals("Should be same", "c", splitResult.get(5));
        assertEquals("Should be same", "d", splitResult.get(6));


        String lazyArtistName = "Pan+An^Dendrik,kelvin";
        String normalizedArtistName = "Pan, An, Dendrik, kelvin";
        String resultNormalized = Lists.asString(Strings.splitByString(lazyArtistName, Track.ARTIST_LAZY_DELIMS.toCharArray(), true), ", ", true, Strings.STRING_CONVERTER);//  ; //lazyArtistName.replace(Track.ARTIST_LAZY_DELIMS, ", ");
        assertEquals("Should be same", normalizedArtistName, resultNormalized);
    }


}
