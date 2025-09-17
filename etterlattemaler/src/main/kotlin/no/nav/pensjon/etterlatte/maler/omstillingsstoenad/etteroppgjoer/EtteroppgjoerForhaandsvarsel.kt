package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer

import no.nav.pensjon.brev.template.Language
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
import no.nav.pensjon.etterlatte.maler.fraser.common.Felles
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadFellesFraser
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselBrevDTOSelectors.data
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselBrevDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselDTOSelectors.beregningsVedleggData
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselDTOSelectors.bosattUtland
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselDTOSelectors.etteroppgjoersAar
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselDTOSelectors.resultatType
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.BeregningsVedleggData
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.EtteroppgjoerGrunnlagDTO
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.beregningsVedlegg
import java.time.LocalDate

data class EtteroppgjoerUtbetalingDTO(
    val stoenadUtbetalt: Kroner,
    val faktiskStoenad: Kroner,
    val avviksBeloep: Kroner
)

data class EtteroppgjoerForhaandsvarselBrevDTO(
    override val innhold: List<Element>,
    val data: EtteroppgjoerForhaandsvarselDTO
) : FerdigstillingBrevDTO

enum class EtteroppgjoerResultatType {
    TILBAKEKREVING,
    ETTERBETALING,
    INGEN_ENDRING_MED_UTBETALING,
}

data class EtteroppgjoerForhaandsvarselDTO(
    val vedleggInnhold: List<Element>,
    val bosattUtland: Boolean,
    val norskInntekt: Boolean,
    val etteroppgjoersAar: Int,
    val rettsgebyrBeloep: Kroner,
    val resultatType: EtteroppgjoerResultatType,
    val stoenad: Kroner,
    val faktiskStoenad: Kroner,
    val avviksBeloep: Kroner,
    val grunnlag: EtteroppgjoerGrunnlagDTO
) {
    val utbetalingData = EtteroppgjoerUtbetalingDTO(
        stoenadUtbetalt = stoenad,
        faktiskStoenad = faktiskStoenad,
        avviksBeloep = avviksBeloep
    )
    val beregningsVedleggData = BeregningsVedleggData(vedleggInnhold, etteroppgjoersAar, utbetalingData, grunnlag)
    val dagensDato: LocalDate = LocalDate.now()
}

