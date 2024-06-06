package com.github.slznvk.notesapp.presentation.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.slznvk.notesapp.databinding.FragmentNoteBinding
import com.github.slznvk.notesapp.domain.dto.Priority
import com.github.slznvk.notesapp.presentation.ui.NoteListFragment.Companion.ID_NOTE
import com.github.slznvk.notesapp.presentation.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class NoteFragment : Fragment() {

    private lateinit var binding: FragmentNoteBinding
    private val viewModel: NoteViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteBinding.inflate(inflater, container, false)
        val id = arguments?.getLong(ID_NOTE)

        binding.apply {
            id?.let { viewModel.getNoteById(it) }
            contentEditText.requestFocus()
            viewModel.pickedNote.observe(viewLifecycleOwner) {
                titleEditText.setText(it.title)
                contentEditText.setText(it.content)
                when (it.priority) {
                    Priority.LOW -> lowButton.isChecked = true
                    Priority.MEDIUM -> mediumButton.isChecked = true
                    Priority.HIGH -> highButton.isChecked = true
                    else -> {
                        lowButton.isChecked = false
                        mediumButton.isChecked = false
                        highButton.isChecked = false
                    }
                }
            }

            lowButton.setOnClickListener {
                viewModel.changePriority(Priority.LOW)
            }
            mediumButton.setOnClickListener {
                viewModel.changePriority(Priority.MEDIUM)
            }
            highButton.setOnClickListener {
                viewModel.changePriority(Priority.HIGH)
            }

            removeNoteButton.setOnClickListener {
                AlertDialog.Builder(requireActivity()).apply {
                    setTitle("Deleting a note")
                    setMessage("Do you really want to delete the note?")
                    setPositiveButton("Yes") { _, _ ->
                        if (id != null) {
                            viewModel.removeNoteById(id)
                        } else {
                            viewModel.cancel()
                        }
                        findNavController().navigateUp()
                    }
                    setNegativeButton("No") { _, _ -> }
                    setCancelable(true)
                }.create().show()
            }
            requireActivity().onBackPressedDispatcher.addCallback(
                viewLifecycleOwner, object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        if (contentEditText.text.isNotBlank()) {
                            viewModel.changeContent(
                                content = contentEditText.text.toString(),
                                time = getTime(),
                                title = titleEditText.text.toString(),
                            )
                            viewModel.saveNote()
                        }
                        findNavController().navigateUp()
                    }
                })
            toolbar.setNavigationOnClickListener {
                if (contentEditText.text.isNotBlank()) {
                    viewModel.changeContent(
                        content = contentEditText.text.toString(),
                        time = getTime(),
                        title = titleEditText.text.toString(),
                    )
                    viewModel.saveNote()
                }
                findNavController().navigateUp()
            }
            return root
        }
    }
}

private fun getTime(): String {
    val calendar = Calendar.getInstance()
    val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
    val currentMonth = SimpleDateFormat("MMMM", Locale.getDefault()).format(calendar.time)
    val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
    return "$currentMonth $currentDay $currentTime"
}