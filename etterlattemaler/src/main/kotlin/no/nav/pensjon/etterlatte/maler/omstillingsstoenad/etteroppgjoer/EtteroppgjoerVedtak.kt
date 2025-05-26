package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.absoluteValue
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.ElementListe
import no.nav.pensjon.etterlatte.maler.FerdigstillingBrevDTO
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadFellesFraser
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerVedtakBrevDTOSelectors.data
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerVedtakBrevDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerVedtakDataDTOSelectors.avviksBeloep
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerVedtakDataDTOSelectors.beregningsVedleggData
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerVedtakDataDTOSelectors.etteroppgjoersAar
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerVedtakDataDTOSelectors.resultatType
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerVedtakDataDTOSelectors.utbetaltBeloep
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.BeregningsVedleggData
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.EtteroppgjoerGrunnlagDTO
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.beregningsVedlegg
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.praktiskInformasjonOmEtteroppgjoeret

data class EtteroppgjoerVedtakBrevDTO(
    override val innhold: ElementListe,
    val data: EtteroppgjoerVedtakDataDTO,
) : FerdigstillingBrevDTO

data class EtteroppgjoerVedtakDataDTO(
    val vedleggInnhold: ElementListe,
    val bosattUtland: Boolean = false,
    val etteroppgjoersAar: Int,
    val avviksBeloep: Kroner,
    val utbetaltBeloep: Kroner,
    val resultatType: EtteroppgjoerResultatType,
    val inntekt: Kroner,
    val faktiskInntekt: Kroner,
    val grunnlag: EtteroppgjoerGrunnlagDTO
) {
    val utbetalingData = EtteroppgjoerUtbetalingDTO(inntekt, faktiskInntekt, avviksBeloep)
    val beregningsVedleggData = BeregningsVedleggData(vedleggInnhold, etteroppgjoersAar, utbetalingData, grunnlag)
}

