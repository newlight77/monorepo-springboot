package io.tricefal.shared.util.json

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import java.io.IOException
import java.time.Instant

class InstantJsonAdapter : JsonAdapter<Instant>() {
    @Synchronized
    @Throws(IOException::class)
    override fun fromJson(reader: JsonReader): Instant? {
        if (reader.peek() == JsonReader.Token.NULL) {
            return reader.nextNull()
        }
        return Instant.parse(reader.nextString())
    }

    @Synchronized
    @Throws(IOException::class)
    override fun toJson(writer: JsonWriter, value: Instant?) {
        writer.value(value.toString())
    }
}