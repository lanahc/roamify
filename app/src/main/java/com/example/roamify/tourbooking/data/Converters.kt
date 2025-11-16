package com.example.roamify.tourbooking.data

import androidx.room.TypeConverter
import java.util.Date

/**
 * Type converters to allow Room to store complex data types that it doesn't
 * natively support, like Date and our custom BookingStatus enum.
 */
class Converters {
    // Converter for java.util.Date
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    // Converter for the BookingStatus enum
    @TypeConverter
    fun fromBookingStatus(status: BookingStatus?): String? {
        return status?.name
    }

    @TypeConverter
    fun toBookingStatus(value: String?): BookingStatus? {
        // Find the enum constant that matches the string value, case-insensitively.
        return value?.let { enumValueOf<BookingStatus>(it) }
    }
}