@TemplateModelHelpers
object EtteroppgjoerVedtak : EtterlatteTemplate<EtteroppgjoerVedtakBrevDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMS_EO_VEDTAK

    override val template =
        createTemplate(
            name = kode.name,
            letterDataType = EtteroppgjoerVedtakBrevDTO::class,
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
            LetterMetadata(
                displayTitle = "Vedtak - Etteroppgjør",
                isSensitiv = true,
                distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
                brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
            ),
        ) {
            title {
                text(
                    Bokmal to "Vedtak om etteroppgjør",
                    Nynorsk to "",
                    English to "",
                )
            }

            outline {
                konverterElementerTilBrevbakerformat(innhold)

                paragraph {
                    text(
                        Language.Bokmal to "Hvert år sjekker Nav inntekten din for å se om du har fått utbetalt riktig beløp i omstillingsstønad året før.",
                        Language.Nynorsk to "",
                        Language.English to "",
                    )
                }

                // Dersom feilutbetalt beløp (tilbakekreving)
                showIf(data.resultatType.equalTo(EtteroppgjoerResultatType.TILBAKEKREVING)) {
                    paragraph {
                        textExpr(
                            Language.Bokmal to "Vår beregning viser at du har fått ".expr() + data.avviksBeloep.absoluteValue().format() + " kroner for mye omstillingsstønad i " + data.etteroppgjoersAar.format() + ". Dette overstiger ett rettsgebyr, som betyr at du må betale tilbake det feilutbetalte beløpet.",
                            Language.Nynorsk to "".expr(),
                            Language.English to "".expr()
                        )
                    }
                }

                // Dersom etterbetaling
                showIf(data.resultatType.equalTo(EtteroppgjoerResultatType.ETTERBETALING)) {
                    paragraph {
                        textExpr(
                            Language.Bokmal to "Vår beregning viser at du har fått utbetalt ".expr() + data.avviksBeloep.absoluteValue().format() + " kroner for lite omstillingsstønad i " + data.etteroppgjoersAar.format() + ". Dette overstiger 25 prosent av rettsgebyret og du har oppfylt aktivitetsplikten. Du får derfor etterbetalt for lite utbetalt omstillingsstønad.",
                            Language.Nynorsk to "".expr(),
                            Language.English to "".expr()
                        )
                    }
                }

                paragraph {
                    text(
                        Language.Bokmal to "Se beregningen av omstillingsstønaden din i vedlegget “Opplysninger om etteroppgjøret”.",
                        Language.Nynorsk to "",
                        Language.English to "",
                    )
                }

                paragraph {
                    text(
                        Language.Bokmal to "Vedtaket er gjort etter bestemmelsene om omstillingsstønad i folketrygdloven § 17-9 og omstillingsstønadsforskriften § 9.",
                        Language.Nynorsk to "",
                        Language.English to "",
                    )
                }

                // Dersom feilutbetalt beløp (tilbakekreving)
                showIf(data.resultatType.equalTo(EtteroppgjoerResultatType.TILBAKEKREVING)) {
                    title2 {
                        text(
                            Bokmal to "Du vil få informasjon om hvordan du kan betale tilbake etter fire uker",
                            Nynorsk to "",
                            English to "",
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to "Du vil få en faktura med informasjon fra Skatteetaten ved avdelingen “Innkrevingssentralen for bidrag og tilbakebetalingskrav”, etter fire uker om når og hvordan du kan betale tilbake pengene. Før du kan få svar på spørsmål om saken din eller kan betale tilbake, må du ha mottatt betalingsinformasjon fra Skatteetaten. Fordi du har betalt skatt av det du har fått for mye utbetalt, vil vi trekke fra skatt fra beløpet du skal betale tilbake. I betalingsinformasjonen du får fra Skatteetaten står det hvor mye du faktisk skal betale tilbake. Du kan lese mer om tilbakebetaling i vedlegget «Praktisk informasjon om etteroppgjøret».",
                            Nynorsk to "",
                            English to "",
                        )
                    }
                }

                // Hvis etterbetaling
                showIf(data.resultatType.equalTo(EtteroppgjoerResultatType.ETTERBETALING)) {
                    title2 {
                        text(
                            Bokmal to "Etterbetaling av beløpet",
                            Nynorsk to "",
                            English to "",
                        )
                    }
                    paragraph {
                        textExpr(
                            Bokmal to "Du får etterbetalt stønad for ".expr() + data.etteroppgjoersAar.format() + ". Vanligvis vil du få denne i løpet av tre uker. Hvis Skatteetaten eller andre ordninger har krav i etterbetalingen kan denne bli forsinket. Fradrag i etterbetalingen vil gå fram av utbetalingsmeldingen.",
                            Nynorsk to "".expr(),
                            English to "".expr(),
                        )
                    }

                    paragraph {
                        text(
                            Bokmal to "Etterbetalingen gjelder tidligere år derfor trekker Nav skatt etter Skatteetatens standardsatser. Du kan lese mer om satsene på nav.no/skattetrekk#etterbetaling.",
                            Nynorsk to "",
                            English to "",
                        )
                    }
                }

                title2 {
                    text(
                        Bokmal to "Du må melde fra om endringer",
                        Nynorsk to "",
                        English to "",
                    )
                }

                paragraph {
                    textExpr(
                        Bokmal to "Du får ".expr() + data.utbetaltBeloep.format() + " kroner hver måned i omstillingsstønad per i dag.",
                        Nynorsk to "".expr(),
                        English to "".expr(),
                    )
                }

                paragraph {
                    text(
                        Bokmal to "Se hvordan og hvilke endringer du skal melde fra om i vedlegget “Dine rettigheter og plikter”.",
                        Nynorsk to "",
                        English to "",
                    )
                }

                includePhrase(OmstillingsstoenadFellesFraser.DuHarRettTilAaKlage)
                showIf(data.resultatType.equalTo(EtteroppgjoerResultatType.TILBAKEKREVING)) {
                    paragraph {
                        text(
                            Bokmal to "Du må som hovedregel begynne å betale tilbake selv om du klager på vedtaket, se forvaltningsloven § 42.",
                            Nynorsk to "",
                            English to "",
                        )
                    }
                }

                includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)
            }

            includeAttachment(beregningsVedlegg, data.beregningsVedleggData)
            includeAttachment(praktiskInformasjonOmEtteroppgjoeret)
        }
}
