package com.siddharth.volumecontrol

import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.SeekBar
import android.widget.TextView

class MainActivity : Activity() {

    private lateinit var audioManager: AudioManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        audioManager =
            getSystemService(Context.AUDIO_SERVICE) as AudioManager

        createUI()
    }

    private fun createUI() {

        val scrollView = ScrollView(this)

        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(24), dp(40), dp(24), dp(30))
            setBackgroundColor(Color.WHITE)
        }

        val title = TextView(this).apply {
            text = "Volume Control"
            textSize = 28f
            setTextColor(Color.BLACK)
        }

        container.addView(
            title,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

        val subtitle = TextView(this).apply {
            text = "Control your phone volume without the physical buttons."
            textSize = 15f
            setTextColor(Color.DKGRAY)
        }

        val subtitleParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        subtitleParams.setMargins(0, dp(8), 0, dp(25))

        container.addView(subtitle, subtitleParams)

        addVolumeSlider(
            container,
            AudioManager.STREAM_MUSIC,
            "🔊  Media"
        )

        addVolumeSlider(
            container,
            AudioManager.STREAM_RING,
            "🔔  Ring"
        )

        addVolumeSlider(
            container,
            AudioManager.STREAM_ALARM,
            "⏰  Alarm"
        )

        val buttonLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
        }

        val minusButton = Button(this).apply {
            text = "−"
            textSize = 22f

            setOnClickListener {
                changeMediaVolume(-1)
            }
        }

        val plusButton = Button(this).apply {
            text = "+"
            textSize = 22f

            setOnClickListener {
                changeMediaVolume(1)
            }
        }

        val buttonParams = LinearLayout.LayoutParams(
            0,
            dp(60),
            1f
        )

        buttonParams.setMargins(0, dp(15), dp(8), 0)

        buttonLayout.addView(
            minusButton,
            buttonParams
        )

        val plusParams = LinearLayout.LayoutParams(
            0,
            dp(60),
            1f
        )

        plusParams.setMargins(
            dp(8),
            dp(15),
            0,
            0
        )

        buttonLayout.addView(
            plusButton,
            plusParams
        )

        container.addView(buttonLayout)

        val note = TextView(this).apply {
            text = "Tip: Add this app to your home screen for quick access."
            textSize = 14f
            setTextColor(Color.GRAY)
        }

        val noteParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        noteParams.setMargins(
            0,
            dp(25),
            0,
            0
        )

        container.addView(note, noteParams)

        scrollView.addView(container)

        setContentView(scrollView)
    }

    private fun addVolumeSlider(
        parent: LinearLayout,
        streamType: Int,
        title: String
    ) {

        val section = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        val label = TextView(this).apply {
            textSize = 18f
            setTextColor(Color.BLACK)
        }

        section.addView(label)

        val maxVolume =
            audioManager.getStreamMaxVolume(streamType)

        val currentVolume =
            audioManager.getStreamVolume(streamType)

        val seekBar = SeekBar(this).apply {
            max = maxVolume
            progress = currentVolume
        }

        section.addView(seekBar)

        fun updateLabel(progress: Int) {

            val percentage =
                if (maxVolume > 0) {
                    progress * 100 / maxVolume
                } else {
                    0
                }

            label.text =
                "$title    $percentage%"
        }

        updateLabel(currentVolume)

        seekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {

                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {

                    if (fromUser) {

                        audioManager.setStreamVolume(
                            streamType,
                            progress,
                            0
                        )
                    }

                    updateLabel(progress)
                }

                override fun onStartTrackingTouch(
                    seekBar: SeekBar?
                ) {
                }

                override fun onStopTrackingTouch(
                    seekBar: SeekBar?
                ) {
                }
            }
        )

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        params.setMargins(
            0,
            dp(10),
            0,
            dp(20)
        )

        parent.addView(section, params)
    }

    private fun changeMediaVolume(direction: Int) {

        val current =
            audioManager.getStreamVolume(
                AudioManager.STREAM_MUSIC
            )

        val max =
            audioManager.getStreamMaxVolume(
                AudioManager.STREAM_MUSIC
            )

        val newVolume =
            (current + direction).coerceIn(0, max)

        audioManager.setStreamVolume(
            AudioManager.STREAM_MUSIC,
            newVolume,
            0
        )

        createUI()
    }

    private fun dp(value: Int): Int {

        return (
            value * resources.displayMetrics.density
        ).toInt()
    }
}