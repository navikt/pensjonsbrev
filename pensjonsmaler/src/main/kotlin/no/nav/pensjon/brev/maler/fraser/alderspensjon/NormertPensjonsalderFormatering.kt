package no.nav.pensjon.brev.maler.fraser.alderspensjon

import no.nav.pensjon.brev.api.model.maler.alderApi.NormertPensjonsalder
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.LocalizedFormatter
import no.nav.pensjon.brev.template.dsl.expression.format

fun Expression<NormertPensjonsalder>.aarOgMaanederFormattert() = format(NormertPensjonsalderFormatter)

object NormertPensjonsalderFormatter : LocalizedFormatter<NormertPensjonsalder>() {
    override fun apply(first: NormertPensjonsalder, second: Language): String {
        val aar = first.aar
        val maaneder = first.maaneder
        return when (second) {
            Bokmal -> "$aar år" + if (maaneder > 0) " og $maaneder måneder" else ""
            Nynorsk -> "$aar år" + if (maaneder > 0) " og $maaneder månadar" else ""
            English -> "$aar years" + if (maaneder > 0) " and $maaneder months" else ""
        }
    }

    override fun stableHashCode(): Int = "NormertPensjonsalderFormatter".hashCode()
}