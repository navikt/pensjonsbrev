package no.nav.pensjon.brev.template.vedlegg

import no.nav.pensjon.brev.template.LanguageSupport

@PDFVedleggMarker
class PDFVedlegg<Lang: LanguageSupport>(private val title: String) {
    private val muterbarSider: MutableList<Side<Lang>> = mutableListOf()
    val sider: List<Side<Lang>>
        get() = muterbarSider

    fun side(filnavn: String, init: Side<Lang>.() -> Unit) {
        muterbarSider.add(Side<Lang>(filnavn).apply(init))
    }

    companion object {
        fun <Lang: LanguageSupport> create(title: String, init: PDFVedlegg<Lang>.() -> Unit): PDFVedlegg<Lang> = PDFVedlegg<Lang>(title).apply(init)
    }
}

@PDFVedleggMarker
class Side<Lang>(val filnavn: String) {
    private val muterbarFelt: MutableList<Felt<Lang>> = mutableListOf()
    val felt: List<Felt<Lang>>
        get() = muterbarFelt

    fun felt(init: Felt<Lang>.() -> Unit) {
        muterbarFelt.add(Felt<Lang>().apply(init))
    }
}

@PDFVedleggMarker
class Felt<Lang>() {
    private val muterbareFelt: MutableMap<String, Any?> = mutableMapOf()
    val felt: Map<String, Any?>
        get() = muterbareFelt

    infix fun String.to(verdi: Any?) {
        muterbareFelt[this] = verdi
    }

    fun add(map: Map<String, Any?>) {
        muterbareFelt.putAll(map)
    }
}

@DslMarker
internal annotation class PDFVedleggMarker