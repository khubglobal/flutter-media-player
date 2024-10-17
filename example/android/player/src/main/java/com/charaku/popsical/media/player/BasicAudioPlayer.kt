package com.easternblu.khub.media.player

import android.media.MediaPlayer
import java.io.File
import java.io.FileInputStream

open class BasicAudioPlayer() {
    var current: MediaPlayer? = null
    var started = false
    var pausePosition = 0

    init {
    }


    @Synchronized
    fun getMediaPlayer(): MediaPlayer {
        return current?.let {
            it
        } ?: run {
            MediaPlayer().also { current = it }
        }
    }

    fun resetState() {
        started = false
        pausePosition = 0
    }

    fun openFile(file: File, startWhenReady: Boolean = true) {
        FileInputStream(file).use { fis ->
            getMediaPlayer().run {
                resetState()
                setOnCompletionListener {
                    it.release()
                    if (it == current) {
                        current = null
                    }
                }
                setDataSource(fis.fd)
                prepare()
                if (startWhenReady) {
                    this@BasicAudioPlayer.play()
                }
            }
        }
    }

    fun getCurrentPosition(): Int {
        return getMediaPlayer().run {
            currentPosition
        }
    }

    fun seekTo(positionMs: Int) {
        getMediaPlayer().run {
            seekTo(positionMs)
            pausePosition = positionMs
        }
    }


    open fun stop() {
        getMediaPlayer().run {
            if (isPlaying) {
                stop()
            }
        }
    }


    open fun play() {
        getMediaPlayer().run {
            if (!isPlaying) {
                seekTo(pausePosition)
                start()
                started = true
            }
        }
    }


    open fun pause() {
        getMediaPlayer().run {
            if (isPlaying) {
                pause()
                pausePosition = this.currentPosition
            }
        }
    }


    open fun isPlaying(): Boolean {
        return getMediaPlayer().run {
            isPlaying
        }
    }


}