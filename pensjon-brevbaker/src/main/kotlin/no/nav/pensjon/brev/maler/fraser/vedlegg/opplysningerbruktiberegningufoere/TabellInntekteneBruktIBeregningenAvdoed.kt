package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningUfoeretrygdSelectors.harFoerstegangstjenesteOpptjening_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningUfoeretrygdSelectors.harOmsorgsopptjening_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningUfoeretrygdSelectors.opptjeningsperiode
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
import no.nav.pensjon.brev.template.dsl.expression.*

// <> brevkode PE_UT_04_102, PE_UT_04_108, PE_UT_04_109, PE_UT_04_500, PE_UT_05_100, PE_UT_06_300, PE_UT_07_100, PE_UT_07_200,

data class TabellInntekteneBruktIBeregningenAvdoed(
    val opptjeningUfoeretrygd: Expression<OpplysningerBruktIBeregningUTDto.OpptjeningUfoeretrygd>,

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

        // TBU036V
        title1 {
            text(
                Bokmal to "Inntekt lagt til grunn for beregning av avdødes uføretrygd fra ",
                Nynorsk to "Inntekt lagd til grunn for berekning av avdødes uføretrygd frå ",
                English to "Income on which to calculate the disability benefit for the deceased from "
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
                ifNotNull(opptjeningUfoeretrygd) { opptjening ->
                        forEach(
                            opptjening.opptjeningsperiode
                        ) { periode ->
                            row {
                                cell {
                                    textExpr(
                                        Bokmal to periode.aar.format(),
                                        Nynorsk to periode.aar.format(),
                                        English to periode.aar.format()
                                    )
                                }
                                cell {
                                    textExpr(
                                        Bokmal to periode.pensjonsgivendeInntekt.format(),
                                        Nynorsk to periode.pensjonsgivendeInntekt.format(),
                                        English to periode.pensjonsgivendeInntekt.format()
                                    )
                                }
                                cell {
                                    textExpr(
                                        Bokmal to periode.justertPensjonsgivendeInntekt.format() + "**",
                                        Nynorsk to periode.justertPensjonsgivendeInntekt.format() + "**",
                                        English to periode.justertPensjonsgivendeInntekt.format() + "**"
                                    )
                                }
                                cell {

                                    showIf(
                                        periode.harFoerstegangstjenesteOpptjening
                                    ) {
                                        text(
                                            Bokmal to "Førstegangstjeneste* ",
                                            Nynorsk to "Førstegongsteneste* ",
                                            English to "Initial service* ",
                                        )
                                    }
                                    showIf(
                                        periode.harOmsorgsopptjening
                                    ) {
                                        text(
                                            Bokmal to "Omsorgsår* ",
                                            Nynorsk to "Omsorgsår* ",
                                            English to "Care work* "
                                        )
                                    }
                                    showIf(periode.harInntektAvtaleland) {
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
            }
        paragraph {
            showIf(
                opptjeningUfoeretrygd.harOmsorgsopptjening_safe.ifNull(false) and opptjeningUfoeretrygd.harFoerstegangstjenesteOpptjening_safe.ifNull(
                    false
                )
            ) {
                text(
                    Bokmal to "*) Markerer år med omsorgsopptjening og militær eller sivil førstegangstjeneste. Det skal ses bort fra år med pensjonsopptjening på grunnlag av omsorgsarbeid dersom dette er en fordel. Dersom inntekten i året før militær eller sivil førstegangstjeneste tok til er høyere, benyttes denne inntekten.",
                    Nynorsk to "*) Markerer år med omsorgsopptening og militær eller sivil førstegongsteneste. Ein skal sjå bort frå år med pensjonsopptening på grunnlag av omsorgsarbeid dersom dette er ein fordel. Dersom inntekta i året før militær eller sivil førstegongsteneste tok til, er høgare, blir denne inntekta brukt.",
                    English to "*) Indicates years when you earned pension points for care work or initial service, either military or civilian. If you stand to benefit from excluding years when you have earned pension points from care work, these years will be excluded. If the income in the year before your military or civilian initial service started is higher, this income will be used as the basis for calculation."
                )
            }
            showIf(
                opptjeningUfoeretrygd.harOmsorgsopptjening_safe.ifNull(false) and not(
                    opptjeningUfoeretrygd.harFoerstegangstjenesteOpptjening_safe.ifNull(
                        false
                    )
                )
            ) {
                text(
                    Bokmal to "*) Markerer år med omsorgsopptjening. Det skal ses bort fra år med pensjonsopptjening på grunnlag av omsorgsarbeid dersom dette er en fordel.",
                    Nynorsk to "*) Markerer år med omsorgsopptening. Ein skal sjå bort frå år med pensjonsopptening på grunnlag av omsorgsarbeid dersom dette er ein fordel.",
                    English to "*) Indicates years when you earned pension points for care work. If you stand to benefit from excluding years when you have earned pension points from care work, these years will be excluded."
                )
            }
            showIf(
                opptjeningUfoeretrygd.harFoerstegangstjenesteOpptjening_safe.ifNull(false) and not(
                    opptjeningUfoeretrygd.harOmsorgsopptjening_safe.ifNull(false)
                )
            ) {
                text(
                    Bokmal to "*) Markerer år med militær eller sivil førstegangstjeneste. Dersom inntekten i året før tjenesten tok til er høyere, benyttes denne inntekten.",
                    Nynorsk to "*) Markerer år med militær eller sivil førstegongsteneste. Dersom inntekta i året før tenesta tok til, er høgare, blir denne inntekta brukt.",
                    English to "* Indicates years when you earned pension points for military or civilian initial service. If the income in the year before your service started is higher, this income will be used as a basis for calculation."
                )
            }
            newline()
            text(
                Bokmal to "**) Inntekten er justert etter endringer i folketrygdens grunnbeløp.",
                Nynorsk to "**) Gjennomsnittleg norsk inntekt justert etter endringar i grunnbeløpet i folketrygda.",
                English to "**) Average Norwegian income adjusted in accordance with changes in the National Insurance basic amount."
            )
        }
    }
}
