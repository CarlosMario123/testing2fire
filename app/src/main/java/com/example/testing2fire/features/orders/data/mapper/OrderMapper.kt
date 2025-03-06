package com.example.testing2fire.features.orders.data.mapper

import CourierInfo
import com.example.testing2fire.features.orders.data.model.*
import com.example.testing2fire.features.orders.domain.model.*
import java.text.SimpleDateFormat
import java.util.*

class OrderMapper {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

    init {
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    }

    fun mapToDomain(dto: OrderDTO): Order {
        return Order(
            id = dto.id,
            notes = dto.notes,
            address = dto.address,
            status = dto.status,
            userInfo = mapUserInfoToDomain(dto.userInfo),
            courierId = dto.courierId,
            courierInfo = dto.courierInfo?.let { mapCourierInfoToDomain(it) },
            createdAt = parseDate(dto.createdAt),
            updatedAt = parseDate(dto.updatedAt),
            assignedAt = dto.assignedAt?.let { parseDate(it) },
            completedAt = dto.completedAt?.let { parseDate(it) }
        )
    }

    fun mapUserInfoToDomain(dto: UserInfoDTO): UserInfo {
        return UserInfo(
            name = dto.name,
            phone = dto.phone,
            email = dto.email
        )
    }

    fun mapCourierInfoToDomain(dto: CourierInfoDTO): CourierInfo {
        return CourierInfo(
            name = dto.name,
            phone = dto.phone,
            email = dto.email
        )
    }

    fun mapMetadataToDomain(dto: MetadataDTO): Metadata {
        return Metadata(
            total = dto.total,
            limit = dto.limit,
            skip = dto.skip,
            hasMore = dto.hasMore
        )
    }

    fun mapPendingOrdersResponseToDomain(dto: PendingOrdersResponseDTO): PendingOrdersResult {
        return PendingOrdersResult(
            orders = dto.orders.map { mapToDomain(it) },
            metadata = mapMetadataToDomain(dto.metadata)
        )
    }

    private fun parseDate(dateString: String): Date {
        return try {
            dateFormat.parse(dateString) ?: Date()
        } catch (e: Exception) {
            Date()
        }
    }
}