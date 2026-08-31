package no.nav.pensjon.brev.planleggepensjon.simulering.tabeller

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.planleggepensjon.simulering.TidsbegrensetOffentligAfp
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.tidsbegrensetOffentligAfp.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.RedigerbarOutlinePhrase
import no.nav.pensjon.brev.template.RedigerbarPhraseBrevdata
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.text

data class AfpOffentligTidsbegrensetTabellRedigerbar(
    val afp: Expression<TidsbegrensetOffentligAfp>,
) : RedigerbarOutlinePhrase<LangBokmal>() {
    override fun OutlineOnlyScope<LangBokmal, RedigerbarPhraseBrevdata>.template() {
        paragraph {
            table(header = {
                column(columnSpan = 3) {
                    text(bokmal { +"AFP i offentlig sektor" })
                }
                column(alignment = RIGHT, columnSpan = 1) {
                    text(bokmal { +"Kr per måned" })
                }
            }) {
                showIf(afp.grunnpensjon.greaterThan(0)) {
                    row {
                        cell { text(bokmal { +"Grunnpensjon" }) }
                        cell { text(bokmal { +redigerbarData(afp.grunnpensjon.format(denominator = false)) }) }
                    }
                }
                showIf(afp.tilleggspensjon.greaterThan(0)) {
                    row {
                        cell { text(bokmal { +"Tilleggspensjon" }) }
                        cell { text(bokmal { +redigerbarData(afp.tilleggspensjon.format(denominator = false)) }) }
                    }
                }
                showIf(afp.afpTillegg.greaterThan(0)) {
                    row {
                        cell { text(bokmal { +"AFP-tillegg" }) }
                        cell { text(bokmal { +redigerbarData(afp.afpTillegg.format(denominator = false)) }) }
                    }
                }
                showIf(afp.saertillegg.greaterThan(0)) {
                    row {
                        cell { text(bokmal { +"Særtillegg" }) }
                        cell { text(bokmal { +redigerbarData(afp.saertillegg.format(denominator = false)) }) }
                    }
                }
                row {
                    cell { text(bokmal { +"Sum AFP" }, fontType = BOLD) }
                    cell { text(bokmal { +redigerbarData(afp.totaltAfpBeloep.format(denominator = false)) }, fontType = BOLD) }
                }
            }
            showIf(afp.erAvkortet) {
                text(bokmal { +"Sum redusert pga. total pensjon oversteg 70 % av tidligere inntekt." })
            }
        }
    }
}
