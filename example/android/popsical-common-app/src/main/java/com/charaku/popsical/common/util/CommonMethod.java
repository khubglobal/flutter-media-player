package com.easternblu.khub.common.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import androidx.annotation.ColorInt;
import androidx.annotation.DimenRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.WorkerThread;
import androidx.appcompat.widget.SearchView;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.TextView;

import com.easternblu.khub.common.Common;
import com.easternblu.khub.common.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import timber.log.Timber;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Commonly used general methods
 * Created by leechunhoe on 15/8/16.
 */
public class CommonMethod {
    public static final String TAG = CommonMethod.class.getSimpleName();


    /**
     * Get local IP (V4) address
     *
     * @return Local IP (V4) address
     */
    public static String getLocalIpV4Address() {
        List<String> ipAddresses = getLocalIpAddresses(true);
        return Lists.isNotEmpty(ipAddresses) ? ipAddresses.get(0) : null;
    }

    /**
     * Get local IP (V4) address
     *
     * @return Local IP (V4) address
     */
    @Deprecated
    public static String getLocalIpAddress() {
        return getLocalIpV4Address();
    }

    /**
     * get a list of
     *
     * @param ipv4 true, then it will only if IP address is ipV4
     * @return
     */
    public static List<String> getLocalIpAddresses(boolean ipv4) {
        List<String> ipAddresses = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            if (en == null) {
                return null;
            }
            while (en.hasMoreElements()) {
                NetworkInterface networkInterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddress = networkInterface.getInetAddresses(); enumIpAddress.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddress.nextElement();

                    if (!inetAddress.isLoopbackAddress() && isValidIpAddress(inetAddress)) {
                        if (ipv4) {
                            if (inetAddress instanceof Inet4Address) {
                                ipAddresses.add(inetAddress.getHostAddress());
                            } else {
                                // if we want v4 ip only and the inetAddress is not V4 then we skip
                            }
                        } else {
                            ipAddresses.add(inetAddress.getHostAddress());
                        }
                    }
                }
            }
        } catch (SocketException | NullPointerException ex) {
            ex.printStackTrace();
        }
        return ipAddresses;
    }


    public static boolean isValidIpAddress(InetAddress inetAddress) {
        String temp = inetAddress.getHostAddress();
        return temp != null && !temp.contains("null");
    }


    /**
     * Get wifi mac address
     *
     * @return
     */
    public static String getWifiMacAddress() {
        return getMacAddress("wlan0");
    }

    /**
     * Simulator will not return a mac address
     * You need to have wifi turned (no need to be connected) for it to return a mac address
     * Use {@link #getWifiMacAddress()}
     *
     * @return
     */
    @Deprecated
    public static String getMacAddress() {
        return getWifiMacAddress();
    }

    /**
     * @param name can be wlan0 or eth0 but it can also be any string specified by Android API
     *             names are normally known by names such as "le0".
     * @return
     */
    public static String getMacAddress(String name) {
        try {
            List<NetworkInterface> all = java.util.Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase(name))
                    continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    String temp = "00" + Integer.toHexString(b & 0xFF) + ":";
                    res1.append(temp.substring(temp.length() - 3, temp.length()));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            //handle exception
        }
        return "";
    }


    /**
     * Get version name
     *
     * @param context Context
     * @return version name
     */
    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return Common.EMPTY_STRING;
        }
    }

    /**
     * Get version name
     *
     * @param context Context
     * @return version name
     */
    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Get local resource URI from resource ID e.g. R.drawable.ic_something
     *
     * @param context    Context
     * @param resourceId Resource ID e.g. R.drawable.ic_something
     * @return Local resource URI
     */
    public static Uri getLocalResourceUri(Context context, int resourceId) {
        return Uri.parse(Common.PROTOCOL_ANDROID_RESOURCE + context.getApplicationContext().getPackageName()
                + Common.SLASH + context.getResources().getResourceTypeName(resourceId)
                + Common.SLASH + context.getResources().getResourceEntryName(resourceId));
    }


    /**
     * Check if service with given class is running
     *
     * @param applicationContext
     * @param serviceClass       the class of the service (e.g. MyService.class)
     * @return
     */
    public static boolean isServiceRunning(Context applicationContext, Class<? extends Service> serviceClass) {
        return isServiceRunning(applicationContext, serviceClass.getName());
    }

    /**
     * Check if service with given class name is running
     *
     * @param applicationContext Application context
     * @param serviceClassName   Service class name
     * @return True if is running
     */
    public static boolean isServiceRunning(Context applicationContext, String serviceClassName) {
        final ActivityManager activityManager = (ActivityManager) applicationContext.getSystemService(ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
            if (runningServiceInfo.service.getClassName().equals(serviceClassName)) {
                //  Timber.i(serviceClassName + " is running");
                return true;
            }
        }
        // Timber.i(serviceClassName + " is NOT running");
        return false;
    }

    /**
     * Get image drawable from Url
     *
     * @param url HTTP URL of image
     * @return Drawable instance of this image
     */
    public static Drawable getImageDrawableFromUrl(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            return Drawable.createFromStream(is, Common.EMPTY_STRING);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get Android unique device ID
     * Build.SERIAL is not-unique (i.e. VIM device will return 0123456789abcdef
     * Settings.Secure.ANDROID_ID is say to "use at your own risk"
     *
     * @param context Context
     * @return Android unique device ID
     */
    public static String getDeviceUid(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    /**
     * Get device height
     *
     * @param activity Activity
     * @return device height in pixel
     */
    public static float getDeviceHeight(Activity activity) {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    /**
     * Get device height
     *
     * @param activity Activity
     * @return device height in pixel
     */
    public static float getStatusBarHeight(Activity activity) {
        int result = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * Convert value from dp to pixel
     *
     * @param context   Context
     * @param valueInDp Value in dp unit
     * @return Value in px unit
     */
    public static int getPxFromDp(Context context, float valueInDp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (valueInDp * density);
    }

    /**
     * Convert value from pixel to dp
     *
     * @param context   Context
     * @param valueInPx Value in px unit
     * @return Value in dp unit
     */
    public static int getDpFromPx(Context context, float valueInPx) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (valueInPx * density);
    }

    /**
     * Hide viewToHide and show viewToShow with elegant animation
     *
     * @param viewToHide        First view, from VISIBLE to GONE
     * @param viewToShow        Second view, from GONE to VISIBLE
     * @param animationDuration Animation duration
     */
    public static void crossFade(final View viewToHide, View viewToShow, int animationDuration) {
        viewToShow.setAlpha(0f);
        viewToShow.setVisibility(View.VISIBLE);
        viewToShow.animate()
                .alpha(1f)
                .setDuration(animationDuration)
                .setListener(null);
        viewToHide.animate()
                .alpha(0f)
                .setDuration(animationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        viewToHide.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * Cross-fade
     *
     * @param viewsToHide       Array of views to hide
     * @param viewsToShow       Array of views to show
     * @param animationDuration Animation duration
     */
    public static void crossFade(View[] viewsToHide, View[] viewsToShow, int animationDuration) {
        for (View viewToShow : viewsToShow) {
            viewToShow.setAlpha(0f);
            viewToShow.setVisibility(View.VISIBLE);
            viewToShow.animate()
                    .alpha(1f)
                    .setDuration(animationDuration)
                    .setListener(null);
        }

        for (final View viewToHide : viewsToHide) {
            viewToHide.animate()
                    .alpha(0f)
                    .setDuration(animationDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            viewToHide.setVisibility(View.GONE);
                        }
                    });
        }
    }

    /**
     * Check if attribute is present in JSON object
     *
     * @param jsonObject     JSON object
     * @param attributeName  Attribute name (key)
     * @param attributeClass Class of this attribute e.g. string, int, etc.
     * @return True if this attribute present
     */
    public static boolean hasJsonAttribute(JSONObject jsonObject, String attributeName, Class<?> attributeClass) {
        if (jsonObject.has(attributeName)) {
            try {
                return jsonObject.get(attributeName).getClass().equals(attributeClass);
            } catch (JSONException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Get JSON attribute from a JSON object
     *
     * @param jsonObject     JSON object
     * @param attributeName  Attribute name (key)
     * @param attributeClass Class of this attribute e.g. string, int, etc.
     * @return Object in specified class if this attribute present, null otherwise
     */
    @SuppressWarnings("unchecked")
    public static <K> K getJsonAttribute(JSONObject jsonObject, String attributeName, Class<K> attributeClass) {
        if (hasJsonAttribute(jsonObject, attributeName, attributeClass)) {
            try {
                return (K) jsonObject.get(attributeName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Get integer JSON attribute from a JSON object
     *
     * @param jsonObject    JSON object
     * @param attributeName Attribute name
     * @param defaultValue  Default value in case this integer not found
     * @return Integer JSON attribute, defaultValue if not found
     */
    public static int getIntegerJsonAttribute(JSONObject jsonObject, String attributeName, int defaultValue) {
        if (hasJsonAttribute(jsonObject, attributeName, Integer.class)) {
            try {

                if (jsonObject.get(attributeName) instanceof Integer) {
                    Object object = CommonMethod.getJsonAttribute(jsonObject, attributeName, Integer.class);
                    if (object != null) {
                        return (Integer) object;
                    }
                } else if (jsonObject.get(attributeName) instanceof String) {
                    String string = (String) CommonMethod.getJsonAttribute(jsonObject, attributeName, String.class);
                    if (!TextUtils.isEmpty(string)) {
                        return Integer.parseInt(string);
                    }
                }
            } catch (NullPointerException | NumberFormatException | JSONException e) {
                e.printStackTrace();
            }
        }
        return defaultValue;
    }

    /**
     * Check if activity is running
     *
     * @param context       Context
     * @param activityClass Activity class
     * @return True if activity is running
     */
    public static boolean isActivityRunning(Context context, Class activityClass) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            List<ActivityManager.AppTask> tasks = activityManager.getAppTasks();
            for (ActivityManager.AppTask task : tasks) {
                if (task.getTaskInfo().origActivity != null) {
                    ComponentName origActivity = task.getTaskInfo().origActivity;
                    if (activityClass.getCanonicalName().equalsIgnoreCase(origActivity.getClassName())) {
                        return true;
                    }
                }
            }
            return false;
        } else {
            List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);
            for (ActivityManager.RunningTaskInfo task : tasks) {
                if (task.baseActivity != null) {
                    ComponentName baseActivity = task.baseActivity;
                    if (activityClass.getCanonicalName().equalsIgnoreCase(baseActivity.getClassName())) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    /**
     * Returns a themed color integer associated with a particular resource ID
     *
     * @param context    Context
     * @param colorResId Color resource ID
     * @return A single color value in the form 0xAARRGGBB.
     */
    @ColorInt
    public static int getColor(Context context, int colorResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getResources().getColor(colorResId, null);
        } else {
            return context.getResources().getColor(colorResId);
        }
    }

    /**
     * Get HTML decoded string from url
     *
     * @param url Url/HTML encoded string
     * @return Decoded string
     */
    public static String fromHtml(String url) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(url, Html.FROM_HTML_MODE_COMPACT).toString();
        } else {
            return Html.fromHtml(url).toString();
        }
    }

    /**
     * Get HTML encoded string from input string
     *
     * @param text Text to be encoded
     * @return HTML encoded string
     */
    public static String toHtml(Spanned text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.toHtml(text, Html.FROM_HTML_MODE_COMPACT);
        } else {
            return Html.toHtml(text);
        }
    }

    /**
     * Encode string into URL, e.g. from & to %26
     *
     * @param string Raw string
     * @return Url encoded string
     */
    public static String encodeUrl(String string) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                return URLEncoder.encode(string, StandardCharsets.UTF_8.name());
            } catch (UnsupportedEncodingException e) {
                return string;
            } catch (IllegalCharsetNameException e) {
                return string;
            } catch (NoClassDefFoundError e) {
                return string;
            }
        } else {
            return URLEncoder.encode(string);
        }
    }

    /**
     * Encode string into URL, e.g. from & to %26
     *
     * @param string Raw string
     * @return Url encoded string
     */
    public static String decodeUrl(String string) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                return URLDecoder.decode(string, StandardCharsets.UTF_8.name());
            } catch (UnsupportedEncodingException | IllegalCharsetNameException | NoClassDefFoundError e) {
                return URLDecoder.decode(string);
            }
        } else {
            return URLDecoder.decode(string);
        }
    }

    /**
     * Get drawable given resource ID
     *
     * @param drawableResId Resource ID of drawable
     * @return Drawable
     */
    public static Drawable getDrawable(Context context, int drawableResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getResources().getDrawable(drawableResId, null);
        } else {
            return context.getResources().getDrawable(drawableResId);
        }
    }

    /**
     * Set edit text color of search view
     *
     * @param searchView Search view (app compat)
     * @param color      Color code (not Resource ID)
     */
    public static void setSearchViewTextColor(SearchView searchView, int color) {
        SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        if (searchAutoComplete != null) {
            searchAutoComplete.setTextColor(color);
        }
    }

    /**
     * Get children by class
     *
     * @param parent        Parent view instance
     * @param childrenClass Class of children view
     * @return Children object if found, null if none
     */
    public static Object[] getChildrenByClass(ViewGroup parent, Class<?> childrenClass) {
        ArrayList<Object> views = new ArrayList<>();

        for (int i = 0; i < parent.getChildCount(); i++) {
            if (parent.getChildAt(i).getClass().equals(childrenClass)) {
                views.add(parent.getChildAt(i));
            }
        }

        return views.toArray();
    }

    @Nullable
    public static WifiInfo getWifiInfo(Context context) {
        WifiManager manager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (manager != null && manager.isWifiEnabled()) {
            WifiInfo wifiInfo = manager.getConnectionInfo();
            if (wifiInfo != null) {
                NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
                if (state == NetworkInfo.DetailedState.CONNECTED || state == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
                    return wifiInfo;
                }
            }
        }
        return null;
    }

    @Nullable
    public static String getWifiName(Context context) {
        WifiInfo wifiInfo = getWifiInfo(context);
        return wifiInfo != null ? Strings.stripEnclosingQuotes(wifiInfo.getSSID()) : null;
    }


    /**
     * Check if string is equal, including case when they are null
     * When both are null, they are considered equal
     *
     * @param string1 First string
     * @param string2 Second string
     * @return True if both string are the same
     */
    public static boolean isStringEqual(String string1, String string2) {
        return (string1 == null && string2 == null) || !(string1 == null || string2 == null) && string1.equals(string2);
    }

    public static void animateBackgroundColorTransitionWithResId(final View view, int colorFromResId, int colorToResId, int duration) {
        int colorFrom = getColor(view.getContext(), colorFromResId);
        int colorTo = getColor(view.getContext(), colorToResId);
        animateBackgroundColorTransition(view, colorFrom, colorTo, duration);
    }

    public static void animateBackgroundColorTransition(final View view, int colorFrom, int colorTo, int duration) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(duration); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view.setBackgroundColor((int) animator.getAnimatedValue());
            }
        });
        colorAnimation.start();
    }

    public static void animateBackgroundColorTransition(final View view, int colorToResId, int duration) {
        Drawable background = view.getBackground();
        if (background instanceof ColorDrawable) {
            int colorFrom = ((ColorDrawable) background).getColor();
            int colorTo = getColor(view.getContext(), colorToResId);
            animateBackgroundColorTransition(view, colorFrom, colorTo, duration);
        } else {
            view.setBackgroundColor(CommonMethod.getColor(view.getContext(), colorToResId));
        }
    }

    /**
     * Check current activity on foreground
     *
     * @param context Context
     * @return The class name of current activity
     */
    public static String getCurrentActivity(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        // Chun Hoe: This does not work
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        //            List<ActivityManager.AppTask> tasks = activityManager.getAppTasks();
        //            if (tasks != null && tasks.size() > 0) {
        //                return tasks.get(0).getClass().getName();
        //            } else {
        //                return Common.EMPTY_STRING;
        //            }
        //        }

        try {
            List<ActivityManager.RunningTaskInfo> taskInfo = activityManager.getRunningTasks(1);
            if (taskInfo != null && taskInfo.size() > 0) {
                return taskInfo.get(0).topActivity.getClassName();
            } else {
                return Common.EMPTY_STRING;
            }
        } catch (SecurityException e) {
            return Common.EMPTY_STRING;
        }
    }

    /**
     * Check if this is current activity
     *
     * @param activity Activity instance
     * @return True if this activity is current activity
     */
    public static boolean isCurrentActivity(@Nullable Activity activity) {
        if (activity == null) {
            return false;
        } else {
            String currentActivity = getCurrentActivity(activity.getApplicationContext());
            return TextUtils.equals(activity.getClass().getName(), currentActivity);
        }
    }

    /**
     * Check if app is in foreground
     *
     * @param context Context
     * @return True if this app is in foreground
     */
    public static boolean isAppInForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        String topActivityName = taskInfo.get(0).topActivity.getPackageName();
        return TextUtils.equals(topActivityName, context.getPackageName());
    }

    /**
     * Hide keyboard
     *
     * @param activity {@link Activity}
     */
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Hide keyboard
     *
     * @param activity {@link Activity}
     */
    public static void hideKeyboard(Activity activity, View currentFocus) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (currentFocus != null) {
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }

    /**
     * Show keyboard
     *
     * @param activity {@link Activity}
     */
    public static void showKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
    }

    /**
     * Set locale
     *
     * @param context Context
     * @param locale  Locale
     */
    public static void setLocale(Context context, Locale locale) {
        Configuration config = new Configuration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }
        context.getResources().updateConfiguration(config, null);
    }

    /**
     * Open browser with specified URL
     *
     * @param context Context
     * @param url     URL
     */
    public static void openBrowser(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    public static void goWifiSettings(Activity activity) {
        try {
            Intent wifiSettingsIntent = new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK);
            activity.startActivity(wifiSettingsIntent);
        } catch (ActivityNotFoundException e) {
            Timber.e(e);
        }
    }

    public static void goGeneralSettings(Context context) {
        try {
            Intent settingsIntent = new Intent(Settings.ACTION_SETTINGS);
            context.startActivity(settingsIntent);
        } catch (ActivityNotFoundException e) {
            Timber.e(e);
        }
    }

    public static boolean isFileExists(String filePath) {
        File folder1 = new File(filePath);
        return folder1.exists();
    }

    public static boolean deleteFile(String filePath) {
        File folder1 = new File(filePath);
        return folder1.delete();
    }

    /**
     * Doen't work for Android 7 and beyond due to exposed uri
     *
     * @param context
     * @param apkFilePath
     */
    @Deprecated
    public static void installNewApk(Context context, String apkFilePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(apkFilePath));
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Get current locale
     *
     * @param context Context
     * @return Current locale
     */
    public static Locale getCurrentLocale(Context context) {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }
        return locale;
    }

    /**
     * Check if valid email
     *
     * @param string Candidate email
     * @return True if email valid
     */
    public static boolean isValidEmail(String string) {
        return !TextUtils.isEmpty(string) && android.util.Patterns.EMAIL_ADDRESS.matcher(string).matches();
    }

    /**
     * Check if valid phone number
     *
     * @param string Candidate phone number
     * @return True if phone number valid
     */
    public static boolean isValidPhone(String string) {
        return !TextUtils.isEmpty(string) && Patterns.PHONE.matcher(string).matches();
    }

    public static void linkifyTextView(final Activity activity, TextView textView, int textResId, HashMap<String, String> labelAndUrlPairs) {
        String text = activity.getString(textResId);
        if (TextUtils.isEmpty(text)) {
            return;
        }

        if (labelAndUrlPairs == null) {
            return;
        }

        SpannableString spannable = new SpannableString(text);

        for (Map.Entry<String, String> labelAndUrlPair : labelAndUrlPairs.entrySet()) {


            String label = labelAndUrlPair.getKey();
            final String url = labelAndUrlPair.getValue();

            if (TextUtils.isEmpty(label)) {
                continue;
            }

            int indexBegin = text.indexOf(label);
            int indexEnd = text.indexOf(label) + label.length();

            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    // TODO: Go to URL
                    openBrowser(activity, url);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(CommonMethod.getColor(activity, R.color.colorAccent));
                    ds.setUnderlineText(true);
                }
            };

            spannable.setSpan(clickableSpan, indexBegin, indexEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(spannable, TextView.BufferType.SPANNABLE);
    }

    /**
     * Check if is on main thread
     *
     * @return True if is on main thread
     */
    public static boolean isOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static float convertPixelsToDp(float px) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return Math.round(dp);
    }

    public static float convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    public static void setTextAppearance(Context context, TextView textView, int textAppearanceResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextAppearance(textAppearanceResId);
        } else {
            textView.setTextAppearance(context, textAppearanceResId);
        }
    }

    public static long getNow() {
        return Calendar.getInstance().getTimeInMillis();
    }

    /**
     * Get bitmap from URL
     *
     * @param urlString URL string
     * @return Bitmap
     */
    @WorkerThread
    public static Bitmap getBitmapFromUrl(String urlString) {
        if (URLUtil.isValidUrl(urlString)) {
            try {
                URL url = new URL(urlString);
                return BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    public static int ceilDiv(int numerator, int denominator) {
        return (int) Math.ceil((double) numerator / (double) denominator);
    }

    /**
     * Print stack trace with message
     *
     * @param tag     Tag
     * @param message Message
     */
    public static void printStackTrace(String tag, String message) {
        try {
            throw new Throwable(message);
        } catch (Throwable t) {
            Log.i(tag, t.getMessage(), t);
        }
    }

    /**
     * Get string height with pixel
     *
     * @param activity Activity
     * @return Screen height in pixel
     */
    public static int getScreenHeight(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    /**
     * Get screen width in pixel
     *
     * @param activity Activity
     * @return Screen width in pixel
     */
    public static int getScreenWidth(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    /**
     * Get screen bigger side in pixel
     *
     * @param activity {@link Activity} instance
     * @return Bigger side among height/width of screen in pixel
     */
    public static int getScreenBiggerSide(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels > metrics.heightPixels ? metrics.widthPixels : metrics.heightPixels;
    }

    /**
     * Get view bigger side
     *
     * @param view {@link View} instance
     * @return Bigger side from view height/width
     */
    public static int getViewBiggerSide(View view) {
        return view.getHeight() > view.getWidth() ? view.getHeight() : view.getWidth();
    }

    /**
     * Get dimension in pixel
     *
     * @param context    Context
     * @param dimenResId Dimension res ID
     * @return Size in pixel
     */
    public static int getDimensionPixelSize(Context context, @DimenRes int dimenResId) {
        if (context == null) {
            return 0;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getResources().getDimensionPixelSize(dimenResId);
        } else {
            return context.getResources().getDimensionPixelSize(dimenResId);
        }
    }

    /**
     * Concatenate string from resource IDs
     *
     * @param context      {@link Context} instance
     * @param stringResIds String resource IDs
     * @return Concatenated string from resource IDs
     */
    public static String getString(Context context, @StringRes int... stringResIds) {
        if (context == null) {
            return Common.EMPTY_STRING;
        }

        if (stringResIds.length == 1) {
            return context.getString(stringResIds[0]);
        } else {
            String string = Common.EMPTY_STRING;
            for (@StringRes int stringResId : stringResIds) {
                string += context.getString(stringResId);
            }
            return string;
        }
    }

    /**
     * Get string
     *
     * @param context     Context
     * @param stringResId String resource ID
     * @return String
     */
    public static String getString(Context context, @StringRes int stringResId) {
        if (context == null) {
            return Common.EMPTY_STRING;
        } else {
            return context.getString(stringResId);
        }
    }

    /**
     * Get string from resource ID with arguments
     *
     * @param context     Context
     * @param stringResId String resource ID
     * @param formatArgs  Arguments to be substituted in string resource
     * @return String
     */
    public static String getString(Context context, @StringRes int stringResId, Object... formatArgs) {
        if (context == null) {
            return Common.EMPTY_STRING;
        } else {
            return context.getString(stringResId, formatArgs);
        }
    }

    /**
     * Convert millis to day time format
     *
     * @param millis Time in milliseconds
     * @return Time in hour:minute:seconds format
     */
    public static String millisToDayTimeFormat(long millis) {
        long seconds = millis / 1000;
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        long h = (seconds / (60 * 60)) % 24;
        return String.format(Locale.ENGLISH, "%d:%02d:%02d", h, m, s);
    }

    /**
     * Check if device is tablet
     *
     * @param context Context
     * @return
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static String getUsername(Context context) {
        AccountManager manager = AccountManager.get(context);
        Account[] accounts = manager.getAccountsByType("com.google");
        List<String> possibleEmails = new LinkedList<>();

        for (Account account : accounts) {
            // TODO: Check possible email against an email regex or treat
            // account.name as an email address only for certain account.type values.
            possibleEmails.add(account.name);
        }

        if (!possibleEmails.isEmpty() && possibleEmails.get(0) != null) {
            String email = possibleEmails.get(0);
            String[] parts = email.split("@");

            if (parts.length > 1)
                return parts[0];
        }
        return null;
    }

    /**
     * Copy to clipboard
     *
     * @param context Context
     * @param text    Text
     */
    public static void copyToClipboard(Context context, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // TODO: Decouple this
        ClipData clip = ClipData.newPlainText("lyric_line", text);
        clipboard.setPrimaryClip(clip);
    }

    public static String capFirstLetter(String rawString) {
        StringBuilder sb = new StringBuilder(rawString);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }

    public static void showShareTextDialog(Context context, String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

    public static String getDateTime() {
        return SimpleDateFormat.getDateTimeInstance().format(new Date());
    }
}