package no.nav.pensjon.brev.template.vedlegg

import no.nav.pensjon.brev.template.Language

@PDFVedleggMarker
class PDFVedlegg() {
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
    private val muterbareFelt: MutableMap<String, Map<Language, String?>?> = mutableMapOf()
    val felt: Map<String, Map<Language, String?>?>
        get() = muterbareFelt

    infix fun String.to(str: String) {
        muterbareFelt[this] = leggTilPaaAlleSpraak(str)
    }

    infix fun String.to(verdi: Map<Language, String?>?) {
        muterbareFelt[this] = verdi
    }

    infix fun String.to(verdi: Any?) {
        muterbareFelt[this] = verdi?.let { leggTilPaaAlleSpraak(it.toString()) }
    }

    private fun leggTilPaaAlleSpraak(str: String?): Map<Language, String?> = mapOf(
        Language.Bokmal to str,
        Language.Nynorsk to str,
        Language.English to str
    )

    fun add(map: Map<String, Any?>) {
        map.entries
            .filter { it.value is String }
            .forEach {
            muterbareFelt[it.key] = leggTilPaaAlleSpraak(it.value as String)
        }
            map.entries
                .filter { it.value is Map<*, *> }
                .forEach { muterbareFelt[it.key] = it.value as Map<Language, String?> }
    }
}

@DslMarker
internal annotation class PDFVedleggMarker