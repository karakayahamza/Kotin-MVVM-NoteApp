package com.example.noteapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.noteapp.data.local.database.NoteDatabase
import com.example.noteapp.data.local.repository.NoteRepository
import com.example.noteapp.model.NoteModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {
        private val repository: NoteRepository

        init {
                val noteDao = NoteDatabase.getInstance(application).noteDao()
                repository = NoteRepository(noteDao)
        }

        val allNotes: LiveData<List<NoteModel>> = repository.allNotes

        fun insertOrUpdate(note: NoteModel) = viewModelScope.launch(Dispatchers.IO) {
                repository.insertOrUpdate(note)
        }

        fun delete(note: NoteModel) = viewModelScope.launch(Dispatchers.IO) {
                repository.delete(note)
        }

        /*fun updatePosition(noteArrayList: ArrayList<NoteModel>)= viewModelScope.launch(Dispatchers.IO) {
                repository.updatePosition(noteArrayList)

                try {
                        // Notları sıra numarasına göre güncellemek için bir döngü oluşturun
                        for ((index, note) in noteArrayList.withIndex()) {
                                // Her notun sıra numarasını güncelleyin
                                note.id = index.toLong()
                        }
                        // Güncellenmiş notları Room veritabanına kaydedin
                        repository.updatePosition(noteArrayList)
                } catch (e: Exception) {
                // Hata işleme kodunu burada ekleyin
                }
        }*/
}

