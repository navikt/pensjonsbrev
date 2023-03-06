package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningUfoeretrygdSelectors.aar
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningUfoeretrygdSelectors.harBeregningsmetodeFolketrygd
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningUfoeretrygdSelectors.harFoerstegangstjenesteOpptjening
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningUfoeretrygdSelectors.harInntektAvtaleland
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningUfoeretrygdSelectors.harOmsorgsopptjening
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningUfoeretrygdSelectors.inntektAvkortet
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningUfoeretrygdSelectors.justertPensjonsgivendeInntekt
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningUfoeretrygdSelectors.pensjonsgivendeInntekt
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.textExpr
import java.time.LocalDate
import no.nav.pensjon.brev.template.dsl.expression.*

// TODO: Forklaringstekster til tabellene
// TODO: Beloepsgrense <> 6000? Gjelder fremdeles? Tror ikke, ifølge Ingrid.
/* IF brevkode not(PE_UT_05_100, PE_UT_05_100, PE_UT_04_300, PE_UT_14_300, PE_UT_04_500, PE_UT_04_102)
OR beloepsgrense not(6000) AND kravAarsakType isNotOneOf(soknad_bt)
AND brevkode not(PE_UT_04_108, PE_UT_04_109, PE_UT_07_200, PE_UT_06_300) INCLUDE */

data class TabellInntekteneBruktIBeregningen(
    val beregningVirkningFraOgMed: Expression<LocalDate>,
    val harAvdoed: Expression<Boolean>,
    val harKravaarsakSoeknadBT: Expression<Boolean>,
    val opptjeningUfoeretrygd: Expression<List<OpplysningerBruktIBeregningUTDto.OpptjeningUfoeretrygd>>,

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        forEach(
            opptjeningUfoeretrygd
        ) { opptjening ->
            // TBU 036V
            showIf(not(harKravaarsakSoeknadBT)) {

                title1 {
                    textExpr(
                        Bokmal to "Inntekt lagt til grunn for beregning av ".expr() +
                                ifElse(harAvdoed, ifTrue = "avdødes uføretrygd", ifFalse = "uføretrygden din") + " fra".expr(),
                        Nynorsk to "Inntekt lagd til grunn for berekning av ".expr() +
                                ifElse(harAvdoed, ifTrue = "avdødes uføretrygd", ifFalse = "uføretrygda di") + " frå".expr(),
                        English to "Income on which to calculate ".expr() +
                                ifElse(harAvdoed, ifTrue = "the disability benefit for the deceased", ifFalse = "your disability benefit") + " from".expr()
                    )
                }
                // TBU037V
                showIf(opptjening.harBeregningsmetodeFolketrygd and not(opptjening.harInntektAvtaleland)) {
                    paragraph {
                        table(header = {
                            column(alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.LEFT) {
                                text(
                                    Bokmal to "År", Nynorsk to "År", English to "Year"

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
                                    Bokmal to "Merknad", Nynorsk to "Merknad", English to "Comments"
                                )
                            }
                        }) {
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
                                        opptjening.harFoerstegangstjenesteOpptjening
                                    ) {
                                        text(
                                            Bokmal to "Førstegangstjeneste",
                                            Nynorsk to "Førstegongsteneste",
                                            English to "Initial service"
                                        )
                                    }
                                    showIf(
                                        opptjening.harOmsorgsopptjening
                                    ) {
                                        text(
                                            Bokmal to "Omsorgsår", Nynorsk to "Omsorgsår", English to "Care work"
                                        )
                                    }
                                }
                            }
                        }

                    }
                }


                // TBU038V
                showIf(not(opptjening.harBeregningsmetodeFolketrygd) and opptjening.harInntektAvtaleland) {
                    paragraph {
                        table(header = {
                            column(alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.LEFT) {
                                text(
                                    Bokmal to "År", Nynorsk to "År", English to "Year"

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
                                    Bokmal to "Merknad", Nynorsk to "Merknad", English to "Comments"
                                )
                            }
                        }) {
                            row {
                                cell {
                                    textExpr(
                                        Bokmal to opptjening.aar.format(),
                                        Nynorsk to opptjening.aar.format(),
                                        English to opptjening.aar.format()
                                    )
                                }
                                cell {
                                    val harInntektAvtaleland = opptjening.harInntektAvtaleland
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
                                        opptjening.harFoerstegangstjenesteOpptjening
                                    ) {
                                        text(
                                            Bokmal to "Førstegangstjeneste",
                                            Nynorsk to "Førstegongsteneste",
                                            English to "Initial service"
                                        )
                                    }
                                    showIf(
                                        opptjening.harOmsorgsopptjening
                                    ) {
                                        text(
                                            Bokmal to "Omsorgsår", Nynorsk to "Omsorgsår", English to "Care work"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                paragraph {
                    showIf(opptjening.harFoerstegangstjenesteOpptjening and opptjening.harOmsorgsopptjening) {
                        text(
                            Bokmal to "År med omsorgsopptjening og militær eller sivil førstegangstjeneste: Det skal ses bort fra år med pensjonsopptjening på grunnlag av omsorgsarbeid dersom dette er en fordel. Dersom inntekten i året før militær eller sivil førstegangstjeneste tok til er høyere, benyttes denne inntekten.",
                            Nynorsk to "År med omsorgsopptening og militær eller sivil førstegongsteneste: Ein skal sjå bort frå år med pensjonsopptening på grunnlag av omsorgsarbeid dersom dette er ein fordel. Dersom inntekta i året før militær eller sivil førstegongsteneste tok til, er høgare, blir denne inntekta brukt.",
                            English to "Years when you earned pension points for care work or initial service, either military or civilian: If you stand to benefit from excluding years when you have earned pension points from care work, these years will be excluded. If the income in the year before your military or civilian initial service started is higher, this income will be used as the basis for calculation."
                        )
                    }
                    showIf(opptjening.harOmsorgsopptjening and not(opptjening.harFoerstegangstjenesteOpptjening)) {
                        text(
                            Bokmal to "År med omsorgsopptjening: Det skal ses bort fra år med pensjonsopptjening på grunnlag av omsorgsarbeid dersom dette er en fordel.",
                            Nynorsk to "År med omsorgsopptening: Ein skal sjå bort frå år med pensjonsopptening på grunnlag av omsorgsarbeid dersom dette er ein fordel.",
                            English to "Years when you earned pension points for care work: If you stand to benefit from excluding years when you have earned pension points from care work, these years will be excluded."
                        )
                    }
                    showIf(opptjening.harFoerstegangstjenesteOpptjening and not(opptjening.harOmsorgsopptjening)) {
                        text(
                            Bokmal to "År med militær eller sivil førstegangstjeneste: Dersom inntekten i året før tjenesten tok til er høyere, benyttes denne inntekten.",
                            Nynorsk to "År med militær eller sivil førstegongsteneste: Dersom inntekta i året før tenesta tok til, er høgare, blir denne inntekta brukt.",
                            English to "Years when you earned pension points for military or civilian initial service: If the income in the year before your service started is higher, this income will be used as a basis for calculation."
                        )
                    }
                }
            }
        }
    }
}
