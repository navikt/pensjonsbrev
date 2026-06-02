package no.nav.pensjon.brev.planleggepensjon.simulering.tabeller

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjon
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjonSelectors.delingstall
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjonSelectors.garantipensjonBeloep
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjonSelectors.garantipensjonsnivaaBeloep
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjonSelectors.kapittel20AndelTeller
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjonSelectors.kapittel20Trygdetid
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjonSelectors.pensjonsbeholdningEtterUttakBeloep
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjonSelectors.pensjonsbeholdningFoerUttakBeloep
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.text

data class OpptjeningKapittel20Tabell(
    val alderspensjon: Expression<SimuleringV1MaanedligAlderspensjon>,
) : OutlinePhrase<LangBokmal>() {
    override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
        paragraph {
            table(header = {
                column(columnSpan = 3) {
                    text(bokmal { +"Opptjening etter kapittel 20" })
                }
                column(alignment = RIGHT, columnSpan = 1) {
                    text(bokmal { +"" })
                }
            }) {
                ifNotNull(alderspensjon.kapittel20AndelTeller) {
                    row {
                        cell { text(bokmal { +"Andelsbrøk" }) }
                        cell { text(bokmal { +it.format() + "/10" }) }
                    }
                }
                ifNotNull(alderspensjon.delingstall) {
                    row {
                        cell { text(bokmal { +"Delingstall ved uttak" }) }
                        cell { text(bokmal { +it.format(2) }) }
                    }
                }
                ifNotNull(alderspensjon.garantipensjonsnivaaBeloep) {
                    row {
                        cell { text(bokmal { +"Garantipensjonsnivå" }) }
                        cell { text(bokmal { +it.format() }) }
                    }
                }
                ifNotNull(alderspensjon.garantipensjonBeloep) {
                    row {
                        cell { text(bokmal { +"Garantipensjon" }) }
                        cell { text(bokmal { +it.format() }) }
                    }
                }
                ifNotNull(alderspensjon.pensjonsbeholdningFoerUttakBeloep) {
                    row {
                        cell { text(bokmal { +"Pensjonsbeholdning før uttak" }) }
                        cell { text(bokmal { +it.format() }) }
                    }
                }
                ifNotNull(alderspensjon.pensjonsbeholdningEtterUttakBeloep) {
                    row {
                        cell { text(bokmal { +"Pensjonsbeholdning etter uttak" }) }
                        cell { text(bokmal { +it.format() }) }
                    }
                }
                ifNotNull(alderspensjon.kapittel20Trygdetid) {
                    row {
                        cell { text(bokmal { +"Trygdetid" }) }
                        cell { text(bokmal { +it.format() + " år" }) }
                    }
                }
            }
        }
    }
}
