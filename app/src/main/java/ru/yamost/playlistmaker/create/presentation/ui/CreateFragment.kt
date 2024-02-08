package ru.yamost.playlistmaker.create.presentation.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.yamost.playlistmaker.R
import ru.yamost.playlistmaker.create.presentation.CreateAction
import ru.yamost.playlistmaker.create.presentation.CreateEvent
import ru.yamost.playlistmaker.create.presentation.CreateViewModel
import ru.yamost.playlistmaker.databinding.FragmentCreateBinding

class CreateFragment : Fragment() {
    private var _binding: FragmentCreateBinding? = null
    private val binding get() = _binding!!
    private val photoPick = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        if (it != null) {
            setImageViewWithUri(it)
            viewModel.obtainEvent(CreateEvent.OnPhotoCaptured(it))
        }
    }
    private var playlistId: Int? = null
    private val viewModel by viewModel<CreateViewModel> { parametersOf(playlistId!!) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { playlistId = CreateFragmentArgs.fromBundle(it).playlistId }
    }

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
        if (playlistId != null && playlistId != -1) {
            binding.topAppBar.title = getString(R.string.create_update_playlist_toolbar_title)
            binding.btnCreatePlaylist.text = getString(R.string.create_btn_update)
        }
        viewModel.action.observe(viewLifecycleOwner) { action ->
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

                    is CreateAction.SetUiWithPlaylist -> {
                        it.playlist.imageUri?.let { uri ->
                            setImageViewWithUri(uri)
                        }
                        binding.editAlbumName.setText(it.playlist.name)
                        binding.editAlbumDescription.setText(it.playlist.description)
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
        binding.editAlbumName.doOnTextChanged { text, _, _, _ ->
            binding.btnCreatePlaylist.isEnabled = !text.isNullOrEmpty()
            viewModel.obtainEvent(CreateEvent.OnAlbumNameType(text?.toString() ?: ""))
        }
        binding.editAlbumDescription.doOnTextChanged { text, _, _, _ ->
            viewModel.obtainEvent(CreateEvent.OnAlbumDescriptionType(text?.toString() ?: ""))
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.obtainEvent(CreateEvent.OnBackRequested)
        }
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    private fun setImageViewWithUri(uri: Uri?) {
        binding.cover.background = null
        binding.cover.scaleType = ImageView.ScaleType.CENTER_CROP
        binding.cover.setImageURI(uri)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }

    companion object {
        const val RESULT_KEY_CREATE_SUCCESS = "create_success"
        const val KEY_NAME_CREATED_PLAYLIST = "created_playlist"
    }
}