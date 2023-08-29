package com.example.noteapp.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteapp.ui.listeners.NoteClickListener
import com.example.noteapp.ui.adapter.NoteAdapter
import com.example.noteapp.databinding.FragmentMainScreenBinding
import com.example.noteapp.model.NoteModel
import com.example.noteapp.util.CustomAlertDialog
import com.example.noteapp.viewModel.NoteViewModel

class MainScreen : Fragment() {

    private lateinit var binding: FragmentMainScreenBinding
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteModelArrayList: ArrayList<NoteModel>
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var note: List<NoteModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupRecyclerView()
        setupAddButton()
        setupNoteItemClickListeners()
        setupNoteItemLongClickListeners()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupUI() {
        noteModelArrayList = ArrayList()
        noteAdapter = NoteAdapter(noteModelArrayList)
        noteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]

        noteViewModel.allNotes.observe(viewLifecycleOwner) { notes ->
            note = notes
            noteModelArrayList.apply {
                clear()
                addAll(notes)
                checkRecyclerView(noteModelArrayList)
            }
            noteAdapter.notifyDataSetChanged()
        }
    }

    private fun setupRecyclerView() {
        binding.recycylerViewNotes.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = noteAdapter
        }
    }

    private fun setupAddButton() {
        binding.addNewNote.setOnClickListener {
            val action = MainScreenDirections.actionMainScreenToNoteEditScreen(null)
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun setupNoteItemClickListeners() {
        noteAdapter.setOnNoteClickListener(object : NoteClickListener {
            override fun onNoteClickListener(position: Int) {
                val action = MainScreenDirections.actionMainScreenToNoteEditScreen(noteModelArrayList[position])
                Navigation.findNavController(binding.root).navigate(action)
            }
        })
    }

    private fun setupNoteItemLongClickListeners() {
        noteAdapter.setOnNoteLongClickListener(object : NoteClickListener {
            override fun onNoteClickListener(position: Int) {
                val customDialog = CustomAlertDialog(requireContext()){
                    val noteToDelete = noteModelArrayList[position]
                    noteViewModel.delete(noteToDelete)
                    noteModelArrayList.removeAt(position)
                    noteAdapter.notifyItemRemoved(position)
                    checkRecyclerView(noteModelArrayList)
                }
                customDialog.showDialog("Bu notu silmek istiyor musunuz?")
            }
        })
    }

    fun checkRecyclerView(notes : ArrayList<NoteModel>){
        if (notes.size==0){
            binding.noNotes.visibility = View.VISIBLE
        }
        else{
            binding.noNotes.visibility = View.GONE
        }
    }
}
