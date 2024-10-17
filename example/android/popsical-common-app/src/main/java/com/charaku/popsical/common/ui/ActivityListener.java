package com.easternblu.khub.common.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

/**
 * A interface similar to {@link android.app.Application.ActivityLifecycleCallbacks}
 * <p>
 * But it should be added by an {@link ActivityListenerSupport} interface
 */
public interface ActivityListener {
    public void onCreate(Bundle savedInstanceState);

    public void onDestroy();

    public void onStart();

    public void onStop();

    public void onResume();

    public void onPause();

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

    public void onActivityResult(int requestCode, int resultCode, Intent data);
}
