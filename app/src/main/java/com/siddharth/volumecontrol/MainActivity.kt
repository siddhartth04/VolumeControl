package com.siddharth.volumecontrol

import android.app.Activity
import android.os.Bundle
import android.media.AudioManager
import android.view.Gravity
import android.widget.*
import android.graphics.Color
import android.content.Context

class MainActivity : Activity() {
    private lateinit var audio: AudioManager
    private val streams = listOf(
        Triple(AudioManager.STREAM_MUSIC, "🔊  Media",  "Media volume"),
        Triple(AudioManager.STREAM_RING,  "🔔  Ring",   "Ring volume"),
        Triple(AudioManager.STREAM_ALARM, "⏰  Alarm",  "Alarm volume")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        audio = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        val scroll = ScrollView(this)
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(36, 48, 36, 36)
            setBackgroundColor(Color.WHITE)
        }

        val title = TextView(this).apply {
            text = "Volume Control"
            textSize = 28f
            setTextColor(Color.rgb(25,25,25))
            setPadding(0, 0, 0, 8)
        }
        box.addView(title)

        val subtitle = TextView(this).apply {
            text = "Use this instead of the broken physical volume buttons."
            textSize = 15f
            setTextColor(Color.DKGRAY)
            setPadding(0, 0, 0, 28)
        }
        box.addView(subtitle)

        streams.forEach { (stream, label, desc) ->
            addSlider(box, stream, label)
        }

        val buttons = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            setPadding(0, 28, 0, 0)
        }

        val minus = Button(this).apply {
            text = "−"
            textSize = 24f
            setOnClickListener { changeMedia(-1) }
        }
        val plus = Button(this).apply {
            text = "+"
            textSize = 24f
            setOnClickListener { changeMedia(1) }
        }
        buttons.addView(minus, LinearLayout.LayoutParams(0, 60.dp(), 1f).apply { setMargins(0,0,12.dp(),0) })
        buttons.addView(plus, LinearLayout.LayoutParams(0, 60.dp(), 1f))

        box.addView(buttons)

        val note = TextView(this).apply {
            text = "Tip: Keep this app on your home screen for quick access."
            textSize = 14f
            setTextColor(Color.GRAY)
            setPadding(0, 28, 0, 0)
        }
        box.addView(note)

        scroll.addView(box)
        setContentView(scroll)
    }

    private fun addSlider(parent: LinearLayout, stream: Int, label: String) {
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, 14, 0, 8)
        }

        val text = TextView(this).apply {
            textSize = 18f
            setTextColor(Color.rgb(35,35,35))
        }
        row.addView(text)

        val max = audio.getStreamMaxVolume(stream)
        val slider = SeekBar(this).apply {
            max = max
            progress = audio.getStreamVolume(stream)
        }
        row.addView(slider)

        fun updateLabel() {
            val pct = if (max == 0) 0 else (slider.progress * 100 / max)
            text.text = "$label    $pct%"
        }
        updateLabel()

        slider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(s: SeekBar?, p: Int, fromUser: Boolean) {
                if (fromUser) audio.setStreamVolume(stream, p, 0)
                updateLabel()
            }
            override fun onStartTrackingTouch(s: SeekBar?) {}
            override fun onStopTrackingTouch(s: SeekBar?) {}
        })

        parent.addView(row, LinearLayout.LayoutParams(-1, -2).apply {
            bottomMargin = 12.dp()
        })
    }

    private fun changeMedia(direction: Int) {
        val current = audio.getStreamVolume(AudioManager.STREAM_MUSIC)
        val max = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, (current + direction).coerceIn(0, max), 0)
        recreate()
    }

    private fun Int.dp(): Int = (this * resources.displayMetrics.density).toInt()
}
