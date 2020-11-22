package io.tricefal.shared.util

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension

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
//        Assertions.assertEquals("circle", result)
        Assertions.assertEquals("circle", result.shape.name)
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

}

data class SimpleShape(
        val name: String,
        val x: Int,
        val y: Int
)

data class CompositeShape(
        val name: String,
        val shape: SimpleShape
)

data class ShapeContainer(
        val name: String,
        val shapes: List<SimpleShape>,
        val accumulator: Int
)