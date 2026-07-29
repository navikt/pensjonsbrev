package no.nav.pensjon.brev.skribenten.vedlegg

import no.nav.pensjon.brevbaker.api.model.LanguageCode
import java.util.Objects

@PDFVedleggMarker
class PDFVedlegg {
    val sider: List<Side>
        field: MutableList<Side> = mutableListOf()

    fun side(filnavn: String, init: Side.() -> Unit) {
        sider.add(Side(filnavn).apply(init))
    }

    override fun equals(other: Any?): Boolean {
        if (other !is PDFVedlegg) return false
        return sider == other.sider
    }

    override fun hashCode() = sider.hashCode()
    override fun toString() = "PDFVedlegg(muterbarSider=${sider})"
}

@PDFVedleggMarker
class Side(val filnavn: String) {
    val felt: List<Felt>
        field: MutableList<Felt> = mutableListOf()

    fun felt(init: Felt.() -> Unit) {
        felt.add(Felt().apply(init))
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Side) return false
        return filnavn == other.filnavn && felt == other.felt
    }

    override fun hashCode() = Objects.hash(filnavn, felt)
    override fun toString() = "Side(filnavn='$filnavn', felt=$felt)"
}

@PDFVedleggMarker
class Felt {
    val felt: Map<String, Map<LanguageCode, String?>?>
        field: MutableMap<String, Map<LanguageCode, String?>?> = mutableMapOf()

    infix fun String.to(str: String) {
        felt[this] = leggTilPaaAlleSpraak(str)
    }

    infix fun String.to(verdi: Map<LanguageCode, String?>) {
        felt[this] = verdi
    }

    infix fun String.to(verdi: Any?) {
        if (verdi is Map<*, *>) {
            throw IllegalArgumentException("Forventa ikke å legge til map her. Bruk infix-versjonen to Map<LanguageCode, String?> for å legge til map av språk til tekst")
        }
        felt[this] = verdi?.let { leggTilPaaAlleSpraak(it.toString()) }
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
                felt[it.key] = leggTilPaaAlleSpraak(it.value as String)
            }
        map.entries
            .filter { it.value is Map<*, *> }
            .forEach { felt[it.key] = it.value as Map<LanguageCode, String?> }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Felt) return false
        return felt == other.felt
    }
    override fun hashCode() = felt.hashCode()
    override fun toString() = "Felt(felt=$felt)"
}

@DslMarker
internal annotation class PDFVedleggMarker