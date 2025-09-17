package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.absoluteValue
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Element
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
    override val innhold: List<Element>,
    val data: EtteroppgjoerVedtakDataDTO,
) : FerdigstillingBrevDTO

data class EtteroppgjoerVedtakDataDTO(
    val vedleggInnhold: List<Element>,
    val bosattUtland: Boolean = false,
    val etteroppgjoersAar: Int,
    val avviksBeloep: Kroner,
    val utbetaltBeloep: Kroner,
    val resultatType: EtteroppgjoerResultatType,
    val stoenad: Kroner,
    val faktiskStoenad: Kroner,
    val grunnlag: EtteroppgjoerGrunnlagDTO
) {
    val utbetalingData = EtteroppgjoerUtbetalingDTO(stoenad, faktiskStoenad, avviksBeloep)
    val beregningsVedleggData = BeregningsVedleggData(vedleggInnhold, etteroppgjoersAar, utbetalingData, grunnlag)
}

@TemplateModelHelpers
object EtteroppgjoerVedtak : EtterlatteTemplate<EtteroppgjoerVedtakBrevDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMS_EO_VEDTAK

    override val template =
        createTemplate(
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
                    bokmal { +"Vedtak om etteroppgjør" },
                    nynorsk { +"" },
                    english { +"" },
                )
            }

            outline {
                konverterElementerTilBrevbakerformat(innhold)

                paragraph {
                    text(
                        bokmal { +"Hvert år sjekker Nav inntekten din for å se om du har fått utbetalt riktig beløp i omstillingsstønad året før." },
                        nynorsk { +"" },
                        english { +"" },
                    )
                }

                // Dersom feilutbetalt beløp (tilbakekreving)
                showIf(data.resultatType.equalTo(EtteroppgjoerResultatType.TILBAKEKREVING)) {
                    paragraph {
                        text(
                            bokmal { +"Vår beregning viser at du har fått " + data.avviksBeloep.absoluteValue().format() + " for mye omstillingsstønad i " + data.etteroppgjoersAar.format() + ". Dette overstiger ett rettsgebyr, som betyr at du må betale tilbake det feilutbetalte beløpet." },
                            nynorsk { +"" },
                            english { +"" }
                        )
                    }
                }

                // Dersom etterbetaling
                showIf(data.resultatType.equalTo(EtteroppgjoerResultatType.ETTERBETALING)) {
                    paragraph {
                        text(
                            bokmal { +"Vår beregning viser at du har fått utbetalt " + data.avviksBeloep.absoluteValue().format() + " for lite omstillingsstønad i " + data.etteroppgjoersAar.format() + ". Dette overstiger 25 prosent av rettsgebyret og du har oppfylt aktivitetsplikten. Du får derfor etterbetalt for lite utbetalt omstillingsstønad." },
                            nynorsk { +"" },
                            english { +"" }
                        )
                    }
                }

                paragraph {
                    text(
                        bokmal { +"Se beregningen av omstillingsstønaden din i vedlegget “Opplysninger om etteroppgjøret”." },
                        nynorsk { +"" },
                        english { +"" },
                    )
                }

                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter bestemmelsene om omstillingsstønad i folketrygdloven § 17-9 og omstillingsstønadsforskriften § 9." },
                        nynorsk { +"" },
                        english { +"" },
                    )
                }

                // Dersom feilutbetalt beløp (tilbakekreving)
                showIf(data.resultatType.equalTo(EtteroppgjoerResultatType.TILBAKEKREVING)) {
                    title2 {
                        text(
                            bokmal { +"Du vil få informasjon om hvordan du kan betale tilbake etter fire uker" },
                            nynorsk { +"" },
                            english { +"" },
                        )
                    }
                    paragraph {
                        text(
                            bokmal { +"Du vil få en faktura med informasjon fra Skatteetaten ved avdelingen “Innkrevingssentralen for bidrag og tilbakebetalingskrav”, etter fire uker om når og hvordan du kan betale tilbake pengene. Før du kan få svar på spørsmål om saken din eller kan betale tilbake, må du ha mottatt betalingsinformasjon fra Skatteetaten. Fordi du har betalt skatt av det du har fått for mye utbetalt, vil vi trekke fra skatt fra beløpet du skal betale tilbake. I betalingsinformasjonen du får fra Skatteetaten står det hvor mye du faktisk skal betale tilbake. Du kan lese mer om tilbakebetaling i vedlegget «Praktisk informasjon om etteroppgjøret»." },
                            nynorsk { +"" },
                            english { +"" },
                        )
                    }
                }

                // Hvis etterbetaling
                showIf(data.resultatType.equalTo(EtteroppgjoerResultatType.ETTERBETALING)) {
                    title2 {
                        text(
                            bokmal { +"Etterbetaling av beløpet" },
                            nynorsk { +"" },
                            english { +"" },
                        )
                    }
                    paragraph {
                        text(
                            bokmal { +"Du får etterbetalt stønad for " + data.etteroppgjoersAar.format() + ". Vanligvis vil du få denne i løpet av tre uker. Hvis Skatteetaten eller andre ordninger har krav i etterbetalingen kan denne bli forsinket. Fradrag i etterbetalingen vil gå fram av utbetalingsmeldingen." },
                            nynorsk { +"" },
                            english { +"" },
                        )
                    }

                    paragraph {
                        text(
                            bokmal { +"Etterbetalingen gjelder tidligere år derfor trekker Nav skatt etter Skatteetatens standardsatser. Du kan lese mer om satsene på nav.no/skattetrekk#etterbetaling." },
                            nynorsk { +"" },
                            english { +"" },
                        )
                    }
                }

                title2 {
                    text(
                        bokmal { +"Du må melde fra om endringer" },
                        nynorsk { +"" },
                        english { +"" },
                    )
                }

                paragraph {
                    text(
                        bokmal { +"Du får " + data.utbetaltBeloep.format() + " hver måned i omstillingsstønad per i dag." },
                        nynorsk { +"" },
                        english { +"" },
                    )
                }

                paragraph {
                    text(
                        bokmal { +"Se hvordan og hvilke endringer du skal melde fra om i vedlegget “Dine rettigheter og plikter”." },
                        nynorsk { +"" },
                        english { +"" },
                    )
                }

                includePhrase(OmstillingsstoenadFellesFraser.DuHarRettTilAaKlage)
                showIf(data.resultatType.equalTo(EtteroppgjoerResultatType.TILBAKEKREVING)) {
                    paragraph {
                        text(
                            bokmal { +"Du må som hovedregel begynne å betale tilbake selv om du klager på vedtaket, se forvaltningsloven § 42." },
                            nynorsk { +"" },
                            english { +"" },
                        )
                    }
                }

                includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)
            }

            includeAttachment(beregningsVedlegg, data.beregningsVedleggData)
            includeAttachment(praktiskInformasjonOmEtteroppgjoeret)
        }
}