@TemplateModelHelpers
object EtteroppgjoerForhaandsvarsel : EtterlatteTemplate<EtteroppgjoerForhaandsvarselBrevDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMS_EO_FORHAANDSVARSEL

    override val template = createTemplate(
        letterDataType = EtteroppgjoerForhaandsvarselBrevDTO::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Varsel - Etteroppgjør",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        ),
    ) {
        title {
            // Ingen endring
            showIf(data.resultatType.equalTo(EtteroppgjoerResultatType.INGEN_ENDRING_MED_UTBETALING)) {
                text(
                    bokmal { +"Informasjon om etteroppgjør av omstillingsstønad for " + data.etteroppgjoersAar.format() },
                    nynorsk { +"Informasjon om etteroppgjer av omstillingsstønad for " + data.etteroppgjoersAar.format() },
                    english { +"Information concerning final settlement of adjustment allowance for " + data.etteroppgjoersAar.format() },
                )
            }
                // Tilbakekreving eller Etterbetaling
                .orShow {
                    text(
                        bokmal { +"Forhåndsvarsel om etteroppgjør av omstillingsstønad for " + data.etteroppgjoersAar.format() },
                        nynorsk { +"Førehandsvarsel om etteroppgjer av omstillingsstønad for " + data.etteroppgjoersAar.format() },
                        english { +"Advance notice of final settlement of adjustment allowance for " + data.etteroppgjoersAar.format() },
                    )
                }
        }

        outline {

            konverterElementerTilBrevbakerformat(innhold)

            // Ingen endring
            showIf(data.resultatType.equalTo(EtteroppgjoerResultatType.INGEN_ENDRING_MED_UTBETALING)) {
                title2 {
                    text(
                        bokmal { +"Etteroppgjøret for " + data.etteroppgjoersAar.format() + " er nå avsluttet" },
                        nynorsk { +"Etteroppgjeret for " + data.etteroppgjoersAar.format() + " er no avslutta" },
                        english { +"Final settlement in " + data.etteroppgjoersAar.format() + " is now concluded" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Du finner beregningen av omstillingsstønaden din i vedlegget “Opplysninger om etteroppgjøret”. Vennligst gi oss beskjed dersom noen av opplysningene ikke stemmer." },
                        nynorsk { +"Du finn utrekninga av omstillingsstønaden din i vedlegget «Opplysningar om etteroppgjeret». Gi oss beskjed dersom opplysningane inneheld feil." },
                        english { +"You will find the calculation of your adjustment allowance in the appendix «Information concerning final settlement». Please notify us if you find that any of the information is incorrect." },
                    )
                }
            }
            // Tilbakekreving eller Etterbetaling
            .orShow {
                title2 {
                    text(
                        bokmal { +"Sjekk beregningen og meld fra hvis noe er feil" },
                        nynorsk { +"Sjekk utrekninga og meld frå viss noko er feil" },
                        english { +"Check the calculation and notify us if anything is incorrect" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Se ny beregning av omstillingsstønaden din for inntektsåret " + data.etteroppgjoersAar.format() + " i vedlegget “Opplysninger om etteroppgjøret”. Du må kontrollere om inntektene som er oppgitt er riktig." },
                        nynorsk { +"Sjå den nye utrekninga av omstillingsstønaden din for inntektsåret " + data.etteroppgjoersAar.format() + " i vedlegget «Opplysningar om etteroppgjeret». Du må kontrollere at inntektene som er oppgitt, stemmer." },
                        english { +"See the new calculation of your adjustment allowance for the income year " + data.etteroppgjoersAar.format() + " in the appendix “Information concerning final settlement”. You must check whether the stated income is correct. " }
                    )
                }

                paragraph {
                    text(
                        bokmal { +"Hvis du melder fra om feil, vil vi vurdere de nye opplysningene før du får et vedtak. " },
                        nynorsk { +"Dersom du melder frå om feil, vil vi vurdere dei nye opplysningane før du får eit vedtak. " },
                        english { +"If you report an error, we will evaluate the new information before sending a decision." },
                    )
                }

                paragraph {
                    text(
                        bokmal { +"Hvis opplysningene er feil, må du gi beskjed innen tre uker. Nye opplysninger må dokumenteres. Hvis du ikke svarer, får du et vedtak basert på de opplysningene som står i vedlegget. Hvis du melder fra om feil, vil vi vurdere de nye opplysningene før du får et vedtak." },
                        nynorsk { +"Dersom opplysningane er feil, må du gi beskjed innan tre veker. Nye opplysningar må dokumenterast. Dersom du ikkje svarer, får du eit vedtak basert på opplysningane som står i vedlegget." },
                        english { +"If the information is incorrect, you must notify us within three weeks. New information must be documented. If you do not reply, a decision will be made based on the information in the appendix." },
                    )
                }

                showIf(data.resultatType.equalTo(EtteroppgjoerResultatType.TILBAKEKREVING)) {
                    paragraph {
                        text(
                            bokmal { +"I vedtaket får du informasjon om hvordan du kan betale tilbake for mye utbetalt omstillingsstønad og hvordan du kan klage på vedtaket." },
                            nynorsk { +"I vedtaket får du informasjon om korleis du kan betale tilbake for mykje utbetalt omstillingsstønad, og korleis du kan klage på vedtaket." },
                            english { +"The decision will contain information about how to repay any overpaid adjustment allowance, and about how you can appeal against the decision." },
                        )
                    }
                }
            }

            includePhrase(Felles.HvordanMelderDuFra)
            includePhrase(OmstillingsstoenadFellesFraser.HarDuIkkeBankID(data.bosattUtland))
            includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)
        }

        includeAttachment(beregningsVedlegg, data.beregningsVedleggData)
    }
}
