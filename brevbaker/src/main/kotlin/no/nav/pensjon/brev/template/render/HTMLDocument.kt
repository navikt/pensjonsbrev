package no.nav.pensjon.brev.template.render

class HTMLDocument(indexHTMLBuilder: Appendable.() -> Unit) : Document {
    private val _files: MutableList<DocumentFile> = mutableListOf()
    override val files: List<DocumentFile>
        get() = _files

    val indexHTML: DocumentFile = DocumentFile("index.html", indexHTMLBuilder).also { _files.add(it) }
}
