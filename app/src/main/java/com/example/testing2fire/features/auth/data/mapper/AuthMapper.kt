package com.example.testing2fire.features.auth.data.mapper

import com.example.testing2fire.features.auth.data.model.CourierDTO
import com.example.testing2fire.features.auth.domain.model.Courier

object AuthMapper {
    fun CourierDTO.toDomain(): Courier {
        return Courier(
            id = _id,
            email = email,
            name = name,
            phone = phone
        )
    }
}