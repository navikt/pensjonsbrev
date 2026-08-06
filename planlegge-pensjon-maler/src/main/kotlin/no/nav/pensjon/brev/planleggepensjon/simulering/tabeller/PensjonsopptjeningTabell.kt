package no.nav.pensjon.brev.planleggepensjon.simulering.tabeller

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.planleggepensjon.simulering.Pensjonsopptjening
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.pensjonsopptjening.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.text

data class PensjonsopptjeningTabell(
    val pensjonsopptjeningListe: Expression<List<Pensjonsopptjening>>,
) : OutlinePhrase<LangBokmal>() {
    override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
        paragraph {
            table(header = {
                column {
                    text(bokmal { +"År" })
                }
                column(columnSpan = 4, alignment = RIGHT) {
                    text(bokmal { +"Pensjonsgivende inntekt" })
                }
                column(columnSpan = 3, alignment = RIGHT) {
                    text(bokmal { +"Pensjonspoeng" })
                }
                column(columnSpan = 4, alignment = RIGHT) {
                    text(bokmal { +"Pensjonsbeholdning" })
                }
                column(columnSpan = 4) {
                    text(bokmal { +"Merknad" })
                }
            }) {
                forEach(pensjonsopptjeningListe) { rad ->
                    row {
                        cell { text(bokmal { +rad.aarstall.format() }) }
                        cell {
                            ifNotNull(rad.pensjonsgivendeInntekt) {
                                text(bokmal { +it.format(denominator = false) + " kr" })
                            }
                        }
                        cell {
                            ifNotNull(rad.pensjonspoeng) {
                                text(bokmal { +it.format(2) })
                            }
                        }
                        cell {
                            ifNotNull(rad.pensjonsbeholdning) {
                                text(bokmal { +it.format(denominator = false) + " kr" })
                            }
                        }
                        cell {
                            ifNotNull(rad.merknad) {
                                text(bokmal { +it })
                            }
                        }
                    }
                }
            }
        }
    }
}
