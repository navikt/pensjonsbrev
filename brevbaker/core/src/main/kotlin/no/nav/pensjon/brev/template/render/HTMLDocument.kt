package no.nav.pensjon.brev.template.render

class HTMLDocument(indexHTMLBuilder: Appendable.() -> Unit) : Document {
    override val files: List<DocumentFile> field: MutableList<DocumentFile> = mutableListOf()

    val indexHTML: DocumentFile = DocumentFile("index.html", indexHTMLBuilder).also { files.add(it) }
}