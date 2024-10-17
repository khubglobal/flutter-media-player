package com.easternblu.khub.common;

/**
 * Constants for Popsical
 * Created by leechunhoe on 3/8/16.
 */
@SuppressWarnings({Common.UNUSED})
public class PopsicalConstant implements PopsicalRemoteToTvConstant, PopsicalTvToRemoteConstant {
    public static final String DEFAULT_URL = "http://192.168.1.1";

    public static final int PARTY_CODE_LENGTH = 6;

    public static final String TAG_POPSICAL_TV = "POPSICAL_TV";
    public static final String TAG_POPSICAL_REMOTE_ANDROID = "POPSICAL_REMOTE_ANDROID";

    public static final String ERR_CODE = "err_code";
    public static final String API_ERROR = "api_error";

    public static final int ERROR_CODE_UNAUTHORIZED = 1401;
    public static final int ERROR_CODE_FORBIDDEN = 1403;
    public static final int ERROR_CODE_NOT_FOUND = 1404;
    public static final int ERROR_CODE_INTERNAL_SERVER_ERROR = 1500;
    public static final int ERROR_CODE_MISSING_PARAMETERS = 1400;
    public static final int ERROR_CODE_ENTITY_ERROR = 1407;
    public static final int ERROR_CODE_TV_IP_NOT_AVAILABLE = 1408;
    public static final int ERROR_CODE_TRACK_ALREADY_EXIST = 1412;
    public static final int ERROR_CODE_DAILY_FREE_TIME_EXCEEDED = 1418;
    public static final int ERROR_CODE_CHARGEBEE_API_ERROR = 1419;

    public static final boolean ALLOW_VIDEO_SKIP = true;



    public static final String TAG_ERROR = "Popsical";

    //  https://github.com/charaku/digimon/blob/master/doc/api/v1/api.md#device-resource
    public static final String DEVICE_TYPE_TV = "tv";
    public static final String DEVICE_TYPE_REMOTE = "remote";

    public static final String OS_ANDROID = "android";
    public static final String OS_IOS = "ios";

    public static final String SHORT_CODE = "short_code";
    public static final String EXPIRES_IN = "expires_in";
    public static final String NONCE = "nonce";
    public static final String CLIENT_ID = "client_id";
    public static final String CLIENT_SECRET = "client_secret";
    public static final String TOKEN = "token";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String GRANT_TYPE = "grant_type";
    public static final String DEVICE = "device";
    public static final String DEVICE_ID = "device_id";
    public static final String DEVICE_UID = "device_uid";
    public static final String TV_DEVICE_UID = "tv_device_uid";
    public static final String DEVICE_NAME = "device_name";
    public static final String FACEBOOK_TOKEN = "facebook_token";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String PASSWORD_CONFIRMATION = "password_confirmation";
    public static final String ERRORS = "errors";
    public static final String OAUTH_ERROR = "error";
    public static final String OAUTH_ERROR_DESCRIPTION = "error_description";

    public static final String VERIFICATION_URL = "verification_url";

    public static final int RESPONSE_CODE_ERROR_GENERIC = 0;
    public static final int RESPONSE_CODE_ERROR_IO = 1;

    public static final int STATUS_IDLE = -1;
    public static final int STATUS_IN_PROGRESS = 0;
    public static final int STATUS_DONE = 1;

    public static final String ALGOLIA_OBJECT_ID = "objectID";
    public static final String ALGOLIA_HIGHLIGHT_RESULT = "_highlightResult";

    public static final String LANGUAGE_CODE_ENGLISH = "en";
    public static final String LANGUAGE_CODE_CHINESE = "zh";
    public static final String LANGUAGE_CODE_MALAY = "ms";
    public static final String LANGUAGE_CODE_ALL = "all";

    public static final String LANGUAGE_NAME_ENGLISH = "English";
    public static final String LANGUAGE_NAME_ALL = "All";

    public static final String SYS_KTV_TYPE = "sys.ktv.type";


    /**
     * Used for remote socket command JSON body element name
     * <p/>
     * <pre>
     * TV sends --> {"f":"tv","c":1,"v":1}
     * Remote sends --> {"f":"remote_android","c":2,"vs":"aasdbaskdjhaskjd"}
     * </pre>
     * <p/>
     * We priority efficient over readablity in this case. Because the old
     * param names were jsut to long.
     */
    //region
    //================================================================================
    //
    //================================================================================
    // short for "from"
    public static final String POPSICAL_BROADCAST_PARAM_FROM = "f";

    /**
     * Possible values of POPSICAL_BROADCAST_PARAM_FROM
     */
    public static final String POPSICAL_BROADCAST_PARAM_FROM_VALUE_TV = "tv";
    public static final String POPSICAL_BROADCAST_PARAM_FROM_VALUE_REMOTE_ANDROID = "ra";
    public static final String POPSICAL_BROADCAST_PARAM_FROM_VALUE_TV_REMOTE_IOS = "ri";
    
    // short for "code"
    public static final String POPSICAL_BROADCAST_PARAM_CODE_SIMPLIFIED = "c";

    // short for "value"
    public static final String POPSICAL_BROADCAST_PARAM_VALUE_SIMPLIFIED = "v";

    // short for "value_string"
    public static final String POPSICAL_BROADCAST_PARAM_VALUE_STRING_SIMPLIFIED = "vs";

    // short for "value_object" we added this to avoid making POPSICAL_BROADCAST_PARAM_VALUE_STRING too messy
    public static final String POPSICAL_BROADCAST_PARAM_VALUE_OBJECT_SIMPLIFIED = "vo";
    //endregion

}