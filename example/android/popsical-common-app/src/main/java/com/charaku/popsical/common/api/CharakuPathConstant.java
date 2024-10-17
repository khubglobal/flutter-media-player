package com.easternblu.khub.common.api;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Path and parameter keys
 * Created by leechunhoe on 4/7/16.
 */
public interface CharakuPathConstant {

    /**
     * Decide this on app side
     */
    @Deprecated
    public static final String VERSION = "/v1";

    public static final String JSON = ".json";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String OAUTH = "/oauth"; // refresh
    public static final String AUTH = "/auth"; // login
    public static final String SESSIONS = "/sessions";
    public static final String APP_VERSION = "/app_version";
    public static final String _VERSION_NAME = "version_name";
    public static final String _VERSION_CODE = "version_code";
    public static final String _CHANGE_NOTES = "change_notes";

    //authentication and authorization
    public static final String AUTHORIZE = "/authorize";
    public static final String TOKEN = "/token";
    public static final String DEVICES = "/devices";
    public static final String USER = "/user";
    public static final String USERS = "/users";

    public static final String CLIENT_ID_TITLE = "client_id";
    public static final String REDIRECT_URI_TITLE = "redirect_uri";
    public static final String RESPONSE_TYPE_TITLE = "response_type";
    public static final String STATE_TITLE = "state";
    public static final String SCOPE_TITLE = "scope";

    //models
    public static final String ADD_NEXT = "/add_next";
    public static final String PLAYQUEUE = "/play_queue";
    public static final String PLAYLISTS = "/playlists";
    public static final String PROFILES = "/profiles";
    public static final String TRACKS = "/tracks";
    public static final String REQUEST = "/request";
    public static final String ARTISTS = "/artists";
    public static final String ME = "/me";
    public static final String LISTS = "/lists";
    public static final String POPULAR = "/popular";
    public static final String GENRES = "/genres";
    public static final String LANGUAGES = "/languages";
    public static final String TOP_LANGUAGES = "/top_languages";
    public static final String CURRENT = "/current";
    public static final String DELETE_ALL = "/delete_all";

    //substitutions
    public static final String ID_SUB = ":id";
    public static final String PLAYLIST_ID_SUB = ":playlist_id";
    public static final String PROFILE_ID_SUB = ":profile_id";
    public static final String PLAYLIST_TRACK_ID_SUB = ":playlist_track_id";
    public static final String TRACK_ID_SUB = ":track_id";
    public static final String ARTIST_ID_SUB = ":artist_id";
    public static final String USER_ID_SUB = ":user_id";
    public static final String PAGE_SUB = ":page";
    public static final String DEVICE_ID_SUB = ":device_id";
    public static final String VIDEO_ID_SUB = ":video_id";

    public static final String _ID = "id";
    public static final String _PUSH_NOTIFICATION_ID = "push_notification_id";
    public static final String _FROM = "from";
    public static final String _TO = "to";
    public static final String _ENABLED = "enabled";
    public static final String _PROFILE_ID = "profile_id";
    public static final String _CURRENT = "current";
    public static final String _NEXT = "next";
    public static final String _PREV = "prev";
    public static final String _PRIORITY = "priority";
    public static final String _PLAYLIST_ID = "playlist_id";
    public static final String _HISTORY_PLAYLIST_ID = "history_playlist_id";
    public static final String _PLAYLIST_TRACK_ID = "playlist_track_id";
    public static final String _PLAY_DURATION = "play_duration";
    public static final String _PLAYLISTS = "playlists";
    public static final String _TRACKS = "tracks";
    public static final String _TRACK = "track";
    public static final String _TRACK_ID = "track_id";
    public static final String _PLAY_QUEUE_ID = "play_queue_id";
    public static final String _PLAY_QUEUE_TRACK_ID = "play_queue_track_id";
    public static final String _PLAN = "plan";
    public static final String _SUBSCRIPTION_ID = "subscription_id";
    public static final String _VIDEO = "video";
    public static final String _VIDEO_ID = "video_id";
    public static final String _VIDEO_URL = "video_url";
    public static final String _QUEUE = "queue";
    public static final String _INCLUDE_BODY = "include_body";
    public static final String _ERROR = "error";
    public static final String _HITS = "hits";
    public static final String _DEVICE = "device";
    public static final String _DEVICE_ID = "device_id";
    public static final String _DEVICE_UID = "device_uid";
    public static final String _TV_DEVICE_UID = "tv_device_uid";
    public static final String _DEVICE_TOKEN = "device_token";
    public static final String _DEVICE_TYPE = "device_type";
    public static final String _CORRECT_SONG_TITLE = "correct_song_title";
    public static final String _OTHER_FEEDBACK = "other_feedback";
    public static final String _TYPE = "type";
    public static final String _IP_ADDRESS = "ip_address";
    public static final String _MAC_ADDRESS = "mac_address";
    public static final String _ETHERNET_MAC_ADDRESS = "ethernet_mac";
    public static final String _WIFI_SSID = "wifi_ssid";
    public static final String _WIFI_BSSID = "wifi_bssid";
    public static final String _HARDWARE_TYPE = "hardware_type";
    public static final String _HARDWARE_HASH = "hardware_hash";

