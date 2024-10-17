package com.easternblu.khub.common.api;

import com.easternblu.khub.common.CharakuConfig;
import com.easternblu.khub.common.Common;

import static com.easternblu.khub.common.Common.AND;
import static com.easternblu.khub.common.Common.EQUALS;
import static com.easternblu.khub.common.Common.QUESTION_MARK;
import static com.easternblu.khub.common.Common.SLASH;
import static com.easternblu.khub.common.api.CharakuPathConstant.APP_VERSION;
import static com.easternblu.khub.common.api.CharakuPathConstant.ARTISTS;
import static com.easternblu.khub.common.api.CharakuPathConstant.ARTIST_ID_SUB;
import static com.easternblu.khub.common.api.CharakuPathConstant.AUTH;
import static com.easternblu.khub.common.api.CharakuPathConstant.AUTHORIZE;
import static com.easternblu.khub.common.api.CharakuPathConstant.CURRENT;
import static com.easternblu.khub.common.api.CharakuPathConstant.DELETE_ALL;
import static com.easternblu.khub.common.api.CharakuPathConstant.DEVICES;
import static com.easternblu.khub.common.api.CharakuPathConstant.GENRES;
import static com.easternblu.khub.common.api.CharakuPathConstant.JSON;
import static com.easternblu.khub.common.api.CharakuPathConstant.LANGUAGES;
import static com.easternblu.khub.common.api.CharakuPathConstant.LISTS;
import static com.easternblu.khub.common.api.CharakuPathConstant.ME;
import static com.easternblu.khub.common.api.CharakuPathConstant.OAUTH;
import static com.easternblu.khub.common.api.CharakuPathConstant.PLAYLISTS;
import static com.easternblu.khub.common.api.CharakuPathConstant.PLAYLIST_ID_SUB;
import static com.easternblu.khub.common.api.CharakuPathConstant.PLAYQUEUE;
import static com.easternblu.khub.common.api.CharakuPathConstant.POPULAR;
import static com.easternblu.khub.common.api.CharakuPathConstant.PROFILES;
import static com.easternblu.khub.common.api.CharakuPathConstant.PROFILE_ID_SUB;
import static com.easternblu.khub.common.api.CharakuPathConstant.REQUEST;
import static com.easternblu.khub.common.api.CharakuPathConstant.SESSIONS;
import static com.easternblu.khub.common.api.CharakuPathConstant.TOKEN;
import static com.easternblu.khub.common.api.CharakuPathConstant.TOP_LANGUAGES;
import static com.easternblu.khub.common.api.CharakuPathConstant.TRACKS;
import static com.easternblu.khub.common.api.CharakuPathConstant.TRACK_ID_SUB;
import static com.easternblu.khub.common.api.CharakuPathConstant.USERS;
import static com.easternblu.khub.common.api.CharakuPathConstant.VERSION;
import static com.easternblu.khub.common.api.CharakuPathConstant._FORCE_ADD;
import static com.easternblu.khub.common.api.CharakuPathConstant._INCLUDES;
import static com.easternblu.khub.common.api.CharakuPathConstant._PAGE;
import static com.easternblu.khub.common.api.CharakuPathConstant._PER_PAGE;
import static com.easternblu.khub.common.api.CharakuPathConstant._TRACKS;

/**
 * Popsical API end-points
 * Deprecated, copy this entire class to your own app
 *
 *
 * use {@link PopsicalApi} instead
 * Created by leechunhoe on 4/7/16.
 */
@Deprecated
public class CharakuApi {
    private static final String CHARAKU_API_DOMAIN_LIVE = "https://app.popsical.tv";
    private static final String CHARAKU_API_DOMAIN_STAGING = "https://app-staging.popsical.tv";

    @Deprecated
    public static final String CHARAKU_API_DOMAIN = CharakuConfig.IS_PRODUCTION ? CHARAKU_API_DOMAIN_LIVE : CHARAKU_API_DOMAIN_STAGING;
//    public static final String CHARAKU_API_DOMAIN = "http://digimon-worker-staging.ap-southeast-1.elasticbeanstalk.com";

