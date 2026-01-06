package dev.shreyaspatil.permissionFlow.example.ui.composePermission

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import dev.shreyaspatil.permissionFlow.PermissionGrantType
import dev.shreyaspatil.permissionflow.compose.rememberMultiplePermissionState
import dev.shreyaspatil.permissionflow.compose.rememberPermissionFlowRequestLauncher

@Composable
fun PermissionListScreen(viewModel: MainViewModel, goSettings: (String) -> Unit = {}) {

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
            val state = viewModel.getPermissionState(permission) ?: return@PermissionListScreen_
            if (g) {
                when (state.permissionGrantType) {
                    PermissionGrantType.NOT_REQUESTED -> {
                        Log.i("wkkk", "第一次去申请权限")
                        permissionLauncher.launch(arrayOf(permission))
                    }

                    PermissionGrantType.GRANTED -> {
                        Log.i("wkkk", "已获取到权限")
                    }

                    PermissionGrantType.DENIED -> {
                        Log.i("wkkk", "第二次去申请权限")
                        permissionLauncher.launch(arrayOf(permission))
                    }

                    PermissionGrantType.DENIED_PERMANENTLY -> {
                        Log.i("wkkk", "以及永久拒绝了，再申请也没用")
                        permissionLauncher.launch(arrayOf(permission))
                    }
                }
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