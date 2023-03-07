package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningUfoeretrygdSelectors.harFoerstegangstjenesteOpptjening
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningUfoeretrygdSelectors.harOmsorgsopptjening
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningUfoeretrygdSelectors.opptjeningsperioder
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningsperiodeSelectors.aar
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningsperiodeSelectors.harFoerstegangstjenesteOpptjening
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningsperiodeSelectors.harInntektAvtaleland
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningsperiodeSelectors.harOmsorgsopptjening
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningsperiodeSelectors.justertPensjonsgivendeInntekt
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningsperiodeSelectors.pensjonsgivendeInntekt
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
// TODO: Inkluder avkortetBeloep når beregningsMetode != FOLKETRYGD istedenfor justertBeloep når beregningsMetode = FOLKETRYGD

data class TabellInntekteneBruktIBeregningen(
    val beregningGjeldendeFraOgMed: Expression<LocalDate>,
    val harAvdoed: Expression<Boolean>,
    val opptjeningUfoeretrygd: Expression<OpplysningerBruktIBeregningUTDto.OpptjeningUfoeretrygd>,

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

        // TBU 036V

        title1 {
            textExpr(
                Bokmal to "Inntekt lagt til grunn for beregning av ".expr() +
                        ifElse(
                            harAvdoed,
                            ifTrue = "avdødes uføretrygd",
                            ifFalse = "uføretrygden din"
                        ) + " fra ".expr() + beregningGjeldendeFraOgMed.format(),
                Nynorsk to "Inntekt lagd til grunn for berekning av ".expr() +
                        ifElse(
                            harAvdoed,
                            ifTrue = "avdødes uføretrygd",
                            ifFalse = "uføretrygda di"
                        ) + " frå ".expr() + beregningGjeldendeFraOgMed.format(),
                English to "Income on which to calculate ".expr() +
                        ifElse(
                            harAvdoed,
                            ifTrue = "the disability benefit for the deceased",
                            ifFalse = "your disability benefit"
                        ) + " from ".expr() + beregningGjeldendeFraOgMed.format()
            )
        }
        // TBU037V
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
                forEach(
                    opptjeningUfoeretrygd.opptjeningsperioder
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
                                opptjening.harFoerstegangstjenesteOpptjening
                            ) {
                                text(
                                    Bokmal to "Førstegangstjeneste* ",
                                    Nynorsk to "Førstegongsteneste* ",
                                    English to "Initial service* ",
                                )
                            }
                            showIf(
                                opptjening.harOmsorgsopptjening
                            ) {
                                text(Bokmal to "Omsorgsår* ", Nynorsk to "Omsorgsår* ", English to "Care work* ")
                            }
                            showIf(opptjening.harInntektAvtaleland) {
                                text(
                                    Bokmal to "Inntekt i utland* ",
                                    Nynorsk to "Inntekt i utland* ",
                                    English to "Income from abroad* "
                                )
                            }
                        }
                    }
                }
            }
        }

        paragraph {
            showIf(opptjeningUfoeretrygd.harFoerstegangstjenesteOpptjening and opptjeningUfoeretrygd.harOmsorgsopptjening) {
                text(
                    Bokmal to "År med omsorgsopptjening og militær eller sivil førstegangstjeneste: Det skal ses bort fra år med pensjonsopptjening på grunnlag av omsorgsarbeid dersom dette er en fordel. Dersom inntekten i året før militær eller sivil førstegangstjeneste tok til er høyere, benyttes denne inntekten.",
                    Nynorsk to "År med omsorgsopptening og militær eller sivil førstegongsteneste: Ein skal sjå bort frå år med pensjonsopptening på grunnlag av omsorgsarbeid dersom dette er ein fordel. Dersom inntekta i året før militær eller sivil førstegongsteneste tok til, er høgare, blir denne inntekta brukt.",
                    English to "Years when you earned pension points for care work or initial service, either military or civilian: If you stand to benefit from excluding years when you have earned pension points from care work, these years will be excluded. If the income in the year before your military or civilian initial service started is higher, this income will be used as the basis for calculation."
                )
            }
            showIf(opptjeningUfoeretrygd.harOmsorgsopptjening and not(opptjeningUfoeretrygd.harFoerstegangstjenesteOpptjening)) {
                text(
                    Bokmal to "År med omsorgsopptjening: Det skal ses bort fra år med pensjonsopptjening på grunnlag av omsorgsarbeid dersom dette er en fordel.",
                    Nynorsk to "År med omsorgsopptening: Ein skal sjå bort frå år med pensjonsopptening på grunnlag av omsorgsarbeid dersom dette er ein fordel.",
                    English to "Years when you earned pension points for care work: If you stand to benefit from excluding years when you have earned pension points from care work, these years will be excluded."
                )
            }
            showIf(opptjeningUfoeretrygd.harFoerstegangstjenesteOpptjening and not(opptjeningUfoeretrygd.harOmsorgsopptjening)) {
                text(
                    Bokmal to "År med militær eller sivil førstegangstjeneste: Dersom inntekten i året før tjenesten tok til er høyere, benyttes denne inntekten.",
                    Nynorsk to "År med militær eller sivil førstegongsteneste: Dersom inntekta i året før tenesta tok til, er høgare, blir denne inntekta brukt.",
                    English to "Years when you earned pension points for military or civilian initial service: If the income in the year before your service started is higher, this income will be used as a basis for calculation."
                )
            }
        }
    }
}
