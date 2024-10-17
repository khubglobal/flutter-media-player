package com.easternblu.khub.common.util;

import com.easternblu.khub.common.Common;

import static com.easternblu.khub.common.PopsicalConstant.POPSICAL_REMOTE_ADD_PLAY_QUEUE_TRACK;
import static com.easternblu.khub.common.PopsicalConstant.POPSICAL_REMOTE_CONTROL_PAD;
import static com.easternblu.khub.common.PopsicalConstant.POPSICAL_REMOTE_DEVICE_NAME;
import static com.easternblu.khub.common.PopsicalConstant.POPSICAL_REMOTE_MUSIC_VOLUME;
import static com.easternblu.khub.common.PopsicalConstant.POPSICAL_REMOTE_PITCH;
import static com.easternblu.khub.common.PopsicalConstant.POPSICAL_REMOTE_PLAY_NOW;
import static com.easternblu.khub.common.PopsicalConstant.POPSICAL_REMOTE_PLAY_PAUSE_VIDEO;
import static com.easternblu.khub.common.PopsicalConstant.POPSICAL_REMOTE_REMOVE_PLAY_QUEUE_TRACK;
import static com.easternblu.khub.common.PopsicalConstant.POPSICAL_REMOTE_REPLAY_VIDEO;
import static com.easternblu.khub.common.PopsicalConstant.POPSICAL_REMOTE_SKIP_VIDEO;
import static com.easternblu.khub.common.PopsicalConstant.POPSICAL_REMOTE_SPEED;
import static com.easternblu.khub.common.PopsicalConstant.POPSICAL_REMOTE_SUBTITLE;
import static com.easternblu.khub.common.PopsicalConstant.POPSICAL_REMOTE_UPDATE_NEXT_TRACK;
import static com.easternblu.khub.common.PopsicalConstant.POPSICAL_REMOTE_UPDATE_PLAY_QUEUE;
import static com.easternblu.khub.common.PopsicalConstant.POPSICAL_REMOTE_VALUE_BACK;
import static com.easternblu.khub.common.PopsicalConstant.POPSICAL_REMOTE_VALUE_DOWN;
import static com.easternblu.khub.common.PopsicalConstant.POPSICAL_REMOTE_VALUE_LEFT;
import static com.easternblu.khub.common.PopsicalConstant.POPSICAL_REMOTE_VALUE_OK;
import static com.easternblu.khub.common.PopsicalConstant.POPSICAL_REMOTE_VALUE_RESET;
import static com.easternblu.khub.common.PopsicalConstant.POPSICAL_REMOTE_VALUE_RIGHT;
import static com.easternblu.khub.common.PopsicalConstant.POPSICAL_REMOTE_VALUE_UP;
import static com.easternblu.khub.common.PopsicalConstant.POPSICAL_REMOTE_VOCAL;
import static com.easternblu.khub.common.PopsicalConstant.POPSICAL_REMOTE_VOCAL_VOLUME;
import static com.easternblu.khub.common.PopsicalConstant.TITLE_POPSICAL_REMOTE_ADD_PLAY_QUEUE_TRACK;
import static com.easternblu.khub.common.PopsicalConstant.TITLE_POPSICAL_REMOTE_CONTROL_PAD;
import static com.easternblu.khub.common.PopsicalConstant.TITLE_POPSICAL_REMOTE_DEVICE_NAME;
import static com.easternblu.khub.common.PopsicalConstant.TITLE_POPSICAL_REMOTE_MUSIC_VOLUME;
import static com.easternblu.khub.common.PopsicalConstant.TITLE_POPSICAL_REMOTE_PITCH;
import static com.easternblu.khub.common.PopsicalConstant.TITLE_POPSICAL_REMOTE_PLAY_NOW;
import static com.easternblu.khub.common.PopsicalConstant.TITLE_POPSICAL_REMOTE_PLAY_PAUSE_VIDEO;
import static com.easternblu.khub.common.PopsicalConstant.TITLE_POPSICAL_REMOTE_REMOVE_PLAY_QUEUE_TRACK;
import static com.easternblu.khub.common.PopsicalConstant.TITLE_POPSICAL_REMOTE_REPLAY_VIDEO;
import static com.easternblu.khub.common.PopsicalConstant.TITLE_POPSICAL_REMOTE_SKIP_VIDEO;
import static com.easternblu.khub.common.PopsicalConstant.TITLE_POPSICAL_REMOTE_SPEED;
import static com.easternblu.khub.common.PopsicalConstant.TITLE_POPSICAL_REMOTE_SUBTITLE;
import static com.easternblu.khub.common.PopsicalConstant.TITLE_POPSICAL_REMOTE_UPDATE_NEXT_TRACK;
import static com.easternblu.khub.common.PopsicalConstant.TITLE_POPSICAL_REMOTE_UPDATE_PLAY_QUEUE;
import static com.easternblu.khub.common.PopsicalConstant.TITLE_POPSICAL_REMOTE_VALUE_BACK;
import static com.easternblu.khub.common.PopsicalConstant.TITLE_POPSICAL_REMOTE_VALUE_DOWN;
import static com.easternblu.khub.common.PopsicalConstant.TITLE_POPSICAL_REMOTE_VALUE_LEFT;
import static com.easternblu.khub.common.PopsicalConstant.TITLE_POPSICAL_REMOTE_VALUE_OK;
import static com.easternblu.khub.common.PopsicalConstant.TITLE_POPSICAL_REMOTE_VALUE_RESET;
import static com.easternblu.khub.common.PopsicalConstant.TITLE_POPSICAL_REMOTE_VALUE_RIGHT;
import static com.easternblu.khub.common.PopsicalConstant.TITLE_POPSICAL_REMOTE_VALUE_UP;
import static com.easternblu.khub.common.PopsicalConstant.TITLE_POPSICAL_REMOTE_VOCAL;
import static com.easternblu.khub.common.PopsicalConstant.TITLE_POPSICAL_REMOTE_VOCAL_VOLUME;

