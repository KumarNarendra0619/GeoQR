package com.geoqr.app.security

/**
 * Security boundary for the Android MVP.
 * Production encryption/key management is intentionally isolated here for the next milestone.
 */
object SecurityBoundary {
    const val PAYLOAD_VERSION = 1

    fun validateQrId(qrId: String): Boolean =
        qrId.length in 8..64 && qrId.all { it.isLetterOrDigit() || it == '-' || it == '_' }
}
