package com.kmp.ollamechat.imagevisual

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.kmp.ollamechat.useCase.ImageUseCase
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher

@Composable
fun ImagePickerScreen(imageUseCase: ImageUseCase) {
    val scope = rememberCoroutineScope()
    var pickedFile by remember { mutableStateOf<PlatformFile?>(null) }

    // Готовый мультиплатформенный лаунчер
    val launcher = rememberFilePickerLauncher(
        type = FileKitType.Image
    ) { file ->
        pickedFile = file // Возвращает PlatformFile или null, если выбор отменили
    }

    // Запуск выбора по клику на кнопку:
    // Button(onClick = { launcher.launch() }) { Text("Выбрать картинку") }

    // Чтение байтов при отправке в Ollama:
    // val bytes: ByteArray = pickedFile.readBytes()
}
