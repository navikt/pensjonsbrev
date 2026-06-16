package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerAvslutning
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerAvslutning.DinePlikter
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerAvslutning.DuHarRettTilInnsyn
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerInnhold
import no.nav.pensjon.brev.alder.maler.felles.HarDuSpoersmaal
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringAndreAvvikAutoDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringAndreAvvikAutoDto.Scenario
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringAndreAvvikAutoDtoSelectors.medlemAvApotekerordningen
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringAndreAvvikAutoDtoSelectors.oppgjoersAar
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringAndreAvvikAutoDtoSelectors.pensjonsgivendeInntekt
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringAndreAvvikAutoDtoSelectors.scenario
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringAndreAvvikAutoDtoSelectors.toleranseBeloep
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

/**
 * Vedtak — ingen endring (andre avvik) — AFP etteroppgjør (autobrev).
 *
 * Konvertert fra Exstream-malen `PE_AF_04_102`. Brevet sendes etter et
 * AFP-etteroppgjør (offentlig sektor / Statens pensjonskasse) når NAV har
 * konkludert med at pensjonsberegningen ikke skal endres. Forklaringen til
 * brukeren avhenger av et av fire gjensidig utelukkende scenarier
 * (se [VedtakAfpEtteroppgjoerIngenEndringAndreAvvikAutoDto.Scenario]).
 */
@TemplateModelHelpers
object VedtakAfpEtteroppgjoerIngenEndringAndreAvvikAuto : AutobrevTemplate<VedtakAfpEtteroppgjoerIngenEndringAndreAvvikAutoDto> {

