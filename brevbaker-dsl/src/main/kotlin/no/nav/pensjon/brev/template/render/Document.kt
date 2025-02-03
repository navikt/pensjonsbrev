package no.nav.pensjon.brev.template.render

import java.nio.file.Path
import java.util.Base64

interface Document {
    val files: List<DocumentFile>
    fun base64EncodedFiles(): Map<String, String> = files.associate {
        it.fileName to base64Encoder.encodeToString(it.content.toByteArray(Charsets.UTF_8))
    }
}

private val base64Encoder: Base64.Encoder = Base64.getEncoder()

class DocumentFile(val fileName: String, val content: String) {
    constructor(fileName: String, contentWriter: Appendable.() -> Unit) :
            this(fileName, String(StringBuilder().apply(contentWriter)))

    fun writeTo(path: Path) {
        path.resolve(fileName).toFile().writeText(content, Charsets.UTF_8)
    }
}