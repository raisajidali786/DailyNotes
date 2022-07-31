package com.rsa.daily.notes.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rsa.daily.notes.R
import com.rsa.daily.notes.databinding.CustomNotesAdapterLayoutBinding
import com.rsa.daily.notes.helper.ClickHandler
import com.rsa.daily.notes.model.NoteModel

class NotesAdapter(
    private val context : Context,
    private val noteList : List<NoteModel>,
    private val clickHandler: ClickHandler
): RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {
    inner class NotesViewHolder(private val binding: CustomNotesAdapterLayoutBinding):
        RecyclerView.ViewHolder(binding.root){
            val title = binding.notesTitle
            val desc = binding.notesDescription
            val date = binding.notesDate
            val time = binding.notesTime
            val more = binding.btnMore
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val binding = DataBindingUtil.inflate<CustomNotesAdapterLayoutBinding>(
            LayoutInflater.from(context),
            R.layout.custom_notes_adapter_layout,
            parent,
            false
        )
        return NotesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.title.text = noteList[position].title
        holder.desc.text = noteList[position].description
        holder.time.text = noteList[position].time
        holder.date.text = noteList[position].date

        holder.more.setOnClickListener {
            val popup = PopupMenu(context, holder.more)
            popup.inflate(R.menu.more_menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.edit -> {
                        clickHandler.onEdit(position,"Edit")
                        true
                    }
                    else -> {
                        showWarningDialog(position)
                        false
                    }
                }
            }
            popup.show()

        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showWarningDialog(position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Are You Sure!")
        builder.setMessage("You want to delete the item")
        builder.setPositiveButton("Yes") { _, _ ->
            clickHandler.onDelete(position)
        }

        builder.setNegativeButton("No") { _, _ ->

        }
        builder.show()
    }

    private fun showMessage(s: String) {
        Toast.makeText(context, "" + s, Toast.LENGTH_SHORT).show()
    }

    override fun getItemCount(): Int {
        return noteList.size
    }
}