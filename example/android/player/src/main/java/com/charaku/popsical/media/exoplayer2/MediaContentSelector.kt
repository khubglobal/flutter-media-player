//package com.easternblu.khub.media.exoplayer2
//
//import android.content.Context
//import com.easternblu.khub.media.exoplayer2.MediaContentSelector.*
//import com.google.android.exoplayer2.C
//import com.google.android.exoplayer2.Format
//import com.google.android.exoplayer2.RendererCapabilities
//import com.google.android.exoplayer2.source.TrackGroup
//import com.google.android.exoplayer2.source.TrackGroupArray
//import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
//import com.google.android.exoplayer2.trackselection.MappingTrackSelector
//import com.google.android.exoplayer2.ui.DefaultTrackNameProvider
//import com.google.android.exoplayer2.ui.TrackNameProvider
//
///**
// * This is wrapper to help us use [MappingTrackSelector.MappedTrackInfo]. [MappingTrackSelector.MappedTrackInfo] is
// * hard to use because it is  all backed by arrays inside arrays and everything is access via indexes.
// *
// * This class is attempting to OOP things.
// *
// * In [MappingTrackSelector.MappedTrackInfo], you have:
// * You can have 1+ video renderers (see [getRenderers]
// * Each video renderer has a [TrackGroupArray] (see [Renderer.groups])
// * Each [TrackGroupArray] can have multiple [TrackGroup] (see [Renderer.groups])
// * Each [TrackGroup] can have multiple [Format] (see [Group.tracks])
// *
// *
// * The above translates to:
// * rendererType --has many--> [Renderer] --has many--> [Group] --has many--> [Track]
// *
// * Multiple [Track]s for audio/video can either belong to different [Group] or the same [Group]
// * This is basically [TrackSelectionView] without the [View]
// */
//class MediaContentSelector(val ctx: Context, val trackSelector: DefaultTrackSelector) {
//    enum class TrackType {
//        audio, video, text
//    }
//
//    val trackNameProvider: TrackNameProvider
//    val trackInfo: MappingTrackSelector.MappedTrackInfo?
//        get() {
//            return trackSelector.currentMappedTrackInfo
//        }
//
//    init {
//        trackNameProvider = DefaultTrackNameProvider(ctx.getResources())
//    }
//
//    val isTrackInfoReady: Boolean
//        get() {
//            return trackInfo?.let {
//                it.rendererCount > 0
//            } ?: false
//        }
//
//    /**
//     * Typical [rendererType] would be C.TRACK_TYPE_AUDIO
//     */
//    private fun getRendererIndexes(rendererType: Int): List<Int> {
//        return mutableListOf<Int>().apply {
//            trackInfo?.let { trackInfo ->
//                list(endIndex = trackInfo.rendererCount) { rendererIndex ->
//                    if (trackInfo.getRendererType(rendererIndex) == rendererType) {
//                        add(rendererIndex)
//                    }
//                }
//            }
//        }
//    }
//
//    private fun isDisabled(rendererIndex: Int): Boolean {
//        return trackSelector.parameters.getRendererDisabled(rendererIndex)
//    }
//
//
//    /**
//     * First [Renderer] (of [C.TRACK_TYPE_VIDEO]) for this media content. Extracted using [DefaultTrackSelector]
//     *
//     */
//    fun getFirstVideoRenderer(): Renderer? {
//        return getRenderers(C.TRACK_TYPE_VIDEO)?.firstOrNull()
//    }
//
//
//    /**
//     * First [Renderer] (of [C.TRACK_TYPE_AUDIO]) for this media content. Extracted using [DefaultTrackSelector]
//     *
//     */
//    fun getFirstAudioRenderer(): Renderer? {
//        return getRenderers(C.TRACK_TYPE_AUDIO)?.firstOrNull()
//    }
//
//
//    /**
//     * Get the [Track] object(s) matching [rendererType] and created by [creator]
//     *
//     * Typical [rendererType] would be C.TRACK_TYPE_AUDIO
//     * Typical [creator] would be [audioTrackCreator]
//     */
//    private fun getTracks(rendererType: Int): List<Track>? {
//        return mutableListOf<Track>().apply {
//            getRenderers(rendererType)?.forEach {
//                addAll(it.tracks)
//            }
//        }
//    }
//
//    /**
//     * More for internal use, in most cases you are only interested in the actual [Track] for a certain type
//     * and don't really care about the group or renderer
//     *
//     * Typical [rendererType] would be C.TRACK_TYPE_AUDIO
//     * Typical [creator] would be [audioTrackCreator]
//     */
//    private fun getRenderers(rendererType: Int): List<Renderer>? {
//        return trackInfo?.let { trackInfo ->
//            list(endIndex = trackInfo.rendererCount) {
//                if (trackInfo.getRendererType(it) == rendererType) Renderer(it) else null
//            }
//        }
//    }
//
//
//    /**
//     * Encapsulate the [TrackGroupArray] from [MappingTrackSelector.MappedTrackInfo] as a
//     * "Renderer"
//     */
//    inner class Renderer(val rendererIndex: Int) {
//
//        val rendererType: Int?
//            get() {
//                return trackInfo?.getRendererType(rendererIndex)
//            }
//
//        val trackType: TrackType?
//            get() {
//                return rendererType?.let { toTrackType(it) }
//            }
//
//        internal val trackGroupArray: TrackGroupArray?
//            get() {
//                return trackInfo?.getTrackGroups(rendererIndex)
//            }
//
//        val uniqueId: String
//            get() {
//                return "rendererIndex=${rendererIndex}"
//            }
//
//        val groups: List<Group>?
//            get() {
//                return trackGroupArray?.let { array ->
//                    list(endIndex = array.length) {
//                        Group(this, it)
//                    }
//                }
//            }
//
//        val tracks: List<Track>
//            get() {
//                return mutableListOf<Track>().apply {
//                    groups?.forEach {
//                        it.tracks?.forEach {
//                            add(it)
//                        }
//                    }
//                }
//            }
//
//        val isDisabled: Boolean
//            get() {
//                return trackSelector.parameters.getRendererDisabled(rendererIndex)
//            }
//
//
//        val isAuto: Boolean
//            get() {
//                return !isDisabled && getSelectionOverride() == null
//            }
//
//        /**
//         * Group all tracks by [Group]
//         */
//        fun groupByGroup(): Map<Group, List<Track>>? {
//            return tracks.groupBy {
//                it.group
//            }
//        }
//
//
//        @JvmOverloads
//        internal fun getSelectionOverride(onError: (Throwable) -> Unit = {}): DefaultTrackSelector.SelectionOverride? {
//            if (isDisabled) {
//                onError(MediaContentManagerException("First renderer of type $rendererType is disabled"))
//                return null
//            }
//            val trackGroupArray = trackGroupArray
//            trackGroupArray ?: run {
//                onError(MediaContentManagerException("Track group array not available"))
//                return null
//            }
//
//            val index = rendererIndex
//
//            val selectionOverride = trackSelector.parameters.getSelectionOverride(index, trackGroupArray)
//            return selectionOverride
//        }
//
//
//        @JvmOverloads
//        fun getSelectedTracksOverride(onError: (Throwable) -> Unit = {}): SelectedTracksOverride? {
//            val selectionOverride = getSelectionOverride(onError)
//            selectionOverride ?: run { return null }
//
//            val tracks = tracks.filter {
//                selectionOverride.groupIndex == it.group.groupArrayIndex && selectionOverride.containsTrack(it.trackIndex)
//            }
//
//            return SelectedTracksOverride(selectionOverride, tracks)
//        }
//
//        fun auto() {
//            apply(null, false)
//        }
//
//        fun disable() {
//            apply(null, true)
//        }
//
//
//        internal fun apply(selectionOverride: DefaultTrackSelector.SelectionOverride?, disable: Boolean) {
//            val rendererIndex = rendererIndex
//            val builder = trackSelector.buildUponParameters().apply {
//                setRendererDisabled(rendererIndex, disable)
//                selectionOverride?.let {
//                    setSelectionOverride(rendererIndex, trackGroupArray, it)
//                } ?: run {
//                    clearSelectionOverrides(rendererIndex)
//                }
//            }
//            trackSelector.setParameters(builder)
//        }
//
//
//    }
//
//
//    /**
//     * Encapsulate the [TrackGroup] object
//     */
//    inner class Group(val renderer: Renderer, val groupArrayIndex: Int) {
//
//        internal val trackGroup: TrackGroup?
//            get() {
//                return renderer.trackGroupArray?.get(groupArrayIndex)
//            }
//
//        val trackType: TrackType?
//            get() {
//                return renderer.trackType
//            }
//
//
//        val tracks: List<Track>?
//            get() {
//                return trackGroup?.let { group ->
//                    list(endIndex = group.length) {
//                        Track(this, it)
//                    }
//                }
//            }
//
//        val uniqueId: String
//            get() {
//                return "${renderer.uniqueId}&groupArrayIndex=${groupArrayIndex}"
//            }
//
//
//        val isEnableAdaptiveSelections: Boolean
//            get() {
//                val hasSiblingTracks = (tracks?.size ?: 0) > 1
//                val hasSupport = (trackInfo?.getAdaptiveSupport(renderer.rendererIndex, groupArrayIndex, false)
//                        ?: RendererCapabilities.ADAPTIVE_NOT_SUPPORTED) != RendererCapabilities.ADAPTIVE_NOT_SUPPORTED
//                return hasSiblingTracks && hasSupport
//            }
//
//
//        fun isSelected(track: Track): Boolean {
//            val selectionOverride = renderer.getSelectionOverride()
//            return selectionOverride != null && track.group.groupArrayIndex == selectionOverride.groupIndex && selectionOverride.containsTrack(track.trackIndex)
//        }
//
//        /**
//         * Set the [track] as [DefaultTrackSelector.SelectionOverride]. If [onlyThis] and [select] are both true
//         * then it will clear existing [DefaultTrackSelector.SelectionOverride.tracks] first
//         */
//        @JvmOverloads
//        fun setSelect(track: Track, select: Boolean, onlyThis: Boolean = false) {
//            val selectedTracksOverride = renderer.getSelectedTracksOverride()
//
//            var disable = false
//            var selectionOverride: DefaultTrackSelector.SelectionOverride? = null
//            if (selectedTracksOverride == null) {
//                selectionOverride = DefaultTrackSelector.SelectionOverride(groupArrayIndex, track.trackIndex)
//            } else {
//                var tracks = selectedTracksOverride.selectionOverride.tracks.toMutableList()
//                if (select) {
//                    if(onlyThis){
//                        tracks.clear()
//                    }
//                    // add track from override
//                    tracks.add(track.trackIndex)
//                } else {
//                    // remove track from override
//                    tracks.remove(track.trackIndex)
//                }
//
//
//                if (tracks.isEmpty()) {
//                    selectionOverride = null
//                    disable = true
//                } else {
//                    selectionOverride = DefaultTrackSelector.SelectionOverride(groupArrayIndex, *tracks.toIntArray())
//                }
//
//            }
//            renderer.apply(selectionOverride, disable)
//        }
//
//    }
//
//
//    /**
//     * Each [Renderer] can have an [SelectedTracksOverride] that contains multiple [Track]
//     *
//     * For example a video renderer can choose to only use the 720p and 1080p track (ignoring lower ones like 480p)
//     */
//    inner class SelectedTracksOverride(val selectionOverride: DefaultTrackSelector.SelectionOverride,
//            // tracks that are used in the override
//                                       val tracks: List<Track>)
//
//
//    /**
//     * Model a [Format] object of the [Group]
//     */
//    inner class Track(val group: Group, val trackIndex: Int) {
//
//        val format: Format?
//            get() {
//                return group.trackGroup?.getFormat(trackIndex)
//            }
//
//        val trackType: TrackType?
//            get() {
//                return group.trackType
//            }
//
//        val isSelected: Boolean
//            get() {
//                return group.isSelected(this)
//            }
//
//        val uniqueId: String
//            get() {
//                return "${group.uniqueId}&trackIndex=${trackIndex}"
//            }
//
//        @JvmOverloads
//        fun setSelected(select: Boolean, onlyThis: Boolean = false) {
//            group.setSelect(this, select, onlyThis)
//        }
//
//        val name: String?
//            get() {
//                return trackNameProvider.getTrackName(format)
//            }
//
//
//    }
//
//
//    private fun <K> list(startIndex: Int = 0, endIndex: Int, getElement: (Int) -> K?): List<K> {
//        return mutableListOf<K>().apply {
//            for (i in startIndex until endIndex) {
//                getElement(i)?.let {
//                    add(it)
//                }
//            }
//        }
//    }
//
//}
//
//internal fun toRendererType(trackType: TrackType): Int {
//    return when (trackType) {
//        TrackType.audio -> C.TRACK_TYPE_AUDIO
//        TrackType.video -> C.TRACK_TYPE_VIDEO
//        TrackType.text -> C.TRACK_TYPE_TEXT
//    }
//}
//
//internal fun toTrackType(rendererType: Int): TrackType? {
//    return when (rendererType) {
//        C.TRACK_TYPE_AUDIO -> TrackType.audio
//        C.TRACK_TYPE_VIDEO -> TrackType.video
//        C.TRACK_TYPE_TEXT -> TrackType.text
//        else -> null
//    }
//}
//
//class MediaContentManagerException(msg: String) : Exception(msg)
//
//
