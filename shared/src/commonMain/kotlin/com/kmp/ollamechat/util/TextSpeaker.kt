package com.kmp.ollamechat.util

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
