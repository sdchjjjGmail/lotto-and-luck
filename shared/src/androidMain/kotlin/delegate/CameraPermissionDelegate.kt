package delegate

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Build
import com.content.lottoandluck.permissions.delegate.PermissionDelegate
import com.content.lottoandluck.permissions.model.Permission
import com.content.lottoandluck.permissions.model.PermissionState
import com.content.lottoandluck.permissions.util.PermissionRequestException
import util.checkPermissions
import util.openAppSettingsPage
import util.providePermissions

internal class CameraPermissionDelegate(
    private val context: Context,
    private val activity: Lazy<Activity>,
) : PermissionDelegate {
    override fun getPermissionState(): PermissionState {
        return checkPermissions(context, activity, cameraPermission)
    }

    override suspend fun providePermission() {
        activity.value.providePermissions(cameraPermission) {
            throw PermissionRequestException(Permission.CAMERA.name)
        }
    }

    override fun openSettingPage() {
        context.openAppSettingsPage(Permission.CAMERA)
    }
}

private val cameraPermission: List<String> =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        listOf(Manifest.permission.CAMERA)
    } else {
        listOf(Manifest.permission.CAMERA)
    }