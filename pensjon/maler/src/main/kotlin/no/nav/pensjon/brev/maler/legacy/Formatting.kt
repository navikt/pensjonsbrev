package no.nav.pensjon.brev.maler.legacy

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
        val dato = first.fodselsdato.format(dateFormatter(second, FormatStyle.LONG))
        val periodetekst = first.tom?.let { " i perioden fra  ${first.fom.format(second)} til ${it.format(second)}" } ?: ""
        val periodetekstNn = first.tom?.let { " i perioden frå  ${first.fom.format(second)} til ${it.format(second)}" } ?: ""
        return when (second) {
            Bokmal -> "barn født ${dato}${periodetekst}"
            Nynorsk -> "barn fødd ${dato}${periodetekstNn}"
            English -> throw Exception()
        }
    }
    override fun stableHashCode(): Int = "BarnetilleggFormatter".hashCode()

    private fun LocalDate.format(lang: Language) = this.format(dateFormatter(lang, FormatStyle.LONG))
}