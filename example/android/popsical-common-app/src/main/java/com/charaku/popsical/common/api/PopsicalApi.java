//package com.easternblu.khub.common.api;
//
//import android.content.Context;
//
//import com.easternblu.khub.common.Common;
//import com.easternblu.khub.common.R;
//
//import static com.easternblu.khub.common.Common.AND;
//import static com.easternblu.khub.common.Common.EQUALS;
//import static com.easternblu.khub.common.Common.QUESTION_MARK;
//import static com.easternblu.khub.common.Common.SLASH;
//import static com.easternblu.khub.common.api.CharakuPathConstant.ARTIST_ID_SUB;
//import static com.easternblu.khub.common.api.CharakuPathConstant.JSON;
//import static com.easternblu.khub.common.api.CharakuPathConstant.PLAYLIST_ID_SUB;
//import static com.easternblu.khub.common.api.CharakuPathConstant.PLAYLIST_TRACK_ID_SUB;
//import static com.easternblu.khub.common.api.CharakuPathConstant.PROFILE_ID_SUB;
//import static com.easternblu.khub.common.api.CharakuPathConstant.TRACK_ID_SUB;
//import static com.easternblu.khub.common.api.CharakuPathConstant._INCLUDES;
//import static com.easternblu.khub.common.api.CharakuPathConstant._PAGE;
//import static com.easternblu.khub.common.api.CharakuPathConstant._PER_PAGE;
//import static com.easternblu.khub.common.api.CharakuPathConstant._TRACKS;
//
///**
// * A place to get URL with your custom parameter(s)
// * Created by leechunhoe on 15/11/16.
// */
//@SuppressWarnings({Common.WEAKER_ACCESS, Common.UNUSED})
//public class PopsicalApi {
//    public static final int SERVER_INSTANCE_PRODUCTION = 0;
//    public static final int SERVER_INSTANCE_STAGING = 1;
//    public static final int[] SERVER_INSTANCES = {SERVER_INSTANCE_PRODUCTION, SERVER_INSTANCE_STAGING};
//
//    public static final String POPSICAL_WEB_DOMAIN = "https://www.popsical.com";
//    public static final String POPSICAL_WEB_DOMAIN_HOST = "popsical.com";
//
//    public static final String POPSICAL_API_DOMAIN_PRODUCTION = "https://api.popsical.tv";
//    public static final String POPSICAL_API_DOMAIN_STAGING = "https://api-staging.popsical.tv";
//    public static final String POPSICAL_API_DOMAIN_TEST = "http://localhost:4000";
////    public static final String POPSICAL_API_DOMAIN_PRODUCTION = "https://app.popsical.tv";
////    public static final String POPSICAL_API_DOMAIN_STAGING = "https://app-staging.popsical.tv";
//
//    protected static final String API_VERSION_ONE = "/v1";
//    protected static final String API_VERSION_TWO = "/v2";
//    protected static final String API_VERSION_THREE = "/v3";
//
//    protected static final String API_VERSION = API_VERSION_ONE;
//
//    private static final String ALGOLIA_SEARCH_TRACK_INDEX_NAME_STAGING = "Track_staging";
//    private static final String ALGOLIA_SEARCH_TRACK_INDEX_NAME_PRODUCTION = "Track_production";
//    private static final String ALGOLIA_SEARCH_ARTIST_INDEX_NAME_STAGING = "Artist_staging";
//    private static final String ALGOLIA_SEARCH_ARTIST_INDEX_NAME_PRODUCTION = "Artist_production";
//
//    /**
//     * Know which server is in use, production or staging? Depends on value of R.string.which_server
//     *
//     * @param context Context
//     * @return SERVER_INSTANCE_PRODUCTION or SERVER_INSTANCE_STAGING
//     */
//    public static int whichServer(Context context) {
//        int whichServer;
//        try {
//            whichServer = Integer.parseInt(context.getString(R.string.which_server));
//        } catch (NumberFormatException | NullPointerException e) {
//            whichServer = -1;
//            e.printStackTrace();
//        }
//        return whichServer;
//    }
//
//    /**
//     * Get full endpoint
//     *
//     * @param context     Context
//     * @param apiEndPoint API endpoint
//     * @return Full endpoint with proper domain name
//     */
//    public static String getFullEndpoint(Context context, String apiEndPoint) {
//        return getFullEndpoint(apiEndPoint, whichServer(context));
//    }
//
//    /**
//     * Get full endpoint
//     *
//     * @param apiEndPoint API endpoint
//     * @return Full endpoint with proper domain name
//     */
//    public static String getFullEndpoint(String apiEndPoint, int whichServer) {
//        switch (whichServer) {
//            case SERVER_INSTANCE_PRODUCTION:
//                return POPSICAL_API_DOMAIN_PRODUCTION + apiEndPoint;
//            case SERVER_INSTANCE_STAGING:
//                return POPSICAL_API_DOMAIN_STAGING + apiEndPoint;
//            default:
//                return Common.EMPTY_STRING;
//        }
//    }
//
//    /**
//     * Get Algolia search track index name
//     *
//     * @param context Context
//     * @return Algolia track index name
//     */
//    public static String getAlgoliaSearchTrackIndexName(Context context) {
//        int whichServer = PopsicalApi.whichServer(context);
//        return getAlgoliaSearchTrackIndexName(whichServer);
//    }
//
//    /**
//     * Get Algolia search track index name
//     *
//     * @param whichServer Server index, e.g. SERVER_INSTANCE_PRODUCTION or SERVER_INSTANCE_STAGING
//     * @return Algolia track index name
//     */
//    public static String getAlgoliaSearchTrackIndexName(int whichServer) {
//        switch (whichServer) {
//            case PopsicalApi.SERVER_INSTANCE_STAGING:
//                return ALGOLIA_SEARCH_TRACK_INDEX_NAME_STAGING;
//            case PopsicalApi.SERVER_INSTANCE_PRODUCTION:
//                return ALGOLIA_SEARCH_TRACK_INDEX_NAME_PRODUCTION;
//            default:
//                return Common.EMPTY_STRING;
//        }
//    }
//
//    /**
//     * Get Algolia search artist index name
//     *
//     * @param context Context
//     * @return Algolia artist index name
//     */
//    public static String getAlgoliaSearchArtistIndexName(Context context) {
//        int whichServer = PopsicalApi.whichServer(context);
//        return getAlgoliaSearchArtistIndexName(whichServer);
//    }
//
//    /**
//     * Get Algolia search artist index name
//     *
//     * @param whichServer Server index, e.g. SERVER_INSTANCE_PRODUCTION or SERVER_INSTANCE_STAGING
//     * @return Algolia artist index name
//     */
//    public static String getAlgoliaSearchArtistIndexName(int whichServer) {
//        switch (whichServer) {
//            case PopsicalApi.SERVER_INSTANCE_STAGING:
//                return ALGOLIA_SEARCH_ARTIST_INDEX_NAME_STAGING;
//            case PopsicalApi.SERVER_INSTANCE_PRODUCTION:
//                return ALGOLIA_SEARCH_ARTIST_INDEX_NAME_PRODUCTION;
//            default:
//                return Common.EMPTY_STRING;
//        }
//    }
//
//    // Authentication and authorization
//    public static final String AUTHORIZE_API = "/oauth/authorize";
//
//    public static final String APP_VERSION_API = API_VERSION + "/app_version.json";
//
//    public static final String AUTHORIZE_RESULT_API = "/oauth/authorize/%s";
//    public static final String TOKEN_API = "/oauth/token";
//    public static final String TOKEN_REVOKE_API = "/oauth/revoke";
//    public static final String REFRESH_ACCESS_TOKEN_API = "/oauth/token";
//    public static final String AUTH_TOKEN_API = API_VERSION + "/auth/token.json";
//
//    public static final String REGISTER_DEVICE_API = API_VERSION + "/me/devices.json";
//
//    //Profiles
//    public static final String PROFILE_ME_API = API_VERSION + "/me.json";
//    public static final String PROFILE_GET_API = API_VERSION + "/profiles/" + PROFILE_ID_SUB + ".json";
//    public static final String PROFILES_LIST_API = API_VERSION + "/profiles.json";
//    public static final String PROFILE_PLAYLISTS_API = API_VERSION + "/" + PROFILE_ID_SUB + "/playlists.json";
//
//    public static final String PROFILE_PLAYLIST_TRACKS_API = API_VERSION + "/profiles/" + PROFILE_ID_SUB + "/playlists/" + PLAYLIST_ID_SUB + "/tracks.json";
//
//    // Play lists
//    public static final String PLAYLISTS_LIST_API = API_VERSION + "/playlists.json";
//    public static final String HISTORY_PLAYLIST_API = API_VERSION_TWO + "/lists/history.json";
//    public static final String HISTORY_PLAYLIST_TRACK_API = API_VERSION_TWO + "/playlist_tracks/" + PLAYLIST_TRACK_ID_SUB + ".json";
//
//    public static final String PLAYLIST_TRACKS_API = API_VERSION + "/" + PLAYLIST_ID_SUB + "/tracks.json";
//
//    public static final String PLAYLIST_GET_API = API_VERSION + "/playlists/" + PLAYLIST_ID_SUB + ".json";
//
//    // Play-queues
//    public static final String PLAYQUEUE_API = API_VERSION + "/me/play_queue.json";
//    public static final String PLAYQUEUE_TRACK_API = API_VERSION + "/me/play_queue/tracks.json";
//    public static final String PLAYQUEUE_TRACK_FORCE_ADD_API = API_VERSION + "/me/play_queue/tracks.json?force_add=true";
//    public static final String PLAY_QUEUE_TRACKS_DELETE_ALL_API = API_VERSION + "/me/play_queue/delete_all.json";
//
//    public static final String PLAYQUEUE_TOKEN_API = API_VERSION + "/play_queue/token.json";
//    public static final String PLAYQUEUE_AUTHORIZE_API = API_VERSION + "/play_queue/authorize.json";
//    public static final String PLAY_QUEUE_DEVICES_API = API_VERSION + "/play_queue/devices.json";
//
//    public static final String PLAY_QUEUE_TRACKS_API = API_VERSION + "/me/play_queue/tracks.json";
//
//    public static final String PLAY_QUEUE_CURRENT_TRACK_API = API_VERSION + "/me/play_queue/current.json";
//
//    public static final String PLAY_QUEUE_ADD_NEXT_API = API_VERSION + "/me/play_queue/add_next.json";
//    public static final String PLAY_QUEUE_SET_NEXT_API = API_VERSION + "/me/play_queue/set_next.json";
//
//    public static final String PLAY_QUEUE_FORCE_ADD_NEXT_API = PLAY_QUEUE_ADD_NEXT_API + "?force_add=true";
//
//    // Tracks
//    public static final String TRACKS_LIST_API = API_VERSION + "/tracks.json";
//    public static final String TRACK_GET_API = API_VERSION + "/tracks/" + TRACK_ID_SUB + ".json";
//
//    // Artists
//    public static final String ARTISTS_LIST_API = API_VERSION + "/artists.json";
//    public static final String ARTIST_GET_API = API_VERSION + "/artists/" + ARTIST_ID_SUB + ".json";
//
//    // Devices
//    public static final String DEVICES_API = API_VERSION + "/me/devices.json";
//
//    // Facebook
//    // POST /v1/auth/sessions.json
//    public static final String FACEBOOK_LOGIN_API = API_VERSION + "/auth/sessions.json";
//
//    public static final String EMAIL_SIGN_UP_API = API_VERSION + "/users.json";
//
//    public static final String EMAIL_LOGIN_API = API_VERSION + "/auth/sessions.json";
//
//    public static final String POPULAR_PLAYLISTS_API = API_VERSION_THREE + "/lists/popular.json";
//
//    public static final String POPULAR_PLAYLISTS_WITH_TRACKS_API = POPULAR_PLAYLISTS_API + "?includes=tracks";
//
//    public static final String GENRES_API = API_VERSION + "/genres.json";
//
//    public static final String LANGUAGES_API = API_VERSION + "/languages.json";
//    public static final String TOP_LANGUAGES_API = API_VERSION + "/top_languages.json";
//
//    public static final String GENRE_PLAY_LISTS_API = API_VERSION + "/genres";
//
//    // Play queue
//    public static final String PLAY_QUEUE_API = API_VERSION + "/me/play_queue.json";
//    public static final String PLAY_QUEUE_PREVIOUS_API = API_VERSION + "/me/play_queue/previous.json";
//
//    // Play queue current
//    public static final String PLAY_QUEUE_CURRENT_API = API_VERSION + "/me/play_queue/current.json";
//
//    public static final String SUGGEST_TRACK_API = API_VERSION + "/tracks/request.json";
//    public static final String FORGOT_PASSWORD_API = API_VERSION + "/users/forgot_password.json";
//
//    public static final String REPORT_TRACK_ISSUE_API = API_VERSION + "/tracks/report.json";
//
//    public static final String SUBSCRIPTIONS_STATUS_API = API_VERSION + "/subscriptions/status.json";
//
//    /**
//     * GET /me/play_queue.json
//     * @see <a href="http://developers.popsical.com/#!/PlayQueue/get_me_play_queue_json">http://developers.popsical.com/#!/PlayQueue/get_me_play_queue_json</a>
//     *
//     * @param page    Page number
//     * @param perPage Tracks per page
//     * @return URL
//     */
//    public static String getPlayQueueApi(int page, int perPage) {
//        return PLAY_QUEUE_API + QUESTION_MARK + _PAGE + EQUALS + page + AND + _PER_PAGE + EQUALS + perPage;
//    }
//
//    public static String getPreviousPlayQueueApi(int page, int perPage) {
//        return PLAY_QUEUE_API + QUESTION_MARK + _PAGE + EQUALS + page + AND + _PER_PAGE + EQUALS + perPage;
//    }
//
//    public static String getGenrePlayListWithTracksApi(String genre, String languageCodes, int currentPage, int playListsPerPage) {
//        String query = _INCLUDES + EQUALS + _TRACKS
//                + AND + CharakuPathConstant._LANG + EQUALS + languageCodes
//                + AND + _PAGE + EQUALS + currentPage
//                + AND + _PER_PAGE + EQUALS + playListsPerPage;
//        return GENRE_PLAY_LISTS_API + SLASH + genre + JSON + QUESTION_MARK + query;
//    }
//
//    /**
//     * GET /v1/artist/{id}.json
//     * https://github.com/charaku/digimon/blob/master/doc/api/v1/api.md#get-artist-show
//     *
//     * @param artistId Artist ID
//     * @return Url
//     */
//    public static String getArtist(int artistId) {
//        String url = ARTIST_GET_API;
//        url = url.replace(ARTIST_ID_SUB, Integer.toString(artistId));
//        return url;
//    }
//
//    /**
//     * GET /v1/tracks/{id}.json
//     * https://github.com/charaku/digimon/blob/master/doc/api/v1/api.md#get-tracks-show
//     *
//     * @param trackId Track ID
//     * @return Url
//     */
//    public static String getTrack(int trackId) {
//        String url = TRACK_GET_API;
//        url = url.replace(TRACK_ID_SUB, Integer.toString(trackId));
//        return url;
//    }
//
//    /**
//     * @param playListId Play list ID
//     * @return Url
//     */
//    public static String getPlayList(int playListId) {
//        String playListTracksUrl = PLAYLIST_GET_API;
//        playListTracksUrl = playListTracksUrl.replace(PLAYLIST_ID_SUB, Integer.toString(playListId));
//        return playListTracksUrl;
//    }
//
//    /**
//     * Register device UID
//     *
//     * @param userId User ID
//     * @return Url GET /me/devices.json
//     */
//    public static String registerDeviceUid(int userId) {
//        String url = REGISTER_DEVICE_API;
//        url = url.replace(CharakuPathConstant.USER_ID_SUB, Integer.toString(userId));
//        return url;
//    }
//}
