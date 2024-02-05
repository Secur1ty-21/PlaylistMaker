package ru.yamost.playlistmaker.create.presentation.ui

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.yamost.playlistmaker.R
import ru.yamost.playlistmaker.create.presentation.CreateAction
import ru.yamost.playlistmaker.create.presentation.CreateEvent
import ru.yamost.playlistmaker.create.presentation.CreateViewModel
import ru.yamost.playlistmaker.databinding.FragmentCreateBinding

class CreateFragment : Fragment() {
    private var _binding: FragmentCreateBinding? = null
    private val binding get() = _binding!!
    private val albumNameTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(p0: Editable?) {
            binding.btnCreatePlaylist.isEnabled = !p0?.toString().isNullOrEmpty()
            viewModel.obtainEvent(CreateEvent.OnAlbumNameType(p0.toString()))
        }
    }
    private val albumDescriptionTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(p0: Editable?) {
            viewModel.obtainEvent(CreateEvent.OnAlbumDescriptionType(p0.toString()))
        }
    }
    private val photoPick = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        if (it != null) {
            setImageViewWithUri(it)
            viewModel.obtainEvent(CreateEvent.OnPhotoCaptured(it))
        }
    }
    private val viewModel by viewModel<CreateViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observeAction().observe(viewLifecycleOwner) { action ->
            action?.let {
                when (it) {
                    is CreateAction.ShowAcceptedDialog -> {
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle(R.string.create_accepted_dialog_title)
                            .setMessage(R.string.create_accepted_dialog_msg)
                            .setPositiveButton(R.string.create_accepted_dialog_pos_btn) { _, _ ->
                                it.onPosBtnClick()
                                findNavController().popBackStack()
                            }
                            .setNegativeButton(R.string.create_accepted_dialog_neg_btn) { _, _ ->
                                it.onCancelBtnClick()
                            }
                            .show()
                    }

                    is CreateAction.NavigateBack -> {
                        findNavController().popBackStack()
                    }

                    is CreateAction.NavigateBackWithResult -> {
                        setFragmentResult(
                            RESULT_KEY_CREATE_SUCCESS, bundleOf(
                                KEY_NAME_CREATED_PLAYLIST to it.albumName
                            )
                        )
                        findNavController().navigateUp()
                    }
                }
            }
        }
        if (viewModel.state.photo != null) {
            setImageViewWithUri(viewModel.state.photo)
        }
        binding.topAppBar.setNavigationOnClickListener {
            viewModel.obtainEvent(CreateEvent.OnBackRequested)
        }
        binding.cover.setOnClickListener {
            photoPick.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.btnCreatePlaylist.setOnClickListener {
            viewModel.obtainEvent(CreateEvent.OnBtnCreateClick)
        }
        binding.editAlbumName.addTextChangedListener(albumNameTextWatcher)
        binding.editAlbumDescription.addTextChangedListener(albumDescriptionTextWatcher)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.obtainEvent(CreateEvent.OnBackRequested)
        }
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    private fun setImageViewWithUri(uri: Uri?) {
        binding.cover.background = null
        Glide.with(binding.cover)
            .load(uri)
            .placeholder(R.drawable.ic_add_photo)
            .transform(
                CenterCrop(),
                RoundedCorners(resources.getDimensionPixelSize(R.dimen.cornerRadiusS))
            )
            .into(binding.cover)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.editAlbumName.removeTextChangedListener(albumNameTextWatcher)
        binding.editAlbumDescription.removeTextChangedListener(albumDescriptionTextWatcher)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }

    companion object {
        const val RESULT_KEY_CREATE_SUCCESS = "create_success"
        const val KEY_NAME_CREATED_PLAYLIST = "created_playlist"
    }
}