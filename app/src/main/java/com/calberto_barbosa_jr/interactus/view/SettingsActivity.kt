package com.calberto_barbosa_jr.interactus.view

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.calberto_barbosa_jr.interactus.databinding.ActivitySettingsBinding
import com.calberto_barbosa_jr.interactus.helper.PermissionManager
import android.app.Activity
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import coil.load
import com.bumptech.glide.Glide
import com.calberto_barbosa_jr.interactus.R
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File

class SettingsActivity : AppCompatActivity() {

    //===============================================================================
    private lateinit var permissionManager: PermissionManager

    private val storagePermissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val storagePermissions33 = arrayOf(
        Manifest.permission.READ_MEDIA_VIDEO,
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.CAMERA
    )

    private fun getRequiredPermissions(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            storagePermissions33
        } else {
            storagePermissions
        }
    }
    //===============================================================================

    private lateinit var binding: ActivitySettingsBinding

    private val storage = Firebase.storage
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val currentUser get() = auth.currentUser
    private val selectedImages = mutableListOf<Uri>()
    private val uploadedImageUrls = mutableListOf<String>()
    private var selectedImageView: ImageView? = null
    private var photoUri: Uri? = null

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && photoUri != null) {
            handleSelectedImage(photoUri!!)
        } else {
            showMessage("Erro ao capturar imagem")
        }
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        result.data?.data?.let { handleSelectedImage(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //========================================================
        permissionManager = PermissionManager(this)
        requestAppPermissions()
        //========================================================
        setupInsets()
        setupClickListeners()
        fetchUserData()
        fetchImagesFromFirebase()
    }

    private fun setupInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupClickListeners() {
        binding.profilePicture.setOnClickListener { onImageClicked(binding.profilePicture) }
        binding.buttonSend.setOnClickListener {
            onUpdateButtonClick()
            startUploadImages()
        }
    }

    private fun onImageClicked(imageView: ImageView) {
        selectedImageView = imageView
        showImageSelectionDialog()
    }

    private fun showImageSelectionDialog() {
        AlertDialog.Builder(this)
            .setTitle("Selecionar Imagem")
            .setMessage("Escolha uma opção:")
            .setPositiveButton("Câmera") { _, _ -> openCamera() }
            .setNegativeButton("Galeria") { _, _ -> openGallery() }
            .show()
    }

    private fun openCamera() {
        val file = File(getExternalFilesDir(null), "photo_${System.currentTimeMillis()}.jpg")
        photoUri = FileProvider.getUriForFile(this, "$packageName.fileprovider", file)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        cameraLauncher.launch(intent)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private fun handleSelectedImage(uri: Uri) {
        selectedImageView?.load(uri)
        selectedImages.clear()
        selectedImages.add(uri)
    }

    private fun startUploadImages() {
        if (selectedImages.isEmpty()) {
            showMessage("Nenhuma imagem selecionada.")
            return
        }

        selectedImages.forEachIndexed { index, uri ->
            uploadImageToStorage(uri, selectedImages.size, index)
        }
        onUpdateButtonClick()
    }

    private fun uploadImageToStorage(uri: Uri, totalImages: Int, index: Int) {
        val userId = currentUser?.uid ?: run {
            showMessage("Usuário não autenticado")
            return
        }

        val imageRef = storage.reference.child("images/profile/$userId/image_$index")

        imageRef.putFile(uri)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    val imageUrl = downloadUri.toString()
                    uploadedImageUrls.add(imageUrl)

                    saveImageUrlToFirestore(userId, imageUrl, index)

                    if (index == 0) {
                        binding.profilePicture.load(imageUrl)
                    }

                    if (uploadedImageUrls.size == totalImages) {
                        onUploadComplete()
                    }
                }
            }
            .addOnFailureListener { e ->
                showMessage("Falha ao fazer upload: ${e.message}")
            }
    }

    private fun saveImageUrlToFirestore(userId: String, imageUrl: String, index: Int) {
        val imageData = mapOf(
            "imageUrl" to imageUrl,
            "timestamp" to System.currentTimeMillis()
        )

        firestore.collection("users")
            .document(userId)
            .collection("images")
            .document("image_$index")
            .set(imageData)
            .addOnSuccessListener {
                showMessage("Imagem salva com sucesso")
            }
            .addOnFailureListener {
                showMessage("Erro ao salvar imagem")
            }
    }

    private fun onUploadComplete() {
        showMessage("Upload concluído com sucesso")
    }

    private fun fetchUserData() {
        val uid = currentUser?.uid ?: return showMessage("Usuário não autenticado")

        firestore.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val name = document.getString("name").orEmpty()
                    binding.editPerfilNome.setText(name)
                } else {
                    showMessage("Perfil do usuário não encontrado.")
                }
            }
            .addOnFailureListener {
                showMessage("Erro ao obter dados: ${it.message}")
            }
    }

    fun onUpdateButtonClick() {
        val updates = getUpdatedFields()

        if (updates.isEmpty()) {
            showMessage("No fields have been modified.")
            return
        }

        updateUserInFirestore(updates)
    }

    private fun getUpdatedFields(): Map<String, Any> = buildMap {
        binding.editPerfilNome.text.toString().takeIf { it.isNotEmpty() }?.let { put("name", it) }
    }

    private fun updateUserInFirestore(updates: Map<String, Any>) {
        val uid = currentUser?.uid ?: return showMessage("Usuário não autenticado")

        firestore.collection("users")
            .document(uid)
            .update(updates)
            .addOnSuccessListener {
                showMessage("Atualização realizada com sucesso!")
            }
            .addOnFailureListener {
                showMessage("Erro ao atualizar: ${it.message}")
            }
    }

    private fun showMessage(message: String) {
        binding.profileMessageTextView.text = message
    }


    private fun fetchImagesFromFirebase() {
        val userId = currentUser?.uid ?: return showMessage("Usuário não autenticado")

        // Corrigido caminho para bater com upload
        val userImagesRef = storage.reference.child("images/profile/$userId")

        userImagesRef.listAll()
            .addOnSuccessListener { result ->
                val imageItems = result.items.take(1)
                val downloadTasks = imageItems.map { it.downloadUrl }

                Tasks.whenAllComplete(downloadTasks)
                    .addOnSuccessListener {
                        val imageUrls = downloadTasks.mapNotNull { task ->
                            if (task.isSuccessful) (task.result as? Uri)?.toString() else null
                        }
                        displayImages(imageUrls)
                    }
                    .addOnFailureListener {
                        showMessage("Erro ao carregar URLs: ${it.message}")
                    }
            }
            .addOnFailureListener {
                showMessage("Erro ao listar imagens: ${it.message}")
            }
    }

    /*
    private fun displayImages(imageUrls: List<String>) {
        if (imageUrls.isNotEmpty()) {
            binding.profilePicture.load(imageUrls.first()) {
                crossfade(true)
                placeholder(R.drawable.choose_image_48)
                error(R.drawable.error_placeholder)
            }
        }
    }
     */

    private fun displayImages(imageUrls: List<String>) {
        if (imageUrls.isNotEmpty()) {
            val imageUrl = imageUrls.first()
            Log.d("DEBUG", "Carregando imagem com Glide: $imageUrl")

            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.choose_image_48)
                .error(R.drawable.error_placeholder)
                .into(binding.profilePicture)
        }
    }




    //===============================================================================
    private fun requestAppPermissions() {
        val requiredPermissions = getRequiredPermissions()

        if (permissionManager.arePermissionsGranted(requiredPermissions)) {
            return // Todas as permissões já foram concedidas
        }

        permissionManager.requestPermissions(requiredPermissions) { granted ->
            if (!granted) {
                showPermissionDeniedAlert()
            }
        }
    }

    private fun showPermissionDeniedAlert() {
        AlertDialog.Builder(this)
            .setTitle("Permissões Negadas")
            .setMessage("Para utilizar o app, você precisa aceitar as permissões necessárias.")
            .setCancelable(false)
            .setPositiveButton("OK") { _, _ -> finish() }
            .create()
            .show()
    }
}