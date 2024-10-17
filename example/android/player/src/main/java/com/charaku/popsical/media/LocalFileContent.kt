package com.easternblu.khub.media

import android.net.Uri
import com.easternblu.khub.media.model.MediaContent
import java.io.File
import java.util.*

class LocalFileContent(val file: File, val customLang: String = "en", val customTitle: String = file.name, val customPlaybackId: String = UUID.randomUUID().toString()) : MediaContent {
    override fun getTag(): String {
        return "local_content=true"
    }

    override fun getTitle(): String {
        return customTitle
    }

    override fun getUri(): Uri {
        return Uri.fromFile(file)
    }

    override fun getId(): String {
        return file.absolutePath
    }

    override fun getVideoId(): String {
        return id
    }

    override fun getLangCode(): String {
        return customLang
    }

    override fun getPlaybackId(): String {
        return customPlaybackId
    }

}