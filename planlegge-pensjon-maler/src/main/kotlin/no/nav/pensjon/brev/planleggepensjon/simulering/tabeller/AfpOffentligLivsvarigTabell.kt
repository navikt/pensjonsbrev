package no.nav.pensjon.brev.planleggepensjon.simulering.tabeller

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.planleggepensjon.simulering.LivsvarigOffentligAfp
import no.nav.pensjon.brev.planleggepensjon.simulering.LivsvarigOffentligAfpSelectors.maanedligBeloep
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

data class AfpOffentligLivsvarigTabell(
    val afp: Expression<LivsvarigOffentligAfp>,
) : OutlinePhrase<LangBokmal>() {
    override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
        paragraph {
            table(header = {
                column(columnSpan = 3) {
                    text(bokmal { +"AFP i offentlig sektor (livsvarig)" })
                }
                column(alignment = RIGHT, columnSpan = 1) {
                    text(bokmal { +"Kr per måned" })
                }
            }) {
                row {
                    cell { text(bokmal { +"Livsvarig AFP" }) }
                    cell { text(bokmal { +afp.maanedligBeloep.format() }) }
                }
                row {
                    cell { text(bokmal { +"Sum AFP" }, fontType = BOLD) }
                    cell { text(bokmal { +afp.maanedligBeloep.format() }, fontType = BOLD) }
                }
            }
        }
    }
}
