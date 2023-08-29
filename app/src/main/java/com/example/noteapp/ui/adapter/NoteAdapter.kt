package com.example.noteapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.ui.listeners.NoteClickListener
import com.example.noteapp.databinding.RecyclerRowNoteBinding
import com.example.noteapp.model.NoteModel
import java.util.*

class NoteAdapter(private val noteModelArrayList: ArrayList<NoteModel>) : RecyclerView.Adapter<NoteAdapter.NoteHolder>() {
    private var longClickListener: NoteClickListener? = null
    private var noteClickListener: NoteClickListener? = null

    fun setOnNoteClickListener(listener: NoteClickListener) {
        noteClickListener = listener
    }

    fun setOnNoteLongClickListener(listener: NoteClickListener) {
        longClickListener = listener
    }

    inner class NoteHolder(val binding: RecyclerRowNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    noteClickListener?.onNoteClickListener(position)
                }
            }

            binding.root.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    longClickListener?.onNoteClickListener(position)
                    true
                }
                else {
                    false
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val binding = RecyclerRowNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        holder.binding.noteDate.text = noteModelArrayList[position].date
        holder.binding.noteTitle.text = noteModelArrayList[position].title
    }

    override fun getItemCount(): Int {
        return noteModelArrayList.size
    }
}