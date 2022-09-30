package com.howmuch_egpay_bridge

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import java.nio.charset.StandardCharsets
import java.security.DigestException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


class IntegrationBridge(private val context: Context) {

    val _DashbordScreen = "Dashboard"
    val _NewOrderScreen = "NewOrder"
    fun openHowmuchPOS(email: String, secretKey: String) {
        openApp(encryptToAES(email, secretKey), "com.arkhitech.howmuchshopadminapp", "Dashboard")
    }

    fun openHowmuchNewOrder(email: String, secretKey: String, actionKey: String) {
        openApp(encryptToAES(email, secretKey), "com.arkhitech.howmuchshopadminapp", "Dashboard")
    }

    fun openFoodnerdPOS(email: String, secretKey: String, actionKey: String) {
        openApp(encryptToAES(email, secretKey), "menumonkey.orderdock", actionKey)
    }

    fun openFoodnerdNewOrder(email: String, secretKey: String, actionKey: String) {
        openApp(encryptToAES(email, secretKey), "menumonkey.orderdock", actionKey)
    }

    fun openApp(encryptedEmail: String, launchAppID: String, actionKey: String) {
        try {
            val i: Intent? = context.packageManager.getLaunchIntentForPackage(launchAppID)
            i?.putExtra("encrypted_email", encryptedEmail)
            i?.putExtra("screen", actionKey)
            context.startActivity(i)
        } catch (e: PackageManager.NameNotFoundException) {
            // TODO Auto-generated catch block
        }
    }
}


fun encryptToAES(cipherText: String, secret: String): String {
    val cipherData = Base64.getDecoder().decode(cipherText)
    val saltData = Arrays.copyOfRange(cipherData, 8, 16)

    val md5 = MessageDigest.getInstance("MD5")
    val keyAndIV =
        GenerateKeyAndIV(32, 16, 1, saltData, secret.toByteArray(), md5)
    val key = SecretKeySpec(keyAndIV!![0], "AES")
    val iv = IvParameterSpec(keyAndIV!![1])

    val encrypted = Arrays.copyOfRange(cipherData, 16, cipherData.size)
    val aesCBC = Cipher.getInstance("AES/CBC/PKCS5Padding")
    aesCBC.init(Cipher.DECRYPT_MODE, key, iv)

    return encrypted.toString()
}


fun GenerateKeyAndIV(
    keyLength: Int,
    ivLength: Int,
    iterations: Int,
    salt: ByteArray?,
    password: ByteArray?,
    md: MessageDigest
): Array<ByteArray?>? {
    val digestLength = md.digestLength
    val requiredLength = (keyLength + ivLength + digestLength - 1) / digestLength * digestLength
    val generatedData = ByteArray(requiredLength)
    var generatedLength = 0
    return try {
        md.reset()

        // Repeat process until sufficient data has been generated
        while (generatedLength < keyLength + ivLength) {

            // Digest data (last digest if available, password data, salt if available)
            if (generatedLength > 0) md.update(
                generatedData,
                generatedLength - digestLength,
                digestLength
            )
            md.update(password)
            if (salt != null) md.update(salt, 0, 8)
            md.digest(generatedData, generatedLength, digestLength)

            // additional rounds
            for (i in 1 until iterations) {
                md.update(generatedData, generatedLength, digestLength)
                md.digest(generatedData, generatedLength, digestLength)
            }
            generatedLength += digestLength
        }

        // Copy key and IV into separate byte arrays
        val result = arrayOfNulls<ByteArray>(2)
        result[0] = Arrays.copyOfRange(generatedData, 0, keyLength)
        if (ivLength > 0) result[1] =
            Arrays.copyOfRange(generatedData, keyLength, keyLength + ivLength)
        result
    } catch (e: DigestException) {
        throw RuntimeException(e)
    } finally {
        // Clean out temporary data
        Arrays.fill(generatedData, 0.toByte())
    }
}

fun md5(s: String): String {
    try {
        // Create MD5 Hash
        val digest: MessageDigest = MessageDigest.getInstance("MD5")
        digest.update(s.toByteArray())
        val messageDigest: ByteArray = digest.digest()

        // Create Hex String
        val hexString = StringBuffer()
        for (i in messageDigest.indices) hexString.append(
            Integer.toHexString(
                0xFF and messageDigest[i]
                    .toInt()
            )
        )
        return hexString.toString()
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    }
    return ""
}

