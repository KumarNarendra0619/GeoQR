package com.geoqr.app.create

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.content.Intent
import java.io.File
import java.io.FileOutputStream

object GeoQrShare {
    fun share(context: Context, bitmap: Bitmap) {
        val file = File(context.cacheDir, "geoqr.png")
        FileOutputStream(file).use { bitmap.compress(CompressFormat.PNG, 100, it) }
        val uri = androidx.core.content.FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Share GeoQR"))
    }
}