    // Authentication and authorization
    public static final String AUTHORIZE_API = CharakuApi.CHARAKU_API_DOMAIN + OAUTH +
            AUTHORIZE;

    public static final int STATUS_CODE_UNAUTHORIZED = 401;

    public static final String APP_VERSION_API = CHARAKU_API_DOMAIN + VERSION + APP_VERSION + JSON;

    public static final String AUTHORIZE_RESULT_API = CHARAKU_API_DOMAIN + OAUTH +
            AUTHORIZE + "/%s";
    public static final String TOKEN_API = CHARAKU_API_DOMAIN + OAUTH +
            TOKEN;
    public static final String REFRESH_ACCESS_TOKEN_API = CHARAKU_API_DOMAIN + OAUTH +
            TOKEN;
    public static final String TV_SHORT_CODE_API = CHARAKU_API_DOMAIN + VERSION + AUTH +
            TOKEN + JSON;

    public static final String REGISTER_DEVICE_API = CHARAKU_API_DOMAIN + VERSION
            + ME + DEVICES + JSON;

    //Profiles
    public static final String PROFILE_ME_API = CHARAKU_API_DOMAIN + VERSION +
            ME + JSON;
    public static final String PROFILE_GET_API = CHARAKU_API_DOMAIN + VERSION +
            PROFILES + PROFILE_ID_SUB + JSON;
    public static final String PROFILES_LIST_API = CHARAKU_API_DOMAIN + VERSION +
            PROFILES + JSON;
    public static final String PROFILE_PLAYLISTS_API = CHARAKU_API_DOMAIN + VERSION +
            ":profile" + PLAYLISTS + JSON;

    public static final String PROFILE_PLAYLIST_TRACKS_API = CHARAKU_API_DOMAIN +
            VERSION + PROFILES + SLASH +
            PROFILE_ID_SUB + PLAYLISTS + SLASH +
            PLAYLIST_ID_SUB + TRACKS + JSON;

    // Play lists
    public static final String PLAYLISTS_LIST_API = CHARAKU_API_DOMAIN + VERSION +
            PLAYLISTS + JSON;
    public static final String PLAYLIST_GET_API = CHARAKU_API_DOMAIN + VERSION +
            PLAYLISTS + SLASH + PLAYLIST_ID_SUB + JSON;

    public static final String PLAYLIST_TRACKS_API = CHARAKU_API_DOMAIN + VERSION +
            PLAYLIST_ID_SUB + TRACKS + JSON;

    // Play-queues
    public static final String PLAYQUEUE_API = CHARAKU_API_DOMAIN + VERSION
            + ME + PLAYQUEUE + JSON;
    public static final String PLAYQUEUE_TRACK_API = CHARAKU_API_DOMAIN + VERSION
            + ME + PLAYQUEUE + TRACKS +
            JSON;
    public static final String PLAYQUEUE_TRACK_FORCE_ADD_API = CHARAKU_API_DOMAIN + VERSION
            + ME + PLAYQUEUE + TRACKS +
            JSON + QUESTION_MARK + _FORCE_ADD + EQUALS + Common.TRUE;
    public static final String PLAY_QUEUE_TRACKS_DELETE_ALL_API = CHARAKU_API_DOMAIN + VERSION
            + ME + PLAYQUEUE + DELETE_ALL +
            JSON;

    public static final String PLAYQUEUE_TOKEN_API = CHARAKU_API_DOMAIN + VERSION
            + PLAYQUEUE + TOKEN +
            JSON;
    public static final String PLAYQUEUE_AUTHORIZE_API = CHARAKU_API_DOMAIN + VERSION
            + PLAYQUEUE + AUTHORIZE +
            JSON;
    public static final String PLAY_QUEUE_DEVICES_API = CHARAKU_API_DOMAIN + VERSION +
            PLAYQUEUE + DEVICES + JSON;

