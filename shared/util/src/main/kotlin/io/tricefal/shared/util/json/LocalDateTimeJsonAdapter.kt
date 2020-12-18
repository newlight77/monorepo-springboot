package io.tricefal.shared.util.json

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import java.io.IOException
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime

class LocalDateTimeJsonAdapter : JsonAdapter<LocalDateTime>() {
    @Synchronized
    @Throws(IOException::class)
    override fun fromJson(reader: JsonReader): LocalDateTime? {
        if (reader.peek() == JsonReader.Token.NULL) {
            return reader.nextNull()
        }
        return LocalDateTime.parse(reader.nextString())
    }

    @Synchronized
    @Throws(IOException::class)
    override fun toJson(writer: JsonWriter, value: LocalDateTime?) {
        writer.value(value.toString())
    }
}