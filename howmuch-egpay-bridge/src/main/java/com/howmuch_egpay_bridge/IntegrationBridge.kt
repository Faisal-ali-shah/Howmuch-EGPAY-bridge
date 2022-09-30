package com.howmuch_egpay_bridge

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class IntegrationBridge(private val context: Context) {

    fun openHowmuchPOS(email: String, secretKey: String, actionKey: String) {
        openApp(md5(email + secretKey), "com.arkhitech.howmuchshopadminapp",actionKey)
    }

    fun openHowmuchNewOrder(email: String, secretKey: String, actionKey: String) {
        openApp(md5(email + secretKey), "com.arkhitech.howmuchshopadminapp",actionKey)
    }

    fun openFoodnerdPOS(email: String, secretKey: String, actionKey: String) {
        openApp(md5(email + secretKey), "menumonkey.orderdock",actionKey)
    }

    fun openFoodnerdNewOrder(email: String, secretKey: String, actionKey: String) {
        openApp(md5(email + secretKey), "menumonkey.orderdock",actionKey)
    }

    fun openApp(encryptedEmail: String, launchAppID: String, actionKey: String) {
        try {
            val i: Intent? = context.packageManager.getLaunchIntentForPackage(launchAppID)
            i?.putExtra("encrypted_email", encryptedEmail)
            i?.putExtra("action_key", actionKey)
            context.startActivity(i)
        } catch (e: PackageManager.NameNotFoundException) {
            // TODO Auto-generated catch block
        }
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

