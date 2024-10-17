package com.easternblu.khub.media.model;

import androidx.annotation.Nullable;

import java.util.UUID;


/**
 * ExoPlayer has additional setup options. All should be specified here
 */
public class SetupExtraParam {

    private boolean preferExtensionDecoders = false;
    private String drmSchemeUuid = null;
    private String drmLicenseUrl = null;
    private String[] drmKeyRequestProperties = null;

    @Nullable
    public UUID getDrmSchemeUuid() {
        return drmSchemeUuid != null ? UUID.fromString(drmSchemeUuid) : null;
    }


    public boolean isPreferExtensionDecoders() {
        return preferExtensionDecoders;
    }

    public void setPreferExtensionDecoders(boolean preferExtensionDecoders) {
        this.preferExtensionDecoders = preferExtensionDecoders;
    }

    public void setDrmSchemeUuid(String drmSchemeUuid) {
        this.drmSchemeUuid = drmSchemeUuid;
    }

    public String getDrmLicenseUrl() {
        return drmLicenseUrl;
    }

    public void setDrmLicenseUrl(String drmLicenseUrl) {
        this.drmLicenseUrl = drmLicenseUrl;
    }

    public String[] getDrmKeyRequestProperties() {
        return drmKeyRequestProperties;
    }

    public void setDrmKeyRequestProperties(String[] drmKeyRequestProperties) {
        this.drmKeyRequestProperties = drmKeyRequestProperties;
    }
}