    public static final String PLAY_QUEUE_TRACKS_API = CHARAKU_API_DOMAIN + VERSION +
            ME + PLAYQUEUE + TRACKS + JSON;

    public static final String PLAY_QUEUE_CURRENT_TRACK_API = CHARAKU_API_DOMAIN + VERSION +
            ME + PLAYQUEUE + CURRENT + JSON;

    // Tracks
    public static final String TRACKS_LIST_API = CHARAKU_API_DOMAIN + VERSION +
            TRACKS + JSON;
    public static final String TRACK_GET_API = CHARAKU_API_DOMAIN + VERSION +
            TRACKS + SLASH + TRACK_ID_SUB + JSON;

    // Artists
    public static final String ARTISTS_LIST_API = CHARAKU_API_DOMAIN + VERSION +
            ARTISTS + JSON;
    public static final String ARTIST_GET_API = CHARAKU_API_DOMAIN + VERSION +
            ARTISTS + SLASH + ARTIST_ID_SUB + JSON;

    // Devices
    public static final String DEVICES_API = CHARAKU_API_DOMAIN + VERSION +
            ME + DEVICES + JSON;

    // Facebook
    // POST /v1/auth/sessions.json
    public static final String FACEBOOK_LOGIN_API = CHARAKU_API_DOMAIN + VERSION +
            AUTH + SESSIONS + JSON;

    public static final String EMAIL_SIGN_UP_API = CHARAKU_API_DOMAIN + VERSION + USERS + JSON;

    public static final String EMAIL_LOGIN_API = CHARAKU_API_DOMAIN + VERSION +
            AUTH + SESSIONS + JSON;

    public static final String POPULAR_PLAYLISTS_API = CHARAKU_API_DOMAIN + VERSION
            + LISTS + POPULAR + JSON;

    public static final String POPULAR_PLAYLISTS_WITH_TRACKS_API = POPULAR_PLAYLISTS_API
            + QUESTION_MARK + _INCLUDES + EQUALS + _TRACKS;

    public static final String GENRES_API = CHARAKU_API_DOMAIN + VERSION + GENRES + JSON;

    public static final String LANGUAGES_API = CHARAKU_API_DOMAIN + VERSION + LANGUAGES + JSON;
    public static final String TOP_LANGUAGES_API = CHARAKU_API_DOMAIN + VERSION + TOP_LANGUAGES + JSON;

    public static final String GENRE_PLAY_LISTS_API = CHARAKU_API_DOMAIN + VERSION + GENRES;

    // Play queue
    public static final String PLAY_QUEUE_API = CHARAKU_API_DOMAIN + VERSION +
            ME + PLAYQUEUE + JSON;

    // Play queue current
    public static final String PLAY_QUEUE_CURRENT_API = CHARAKU_API_DOMAIN + VERSION +
            ME + PLAYQUEUE + CURRENT + JSON;

    // TODO: Update this
    public static final String SUGGEST_TRACK_API = CHARAKU_API_DOMAIN + VERSION + TRACKS + REQUEST + JSON;

    /**
     * GET /me/play_queue.json
     * http://developers.popsical.com/#!/PlayQueue/get_me_play_queue_json
     *
     * @param page    Page number
     * @param perPage Tracks per page
     * @return URL
     */
    public static String getPlayQueueApi(int page, int perPage) {
        return PLAY_QUEUE_API + QUESTION_MARK + _PAGE + EQUALS + page + AND + _PER_PAGE + EQUALS + perPage;
    }

    public static String getGenrePlayListWithTracksApi(String genre, String languageCodes, int currentPage, int playListsPerPage) {
        String query = CharakuPathConstant._INCLUDES + EQUALS + CharakuPathConstant._TRACKS
                + AND + CharakuPathConstant._LANG + EQUALS + languageCodes
                + AND + _PAGE + EQUALS + currentPage
                + Common.AND + _PER_PAGE + Common.EQUALS + playListsPerPage;
        return CharakuApi.GENRE_PLAY_LISTS_API + SLASH + genre + CharakuPathConstant.JSON + QUESTION_MARK + query;
    }
}
