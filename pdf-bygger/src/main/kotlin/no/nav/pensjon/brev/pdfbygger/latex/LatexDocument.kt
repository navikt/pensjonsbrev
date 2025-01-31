package no.nav.pensjon.brev.pdfbygger.latex

import no.nav.pensjon.brev.template.render.Document
import no.nav.pensjon.brev.template.render.DocumentFile

// TODO: Fas ut denne til fordel for noko enklare
class LatexDocument : Document {
    private val _files: MutableList<DocumentFile> = mutableListOf()
    override val files: List<DocumentFile>
        get() = _files

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