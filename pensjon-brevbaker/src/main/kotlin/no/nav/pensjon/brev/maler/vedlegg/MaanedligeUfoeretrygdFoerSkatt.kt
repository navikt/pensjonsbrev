package no.nav.pensjon.brev.no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.expression.select
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import java.time.LocalDate

data class MaanedligeUfoeretrygdFoerSkattDto(
    val kravVirkningFraOgMed: LocalDate,
    val kravVirkningTilOgMed: LocalDate, // TODO: Gjør om til nullable
    val gjeldendeBeregnetUfoeretrygdPerMaaned: Int,
)

val maanedligeUfoeretrygdFoerSkatt = createAttachment<LangBokmalNynorskEnglish, MaanedligeUfoeretrygdFoerSkattDto>(
    title = newText(
        Bokmal to "Dette er din månedlige uføretrygd før skatt",
        Nynorsk to "Dette er den månadlege uføretrygda di før skatt",
        English to "This is your monthly disability benefit before tax",
    ),
) {
    paragraph {
        text(
            Bokmal to "Nedenfor ser du den månedlige uføretrygden din.",
            Nynorsk to "Nedanfor ser du den månadlege uføretrygda di.",
            English to "Below is a presentation of your monthly disability benefit.",
        )

        table {
            title {
                val virkningsDatoFraOgMed =
                    argument().select(MaanedligeUfoeretrygdFoerSkattDto::kravVirkningFraOgMed).format(short = true)
                val virkningsDatoTilOgMed =
                    argument().select(MaanedligeUfoeretrygdFoerSkattDto::kravVirkningTilOgMed).format(short = true)

                textExpr(
                    Bokmal to "Den månedlige uføretrygden fra ".expr() + virkningsDatoFraOgMed + " til " + virkningsDatoTilOgMed,
                    Nynorsk to "Den månadlege uføretrygda frå ".expr() + virkningsDatoFraOgMed + " til " + virkningsDatoTilOgMed,
                    English to "Your monthly disability benefit from ".expr() + virkningsDatoFraOgMed + " to " + virkningsDatoTilOgMed,
                )

                newline()

                val beregnetUfoeretrygd = argument().select(MaanedligeUfoeretrygdFoerSkattDto::gjeldendeBeregnetUfoeretrygdPerMaaned).format()

                textExpr(
                    Bokmal to "Folketrygdens grunnbeløp (G) benyttet i beregningen er ".expr() + beregnetUfoeretrygd,
                    Nynorsk to "Grunnbeløpet i folketrygda (G) nytta i utrekninga er ".expr() + beregnetUfoeretrygd,
                    English to "The National Insurance basic amount (G) applied in the calculation is NOK ".expr() + beregnetUfoeretrygd,
                )
            }
            row {
              cell {
                  text(
                  Bokmal to "",
                  Nynorsk to "",
                  English to "",
              )
              }
            }
        }
    }

}