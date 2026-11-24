package no.nav.pensjon.brev.planleggepensjon.simulering.tabeller

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.planleggepensjon.simulering.LivsvarigOffentligAfp
import no.nav.pensjon.brev.planleggepensjon.simulering.Uttaksinformasjon
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.livsvarigOffentligAfp.*
import no.nav.pensjon.brev.planleggepensjon.simulering.vedlegg.VedAlderPhrase
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

data class LivsvarigOffentligAfpSeksjon(
    val afp: Expression<LivsvarigOffentligAfp?>,
    val beskrivelse: Expression<String?>,
    val uttaksinformasjon: Expression<Uttaksinformasjon>,
) : OutlinePhrase<LangBokmal>() {
    override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
        title2 {
            text(bokmal { +"Livsvarig AFP i offentlig sektor" })
        }
        ifNotNull(afp) { beloep ->
            title3 {
                includePhrase(VedAlderPhrase(uttaksinformasjon))
            }
            paragraph {
                table(header = {
                    column(columnSpan = 3) {
                        text(bokmal { +"AFP i offentlig sektor" })
                    }
                    column(alignment = RIGHT, columnSpan = 1) {
                        text(bokmal { +"Kr per måned" })
                    }
                }) {
                    row {
                        cell { text(bokmal { +"Livsvarig AFP" }) }
                        cell { text(bokmal { +beloep.maanedligBeloep.format(denominator = false) }) }
                    }
                    row {
                        cell { text(bokmal { +"Sum AFP" }, fontType = BOLD) }
                        cell { text(bokmal { +beloep.maanedligBeloep.format(denominator = false) }, fontType = BOLD) }
                    }
                }
            }
        }
        ifNotNull(beskrivelse) { tekst ->
            paragraph {
                text(bokmal { +tekst })
            }
        }
    }
}
