package com.example.noteapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.noteapp.databinding.FragmentNoteEditScreenBinding
import com.example.noteapp.model.NoteModel
import com.example.noteapp.viewModel.NoteViewModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class NoteEditScreen : Fragment() {

    private var _binding: FragmentNoteEditScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var noteViewModel: NoteViewModel
    private var note: NoteModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentNoteEditScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()

        arguments?.let {
            val receivedData = NoteEditScreenArgs.fromBundle(it).note
            if (receivedData != null) {
                note = receivedData
                binding.noteText.setText(note!!.content)
                binding.noteTitle.setText(note!!.title)
            }
        }

        binding.saveButton.setOnClickListener {
            saveNote()
            navigateBackToMainScreen()
            onDestroy()
        }
        binding.backHome.setOnClickListener {
            navigateBackToMainScreen()
        }
    }

    private fun setupViewModel() {
        noteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]
    }

    private fun saveNote() {
        val title = binding.noteTitle.text.toString()
        val content = binding.noteText.text.toString()
        val formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm", Locale("tr", "TR"))
        val currentDate = formatter.format(LocalDateTime.now())

        if (note == null) {
            note = NoteModel(title, content, currentDate)
        }

        note!!.title = title
        note!!.content = content
        note!!.date = currentDate

        noteViewModel.insertOrUpdate(note!!)
    }

    private fun navigateBackToMainScreen() {
        val action = NoteEditScreenDirections.actionNoteEditScreenToMainScreen()
        Navigation.findNavController(requireView()).navigate(action)

        // Delete NoteEditScreen
        /**
        Navigation.findNavController(requireView()).navigate(action,NavOptions.Builder()
        .setPopUpTo(R.id.noteEditScreen,true)
        .build()
        )*/
        // Or you can do it in navigation xml pop up behavior inclusive and itself

    }
}
