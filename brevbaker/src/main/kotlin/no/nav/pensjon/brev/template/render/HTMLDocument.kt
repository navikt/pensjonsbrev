package no.nav.pensjon.brev.template.render

class HTMLDocument(indexHTMLBuilder: Appendable.() -> Unit) : Document {
    private val _files: MutableList<DocumentFile> = mutableListOf()
    override val files: List<DocumentFile>
        get() = _files

    val indexHTML: DocumentFile.PlainText = DocumentFile.PlainText("index.html", indexHTMLBuilder).also { _files.add(it) }

    fun newFile(filename: String, writeToFile: Appendable.() -> Unit) {
        _files.add(DocumentFile.PlainText(filename, writeToFile))
    }

    fun addFile(file: DocumentFile.PlainText) {
        _files.add(file)
    }
}