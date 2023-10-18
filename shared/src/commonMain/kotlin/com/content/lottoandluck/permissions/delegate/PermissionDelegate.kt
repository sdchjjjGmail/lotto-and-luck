package com.content.lottoandluck.permissions.delegate

import com.content.lottoandluck.permissions.model.PermissionState

internal interface PermissionDelegate {
    fun getPermissionState(): PermissionState
    suspend fun providePermission()
    fun openSettingPage()
}
