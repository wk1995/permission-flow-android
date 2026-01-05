package dev.shreyaspatil.permissionFlow.example.ui.composePermission

data class PermissionUiBean(
    val permission: String, val label: String, val description: String?, val granted: Boolean
)