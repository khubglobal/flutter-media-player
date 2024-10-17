package com.easternblu.khub.common;

/**
 * Constants for Popsical
 * Created by leechunhoe on 3/8/16.
 */
@SuppressWarnings({Common.UNUSED})
public interface PopsicalTvToRemoteConstant {


    /**
     * See {@link PopsicalConstant#POPSICAL_BROADCAST_PARAM_CODE_SIMPLIFIED}
     */
    @Deprecated
    String POPSICAL_TV_TO_REMOTE_BROADCAST_PARAM_CODE = "POPSICAL_TV_TO_REMOTE_BROADCAST_PARAM_CODE";


    /**
     * See {@link PopsicalConstant#POPSICAL_BROADCAST_PARAM_VALUE_SIMPLIFIED}
     */
    @Deprecated
    String POPSICAL_TV_TO_REMOTE_BROADCAST_PARAM_VALUE = "POPSICAL_TV_TO_REMOTE_BROADCAST_PARAM_VALUE";


    /**
     * See {@link PopsicalConstant#POPSICAL_BROADCAST_PARAM_VALUE_STRING_SIMPLIFIED}
     */
    @Deprecated
    String POPSICAL_TV_TO_REMOTE_BROADCAST_PARAM_VALUE_STRING = "POPSICAL_TV_TO_REMOTE_BROADCAST_PARAM_VALUE_STRING";

    //region int code constants that TV sends to remote (These values should be nouns)
    //================================================================================
    // CODES
    //================================================================================
    int POPSICAL_TV_UPDATE_PLAY_QUEUE = 0;
    @Deprecated
    int POPSICAL_TV_PLAYQUEUE_ID = PopsicalRemoteToTvConstant.POPSICAL_REMOTE_VALUE_PLAYQUEUE_ID; // use POPSICAL_TV_TRUE_PLAYQUEUE_ID instead
    int POPSICAL_TV_TRUE_PLAYQUEUE_ID = PopsicalRemoteToTvConstant.POPSICAL_REMOTE_VALUE_TRUE_PLAYQUEUE_ID;
    @Deprecated
    int POPSICAL_TV_PARTYCODE = PopsicalRemoteToTvConstant.POPSICAL_REMOTE_VALUE_PARTYCODE;
    int POPSICAL_TV_SPEC = 2;//PopsicalRemoteToTvConstant.POPSICAL_REMOTE_VALUE_TV_SPEC;


    int POPSICAL_TV_DSP_SETTINGS = 5;
    @Deprecated // use POPSICAL_TV_DSP_SETTINGS
    int POPSICAL_TV_DSP_SETTING = POPSICAL_TV_DSP_SETTINGS;
    int POPSICAL_TV_DSP_SHORTCUTS = 6;


    int POPSICAL_TV_LOGOUT = 100;
    int POPSICAL_TV_PAYMENT_INPUT = 101;
    int POPSICAL_TV_EDITTEXT = 102;
    int POPSICAL_TV_LIST = 103;
    int POPSICAL_TV_BUTTON = 104;
    int POPSICAL_TV_DIALOG = 105;
    
    int POPSICAL_TV_PLAYER_STATUS = 200;
    int POPSICAL_TV_PLAYER_PROGRESS = 201;


    //endregion

    //region int value constants that tv sends to remote (These values should be verbs)
    //================================================================================
    // CODES
    //================================================================================
    int POPSICAL_TV_VALUE_FOCUS = 7;
    int POPSICAL_TV_VALUE_UNFOCUS = 8;
    int POPSICAL_TV_VALUE_SHOW = 9;
    int POPSICAL_TV_VALUE_HIDE = 10;
    int POPSICAL_TV_VALUE_LOADING = 11;
    int POPSICAL_TV_VALUE_READY = 12;
    //endregion

    //region value object names
    //================================================================================
    // value object names
    //================================================================================
    String POPSICAL_TV_LABEL = "label";
    String POPSICAL_TV_MAX_LENGTH = "max_length";
    String POPSICAL_TV_COMPULSORY = "compulsory";
    String POPSICAL_TV_HINT = "hint";
    String POPSICAL_TV_ID = "id";
    String POPSICAL_TV_TEXT = "text";
    String POPSICAL_TV_SECURE_B64 = "secure_b64";
    String POPSICAL_TV_SECURE = "secure";
    String POPSICAL_TV_TYPE = "type";
    String POPSICAL_TV_POSITION = "position";

    String POPSICAL_TV_TITLE = "title";
    String POPSICAL_TV_ARTISTS_STRING= "artists_string";
    String POPSICAL_TV_PROGRESS = "progress";
    String POPSICAL_TV_IN_SKIPPABLE_RANGE = "in_skippable_range";
    String POPSICAL_TV_TOTAL_PROGRESS = "total_progress";
    String POPSICAL_TV_REPEAT_MODE = "repeat_mode";
    String POPSICAL_TV_VOCAL = "vocal";
    String POPSICAL_TV_MUSIC_VOLUME = "music_volume";
    String POPSICAL_TV_STATUS = "status";

    String POPSICAL_TV_START = "start";
    String POPSICAL_TV_STOP = "stop";
    String POPSICAL_TV_PAUSE = "pause";
    String POPSICAL_TV_RESUME = "resume";
    String POPSICAL_TV_BUFFERING = "buffering";
    String POPSICAL_TV_IDLE = "idle";


    //endregion

    /**
     * Possible values of POPSICAL_TV_TYPE, i.e. type of text fields
     */
    enum RemoteInputType {
        number,
        phone,
        text,
        name,
        email,
        credit_card_number,
        credit_card_date
    }


    /**
     * Possible values for POPSICAL_TV_ID
     */
    enum ValueObjectId {
        first_name,
        last_name,
        email,
        address_line1,
        address_city,
        address_state,
        address_zip,
        promo_code,
        phone,
        card_num,
        card_expiry,
        card_cvv,
        card_country,
        done,
        payment_confirm_dialog
    }
}