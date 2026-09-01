package no.nav.pensjon.brev.planleggepensjon.simulering.tabeller

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.planleggepensjon.simulering.TidsbegrensetOffentligAfp
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.tidsbegrensetOffentligAfp.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.RedigerbarOutlinePhrase
import no.nav.pensjon.brev.template.RedigerbarPhraseBrevdata
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text

data class AfpOffentligTidsbegrensetOpptjeningTabell(
    val afp: Expression<TidsbegrensetOffentligAfp>,
) : RedigerbarOutlinePhrase<LangBokmal>() {
    override fun OutlineOnlyScope<LangBokmal, RedigerbarPhraseBrevdata>.template() {
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
                    cell { text(bokmal { +redigerbarData(afp.afpGrad.format()) + " %" }) }
                }
                row {
                    cell { text(bokmal { +"Tidligere arbeidsinntekt" }) }
                    cell { text(bokmal { +redigerbarData(afp.tidligereArbeidsinntekt.format(denominator = false)) + " kr" }) }
                }
                row {
                    cell { text(bokmal { +"Grunnbeløp (G)" }) }
                    cell { text(bokmal { +redigerbarData(afp.grunnbeloep.format(denominator = false)) + " kr" }) }
                }
                row {
                    cell { text(bokmal { +"Sluttpoengtall" }) }
                    cell { text(bokmal { +redigerbarData(afp.sluttpoengtall.format()) }) }
                }
                row {
                    cell { text(bokmal { +"Trygdetid" }) }
                    cell { text(bokmal { +redigerbarData(afp.trygdetid.format()) + " år" }) }
                }
                row {
                    cell { text(bokmal { +"Poengår" }) }
                    cell { text(bokmal { +redigerbarData((afp.poengaarTom1991 + afp.poengaarFom1992).format()) + " år" }) }
                }
                row {
                    cell { text(bokmal { +"Poengår før 1992 (45 %)" }) }
                    cell { text(bokmal { +redigerbarData(afp.poengaarTom1991.format()) + " år" }) }
                }
                row {
                    cell { text(bokmal { +"Poengår etter 1991 (42 %)" }) }
                    cell { text(bokmal { +redigerbarData(afp.poengaarFom1992.format()) + " år" }) }
                }
            }
        }
    }
}
