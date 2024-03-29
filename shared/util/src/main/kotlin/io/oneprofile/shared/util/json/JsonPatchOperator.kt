package io.oneprofile.shared.util.json

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fge.jsonpatch.JsonPatch
import com.squareup.moshi.*
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.lang.reflect.Type
import java.time.*
import java.util.*


class JsonPatchOperator {

    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
        .add(Instant::class.java, InstantJsonAdapter().nullSafe())
        .add(LocalDate::class.java, LocalDateJsonAdapter().nullSafe())
        .add(LocalTime::class.java, LocalTimeJsonAdapter().nullSafe())
        .add(LocalDateTime::class.java, LocalDateTimeJsonAdapter().nullSafe())
        .add(CustomDateTimeAdapter())
        .add(EpochMillisAdapter())
        .build()

    private val mapper = ObjectMapper()

    fun <T : Any> apply(source: T, ops: List<PatchOperation>): T {
        return try {

            val sourceAdapter = moshi.adapter(source.javaClass)
            val sourceJson: String = sourceAdapter.toJson(source)
            val sourceNode: JsonNode = mapper.readValue(sourceJson, JsonNode::class.java)

            val opsListType: Type = Types.newParameterizedType(List::class.java, PatchOperation::class.java)
            val patchOperationAdapter: JsonAdapter<List<PatchOperation>> = moshi.adapter(opsListType)

            val operations = ops.map {
                if (!sourceNode.findPath(it.path).isNull) {
                    it.op = "add"
                }
                it
            }

            val opJson: String = patchOperationAdapter.toJson(operations)

            val patchNode: JsonNode = mapper.readValue(opJson, JsonNode::class.java)

            val patched: JsonNode = JsonPatch.fromJson(patchNode).apply(sourceNode)
            sourceAdapter.fromJson(patched.toString()) as T
        } catch (e: Exception) {
            throw JsonPatchException("Error when trying to apply json patch for $ops")
        }
    }
}

class JsonPatchException(private val msg: String) : Throwable(msg) {}

class PatchOperation(
    var op: String,
    val path: String,
    val value: Any,
) {
    data class Builder(
        var op: String,
        var path: String? = null,
        var value: Any? = null,
    ) {
        fun path(path: String?) = apply { this.path = path }
        fun value(value: Any?) = apply { this.value = value }

        fun build() = PatchOperation(
                op = op,
                path = path!!,
                value = value!!
        )
    }
}
