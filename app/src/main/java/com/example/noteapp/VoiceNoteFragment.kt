package com.example.noteapp

import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.*
import androidx.fragment.app.Fragment
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_voice_note.*

/**
 * A simple [Fragment] subclass.
 */
class VoiceNoteFragment : Fragment() {

    var running = false
    var pauseOffset = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_voice_note, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        starBtn.setOnClickListener{
            if (!running){
                chronometer.base = SystemClock.elapsedRealtime() - pauseOffset
                chronometer.start()
                running = true
                starBtn.setImageDrawable(resources.getDrawable(R.drawable.ic_pause_black_24dp, null))
            }else{
                chronometer.stop()
                pauseOffset = SystemClock.elapsedRealtime() - chronometer.base
                running = false
                starBtn.setImageDrawable(resources.getDrawable(R.drawable.ic_play_arrow_black_24dp, null))
            }
        }

        stopBtn.setOnClickListener{
            pauseOffset = 0L
            chronometer.stop()
            chronometer.base = SystemClock.elapsedRealtime() - pauseOffset
            running = false
            starBtn.setImageDrawable(resources.getDrawable(R.drawable.ic_fiber_manual_record_black_24dp, null))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.note_details_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.close_menu->view?.findNavController()?.navigate(R.id.action_voiceNoteFragment_to_notesListFragment2)
        }
        return super.onOptionsItemSelected(item)
    }

}