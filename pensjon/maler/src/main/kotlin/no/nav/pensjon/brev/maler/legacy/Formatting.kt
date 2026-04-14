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

class HjemmelFormatter(private val avsluttMedOg: Boolean) : LocalizedFormatter<Collection<String>>() {
    override fun apply(first: Collection<String>, second: Language): String {
        return formatterHjemler(first)
    }

    private fun formatterHjemler(hjemmeltekster: Collection<String>): String {
        data class Hjemmel(val del1: Int, val del2: Int, val hjemmeltekst: String)

        if (hjemmeltekster.isEmpty()) return ""

        val hjemler = hjemmeltekster
            .map { hjemmeltekst -> val (del1, del2) = hjemmeltekst.split("-"); Hjemmel(del1.toInt(), del2.toInt(), hjemmeltekst) }
            .sortedWith(compareBy({ it.del1 }, { it.del2 }))

        val gruppeAvSammeDel1 = mutableListOf<MutableList<Hjemmel>>()
        var currentHjemmelgruppe = mutableListOf(hjemler[0])

        for (i in 1 until hjemler.size) {
            val previous = hjemler[i - 1]
            val current = hjemler[i]
            if (current.del1 == previous.del1 && current.del2 == previous.del2 + 1) {
                currentHjemmelgruppe.add(current)
            } else {
                gruppeAvSammeDel1.add(currentHjemmelgruppe)
                currentHjemmelgruppe = mutableListOf(current)
            }
        }
        gruppeAvSammeDel1.add(currentHjemmelgruppe)

        val prefix = if (hjemmeltekster.size == 1) "§ " else "§§ "
        val hjemmeltekstPerGruppe = gruppeAvSammeDel1.map { group ->
            if (group.size == 1) group.first().hjemmeltekst
            else "${group.first().hjemmeltekst} til ${group.last().hjemmeltekst}"
        }
        return prefix + if (avsluttMedOg && hjemmeltekstPerGruppe.size > 1) {
            hjemmeltekstPerGruppe.dropLast(1).joinToString(", ") + " og " + hjemmeltekstPerGruppe.last()
        } else {
            hjemmeltekstPerGruppe.joinToString(", ")
        }
    }

    override fun stableHashCode(): Int = "HjemmelFormatter($avsluttMedOg)".hashCode()
}