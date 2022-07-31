package com.rsa.daily.notes.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rsa.daily.notes.R
import com.rsa.daily.notes.adapter.NotesAdapter
import com.rsa.daily.notes.controller.NoteRepository
import com.rsa.daily.notes.controller.NoteViewModelFactory
import com.rsa.daily.notes.databinding.ActivityMainBinding
import com.rsa.daily.notes.db.NoteDatabase
import com.rsa.daily.notes.helper.ClickHandler
import com.rsa.daily.notes.model.NoteModel
import com.rsa.daily.notes.viewModel.NoteViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : BaseActivity(), ClickHandler {
    private var editNotePos: Int = 0
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: NoteViewModel
    private lateinit var noteDatabase: NoteDatabase
    private lateinit var repository: NoteRepository
    private lateinit var factory: NoteViewModelFactory
    private lateinit var notesList: List<NoteModel>

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.onClick = this@MainActivity
        inIt()
        showData()
    }

    private fun inIt() {
        noteDatabase = NoteDatabase(this)
        repository = NoteRepository(noteDatabase)
        factory = NoteViewModelFactory(repository)
        notesList = ArrayList<NoteModel>()
        viewModel = ViewModelProvider(this, factory)[NoteViewModel::class.java]
    }

    override fun onClick(vararg objects: Any?) {
        val view = objects[0] as View
        when (view.id) {
            R.id.btnAddNote -> {
                showDialogToAddNote("Add")
            }
            R.id.createNoteTv -> {
                showDialogToAddNote("Add")
            }
        }
    }

    override fun onEdit(position: Int, type: String) {
        editNotePos = position
        showDialogToAddNote(type)
    }

    override fun onDelete(position: Int) {
        deleteNoteById(notesList[position].id)
    }

    @SuppressLint("CutPasteId")
    private fun showDialogToAddNote(type: String) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.custom_add_note_layout)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCanceledOnTouchOutside(false)
        val window = dialog.window
        window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        dialog.show()

        val noteTitle = dialog.findViewById<EditText>(R.id.addNoteTitle)
        val noteDesc = dialog.findViewById<EditText>(R.id.addNotesDescription)
        val btnCancel = dialog.findViewById<Button>(R.id.btnCancel)
        val btnSave = dialog.findViewById<Button>(R.id.btnSave)

        if (type == "Edit") {
            noteTitle.setText(notesList[editNotePos].title)
            noteDesc.setText(notesList[editNotePos].description)
        }

        btnSave.setOnClickListener {
            val title = noteTitle.text.toString()
            val desc = noteDesc.text.toString()
            if (TextUtils.isEmpty(title)) {
                noteTitle.requestFocus()
                noteTitle.error = "Enter Title"
            } else if (TextUtils.isEmpty(desc)) {
                noteDesc.requestFocus()
                noteDesc.error = "Enter Description"
            } else {
                dialog.dismiss()
                if (type == "Edit") {
                    updateData(notesList[editNotePos].id,title,desc,
                        notesList[editNotePos].time.toString(), notesList[editNotePos].date.toString()
                    )
                } else {
                    insertData(noteTitle.text.toString(), noteDesc.text.toString())
                }
            }
        }
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

    }

    private fun setNotesAdapter() {
        notesAdapter = NotesAdapter(this, notesList, this)
        binding.notesRecycle.layoutManager = LinearLayoutManager(this)
        binding.notesRecycle.adapter = notesAdapter
    }

    //CREATE Note
    @SuppressLint("SimpleDateFormat")
    private fun insertData(title: String, desc: String) {

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        val dateTime = currentDate.split(" ").toTypedArray()
        val note = NoteModel(
            id = null,
            title = title,
            description = desc,
            time = dateTime[1],
            date = dateTime[0]
        )
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.insertNote(note).also {
                showToast("Note Created")
            }
        }
    }

    //READ DATA
    private fun showData() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getAllNotes().observe(this@MainActivity) {
                notesList = it
                if(notesList.isEmpty()){
                    binding.firstView.visibility = View.VISIBLE
                    binding.notesRecycle.visibility = View.GONE
                    binding.btnAddNote.visibility = View.GONE
                }else{
                    binding.firstView.visibility = View.GONE
                    binding.notesRecycle.visibility = View.VISIBLE
                    binding.btnAddNote.visibility = View.VISIBLE
                    setNotesAdapter()
                }
            }
        }
    }

    //UPDATE DATA
    private fun updateData(id: Int?,title: String,desc: String,time : String,date : String) {
        val note =
            NoteModel(id = id, title = title, description = desc, time = time, date = date)
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.updateNote(note).also {

            }
        }
    }

    //DELETE BY ID
    @SuppressLint("NotifyDataSetChanged")
    private fun deleteNoteById(id: Int?) {

        CoroutineScope(Dispatchers.Main).launch {
            if (id != null) {
                viewModel.deleteNoteById(id).also {
                        showToast("Deleted")
                }
            }
        }
    }

    //DELETE ALL
    private fun clearNote() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.clearNote().also {
                //do action here
            }
        }
    }


}