package no.nav.pensjon.brev.template.vedlegg

import no.nav.pensjon.brevbaker.api.model.LanguageCode
import java.util.Objects

@PDFVedleggMarker
class PDFVedlegg() {
    private val muterbarSider: MutableList<Side> = mutableListOf()
    val sider: List<Side>
        get() = muterbarSider

    fun side(filnavn: String, init: Side.() -> Unit) {
        muterbarSider.add(Side(filnavn).apply(init))
    }

    override fun equals(other: Any?): Boolean {
        if (other !is PDFVedlegg) return false
        return muterbarSider == other.muterbarSider
    }
    override fun hashCode() = muterbarSider.hashCode()
    override fun toString() = "PDFVedlegg(muterbarSider=$muterbarSider)"
}

@PDFVedleggMarker
class Side(val filnavn: String) {
    private val muterbarFelt: MutableList<Felt> = mutableListOf()
    val felt: List<Felt>
        get() = muterbarFelt

    fun felt(init: Felt.() -> Unit) {
        muterbarFelt.add(Felt().apply(init))
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Side) return false
        return filnavn == other.filnavn && muterbarFelt == other.muterbarFelt
    }

    override fun hashCode() = Objects.hash(filnavn, muterbarFelt)
    override fun toString() = "Side(filnavn='$filnavn', felt=$felt)"
}

@PDFVedleggMarker
class Felt() {
    private val muterbareFelt: MutableMap<String, Map<LanguageCode, String?>?> = mutableMapOf()
    val felt: Map<String, Map<LanguageCode, String?>?>
        get() = muterbareFelt

    infix fun String.to(str: String) {
        muterbareFelt[this] = leggTilPaaAlleSpraak(str)
    }

    infix fun String.to(verdi: Map<LanguageCode, String?>?) {
        muterbareFelt[this] = verdi
    }

    infix fun String.to(verdi: Any?) {
        muterbareFelt[this] = verdi?.let { leggTilPaaAlleSpraak(it.toString()) }
    }

    private fun leggTilPaaAlleSpraak(str: String?): Map<LanguageCode, String?> = mapOf(
        LanguageCode.BOKMAL to str,
        LanguageCode.NYNORSK to str,
        LanguageCode.ENGLISH to str
    )

    fun add(map: Map<String, Any?>) {
        map.entries
            .filter { it.value is String }
            .forEach {
            muterbareFelt[it.key] = leggTilPaaAlleSpraak(it.value as String)
        }
            map.entries
                .filter { it.value is Map<*, *> }
                .forEach { muterbareFelt[it.key] = it.value as Map<LanguageCode, String?> }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Felt) return false
        return muterbareFelt == other.muterbareFelt
    }
    override fun hashCode() = muterbareFelt.hashCode()
    override fun toString() = "Felt(muterbareFelt=$muterbareFelt)"
}

@DslMarker
internal annotation class PDFVedleggMarker