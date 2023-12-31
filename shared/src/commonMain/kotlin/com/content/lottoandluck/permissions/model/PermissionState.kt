package com.content.lottoandluck.permissions.model


/**
 * Represents the state of a permission
 */
enum class PermissionState {
    /**
     * Indicates that the permission has not been requested yet
     */
    NOT_DETERMINED,

    /**
     * Indicates that the permission has been requested and accepted.
     */
    GRANTED,

    /**
     * Indicates that the permission has been requested but the user denied the permission
     */
    DENIED;
}

/**
 * Extension function to check if the permission is granted
 */
fun PermissionState.granted(): Boolean {
    return this == PermissionState.GRANTED
}

/**
 * Extension function to check if the permission is not granted
 */
fun PermissionState.notGranted(): Boolean {
    return this != PermissionState.GRANTED
}
