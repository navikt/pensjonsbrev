package no.nav.pensjon.brev.pdfbygger.typst

import no.nav.pensjon.brev.template.render.Document
import no.nav.pensjon.brev.template.render.DocumentFile

internal class TypstDocument : Document {
    private val _files: MutableList<DocumentFile> = mutableListOf()
    override val files: List<DocumentFile>
        get() = _files

    private fun newPlainTextFile(fileName: String, writeToFile: Appendable.() -> Unit) =
        _files.add(DocumentFile(fileName, writeToFile))

    internal fun newTypstFile(fileName: String, writeToFile: TypstAppendable.() -> Unit) =
        newPlainTextFile(fileName) {
            TypstAppendable(this).apply(writeToFile)
        }
}