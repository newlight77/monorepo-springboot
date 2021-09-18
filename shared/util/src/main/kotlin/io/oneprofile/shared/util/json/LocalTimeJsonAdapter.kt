package io.oneprofile.shared.util.json

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import java.io.IOException
import java.time.LocalTime

class LocalTimeJsonAdapter : JsonAdapter<LocalTime>() {
    @Synchronized
    @Throws(IOException::class)
    override fun fromJson(reader: JsonReader): LocalTime? {
        if (reader.peek() == JsonReader.Token.NULL) {
            return reader.nextNull()
        }
        return LocalTime.parse(reader.nextString())
    }

    @Synchronized
    @Throws(IOException::class)
    override fun toJson(writer: JsonWriter, value: LocalTime?) {
        writer.value(value.toString())
    }
}