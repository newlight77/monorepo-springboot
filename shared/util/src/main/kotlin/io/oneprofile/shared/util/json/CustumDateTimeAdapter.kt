package io.oneprofile.shared.util.json

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime


class JsonDateTime (
    val date: String? = null,
    val time: String? = null,
    val timezone: String? = null,
)

class CustomDateTimeAdapter {

    @ToJson
    fun toJson(input: ZonedDateTime): JsonDateTime {
        val date = input.toLocalDate().toString()
        val time = input.toLocalTime().toString()
        val timezone = input.zone.toString()
        return JsonDateTime(date, time, timezone)
    }

    @FromJson
    fun fromJson(input: JsonDateTime): ZonedDateTime {
        val date: LocalDate = LocalDate.parse(input.date)
        val time: LocalTime = LocalTime.parse(input.time)
        val timezone = ZoneId.of(input.timezone)
        return ZonedDateTime.of(date, time, timezone)
    }
}