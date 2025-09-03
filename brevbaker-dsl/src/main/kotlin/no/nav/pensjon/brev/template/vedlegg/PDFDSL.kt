package no.nav.pensjon.brev.template.vedlegg

@PDFVedleggMarker
class PDFVedlegg {
    private val muterbarSider: MutableList<Side> = mutableListOf()
    val sider: List<Side>
        get() = muterbarSider

    fun side(filnavn: String, init: Side.() -> Unit) {
        muterbarSider.add(Side(filnavn).apply(init))
    }

    companion object {
        fun create(init: PDFVedlegg.() -> Unit): PDFVedlegg = PDFVedlegg().apply(init)
    }
}

@PDFVedleggMarker
class Side(val filnavn: String) {
    private val muterbarFelt: MutableList<Felt> = mutableListOf()
    val felt: List<Felt>
        get() = muterbarFelt

    fun felt(init: Felt.() -> Unit) {
        muterbarFelt.add(Felt().apply(init))
    }
}

@PDFVedleggMarker
class Felt() {
    private val muterbareFelt: MutableMap<String, Any> = mutableMapOf()
    val felt: Map<String, Any>
        get() = muterbareFelt

    infix fun String.to(verdi: Any) {
        muterbareFelt[this] = verdi
    }
}

@DslMarker
internal annotation class PDFVedleggMarker