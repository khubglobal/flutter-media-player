package com.khub.plugin_player.lyrics.utils;


import com.easternblu.khub.common.util.Strings;

public class AppPrefs {
    public final String TAG = this.getClass().getSimpleName();
    public static final String PREFS_DEBUG_LYRICS_RENDERER = "PREFS_DEBUG_LYRICS_RENDERER";
    public volatile static boolean SHARE_PREF_LISTENER_ENABLED = true;
    public static class PrefChange {
        private String name;

        public PrefChange(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public boolean isSame(String pref) {
            return Strings.isNotNullAndEquals(pref, name);
        }
    }}