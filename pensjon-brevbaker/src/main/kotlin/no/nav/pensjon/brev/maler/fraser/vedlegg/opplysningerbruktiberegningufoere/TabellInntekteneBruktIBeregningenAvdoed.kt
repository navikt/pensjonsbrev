package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningUfoeretrygdAvdoedSelectors.harFoerstegangstjenesteOpptjeningAvdoed_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningUfoeretrygdAvdoedSelectors.harOmsorgsopptjeningAvdoed_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningUfoeretrygdAvdoedSelectors.opptjeningsperioderAvdoed
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningsperiodeAvdoedSelectors.aarAvdoed
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningsperiodeAvdoedSelectors.harFoerstegangstjenesteOpptjeningAvdoed
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningsperiodeAvdoedSelectors.harInntektAvtalelandAvdoed
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningsperiodeAvdoedSelectors.harOmsorgsopptjeningAvdoed
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningsperiodeAvdoedSelectors.justertPensjonsgivendeInntektAvdoed
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningsperiodeAvdoedSelectors.pensjonsgivendeInntektAvdoed
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


data class TabellInntekteneBruktIBeregningenAvdoed(
    val beregningGjeldendeFraOgMed: Expression<LocalDate>,
    val opptjeningUfoeretrygdAvdoed: Expression<OpplysningerBruktIBeregningUTDto.OpptjeningUfoeretrygdAvdoed?>,

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

        // TBU036V
        title1 {
            textExpr(
                Bokmal to "Inntekt lagt til grunn for beregning av avdødes uføretrygd fra ".expr() + beregningGjeldendeFraOgMed.format(),
                Nynorsk to "Inntekt lagd til grunn for berekning av avdødes uføretrygd frå ".expr() + beregningGjeldendeFraOgMed.format(),
                English to "Income on which to calculate the disability benefit for the deceased from ".expr() + beregningGjeldendeFraOgMed.format()
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
                ifNotNull(opptjeningUfoeretrygdAvdoed) { opptjeningUfoeretrygdAvdoed ->
                        forEach(
                            opptjeningUfoeretrygdAvdoed.opptjeningsperioderAvdoed
                        ) { opptjeningAvdoed ->
                            row {
                                cell {
                                    textExpr(
                                        Bokmal to opptjeningAvdoed.aarAvdoed.format(),
                                        Nynorsk to opptjeningAvdoed.aarAvdoed.format(),
                                        English to opptjeningAvdoed.aarAvdoed.format()
                                    )
                                }
                                cell {
                                    textExpr(
                                        Bokmal to opptjeningAvdoed.pensjonsgivendeInntektAvdoed.format(),
                                        Nynorsk to opptjeningAvdoed.pensjonsgivendeInntektAvdoed.format(),
                                        English to opptjeningAvdoed.pensjonsgivendeInntektAvdoed.format()
                                    )
                                }
                                cell {
                                    textExpr(
                                        Bokmal to opptjeningAvdoed.justertPensjonsgivendeInntektAvdoed.format() + "**",
                                        Nynorsk to opptjeningAvdoed.justertPensjonsgivendeInntektAvdoed.format() + "**",
                                        English to opptjeningAvdoed.justertPensjonsgivendeInntektAvdoed.format() + "**"
                                    )
                                }
                                cell {

                                    showIf(
                                        opptjeningAvdoed.harFoerstegangstjenesteOpptjeningAvdoed
                                    ) {
                                        text(
                                            Bokmal to "Førstegangstjeneste* ",
                                            Nynorsk to "Førstegongsteneste* ",
                                            English to "Initial service* ",
                                        )
                                    }
                                    showIf(
                                        opptjeningAvdoed.harOmsorgsopptjeningAvdoed
                                    ) {
                                        text(
                                            Bokmal to "Omsorgsår* ",
                                            Nynorsk to "Omsorgsår* ",
                                            English to "Care work* "
                                        )
                                    }
                                    showIf(opptjeningAvdoed.harInntektAvtalelandAvdoed) {
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
                opptjeningUfoeretrygdAvdoed.harOmsorgsopptjeningAvdoed_safe.ifNull(false) and opptjeningUfoeretrygdAvdoed.harFoerstegangstjenesteOpptjeningAvdoed_safe.ifNull(
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
                opptjeningUfoeretrygdAvdoed.harOmsorgsopptjeningAvdoed_safe.ifNull(false) and not(
                    opptjeningUfoeretrygdAvdoed.harFoerstegangstjenesteOpptjeningAvdoed_safe.ifNull(
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
                opptjeningUfoeretrygdAvdoed.harFoerstegangstjenesteOpptjeningAvdoed_safe.ifNull(false) and not(
                    opptjeningUfoeretrygdAvdoed.harOmsorgsopptjeningAvdoed_safe.ifNull(false)
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
