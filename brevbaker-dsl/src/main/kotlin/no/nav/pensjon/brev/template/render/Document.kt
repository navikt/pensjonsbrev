package no.nav.pensjon.brev.template.render

import java.nio.file.Path
import java.util.*

interface Document {
    val files: List<DocumentFile>
    fun base64EncodedFiles(): Map<String, String> =
        files.associate {
            when (it) {
                is DocumentFile.Binary -> it.fileName to base64Encoder.encodeToString(it.content)
                is DocumentFile.PlainText -> it.fileName to base64Encoder.encodeToString(it.content.toByteArray(Charsets.UTF_8))
            }
        }
}

private val base64Encoder: Base64.Encoder = Base64.getEncoder()

sealed class DocumentFile {
    abstract val fileName: String
    abstract fun writeTo(path: Path)

    class Binary(override val fileName: String, val content: ByteArray) : DocumentFile() {
        override fun writeTo(path: Path) {
            path.resolve(fileName).toFile().writeBytes(content)
        }
    }

    class PlainText(override val fileName: String, val content: String) : DocumentFile() {
        constructor(fileName: String, contentWriter: Appendable.() -> Unit) :
                this(fileName, String(StringBuilder().apply(contentWriter)))

        override fun writeTo(path: Path) {
            path.resolve(fileName).toFile().writeText(content, Charsets.UTF_8)
        }
    }
}