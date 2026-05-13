package no.nav.pensjon.brev.planleggepensjon.simulering.tabeller

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.planleggepensjon.simulering.PrivatAfp
import no.nav.pensjon.brev.planleggepensjon.simulering.PrivatAfpSelectors.kompensasjonstillegg
import no.nav.pensjon.brev.planleggepensjon.simulering.PrivatAfpSelectors.kronetillegg
import no.nav.pensjon.brev.planleggepensjon.simulering.PrivatAfpSelectors.livsvarig
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjon
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjonSelectors.garantipensjonBeloep
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjonSelectors.garantitilleggBeloep
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjonSelectors.gjenlevendetillegg
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjonSelectors.grunnpensjonBeloep
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjonSelectors.inntektspensjonBeloep
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjonSelectors.pensjonstillegg
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjonSelectors.skjermingstillegg
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjonSelectors.tilleggspensjonBeloep
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

data class SumTabell(
    val alderspensjon: Expression<SimuleringV1MaanedligAlderspensjon>,
    val privatAfp: Expression<PrivatAfp>,
) : OutlinePhrase<LangBokmal>() {
    override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
        paragraph {
            table(header = {
                column(columnSpan = 3) {
                    text(bokmal { +"Alderspensjon og AFP" })
                }
                column(alignment = RIGHT, columnSpan = 1) {
                    text(bokmal { +"Kr per måned" })
                }
            }) {
                row {
                    cell { text(bokmal { +"Alderspensjon" }) }
                    cell {
                        val sumAlderspensjon = alderspensjon.grunnpensjonBeloep.ifNull(Kroner(0)) +
                                alderspensjon.tilleggspensjonBeloep.ifNull(Kroner(0)) +
                                alderspensjon.pensjonstillegg.ifNull(Kroner(0)) +
                                alderspensjon.inntektspensjonBeloep.ifNull(Kroner(0)) +
                                alderspensjon.garantipensjonBeloep.ifNull(Kroner(0)) +
                                alderspensjon.garantitilleggBeloep.ifNull(Kroner(0)) +
                                alderspensjon.skjermingstillegg.ifNull(Kroner(0)) +
                                alderspensjon.gjenlevendetillegg.ifNull(Kroner(0))
                        text(bokmal { +sumAlderspensjon.format() })
                    }
                }
                row {
                    cell { text(bokmal { +"AFP i privat sektor" }) }
                    cell {
                        val sumAfp = privatAfp.kronetillegg.ifNull(Kroner(0)) +
                                privatAfp.livsvarig.ifNull(Kroner(0)) +
                                privatAfp.kompensasjonstillegg.ifNull(Kroner(0))
                        text(bokmal { +sumAfp.format() })
                    }
                }
                row {
                    cell { text(bokmal { +"Sum pensjon" }, fontType = BOLD) }
                    cell {
                        val sumAlderspensjon = alderspensjon.grunnpensjonBeloep.ifNull(Kroner(0)) +
                                alderspensjon.tilleggspensjonBeloep.ifNull(Kroner(0)) +
                                alderspensjon.pensjonstillegg.ifNull(Kroner(0)) +
                                alderspensjon.inntektspensjonBeloep.ifNull(Kroner(0)) +
                                alderspensjon.garantipensjonBeloep.ifNull(Kroner(0)) +
                                alderspensjon.garantitilleggBeloep.ifNull(Kroner(0)) +
                                alderspensjon.skjermingstillegg.ifNull(Kroner(0)) +
                                alderspensjon.gjenlevendetillegg.ifNull(Kroner(0))
                        val sumAfp = privatAfp.kronetillegg.ifNull(Kroner(0)) +
                                privatAfp.livsvarig.ifNull(Kroner(0)) +
                                privatAfp.kompensasjonstillegg.ifNull(Kroner(0))
                        val sumPensjon = sumAlderspensjon + sumAfp
                        text(bokmal { +sumPensjon.format() }, fontType = BOLD)
                    }
                }
            }
        }
    }
}
