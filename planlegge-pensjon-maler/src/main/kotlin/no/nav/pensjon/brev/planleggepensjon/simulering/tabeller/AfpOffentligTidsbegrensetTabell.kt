package no.nav.pensjon.brev.planleggepensjon.simulering.tabeller

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.planleggepensjon.simulering.TidsbegrensetOffentligAfp
import no.nav.pensjon.brev.planleggepensjon.simulering.TidsbegrensetOffentligAfpSelectors.afpTillegg
import no.nav.pensjon.brev.planleggepensjon.simulering.TidsbegrensetOffentligAfpSelectors.grunnpensjon
import no.nav.pensjon.brev.planleggepensjon.simulering.TidsbegrensetOffentligAfpSelectors.saertillegg
import no.nav.pensjon.brev.planleggepensjon.simulering.TidsbegrensetOffentligAfpSelectors.tilleggspensjon
import no.nav.pensjon.brev.planleggepensjon.simulering.TidsbegrensetOffentligAfpSelectors.totaltAfpBeloep
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.text

data class AfpOffentligTidsbegrensetTabell(
    val afp: Expression<TidsbegrensetOffentligAfp>,
) : OutlinePhrase<LangBokmal>() {
    override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
        paragraph {
            table(header = {
                column(columnSpan = 3) {
                    text(bokmal { +"AFP i offentlig sektor (tidsbegrenset)" })
                }
                column(alignment = RIGHT, columnSpan = 1) {
                    text(bokmal { +"Kr per måned" })
                }
            }) {
                showIf(afp.grunnpensjon.greaterThan(0)) {
                    row {
                        cell { text(bokmal { +"Grunnpensjon" }) }
                        cell { text(bokmal { +afp.grunnpensjon.format() }) }
                    }
                }
                showIf(afp.tilleggspensjon.greaterThan(0)) {
                    row {
                        cell { text(bokmal { +"Tilleggspensjon" }) }
                        cell { text(bokmal { +afp.tilleggspensjon.format() }) }
                    }
                }
                showIf(afp.afpTillegg.greaterThan(0)) {
                    row {
                        cell { text(bokmal { +"AFP-tillegg" }) }
                        cell { text(bokmal { +afp.afpTillegg.format() }) }
                    }
                }
                showIf(afp.saertillegg.greaterThan(0)) {
                    row {
                        cell { text(bokmal { +"Særtillegg" }) }
                        cell { text(bokmal { +afp.saertillegg.format() }) }
                    }
                }
                row {
                    cell { text(bokmal { +"Sum AFP" }, fontType = BOLD) }
                    cell { text(bokmal { +afp.totaltAfpBeloep.format() }, fontType = BOLD) }
                }
            }
        }
    }
}
