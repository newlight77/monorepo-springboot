package io.oneprofile.signup.storage

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.ByteArrayInputStream
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

internal class FileStorageTest {

    @TempDir
    lateinit var tmpDir: File

    @Test
    fun `should save a file in the file system`() {
        // Arrange
        val filePath = Paths.get(tmpDir.absolutePath + "-test")
        val inputStream = ByteArrayInputStream("testing".toByteArray())

        // Act
        val byteCount = FileStorage().save(inputStream, filePath)

        // Assert
        Assertions.assertTrue(Files.exists(filePath))
        Assertions.assertEquals(7, byteCount)
    }
}