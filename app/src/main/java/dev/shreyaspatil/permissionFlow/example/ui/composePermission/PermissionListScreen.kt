package dev.shreyaspatil.permissionFlow.example.ui.composePermission

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.shreyaspatil.permissionflow.compose.rememberMultiplePermissionState
import dev.shreyaspatil.permissionflow.compose.rememberPermissionFlowRequestLauncher

@Composable
fun PermissionListScreen(viewModel: MainViewModel) {

    val permissions = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_CALL_LOG,
        android.Manifest.permission.READ_CONTACTS,
        android.Manifest.permission.READ_PHONE_STATE,
    )
    val permissionLauncher = rememberPermissionFlowRequestLauncher()
    val context = LocalContext.current
//    val permissionList by viewModel.permissionList.collectAsStateWithLifecycle()
    val state by rememberMultiplePermissionState(
        *permissions
    )
//    LaunchedEffect(Unit) {
//        viewModel.initDeclaredPermissions(context)
//    }

    PermissionListScreen_(
        viewModel.getPermissionBean(context, state.permissions),
        changePermission = { permission, g ->
            if (g) {
                permissionLauncher.launch(arrayOf(permission))
            }

        })
}


@Composable
fun PermissionListScreen_(
    permission: List<PermissionUiBean> = emptyList(),
    changePermission: (String, Boolean) -> Unit = { _, _ -> }
) {
    LazyColumn() {
        items(permission.size) {
            PermissionItem(permission[it], changePermission = changePermission)
        }
    }
}


@Preview
@Composable
fun PermissionListScreenPreview() {
    PermissionListScreen_()
}