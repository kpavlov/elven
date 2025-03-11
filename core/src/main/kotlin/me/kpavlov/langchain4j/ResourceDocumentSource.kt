package me.kpavlov.langchain4j

import com.badlogic.gdx.files.FileHandle
import dev.langchain4j.data.document.DocumentSource
import dev.langchain4j.data.document.Metadata
import java.io.InputStream

class ResourceDocumentSource(
    val file: FileHandle,
) : DocumentSource {
    private val metadata =
        Metadata(
            mapOf(
                "name" to file.nameWithoutExtension(),
            ),
        )

    override fun inputStream(): InputStream = file.read()

    override fun metadata(): Metadata = metadata
}
