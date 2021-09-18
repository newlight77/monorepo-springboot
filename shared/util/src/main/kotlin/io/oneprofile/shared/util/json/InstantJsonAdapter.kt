package io.oneprofile.shared.util.json

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import java.io.IOException
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class InstantJsonAdapter : JsonAdapter<Instant>() {
    @Synchronized
    @Throws(IOException::class)
    override fun fromJson(reader: JsonReader): Instant? {
        if (reader.peek() == JsonReader.Token.NULL) {
            return reader.nextNull()
        }
        val dateString = reader.nextString()
        if (dateString.length < 11) {
            return LocalDate.parse(dateString).atStartOfDay().atZone(ZoneId.of("CET")).toInstant()
        }
        return Instant.parse(dateString)
    }

    @Synchronized
    @Throws(IOException::class)
    override fun toJson(writer: JsonWriter, value: Instant?) {
        writer.value(value.toString())
    }
}