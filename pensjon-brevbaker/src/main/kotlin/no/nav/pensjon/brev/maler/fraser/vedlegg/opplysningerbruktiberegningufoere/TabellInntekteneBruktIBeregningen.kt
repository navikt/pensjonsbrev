package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.ufoer.Gjenlevendetillegg
import no.nav.pensjon.brev.maler.fraser.ufoer.Ufoeretrygd
import no.nav.pensjon.brev.model.tableFormat
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import java.time.LocalDate


/* IF brevkode not(PE_UT_05_100, PE_UT_05_100, PE_UT_04_300, PE_UT_14_300, PE_UT_04_500, PE_UT_04_102)
OR beloepsgrense not(6000) AND kravAarsakType isNotOneOf(soknad_bt)
AND brevkode not(PE_UT_04_108, PE_UT_04_109, PE_UT_07_200, PE_UT_06_300) INCLUDE */

data class TabellInntekteneBruktIBeregningen(
    val beregningVirkningFraOgMed: Expression<LocalDate>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        // TBU 036V
        title1 {
            text(
                Bokmal to "Dette er inntektene vi har brukt i beregningen din",
                Nynorsk to "Dette er inntektene vi har brukt i berekninga di",
                English to "This is the income on which we have based your calculations"

            )
        }

// TBU037V
        paragraph {
            table(
                header = {
                    column(4) {
                        text(
                            Bokmal to "Inntekt lagt til grunn for beregning av uføretrygden din fra",
                            Nynorsk to "Inntekt lagd til grunn for berekning av uføretrygda di frå",
                            English to "Income on which to calculate your disbability benefit of",
                            Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
                        )
                    }
                    column(alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {
                    }
                }
            ) {
            row {
                cell {
                    text(
                        Bokmal to "År",
                        Nynorsk to "År",
                        English to "Year"
                    )
                }
                cell {
                    text(
                        Bokmal to "Pensjonsgivende inntekt",
                        Nynorsk to "Pensjonsgivande inntekt",
                        English to "Pensionable income"
                    )
                }
                cell {
                    text(
                        Bokmal to "Inntekt justert med folketrygdens grunnbeløp",
                        Nynorsk to "Inntekt justert med grunnbeløpet i folketrygda",
                        English to "Income adjusted in accordance with the National Insurance basic amount"
                    )
                }
                cell {
                    text(
                        Bokmal to "Merknad",
                        Nynorsk to "Merknad",
                        English to "Comments"
                    )
                }
            }
                row {
                    cell {

                    }
                }


            }
        }
    }
}