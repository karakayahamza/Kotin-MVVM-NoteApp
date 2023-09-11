package com.example.noteapp.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteapp.ui.listeners.NoteClickListener
import com.example.noteapp.ui.adapter.NoteAdapter
import com.example.noteapp.databinding.FragmentMainScreenBinding
import com.example.noteapp.model.NoteModel
import com.example.noteapp.util.CustomAlertDialog
import com.example.noteapp.viewModel.NoteViewModel
import java.text.Normalizer
import java.util.regex.Pattern

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

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupRecyclerView()
        setupAddButton()
        setupNoteItemClickListeners()
        setupNoteItemLongClickListeners()

        binding.searchView.clearFocus()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun filterList(newText: String?) {
        val filteredList = ArrayList<NoteModel>()
        val normalizedText = normalizeText(newText)
        for (item in note) {
            val normalizedTitle = normalizeText(item.title)
            if (normalizedTitle.contains(normalizedText)) {
                filteredList.add(item)
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(requireContext(), "Not bulunamadı.", Toast.LENGTH_SHORT).show()
        } else {
            noteAdapter.setFilteredList(filteredList)
            noteAdapter.notifyDataSetChanged()
        }
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
            @SuppressLint("NotifyDataSetChanged")
            override fun onNoteClickListener(position: Int) {
                val customDialog = CustomAlertDialog(requireContext(),
                    onOkButtonClickListener = {
                        val noteToDelete = noteModelArrayList[position]
                        noteViewModel.delete(noteToDelete)
                        noteModelArrayList.removeAt(position)
                        noteAdapter.notifyItemRemoved(position)
                        checkRecyclerView(noteModelArrayList)
                        Toast.makeText(requireContext(),"Not silindi.",Toast.LENGTH_SHORT).show()
                    })
                customDialog.showDialog("Bu notu silmek istiyor musunuz?")
        } })
    }
    private fun normalizeText(input: String?): String {
        if (input == null) return ""
        val normalizedString = Normalizer.normalize(input, Normalizer.Form.NFD)
        val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
        return pattern.matcher(normalizedString).replaceAll("").uppercase()
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
