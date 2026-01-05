package dev.shreyaspatil.permissionFlow.example.ui.composePermission

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun PermissionListScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val permissionList by viewModel.permissionList.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.initAllPermissionsWithState(context)
    }
    PermissionListScreen_(permissionList)
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