/**
 * Utility for
 * <p>
 * Created by leechunhoe on 21/3/17.
 */
public class PopsicalSocketUtil {
    /**
     * Get title for remote to TV command keys, for Firebase reporting purpose
     * https://charaku.myjetbrains.com/youtrack/issue/POPSICAL-541
     *
     * @param remoteToTvCommandKey {@link Integer} Remote to TV command key
     * @return {@link String} Title of remoteToTvCommandKey
     */
    public static String getRemoteToTvCommandKeyValuePairTitle(int remoteToTvCommandKey, int remoteToTvCommandValue) {
        String key = getRemoteToTvCodeTitle(remoteToTvCommandKey);
        String value = getRemoteToTvValueTitle(remoteToTvCommandValue);
        return key + Common.COMMA + value;
    }

    public static String getRemoteToTvCodeTitle(int remoteToTvCommandCode) {
        String key;
        switch (remoteToTvCommandCode) {
            case POPSICAL_REMOTE_MUSIC_VOLUME:
                key = TITLE_POPSICAL_REMOTE_MUSIC_VOLUME;
                break;
            case POPSICAL_REMOTE_VOCAL_VOLUME:
                key = TITLE_POPSICAL_REMOTE_VOCAL_VOLUME;
                break;
            case POPSICAL_REMOTE_PITCH:
                key = TITLE_POPSICAL_REMOTE_PITCH;
                break;
            case POPSICAL_REMOTE_SPEED:
                key = TITLE_POPSICAL_REMOTE_SPEED;
                break;
            case POPSICAL_REMOTE_SUBTITLE:
                key = TITLE_POPSICAL_REMOTE_SUBTITLE;
                break;
            case POPSICAL_REMOTE_VOCAL:
                key = TITLE_POPSICAL_REMOTE_VOCAL;
                break;
            case POPSICAL_REMOTE_SKIP_VIDEO:
                key = TITLE_POPSICAL_REMOTE_SKIP_VIDEO;
                break;
            case POPSICAL_REMOTE_PLAY_PAUSE_VIDEO:
                key = TITLE_POPSICAL_REMOTE_PLAY_PAUSE_VIDEO;
                break;
            case POPSICAL_REMOTE_CONTROL_PAD:
                key = TITLE_POPSICAL_REMOTE_CONTROL_PAD;
                break;
            case POPSICAL_REMOTE_DEVICE_NAME:
                key = TITLE_POPSICAL_REMOTE_DEVICE_NAME;
                break;
            case POPSICAL_REMOTE_UPDATE_PLAY_QUEUE:
                key = TITLE_POPSICAL_REMOTE_UPDATE_PLAY_QUEUE;
                break;
            case POPSICAL_REMOTE_REMOVE_PLAY_QUEUE_TRACK:
                key = TITLE_POPSICAL_REMOTE_REMOVE_PLAY_QUEUE_TRACK;
                break;
            case POPSICAL_REMOTE_ADD_PLAY_QUEUE_TRACK:
                key = TITLE_POPSICAL_REMOTE_ADD_PLAY_QUEUE_TRACK;
                break;
            case POPSICAL_REMOTE_PLAY_NOW:
                key = TITLE_POPSICAL_REMOTE_PLAY_NOW;
                break;
            case POPSICAL_REMOTE_UPDATE_NEXT_TRACK:
                key = TITLE_POPSICAL_REMOTE_UPDATE_NEXT_TRACK;
                break;
            case POPSICAL_REMOTE_REPLAY_VIDEO:
                key = TITLE_POPSICAL_REMOTE_REPLAY_VIDEO;
                break;
            default:
                return Common.EMPTY_STRING;
        }

        return key;
    }

    public static String getRemoteToTvValueTitle(int remoteToTvCommandValue) {
        String value;

        switch (remoteToTvCommandValue) {
            case POPSICAL_REMOTE_VALUE_DOWN:
                value = TITLE_POPSICAL_REMOTE_VALUE_DOWN;
                break;
            case POPSICAL_REMOTE_VALUE_UP:
                value = TITLE_POPSICAL_REMOTE_VALUE_UP;
                break;
            case POPSICAL_REMOTE_VALUE_LEFT:
                value = TITLE_POPSICAL_REMOTE_VALUE_LEFT;
                break;
            case POPSICAL_REMOTE_VALUE_RIGHT:
                value = TITLE_POPSICAL_REMOTE_VALUE_RIGHT;
                break;
            case POPSICAL_REMOTE_VALUE_OK:
                value = TITLE_POPSICAL_REMOTE_VALUE_OK;
                break;
            case POPSICAL_REMOTE_VALUE_BACK:
                value = TITLE_POPSICAL_REMOTE_VALUE_BACK;
                break;
            case POPSICAL_REMOTE_VALUE_RESET:
                value = TITLE_POPSICAL_REMOTE_VALUE_RESET;
                break;
            default:
                return Common.EMPTY_STRING;
        }

        return value;
    }
}