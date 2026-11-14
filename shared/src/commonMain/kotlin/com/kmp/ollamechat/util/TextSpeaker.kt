package com.kmp.ollamechat.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

interface TextSpeaker {
    /**
     * Озвучивает текст.
     * @param flush если true — прерывает текущую речь и начинает заново;
     *              если false — добавляет фразу в очередь (удобно для стриминга).
     */
    fun speak(text: String, flush: Boolean = true)

    /** Немедленно останавливает озвучивание. */
    fun stop()

    /** Освобождает ресурсы движка при закрытии экрана. */
    fun shutdown()
}

fun String.leanMarkdown(): String {
    return this
        .replace(Regex("(?s)```.*?```"), " Код опущен. ") // (?s) включает захват переносов строк на всех платформах
        .replace(Regex("[*#_`~>]"), "") // Убираем символы разметки
        .replace(Regex("""\s+"""), " ") // Схлопываем лишние пробелы и переносы
        .trim()
}

fun String.toBoldAnnotatedString(): AnnotatedString {
    val source = this
    return buildAnnotatedString {
        var index = 0
        while (index < source.length) {
            val start = source.indexOf("**", index)
            if (start == -1) {
                append(source.substring(index)); break
            }
            append(source.substring(index, start))
            val end = source.indexOf("**", start + 2)
            if (end == -1) {
                append(source.substring(start)); break
            }
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append(source.substring(start + 2, end))
            }
            index = end + 2
        }
    }
}
