package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.maler.Brevkategori
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerAvslutning
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerAvslutning.DinePlikter
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerAvslutning.DuHarRettTilInnsyn
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerInnhold
import no.nav.pensjon.brev.alder.maler.brev.FeatureToggles
import no.nav.pensjon.brev.alder.maler.felles.HarDuSpoersmaal
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.Sakstype
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringAndreAvvikDto.Scenario
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringAndreAvvikDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringAndreAvvikDtoSelectors.PesysDataSelectors.medlemAvApotekerordningen
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringAndreAvvikDtoSelectors.PesysDataSelectors.oppgjoersAar
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringAndreAvvikDtoSelectors.PesysDataSelectors.pensjonsgivendeInntekt
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringAndreAvvikDtoSelectors.PesysDataSelectors.scenario
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringAndreAvvikDtoSelectors.PesysDataSelectors.toleranseBeloep
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringAndreAvvikDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.TemplateDescription.ISakstype
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

/**
 * Redigerbart vedtak — AFP etteroppgjør, ingen endring (andre avvik).
 *
 * Konvertert fra Exstream-malen `PE_AF_04_110`. Auto-varianten av samme situasjon
 * er [VedtakAfpEtteroppgjoerIngenEndringAuto] (`PE_AF_04_102`). Forklaringen til
 * brukeren avhenger av et av fire gjensidig utelukkende scenarier
 * (se [no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringAndreAvvikDto.Scenario]).
 */
@TemplateModelHelpers
object VedtakAfpEtteroppgjoerIngenEndringAndreAvvik : RedigerbarTemplate<VedtakAfpEtteroppgjoerIngenEndringAndreAvvikDto> {

    override val kode = Aldersbrevkoder.Redigerbar.PE_AFP_ETTEROPPGJOER_INGEN_ENDRING_ANDRE_AVVIK

    override val featureToggle = FeatureToggles.vedtakAfpEtteroppgjoerIngenEndring.toggle

    override val kategori = Brevkategori.ETTEROPPGJOER

    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.VEDTAK

