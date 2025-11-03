package com.saif.iglite.ui.post

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.saif.iglite.R
import com.saif.iglite.data.PostEntity
import com.saif.iglite.databinding.DialogAddEditPostBinding

class AddEditPostDialog : DialogFragment() {

    private var _b: DialogAddEditPostBinding? = null
    private val b get() = _b!!
    private var imageUri: String? = null
    var editing: PostEntity? = null

    private val pickImage = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri == null) return@registerForActivityResult
        try {
            requireContext().contentResolver.takePersistableUriPermission(
                uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        } catch (_: SecurityException) {}

        imageUri = uri.toString()
        b.cardPreview.visibility = View.VISIBLE
        Glide.with(b.ivPreview)
            .load(uri)
            .error(R.drawable.ic_image_24)
            .into(b.ivPreview)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.App_Dialog_Transparent)

        @Suppress("DEPRECATION")
        editing = arguments?.getParcelable(ARG_POST)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        s: Bundle?
    ): View {
        _b = DialogAddEditPostBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val dm = resources.displayMetrics
            val screenW = dm.widthPixels
            val margin = (24 * dm.density).toInt()
            val maxW = (360 * dm.density).toInt()
            val targetW = minOf(screenW - margin * 2, maxW)
            setLayout(targetW, LayoutParams.WRAP_CONTENT)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dialog?.setTitle(if (editing == null) "Tambah Post Baru" else "Edit Post")
        b.btnPick.setOnClickListener { pickImage.launch(arrayOf("image/*")) }
        b.btnCancel?.setOnClickListener { dismiss() }
        b.btnSave.setOnClickListener {
            val user = b.etUser.text?.toString()?.trim().orEmpty()
            val cap  = b.etCaption.text?.toString()?.trim().orEmpty()
            val img  = imageUri ?: editing?.imageUri

            if (user.isEmpty() || cap.isEmpty() || img.isNullOrEmpty()) {
                Snackbar.make(b.root, "Isi semua kolom dulu", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val result = editing?.copy(
                username = user,
                caption  = cap,
                imageUri = img
            ) ?: PostEntity(
                username = user,
                caption  = cap,
                imageUri = img
            )

            parentFragmentManager.setFragmentResult(RESULT_KEY, bundleOf("post" to result))
            dismiss()
        }


        editing?.let {
            b.etUser.setText(it.username)
            b.etCaption.setText(it.caption)
            imageUri = it.imageUri
            b.ivPreview.visibility = View.VISIBLE
            Glide.with(b.ivPreview)
                .load(it.imageUri)
                .error(R.drawable.ic_image_24)
                .into(b.ivPreview)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }

    companion object {
        private const val ARG_POST = "arg_post"
        const val RESULT_KEY = "result_add_edit"

        fun newInstance(edit: PostEntity? = null) = AddEditPostDialog().apply {
            arguments = bundleOf(ARG_POST to edit)
        }
    }
}
