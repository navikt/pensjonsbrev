package no.nav.pensjon.brev.planleggepensjon.simulering.tabeller

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjon
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.simuleringV1MaanedligAlderspensjon.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.text

data class OpptjeningKapittel19Tabell(
    val alderspensjon: Expression<SimuleringV1MaanedligAlderspensjon>,
) : OutlinePhrase<LangBokmal>() {
    override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
        paragraph {
            table(header = {
                column(columnSpan = 3) {
                    text(bokmal { +"Opptjening etter kapittel 19" })
                }
                column(alignment = RIGHT, columnSpan = 1) {
                    text(bokmal { +"" })
                }
            }) {
                ifNotNull(alderspensjon.kapittel19AndelTeller) {
                    row {
                        cell { text(bokmal { +"Andelsbrøk" }) }
                        cell { text(bokmal { +it.format() + "/10" }) }
                    }
                }
                ifNotNull(alderspensjon.grunnbeloep) {
                    row {
                        cell { text(bokmal { +"Grunnbeløp (G)" }) }
                        cell { text(bokmal { +it.format() }) }
                    }
                }
                ifNotNull(alderspensjon.minstePensjonsnivaaBeloep) {
                    row {
                        cell { text(bokmal { +"Minste pensjonsnivå" }) }
                        cell { text(bokmal { +it.format() }) }
                    }
                }
                ifNotNull(alderspensjon.forholdstall) {
                    row {
                        cell { text(bokmal { +"Forholdstall ved uttak" }) }
                        cell { text(bokmal { +it.format(2) }) }
                    }
                }
                ifNotNull(alderspensjon.sluttpoengtall) {
                    row {
                        cell { text(bokmal { +"Sluttpoengtall" }) }
                        cell { text(bokmal { +it.format(2) }) }
                    }
                }
                ifNotNull(alderspensjon.kapittel19Trygdetid) {
                    row {
                        cell { text(bokmal { +"Trygdetid" }) }
                        cell { text(bokmal { +it.format() + " år" }) }
                    }
                }
                ifNotNull(alderspensjon.poengaarFom1992) {
                    row {
                        cell { text(bokmal { +"Poengår etter 1991" }) }
                        cell { text(bokmal { +it.format() }) }
                    }
                }
                ifNotNull(alderspensjon.poengaarTom1991) {
                    row {
                        cell { text(bokmal { +"Poengår før 1992" }) }
                        cell { text(bokmal { +it.format() }) }
                    }
                }
            }
        }
    }
}
