package com.example.noteapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.text.Editable
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_note_details.*

/**
 * A simple [Fragment] subclass.
 */
class NoteDetailsFragment : Fragment() {

    private var isEditMode = false
    private var recordId = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note_details, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        requireActivity().bottomNav.visibility = View.GONE

        if (arguments!=null){
            val title = requireArguments().getString("title")
            val desc = requireArguments().getString("desc")
            recordId = requireArguments().getInt("id")
            val eTitle = Editable.Factory.getInstance().newEditable(title.toString())
            val dTitle = Editable.Factory.getInstance().newEditable(desc.toString())
            titleET.text = eTitle
            descET.text = dTitle
            createNoteBtn.text = "Edit"
            isEditMode = true
        }

        createNoteBtn.setOnClickListener{
            if (!isEditMode) {
                addNote(titleET.text.toString(), descET.text.toString())
                view.findNavController().navigate(R.id.action_noteDetailsFragment_to_notesListFragment2)
            }else{
                editNote(recordId, titleET.text.toString(), descET.text.toString())
                view.findNavController().navigate(R.id.action_noteDetailsFragment_to_notesListFragment2)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.note_details_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.close_menu->view?.findNavController()?.navigate(R.id.action_noteDetailsFragment_to_notesListFragment2)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addNote(title: String, desc: String){
        val values = ContentValues()
        values.put("title", title)
        values.put("description", desc)

        val db = DbManager(this.requireContext())
        val id = db.insertRecord(values)
        db.sqlDb!!.close()

        if (id>0){
            Toast.makeText(this.activity, "Note Added With ID: $id", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this.activity, "There Is A problem!!", Toast.LENGTH_SHORT).show()
        }

    }

    private fun editNote(id: Int, title: String, desc: String){
        val ids = arrayOf(id.toString())
        val db = DbManager(this.requireContext())
        db.editRecord(ids,title, desc)
        Toast.makeText(this.activity, "Note Edited", Toast.LENGTH_SHORT).show()
        db.sqlDb!!.close()
    }
}
