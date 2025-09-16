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
                    bokmal { +"Vedtak om etteroppgjør" },
                    nynorsk { +"Vedtak om etteroppgjer" },
                    english { +"Decision on final settlement" },
                )
            }

            outline {
                konverterElementerTilBrevbakerformat(innhold)

                // Hvis feilutbetalt (tilbakekreving)
                showIf(data.resultatType.equalTo(EtteroppgjoerResultatType.TILBAKEKREVING)) {
                    paragraph {
                        text(
                            bokmal { +"Vår beregning viser at du har fått " + data.avviksBeloep.absoluteValue().format() + " for mye u omstillingsstønad i " + data.etteroppgjoersAar.format() + ". Dette overstiger ett rettsgebyr. Du må derfor betale tilbake det feilutbetalte beløpet." },
                            nynorsk { +"Utrekning vår viser at du har fått utbetalt " + data.avviksBeloep.absoluteValue().format() + " for mykje i omstillingsstønad i " + data.etteroppgjoersAar.format() + ". Dette overstig eitt rettsgebyr, og du må difor betale tilbake det feilutbetalte beløpet." },
                            english { +"Our calculations show that you have been overpaid " + data.avviksBeloep.absoluteValue().format() + " in adjustment allowance in " + data.etteroppgjoersAar.format() + ". This exceeds a standard court fee, which means that you must repay the incorrect amount paid to you." }
                        )
                    }
                }

                // Dersom etterbetaling
                showIf(data.resultatType.equalTo(EtteroppgjoerResultatType.ETTERBETALING)) {
                    paragraph {
                        text(
                            bokmal { +"Vår beregning viser at du har fått utbetalt " + data.avviksBeloep.absoluteValue().format() + " for lite i omstillingsstønad i " + data.etteroppgjoersAar.format() + ". Dette overstiger 25 prosent av rettsgebyret og du har oppfylt aktivitetsplikten. Du får derfor etterbetalt for lite utbetalt." },
                            nynorsk { +"Utrekninga vår viser at du har fått utbetalt " + data.avviksBeloep.absoluteValue().format() + " kroner for lite i omstillingsstønad i " + data.etteroppgjoersAar.format() + ". Dette overstig 25 prosent av rettsgebyret og du har oppfylt aktivitetsplikta. Du får etterbetalt for lite utbetalt." },
                            english { +"Our calculations show that you have been paid " + data.avviksBeloep.absoluteValue().format() + " too little in adjustment allowance in " + data.etteroppgjoersAar.format() + ". This exceeds 25 percent of a standard court fee and you have fulfilled the activity obligation. Therefore, you will receive back payments for the amount underpaid." }
                        )
                    }
                }

                paragraph {
                    text(
                        bokmal { +"Se beregningen av omstillingsstønaden din i vedlegget «Opplysninger om etteroppgjøret»." },
                        nynorsk { +"Sjå utrekninga av omstillingsstønaden din i vedlegget «Opplysningar om etteroppgjeret»." },
                        english { +"You will find the calculation of your adjustment allowance in the appendix «Information concerning final settlement»." },
                    )
                }

                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter bestemmelsene om omstillingsstønad i folketrygdloven § 17-9 og omstillingsstønadsforskriften § 9." },
                        nynorsk { +"Vedtaket er fatta etter føresegnene om omstillingsstønad i folketrygdlova § 17-9 og omstillingsstønadsforskriften § 9." },
                        english { +"The decision has been made pursuant to Section 17-9 of the Norwegian National Insurance Act and Section 9 of the Adjustment Allowance Regulations.." },
                    )
                }

                // Hvis feilutbetalt (tilbakekreving)
                showIf(data.resultatType.equalTo(EtteroppgjoerResultatType.TILBAKEKREVING)) {
                    title2 {
                        text(
                            bokmal { +"Hvordan skal du betale tilbake" },
                            nynorsk { +"Korleis du skal betale tilbake" },
                            english { +"" },
                        )
                    }
                    paragraph {
                        text(
                            bokmal { +"Etter fire uker får du et brev fra Skatteetatens avdeling “Innkrevingsentralen for bidrag og tilbakebetalingskrav”. I brevet finner du faktura og informasjon om når og hvordan du kan betale tilbake beløpet." },
                            nynorsk { +"Etter fire veker får du eit brev frå Skatteetaten si avdeling “Innkrevingssentralen for bidrag og tilbakebetalingskrav”. I brevet finn du faktura og informasjon om når og korleis du kan betale tilbake beløpet." },
                            english { +"After four weeks, you will receive a letter from the Tax Administration’s department “Innkrevingsentralen for bidrag og tilbakebetalingskrav”. The letter will include an invoice and information about when and how to repay the amount." },
                        )
                    }
                    paragraph {
                        text(
                            bokmal { +"Du skal ikke betale tilbake før du har fått betalingsinformasjon fra Skatteetaten." },
                            nynorsk { +"Du skal ikkje betale tilbake før du har fått betalingsinformasjon frå Skatteetaten." },
                            english { +"You should not make any payment until you have received payment information from the Tax Administration." },
                        )
                    }
                    paragraph {
                        text(
                            bokmal { +"Fordi du har betalt skatt av det du har fått for mye utbetalt, vil vi trekke fra skatt fra beløpet du skal betale tilbake. I betalingsinformasjonen du får fra Skatteetaten står det hvor mye du faktisk skal betale tilbake." },
                            nynorsk { +"Fordi du har betalt skatt av det du har fått for mykje utbetalt, vil vi trekkje frå skatten frå beløpet du skal betale tilbake. I betalingsinformasjonen du får frå Skatteetaten står det kor mykje du faktisk skal betale tilbake." },
                            english { +"Since you have already paid tax on the amount that was overpaid to you, we will deduct the tax from the amount you need to repay. The payment information from the Tax Administration will state the actual amount you have to repay." },
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
                            bokmal { +"Du får etterbetalt stønad for " + data.etteroppgjoersAar.format() + ". Utbetalingen skjer vanligvis innen tre uker. Dersom Skatteetaten eller andre instanser har krav mot deg, kan de trekke dette fra etterbetalingen. Eventuelle fradrag vises i utbetalingsmeldingen." },
                            nynorsk { +"Du får etterbetalt stønad for " + data.etteroppgjoersAar.format() + ". Utbetalinga skjer vanlegvis innan tre veker. Dersom Skatteetaten eller andre instansar har krav mot deg, kan dei trekkje dette frå etterbetalinga. Eventuelle frådrag blir viste i utbetalingsmeldinga." },
                            english { +"You will receive a back payment of allowance for " + data.etteroppgjoersAar.format() + ". The payment is usually made within three weeks. If the Tax Administration or other agencies have claims against you, they may deduct these from the back payment. Any deductions will be shown in the payment notification." },
                        )
                    }

                    paragraph {
                        text(
                            bokmal { +"Etterbetalingen gjelder tidligere år derfor trekker Nav skatt etter Skatteetatens standardsatser. Du kan lese mer om satsene på nav.no/skattetrekk#etterbetaling." },
                            nynorsk { +"Etterbetalinga gjeld tidlegare år, derfor trekkjer Nav skatt etter Skatteetaten sine standardsatsar. Du kan lese meir om satsane på nav.no/skattetrekk#etterbetaling." },
                            english { +"Since the back payment applies to a previous year, Nav withholds tax according to the Tax Administration’s standard rates. You can read more about the rates at nav.no/skattetrekk#etterbetaling." },
                        )
                    }
                }

                title2 {
                    text(
                        bokmal { +"Du må melde fra om endringer" },
                        nynorsk { +"Du må melde frå om endringar" },
                        english { +"You must report any changes" },
                    )
                }

                paragraph {
                    text(
                        bokmal { +"Du får fortsatt omstillingsstønad og den er på " + data.utbetaltBeloep.format() + " kroner hver måned før skatt." },
                        nynorsk { +"Du får framleis omstillingsstønad. Du får " + data.utbetaltBeloep.format() + " kroner kvar månad før skatt." },
                        english { +"You will continue to receive adjustment allowance and this will be " + data.utbetaltBeloep.format() + " per month before tax." },
                    )
                }

                paragraph {
                    text(
                        bokmal { +"Se hvordan du melder fra om endringer i vedlegget “Dine rettigheter og plikter”." },
                        nynorsk { +"I vedlegget «Dine rettar og plikter» ser du kva endringar du må seie frå om." },
                        english { +"You will see which changes you must report in the attachment, Your Rights and Obligations." },
                    )
                }

                includePhrase(OmstillingsstoenadFellesFraser.DuHarRettTilAaKlage)

                showIf(data.resultatType.equalTo(EtteroppgjoerResultatType.TILBAKEKREVING)) {
                    paragraph {
                        text(
                            bokmal { +"Du må som hovedregel begynne å betale tilbake selv om du klager på vedtaket, se forvaltningsloven § 42." },
                            nynorsk { +"Som hovudregel må du begynne å betale tilbake sjølv om du klagar på vedtaket, sjå forvaltningslova § 42." },
                            english { +"As a general rule, you must start repaying even if you appeal the decision. This is stated in Section 42 of the Public Administration Act and associated circulars." },
                        )
                    }
                }

                includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)
            }

            includeAttachment(beregningsVedlegg, data.beregningsVedleggData)
            includeAttachment(praktiskInformasjonOmEtteroppgjoeret)
        }
}