    override val sakstyper: Set<ISakstype> = setOf(Sakstype.AFP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - ingen endring (andre avvik) - AFP etteroppgjør",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                bokmal { +"Avtalefestet pensjon (AFP) - vedtak i etteroppgjør for " + pesysData.oppgjoersAar.format() },
                nynorsk { +"Avtalefesta pensjon (AFP) - vedtak i etteroppgjer for " + pesysData.oppgjoersAar.format() },
            )
        }

        outline {
            includePhrase(AfpEtteroppgjoerInnhold.EtteroppgjoerIntro)
            includePhrase(AfpEtteroppgjoerInnhold.IkkeFunnetGrunnlagForAaEndre(pesysData.oppgjoersAar))
            showIf(pesysData.medlemAvApotekerordningen) {
                includePhrase(AfpEtteroppgjoerInnhold.VedtaksgrunnlagAfpApotekerordningen)
            }.orShow {
                includePhrase(AfpEtteroppgjoerInnhold.VedtaksgrunnlagAfpSpk)
            }
            includePhrase(AfpEtteroppgjoerInnhold.InntektenDinIAarTittel(pesysData.oppgjoersAar))

            showIf(pesysData.scenario.equalTo(Scenario.HEL_AFP_HELE_AARET_INNTEKT_FOER_UTTAK)) {
                paragraph {
                    text(
                        bokmal {
                            +"Du har fått utbetalt 100 prosent AFP. Opplysninger fra Skatteetaten viser at du har hatt en samlet pensjonsgivende inntekt på " + pesysData.pensjonsgivendeInntekt.format() + " for inntektsåret " + pesysData.oppgjoersAar.format() + ". Vi antar at hele denne inntekten er opptjent i perioden før du tok ut pensjon, og den blir derfor holdt utenfor etteroppgjøret."
                        },
                        nynorsk {
                            +"Du har fått utbetalt 100 prosent AFP. Opplysningar frå Skatteetaten viser at du har hatt ei samla pensjonsgivande inntekt på " + pesysData.pensjonsgivendeInntekt.format() + " for inntektsåret " + pesysData.oppgjoersAar.format() + ". Vi reknar med at heile denne inntekta er opptent i perioden før du tok ut pensjon, og ho blir derfor halden utanfor etteroppgjeret."
                        },
                    )
                }
            }

            showIf(pesysData.scenario.equalTo(Scenario.HEL_AFP_HELE_AARET_INGEN_INNTEKT)) {
                paragraph {
                    text(
                        bokmal {
                            +"Du har fått utbetalt 100 prosent AFP. Ved beregningen av pensjonen har vi lagt " +
                                "til grunn at du ikke ville ha arbeidsinntekt. Ifølge opplysninger fra " +
                                "Skatteetaten har du ikke hatt pensjonsgivende inntekt for inntektsåret " +
                                pesysData.oppgjoersAar.format() + "."
                        },
                        nynorsk {
                            +"Du har fått utbetalt 100 prosent AFP. Ved berekninga av pensjonen har vi lagt " +
                                "til grunn at du ikkje ville ha arbeidsinntekt. Ifølgje opplysningar frå " +
                                "Skatteetaten har du ikkje hatt pensjonsgivande inntekt for inntektsåret " +
                                pesysData.oppgjoersAar.format() + "."
                        },
                    )
                }
            }

            showIf(pesysData.scenario.equalTo(Scenario.IKKE_AFP_FULL_INNTEKT)) {
                paragraph {
                    text(
                        bokmal {
                            +"Du har ikke fått utbetalt pensjon fra AFP-ordningen for året " +
                                pesysData.oppgjoersAar.format() + " fordi du har vært i fullt inntektsgivende " +
                                "arbeid. Opplysninger fra Skatteetaten viser at du har hatt en pensjonsgivende " +
                                "inntekt på " + pesysData.pensjonsgivendeInntekt.format() + " for " + pesysData.oppgjoersAar.format() + ". " +
                                "Dette er det samme som tidligere er lagt til grunn ved pensjonsberegningen."
                        },
                        nynorsk {
                            +"Du har ikkje fått utbetalt pensjon frå AFP-ordninga for året " +
                                pesysData.oppgjoersAar.format() + " fordi du har vore i fullt inntektsgivande " +
                                "arbeid. Opplysningar frå Skatteetaten viser at du har hatt ei pensjonsgivande " +
                                "inntekt på " + pesysData.pensjonsgivendeInntekt.format() + " for " + pesysData.oppgjoersAar.format() + ". " +
                                "Det er det same som tidlegare er lagt til grunn ved pensjonsberekninga."
                        },
                    )
                }
            }

            showIf(pesysData.scenario.equalTo(Scenario.HEL_AFP_DELER_AV_AARET)) {
                paragraph {
                    text(
                        bokmal {
                            +"Du har fått utbetalt 100 prosent AFP for deler av året. Ved beregningen av " +
                                "pensjonen har vi lagt til grunn at du ikke ville ha arbeidsinntekt som " +
                                "er høyere enn godkjent toleransebeløp på " + pesysData.toleranseBeloep.format() + " i perioden med AFP. " +
                                "Ifølge opplysninger fra Skatteetaten har du heller ikke hatt slik " +
                                "pensjonsgivende inntekt i " + pesysData.oppgjoersAar.format() + "."
                        },
                        nynorsk {
                            +"Du har fått utbetalt 100 prosent AFP for delar av året. Ved berekninga av " +
                                "pensjonen har vi lagt til grunn at du ikkje ville ha arbeidsinntekt som " +
                                "er høgare enn det godkjende toleransebeløpet på " + pesysData.toleranseBeloep.format() + " i perioden med " +
                                "AFP. Ifølgje opplysningar frå Skatteetaten har du heller ikkje hatt slik " +
                                "pensjonsgivande inntekt i " + pesysData.oppgjoersAar.format() + "."
                        },
                    )
                }
            }

            includePhrase(AfpEtteroppgjoerInnhold.PensjonsberegningenBlirIkkeEndret(pesysData.oppgjoersAar))

            showIf(pesysData.scenario.equalTo(Scenario.IKKE_AFP_FULL_INNTEKT)) {
                includePhrase(AfpEtteroppgjoerInnhold.MeldingOmEndringerInnledning)

                includePhrase(AfpEtteroppgjoerInnhold.InntektUtenforEtteroppgjoerListe)

                includePhrase(AfpEtteroppgjoerInnhold.AnnenInntektInntektsproevd)

                includePhrase(AfpEtteroppgjoerInnhold.DokumenterInntekterUtenforAvkorting)

                includePhrase(AfpEtteroppgjoerInnhold.SkjemaForDokumentasjon)
            }

            includePhrase(DinePlikter)
            includePhrase(AfpEtteroppgjoerAvslutning.DuHarRettTilAaKlageSeksUkerMedLenke)
            includePhrase(DuHarRettTilInnsyn)
            includePhrase(HarDuSpoersmaal.afpEtteroppgjoer)
        }
    }
}
