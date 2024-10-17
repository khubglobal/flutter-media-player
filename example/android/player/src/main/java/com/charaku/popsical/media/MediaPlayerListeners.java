//package com.easternblu.khub.media;
//
//import android.net.Uri;
//import androidx.annotation.Nullable;
//
//import com.easternblu.khub.media.model.MediaContent;
//import com.easternblu.khub.media.model.PlaybackErrorType;
//import com.google.android.exoplayer2.Player;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.LinkedHashSet;
//
//import timber.log.Timber;
//
///**
// * Created by pan on 8/3/18.
// * <p>
// * A {@link MediaPlayerListener} implementation that aggregate multiple {@link MediaPlayerListener}s
// */
//
//public class MediaPlayerListeners implements MediaPlayerListener {
//    public enum State {
//        started, stopped, resumed, paused, buffering, idle
//    }
//
//    @Nullable
//    private State currentState = State.idle;
//    public static final String TAG = MediaPlayerListeners.class.getSimpleName();
//    private Integer lastPlayerState = null;
//    private Boolean lastPlayWhenReady = null;
//    private String lastPlaybackId = null;
//    private Collection<MediaPlayerListener> listeners = Collections.synchronizedSet(new LinkedHashSet<MediaPlayerListener>());
//    private MediaPlayer player;
//
//    public MediaPlayerListeners(MediaPlayer player) {
//        this.player = player;
//    }
//
//    public synchronized void addListener(MediaPlayerListener listener) {
//        listeners.add(listener);
//    }
//
//    public synchronized void removeListener(MediaPlayerListener listener) {
//        if (listener != null) {
//            listeners.remove(listener);
//        }
//    }
//
//    public synchronized void removeAllListeners() {
//        listeners.clear();
//    }
//
//    @Nullable
//    public Integer getLastPlayerState() {
//        return lastPlayerState;
//    }
//
//    @Nullable
//    public Boolean getLastPlayWhenReady() {
//        return lastPlayWhenReady;
//    }
//
//
//    public void resetLastPlayWhenReady() {
//        this.lastPlayWhenReady = null;
//    }
//
//
//    public void setLastPlayWhenReady(boolean lastPlayWhenReady) {
//        this.lastPlayWhenReady = lastPlayWhenReady;
//    }
//
//    public void resetLastPlayerState() {
//        this.lastPlayerState = null;
//    }
//
//    public void setLastPlayerState(int lastPlayerState) {
//        this.lastPlayerState = lastPlayerState;
//    }
//
//    @Nullable
//    public State getCurrentState() {
//        return currentState;
//    }
//
//    @Override
//    public synchronized void onMediaStart(final boolean restart) {
//        currentState = State.started;
//        foreach(new Utils.Lambda<MediaPlayerListener, Void>() {
//            @Override
//            public Void invoke(MediaPlayerListener listener) {
//                listener.onMediaStart(restart);
//                return null;
//            }
//        });
//    }
//
//    @Override
//    public synchronized void onMediaResume() {
//        currentState = State.resumed;
//        foreach(new Utils.Lambda<MediaPlayerListener, Void>() {
//            @Override
//            public Void invoke(MediaPlayerListener listener) {
//                listener.onMediaResume();
//                return null;
//            }
//        });
//    }
//
//    @Override
//    public synchronized void onMediaPause() {
//        currentState = State.paused;
//        foreach(new Utils.Lambda<MediaPlayerListener, Void>() {
//            @Override
//            public Void invoke(MediaPlayerListener listener) {
//                listener.onMediaPause();
//                return null;
//            }
//        });
//    }
//
//    @Override
//    public synchronized void onMediaBuffering() {
//        currentState = State.buffering;
//        foreach(new Utils.Lambda<MediaPlayerListener, Void>() {
//            @Override
//            public Void invoke(MediaPlayerListener listener) {
//                listener.onMediaBuffering();
//                return null;
//            }
//        });
//    }
//
//    @Override
//    public synchronized void onMediaStop() {
//        currentState = State.stopped;
//        foreach(new Utils.Lambda<MediaPlayerListener, Void>() {
//            @Override
//            public Void invoke(MediaPlayerListener listener) {
//                listener.onMediaStop();
//                return null;
//            }
//        });
//    }
//
//
//    @Override
//    public synchronized void onMediaIdle() {
//        currentState = State.idle;
//        foreach(new Utils.Lambda<MediaPlayerListener, Void>() {
//            @Override
//            public Void invoke(MediaPlayerListener listener) {
//                listener.onMediaIdle();
//                return null;
//            }
//        });
//    }
//
//    @Override
//    public synchronized void onMediaContentEmpty() {
//
//
//        foreach(new Utils.Lambda<MediaPlayerListener, Void>() {
//            @Override
//            public Void invoke(MediaPlayerListener listener) {
//                listener.onMediaContentEmpty();
//                return null;
//            }
//        });
//
//    }
//
//    @Override
//    public synchronized void onPlayerCreated() {
//        currentState = State.idle;
//        foreach(new Utils.Lambda<MediaPlayerListener, Void>() {
//            @Override
//            public Void invoke(MediaPlayerListener listener) {
//                listener.onPlayerCreated();
//                return null;
//            }
//        });
//    }
//
//    @Override
//    public synchronized void onPlayerDestroyed() {
//        foreach(new Utils.Lambda<MediaPlayerListener, Void>() {
//            @Override
//            public Void invoke(MediaPlayerListener listener) {
//                listener.onPlayerDestroyed();
//                return null;
//            }
//        });
//
//    }
//
//    @Override
//    public synchronized void onMediaPlayerError(final Throwable error, final PlaybackErrorType event, final Object... args) {
//
//        foreach(new Utils.Lambda<MediaPlayerListener, Void>() {
//            @Override
//            public Void invoke(MediaPlayerListener listener) {
//                listener.onMediaPlayerError(error, event, args);
//                return null;
//            }
//        });
//
//    }
//
//    @Override
//    public synchronized void onPlayBeginning() {
//
//        foreach(new Utils.Lambda<MediaPlayerListener, Void>() {
//            @Override
//            public Void invoke(MediaPlayerListener listener) {
//                listener.onPlayBeginning();
//                return null;
//            }
//        });
//
//    }
//
//    @Override
//    public synchronized void onPlayEnding() {
//
//        foreach(new Utils.Lambda<MediaPlayerListener, Void>() {
//            @Override
//            public Void invoke(MediaPlayerListener listener) {
//                listener.onPlayEnding();
//                return null;
//            }
//        });
//    }
//
//    @Override
//    public synchronized void onPlayerProgressUpdate(final int currentPosition, final long duration) {
//
//        foreach(new Utils.Lambda<MediaPlayerListener, Void>() {
//            @Override
//            public Void invoke(MediaPlayerListener listener) {
//                listener.onPlayerProgressUpdate(currentPosition, duration);
//                return null;
//            }
//        });
//
//    }
//
//    @Override
//    public synchronized void onNetworkTraffiLog(final String msg) {
//
//
//        foreach(new Utils.Lambda<MediaPlayerListener, Void>() {
//            @Override
//            public Void invoke(MediaPlayerListener listener) {
//                listener.onNetworkTraffiLog(msg);
//                return null;
//            }
//        });
//    }
//
//    @Override
//    public synchronized void next() {
//
//        foreach(new Utils.Lambda<MediaPlayerListener, Void>() {
//            @Override
//            public Void invoke(MediaPlayerListener listener) {
//                listener.next();
//                return null;
//            }
//        });
//    }
//
//    @Override
//    public synchronized void previous() {
//
//        foreach(new Utils.Lambda<MediaPlayerListener, Void>() {
//            @Override
//            public Void invoke(MediaPlayerListener listener) {
//                listener.previous();
//                return null;
//            }
//        });
//    }
//
//    @Override
//    public synchronized void onMusicVolumeChanged(final float musicVolume, final boolean muted) {
//        foreach(new Utils.Lambda<MediaPlayerListener, Void>() {
//            @Override
//            public Void invoke(MediaPlayerListener listener) {
//                listener.onMusicVolumeChanged(musicVolume, muted);
//                return null;
//            }
//        });
//    }
//
//    @Override
//    public void onMediaTransferStart(final Uri uri) {
//        foreach(new Utils.Lambda<MediaPlayerListener, Void>() {
//            @Override
//            public Void invoke(MediaPlayerListener listener) {
//                listener.onMediaTransferStart(uri);
//                return null;
//            }
//        });
//    }
//
//    @Override
//    public void onMediaTransferring(final Uri uri, final long bytesTransferred, final long duration) {
//        foreach(new Utils.Lambda<MediaPlayerListener, Void>() {
//            @Override
//            public Void invoke(MediaPlayerListener listener) {
//                listener.onMediaTransferring(uri, bytesTransferred, duration);
//                return null;
//            }
//        });
//    }
//
//    @Override
//    public void onMediaTransferEnd(final Uri uri, final long bytesTransferred, final long duration) {
//        foreach(new Utils.Lambda<MediaPlayerListener, Void>() {
//            @Override
//            public Void invoke(MediaPlayerListener listener) {
//                listener.onMediaTransferEnd(uri, bytesTransferred, duration);
//                return null;
//            }
//        });
//    }
//
//
//    private synchronized void foreach(Utils.Lambda<MediaPlayerListener, Void> lambda) {
//        for (MediaPlayerListener listener : new ArrayList<>(listeners)) {
//            lambda.invoke(listener);
//        }
//    }
//
//    /**
//     * @param playWhenReady
//     * @param playbackState
//     */
//    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
//        Integer lastPlayerState = getLastPlayerState();
//
//        Timber.d("onPlayerStateChanged: begin playWhenReady = " + playWhenReady + " playbackState = " + playbackState + " previous = " + lastPlayerState);
//        boolean duplicateState = (lastPlayerState != null && lastPlayerState == playbackState);
//        // must be called immediate before calling methods in PlayerEventListener
//        // as the PlayerEventListener methods can also trigger additional state change
//        setLastPlayWhenReady(playWhenReady);
//        setLastPlayerState(playbackState);
//
//        switch (playbackState) {
//            case Player.STATE_ENDED: // 4f
//                if (duplicateState) {
//                    Timber.d("duplicate state(STATE_ENDED)");
//                } else {
//                    Timber.d("onPlayerStateChanged: onMediaStop");
//                    onMediaStop();
//                }
//                break;
//            case Player.STATE_IDLE: // 1
//                Timber.d("onPlayerStateChanged: onMediaIdle");
//                onMediaIdle();
//                break;
//            case Player.STATE_BUFFERING: //2
//                Timber.d("onPlayerStateChanged: onMediaBuffering");
//                onMediaBuffering();
//                break;
//            case Player.STATE_READY: //3
//                if (playWhenReady) {
//                    if (lastPlayerState != Player.STATE_READY) {
//                        Timber.d("onPlayerStateChanged: onMediaStart");
//
//                        MediaContent mc;
//                        String playbackId = null;
//                        if ((mc = player.getCurrentMediaContent()) != null) {
//                            playbackId = mc.getPlaybackId();
//                        }
//                        boolean restart = (playbackId != null && playbackId.equals(lastPlaybackId));
//                        if (!restart) {
//                            lastPlaybackId = playbackId;
//                        }
//                        onMediaStart(restart);
//                    } else {
//                        Timber.d("onPlayerStateChanged: onMediaResume");
//                        onMediaResume();
//                    }
//                } else {
//                    Timber.d("onPlayerStateChanged: onMediaPause");
//                    onMediaPause();
//                }
//                break;
//        }
//        Timber.d("onPlayerStateChanged: end");
//    }
//
//    /**
//     * Call this if you are sure the restart boolean from onMediaStart to be false on next onMediaStart
//     */
//    public void resetRestartCheck() {
//        lastPlaybackId = null;
//    }
//}
