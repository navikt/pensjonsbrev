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
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.BrevDTO
import no.nav.pensjon.etterlatte.maler.BrevDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.fraser.common.KronerText
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerUtbetalingDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerUtbetalingDTOSelectors.avviksBeloep
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerUtbetalingDTOSelectors.faktiskStoenad
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerUtbetalingDTOSelectors.stoenadUtbetalt
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.BeregningsVedleggDataSelectors.erVedtak
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
    val erVedtak: Boolean = false
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

    showIf(erVedtak) {
        paragraph {
            text(
                bokmal { +"Omstillingsstønaden din er nå beregnet på nytt for " + etteroppgjoersAar.format() +"." },
                nynorsk { +"Vi har no gjort ei ny utrekning av omstillingsstønaden din for " + etteroppgjoersAar.format() +"." },
                english { +"Your transitional benefit has now been recalculated for " + etteroppgjoersAar.format() +"." }
            )
        }
        paragraph {
            text(
                bokmal { +"I beregningen har vi brukt opplysninger om din faktiske inntekt for " + etteroppgjoersAar.format() + " fra Skatteetaten og a-ordningen. Hvis du har svart på varselet du mottok tidligere, har vi vurdert det du skrev. Eventuelle kommentarer til dette finner du i avsnittet “Beløp trukket fra din pensjonsgivende inntekt”." },
                nynorsk { +"I utrekninga har vi brukt opplysningar om den faktiske inntekta di for " + etteroppgjoersAar.format() + " frå Skatteetaten og a-ordninga. Dersom du har svart på varselet du fekk tidlegare, har vi vurdert det du skreiv. Eventuelle merknader til dette finn du i avsnittet “Beløp trekt frå di pensjonsgivande inntekt”." },
                english { +"In the calculation, we have used information about your actual income for " + etteroppgjoersAar.format() + " from the Norwegian Tax Administration and the A-scheme. If you responded to the notice you received earlier, we have considered what you wrote. Any comments related to this can be found in the section “Amount deducted from your pensionable income”." },
            )
        }
    }.orShow {
        paragraph {
            text(
                bokmal { +"Omstillingsstønaden din ble beregnet ut fra inntekten du oppga som forventet i " + etteroppgjoersAar.format() +". Vi har nå gjort en ny beregning basert på opplysninger fra Skatteetaten og a-ordningen om din faktiske inntekt for "+etteroppgjoersAar.format()+". Du kan se skatteoppgjøret ditt på skatteetaten.no." },
                nynorsk { +"Omstillingsstønaden din blei rekna ut på grunnlag av det du oppgav som forventa inntekt i " + etteroppgjoersAar.format() +". Vi har no gjort ei ny utrekning av den faktiske inntekta di for "+etteroppgjoersAar.format()+" basert på opplysningar frå Skatteetaten og a-ordninga. Du kan sjå skatteoppgjeret ditt på skatteetaten.no." },
                english { +"Your adjustment allowance was calculated based on your expected income you stated in " + etteroppgjoersAar.format() +". We have now carried out a new calculation based on information provided by the Tax Administration and A-scheme regarding your actual income for "+etteroppgjoersAar.format()+". You can see your tax settlement at: skatteetaten.no." },
            )
        }
        paragraph {
            text(
                bokmal { +"Husk at du må melde fra til oss innen tre uker hvis du mener beregningene er feil." },
                nynorsk { +"Hugs at du må melde frå til oss innan tre veker dersom du meiner at utrekningane ikkje stemmer." },
                english { +"Remember that you must notify us within three weeks if you believe that the calculations are incorrect." },
            )
        }
    }


}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, BeregningsVedleggData>.hvaDuFikkUtbetalt(
    etteroppgjoersAar: Expression<Int>,
    utbetalingData: Expression<EtteroppgjoerUtbetalingDTO>
) {
    title2 {
        text(
            bokmal { +"Hva du fikk utbetalt og hva du skulle fått utbetalt i " + etteroppgjoersAar.format() },
            nynorsk { +"Kva du fekk utbetalt og kva du skulle ha fått utbetalt i " + etteroppgjoersAar.format() },
            english { +"What you were paid, and what you should have been paid in " + etteroppgjoersAar.format() },
        )
    }

    paragraph {
        table(
            header = {
                column(1) {
                    text(
                        bokmal { +"Type stønad" },
                        nynorsk { +"Type stønad" },
                        english { +"Type of allowance" },
                    )
                }
                column(1) {
                    text(
                        bokmal { +"Dette skulle du fått" },
                        nynorsk { +"Dette skulle du fått" },
                        english { +"Amount you should have received " },
                    )
                }
                column(1) {
                    text(
                        bokmal { +"Dette fikk du" },
                        nynorsk { +"Dette fekk du" },
                        english { +"Amount you received" },
                    )
                }
                column(1) {
                    text(
                        bokmal { +"Avviksbeløp" },
                        nynorsk { +"Avvikande beløp" },
                        english { +"Difference, sum" },
                    )
                }
            }
        ) {
            row {
                cell { text(
                    bokmal { +"Omstillingsstønad" },
                    nynorsk { +"Omstillingsstønad" },
                    english { +"Adjustment allowance" },
                ) }

                cell { includePhrase(KronerText(utbetalingData.faktiskStoenad)) }
                cell { includePhrase(KronerText(utbetalingData.stoenadUtbetalt)) }
                cell { includePhrase(KronerText(utbetalingData.avviksBeloep.absoluteValue())) }
            }
        }
    }

    // ingen avvik
    showIf(utbetalingData.avviksBeloep.absoluteValue().equalTo(0)) {
        paragraph {
            text(
                bokmal { +"Tabellen viser at du har fått utbetalt riktig stønad i " + etteroppgjoersAar.format() + "." },
                nynorsk { +"Tabellen viser at du har fått utbetalt rett stønad i " + etteroppgjoersAar.format() + "." },
                english { +"The table shows that you have been paid the correct amount of allowance in " + etteroppgjoersAar.format() + "." },
            )
        }
    }
    // avvik
    .orShow {
        paragraph {
            text(
                bokmal { +"Du fikk utbetalt " + utbetalingData.avviksBeloep.absoluteValue().format() + " for " + ifElse(utbetalingData.avviksBeloep.greaterThan(0), "mye", "lite") + " stønad i " + etteroppgjoersAar.format() + " inkludert skatt." },
                nynorsk { +"Du fekk utbetalt " + utbetalingData.avviksBeloep.absoluteValue().format() + " for " + ifElse(utbetalingData.avviksBeloep.greaterThan(0), "mykje", "lite") + " stønad i " + etteroppgjoersAar.format() + " inkludert skatt." },
                english { +"You received " + utbetalingData.avviksBeloep.absoluteValue().format() + " too " + ifElse(utbetalingData.avviksBeloep.greaterThan(0), "much", "little") + " allowance in " + etteroppgjoersAar.format() + " including tax." },
            )
        }
    }
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, BeregningsVedleggData>.omBeregningAvOmstillingsstoenad(
    etteroppgjoersAar: Expression<Int>
) {
    title2 {
        text(
            bokmal { +"Om beregningen av omstillingsstønad for " + etteroppgjoersAar.format() },
            nynorsk { +"Om utrekninga av omstillingsstønad for " + etteroppgjoersAar.format() },
            english { +"About the calculation of adjustment allowance for " + etteroppgjoersAar.format() },
        )
    }

    paragraph {
        text(
            bokmal { +"Din pensjonsgivende inntekt avgjør hvor mye du får i omstillingsstønad. Dette står i § 3‑15 i folketrygdloven." },
            nynorsk { +"Den pensjonsgivande inntekta di avgjer kor mykje du får i omstillingsstønad. Dette går fram av § 3‑15 i folketrygdlova." },
            english { +"Your pensionable income determines how much you receive in adjustment allowance. This is pursuant to Section 3-15 in the National Insurance Act." },
        )
    }

    paragraph {
        text(
            bokmal { +"Pensjonsgivende inntekt inkluderer blant annet:" },
            nynorsk { +"Pensjonsgivande inntekt inkluderer mellom anna følgjande:" },
            english { +"Pensionable income includes:" },
        )

        list {
            item {
                text(
                    bokmal { +"brutto lønnsinntekt, inkludert feriepenger, fra alle norske arbeidsgivere" },
                    nynorsk { +"brutto lønsinntekt, inkludert feriepengar, frå alle norske arbeidsgivarar" },
                    english { +"Gross wage income, including holiday pay, from all Norwegian employers" },
                )
            }
            item {
                text(
                    bokmal { +"næringsinntekt og inntekt fra salg av næringsvirksomhet" },
                    nynorsk { +"næringsinntekt og inntekt frå sal av næringsverksemd" },
                    english { +"Income from self-employment and income from sales of business activities" },
                )
            }
            item {
                text(
                    bokmal { +"styregodtgjørelse og andre godtgjørelser" },
                    nynorsk { +"styregodtgjersle og andre godtgjersler" },
                    english { +"Director’s emoluments and other fees paid" },
                )
            }
            item {
                text(
                    bokmal { +"royalties" },
                    nynorsk { +"royalties" },
                    english { +"Royalties" },
                )
            }
            item {
                text(
                    bokmal { +"dagpenger, sykepenger, arbeidsavklaringspenger og kvalifiseringsstønad" },
                    nynorsk { +"dagpengar, sjukepengar, arbeidsavklaringspengar og kvalifiseringsstønad" },
                    english { +"Unemployment benefits, sick pay, work assessment allowance and qualification benefit" },
                )
            }
            item {
                text(
                    bokmal { +"svangerskapspenger og foreldrepenger" },
                    nynorsk { +"svangerskapspengar og foreldrepengar" },
                    english { +"Maternity allowance and parental allowance" },
                )
            }
            item {
                text(
                    bokmal { +"omsorgsstønad" },
                    nynorsk { +"omsorgsstønad" },
                    english { +"Care benefit" },
                )
            }
            item {
                text(
                    bokmal { +"inntekt som fosterforelder" },
                    nynorsk { +"inntekt som fosterforelder" },
                    english { +"Income as a foster parent" },
                )
            }
            item {
                text(
                    bokmal { +"omstillingsstønad" },
                    nynorsk { +"omstillingsstønad" },
                    english { +"Adjustment allowance" },
                )
            }
        }
    }

    paragraph {
        text(
            bokmal { +"Følgende pensjonsgivende inntekter kan trekkes fra i beregningen av omstillingsstønad:" },
            nynorsk { +"Følgjande pensjonsgivande inntekter kan trekkjast frå i utrekninga av omstillingsstønad:" },
            english { +"The following pensionable incomes can be deducted in the calculation of adjustment allowance:" },
        )

        list {
            item {
                text(
                    bokmal { +"omstillingsstønad (blir automatisk trukket fra)" },
                    nynorsk { +"omstillingsstønad (blir trekt frå automatisk)" },
                    english { +"Adjustment allowance (automatically deducted)" },
                )
            }
            item {
                text(
                    bokmal { +"inntekt for periode(r) før du fikk innvilget omstillingsstønad" },
                    nynorsk { +"inntekt før du fekk innvilga omstillingsstønad" },
                    english { +"Income for the period/periods before you were granted adjustment allowance" },
                )
            }
            item {
                text(
                    bokmal { +"inntekt for periode(r) etter at omstillingsstønaden opphørte" },
                    nynorsk { +"inntekt etter at omstillingsstønaden blei avvikla" },
                    english { +"Income for the period/periods after your adjustment allowance ended" },
                )
            }
            item {
                text(
                    bokmal { +"etterbetaling av andre ytelser du har mottatt fra Nav for perioder før du fikk innvilget omstillingsstønad" },
                    nynorsk { +"etterbetaling av andre ytingar du fekk frå Nav før omstillingsstønaden blei innvilga" },
                    english { +"Post-payment of other benefits you have received from Nav for periods before you were granted adjustment allowance" },
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
            bokmal { +"Din pensjonsgivende inntekt i innvilget periode" },
            nynorsk { +"Den pensjonsgivande inntekta di i innvilga periode" },
            english { +"Your pensionable income during the period you were granted adjustment allowance" },
        )
    }


    showIf(
        grunnlag.loennsinntekt.absoluteValue().greaterThan(0)
            .or(grunnlag.naeringsinntekt.absoluteValue().greaterThan(0)
                .or(grunnlag.afp.absoluteValue().greaterThan(0)
                    .or(grunnlag.utlandsinntekt.absoluteValue().greaterThan(0))))
    ) {
        paragraph {
            text(
                bokmal { +"I " + etteroppgjoersAar.format()+ " var din pensjonsgivende inntekt " + grunnlag.inntekt.format() + " inkludert skatt, i følge opplysninger fra Skatteetaten og a-ordningen. Den fordeler seg slik:" },
                nynorsk { +"Ifølgje opplysningar frå Skatteetaten og a-ordninga hadde du ei pensjonsgivande inntekt på " + grunnlag.inntekt.format() + " inkludert skatt i " + etteroppgjoersAar.format()+ ".  Inntekta fordeler seg slik:" },
                english { +"In " + etteroppgjoersAar.format()+ " your pensionable income was " + grunnlag.inntekt.format() + " including tax, according to information obtained from the Tax Administration and A-scheme. This is distributed as follows: " },
            )
        }

        paragraph {
            table(
                header = {
                    column(1) {
                        text(
                            bokmal { +"Type inntekt" },
                            nynorsk { +"Type inntekt" },
                            english { +"Type of income " },
                        )
                    }
                    column(1, alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {
                        text(
                            bokmal { +"Beløp" },
                            nynorsk { +"Beløp" },
                            english { +"Amount " },
                        )
                    }
                }
            ) {
                row {
                    cell { text(
                        bokmal { +"Lønnsinntekt" },
                        nynorsk { +"Lønnsinntekt" },
                        english { +"Wage income" },
                    ) }
                    cell { includePhrase(KronerText(grunnlag.loennsinntekt)) }
                }
                row {
                    cell { text(
                        bokmal { +"Avtalefestet pensjon (AFP) offentlig eller privat" },
                        nynorsk { +"Avtalefesta pensjon (AFP) offentleg eller privat" },
                        english { +"AFP (contractual pension in the public or private sector)" },
                    ) }
                    cell { includePhrase(KronerText(grunnlag.afp)) }
                }
                row {
                    cell { text(
                        bokmal { +"Næringsinntekt" },
                        nynorsk { +"Næringsinntekt" },
                        english { +"Income from self-employment" },
                    ) }
                    cell { includePhrase(KronerText(grunnlag.naeringsinntekt)) }
                }
                row {
                    cell { text(
                        bokmal { +"Utlandsinntekt" },
                        nynorsk { +"Utlandsinntekt" },
                        english { +"Foreign income" },
                    ) }
                    cell { includePhrase(KronerText(grunnlag.utlandsinntekt)) }
                }

                row {
                    cell { text(
                        bokmal { +"Sum" },
                        nynorsk { +"Sum" },
                        english { +"Total" },
                        fontType = Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
                    ) }
                    cell { includePhrase(KronerText(grunnlag.inntekt, fontType = Element.OutlineContent.ParagraphContent.Text.FontType.BOLD)) }
                }
            }
        }
    }.orShow {
        paragraph {
            text(
                bokmal { +"I " + etteroppgjoersAar.format()+ " var din pensjonsgivende inntekt " + grunnlag.inntekt.format() + " inkludert skatt, i følge opplysninger fra Skatteetaten og a-ordningen. Dersom dette ikke stemmer, må du sende oss opplysninger innen tre uker." },
                nynorsk { +"Ifølgje opplysningar frå Skatteetaten og a-ordninga hadde du ei pensjonsgivande inntekt på " + grunnlag.inntekt.format() + " inkludert skatt i " + etteroppgjoersAar.format()+ ". Dersom dette ikkje stemmer, må du sende oss korrekte opplysningar innan tre veker" },
                english { +"In " + etteroppgjoersAar.format()+ " your pensionable income was " + grunnlag.inntekt.format() + " including tax, according to information obtained from the Tax Administration and A-scheme. If this is incorrect, you must send us information within three weeks." }
            )
        }
    }

}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, BeregningsVedleggData>.inntektBruktIBeregningenAvOms(
    etteroppgjoersAar: Expression<Int>,
    grunnlagData: Expression<EtteroppgjoerGrunnlagDTO>,
) {

    title2 {
        text(
            bokmal { +"Inntekt brukt i beregningen av omstillingsstønad" },
            nynorsk { +"Inntekt som er lagt til grunn i utrekninga av omstillingsstønad" },
            english { +"Income applied in the calculation of adjustment allowance" },
        )
    }

    paragraph {
        text(
            bokmal { +"Vi har beregnet omstillingsstønaden din for " + etteroppgjoersAar.format() + " basert på en inntekt på " + grunnlagData.inntekt.format() + ". Dette tilsvarer din pensjonsgivende inntekt minus fradragsbeløpet." },
            nynorsk { +"Vi har rekna ut omstillingsstønaden din for " + etteroppgjoersAar.format() + " med utgangspunkt i ei inntekt på " + grunnlagData.inntekt.format() + ". Dette svarer til den pensjonsgivande inntekta di minus frådragsbeløpet." },
            english { +"We have calculated your adjustment allowance for " + etteroppgjoersAar.format() + " based on an income of " + grunnlagData.inntekt.format() + ". This corresponds to your pensionable income minus any deductions." },
        )
    }

    paragraph {
        text(bokmal { +"Omstillingsstønaden reduseres med 45 prosent av beløpet som er over halvparten av grunnbeløpet.  Inntekten er fordelt på antall innvilgede måneder." },
            nynorsk { +"Omstillingsstønaden blir redusert med 45 prosent av beløpet som er over halvparten av grunnbeløpet.  Inntekta er fordelt på antal månader som er innvilga." },
            english { +"Adjustment allowance is reduced by 45 percent of the sum that exceeds half of the base amount (G). Income is distributed across the number of months the allowance is granted." }
        )
    }

    title2 {
        text(
            bokmal { +"Er opplysningene om pensjonsgivende inntekt feil?" },
            nynorsk { +"Er opplysningane om pensjonsgivande inntekt feil?" },
            english { +"Is the information about your pensionable income incorrect?" },
        )
    }

    paragraph {
        text(
            bokmal { +"Det er Skatteetaten som vurderer om inntekten skal endres. Hvis du mener at inntektsopplysningene i skatteoppgjøret er feil, må du kontakte Skatteetaten. Gjør de en endring, gjennomfører vi automatisk et nytt etteroppgjør. Du vil få tilbakemelding dersom endringen påvirker etteroppgjøret ditt." },
            nynorsk { +"Det er Skatteetaten som vurderer om inntekta skal endrast. Dersom du meiner at inntektsopplysningane i skatteoppgjeret er feil, må du kontakte Skatteetaten. Viss noko blir endra, gjennomfører vi automatisk eit nytt etteroppgjer. Du vil få tilbakemelding dersom endringa påverkar etteroppgjeret ditt." },
            english { +"The Tax Administration evaluates whether your income is to be amended. If you believe that information concerning your income in the tax settlement is incorrect, you must contact the Tax Administration. If they make an amendment, we will automatically implement a new final settlement. We will notify you if the amendment affects your final settlement." },
        )
    }
}

