package com.kmp.ollamechat.util

import platform.AVFAudio.AVSpeechBoundary
import platform.AVFAudio.AVSpeechSynthesisVoice
import platform.AVFAudio.AVSpeechSynthesizer
import platform.AVFAudio.AVSpeechUtterance

class IosTextSpeaker : TextSpeaker {

    private val synthesizer = AVSpeechSynthesizer()

    override fun speak(text: String, flush: Boolean) {
        val cleanText = text.leanMarkdown()
        if (cleanText.isBlank()) return

        if (flush && synthesizer.isSpeaking()) {
            synthesizer.stopSpeakingAtBoundary(AVSpeechBoundary.AVSpeechBoundaryImmediate)
        }

        val utterance = AVSpeechUtterance(string = cleanText).apply {
            voice = AVSpeechSynthesisVoice.voiceWithLanguage("ru-RU")
            rate = 0.5f // Комфортная скорость речи (по умолчанию ~0.5)
        }
        synthesizer.speakUtterance(utterance)
    }

    override fun stop() {
        synthesizer.stopSpeakingAtBoundary(AVSpeechBoundary.AVSpeechBoundaryImmediate)
    }

    override fun shutdown() {
        stop()
    }
}
