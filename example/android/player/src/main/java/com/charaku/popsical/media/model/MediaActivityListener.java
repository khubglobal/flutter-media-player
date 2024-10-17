package com.easternblu.khub.media.model;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

public interface MediaActivityListener {
    public void onCreate(Bundle savedInstanceState);

    public void onDestroy();

    public void onStart();

    public void onStop();

    public void onResume();

    public void onPause();

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

    public void onActivityResult(int requestCode, int resultCode, Intent data);
}
