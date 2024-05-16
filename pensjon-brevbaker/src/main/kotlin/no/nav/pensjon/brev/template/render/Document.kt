package no.nav.pensjon.brev.template.render

import no.nav.pensjon.brev.latex.LatexAppendable
import java.nio.file.Path
import java.util.*

interface Document {
    fun base64EncodedFiles(): Map<String, String>
    val files: List<DocumentFile>
}

private val base64Encoder: Base64.Encoder = Base64.getEncoder()

class LatexDocument : Document {
    private val _files: MutableList<DocumentFile> = mutableListOf()
    override val files: List<DocumentFile>
        get() = _files

    override fun base64EncodedFiles(): Map<String, String> {
        return _files.associate {
            when (it) {
                is DocumentFile.Binary -> it.fileName to base64Encoder.encodeToString(it.content)
                is DocumentFile.PlainText -> it.fileName to base64Encoder.encodeToString(it.content.toByteArray(Charsets.UTF_8))
            }
        }
    }

    private fun newPlainTextFile(fileName: String, writeToFile: Appendable.() -> Unit) =
        _files.add(DocumentFile.PlainText(fileName, writeToFile))

    fun newLatexFile(fileName: String, writeToFile: LatexAppendable.() -> Unit) =
        newPlainTextFile(fileName) {
            LatexAppendable(this).apply(writeToFile)
        }

    fun addFiles(newFiles: List<DocumentFile>) {
        _files.addAll(newFiles)
    }
}

class HTMLDocument : Document {
    private val _files: MutableList<DocumentFile.PlainText> = mutableListOf()
    override val files: List<DocumentFile>
        get() = _files

    override fun base64EncodedFiles(): Map<String, String> {
        return _files.associate { it.fileName to base64Encoder.encodeToString(it.content.toByteArray(Charsets.UTF_8)) }
    }

    fun newFile(filename: String, writeToFile: Appendable.() -> Unit) {
        _files.add(DocumentFile.PlainText(filename, writeToFile))
    }

    fun addFile(file: DocumentFile.PlainText) {
        _files.add(file)
    }
}

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