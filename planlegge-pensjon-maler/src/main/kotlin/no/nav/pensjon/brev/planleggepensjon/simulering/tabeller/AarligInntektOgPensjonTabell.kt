package no.nav.pensjon.brev.planleggepensjon.simulering.tabeller

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.planleggepensjon.simulering.AarligInntektOgPensjon
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.aarligInntektOgPensjon.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text

data class AarligInntektOgPensjonTabell(
    val aarligInntektOgPensjonListe: Expression<List<AarligInntektOgPensjon>>,
) : OutlinePhrase<LangBokmal>() {
    override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
        paragraph {
            table(header = {
                column(columnSpan = 1) {
                    text(bokmal { +"Alder" })
                }
                column(columnSpan = 1, alignment = RIGHT) {
                    text(bokmal { +"Alderspensjon" })
                }
                column(columnSpan = 1, alignment = RIGHT) {
                    text(bokmal { +"Avtalefestet pensjon" })
                }
                column(columnSpan = 1, alignment = RIGHT) {
                    text(bokmal { +"Pensjonsgivende inntekt" })
                }
                column(columnSpan = 1, alignment = RIGHT) {
                    text(bokmal { +"Sum kr per år" })
                }
            }) {
                forEach(aarligInntektOgPensjonListe) { rad ->
                    row {
                        cell { text(bokmal { +rad.alderLabel }) }
                        cell { text(bokmal { +rad.alderspensjon.format() }) }
                        cell { text(bokmal { +rad.avtalefestetPensjon.format() }) }
                        cell { text(bokmal { +rad.pensjonsgivendeInntekt.format() }) }
                        cell {
                            val sum = rad.alderspensjon + rad.avtalefestetPensjon + rad.pensjonsgivendeInntekt
                            text(bokmal { +sum.format() }, fontType = BOLD)
                        }
                    }
                }
            }
        }
    }
}
