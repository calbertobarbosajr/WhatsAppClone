package com.calberto_barbosa_jr.interactus.helper

interface PermissionHandler {
    fun requestPermissions(permissions: Array<String>, callback: (Boolean) -> Unit)
    fun arePermissionsGranted(permissions: Array<String>): Boolean
}