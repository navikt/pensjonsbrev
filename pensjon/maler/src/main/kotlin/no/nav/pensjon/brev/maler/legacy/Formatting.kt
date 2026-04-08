package no.nav.pensjon.brev.maler.legacy

import no.nav.pensjon.brev.api.model.maler.legacy.Tillegg
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.BarnetilleggUTDto
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.LocalizedFormatter
import no.nav.pensjon.brev.template.dateFormatter
import java.time.LocalDate
import java.time.format.FormatStyle

object BarnetilleggFormatter : LocalizedFormatter<BarnetilleggUTDto>() {
    override fun apply(first: BarnetilleggUTDto, second: Language): String {
        val antall = antallTekst(first.antallBarn)
        val dato = first.fodselsdato.format(dateFormatter(second, FormatStyle.LONG))
        val periodetekst = first.tom?.let { " i perioden fra  ${first.fom.format(second)} til ${it.format(second)}" } ?: ""
        val periodetekstNn = first.tom?.let { " i perioden frå  ${first.fom.format(second)} til ${it.format(second)}" } ?: ""
        return when (second) {
            Bokmal -> "${antall}barn født ${dato}${periodetekst}"
            Nynorsk -> "${antall}barn fødd ${dato}${periodetekstNn}"
            English -> throw Exception()
        }
    }

    private fun antallTekst(antall: Int) =
        when (antall) {
            1 -> ""
            2 -> "to "
            3 -> "tre "
            4 -> "fire "
            5 -> "fem "
            6 -> "seks "
            7 -> "sju "
            8 -> "åtte "
            9 -> "ni "
            else -> "$antall "
        }

    override fun stableHashCode(): Int = "BarnetilleggFormatter".hashCode()

    private fun LocalDate.format(lang: Language) = this.format(dateFormatter(lang, FormatStyle.LONG))
}

object UTOgTilleggMapper : LocalizedFormatter<Collection<Tillegg>>() {
    override fun apply(first: Collection<Tillegg>, second: Language): String {
        val tillegg = first.map {
            when (second) {
                Bokmal -> it.bokmal
                Nynorsk -> it.nynorsk
                English -> throw Exception()
            }.lowercase()
        }
        return CollectionFormat.apply(listOf("uføretrygd") + tillegg, second)
    }

    override fun stableHashCode(): Int = "UTOgTilleggMapper".hashCode()
}