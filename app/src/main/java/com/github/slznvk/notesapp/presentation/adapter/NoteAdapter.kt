package com.github.slznvk.notesapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.slznvk.notesapp.R
import com.github.slznvk.notesapp.databinding.CardNoteBinding
import com.github.slznvk.notesapp.domain.dto.Note
import com.github.slznvk.notesapp.domain.dto.Priority

interface OnInteractionListener {
    fun onNoteClick(note: Note)
}

class NoteAdapter(
    private val onInteractionListener: OnInteractionListener
) : ListAdapter<Note, NoteViewHolder>(NoteDiffCallBack) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = CardNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(view, onInteractionListener)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note)
    }
}

class NoteViewHolder(
    private val binding: CardNoteBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(note: Note) {
        with(binding) {
            title.text = note.title
            content.text = note.content
            time.text = note.time
            root.setOnClickListener { onInteractionListener.onNoteClick(note) }

            priorityIcon.isVisible = note.priority != Priority.NOT
            when (note.priority) {
                Priority.LOW -> priorityIcon.apply {
                    setImageResource(R.drawable.priority_icon)
                }

                Priority.MEDIUM -> priorityIcon.apply {
                    setImageResource(R.drawable.priority_icon_medium)
                }

                Priority.HIGH -> priorityIcon.apply {
                    setImageResource(R.drawable.priority_icon_high)
                }

                else -> priorityIcon.isVisible = false
            }
        }
    }
}

object NoteDiffCallBack : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Note, newItem: Note) = oldItem == newItem
}