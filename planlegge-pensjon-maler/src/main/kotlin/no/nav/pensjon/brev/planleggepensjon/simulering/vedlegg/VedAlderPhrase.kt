package no.nav.pensjon.brev.planleggepensjon.simulering.vedlegg

import no.nav.pensjon.brev.planleggepensjon.simulering.Uttaksinformasjon
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.alder.aar
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.alder.maaneder
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.uttaksinformasjon.alder
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.uttaksinformasjon.uttaksdato
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.PlainTextOnlyPhrase
import no.nav.pensjon.brev.template.dsl.PlainTextOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text

data class VedAlderPhrase(
    val informasjon: Expression<Uttaksinformasjon>,
) : PlainTextOnlyPhrase<LangBokmal>() {
    override fun PlainTextOnlyScope<LangBokmal, Unit>.template() {
        text(bokmal { +"Ved " + informasjon.alder.aar.format() + " år" })
        showIf(informasjon.alder.maaneder greaterThan 1) {
            text(bokmal { +" og " + informasjon.alder.maaneder.format() + " måneder" })
        }.orShowIf(informasjon.alder.maaneder greaterThan 0) {
            text(bokmal { +" og 1 måned" })
        }
        text(bokmal { +" (" + informasjon.uttaksdato + ")" })
    }
}