    public static final String _PORT = "port";
    public static final String _OS = "os";
    public static final String _OS_VERSION = "os_version";
    public static final String _DEVICES = "devices";
    public static final String _POSITION = "position";
    public static final String _ACCESS_TOKEN = "access_token";
    public static final String _EXPIRES_AT = "expires_at";
    public static final String _EXPIRES_IN = "expires_in";
    public static final String _SCOPE = "scope";
    public static final String _ARTIST_ID = "artist_id";
    public static final String _IMAGES = "images";
    public static final String _SKIPPABLE_RANGES = "skippable_ranges";
    public static final String _ERR_CODE = "err_code";
    public static final String _ERRORS = "errors";
    public static final String _NUMBER = "number";
    public static final String _TITLE = "title";
    public static final String _MESSAGE = "message";
    public static final String _LINK = "link";
    public static final String _VOCAL_CHANNEL = "vocal_channel";
    public static final String _ALT_TITLE = "alt_title";
    public static final String _LANG_CODE = "lang_code";
    public static final String _LANG_CODES = "lang_codes";
    public static final String _RUNTIME = "runtime";
    public static final String _RELEASE_DATE = "release_date";
    public static final String _SOURCE = "source";
    public static final String _HAS_VIDEO = "has_video";
    public static final String _DESCRIPTION = "description";
    public static final String _HAS_STREAM = "has_stream";
    public static final String _DASH = "dash";
    public static final String _P720 = "p720";
    public static final String _VOCAL_URL = "vocal_url";
    public static final String _NON_VOCAL_URL = "non_vocal_url";
    public static final String _HLS = "hls";
    public static final String _ARTIST = "artist";
    public static final String _ARTISTS = "artists";
    public static final String _TRACK_ARTISTS = "track_artists";
    public static final String _ARTIST_NAME = "artist_name";
    public static final String _ARTISTS_NAME = "artists_name";
    public static final String _TAGS = "tags";
    public static final String _BLOCK = "block";
    public static final String _BLOCK_REASON = "block_reason";
    public static final String _POSTER = "poster";
    public static final String _URL = "url";
    public static final String _TV_IP_ADDRESS = "tv_ip_address";
    public static final String _TV_UID = "tv_uid";
    public static final String _TV_PORT = "tv_port";
    public static final String _TOTAL = "total";
    public static final String _TOTAL_FORWARD = "total_forward";
    public static final String _TOTAL_TRACKS = "total_tracks";
    public static final String _INCLUDES = "includes";
    public static final String _LANG = "lang";
    public static final String _LANGUAGE = "language";
    public static final String _LANGUAGE_NATIVE = "language_native";
    public static final String _LRC_URL = "lrc_url";
    public static final String _LINES = "lines";
    public static final String _ALT_LINES = "alt_lines";
    public static final String _PHONETICS = "phonetics";
    public static final String _WRITE_SYSTEM_CODE = "write_system_code";
    public static final String _WRITE_SYSTEM = "write_system";
    public static final String _WRITE_SYSTEM_NATIVE = "write_system_native";
    public static final String _TRANSLATIONS = "translations";
    public static final String _FCM_DEVICE_TOKEN = "fcm_device_token";

