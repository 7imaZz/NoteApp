package com.example.noteapp

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView

class VoiceNoteAdapter(val context: Context, private val voiceNotes: MutableList<VoiceNote>):
    RecyclerView.Adapter<VoiceNoteAdapter.VoiceNoteViewHolder>() {
    var player: MediaPlayer? = null
    class VoiceNoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val titleTextView = itemView.findViewById<TextView>(R.id.titleTV)!!
        val dateTextView = itemView.findViewById<TextView>(R.id.dateTV)!!
        val durationTextView = itemView.findViewById<TextView>(R.id.durationTV)!!
        val voiceSeekBar = itemView.findViewById<SeekBar>(R.id.seekBar)!!
        val playImageButton = itemView.findViewById<ImageButton>(R.id.playBtn)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoiceNoteViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.voice_note_item, parent, false)
        return VoiceNoteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return voiceNotes.size
    }

    override fun onBindViewHolder(holder: VoiceNoteViewHolder, position: Int) {
        holder.titleTextView.text = voiceNotes[position].title
        holder.dateTextView.text = voiceNotes[position].date
        holder.voiceSeekBar.isEnabled = false

        holder.playImageButton.setOnClickListener {

            holder.playImageButton.setImageResource(R.drawable.ic_stop_black_24dp)
            holder.playImageButton.isEnabled = false

            val voiceNotePath = voiceNotes[position].directory
            player = MediaPlayer.create(context, Uri.parse(voiceNotePath))
            player!!.start()

            holder.voiceSeekBar.max = player!!.duration
            holder.durationTextView.text = player!!.duration.toString()
            val handler = Handler()
            handler.postDelayed(object : Runnable {
                @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                override fun run() {
                    try {
                        holder.voiceSeekBar.progress = player!!.currentPosition
                        handler.postDelayed(this, 1000)
                        if (!player!!.isPlaying) {
                            player!!.release()
                            holder.voiceSeekBar.progress = 0
                            Toast.makeText(context, "Ended", Toast.LENGTH_SHORT).show()
                            holder.playImageButton.setImageResource(R.drawable.ic_play_arrow_black_24dp)
                            holder.playImageButton.isEnabled = true
                        }
                    } catch (e: Exception) {
                        holder.voiceSeekBar.progress = 0
                        Log.e("Ex: ", e.message.toString())
                    }
                }
            }, 0)

        }
    }
}