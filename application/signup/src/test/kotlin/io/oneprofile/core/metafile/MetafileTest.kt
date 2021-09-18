package io.oneprofile.core.metafile

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.Instant

class MetafileTest {

    @Test
    fun `should get the full path when mapping domain to a model`() {
        // Arrange
        val fullPath = "/data/files/newlight77+test1@gmail.com/CV/20210131-205843-_-resume.pdf"
        val domain = MetafileDomain(
            username = "username",
            filename = fullPath,
            representation = Representation.CV,
            contentType = "octet/stream",
            size = 1024,
            creationDate = Instant.now()
        )

        // Act
        val result = toModel(domain)

        // Assert
        Assertions.assertEquals("/data/files/newlight77+test1@gmail.com/CV/20210131-205843-_-resume.pdf", result.filename)
    }
}