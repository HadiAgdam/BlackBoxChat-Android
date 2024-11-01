package ir.hadiagdamapps.blackboxchat.data.qr

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder


class QrCodeGenerator {

    private val writer = MultiFormatWriter()
    private val encoder = BarcodeEncoder()


    fun generateCode(text: String): Bitmap {
        val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 720, 720)

        return encoder.createBitmap(bitMatrix)
    }

    companion object {
        const val WIDTH = 720
        const val HEIGHT = 720
    }
}