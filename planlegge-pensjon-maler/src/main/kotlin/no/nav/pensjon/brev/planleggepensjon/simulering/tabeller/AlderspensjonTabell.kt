package no.nav.pensjon.brev.planleggepensjon.simulering.tabeller

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjon
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.simuleringV1MaanedligAlderspensjon.*
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerType

data class AlderspensjonTabell(
    val alderspensjon: Expression<SimuleringV1MaanedligAlderspensjon>,
) : OutlinePhrase<LangBokmal>() {
    override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
        paragraph {
            table(header = {
                column(columnSpan = 3) {
                    text(bokmal { +"100 % alderspensjon" })
                }
                column(alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT, columnSpan = 1) {
                    text(bokmal { +"Kr per måned" })
                }
            }) {
                ifNotNull(alderspensjon.grunnpensjonBeloep) {
                    showIf(it.greaterThan(0)) {
                        row {
                            cell { text(bokmal { +"Grunnpensjon (kap. 19)" }) }
                            cell { text(bokmal { +it.format() }) }
                        }
                    }
                }
                ifNotNull(alderspensjon.tilleggspensjonBeloep) {
                    showIf(it.greaterThan(0)) {
                        row {
                            cell { text(bokmal { +"Tilleggspensjon (kap. 19)" }) }
                            cell { text(bokmal { +it.format() }) }
                        }
                    }
                }
                ifNotNull(alderspensjon.pensjonstillegg) {
                    showIf(it.greaterThan(0)) {
                        row {
                            cell { text(bokmal { +"Pensjonstillegg (kap. 19)" }) }
                            cell { text(bokmal { +it.format() }) }
                        }
                    }
                }
                ifNotNull(alderspensjon.gjenlevendetillegg) {
                    row {
                        cell { text(bokmal { +"Gjenlevendetillegg (kap. 19)" }) }
                        cell { text(bokmal { +it.format() }) }
                    }
                }
                ifNotNull(alderspensjon.inntektspensjonBeloep) {
                    showIf(it.greaterThan(0)) {
                        row {
                            cell { text(bokmal { +"Inntektspensjon (kap. 20)" }) }
                            cell { text(bokmal { +it.format() }) }
                        }
                    }
                }
                ifNotNull(alderspensjon.garantipensjonBeloep) {
                    showIf(it.greaterThan(0)) {
                        row {
                            cell { text(bokmal { +"Garantipensjon (kap. 20)" }) }
                            cell { text(bokmal { +it.format() }) }
                        }
                    }
                }
                ifNotNull(alderspensjon.garantitilleggBeloep) {
                    showIf(it.greaterThan(0)) {
                        row {
                            cell { text(bokmal { +"Garantitillegg (kap. 20)" }) }
                            cell { text(bokmal { +it.format() }) }
                        }
                    }
                }
                ifNotNull(alderspensjon.skjermingstillegg) {
                    showIf(it.greaterThan(0)) {
                        row {
                            cell { text(bokmal { +"Skjermingstillegg" }) }
                            cell { text(bokmal { +it.format() }) }
                        }
                    }
                }
                // Sum alderspensjon
                row {
                    cell { text(bokmal { +"Sum alderspensjon" }, fontType = Element.OutlineContent.ParagraphContent.Text.FontType.BOLD) }
                    cell {
                        val sum = alderspensjon.grunnpensjonBeloep.ifNull(BrevbakerType.Kroner(0)) +
                                alderspensjon.tilleggspensjonBeloep.ifNull(BrevbakerType.Kroner(0)) +
                                alderspensjon.pensjonstillegg.ifNull(BrevbakerType.Kroner(0)) +
                                alderspensjon.inntektspensjonBeloep.ifNull(BrevbakerType.Kroner(0)) +
                                alderspensjon.garantipensjonBeloep.ifNull(BrevbakerType.Kroner(0)) +
                                alderspensjon.garantitilleggBeloep.ifNull(BrevbakerType.Kroner(0)) +
                                alderspensjon.skjermingstillegg.ifNull(BrevbakerType.Kroner(0)) +
                                alderspensjon.gjenlevendetillegg.ifNull(BrevbakerType.Kroner(0))
                        text(bokmal { +sum.format() }, fontType = Element.OutlineContent.ParagraphContent.Text.FontType.BOLD)
                    }
                }
            }
        }
    }
}