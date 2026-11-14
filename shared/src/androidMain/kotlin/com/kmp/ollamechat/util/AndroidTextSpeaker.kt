package com.kmp.ollamechat.util

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class AndroidTextSpeaker(context: Context) : TextSpeaker {

    private var isReady = false
    private var pendingText: String? = null

    private val tts: TextToSpeech = TextToSpeech(context.applicationContext) { status ->
        if (status == TextToSpeech.SUCCESS) {
            // Устанавливаем русский язык (или Locale.getDefault())
            val result = tts.setLanguage(Locale("ru", "RU"))
            isReady = result != TextToSpeech.LANG_MISSING_DATA &&
                    result != TextToSpeech.LANG_NOT_SUPPORTED

            // Если текст передали до окончания инициализации — озвучиваем его сейчас
            pendingText?.let { text ->
                speak(text, flush = true)
                pendingText = null
            }
        }
    }

    override fun speak(text: String, flush: Boolean) {
        val cleanText = text.leanMarkdown()
        if (cleanText.isBlank()) return

        if (!isReady) {
            pendingText = cleanText
            return
        }

        val queueMode = if (flush) TextToSpeech.QUEUE_FLUSH else TextToSpeech.QUEUE_ADD
        tts.speak(cleanText, queueMode, null, cleanText.hashCode().toString())
    }

    override fun stop() {
        pendingText = null
        if (isReady) {
            tts.stop()
        }
    }

    override fun shutdown() {
        tts.stop()
        tts.shutdown()
    }
}
