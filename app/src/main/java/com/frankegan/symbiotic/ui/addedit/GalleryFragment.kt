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
import androidx.lifecycle.lifecycleScope
import com.frankegan.symbiotic.format
import com.frankegan.symbiotic.launchSilent
import com.github.florent37.runtimepermission.kotlin.PermissionException
import com.github.florent37.runtimepermission.kotlin.coroutines.experimental.askPermission
import kotlinx.android.synthetic.main.gallery_fragment.*
import org.threeten.bp.LocalDateTime
import java.io.File
import java.io.IOException

const val REQUEST_TAKE_PHOTO = 1

/**
 * A simple [Fragment] subclass.
 *
 */
class GalleryFragment : Fragment() {
    private lateinit var adapter: GalleryAdapter
    private var latestPath = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.frankegan.symbiotic.R.layout.gallery_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = GalleryAdapter()
        gallery_list_view.adapter = adapter
        photo_button.setOnClickListener {
            askForPhoto()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            Log.d("Gallery", latestPath)
        }
    }

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
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
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
            Log.d("Gallery", absolutePath)
            latestPath = absolutePath
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): GalleryFragment = GalleryFragment()
    }
}
