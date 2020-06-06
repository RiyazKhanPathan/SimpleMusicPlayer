package com.example.musicplayer


import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.media.MediaPlayer
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private var musicDuration: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mediaPlayer = MediaPlayer.create(this, R.raw.sound)
        mediaPlayer.setVolume(0.5f, 0.5f)
        mediaPlayer.isLooping = true
        musicDuration = mediaPlayer.duration

        // Volume Bar
        volumeBar.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekbar: SeekBar?, progressBar: Int, fromUser: Boolean) {
                        if (fromUser) {
                            var volumeLevel = progressBar / 100.0f
                            mediaPlayer.setVolume(volumeLevel, volumeLevel)
                        }
                    }
                    override fun onStartTrackingTouch(p0: SeekBar?) {
                    }
                    override fun onStopTrackingTouch(p0: SeekBar?) {
                    }
                }
        )

        // Position Bar
        positionBar.max = musicDuration
        positionBar.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar?, progressBar: Int, fromUser: Boolean) {
                        if (fromUser) {
                            mediaPlayer.seekTo(progressBar)
                        }
                    }
                    override fun onStartTrackingTouch(p0: SeekBar?) {
                    }
                    override fun onStopTrackingTouch(p0: SeekBar?) {
                    }
                }
        )

        // Thread
        Thread(Runnable {
            while (mediaPlayer != null) {
                try {
                    var message = Message()
                    message.what = mediaPlayer.currentPosition
                    handler.sendMessage(message)
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                }
            }
        }).start()
    }

    fun playBtnClick(v: View) {

        if (mediaPlayer.isPlaying) {
            // Stop
            mediaPlayer.pause()
            playBtn.setBackgroundResource(R.drawable.play)

        } else {
            // Start
            mediaPlayer.start()
            playBtn.setBackgroundResource(R.drawable.pause)
        }
    }

    @SuppressLint("HandlerLeak")
    var handler = object : Handler() {
        override fun handleMessage(message: Message) {
            var currentPositionInPositionBar = message.what

            var elapsedTime= createTimeLabel(currentPositionInPositionBar)
            elapsedTimeLabel.text = elapsedTime

            positionBar.progress = currentPositionInPositionBar


            var timeRemaining = createTimeLabel(musicDuration - currentPositionInPositionBar)
            remainingTimeLabel.text = "-$timeRemaining"
        }
    }

    fun createTimeLabel(time: Int): String {
        var timeLabel = ""
        var min = time / 1000 / 60
        var sec = time / 1000 % 60

        timeLabel = "$min:"
        if (sec < 10) timeLabel += "0"
        timeLabel += sec

        return timeLabel
    }




}