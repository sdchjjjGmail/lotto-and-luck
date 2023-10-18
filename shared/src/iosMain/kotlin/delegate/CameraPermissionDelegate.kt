package delegate

import com.content.lottoandluck.permissions.delegate.PermissionDelegate
import com.content.lottoandluck.permissions.model.Permission
import com.content.lottoandluck.permissions.model.PermissionState
import com.content.lottoandluck.permissions.util.PermissionRequestException
import mainContinuation
import platform.AVFoundation.AVAuthorizationStatus
import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVAuthorizationStatusDenied
import platform.AVFoundation.AVAuthorizationStatusNotDetermined
import platform.AVFoundation.AVAuthorizationStatusRestricted
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaType
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.requestAccessForMediaType
import util.openAppSettingsPage
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal class CameraPermissionDelegate(
    private val type: AVMediaType,
    private val permission: Permission
) : PermissionDelegate {
    override fun getPermissionState(): PermissionState {
        return when (currentAuthorizationStatus()) {
            AVAuthorizationStatusNotDetermined -> PermissionState.NOT_DETERMINED
            AVAuthorizationStatusAuthorized, AVAuthorizationStatusRestricted -> PermissionState.GRANTED
            AVAuthorizationStatusDenied -> PermissionState.DENIED
            else -> PermissionState.NOT_DETERMINED
        }
    }

    private fun currentAuthorizationStatus(): AVAuthorizationStatus {
        return AVCaptureDevice.authorizationStatusForMediaType(type)
    }

    override suspend fun providePermission() {
        when (val status: AVAuthorizationStatus = currentAuthorizationStatus()) {
            AVAuthorizationStatusAuthorized -> return
            AVAuthorizationStatusNotDetermined -> {
                val isGranted: Boolean = suspendCoroutine { continuation ->
                    AVCaptureDevice.requestAccess(type) { continuation.resume(it) }
                }
                if (isGranted) return
                else throw PermissionRequestException(permission.name)
            }

            AVAuthorizationStatusDenied -> throw PermissionRequestException(permission.name)
            else -> error("unknown authorization status $status")
        }
    }

    private fun AVCaptureDevice.Companion.requestAccess(
        type: AVMediaType,
        callback: (isGranted: Boolean) -> Unit
    ) {
        this.requestAccessForMediaType(type, mainContinuation { isGranted: Boolean ->
            callback(isGranted)
        })
    }

    override fun openSettingPage() {
        openAppSettingsPage()
    }
}
