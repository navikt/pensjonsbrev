package no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AttachmentTemplate
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.absoluteValue
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.BrevDTO
import no.nav.pensjon.etterlatte.maler.BrevDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.fraser.common.KronerText
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerUtbetalingDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerUtbetalingDTOSelectors.avviksBeloep
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerUtbetalingDTOSelectors.faktiskStoenad
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerUtbetalingDTOSelectors.stoenadUtbetalt
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.BeregningsVedleggDataSelectors.etteroppgjoersAar
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.BeregningsVedleggDataSelectors.grunnlag
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.BeregningsVedleggDataSelectors.utbetalingData
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.EtteroppgjoerGrunnlagDTOSelectors.afp
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.EtteroppgjoerGrunnlagDTOSelectors.inntekt
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.EtteroppgjoerGrunnlagDTOSelectors.loennsinntekt
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.EtteroppgjoerGrunnlagDTOSelectors.naeringsinntekt
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.EtteroppgjoerGrunnlagDTOSelectors.utlandsinntekt
import java.time.YearMonth

data class BeregningsVedleggData(
    override val innhold: List<no.nav.pensjon.etterlatte.maler.Element>,
    val etteroppgjoersAar: Int,
    val utbetalingData : EtteroppgjoerUtbetalingDTO,
    val grunnlag: EtteroppgjoerGrunnlagDTO,
) : BrevDTO

data class EtteroppgjoerGrunnlagDTO(
    val fom: YearMonth,
    val tom: YearMonth,
    val innvilgedeMaaneder: Int,
    val loennsinntekt: Kroner,
    val naeringsinntekt: Kroner,
    val afp: Kroner,
    val utlandsinntekt: Kroner,
    val inntekt: Kroner
)

