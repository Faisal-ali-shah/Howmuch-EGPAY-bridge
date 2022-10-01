package com.howmuch_egpay_bridge

import android.content.Context
import android.content.pm.PackageManager


object IntegrationBridge {

    val _DashbordScreen = "Dashboard"
    val _NewOrderScreen = "NewOrder"
    fun openHowmuchPOS(context: Context, email: String, secretKey: String) {
        openApp(
            context,
            AESUtils.encrypt(email, secretKey.toByteArray()),
            "com.arkhitech.howmuchshopadminapp",
            _DashbordScreen,
        )




    }

    fun openHowmuchNewOrder(context: Context, email: String, secretKey: String) {
        openApp(
            context,
            AESUtils.encrypt(email, secretKey.toByteArray()),
            "com.arkhitech.howmuchshopadminapp",
            _DashbordScreen
        )
    }

    fun openFoodnerdPOS(context: Context, email: String, secretKey: String) {
        openApp(
            context,
            AESUtils.encrypt(email, secretKey.toByteArray()),
            "menumonkey.orderdock",
            _NewOrderScreen
        )
    }

    fun openFoodnerdNewOrder(context: Context, email: String, secretKey: String) {
        openApp(
            context,
            AESUtils.encrypt(email, secretKey.toByteArray()),
            "menumonkey.orderdock",
            _NewOrderScreen
        )
    }

    fun openApp(context: Context, encryptedEmail: String, launchAppID: String, actionKey: String) {
        try {
            val pm: PackageManager = context.getPackageManager()
            val i = pm.getLaunchIntentForPackage(launchAppID)


            i?.putExtra("encrypted_email", encryptedEmail)
            i?.putExtra("screen", actionKey)
            context.startActivity(i)
        } catch (e: PackageManager.NameNotFoundException) {
            // TODO Auto-generated catch block
        }
    }
}