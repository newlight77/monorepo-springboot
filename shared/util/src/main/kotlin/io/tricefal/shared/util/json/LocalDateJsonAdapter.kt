package io.tricefal.shared.util.json

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import java.io.IOException
import java.time.Instant
import java.time.LocalDate

class LocalDateJsonAdapter : JsonAdapter<LocalDate>() {
    @Synchronized
    @Throws(IOException::class)
    override fun fromJson(reader: JsonReader): LocalDate? {
        if (reader.peek() == JsonReader.Token.NULL) {
            return reader.nextNull()
        }
        return LocalDate.parse(reader.nextString())
    }

    @Synchronized
    @Throws(IOException::class)
    override fun toJson(writer: JsonWriter, value: LocalDate?) {
        writer.value(value.toString())
    }
}