    public static final String _POSTER_URL = "poster_url";
    public static final String _ICON_URL = "icon_url";
    public static final String _PAGE = "page";
    public static final String _PER_PAGE = "per_page";
    public static final String _VARIANT = "variant";
    public static final String _META = "meta";
    public static final String _CURRENT_PAGE = "current_page";
    public static final String _GENRES = "genres";
    public static final String _GENRE = "genre";
    public static final String _NATIVE_NAME = "native_name";
    public static final String _FORCE_ADD = "force_add";
    public static final String _GENDER = "gender";
    public static final String _CLIENT_ID = "client_id";
    public static final String _CLIENT_SECRET = "client_secret";
    public static final String _PROFILE_PIC = "profile_pic";
    public static final String _FIRST_NAME = "first_name";
    public static final String _LAST_NAME = "last_name";
    public static final String _FULL_NAME = "full_name";
    public static final String _NUM_PAGES = "num_pages";
    public static final String _ADDED_BY = "added_by";
    public static final String _USER_ID = "user_id";
    public static final String _UPDATED_AT = "updated_at";
    public static final String _PREMIUM = "premium";
    public static final String _EMAIL = "email";
 
    public static final String _USER_TYPE = "user_type";


    public static final String _PROFILES = "profiles";
    public static final String _URLS = "urls";
    public static final String _PLAYLIST = "playlist";
    public static final String _PASSWORD = "password";
    public static final String _PASSWORD_CONFIRMATION = "password_confirmation";
    public static final String _SAMPLE_LINK = "sample_link";
    public static final String _DATA = "data";
    public static final String IS_CURRENT = "is_current";
    public static final String _REASON = "reason";
    public static final String _ARTIST_GENDER = "artist_gender";
    public static final String _COUNTRY = "country";
    public static final String _ARTIST_CATEGORY_CODE = "artist_category_code";
    public static final String _STATUS = "status";
    public static final String _SUBSCRIPTION_STATUS_CODE = "status_code";
    public static final String _MODEL = "model";
    public static final String _HARDWARE = "hardware";
    public static final String _TOTAL_TRACKS_ADDED = "total_tracks_added";
    public static final String _CLEAR_ALL = "clear_all";

    // For remote lyrics feature
    public static final String _IS_ORIGINAL = "is_original";
    public static final String _LYRICS_COUNT = "lyrics_count";
    public static final String _TIME_REMAINING = "time_remaining";
    public static final String _CREATED_AT = "created_at";
    public static final String _TRACK_RUNTIME = "track_runtime";
    public static final String _PLAY_TIME = "play_time";
    public static final String _PAYLOAD = "payload";
    public static final String _TIMESTAMP = "timestamp";
    public static final String _BUSINESS_ROLE = "business_role";


    public static final String _ADDRESS_LINE1 = "line1";
    public static final String _ADDRESS_CITY = "city";
    public static final String _ADDRESS_STATE = "state";
    public static final String _ADDRESS_ZIP = "zip";


    public static final String _KEY = "key";
    public static final String _NAME = "name";
    public static final String _ALT_NAME = "alt_name";


    public static final String _PHONE = "phone";
    public static final String _COUPON_ID = "coupon_id";
    public static final String _PLAN_ID = "plan_id";
    public static final String _ADDON_ID = "addon_id";

    public static final String _CC_NUMBER = "number";
    public static final String _CC_EXPIRY_MONTH = "expiry_month";
    public static final String _CC_EXPIRY_YEAR = "expiry_year";
    public static final String _CC_CVV = "cvv";
    public static final String _CC_COUNTRY = "country";

    public static final String _MUSIXMATCH_ID = "musixmatch_id";
    public static final String _MUSIXMATCH_LYRICS_ID = "musixmatch_lyrics_id";
    public static final String _MUSIXMATCH_SUBTITLE_ID = "musixmatch_subtitle_id";

    public static final String _EVENT_TYPE = "event_type";

    public static final String _COUNTRY_BLOCKED = "country_blocked";
    public static final String _PLATFORM_BLOCKED = "platform_blocked";
    public static final String _PREMIUM_ACCESS_BLOCKED = "premium_access_blocked";
    public static final String _TIME_LAPSED_BLOCKED = "time_lapsed_blocked";

    public static final String _SHORT_CODE = "short_code";

