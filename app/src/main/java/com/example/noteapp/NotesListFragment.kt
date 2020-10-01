package com.example.noteapp

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_notes_list.*

/**
 * A simple [Fragment] subclass.
 */
class NotesListFragment : Fragment() {

    private var notes = mutableListOf<Note>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        notesRV.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
        searchQuery("%")
    }

    private fun searchQuery(title: String){
        val dbManager = DbManager(this.requireContext())
        val projections = arrayOf("id", "title", "description")
        val selectionArgs = arrayOf(title)
        val cursor = dbManager.query(projections, "title Like ?", selectionArgs)

        if(cursor.moveToFirst()){
            notes.clear()
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val colTitle = cursor.getString(cursor.getColumnIndex("title"))
                val desc = cursor.getString(cursor.getColumnIndex("description"))
                notes.add(Note(id, colTitle, desc))
            }while (cursor.moveToNext())
            cursor.close()
            dbManager.sqlDb!!.close()
        }

        notes.sortBy { it.id }
        notes.reverse()
        val adapter = NoteAdapter(this.requireContext(), notes)
        notesRV.adapter = adapter
        adapter.notifyDataSetChanged()
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.note_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.add_note_menu -> view?.findNavController()?.navigate(R.id.action_notesListFragment2_to_noteDetailsFragment)
            R.id.add_voice_note_menu -> view?.findNavController()?.navigate(R.id.action_notesListFragment2_to_voiceNoteFragment)
            R.id.voice_note_menu -> view?.findNavController()?.navigate(R.id.action_notesListFragment2_to_voiceNotesFragment)

        }
        return super.onOptionsItemSelected(item)
    }
}
