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
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.formatYearMonth
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
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerUtbetalingDTOSelectors.faktiskInntekt
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerUtbetalingDTOSelectors.inntekt
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.BeregningsVedleggDataSelectors.etteroppgjoersAar
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.BeregningsVedleggDataSelectors.grunnlag
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.BeregningsVedleggDataSelectors.utbetalingData
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.EtteroppgjoerGrunnlagDTOSelectors.afp
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.EtteroppgjoerGrunnlagDTOSelectors.fom
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.EtteroppgjoerGrunnlagDTOSelectors.loennsinntekt
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.EtteroppgjoerGrunnlagDTOSelectors.naeringsinntekt
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.EtteroppgjoerGrunnlagDTOSelectors.tom
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
    val utlandsinntekt: Kroner
)

@TemplateModelHelpers
val beregningsVedlegg: AttachmentTemplate<LangBokmalNynorskEnglish, BeregningsVedleggData> =
    createAttachment(
        title =
        newText(
            Bokmal to "Opplysninger om etteroppgjøret",
            Nynorsk to "",
            English to "",
        ),
        includeSakspart = false,
    ) {
        opplysningerOmEtteroppgjoer(argument.etteroppgjoersAar)
        hvaDuFikkUtbetalt(argument.etteroppgjoersAar, argument.utbetalingData)
        omBeregningAvOmstillingsstoenad(argument.etteroppgjoersAar)
        dinPensjonsgivendeInntekt(argument.etteroppgjoersAar, argument.utbetalingData, argument.grunnlag)
        beloepTrukketFraDinPensjonsgivendeInntekt()

        konverterElementerTilBrevbakerformat(argument.innhold)

        inntektBruktIBeregningenAvOms(argument.etteroppgjoersAar, argument.utbetalingData)
    }


