package com.capstone.laperinapp.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.SparseArray
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.MediaItem
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.MergingMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.capstone.laperinapp.databinding.ActivityVideoBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class VideoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoBinding

    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val urlVideo = intent.getStringExtra(EXTRA_DATA)

        initPlayer(urlVideo.toString())
        hideSystemUi()
    }

    private fun initPlayer(urlVideo: String) {
        val videoItem = MediaItem.fromUri(urlVideo)
        val audioItem = MediaItem.fromUri(urlVideo)

        player = if (player == null) {
            ExoPlayer.Builder(this).build().also { exoPlayer ->
                exoPlayer.setMediaItem(videoItem)
                exoPlayer.addMediaItem(audioItem)
                exoPlayer.prepare()
            }
        } else {
            player!!.also {
                it.setMediaItem(videoItem)
                it.addMediaItem(audioItem)
                it.prepare()
            }
        }

        binding.playerView.player = player
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    private fun releasePlayer() {
        player?.release()
        player = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        player?.let {
            outState.putLong("playerPosition", it.currentPosition)
            outState.putBoolean("isPlaying", it.isPlaying)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        player?.let {
            it.seekTo(savedInstanceState.getLong("playerPosition"))
            if (savedInstanceState.getBoolean("isPlaying")) {
                it.play()
            }
        }
    }

    private fun hideSystemUi() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.playerView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}