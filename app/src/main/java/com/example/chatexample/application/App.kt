package com.example.chatexample.application

import android.app.Application
import androidx.emoji.bundled.BundledEmojiCompatConfig
import androidx.emoji.text.EmojiCompat

/**
 * @author Perry Lance
 * @since 2019-06-02 Created
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        EmojiCompat.init(BundledEmojiCompatConfig(applicationContext))
    }
}