private fun OutlineOnlyScope<LangBokmalNynorskEnglish, BeregningsVedleggData>.opplysningerOmEtteroppgjoer(
    etteroppgjoersAar: Expression<Int>
) {
    paragraph {
        textExpr(
            Bokmal to "Omstillingsstønaden din ble beregnet ut fra inntekten du oppga som forventet i ".expr() + etteroppgjoersAar.format() +". Vi har nå gjort en ny beregning basert på opplysninger fra Skatteetaten om din faktiske inntekt for "+etteroppgjoersAar.format()+". Du kan se skatteoppgjøret ditt på skatteetaten.no.",
            Nynorsk to "".expr(),
            English to "".expr(),
        )
    }
    paragraph {
        text(
            Bokmal to "Husk at du må melde fra til oss innen tre uker hvis du mener beregningene er feil.",
            Nynorsk to "",
            English to "",
        )
    }
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, BeregningsVedleggData>.hvaDuFikkUtbetalt(
    etteroppgjoersAar: Expression<Int>,
    utbetalingData: Expression<EtteroppgjoerUtbetalingDTO>
) {
    title2 {
        textExpr(
            Bokmal to "Hva du fikk utbetalt og hva du skulle fått utbetalt i ".expr() + etteroppgjoersAar.format() + "",
            Nynorsk to "".expr(),
            English to "".expr(),
        )
    }

    // TODO include
    paragraph {
        table(
            header = {
                column(1) {
                    text(
                        Language.Bokmal to "Type stønad",
                        Language.Nynorsk to "",
                        Language.English to "",
                    )
                }
                column(1) {
                    text(
                        Language.Bokmal to "Dette skulle du fått",
                        Language.Nynorsk to "",
                        Language.English to "",
                    )
                }
                column(1) {
                    text(
                        Language.Bokmal to "Dette fikk du",
                        Language.Nynorsk to "",
                        Language.English to "",
                    )
                }
                column(1) {
                    text(
                        Language.Bokmal to "Avviksbeløp",
                        Language.Nynorsk to "",
                        Language.English to "",
                    )
                }
            }
        ) {

            row {
                cell { text(
                    Language.Bokmal to "Omstillingsstønad",
                    Language.Nynorsk to "",
                    Language.English to "",
                ) }

                cell { includePhrase(KronerText(utbetalingData.faktiskInntekt)) }
                cell { includePhrase(KronerText(utbetalingData.inntekt)) }
                cell { includePhrase(KronerText(utbetalingData.avviksBeloep)) }
            }
        }
    }

    paragraph {
        textExpr(
            Bokmal to "Du fikk utbetalt ".expr() + utbetalingData.avviksBeloep.absoluteValue().format() + " kroner for " + ifElse(utbetalingData.avviksBeloep.greaterThan(0), "mye", "lite") + " i " + etteroppgjoersAar.format() + " inkludert skatt.",
            Nynorsk to "".expr(),
            English to "".expr(),
        )
    }
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, BeregningsVedleggData>.omBeregningAvOmstillingsstoenad(
    etteroppgjoersAar: Expression<Int>
) {
    title2 {
        textExpr(
            Bokmal to "Om beregningen av omstillingsstønad for ".expr() + etteroppgjoersAar.format() + "",
            Nynorsk to "".expr(),
            English to "".expr(),
        )
    }

    paragraph {
        text(
            Bokmal to "Din pensjonsgivende inntekt avgjør hvor mye du får i omstillingsstønad. Dette står i § 3‑15 i folketrygdloven.",
            Nynorsk to "",
            English to "",
        )
    }

    paragraph {
        text(
            Bokmal to "Pensjonsgivende inntekt inkluderer blant annet:",
            Nynorsk to "",
            English to "",
        )

        list {
            item {
                text(
                    Bokmal to "brutto lønnsinntekt, inkludert feriepenger, fra alle norske arbeidsgivere",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "næringsinntekt og inntekt fra salg av næringsvirksomhet",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "styregodtgjørelse og andre godtgjørelser",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "royalties",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "dagpenger, sykepenger og arbeidsavklaringspenger",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "svangerskapspenger og foreldrepenger",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "omsorgsstønad",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "inntekt som fosterforelder",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "omstillingsstønad",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }

    paragraph {
        text(
            Bokmal to "Følgende pensjonsgivende inntekter kan trekkes fra i beregningen av omstillingsstønad:",
            Nynorsk to "",
            English to "",
        )

        list {
            item {
                text(
                    Bokmal to "omstillingsstønad (blir automatisk trukket fra)",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "inntekt for periode(r) før du fikk innvilget omstillingsstønad",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "inntekt for periode(r) etter at omstillingsstønaden opphørte",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "etterbetaling av andre ytelser du har mottatt fra Nav for perioder før du fikk innvilget omstillingsstønad",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }


}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, BeregningsVedleggData>.dinPensjonsgivendeInntekt(
    etteroppgjoersAar: Expression<Int>,
    utbetalingData: Expression<EtteroppgjoerUtbetalingDTO>,
    grunnlag: Expression<EtteroppgjoerGrunnlagDTO>
) {

    title2 {
        text(
            Bokmal to "Din pensjonsgivende inntekt i innvilget periode",
            Nynorsk to "",
            English to "",
        )
    }

    paragraph {
        textExpr(
            Bokmal to "I periode " .expr() + grunnlag.fom.formatYearMonth()+ " til " + grunnlag.tom.formatYearMonth() + " var den faktiske inntekten din " + utbetalingData.faktiskInntekt.format() + " kroner. Du kan se fordelingen i tabellen under.",
            Nynorsk to "".expr(),
            English to "".expr(),)
    }

    paragraph {
        table(
            header = {
                column(1) {
                    text(
                        Language.Bokmal to "Type inntekt",
                        Language.Nynorsk to "",
                        Language.English to "",
                    )
                }
                column(1, alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {
                    text(
                        Language.Bokmal to "Beløp",
                        Language.Nynorsk to "",
                        Language.English to "",
                    )
                }
            }
        ) {

            // beregnetEtteroppgjoerResultat
            row {
                cell { text(
                    Language.Bokmal to "Lønnsinntekt",
                    Language.Nynorsk to "",
                    Language.English to "",
                ) }

                cell { includePhrase(KronerText(grunnlag.loennsinntekt)) }

            }
            row {
                cell { text(
                    Language.Bokmal to "Næringsinntekt",
                    Language.Nynorsk to "",
                    Language.English to "",
                ) }

                cell { includePhrase(KronerText(grunnlag.naeringsinntekt)) }


            }
            row {
                cell { text(
                    Language.Bokmal to "AFP",
                    Language.Nynorsk to "",
                    Language.English to "",
                ) }

                cell { includePhrase(KronerText(grunnlag.afp)) }


            }
            row {
                cell { text(
                    Language.Bokmal to "Utlandsinntekt",
                    Language.Nynorsk to "",
                    Language.English to "",
                ) }

                cell { includePhrase(KronerText(grunnlag.utlandsinntekt)) }
            }

            row {
                cell { text(
                    Language.Bokmal to "Sum",
                    Language.Nynorsk to "",
                    Language.English to "",
                    fontType = Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
                ) }
                cell { includePhrase(KronerText(utbetalingData.faktiskInntekt, fontType = Element.OutlineContent.ParagraphContent.Text.FontType.BOLD)) }
            }

        }
    }
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, BeregningsVedleggData>.beloepTrukketFraDinPensjonsgivendeInntekt() {
    title2 {
        text(
            Bokmal to "Beløp trukket fra din pensjonsgivende inntekt",
            Nynorsk to "",
            English to "",
        )
    }

    // TODO: tabell

    paragraph {
        text(
            Bokmal to "Hvis du har hatt andre inntekter som kan trekkes fra, må du sende oss dokumentasjon på det innen tre uker.",
            Nynorsk to "",
            English to "",
        )
    }
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, BeregningsVedleggData>.inntektBruktIBeregningenAvOms(
    etteroppgjoersAar: Expression<Int>,
    utbetalingData: Expression<EtteroppgjoerUtbetalingDTO>
) {

    title2 {
        text(
            Bokmal to "Inntekt brukt i beregningen av omstillingsstønad",
            Nynorsk to "",
            English to "",
        )
    }

    paragraph {
        textExpr(
            Bokmal to "Vi har beregnet omstillingsstønaden din for ".expr() + etteroppgjoersAar.format() + " basert på en inntekt på " + utbetalingData.inntekt.format() + " kroner. Dette tilsvarer din pensjonsgivende inntekt minus fradragsbeløpet.",
            Nynorsk to "".expr(),
            English to "".expr(),
        )
    }

    paragraph {
        text(Bokmal to "Omstillingsstønaden reduseres med 45 prosent av beløpet som er over halvparten av grunnbeløpet.  Inntekten er fordelt på antall innvilgede måneder.",
            Nynorsk to "",
            English to ""
        )
    }

    title2 {
        text(
            Bokmal to "Er opplysningene om pensjonsgivende inntekt feil?",
            Nynorsk to "",
            English to "",
        )
    }

    paragraph {
        text(
            Bokmal to "Det er Skatteetaten som vurderer om inntekten skal endres. Hvis du mener at inntektsopplysningene i skatteoppgjøret er feil, må du kontakte Skatteetaten. Gjør de en endring, gjennomfører vi automatisk et nytt etteroppgjør. Du vil få tilbakemelding dersom endringen påvirker etteroppgjøret ditt.",
            Nynorsk to "",
            English to "",
        )
    }
}

