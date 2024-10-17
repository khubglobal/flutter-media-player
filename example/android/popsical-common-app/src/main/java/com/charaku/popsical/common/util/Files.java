package com.easternblu.khub.common.util;

import android.content.Context;
import android.os.Environment;
import androidx.core.content.ContextCompat;
import androidx.core.os.EnvironmentCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import timber.log.Timber;

/**
 * Created by pan on 12/7/17.
 */

public class Files {
    private static final String TAG = Files.class.getSimpleName();
    private static final Pattern DIR_SEPORATOR = Pattern.compile("/");

    public static File getDownloadFolder() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    }

    public static File getDownloadFile(String name) {
        return new File(getDownloadFolder(), name);
    }


    public static class StorageInfo {

        public final String path;
        public final boolean readonly;
        public final boolean removable;
        public final int number;

        StorageInfo(String path, boolean readonly, boolean removable, int number) {
            this.path = path;
            this.readonly = readonly;
            this.removable = removable;
            this.number = number;
        }

        public String getDisplayName() {
            StringBuilder res = new StringBuilder();
            if (!removable) {
                res.append("Internal SD card");
            } else if (number > 1) {
                res.append("SD card " + number);
            } else {
                res.append("SD card");
            }
            if (readonly) {
                res.append(" (Read only)");
            }
            return res.toString();
        }
    }

    public static List<StorageInfo> getStorageList() {

        List<StorageInfo> list = new ArrayList<StorageInfo>();
        String def_path = Environment.getExternalStorageDirectory().getPath();
        boolean def_path_removable = Environment.isExternalStorageRemovable();
        String def_path_state = Environment.getExternalStorageState();
        boolean def_path_available = def_path_state.equals(Environment.MEDIA_MOUNTED)
                || def_path_state.equals(Environment.MEDIA_MOUNTED_READ_ONLY);
        boolean def_path_readonly = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY);

        HashSet<String> paths = new HashSet<String>();
        int cur_removable_number = 1;

        if (def_path_available) {
            paths.add(def_path);
            list.add(0, new StorageInfo(def_path, def_path_readonly, def_path_removable, def_path_removable ? cur_removable_number++ : -1));
        }

        BufferedReader buf_reader = null;
        try {
            buf_reader = new BufferedReader(new FileReader("/proc/mounts"));
            String line;
            Log.i(TAG,"/proc/mounts");
            while ((line = buf_reader.readLine()) != null) {
                Timber.i(line);
                if (line.contains("vfat") || line.contains("/mnt")) {
                    StringTokenizer tokens = new StringTokenizer(line, " ");
                    String unused = tokens.nextToken(); //device
                    String mount_point = tokens.nextToken(); //mount point
                    if (paths.contains(mount_point)) {
                        continue;
                    }
                    unused = tokens.nextToken(); //file system
                    List<String> flags = Arrays.asList(tokens.nextToken().split(",")); //flags
                    boolean readonly = flags.contains("ro");

                    if (line.contains("/dev/block/vold")) {
                        if (!line.contains("/mnt/secure")
                                && !line.contains("/mnt/asec")
                                && !line.contains("/mnt/obb")
                                && !line.contains("/dev/mapper")
                                && !line.contains("tmpfs")) {
                            paths.add(mount_point);
                            list.add(new StorageInfo(mount_point, readonly, true, cur_removable_number++));
                        }
                    }
                }
            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (buf_reader != null) {
                try {
                    buf_reader.close();
                } catch (IOException ex) {
                }
            }
        }
        return list;
    }


    /**
     * Returns absolute paths to application-specific directories on all
     * external storage devices where the application can place persistent files
     * it owns. These files are internal to the application, and not typically
     * visible to the user as media.
     * <p>
     * This is like {@link Context#getFilesDir()} in that these files will be
     * deleted when the application is uninstalled, however there are some
     * important differences:
     * <ul>
     * <li>External files are not always available: they will disappear if the
     * user mounts the external storage on a computer or removes it.
     * <li>There is no security enforced with these files.
     * </ul>
     * <p>
     * External storage devices returned here are considered a permanent part of
     * the device, including both emulated external storage and physical media
     * slots, such as SD cards in a battery compartment. The returned paths do
     * not include transient devices, such as USB flash drives.
     * <p>
     * An application may store data on any or all of the returned devices. For
     * example, an app may choose to store large files on the device with the
     * most available space, as measured by {@link android.os.StatFs}.
     * <p>
     * Starting in {@link android.os.Build.VERSION_CODES#KITKAT}, no permissions
     * are required to write to the returned paths; they're always accessible to
     * the calling app. Before then,
     * {@link android.Manifest.permission#WRITE_EXTERNAL_STORAGE} is required to
     * write. Write access outside of these paths on secondary external storage
     * devices is not available. To request external storage access in a
     * backwards compatible way, consider using {@code android:maxSdkVersion}
     * like this:
     * <p>
     * <pre class="prettyprint">&lt;uses-permission
     * android:name="android.permission.WRITE_EXTERNAL_STORAGE"
     * android:maxSdkVersion="18" /&gt;</pre>
     * <p>
     * The first path returned is the same as
     * {@link Context#getExternalFilesDir(String)}. Returned paths may be
     * {@code null} if a storage device is unavailable.
     *
     * @see Context#getExternalFilesDir(String)
     * @see EnvironmentCompat#getStorageState(File)
     */
    public static File[] getExternalFilesDirs(Context ctx, boolean rootFolderOnly) {
        //  07-12 15:45:26.146 D/InfoActivity: [D]/storage/emulated/0/Android/data/com.easternblu.khub.tv/files
        //  07-12 15:45:26.147 D/InfoActivity: [D]/storage/1EF5-15F8/Android/data/com.easternblu.khub.tv/files
        File[] fileDirs = ContextCompat.getExternalFilesDirs(ctx, null);
        if (!rootFolderOnly || fileDirs == null)
            return fileDirs;

        String appSuffix = getAppPackageFormat(ctx);
        for (int i = 0; i < fileDirs.length; i++) {
            File file = fileDirs[i];
            String filePath = file.getPath();
            if (filePath.endsWith(appSuffix)) {
                fileDirs[i] = new File(filePath.substring(0, filePath.length() - appSuffix.length()));
            }
        }
        return fileDirs;
    }


    /**
     * Return "Android/data/<package_name>/files"
     *
     * @param ctx
     * @return
     */
    private static String getAppPackageFormat(Context ctx) {
        return Strings.format("/Android/data/%1$s/files", ctx.getPackageName());
    }


    /**
     * Delete everything in a directory and its subdirectories
     * @param file
     * @return
     */
    public static boolean deleteDirectory(File file) {
        if (file.exists()) {
            boolean success = true;
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        success = success && deleteDirectory(files[i]);
                    } else {
                        success = success && files[i].delete();
                    }
                }
            }
            success = success && file.delete();
            return success;
        } else {
            return false;
        }
    }


    /**
     * Returns a readable indication of the size provided
     * @param sizeInByte
     * Size in bytes
     * @return
     */
    public static String toSizeString(long sizeInByte) {
        if (sizeInByte < 1000) {
            return Strings.format("%dB", sizeInByte);

        } else if (sizeInByte < 1000 * 1000) {
            return Strings.format("%.2fkB", sizeInByte / 1000d);

        } else if (sizeInByte < 1000 * 1000 * 1000){
            return Strings.format("%.2fMB", sizeInByte / (1000d * 1000));

        } else {
            return Strings.format("%.2fGB", sizeInByte / (1000d * 1000 * 1000));
        }
    }

}