    override val kode = Aldersbrevkoder.AutoBrev.PE_AFP_ETTEROPPGJOER_INGEN_ENDRING_ANDRE_AVVIK_AUTO

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
                bokmal { +"Avtalefestet pensjon (AFP) - vedtak i etteroppgjør for " + oppgjoersAar.format() },
                nynorsk { +"Avtalefesta pensjon (AFP) - vedtak i etteroppgjer for " + oppgjoersAar.format() },
            )
        }

        outline {
            includePhrase(AfpEtteroppgjoerInnhold.EtteroppgjoerIntro)
            includePhrase(AfpEtteroppgjoerInnhold.IkkeFunnetGrunnlagForAaEndre(oppgjoersAar))
            showIf(medlemAvApotekerordningen) {
                includePhrase(AfpEtteroppgjoerInnhold.VedtaksgrunnlagAfpApotekerordningen)
            }.orShow {
                includePhrase(AfpEtteroppgjoerInnhold.VedtaksgrunnlagAfpSpk)
            }
            includePhrase(AfpEtteroppgjoerInnhold.InntektenDinIAarTittel(oppgjoersAar))

            // Scenario A — 100% AFP, all inntekt før uttak
            showIf(scenario.equalTo(Scenario.HEL_AFP_HELE_AARET_INNTEKT_FOER_UTTAK)) {
                paragraph {
                    text(
                        bokmal {
                            +"Du har fått utbetalt 100 prosent AFP. Opplysninger fra Skatteetaten viser at du har hatt en samlet pensjonsgivende inntekt på " + pensjonsgivendeInntekt.format() + " for inntektsåret " + oppgjoersAar.format() + ". Vi antar at hele denne inntekten er opptjent i perioden før du tok ut pensjon, og den blir derfor holdt utenfor etteroppgjøret."
                        },
                        nynorsk {
                            +"Du har fått utbetalt 100 prosent AFP. Opplysningar frå Skatteetaten viser at du har hatt ei samla pensjonsgivande inntekt på " + pensjonsgivendeInntekt.format() + " for inntektsåret " + oppgjoersAar.format() + ". Vi reknar med at heile denne inntekta er opptent i perioden før du tok ut pensjon, og ho blir derfor halden utanfor etteroppgjeret."
                        },
                    )
                }
            }

            // Scenario B — 100% AFP, ingen inntekt
            showIf(scenario.equalTo(Scenario.HEL_AFP_HELE_AARET_INGEN_INNTEKT)) {
                paragraph {
                    text(
                        bokmal {
                            +"Du har fått utbetalt 100 prosent AFP. Ved beregningen av pensjonen har vi lagt til grunn at du ikke ville ha arbeidsinntekt. Ifølge opplysninger fra Skatteetaten har du ikke hatt pensjonsgivende inntekt for inntektsåret " + oppgjoersAar.format() + "."
                        },
                        nynorsk {
                            +"Du har fått utbetalt 100 prosent AFP. Ved berekninga av pensjonen har vi lagt til grunn at du ikkje ville ha arbeidsinntekt. Ifølgje opplysningar frå Skatteetaten har du ikkje hatt pensjonsgivande inntekt for inntektsåret " + oppgjoersAar.format() + "."
                        },
                    )
                }
            }

            // Scenario C — 0% AFP, full inntekt
            showIf(scenario.equalTo(Scenario.IKKE_AFP_FULL_INNTEKT)) {
                paragraph {
                    text(
                        bokmal {
                            +"Du har ikke fått utbetalt pensjon fra AFP-ordningen for året " + oppgjoersAar.format() + " fordi du har vært i fullt inntektsgivende arbeid. Opplysninger fra Skatteetaten viser at du har hatt en pensjonsgivende inntekt på " + pensjonsgivendeInntekt.format() + " for " + oppgjoersAar.format() + ". Dette er det samme som tidligere er lagt til grunn ved pensjonsberegningen."
                        },
                        nynorsk {
                            +"Du har ikkje fått utbetalt pensjon frå AFP-ordninga for året " + oppgjoersAar.format() + " fordi du har vore i fullt inntektsgivande arbeid. Opplysningar frå Skatteetaten viser at du har hatt ei pensjonsgivande inntekt på " + pensjonsgivendeInntekt.format() + " for " + oppgjoersAar.format() + ". Det er det same som tidlegare er lagt til grunn ved pensjonsberekninga."
                        },
                    )
                }
            }

            // Scenario D — 100% AFP deler av året, inntekt under toleransebeløpet
            showIf(scenario.equalTo(Scenario.HEL_AFP_DELER_AV_AARET)) {
                paragraph {
                    text(
                        bokmal {
                            +"Du har fått utbetalt 100 prosent AFP for deler av året. Ved beregningen av pensjonen har vi lagt til grunn at du ikke ville ha arbeidsinntekt som er høyere enn godkjent toleransebeløp på " + toleranseBeloep.format() + " i perioden med AFP. Ifølge opplysninger fra Skatteetaten har du heller ikke hatt slik pensjonsgivende inntekt i " + oppgjoersAar.format() + "."
                        },
                        nynorsk {
                            +"Du har fått utbetalt 100 prosent AFP for delar av året. Ved berekninga av pensjonen har vi lagt til grunn at du ikkje ville ha arbeidsinntekt som er høgare enn det godkjende toleransebeløpet på " + toleranseBeloep.format() + " i perioden med AFP. Ifølgje opplysningar frå Skatteetaten har du heller ikkje hatt slik pensjonsgivande inntekt i " + oppgjoersAar.format() + "."
                        },
                    )
                }
            }

            includePhrase(AfpEtteroppgjoerInnhold.PensjonsberegningenBlirIkkeEndret(oppgjoersAar))

            showIf(scenario.equalTo(Scenario.IKKE_AFP_FULL_INNTEKT)) {
                // Seksjon 1: Melding om endringer av inntekten.
                includePhrase(AfpEtteroppgjoerInnhold.MeldingOmEndringerInnledning)

                includePhrase(AfpEtteroppgjoerInnhold.InntektUtenforEtteroppgjoerListe)

                includePhrase(AfpEtteroppgjoerInnhold.AnnenInntektInntektsproevd)

                includePhrase(AfpEtteroppgjoerInnhold.DokumenterInntekterUtenforAvkorting)

                includePhrase(AfpEtteroppgjoerInnhold.SkjemaForDokumentasjon)
            }

            // TODO: hør om lenken til klage skal være med, om ikke kan vi endre de fire under til frase: AfpEtteroppgjoerAvslutning
            includePhrase(DinePlikter)
            includePhrase(AfpEtteroppgjoerAvslutning.DuHarRettTilAaKlageSeksUkerMedLenke)
            includePhrase(DuHarRettTilInnsyn)
            includePhrase(HarDuSpoersmaal.afpEtteroppgjoer)
        }
    }
}
