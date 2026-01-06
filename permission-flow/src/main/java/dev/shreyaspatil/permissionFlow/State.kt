/**
 * Copyright 2022 Shreyas Patil
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.shreyaspatil.permissionFlow

/**
 * State model of a permission
 *
 * @property permission Name of a permission
 * @property isGranted State of a permission whether it's granted or not
 * @property isRationaleRequired Whether to show rationale for a permission or not.
 */
data class PermissionState(
    val permission: String,
    @Deprecated("") val isGranted: Boolean,
    @Deprecated("") val isRationaleRequired: Boolean?,
    val permissionGrantType: PermissionGrantType = PermissionGrantType.NOT_REQUESTED
)

enum class PermissionGrantType {
    /**
     * 有权限
     * */
    GRANTED,

    /**
     * 没申请过权限
     * */
    NOT_REQUESTED,

    /**
     * 没权限
     * */
    DENIED,

    /**
     * 永久拒绝权限
     * */
    DENIED_PERMANENTLY
}

/**
 * State model for multiple permissions
 *
 * @property permissions List of state of multiple permissions
 */
data class MultiplePermissionState(val permissions: List<PermissionState>) {
    /** Returns true if all permissions are granted by user */
    val allGranted by lazy { permissions.all { it.isGranted } }

    /** List of permissions which are granted by user */
    val grantedPermissions by lazy { permissions.filter { it.isGranted }.map { it.permission } }

    /** List of permissions which are denied / not granted by user */
    val deniedPermissions by lazy { permissions.filter { !it.isGranted }.map { it.permission } }

    /** List of permissions which are required to show rationale */
    val permissionsRequiringRationale by lazy {
        permissions.filter { it.isRationaleRequired == true }.map { it.permission }
    }
}
