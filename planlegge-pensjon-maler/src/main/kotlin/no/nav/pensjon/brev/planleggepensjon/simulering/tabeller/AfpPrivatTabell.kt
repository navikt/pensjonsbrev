package no.nav.pensjon.brev.planleggepensjon.simulering.tabeller

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.planleggepensjon.simulering.PrivatAfp
import no.nav.pensjon.brev.planleggepensjon.simulering.PrivatAfpSelectors.kompensasjonstillegg
import no.nav.pensjon.brev.planleggepensjon.simulering.PrivatAfpSelectors.kronetillegg
import no.nav.pensjon.brev.planleggepensjon.simulering.PrivatAfpSelectors.livsvarig
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text

data class AfpPrivatTabell(
    val privatAfp: Expression<PrivatAfp>,
) : OutlinePhrase<LangBokmal>() {
    override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
        paragraph {
            table(header = {
                column(columnSpan = 3) {
                    text(bokmal { +"AFP i privat sektor" })
                }
                column(alignment = RIGHT, columnSpan = 1) {
                    text(bokmal { +"Kr per måned" })
                }
            }) {
                showIf(privatAfp.kompensasjonstillegg.greaterThan(0)) {
                    row {
                        cell { text(bokmal { +"Kompensasjonstillegg" }) }
                        cell { text(bokmal { +privatAfp.kompensasjonstillegg.format() }) }
                    }
                }
                showIf(privatAfp.kronetillegg.greaterThan(0)) {
                    row {
                        cell { text(bokmal { +"Kronetillegg" }) }
                        cell { text(bokmal { +privatAfp.kronetillegg.format() }) }
                    }
                }
                showIf(privatAfp.livsvarig.greaterThan(0)) {
                    row {
                        cell { text(bokmal { +"Livsvarig del" }) }
                        cell { text(bokmal { +privatAfp.livsvarig.format() }) }
                    }
                }
                // Sum privat AFP
                row {
                    cell { text(bokmal { +"Sum AFP" }, fontType = BOLD) }
                    cell {
                        val sum = privatAfp.kompensasjonstillegg +
                                privatAfp.kronetillegg +
                                privatAfp.livsvarig
                        text(bokmal { +sum.format() }, fontType = BOLD)
                    }
                }
            }
        }
    }
}
