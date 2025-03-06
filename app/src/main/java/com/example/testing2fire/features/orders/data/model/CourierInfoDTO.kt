package com.example.testing2fire.features.orders.data.model

import com.google.gson.annotations.SerializedName


data class CourierInfoDTO(
    @SerializedName("name") val name: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("email") val email: String
)
