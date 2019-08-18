package com.frankegan.symbiotic.ui.addedit


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.frankegan.symbiotic.R
import com.frankegan.symbiotic.data.Image
import com.frankegan.symbiotic.di.injector
import com.frankegan.symbiotic.format
import com.frankegan.symbiotic.launchSilent
import com.frankegan.symbiotic.textInputDialog
import com.github.florent37.runtimepermission.kotlin.PermissionException
import com.github.florent37.runtimepermission.kotlin.coroutines.experimental.askPermission
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.gallery_fragment.*
import org.threeten.bp.LocalDateTime
import java.io.File
import java.io.IOException


/**
 * Request code for taking a photo.
 */
private const val REQ_TAKE_PHOTO = 1

/**
 * Keys for retrieving the new photo file path from savedState.
 */
private const val KEY_PATH = "PATH"
/**
 * Keys for retrieving image data from savedState.
 */
private const val KEY_IMAGES = "IMAGES"

/**
 * A simple [Fragment] subclass for displaying image and captions of fermentations.
 *
 */
class GalleryFragment : Fragment(), Step {
    /**
     * We need to inject a ViewModelFactory to build our ViewModels with custom parameters.
     */
    private val factory by lazy { injector.addEditViewModelFactory() }
    private val viewModel by viewModels<AddEditViewModel>(
        ownerProducer = ::requireActivity,
        factoryProducer = { factory }
    )

    /**
     * Keeps the latest photo path in memory and retrieves it from savedInstanceState.
     */
    private var latestPath = ""

    /**
     * RecyclerView adapter.
     */
    private lateinit var adapter: GalleryAdapter

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_PATH, latestPath)
        outState.putParcelableArrayList(KEY_IMAGES, ArrayList(adapter.items))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        savedInstanceState?.getParcelableArrayList<Image>(KEY_IMAGES)?.forEach {
            viewModel.addImage(it.filename, it.caption)
        }
        latestPath = savedInstanceState?.getString(KEY_PATH) ?: ""
        return inflater.inflate(R.layout.gallery_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gallery_list_view.adapter = GalleryAdapter {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                val input = textInputDialog()
                if (input.isEmpty()) return@launchWhenStarted
                viewModel.addCaption(it.filename, input)
            }
        }.apply { adapter = this }

        photo_button.setOnClickListener {
            askForPhoto()
        }

        viewModel.imageData.observe(this) {
            adapter.updateItems(it)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            viewModel.addImage(latestPath)
            Log.d("Gallery", latestPath)
        }
    }

    /**
     * This method will ask for the necessary permissions to save a photo.
     */
    private fun askForPhoto() = lifecycleScope.launchSilent {
        try {
            askPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            //all permissions already granted or just granted your action
            dispatchTakePictureIntent()
        } catch (e: PermissionException) {
            if (e.hasDenied()) {
                //but you can ask them again, eg:
                AlertDialog.Builder(requireContext())
                    .setMessage("Please accept our permissions")
                    .setPositiveButton("yes") { _, _ ->
                        e.permissionResult.askAgain()
                    }
                    .setNegativeButton("no") { d, _ ->
                        d.dismiss()
                    }.show()
            }
        }
    }

    private fun dispatchTakePictureIntent() {
        @Throws(IOException::class)
        fun createImageFile(): File {
            // Create an image file name
            val timeStamp = LocalDateTime.now().format("yyyyMMdd_HHmmss")
            val storageDir = Environment
                .getExternalStoragePublicDirectory("${Environment.DIRECTORY_PICTURES}/Symbiotic").apply {
                    mkdirs()
                }
            return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
            ).apply {
                // Save a file: path for use with ACTION_VIEW intents
                latestPath = absolutePath
            }
        }

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    Log.e("Gallery", ex.message, ex)
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.example.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQ_TAKE_PHOTO)
                }
            }
        }
    }

    override fun onSelected() = Unit

    override fun verifyStep(): VerificationError? = null

    override fun onError(error: VerificationError) = Unit

    companion object {
        @JvmStatic
        fun newInstance(): GalleryFragment = GalleryFragment()
    }
}

