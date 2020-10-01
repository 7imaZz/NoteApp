package com.example.noteapp


import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.view.*
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_voice_note.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class VoiceNoteFragment : Fragment() {

    var pauseOffset = 0L
    lateinit var outputfile: String
    private var myAudioRecorder: MediaRecorder? = null

    private var c = 0
    private val recordCount = "record_count"
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var sqlDb: VoiceNotesDbManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_voice_note, container, false)
    }

    @SuppressLint("CommitPrefEdits", "SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        editor = requireContext().getSharedPreferences(recordCount, Context.MODE_PRIVATE).edit()
        sqlDb = VoiceNotesDbManager(requireContext())

        val prefs = requireContext().getSharedPreferences(recordCount, Context.MODE_PRIVATE)
        c = prefs.getInt("count", 0)
        record(starBtn)
        

        stopBtn.setOnClickListener{
            if (myAudioRecorder != null) {

                val sdf = SimpleDateFormat("dd/M/yyyy")
                val currentDate = sdf.format(Date())

                val id = sqlDb.insertVoiceNote("Voice Note $c", outputfile, currentDate)
                if(id>0) {
                    Toast.makeText(requireContext(), "id: $id, duration: ${chronometer.text}", Toast.LENGTH_SHORT).show()
                }
                myAudioRecorder!!.stop()
                myAudioRecorder!!.release()
                myAudioRecorder = null
                pauseOffset = 0L
                chronometer.stop()
                chronometer.base = SystemClock.elapsedRealtime() - pauseOffset
                starBtn.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.note_details_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.close_menu -> view?.findNavController()?.navigate(R.id.action_voiceNoteFragment_to_notesListFragment2)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupMediaRecorder(){
        myAudioRecorder = MediaRecorder()
        outputfile = Environment.getExternalStorageDirectory().absolutePath+"/voiceRecord${c}.3gpp"
        editor.putInt("count", ++c)
        editor.apply()
        myAudioRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        myAudioRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        myAudioRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        myAudioRecorder!!.setOutputFile(outputfile)
    }
    private fun record(btn: ImageButton) {

        btn.setOnClickListener {
            setupMediaRecorder()
            myAudioRecorder!!.prepare()
            myAudioRecorder!!.start()
            chronometer.base = SystemClock.elapsedRealtime() - pauseOffset
            chronometer.start()
            starBtn.visibility = View.GONE
            Toast.makeText(requireContext(), outputfile, Toast.LENGTH_SHORT).show()
        }
    }
}