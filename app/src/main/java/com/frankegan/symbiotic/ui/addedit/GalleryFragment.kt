package com.frankegan.symbiotic.ui.addedit

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.frankegan.symbiotic.R
import com.frankegan.symbiotic.data.Image
import com.frankegan.symbiotic.di.VMInjectionFactory
import com.frankegan.symbiotic.di.injector
import com.frankegan.symbiotic.photoOutputUri
import com.frankegan.symbiotic.textInputDialog
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.gallery_fragment.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

/**
 * A simple [Fragment] subclass for displaying image and captions of fermentations.
 *
 */
class GalleryFragment : Fragment(), Step {
    @Inject
    lateinit var adapter: GalleryAdapter

    @Inject
    lateinit var factory: VMInjectionFactory<AddEditViewModel>

    private val viewModel by activityViewModels<AddEditViewModel>(
        factoryProducer = { factory }
    )

    /**
     * Keeps the latest photo path in memory and retrieves it from savedInstanceState.
     */
    private var fileURI: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.inject(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_PATH, fileURI.toString())
        outState.putParcelableArrayList(KEY_IMAGES, ArrayList(adapter.items))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        savedInstanceState?.getParcelableArrayList<Image>(KEY_IMAGES)?.forEach {
            viewModel.addImage(it.fileUri.toUri(), it.caption)
        }
        fileURI = savedInstanceState?.getString(KEY_PATH)?.toUri()
        return inflater.inflate(R.layout.gallery_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gallery_list_view.adapter = this.adapter

        this.adapter.onItemClick = {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                val input = textInputDialog()
                if (input.isEmpty()) return@launchWhenStarted
                viewModel.addCaption(it.fileUri, input)
            }
        }

        photo_button.setOnClickListener { takePhoto() }

        viewModel.imageData.observe(this) {
            adapter.updateItems(it)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            viewModel.addImage(fileURI)
            Log.d("Gallery", fileURI.toString())
        }
    }

    /**
     * This method will ask for the necessary permissions to save a photo.
     */
    @AfterPermissionGranted(REQ_WRITE_PERM)
    private fun takePhoto() {
        if (needsFileWritePermission()) {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.write_permission_rationale),
                REQ_WRITE_PERM,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            return
        }
        this.fileURI = photoOutputUri() ?: return
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            .putExtra(MediaStore.EXTRA_OUTPUT, this.fileURI)
        startActivityForResult(cameraIntent, REQ_TAKE_PHOTO)
    }

    override fun onSelected() = Unit

    override fun verifyStep(): VerificationError? = null

    override fun onError(error: VerificationError) = Unit

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private fun needsFileWritePermission(): Boolean {
        return Build.VERSION.SDK_INT in Build.VERSION_CODES.M..Build.VERSION_CODES.P
                && !EasyPermissions.hasPermissions(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    companion object {
        @JvmStatic
        fun newInstance(): GalleryFragment = GalleryFragment()


        /**
         * Request code for taking a photo.
         */
        private const val REQ_TAKE_PHOTO = 100
        private const val REQ_WRITE_PERM = 101

        /**
         * Keys for retrieving the new photo file path from savedState.
         */
        private const val KEY_PATH = "PATH"
        /**
         * Keys for retrieving image data from savedState.
         */
        private const val KEY_IMAGES = "IMAGES"
    }
}

