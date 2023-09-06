package com.pyramidal.luuck.ui.main.privacy

import android.content.Context
import android.content.Intent
import android.net.Uri

object RemotePrivacy {
    private var mainLink: String = "https://www.google.com/"
    fun openPrivacyLink(context: Context) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mainLink))
        context.startActivity(intent)
    }
}