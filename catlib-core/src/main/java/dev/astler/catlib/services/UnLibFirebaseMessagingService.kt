package dev.astler.catlib.services

import com.google.firebase.messaging.FirebaseMessagingService

open class UnLibFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}
