package com.calberto_barbosa_jr.interactus.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.calberto_barbosa_jr.interactus.R
import androidx.activity.enableEdgeToEdge
import com.calberto_barbosa_jr.interactus.databinding.ActivitySettingsBinding
import android.Manifest
import android.os.Build
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import coil.load
import com.calberto_barbosa_jr.interactus.helper.PermissionManager
import java.io.File
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

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
        Manifest.permission.READ_MEDIA_IMAGES
    )

    private fun getRequiredPermissions(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            storagePermissions33
        } else {
            storagePermissions
        }
    }
    //===============================================================================

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