@TemplateModelHelpers
val beregningsVedlegg: AttachmentTemplate<LangBokmalNynorskEnglish, BeregningsVedleggData> =
    createAttachment(
        title =
        newText(
            Bokmal to "Opplysninger om etteroppgjøret",
            Nynorsk to "Opplysningar om etteroppgjeret",
            English to "Information concerning final settlement",
        ),
        includeSakspart = false,
    ) {
        opplysningerOmEtteroppgjoer(argument.etteroppgjoersAar)
        hvaDuFikkUtbetalt(argument.etteroppgjoersAar, argument.utbetalingData)
        omBeregningAvOmstillingsstoenad(argument.etteroppgjoersAar)
        dinPensjonsgivendeInntekt(argument.etteroppgjoersAar, argument.grunnlag)
        konverterElementerTilBrevbakerformat(argument.innhold)
        inntektBruktIBeregningenAvOms(argument.etteroppgjoersAar, argument.grunnlag)
    }

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, BeregningsVedleggData>.opplysningerOmEtteroppgjoer(
    etteroppgjoersAar: Expression<Int>
) {
    paragraph {
        textExpr(
            Bokmal to "Omstillingsstønaden din ble beregnet ut fra inntekten du oppga som forventet i ".expr() + etteroppgjoersAar.format() +". Vi har nå gjort en ny beregning basert på opplysninger fra Skatteetaten og a-ordningen om din faktiske inntekt for "+etteroppgjoersAar.format()+". Du kan se skatteoppgjøret ditt på skatteetaten.no.",
            Nynorsk to "Omstillingsstønaden din blei rekna ut på grunnlag av det du oppgav som forventa inntekt i ".expr() + etteroppgjoersAar.format() +". Vi har no gjort ei ny utrekning av den faktiske inntekta di for "+etteroppgjoersAar.format()+" basert på opplysningar frå Skatteetaten og a-ordninga. Du kan sjå skatteoppgjeret ditt på skatteetaten.no.",
            English to "Your adjustment allowance was calculated based on your expected income you stated in ".expr() + etteroppgjoersAar.format() +". We have now carried out a new calculation based on information provided by the Tax Administration and A-scheme regarding your actual income for "+etteroppgjoersAar.format()+". You can see your tax settlement at: skatteetaten.no.",
        )
    }
    paragraph {
        text(
            Bokmal to "Husk at du må melde fra til oss innen tre uker hvis du mener beregningene er feil.",
            Nynorsk to "Hugs at du må melde frå til oss innan tre veker dersom du meiner at utrekningane ikkje stemmer.",
            English to "Remember that you must notify us within three weeks if you believe that the calculations are incorrect.",
        )
    }
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, BeregningsVedleggData>.hvaDuFikkUtbetalt(
    etteroppgjoersAar: Expression<Int>,
    utbetalingData: Expression<EtteroppgjoerUtbetalingDTO>
) {
    title2 {
        textExpr(
            Bokmal to "Hva du fikk utbetalt og hva du skulle fått utbetalt i ".expr() + etteroppgjoersAar.format(),
            Nynorsk to "Kva du fekk utbetalt og kva du skulle ha fått utbetalt i ".expr() + etteroppgjoersAar.format(),
            English to "What you were paid, and what you should have been paid in ".expr() + etteroppgjoersAar.format(),
        )
    }

    paragraph {
        table(
            header = {
                column(1) {
                    text(
                        Language.Bokmal to "Type stønad",
                        Language.Nynorsk to "Type stønad",
                        Language.English to "Type of allowance",
                    )
                }
                column(1) {
                    text(
                        Language.Bokmal to "Dette skulle du fått",
                        Language.Nynorsk to "Dette skulle du fått",
                        Language.English to "Amount you should have received ",
                    )
                }
                column(1) {
                    text(
                        Language.Bokmal to "Dette fikk du",
                        Language.Nynorsk to "Dette fekk du",
                        Language.English to "Amount you received",
                    )
                }
                column(1) {
                    text(
                        Language.Bokmal to "Avviksbeløp",
                        Language.Nynorsk to "Avvikande beløp",
                        Language.English to "Difference, sum",
                    )
                }
            }
        ) {
            row {
                cell { text(
                    Language.Bokmal to "Omstillingsstønad",
                    Language.Nynorsk to "Omstillingsstønad",
                    Language.English to "Adjustment allowance",
                ) }

                cell { includePhrase(KronerText(utbetalingData.faktiskStoenad)) }
                cell { includePhrase(KronerText(utbetalingData.stoenadUtbetalt)) }
                cell { includePhrase(KronerText(utbetalingData.avviksBeloep)) }
            }
        }
    }

    // ingen avvik
    showIf(utbetalingData.avviksBeloep.absoluteValue().equalTo(0)) {
        paragraph {
            textExpr(
                Bokmal to "Tabellen viser at du har fått utbetalt riktig stønad i ".expr() + etteroppgjoersAar.format(),
                Nynorsk to "Tabellen viser at du har fått utbetalt rett stønad i ".expr() + etteroppgjoersAar.format(),
                English to "The table shows that you have been paid the correct amount of allowance in ".expr() + etteroppgjoersAar.format(),
            )
        }
    }
    // avvik
    .orShow {
        paragraph {
            textExpr(
                Bokmal to "Du fikk utbetalt ".expr() + utbetalingData.avviksBeloep.absoluteValue().format() + " kroner for " + ifElse(utbetalingData.avviksBeloep.greaterThan(0), "mye", "lite") + " i " + etteroppgjoersAar.format() + " inkludert skatt.",
                Nynorsk to "Du fekk utbetalt ".expr() + utbetalingData.avviksBeloep.absoluteValue().format() + " kroner for " + ifElse(utbetalingData.avviksBeloep.greaterThan(0), "mykje", "lite") + " i " + etteroppgjoersAar.format() + " inkludert skatt.",
                English to "You received NOK ".expr() + utbetalingData.avviksBeloep.absoluteValue().format() + " too " + ifElse(utbetalingData.avviksBeloep.greaterThan(0), "much", "little") + " in " + etteroppgjoersAar.format() + " including tax.",
            )
        }
    }
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, BeregningsVedleggData>.omBeregningAvOmstillingsstoenad(
    etteroppgjoersAar: Expression<Int>
) {
    title2 {
        textExpr(
            Bokmal to "Om beregningen av omstillingsstønad for ".expr() + etteroppgjoersAar.format(),
            Nynorsk to "Om utrekninga av omstillingsstønad for ".expr() + etteroppgjoersAar.format(),
            English to "About the calculation of adjustment allowance for ".expr() + etteroppgjoersAar.format(),
        )
    }

    paragraph {
        text(
            Bokmal to "Din pensjonsgivende inntekt avgjør hvor mye du får i omstillingsstønad. Dette står i § 3‑15 i folketrygdloven.",
            Nynorsk to "Den pensjonsgivande inntekta di avgjer kor mykje du får i omstillingsstønad. Dette går fram av § 3‑15 i folketrygdlova.",
            English to "Your pensionable income determines how much you receive in adjustment allowance. This is pursuant to Section 3-15 in the National Insurance Act.",
        )
    }

    paragraph {
        text(
            Bokmal to "Pensjonsgivende inntekt inkluderer blant annet:",
            Nynorsk to "Pensjonsgivande inntekt inkluderer mellom anna følgjande:",
            English to "Pensionable income includes:",
        )

        list {
            item {
                text(
                    Bokmal to "brutto lønnsinntekt, inkludert feriepenger, fra alle norske arbeidsgivere",
                    Nynorsk to "brutto lønsinntekt, inkludert feriepengar, frå alle norske arbeidsgivarar",
                    English to "Gross wage income, including holiday pay, from all Norwegian employers",
                )
            }
            item {
                text(
                    Bokmal to "næringsinntekt og inntekt fra salg av næringsvirksomhet",
                    Nynorsk to "næringsinntekt og inntekt frå sal av næringsverksemd",
                    English to "Income from self-employment and income from sales of business activities",
                )
            }
            item {
                text(
                    Bokmal to "styregodtgjørelse og andre godtgjørelser",
                    Nynorsk to "styregodtgjersle og andre godtgjersler",
                    English to "Director’s emoluments and other fees paid",
                )
            }
            item {
                text(
                    Bokmal to "royalties",
                    Nynorsk to "royalties",
                    English to "Royalties",
                )
            }
            item {
                text(
                    Bokmal to "dagpenger, sykepenger og arbeidsavklaringspenger",
                    Nynorsk to "dagpengar, sjukepengar og arbeidsavklaringspengar",
                    English to "Unemployment benefits, sick pay and work assessment allowance",
                )
            }
            item {
                text(
                    Bokmal to "svangerskapspenger og foreldrepenger",
                    Nynorsk to "svangerskapspengar og foreldrepengar",
                    English to "Maternity allowance and parental allowance",
                )
            }
            item {
                text(
                    Bokmal to "omsorgsstønad",
                    Nynorsk to "omsorgsstønad",
                    English to "Care benefit",
                )
            }
            item {
                text(
                    Bokmal to "inntekt som fosterforelder",
                    Nynorsk to "inntekt som fosterforelder",
                    English to "Income as a foster parent",
                )
            }
            item {
                text(
                    Bokmal to "omstillingsstønad",
                    Nynorsk to "omstillingsstønad",
                    English to "Adjustment allowance",
                )
            }
        }
    }

    paragraph {
        text(
            Bokmal to "Følgende pensjonsgivende inntekter kan trekkes fra i beregningen av omstillingsstønad:",
            Nynorsk to "Følgjande pensjonsgivande inntekter kan trekkjast frå i utrekninga av omstillingsstønad:",
            English to "The following pensionable incomes can be deducted in the calculation of adjustment allowance:",
        )

        list {
            item {
                text(
                    Bokmal to "omstillingsstønad (blir automatisk trukket fra)",
                    Nynorsk to "omstillingsstønad (blir trekt frå automatisk)",
                    English to "Adjustment allowance (automatically deducted)",
                )
            }
            item {
                text(
                    Bokmal to "inntekt for periode(r) før du fikk innvilget omstillingsstønad",
                    Nynorsk to "inntekt før du fekk innvilga omstillingsstønad",
                    English to "Income for the period/periods before you were granted adjustment allowance",
                )
            }
            item {
                text(
                    Bokmal to "inntekt for periode(r) etter at omstillingsstønaden opphørte",
                    Nynorsk to "inntekt etter at omstillingsstønaden blei avvikla",
                    English to "Income for the period/periods after your adjustment allowance ended",
                )
            }
            item {
                text(
                    Bokmal to "etterbetaling av andre ytelser du har mottatt fra Nav for perioder før du fikk innvilget omstillingsstønad",
                    Nynorsk to "etterbetaling av andre ytingar du fekk frå Nav før omstillingsstønaden blei innvilga",
                    English to "Post-payment of other benefits you have received from Nav for periods before you were granted adjustment allowance",
                )
            }
        }
    }


}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, BeregningsVedleggData>.dinPensjonsgivendeInntekt(
    etteroppgjoersAar: Expression<Int>,
    grunnlag: Expression<EtteroppgjoerGrunnlagDTO>
) {

    title2 {
        text(
            Bokmal to "Din pensjonsgivende inntekt i innvilget periode",
            Nynorsk to "Den pensjonsgivande inntekta di i innvilga periode",
            English to "Your pensionable income during the period you were granted adjustment allowance",
        )
    }

    paragraph {
        textExpr(
            Bokmal to "I ".expr() + etteroppgjoersAar.format()+ " var din pensjonsgivende inntekt " + grunnlag.inntekt.format() + " kroner inkludert skatt, i følge opplysninger fra Skatteetaten og a-ordningen. Den fordeler seg slik:",
            Nynorsk to "Ifølgje opplysningar frå Skatteetaten og a-ordninga hadde du ei pensjonsgivande inntekt på ".expr() + grunnlag.inntekt.format() + " kroner inkludert skatt i " + etteroppgjoersAar.format()+ ".  Inntekta fordeler seg slik:",
            English to "In ".expr() + etteroppgjoersAar.format()+ " your pensionable income was NOK " + grunnlag.inntekt.format() + " including tax, according to information obtained from the Tax Administration and A-scheme. This is distributed as follows: ",
        )
    }

    paragraph {
        table(
            header = {
                column(1) {
                    text(
                        Language.Bokmal to "Type inntekt",
                        Language.Nynorsk to "Type inntekt",
                        Language.English to "Type of income ",
                    )
                }
                column(1, alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {
                    text(
                        Language.Bokmal to "Beløp",
                        Language.Nynorsk to "Beløp",
                        Language.English to "Amount ",
                    )
                }
            }
        ) {
            row {
                cell { text(
                    Language.Bokmal to "Lønnsinntekt",
                    Language.Nynorsk to "Lønnsinntekt",
                    Language.English to "Wage income",
                ) }
                cell { includePhrase(KronerText(grunnlag.loennsinntekt)) }
            }
            row {
                cell { text(
                    Language.Bokmal to "AFP",
                    Language.Nynorsk to "AFP",
                    Language.English to "AFP (contractual pension)",
                ) }
                cell { includePhrase(KronerText(grunnlag.afp)) }
            }
            row {
                cell { text(
                    Language.Bokmal to "Næringsinntekt",
                    Language.Nynorsk to "Næringsinntekt",
                    Language.English to "Income from self-employment",
                ) }
                cell { includePhrase(KronerText(grunnlag.naeringsinntekt)) }
            }
            row {
                cell { text(
                    Language.Bokmal to "Utlandsinntekt",
                    Language.Nynorsk to "Utlandsinntekt",
                    Language.English to "Foreign income",
                ) }
                cell { includePhrase(KronerText(grunnlag.utlandsinntekt)) }
            }

            row {
                cell { text(
                    Language.Bokmal to "Sum",
                    Language.Nynorsk to "Sum",
                    Language.English to "Total",
                    fontType = Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
                ) }
                cell { includePhrase(KronerText(grunnlag.inntekt, fontType = Element.OutlineContent.ParagraphContent.Text.FontType.BOLD)) }
            }
        }
    }
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, BeregningsVedleggData>.inntektBruktIBeregningenAvOms(
    etteroppgjoersAar: Expression<Int>,
    grunnlagData: Expression<EtteroppgjoerGrunnlagDTO>,
) {

    title2 {
        text(
            Bokmal to "Inntekt brukt i beregningen av omstillingsstønad",
            Nynorsk to "Inntekt som er lagt til grunn i utrekninga av omstillingsstønad",
            English to "Income applied in the calculation of adjustment allowance",
        )
    }

    paragraph {
        textExpr(
            Bokmal to "Vi har beregnet omstillingsstønaden din for ".expr() + etteroppgjoersAar.format() + " basert på en inntekt på " + grunnlagData.inntekt.format() + " kroner. Dette tilsvarer din pensjonsgivende inntekt minus fradragsbeløpet.",
            Nynorsk to "Vi har rekna ut omstillingsstønaden din for ".expr() + etteroppgjoersAar.format() + " med utgangspunkt i ei inntekt på " + grunnlagData.inntekt.format() + " kroner. Dette svarer til den pensjonsgivande inntekta di minus frådragsbeløpet.",
            English to "We have calculated your adjustment allowance for ".expr() + etteroppgjoersAar.format() + " based on an income of NOK " + grunnlagData.inntekt.format() + ". This corresponds to your pensionable income minus any deductions.",
        )
    }

    paragraph {
        text(Bokmal to "Omstillingsstønaden reduseres med 45 prosent av beløpet som er over halvparten av grunnbeløpet.  Inntekten er fordelt på antall innvilgede måneder.",
            Nynorsk to "Omstillingsstønaden blir redusert med 45 prosent av beløpet som er over halvparten av grunnbeløpet.  Inntekta er fordelt på antal månader som er innvilga.",
            English to "Adjustment allowance is reduced by 45 percent of the sum that exceeds half of the base amount (G). Income is distributed across the number of months the allowance is granted."
        )
    }

    title2 {
        text(
            Bokmal to "Er opplysningene om pensjonsgivende inntekt feil?",
            Nynorsk to "Er opplysningane om pensjonsgivande inntekt feil?",
            English to "Is the information about your pensionable income incorrect?",
        )
    }

    paragraph {
        text(
            Bokmal to "Det er Skatteetaten som vurderer om inntekten skal endres. Hvis du mener at inntektsopplysningene i skatteoppgjøret er feil, må du kontakte Skatteetaten. Gjør de en endring, gjennomfører vi automatisk et nytt etteroppgjør. Du vil få tilbakemelding dersom endringen påvirker etteroppgjøret ditt.",
            Nynorsk to "Det er Skatteetaten som vurderer om inntekta skal endrast. Dersom du meiner at inntektsopplysningane i skatteoppgjeret er feil, må du kontakte Skatteetaten. Viss noko blir endra, gjennomfører vi automatisk eit nytt etteroppgjer. Du vil få tilbakemelding dersom endringa påverkar etteroppgjeret ditt.",
            English to "The Tax Administration evaluates whether your income is to be amended. If you believe that information concerning your income in the tax settlement is incorrect, you must contact the Tax Administration. If they make an amendment, we will automatically implement a new final settlement. We will notify you if the amendment affects your final settlement.",
        )
    }
}

