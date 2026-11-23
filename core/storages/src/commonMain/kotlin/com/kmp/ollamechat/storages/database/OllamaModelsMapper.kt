package com.kmp.ollamechat.storages.database

import com.kmp.ollamechat.domain.model.OllamaModel

// Domain to Entity
fun OllamaModel.toEntity(id: Long = 0L): ModelsEntity = ModelsEntity(
//    id = id,
    name = name,
    model = model,
    modifiedAt = modifiedAt,
    size = size,
    digest = digest,
    isSelected = isSelected
)

fun List<OllamaModel>.toEntity(): List<ModelsEntity> = map {it.toEntity()}

// Entity to Domain
fun ModelsEntity.toDomain(): OllamaModel = OllamaModel(
//    id = id,
    name = name,
    model = model,
    modifiedAt = modifiedAt,
    size = size,
    digest = digest,
    isSelected = isSelected,
)

// List Entity to List Domain
fun List<ModelsEntity>.toDomain(): List<OllamaModel> = map { it.toDomain() }