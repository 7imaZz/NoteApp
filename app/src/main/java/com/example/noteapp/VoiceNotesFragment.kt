package com.example.noteapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_voice_notes.*

/**
 * A simple [Fragment] subclass.
 */

//@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class VoiceNotesFragment : Fragment() {
    //private var player: MediaPlayer? = null

    private var sqlDb: VoiceNotesDbManager? = null
    private val voiceNotes = mutableListOf<VoiceNote>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_voice_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().bottomNav.visibility = View.VISIBLE

        setHasOptionsMenu(true)
        sqlDb = VoiceNotesDbManager(requireContext())

        voiceNotesRV.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        voiceNotesRV.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        searchQuery()


    }

    @SuppressLint("SimpleDateFormat")
    private fun searchQuery(){
        val dbManager = VoiceNotesDbManager(this.requireContext())
        val projections = arrayOf(Constants().V_ID, Constants().V_COL_TITLE, Constants().V_COL_DIR, Constants().V_COL_DATE)
        val selectionArgs = arrayOf("%")
        val cursor = dbManager.query(projections, "title Like ?", selectionArgs)

        if(cursor.moveToFirst()){
            voiceNotes.clear()
            do {
                val id = cursor.getInt(cursor.getColumnIndex(Constants().V_ID))
                val colTitle = cursor.getString(cursor.getColumnIndex(Constants().V_COL_TITLE))
                val colDir = cursor.getString(cursor.getColumnIndex(Constants().V_COL_DIR))
                val colDate = cursor.getString(cursor.getColumnIndex(Constants().V_COL_DATE))

                voiceNotes.add(VoiceNote(id, colTitle, colDate, colDir, ""))
            }while (cursor.moveToNext())
            cursor.close()
            dbManager.sqlDb!!.close()
        }

        voiceNotes.sortBy { it.id }
        voiceNotes.reverse()
        val adapter = VoiceNoteAdapter(this.requireContext(), voiceNotes)
        voiceNotesRV.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.voice_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.add_voice_note_menu -> view?.findNavController()?.navigate(R.id.action_voiceNotesFragment_to_voiceNoteFragment)
        }
        return super.onOptionsItemSelected(item)
    }
}
