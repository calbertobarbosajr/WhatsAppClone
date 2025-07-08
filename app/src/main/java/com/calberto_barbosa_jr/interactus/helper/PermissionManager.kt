package com.calberto_barbosa_jr.interactus.helper

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PermissionManager(private val activity: Activity) : PermissionHandler {

    companion object {
        const val REQUEST_PERMISSIONS_CODE = 100
    }

    private var permissionRequestCallback: ((Boolean) -> Unit)? = null

    // Inicializa o launcher de permissões corretamente
    private val requestPermissionLauncher =
        (activity as? AppCompatActivity)?.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { results ->
            val allGranted = results.all { it.value }
            permissionRequestCallback?.invoke(allGranted)
        } ?: throw IllegalStateException("Activity must be an instance of AppCompatActivity")

    override fun requestPermissions(permissions: Array<String>, callback: (Boolean) -> Unit) {
        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isEmpty()) {
            callback(true)
            return
        }

        permissionRequestCallback = callback
        requestPermissionLauncher.launch(permissionsToRequest.toTypedArray())
    }

    override fun arePermissionsGranted(permissions: Array<String>): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}