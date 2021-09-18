package io.oneprofile.shared.util

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.oneprofile.shared.util.json.JsonPatchOperator
import io.oneprofile.shared.util.json.PatchOperation
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@ExtendWith(MockitoExtension::class)
class JsonPatchOperatorTest {

    @Test
    fun `should patch an simple flat object`() {
        // Arranges
        val shape = SimpleShape("square", 10, 14)
        val op = PatchOperation.Builder("replace")
                .path("/name")
                .value("rectangle")
                .build()

        // Act
        val result = JsonPatchOperator().apply(shape, listOf(op))

        // Arrange
        Assertions.assertEquals("rectangle", result.name)
    }

    @Test
    fun `should patch a complex object with list by adding an item to a list`() {
        // Arranges
        val shapes = CompositeShape("composite", SimpleShape("square", 10, 14))

        val op = PatchOperation.Builder("replace")
                .path("/shape/name")
                .value("circle")
                .build()

        // Act
        val result = JsonPatchOperator().apply(shapes, listOf(op))

        // Arrange
        Assertions.assertEquals("circle", result.shape!!.name)
    }

    @Test
    fun `should patch a complex object by adding new value when trying to replace a non existing path`() {
        // Arranges
        val shapes = CompositeShape("composite", SimpleShape(x = 1, y = 2))

        val op = PatchOperation.Builder("replace")
            .path("/shape/name")
            .value("circle")
            .build()

        // Act
        val result = JsonPatchOperator().apply(shapes, listOf(op))

        // Arrange
        Assertions.assertEquals("circle", result.shape!!.name)
    }

    @Test
    fun `should patch a complex object by changing the nested object`() {
        // Arranges
        val shapes = ShapeContainer(
                "list", listOf(
                SimpleShape("square", 10, 14)
        ),
                0)

        val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val op1 = PatchOperation.Builder("add")
                .path("/shapes/0")
                .value(moshi.adapter(SimpleShape::class.java).toJsonValue(SimpleShape("rectangle", 10, 14)))
                .build()
        val op2 = PatchOperation.Builder("add")
                .path("/shapes/0")
                .value(moshi.adapter(SimpleShape::class.java).toJsonValue(SimpleShape("circle", 1, 2)))
                .build()

        // Act
        val result = JsonPatchOperator().apply(shapes, listOf(op1, op2))

        // Arrange
        Assertions.assertEquals(3, result.shapes.size)
        Assertions.assertEquals("circle", result.shapes.first().name)
        Assertions.assertEquals("square", result.shapes.last().name)
    }

    @Test
    fun `should patch an complex object with java instant type`() {
        // Arranges
        val shape = ComplexWithInstant("java instant", Instant.now())
        val dateTime = Instant.now().toString()
        val op = PatchOperation.Builder("replace")
            .path("/date")
            .value(dateTime)
            .build()

        // Act
        val result = JsonPatchOperator().apply(shape, listOf(op))

        // Arrange
        Assertions.assertEquals("java instant", result.name)
        Assertions.assertEquals(dateTime, result.date.toString())
    }

    @Test
    fun `should patch an complex object with java instant when having only date format without time`() {
        // Arranges
        val shape = ComplexWithInstant("java instant", Instant.now())
        val op = PatchOperation.Builder("replace")
            .path("/date")
            .value("2021-01-22")
            .build()

        // Act
        val result = JsonPatchOperator().apply(shape, listOf(op))

        // Arrange
        Assertions.assertEquals("java instant", result.name)
        Assertions.assertEquals("2021-01-21T23:00:00Z", result.date.toString())
    }

    @Test
    fun `should patch an complex object with java local date type`() {
        // Arranges
        val shape = ComplexWithLocalDate("java local date", LocalDate.now())
        val newDate = LocalDate.now().toString()
        val op = PatchOperation.Builder("replace")
            .path("/date")
            .value(newDate)
            .build()

        // Act
        val result = JsonPatchOperator().apply(shape, listOf(op))

        // Arrange
        Assertions.assertEquals("java local date", result.name)
        Assertions.assertEquals(newDate, result.date.toString())
    }

    @Test
    fun `should patch an complex object with java local time type`() {
        // Arranges
        val shape = ComplexWithLocalTime("java local time", LocalTime.now())
        val newTime = LocalTime.now().toString()
        val op = PatchOperation.Builder("replace")
            .path("/time")
            .value(newTime)
            .build()

        // Act
        val result = JsonPatchOperator().apply(shape, listOf(op))

        // Arrange
        Assertions.assertEquals("java local time", result.name)
        Assertions.assertEquals(newTime, result.time.toString())
    }

    @Test
    fun `should patch an complex object with java local date time type`() {
        // Arranges
        val shape = ComplexWithLocalDateTime("java local date time", LocalDateTime.now())
        val newDateTime = LocalDateTime.now().toString()
        val op = PatchOperation.Builder("replace")
            .path("/datetime")
            .value(newDateTime)
            .build()

        // Act
        val result = JsonPatchOperator().apply(shape, listOf(op))

        // Arrange
        Assertions.assertEquals("java local date time", result.name)
        Assertions.assertEquals(newDateTime, result.datetime.toString())
    }

}

data class SimpleShape(
        val name: String? = null,
        val x: Int? = null,
        val y: Int? = null
)

data class CompositeShape(
        val name: String,
        val shape: SimpleShape? = null
)

data class ShapeContainer(
        val name: String,
        val shapes: List<SimpleShape>,
        val accumulator: Int
)

data class ComplexWithInstant(
    val name: String,
    val date: Instant
)

data class ComplexWithLocalDate(
    val name: String,
    val date: LocalDate
)

data class ComplexWithLocalTime(
    val name: String,
    val time: LocalTime
)

data class ComplexWithLocalDateTime(
    val name: String,
    val datetime: LocalDateTime
)