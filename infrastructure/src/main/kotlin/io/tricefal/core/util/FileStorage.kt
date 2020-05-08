package io.tricefal.core.util

import org.apache.tomcat.util.http.fileupload.IOUtils
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import java.nio.file.StandardOpenOption.*

class FileStorage {

    fun save(inputStream: InputStream, target: Path): Long {
        inputStream.use {
            if (!Files.isDirectory(target.parent))
                Files.createDirectories(target.parent)

            val tempFile = Paths.get("$target.tmp")
            Files.deleteIfExists(tempFile)

            val byteCount = Files.newOutputStream(tempFile, CREATE_NEW, WRITE).use { outputStream ->
                try {
                    IOUtils.copyLarge(inputStream, outputStream)
                } catch (e: IOException) {
                    Files.deleteIfExists(tempFile)
                } as Long
            }

            Files.move(tempFile, target)

            return byteCount
        }
    }

}


