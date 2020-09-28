package com.example.noteapp

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView


class NoteAdapter(private val context: Context, private val notes: MutableList<Note>): RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val ll = itemView.findViewById<LinearLayout>(R.id.ll)!!
        val titleTV = itemView.findViewById<TextView>(R.id.titleTV)!!
        val descTV = itemView.findViewById<TextView>(R.id.descTV)!!
        val descAllTV = itemView.findViewById<TextView>(R.id.descAllTV)!!
        val deleteImage = itemView.findViewById<ImageView>(R.id.deleteImage)!!
        val editImage = itemView.findViewById<ImageView>(R.id.editImage)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

        val db = DbManager(context)
        val ids = arrayOf(notes[position].id.toString())

        holder.titleTV.text = notes[position].title
        holder.descTV.text = notes[position].desc
        holder.descAllTV.text = notes[position].desc


        holder.deleteImage.setOnClickListener{
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete")
                .setMessage("Are You Sure You Want To Delete This Note?")
                .setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
                    notes.removeAt(position)
                    this.notifyDataSetChanged()
                    db.deleteRecord(ids)
                    db.sqlDb!!.close()
                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, _ ->
                    dialog.dismiss()
                })
                .create()
                .show()
        }
        var t = 0
        holder.ll.setOnClickListener{
            if (t == 0){
                holder.descAllTV.visibility = View.VISIBLE
                holder.descTV.visibility = View.GONE
                t = 1
            }else{
                holder.descTV.visibility = View.VISIBLE
                holder.descAllTV.visibility = View.GONE
                t = 0
            }
        }

        holder.editImage.setOnClickListener{
            val b = Bundle()
            b.putString("title", notes[position].title)
            b.putString("desc", notes[position].desc)
            b.putInt("id", notes[position].id)
            it.findNavController().navigate(R.id.action_notesListFragment2_to_noteDetailsFragment, b)
        }
    }
}