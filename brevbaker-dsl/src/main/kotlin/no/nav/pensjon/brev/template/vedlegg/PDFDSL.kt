package no.nav.pensjon.brev.template.vedlegg

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LanguageSupport

@PDFVedleggMarker
class PDFVedlegg<Lang: LanguageSupport>() {
    private val muterbarSider: MutableList<Side<Lang>> = mutableListOf()
    val sider: List<Side<Lang>>
        get() = muterbarSider

    fun side(filnavn: String, init: Side<Lang>.() -> Unit) {
        muterbarSider.add(Side<Lang>(filnavn).apply(init))
    }

    companion object {
        fun <Lang: LanguageSupport> create(init: PDFVedlegg<Lang>.() -> Unit): PDFVedlegg<Lang> = PDFVedlegg<Lang>().apply(init)
    }
}

@PDFVedleggMarker
class Side<Lang : LanguageSupport>(val filnavn: String) {
    private val muterbarFelt: MutableList<Felt<Lang>> = mutableListOf()
    val felt: List<Felt<Lang>>
        get() = muterbarFelt

    fun felt(init: Felt<Lang>.() -> Unit) {
        muterbarFelt.add(Felt<Lang>().apply(init))
    }
}

@PDFVedleggMarker
class Felt<Lang : LanguageSupport>() {
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