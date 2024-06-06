package com.github.slznvk.notesapp.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.slznvk.notesapp.R
import com.github.slznvk.notesapp.databinding.FragmentNoteListBinding
import com.github.slznvk.notesapp.domain.dto.Note
import com.github.slznvk.notesapp.presentation.adapter.NoteAdapter
import com.github.slznvk.notesapp.presentation.adapter.OnInteractionListener
import com.github.slznvk.notesapp.presentation.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteListFragment : Fragment() {
    private lateinit var binding: FragmentNoteListBinding
    private val viewModel: NoteViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteListBinding.inflate(layoutInflater, container, false)
        val adapter = NoteAdapter(object : OnInteractionListener {
            override fun onNoteClick(note: Note) {
                viewModel.edit(note)
                findNavController().navigate(
                    R.id.action_noteListFragment_to_editNoteFragment,
                    Bundle().apply {
                        putLong(ID_NOTE, note.id)
                    })
            }
        })
        binding.apply {
            recyclerView.adapter = adapter
            viewModel.data.observe(viewLifecycleOwner) { list ->
                adapter.submitList(list.sortedBy { it.priority.code })
            }

            viewModel.dataState.observe(viewLifecycleOwner) {
                errorGroup.isVisible = it.error
            }

            addNoteButton.setOnClickListener {
                findNavController().navigate(R.id.action_noteListFragment_to_editNoteFragment)
            }

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                android.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.filter(newText)
                    viewModel.filteredData.observe(viewLifecycleOwner) { list ->
                        adapter.submitList(list.sortedBy { it.priority.code })
                        notFoundText.isVisible = list.isEmpty()
                    }
                    return false
                }
            })

            return root
        }
    }

    companion object {
        const val ID_NOTE = "ID"
    }
}