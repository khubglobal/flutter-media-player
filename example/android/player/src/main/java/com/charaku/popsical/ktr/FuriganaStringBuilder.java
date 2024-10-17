package com.easternblu.khub.ktr;


import com.easternblu.khub.ktr.view.Utils;

/**
 * Used to build string supported by {@link com.easternblu.khub.ktr.view.FuriganaStrokeTextView}
 * Created by pan on 25/1/18.
 */
public class FuriganaStringBuilder {

    private StringBuilder sb = new StringBuilder();

    public FuriganaStringBuilder() {
    }

    public StringBuilder append(String text, String furigana) {
        return sb.append(Utils.format("{%1$s;%2$s}", text, furigana));
    }

    /**
     * True if this text
     *
     * @param text
     * @return
     */
    public static boolean containsFuriganaMarkup(String text) {
        int semiColonPos, startBracketPos;
        return text != null && !(text = text.trim()).isEmpty() &&
                (startBracketPos = text.indexOf('{')) > 0 &&
                text.indexOf("}") > (semiColonPos = text.indexOf(';')) && semiColonPos > startBracketPos;
    }


    public StringBuilder append(Object obj) {
        return sb.append(obj);
    }

    public StringBuilder append(String str) {
        return sb.append(str);
    }

    public StringBuilder append(StringBuffer sb) {
        return this.sb.append(sb);
    }

    public StringBuilder append(CharSequence s) {
        return sb.append(s);
    }

    public StringBuilder append(CharSequence s, int start, int end) {
        return sb.append(s, start, end);
    }

    public StringBuilder append(char[] str) {
        return sb.append(str);
    }

    public StringBuilder append(char[] str, int offset, int len) {
        return sb.append(str, offset, len);
    }

    public StringBuilder append(boolean b) {
        return sb.append(b);
    }

    public StringBuilder append(char c) {
        return sb.append(c);
    }

    public StringBuilder append(int i) {
        return sb.append(i);
    }

    public StringBuilder append(long lng) {
        return sb.append(lng);
    }

    public StringBuilder append(float f) {
        return sb.append(f);
    }

    public StringBuilder append(double d) {
        return sb.append(d);
    }

    public StringBuilder appendCodePoint(int codePoint) {
        return sb.appendCodePoint(codePoint);
    }

    public StringBuilder delete(int start, int end) {
        return sb.delete(start, end);
    }

    public StringBuilder deleteCharAt(int index) {
        return sb.deleteCharAt(index);
    }

    public StringBuilder replace(int start, int end, String str) {
        return sb.replace(start, end, str);
    }

    public StringBuilder insert(int index, char[] str, int offset, int len) {
        return sb.insert(index, str, offset, len);
    }

    public StringBuilder insert(int offset, Object obj) {
        return sb.insert(offset, obj);
    }

    public StringBuilder insert(int offset, String str) {
        return sb.insert(offset, str);
    }

    public StringBuilder insert(int offset, char[] str) {
        return sb.insert(offset, str);
    }

    public StringBuilder insert(int dstOffset, CharSequence s) {
        return sb.insert(dstOffset, s);
    }

    public StringBuilder insert(int dstOffset, CharSequence s, int start, int end) {
        return sb.insert(dstOffset, s, start, end);
    }

    public StringBuilder insert(int offset, boolean b) {
        return sb.insert(offset, b);
    }

    public StringBuilder insert(int offset, char c) {
        return sb.insert(offset, c);
    }

    public StringBuilder insert(int offset, int i) {
        return sb.insert(offset, i);
    }

    public StringBuilder insert(int offset, long l) {
        return sb.insert(offset, l);
    }

    public StringBuilder insert(int offset, float f) {
        return sb.insert(offset, f);
    }

    public StringBuilder insert(int offset, double d) {
        return sb.insert(offset, d);
    }

    public int indexOf(String str) {
        return sb.indexOf(str);
    }

    public int indexOf(String str, int fromIndex) {
        return sb.indexOf(str, fromIndex);
    }

    public int lastIndexOf(String str) {
        return sb.lastIndexOf(str);
    }

    public int lastIndexOf(String str, int fromIndex) {
        return sb.lastIndexOf(str, fromIndex);
    }

    public StringBuilder reverse() {
        return sb.reverse();
    }

    public int length() {
        return sb.length();
    }

    public int capacity() {
        return sb.capacity();
    }

    public void ensureCapacity(int minimumCapacity) {
        sb.ensureCapacity(minimumCapacity);
    }

    public void trimToSize() {
        sb.trimToSize();
    }

    public void setLength(int newLength) {
        sb.setLength(newLength);
    }

    public char charAt(int index) {
        return sb.charAt(index);
    }

    public int codePointAt(int index) {
        return sb.codePointAt(index);
    }

    public int codePointBefore(int index) {
        return sb.codePointBefore(index);
    }

    public int codePointCount(int beginIndex, int endIndex) {
        return sb.codePointCount(beginIndex, endIndex);
    }

    public int offsetByCodePoints(int index, int codePointOffset) {
        return sb.offsetByCodePoints(index, codePointOffset);
    }

    public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
        sb.getChars(srcBegin, srcEnd, dst, dstBegin);
    }

    public void setCharAt(int index, char ch) {
        sb.setCharAt(index, ch);
    }

    public String substring(int start) {
        return sb.substring(start);
    }

    public CharSequence subSequence(int start, int end) {
        return sb.subSequence(start, end);
    }

    public String substring(int start, int end) {
        return sb.substring(start, end);
    }

    @Override
    public String toString() {
        return sb.toString();
    }
}
