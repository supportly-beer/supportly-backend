package beer.supportly.backend.utils

import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.qrcode.QRCodeWriter
import org.apache.tomcat.util.codec.binary.Base64
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

/**
 * Utility class for generating QR codes.
 */
class QrCodeUtils {

    companion object {

        /**
         * Generates a QR code for the given email and secret.
         *
         * @param email The email.
         * @param secret The secret.
         *
         * @return The base64 encoded QR code.
         */
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