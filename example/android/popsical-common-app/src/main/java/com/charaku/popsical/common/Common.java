package com.easternblu.khub.common;

import android.content.ContentResolver;

/**
 * Commonly used constants
 */
public class Common {
    public static final String EMPTY_STRING = "";
    public static final String PIPE = "|";
    public static final String SPACE = " ";
    public static final String DOUBLE_SPACE = "  ";
    public static final String QUESTION_MARK = "?";
    public static final String COMMA = ", ";
    public static final String COLON = ":";
    public static final String COLON_ = ": ";
    public static final String SEMICOLON = ";";
    public static final String SEMICOLON_ = "; ";
    public static final String COMMA_SHORT = ",";
    public static final String EQUALS = "=";
    public static final String AND = "&";
    public static final String SLASH = "/";
    public static final String DOUBLE_SLASH = "//";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String EXPIRES_IN = "expires_in"; // belong to OAUTH specification
    public static final String SCOPES = "scope"; // belong to OAUTH specification
    public static final String NEWLINE = "\n";
    public static final String UNUSED = "unused";
    public static final String WEAKER_ACCESS = "WeakerAccess";
    public static final String UNNECCESSARY_LOCAL_VARIABLE = "UnnecessaryLocalVariable";
    public static final String DOT = ".";
    public static final String DOT_ = ". ";
    public static final String CARET = "^";
    public static final String UNDERSCORE = "_";
    public static final String DASH = "-";
    public static final String ASTERISK = "*";
    public static final String QUOTE = "\"";

    public static final String USER_AGENT = "User-Agent";
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String EMAIL = "email";
    public static final String PUBLIC_PROFILE = "public_profile";
    public static final String VALUE = "value";
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String FORMAT = "format";
    public static final String WPA = "WPA";
    public static final String WEP = "WEP";

    public static final String SCOPE = "scope";

    public static final String HTTP = "http:";
    public static final String HTTPS = "https:";

    public static final String PROTOCOL_HTTP = "http://";
    public static final String PROTOCOL_HTTPS = "https://";
    public static final String PROTOCOL_ANDROID_RESOURCE = ContentResolver.SCHEME_ANDROID_RESOURCE + "://";

    public static final String OKHTTP_MEDIA_TYPE_JSON = "application/json; charset=utf-8";

    public static final String MEDIA_TYPE_TEXT_PLAIN = "text/plain";

    public static final String JSON = "json";

    public static final String SUCCESS = "success";
    public static final String ERROR = "error";
    public static final String OK = "ok";
    public static final String NONE = "none";

    public static final String PLUS = "+";

    public static final int ZERO = 0;
    public static final double HALF = 0.5;
    public static final int ONE = 1;
    public static final int EIGHT = 8;
    public static final int ONE_HUNDRED_AND_EIGHTY = 180;

    public static final int ONE_MINUTE_IN_SECONDS = 60;
    public static final int ONE_SECOND_IN_MILLISECONDS = 1000;

    public static final int HTTP_STATUS_CODE_UNPROCESSABLE_ENTITY = 422;

    public static final int ONE_K = 1000;

    public static final String ONE_K_STRING = "1k";
    public static final String ONE_K_PLUS_STRING = "1k+";

    public static final String FORMAT_DECIMAL = "%d";
    public static final String FORMAT_STRING = "%s";

    public static final String ANDROID_OS_SYSTEM_PROPERTIES = "android.os.SystemProperties";

    public static final String ANDROID = "android";

    public static final String SET = "set";

    // RSSI level for WiFi strength, 0 is the worst
    // Reference: http://www.metageek.com/training/resources/understanding-rssi.html
    public static final int RSSI_LEVEL_0 = -90;
    public static final int RSSI_LEVEL_1 = -80;
    public static final int RSSI_LEVEL_2 = -70;
    public static final int RSSI_LEVEL_3 = -67;
    public static final int RSSI_LEVEL_4 = -50; // was -30 but that is too strict, ideally level4 should be full bars

    public static final int TWO_WEEKS_IN_MILLISECONDS = 1209600000;
}

