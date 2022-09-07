package no.nav.pensjon.brev.template.render

import no.nav.pensjon.brev.latex.LatexAppendable
import java.nio.file.Path
import java.util.*

interface RenderedLetter {
    fun base64EncodedFiles(): Map<String, String>
    val files: List<RenderedFile>
}

private val base64Encoder: Base64.Encoder = Base64.getEncoder()

class RenderedLatexLetter : RenderedLetter {
    private val _files: MutableList<RenderedFile> = mutableListOf()
    override val files: List<RenderedFile>
        get() = _files

    override fun base64EncodedFiles(): Map<String, String> {
        return _files.associate {
            when (it) {
                is RenderedFile.Binary -> it.fileName to base64Encoder.encodeToString(it.content)
                is RenderedFile.PlainText -> it.fileName to base64Encoder.encodeToString(it.content.toByteArray(Charsets.UTF_8))
            }
        }
    }

    private fun newPlainTextFile(fileName: String, writeToFile: Appendable.() -> Unit) =
        _files.add(RenderedFile.PlainText(fileName, writeToFile))

    fun newLatexFile(fileName: String, writeToFile: LatexAppendable.() -> Unit) =
        newPlainTextFile(fileName) {
            LatexAppendable(this).apply(writeToFile)
        }

    fun addFiles(newFiles: List<RenderedFile>) {
        _files.addAll(newFiles)
    }
}

class RenderedHtmlLetter : RenderedLetter {
    private val _files: MutableList<RenderedFile.PlainText> = mutableListOf()
    override val files: List<RenderedFile>
        get() = _files

    override fun base64EncodedFiles(): Map<String, String> {
        return _files.associate { it.fileName to base64Encoder.encodeToString(it.content.toByteArray(Charsets.UTF_8)) }
    }

    fun newFile(filename: String, writeToFile: Appendable.() -> Unit) {
        _files.add(RenderedFile.PlainText(filename, writeToFile))
    }
}

sealed class RenderedFile {
    abstract val fileName: String
    abstract fun writeTo(path: Path)

    class Binary(override val fileName: String, val content: ByteArray) : RenderedFile() {
        override fun writeTo(path: Path) {
            path.resolve(fileName).toFile().writeBytes(content)
        }
    }

    class PlainText(override val fileName: String, val content: String) : RenderedFile() {
        constructor(fileName: String, contentWriter: Appendable.() -> Unit) :
                this(fileName, String(StringBuilder().apply(contentWriter)))

        override fun writeTo(path: Path) {
            path.resolve(fileName).toFile().writeText(content, Charsets.UTF_8)
        }
    }
}