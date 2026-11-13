package com.kmp.ollamechat.util

/**
 * KMP Logger abstraction to allow using Timber on Android and print on iOS/others
 * from common code.
 */
expect object Logger {
    fun d(message: String)
    fun i(message: String)
    fun w(message: String)
    fun e(message: String, throwable: Throwable? = null)
}
