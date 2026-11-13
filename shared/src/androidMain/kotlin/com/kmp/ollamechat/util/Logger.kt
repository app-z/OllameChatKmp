package com.kmp.ollamechat.util

import timber.log.Timber

actual object Logger {
    actual fun d(message: String) {
        Timber.d(message)
    }

    actual fun i(message: String) {
        Timber.i(message)
    }

    actual fun w(message: String) {
        Timber.w(message)
    }

    actual fun e(message: String, throwable: Throwable?) {
        Timber.e(throwable, message)
    }
}
