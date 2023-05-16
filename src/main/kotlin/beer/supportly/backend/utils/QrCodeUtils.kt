package beer.supportly.backend.utils

import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.qrcode.QRCodeWriter
import org.apache.tomcat.util.codec.binary.Base64
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

class QrCodeUtils {

    companion object {
        fun generateQrCode(email: String, secret: String): String {
            val issuer = "Supportly"
            val uri = "otpauth://totp/$issuer:$email?secret=$secret&issuer=$issuer"
            val bitMatrix = QRCodeWriter().encode(uri, BarcodeFormat.QR_CODE, 200, 200)
            val bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix)

            val byteArrayOutputStream = ByteArrayOutputStream()
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream)

            val imageBytes = byteArrayOutputStream.toByteArray()

            return Base64.encodeBase64String(imageBytes)
        }
    }
}