    public static final String _ALGOLIA_ATTRIBUTE_TITLE = "title";
    public static final String _ALGOLIA_ATTRIBUTE_ALT_TITLE = "alt_title";
    public static final String _ALGOLIA_ATTRIBUTE_ALT_NAME = "alt_name";
    public static final String _ALGOLIA_ATTRIBUTE_POSTER_URL = "poster_url";
    public static final String _ALGOLIA_ATTRIBUTE_ARTISTS = "artists";
    public static final String _ALGOLIA_ATTRIBUTE_TRACK_ARTISTS = "track_artists";
    public static final String _ALGOLIA_ATTRIBUTE_TRACK_ALT_ARTISTS = "track_alt_artists";

    public static final String _ALGOLIA_ATTRIBUTE_LANG_CODE = "lang_code";
    public static final String _ALGOLIA_ATTRIBUTE_OBJECT_ID = "objectID";
    public static final String _ALGOLIA_ATTRIBUTE_PREMIUM = _PREMIUM;

    public static final String _ALGOLIA_ATTRIBUTE_VALUE = "value";
    public static final String _ALGOLIA_ATTRIBUTE_MATCH_LEVEL = "match_level";
    public static final String _ALGOLIA_ATTRIBUTE_FULLY_HIGHLIGHTED = "fully_highlighted";
    public static final String _ALGOLIA_ATTRIBUTE_MATCHED_WORDS = "matched_words";
    public static final String _ALGOLIA_ATTRIBUTE_HIGHLIGHT_RESULT = "highlight_result";

    public static final String _ALGOLIA_ATTRIBUTE_NAME = "name";
    public static final String _ALGOLIA_ATTRIBUTE_TYPE = "type";

    public static final String _SUBSCRIPTION_NAME = "name";
    public static final String _SUBSCRIPTION_INVOICE_NAME = "invoice_name";
    public static final String _SUBSCRIPTION_DESCRIPTION = "description";
    public static final String _SUBSCRIPTION_TYPE = "type";
    public static final String _SUBSCRIPTION_PRICE = "price";
    public static final String _SUBSCRIPTION_PERIOD = "period";
    public static final String _SUBSCRIPTION_UNIT = "unit";
    public static final String _SUBSCRIPTION_PERIOD_UNIT = "period_unit";
    public static final String _SUBSCRIPTION_TRIAL_PERIOD = "trial_period";
    public static final String _SUBSCRIPTION_TRIAL_PERIOD_UNIT = "trial_period_unit";
    public static final String _SUBSCRIPTION_CHARGE_MODEL = "charge_model";
    public static final String _SUBSCRIPTION_CHARGE_TYPE = "charge_type";
    public static final String _SUBSCRIPTION_FREE_QUANITY = "free_quantity";
    public static final String _SUBSCRIPTION_STATUS = "status";
    public static final String _SUBSCRIPTION_ENABLED_IN_HOSTED_PAGES = "enabled_in_hosted_pages";
    public static final String _SUBSCRIPTION_ENABLED_IN_PORTAL = "enabled_in_portal";
    public static final String _SUBSCRIPTION_UPDATED_AT = "updated_at";
    public static final String _SUBSCRIPTION_RES_VERSION = "resource_version";
    public static final String _SUBSCRIPTION_CURRENCY_CODE = "currency_code";
    public static final String _SUBSCRIPTION_ALLOWED_FIELDS = "allowed_fields";

    public static final String _SUBSCRIPTION_DISPLAY_TITLE = "display_title";
    public static final String _SUBSCRIPTION_DISPLAY_TITLE_ALT = "display_title_alt";
    public static final String _SUBSCRIPTION_DISPLAY_HEADER = "display_header";
    public static final String _SUBSCRIPTION_DISPLAY_DESCRIPTION = "display_description";


    // Note: this algolia attribute doesn't work. It is never returned.
    // algolia has no access to user premium or geo status, so it will
    // never know if it is blocked for the user.
    // don't use it.
    @Deprecated
    public static final String _ALGOLIA_ATTRIBUTE_BLOCK = _BLOCK;


    public static final String _POPSICAL_SIMPLEDATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    public static final String GENDER_MALE = "m";
    public static final String GENDER_FEMALE = "f";
    public static final String GENDER_MIX = "mixed";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({GENDER_MALE, GENDER_FEMALE, GENDER_MIX})
    public @interface ArtistGender {
    }

    public static final String INCLUDE_TRACKS = "tracks";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({INCLUDE_TRACKS})
    public @interface IncludeType {
    }
}
