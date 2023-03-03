package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningUfoeretrygdSelectors.aar
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningUfoeretrygdSelectors.foerstegangstjenesteOpptjening
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningUfoeretrygdSelectors.inntektAvkortet
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningUfoeretrygdSelectors.inntektAvtaleland
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningUfoeretrygdSelectors.justertPensjonsgivendeInntekt
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningUfoeretrygdSelectors.omsorgsopptjening
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningUfoeretrygdSelectors.pensjonsgivendeInntekt
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.textExpr
import java.time.LocalDate
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType

// TODO: Forklaringstekster til tabellene
// TODO: Beloepsgrense <> 6000? Gjelder fremdeles? Tror ikke, ifølge Ingrid.
/* IF brevkode not(PE_UT_05_100, PE_UT_05_100, PE_UT_04_300, PE_UT_14_300, PE_UT_04_500, PE_UT_04_102)
OR beloepsgrense not(6000) AND kravAarsakType isNotOneOf(soknad_bt)
AND brevkode not(PE_UT_04_108, PE_UT_04_109, PE_UT_07_200, PE_UT_06_300) INCLUDE */

data class TabellInntekteneBruktIBeregningen(
    val beregningVirkningFraOgMed: Expression<LocalDate>,
    val opptjeningUfoeretrygd: Expression<List<OpplysningerBruktIBeregningUTDto.OpptjeningUfoeretrygd>>,

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {


        // TBU 036V
        title1 {
            text(
                Bokmal to "Inntekt lagt til grunn for beregning av uføretrygden din fra",
                Nynorsk to "Inntekt lagd til grunn for berekning av uføretrygda di frå",
                English to "Income on which to calculate your disbability benefit of",
            )
        }
        // TBU037V
        paragraph {
            table(
                header = {
                    column(alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.LEFT) {
                        text(
                            Bokmal to "År",
                            Nynorsk to "År",
                            English to "Year"

                        )
                    }
                    column(alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {
                        text(
                            Bokmal to "Pensjonsgivende inntekt",
                            Nynorsk to "Pensjonsgivande inntekt",
                            English to "Pensionable income"

                        )
                    }
                    column(alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {
                        text(
                            Bokmal to "Inntekt brukt i beregningen",
                            Nynorsk to "Inntekt brukt i berekninga",
                            English to "Income applied in the calculation"
                        )
                    }
                    column(alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {
                        text(
                            Bokmal to "Merknad",
                            Nynorsk to "Merknad",
                            English to "Comments"
                        )
                    }
                }
            ) {
                forEach(
                    opptjeningUfoeretrygd
                ) { opptjening ->
                    row {
                        cell {
                            textExpr(
                                Bokmal to opptjening.aar.format(),
                                Nynorsk to opptjening.aar.format(),
                                English to opptjening.aar.format()
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to opptjening.pensjonsgivendeInntekt.format(),
                                Nynorsk to opptjening.pensjonsgivendeInntekt.format(),
                                English to opptjening.pensjonsgivendeInntekt.format()
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to opptjening.justertPensjonsgivendeInntekt.format(),
                                Nynorsk to opptjening.justertPensjonsgivendeInntekt.format(),
                                English to opptjening.justertPensjonsgivendeInntekt.format()
                            )
                        }
                        cell {
                            showIf(
                                opptjening.foerstegangstjenesteOpptjening
                            ) {
                                text(
                                    Bokmal to "Førstegangstjeneste",
                                    Nynorsk to "Førstegongsteneste",
                                    English to "Initial service"
                                )
                            }
                            showIf(
                                opptjening.omsorgsopptjening
                            ) {
                                text(
                                    Bokmal to "Omsorgsår",
                                    Nynorsk to "Omsorgsår",
                                    English to "Care work"
                                )
                            }
                        }
                    }
                }

            }
        }
        // TBU038V
        paragraph {
            table(
                header = {
                    column(alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.LEFT) {
                        text(
                            Bokmal to "År",
                            Nynorsk to "År",
                            English to "Year"

                        )
                    }
                    column(alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {
                        text(
                            Bokmal to "Inntekt i utland",
                            Nynorsk to "Inntekt i utland",
                            English to "Income from abroard"
                        )
                    }
                    column(alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {
                        text(
                            Bokmal to "Pensjonsgivende inntekt",
                            Nynorsk to "Pensjonsgivande inntekt",
                            English to "Pensionable income"

                        )
                    }
                    column(alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {
                        text(
                            Bokmal to "Inntekt brukt i beregningen",
                            Nynorsk to "Inntekt brukt i berekninga",
                            English to "Income applied in the calculation"
                        )
                    }
                    column(alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {
                        text(
                            Bokmal to "Merknad",
                            Nynorsk to "Merknad",
                            English to "Comments"
                        )
                    }
                }
            ) {
                forEach(
                    opptjeningUfoeretrygd
                ) { opptjening ->
                    row {
                        cell {
                            textExpr(
                                Bokmal to opptjening.aar.format(),
                                Nynorsk to opptjening.aar.format(),
                                English to opptjening.aar.format()
                            )
                        }
                        cell {
                            val harInntektAvtaleland = opptjening.inntektAvtaleland
                            textExpr(
                                Bokmal to ifElse(harInntektAvtaleland, ifTrue = "Ja", ifFalse = "Nei"),
                                Nynorsk to ifElse(harInntektAvtaleland, ifTrue = "Ja", ifFalse = "Nei"),
                                English to ifElse(harInntektAvtaleland, ifTrue = "Yes", ifFalse = "No")
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to opptjening.pensjonsgivendeInntekt.format(),
                                Nynorsk to opptjening.pensjonsgivendeInntekt.format(),
                                English to opptjening.pensjonsgivendeInntekt.format()
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to opptjening.inntektAvkortet.format(),
                                Nynorsk to opptjening.inntektAvkortet.format(),
                                English to opptjening.inntektAvkortet.format()
                            )
                        }
                        cell {
                            showIf(
                                opptjening.foerstegangstjenesteOpptjening
                            ) {
                                text(
                                    Bokmal to "Førstegangstjeneste",
                                    Nynorsk to "Førstegongsteneste",
                                    English to "Initial service"
                                )
                            }
                            showIf(
                                opptjening.omsorgsopptjening
                            ) {
                                text(
                                    Bokmal to "Omsorgsår",
                                    Nynorsk to "Omsorgsår",
                                    English to "Care work"
                                )
                            }
                        }
                    }
                }

            }

        }
    }
}