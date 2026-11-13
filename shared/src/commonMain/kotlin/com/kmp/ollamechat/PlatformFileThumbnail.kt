package com.kmp.ollamechat

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.github.vinceglb.filekit.PlatformFile

@Composable
fun PlatformFileThumbnail(
    file: PlatformFile,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = file, // Передаем PlatformFile напрямую
        contentDescription = "Превью изображения",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(36.dp)
            .clip(RoundedCornerShape(8.dp))
    )
}
