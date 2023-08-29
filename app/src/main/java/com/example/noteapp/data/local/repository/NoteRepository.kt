package com.example.noteapp.data.local.repository

import androidx.lifecycle.LiveData
import com.example.noteapp.data.local.dao.NoteDao
import com.example.noteapp.model.NoteModel

class NoteRepository(private val noteDao: NoteDao) {
    val allNotes: LiveData<List<NoteModel>> = noteDao.getAllNotes()

    fun insertOrUpdate(note: NoteModel) {
        noteDao.insertOrUpdate(note)
    }

    fun delete(note: NoteModel) {
        noteDao.delete(note)
    }
}
