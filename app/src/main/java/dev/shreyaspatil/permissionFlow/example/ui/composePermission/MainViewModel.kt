package dev.shreyaspatil.permissionFlow.example.ui.composePermission

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PermissionInfo
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {

    private val _permissionList = MutableStateFlow(emptyList<PermissionUiBean>())
    val permissionList = _permissionList.asStateFlow()

    /**
     * 应用“声明支持”的全部权限,不关心是否已授权
     * */
    private fun getDeclaredPermissions(context: Context): List<String> {
        val pm = context.packageManager
        val pkgInfo = pm.getPackageInfo(
            context.packageName, PackageManager.GET_PERMISSIONS
        )
        return pkgInfo.requestedPermissions?.toList() ?: emptyList()
    }

    private fun getPermissionGrantState(
        context: Context, permission: String
    ): Boolean {
        return ContextCompat.checkSelfPermission(
            context, permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun initAllPermissionsWithState(context: Context) {
        val pm = context.packageManager
        val result = getDeclaredPermissions(context).map { perm ->
            val granted = getPermissionGrantState(context, perm)
            val permissionInfo = getPermissionInfo(context, perm)

            val protection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                permissionInfo?.protection
            } else {

            }
            val protectionLevel = permissionInfo?.protectionLevel
            val getProtectionFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                permissionInfo?.protectionFlags
            } else {

            }
            PermissionUiBean(
                permission = perm,
                label = permissionInfo?.loadLabel(pm)?.toString() ?: "unKnow",
                description = permissionInfo?.loadDescription(pm)?.toString(),
                granted = granted,
            )
        }
        _permissionList.value = result
    }

    /**
     * 常见类型：
     * 	•	PROTECTION_NORMAL：安装即授权（不弹窗）
     * 	•	PROTECTION_DANGEROUS：运行时权限（会弹）
     * 	•	PROTECTION_SIGNATURE / PRIVILEGED：系统/签名权限
     * */
    private fun getPermissionInfo(
        context: Context, permission: String
    ): PermissionInfo? {
        return try {
            context.packageManager.getPermissionInfo(permission, 0)
        } catch (e: Exception) {
            null
        }
    }
}