package com.geoqr.app.qr

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix

object GeoQrGenerator {
    fun generate(payload: String, size: Int = 768): Bitmap {
        require(payload.isNotBlank()) { "payload must not be blank" }
        require(size in 256..2048) { "size must be between 256 and 2048" }
        val matrix: BitMatrix = MultiFormatWriter().encode(payload, BarcodeFormat.QR_CODE, size, size)
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        for (x in 0 until size) {
            for (y in 0 until size) {
                bitmap.setPixel(x, y, if (matrix[x, y]) 0xFF000000.toInt() else 0xFFFFFFFF.toInt())
            }
        }
        return bitmap
    }
}
