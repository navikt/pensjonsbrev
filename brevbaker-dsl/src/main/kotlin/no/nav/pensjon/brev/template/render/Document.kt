package no.nav.pensjon.brev.template.render

import java.nio.file.Path

interface Document {
    val files: List<DocumentFile>
}

class DocumentFile(val fileName: String, val content: String) {
    constructor(fileName: String, contentWriter: Appendable.() -> Unit) :
        this(fileName, String(StringBuilder().apply(contentWriter)))

    fun writeTo(path: Path) {
        path.resolve(fileName).toFile().writeText(content, Charsets.UTF_8)
    }
}
