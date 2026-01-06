package dev.shreyaspatil.permissionFlow.example.ui.composePermission

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PermissionInfo
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import dev.shreyaspatil.permissionFlow.PermissionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {
    companion object{
        private const val TAG = "MainViewModel"
    }
    private val _permissionList = MutableStateFlow(emptyList<String>())
    val permissionList = _permissionList.asStateFlow()

    private val _permissionBeanList = MutableStateFlow(emptyList<PermissionUiBean>())
    val permissionBean = _permissionBeanList.asStateFlow()


    fun getPermissionBean(context:Context,permissionStates:List<PermissionState>):List<PermissionUiBean>{
        Log.d(
            TAG,
            "sdk=${Build.VERSION.SDK_INT}, target=${context.applicationInfo.targetSdkVersion}"
        )
        val pm = context.packageManager
        return permissionStates.map {
            val systemGranted = it.isGranted
            Log.d(
                TAG,
                "permission： ${it.permission} Granted ： ${it.isGranted} isRationaleRequired:  ${it.isRationaleRequired}"
            )
            val permissionInfo = getPermissionInfo(context, it.permission)

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
                permission = it.permission,
                label = permissionInfo?.loadLabel(pm)?.toString() ?: "unKnow",
                description = permissionInfo?.loadDescription(pm)?.toString(),
                granted = systemGranted,
            )
        }
    }


    /**
     * 应用“声明支持”的全部权限,不关心是否已授权
     * */
    fun initDeclaredPermissions(context: Context) {
        val pm = context.packageManager
        val pkgInfo = pm.getPackageInfo(
            context.packageName, PackageManager.GET_PERMISSIONS
        )
        _permissionList.value = pkgInfo.requestedPermissions?.toList() ?: emptyList()
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
        initDeclaredPermissions(context)
        val result = permissionList.value.map { perm ->
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
        _permissionBeanList.value = result
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