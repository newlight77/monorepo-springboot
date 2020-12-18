package io.tricefal.shared.util.json

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson
import java.time.Instant


@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION, AnnotationTarget.TYPE_PARAMETER, AnnotationTarget.VALUE_PARAMETER)
@JsonQualifier
annotation class EpochMillis

/**
 * Here is an example of usage:
 *
 * <pre>{@code
 * public class Post {
 * private String title;
 * private String author;
 * @EpochMillis Instant posted;
 * // constructor, getters and setters
 * }
 * }</pre>
 */
class EpochMillisAdapter {

    @ToJson
    fun toJson(@EpochMillis input: Instant): Long {
        return input.toEpochMilli()
    }

    @FromJson
    @EpochMillis
    fun fromJson(input: Long): Instant {
        return Instant.ofEpochMilli(input)
    }
}