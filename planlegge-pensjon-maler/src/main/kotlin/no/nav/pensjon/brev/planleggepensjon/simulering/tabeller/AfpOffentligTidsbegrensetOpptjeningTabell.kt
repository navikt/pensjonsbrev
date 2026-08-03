package no.nav.pensjon.brev.planleggepensjon.simulering.tabeller

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.planleggepensjon.simulering.TidsbegrensetOffentligAfp
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.tidsbegrensetOffentligAfp.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text

data class AfpOffentligTidsbegrensetOpptjeningTabell(
    val afp: Expression<TidsbegrensetOffentligAfp>,
) : OutlinePhrase<LangBokmal>() {
    override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
        paragraph {
            table(header = {
                column(columnSpan = 3) {
                    text(bokmal { +"Opptjening AFP i offentlig sektor" })
                }
                column(alignment = RIGHT, columnSpan = 1) {
                    text(bokmal { +"" })
                }
            }) {
                row {
                    cell { text(bokmal { +"AFP grad" }) }
                    cell { text(bokmal { +afp.afpGrad.format() + " %" }) }
                }
                row {
                    cell { text(bokmal { +"Grunnbeløp (G)" }) }
                    cell { text(bokmal { +afp.grunnbeloep.format() }) }
                }
                row {
                    cell { text(bokmal { +"Tidligere arbeidsinntekt" }) }
                    cell { text(bokmal { +afp.tidligereArbeidsinntekt.format() }) }
                }
                row {
                    cell { text(bokmal { +"Sluttpoengtall" }) }
                    cell { text(bokmal { +afp.sluttpoengtall.format() }) }
                }
                row {
                    cell { text(bokmal { +"Poengår" }) }
                    cell { text(bokmal { +(afp.poengaarTom1991 + afp.poengaarFom1992).format() + " år" }) }
                }
                row {
                    cell { text(bokmal { +"Trygdetid" }) }
                    cell { text(bokmal { +afp.trygdetid.format() + " år" }) }
                }
                row {
                    cell { text(bokmal { +"Poengår før 1992 (45 %)" }) }
                    cell { text(bokmal { +afp.poengaarTom1991.format() + " av " + afp.trygdetid.format() + " år" }) }
                }
                row {
                    cell { text(bokmal { +"Poengår etter 1991 (42 %)" }) }
                    cell { text(bokmal { +afp.poengaarFom1992.format() + " av " + afp.trygdetid.format() + " år" }) }
                }
            }
        }
    }
}
