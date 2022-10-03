package com.howmuch_egpay_bridge

import android.content.Context
import android.content.pm.PackageManager
import java.math.BigInteger
import java.security.MessageDigest


object IntegrationBridge {

    val _DashbordScreen = "Dashboard"
    val _NewOrderScreen = "NewOrder"
    fun openHowmuchPOS(context: Context, email: String, secretKey: String) {
        openApp(
            context,
            email,
            (email+secretKey).md5(),
            "com.arkhitech.howmuchshopadminapp",
            _DashbordScreen,
        )




    }

    fun openHowmuchNewOrder(context: Context, email: String, secretKey: String) {
        openApp(
            context,
            email,
            (email+secretKey).md5(),
            "com.arkhitech.howmuchshopadminapp",
            _DashbordScreen
        )
    }

    fun openFoodnerdPOS(context: Context, email: String, secretKey: String) {
        openApp(
            context,
            email,
            (email+secretKey).md5(),
            "menumonkey.orderdock",
            _NewOrderScreen
        )
    }

    fun openFoodnerdNewOrder(context: Context, email: String, secretKey: String) {
        openApp(
            context,
            email,
            (email+secretKey).md5(),
            "menumonkey.orderdock",
            _NewOrderScreen
        )
    }

    private fun openApp(context: Context,email:String, encryptedEmail: String, launchAppID: String, actionKey: String) {
        try {
            val pm: PackageManager = context.getPackageManager()
            val i = pm.getLaunchIntentForPackage(launchAppID)


            i?.putExtra("encrypted_email", encryptedEmail)
            i?.putExtra("screen", actionKey)
            i?.putExtra("email", email)

            context.startActivity(i)
        } catch (e: PackageManager.NameNotFoundException) {
            // TODO Auto-generated catch block
        }
    }


    fun String.md5(): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
    }
}