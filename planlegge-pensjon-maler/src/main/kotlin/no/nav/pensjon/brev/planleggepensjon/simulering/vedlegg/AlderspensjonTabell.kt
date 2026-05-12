package no.nav.pensjon.brev.planleggepensjon.simulering.vedlegg

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjon
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjonSelectors.basispensjonBeloep
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjonSelectors.beloep
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjonSelectors.garantipensjonBeloep
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjonSelectors.garantitilleggBeloep
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjonSelectors.gjenlevendetillegg
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjonSelectors.grunnpensjonBeloep
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjonSelectors.inntektspensjonBeloep
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjonSelectors.pensjonstillegg
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjonSelectors.restpensjonBeloep
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjonSelectors.skjermingstillegg
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjonSelectors.tilleggspensjonBeloep
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

data class AlderspensjonTabell(
    val alderspensjon: Expression<SimuleringV1MaanedligAlderspensjon>,
) : OutlinePhrase<LangBokmal>() {
    override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
        paragraph {
            table(header = {
                column(columnSpan = 3) {
                    text(bokmal { +"100 % alderspensjon" })
                }
                column(alignment = RIGHT, columnSpan = 1) {
                    text(bokmal { +"Kr per måned" })
                }
            }) {
                ifNotNull(alderspensjon.grunnpensjonBeloep) {
                    row {
                        cell { text(bokmal { +"Grunnpensjon (kap. 19)" }) }
                        cell { text(bokmal { +it.format() }) }
                    }
                }
                ifNotNull(alderspensjon.tilleggspensjonBeloep) {
                    row {
                        cell { text(bokmal { +"Tilleggspensjon (kap. 19)" }) }
                        cell { text(bokmal { +it.format() }) }
                    }
                }
                ifNotNull(alderspensjon.pensjonstillegg) {
                    row {
                        cell { text(bokmal { +"Pensjonstillegg (kap. 19)" }) }
                        cell { text(bokmal { +it.format() }) }
                    }
                }
                ifNotNull(alderspensjon.inntektspensjonBeloep) {
                    row {
                        cell { text(bokmal { +"Inntektspensjon (kap. 20)" }) }
                        cell { text(bokmal { +it.format() }) }
                    }
                }
                ifNotNull(alderspensjon.garantipensjonBeloep) {
                    row {
                        cell { text(bokmal { +"Garantipensjon (kap. 20)" }) }
                        cell { text(bokmal { +it.format() }) }
                    }
                }
                ifNotNull(alderspensjon.garantitilleggBeloep) {
                    row {
                        cell { text(bokmal { +"Garantitillegg" }) }
                        cell { text(bokmal { +it.format() }) }
                    }
                }
                ifNotNull(alderspensjon.skjermingstillegg) {
                    row {
                        cell { text(bokmal { +"Skjermingstillegg" }) }
                        cell { text(bokmal { +it.format() }) }
                    }
                }
                ifNotNull(alderspensjon.basispensjonBeloep) {
                    row {
                        cell { text(bokmal { +"Basispensjon" }) }
                        cell { text(bokmal { +it.format() }) }
                    }
                }
                ifNotNull(alderspensjon.restpensjonBeloep) {
                    row {
                        cell { text(bokmal { +"Restpensjon" }) }
                        cell { text(bokmal { +it.format() }) }
                    }
                }
                ifNotNull(alderspensjon.gjenlevendetillegg) {
                    row {
                        cell { text(bokmal { +"Gjenlevendetillegg" }) }
                        cell { text(bokmal { +it.format() }) }
                    }
                }
                // Sum alderspensjon
                row {
                    cell { text(bokmal { +"Sum alderspensjon" }) }
                    cell { text(bokmal { +alderspensjon.beloep.format() }) }
                }
            }
        }
    }
}

