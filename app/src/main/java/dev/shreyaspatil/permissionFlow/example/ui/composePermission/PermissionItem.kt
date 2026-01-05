package dev.shreyaspatil.permissionFlow.example.ui.composePermission

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PermissionItem(
    permission: PermissionUiBean, changePermission: (String, Boolean) -> Unit = { _, _ -> }
) {
    PermissionItem_(permission, changePermission)
}

@Composable
fun PermissionItem_(
    permission: PermissionUiBean, changePermission: (String, Boolean) -> Unit = { _, _ -> }
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = permission.label, style = LocalTextStyle.current.copy(fontSize = 20.sp), maxLines = 1)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "${permission.description}", maxLines = 1)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Switch(checked = permission.granted, onCheckedChange = {
            changePermission(permission.permission, it)
        })
    }
}

@Preview
@Composable
fun PermissionItemPreview() {
    PermissionItem_(
        PermissionUiBean(
            permission = "permission", label = "label", description = "description", granted = true
        )
    )
}