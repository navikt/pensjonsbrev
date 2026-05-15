package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerAvslutning
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerInnhold
import no.nav.pensjon.brev.alder.maler.felles.Constants.AFP_ETTEROPPGJOER_URL
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringAutoDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringAutoDto.Scenario
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringAutoDtoSelectors.oppgjoersAar
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringAutoDtoSelectors.pgi
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringAutoDtoSelectors.scenario
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
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
 * (se [VedtakAfpEtteroppgjoerIngenEndringAutoDto.Scenario]).
 *
 * Konverterte avvik fra kilden (Step 7 i convert-exstream-letter-skill):
 *  - De fire `showIf`-blokkene som beskriver scenariene var i originalen
 *    overlappende og brukte rådata. Her uttrykkes valget som én diskriminator
 *    levert av kalleren.
 *  - Originalen hadde ti repeterte `showIf` med identisk gating
 *    (`UtbetaltAFP=0 AND PGI>=TPIberegnet`) for tilleggstekstene under
 *    [Scenario.IKKE_AFP_FULL_INNTEKT]; slått sammen til én blokk.
 *  - "Vennlig hilsen" + avsenderenhet er fjernet — brevbaker-rammeverket setter
 *    signaturen selv via fellesAuto.
 *  - To "TODO Merge failure"-paragrafer fra konverteringen og flere
 *    bokmål/nynorsk-feiljusterte paragrafer i avslutningen er manuelt rettet
 *    opp ved sammenslåing av tilsvarende språkvarianter.
 */
@TemplateModelHelpers
object VedtakAfpEtteroppgjoerIngenEndringAuto : AutobrevTemplate<VedtakAfpEtteroppgjoerIngenEndringAutoDto> {

    override val kode = Aldersbrevkoder.AutoBrev.PE_AFP_ETTEROPPGJOER_INGEN_ENDRING_AUTO

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
            includePhrase(AfpEtteroppgjoerInnhold.VedtaksgrunnlagAfpSpk)
            includePhrase(AfpEtteroppgjoerInnhold.InntektenDinIAarTittel(oppgjoersAar))

            // Scenario A — 100% AFP, all inntekt før uttak
            showIf(scenario.equalTo(Scenario.HEL_AFP_HELE_AARET_INNTEKT_FOER_UTTAK)) {
                paragraph {
                    text(
                        bokmal {
                            +"Du har fått utbetalt 100 prosent AFP. Opplysninger fra Skatteetaten viser at du " +
                                "har hatt en samlet pensjonsgivende inntekt på " + pgi.format() + " for " +
                                "inntektsåret " + oppgjoersAar.format() + ". Vi antar at hele denne inntekten " +
                                "er opptjent i perioden før du tok ut pensjon, og den blir derfor holdt utenfor " +
                                "etteroppgjøret."
                        },
                        nynorsk {
                            +"Du har fått utbetalt 100 prosent AFP. Opplysningar frå Skatteetaten viser at du " +
                                "har hatt ei samla pensjonsgivande inntekt på " + pgi.format() + " for " +
                                "inntektsåret " + oppgjoersAar.format() + ". Vi reknar med at heile denne " +
                                "inntekta er opptent i perioden før du tok ut pensjon, og ho blir derfor halden " +
                                "utanfor etteroppgjeret."
                        },
                    )
                }
            }

            // Scenario B — 100% AFP, ingen inntekt
            showIf(scenario.equalTo(Scenario.HEL_AFP_HELE_AARET_INGEN_INNTEKT)) {
                paragraph {
                    text(
                        bokmal {
                            +"Du har fått utbetalt 100 prosent AFP. Ved beregningen av pensjonen har vi lagt " +
                                "til grunn at du ikke ville ha arbeidsinntekt. Ifølge opplysninger fra " +
                                "Skatteetaten har du ikke hatt pensjonsgivende inntekt for inntektsåret " +
                                oppgjoersAar.format() + "."
                        },
                        nynorsk {
                            +"Du har fått utbetalt 100 prosent AFP. Ved berekninga av pensjonen har vi lagt " +
                                "til grunn at du ikkje ville ha arbeidsinntekt. Ifølgje opplysningar frå " +
                                "Skatteetaten har du ikkje hatt pensjonsgivande inntekt for inntektsåret " +
                                oppgjoersAar.format() + "."
                        },
                    )
                }
            }

            // Scenario C — 0% AFP, full inntekt
            showIf(scenario.equalTo(Scenario.IKKE_AFP_FULL_INNTEKT)) {
                paragraph {
                    text(
                        bokmal {
                            +"Du har ikke fått utbetalt pensjon fra AFP-ordningen for året " +
                                oppgjoersAar.format() + " som følge av at du har vært i fullt inntektsgivende " +
                                "arbeid. Opplysninger fra Skatteetaten viser at du har hatt en pensjonsgivende " +
                                "inntekt på " + pgi.format() + " for " + oppgjoersAar.format() + ". " +
                                "Dette samsvarer med det som tidligere er lagt til grunn ved pensjonsberegningen."
                        },
                        nynorsk {
                            +"Du har ikkje fått utbetalt pensjon frå AFP-ordninga for året " +
                                oppgjoersAar.format() + " som følgje av at du har vore i fullt inntektsgivande " +
                                "arbeid. Opplysningar frå Skatteetaten viser at du har hatt ei pensjonsgivande " +
                                "inntekt på " + pgi.format() + " for " + oppgjoersAar.format() + ". " +
                                "Det samsvarer med det som tidlegare er lagt til grunn ved pensjonsberekninga."
                        },
                    )
                }
            }

            // Scenario D — 100% AFP deler av året, inntekt under toleransebeløpet
            showIf(scenario.equalTo(Scenario.HEL_AFP_DELER_AV_AARET)) {
                paragraph {
                    text(
                        bokmal {
                            +"Du har fått utbetalt 100 prosent AFP for deler av året. Ved beregningen av " +
                                "pensjonen har vi lagt til grunn at du ikke ville ha arbeidsinntekt som " +
                                "overstiger godkjent toleransebeløp på 15 000 kroner i perioden med AFP. " +
                                "Ifølge opplysninger fra Skatteetaten har du heller ikke hatt slik " +
                                "pensjonsgivende inntekt i " + oppgjoersAar.format() + "."
                        },
                        nynorsk {
                            +"Du har fått utbetalt 100 prosent AFP for delar av året. Ved berekninga av " +
                                "pensjonen har vi lagt til grunn at du ikkje ville ha arbeidsinntekt som " +
                                "oversteig det godkjende toleransebeløpet på 15 000 kroner i perioden med " +
                                "AFP. Ifølgje opplysningar frå Skatteetaten har du heller ikkje hatt slik " +
                                "pensjonsgivande inntekt i " + oppgjoersAar.format() + "."
                        },
                    )
                }
            }

            includePhrase(AfpEtteroppgjoerInnhold.PensjonsberegningenBlirIkkeEndret(oppgjoersAar))

            showIf(scenario.equalTo(Scenario.IKKE_AFP_FULL_INNTEKT)) {
                // Seksjon 1: Melding om endringer av inntekten.
                includePhrase(AfpEtteroppgjoerInnhold.MeldingOmEndringerInnledning)

                // Inlinet: «Inntekt som skal holdes utenfor etteroppgjøret» — covid-19-punktet
                // i listen avviker fra PE_AF_04_100.
                paragraph {
                    text(
                        bokmal { +"Inntekt som skal holdes utenfor etteroppgjøret:" },
                        nynorsk { +"Inntekt som skal haldast utanfor etteroppgjeret:" },
                    )
                    list {
                        item {
                            text(
                                bokmal { +"Feriepenger og lønn som er opptjent før første uttak av AFP." },
                                nynorsk { +"Feriepengar og lønn som er opptente før første uttaket av AFP." },
                            )
                        }
                        item {
                            text(
                                bokmal {
                                    +"Honorar, royalty, bonus eller andre inntekter som stammer fra arbeid " +
                                        "eller virksomhet før første uttak av AFP."
                                },
                                nynorsk {
                                    +"Honorar, royalty, bonus eller andre inntekter som stammar frå arbeid " +
                                        "eller verksemd før det første uttaket av AFP."
                                },
                            )
                        }
                        item {
                            text(
                                bokmal {
                                    +"Etterbetaling av trygdeytelser som gjelder for tidsrom før AFP ble " +
                                        "tatt ut."
                                },
                                nynorsk {
                                    +"Etterbetaling av trygdeytingar som gjeld for tidsrom før AFP vart " +
                                        "tatt ut."
                                },
                            )
                        }
                        // TODO ordlydsavvik vs PE_AF_04_100: 04_100 har "Feriepenger som stammer fra enkelte typer arbeid i forbindelse med covid-19." / "...samband med covid-19" (uten "-pandemien"). Avklar med fag om ordlyden skal harmoniseres.
                        item {
                            text(
                                bokmal { +"Feriepenger som stammer fra arbeid med covid-19." },
                                nynorsk {
                                    +"Feriepengar som skriv seg frå enkelte typar arbeid i samband med " +
                                        "covid-19-pandemien."
                                },
                            )
                        }
                        item {
                            text(
                                bokmal {
                                    +"Inntekt som stammer fra arbeid i forbindelse med fordrevne fra Ukraina."
                                },
                                nynorsk {
                                    +"Inntekt som skriv seg frå arbeid i forbindelse med fordrivne frå Ukraina."
                                },
                            )
                        }
                    }
                    text(
                        bokmal {
                            +"Se mer informasjon om arbeid i forbindelse med covid-19 og fordrevne fra " +
                                "Ukraina nedenfor."
                        },
                        nynorsk {
                            +"Sjå meir informasjon om arbeid i forbindelse med covid-19 og fordrivne frå " +
                                "Ukraina nedanfor."
                        },
                    )
                }

                includePhrase(AfpEtteroppgjoerInnhold.AnnenInntektInntektsproevd)

                // Inlinet: «Du må dokumentere ...» — bokmål avviker fra PE_AF_04_100.
                // TODO bokmål-ordlyd avviker fra PE_AF_04_100: 04_100 har "Du må dokumentere hvilke av inntektene dine som skal holdes utenfor avkorting av AFP. ... benytter vi de opplysningene vi har ...". Avklar med fag.
                paragraph {
                    text(
                        bokmal {
                            +"Du må dokumentere at inntekten er opptjent eller stammer fra arbeid eller " +
                                "virksomhet før uttaket av AFP. Hvis du ikke sender inn ny dokumentasjon innen " +
                                "fristen, benytter vi de foreliggende opplysningene vi har, og etteroppgjøret " +
                                "vil bli vurdert som avsluttet."
                        },
                        nynorsk {
                            +"Du må dokumentere kva delar av inntektene dine som skal haldast utanfor " +
                                "avkorting av AFP. Dersom du ikkje sender inn ny dokumentasjon innan fristen, " +
                                "nyttar vi dei opplysningane vi har, og etteroppgjeret blir rekna som avslutta."
                        },
                    )
                }

                includePhrase(AfpEtteroppgjoerInnhold.SkjemaForDokumentasjon)

                // Seksjon 2: Spesielt om inntekter opptjent i forbindelse med covid-19.
                includePhrase(AfpEtteroppgjoerInnhold.SpesieltOmCovidInntekterInnledning)

                // Inlinet: covid-19-dokumentasjonslisten — avviker fra PE_AF_04_100.
                // TODO dokumentasjonslisten avviker fra PE_AF_04_100: 04_100 har 3 punkter (uten "om arbeidet var beordret eller frivillig") og ordlyd "hvilken tidsperiode(-r) feriepengene er opptjent". Avklar med fag om listen skal harmoniseres.
                paragraph {
                    text(
                        bokmal {
                            +"For at NAV skal kunne holde slike inntekter utenfor avkorting, må du sende " +
                                "oss dokumentasjon. Se mer på $AFP_ETTEROPPGJOER_URL om hvordan du sender " +
                                "dokumentasjon. Vi trenger bekreftelse fra arbeidsgiveren din om følgende:"
                        },
                        nynorsk {
                            +"For at NAV skal kunne halde slike inntekter utanfor avkorting, må du sende " +
                                "oss dokumentasjon. Sjå meir på $AFP_ETTEROPPGJOER_URL. Vi treng stadfesting " +
                                "frå arbeidsgivaren din om følgjande:"
                        },
                    )
                    list {
                        item {
                            text(
                                bokmal { +"hvor mye du har hatt i inntekter fra slikt ekstra arbeid" },
                                nynorsk { +"kor mykje du har hatt i feriepengar frå slikt ekstra arbeid" },
                            )
                        }
                        item {
                            text(
                                bokmal { +"i hvilken tidsperiode(-r) dette gjelder" },
                                nynorsk { +"i kva for tidsperiode (-periodar) feriepengane er tent opp" },
                            )
                        }
                        item {
                            text(
                                bokmal { +"om arbeidet var beordret eller frivillig" },
                                nynorsk { +"om arbeidet var beordra eller frivillig" },
                            )
                        }
                        item {
                            text(
                                bokmal { +"om utbetalingen er gjort etter særskilt sats for pensjonistavlønning" },
                                nynorsk { +"om utbetalinga er gjort etter særskilt sats for pensjonistavlønning" },
                            )
                        }
                    }
                }

                // Seksjon 3: Spesielt om unntak ... fordrevne fra Ukraina.
                includePhrase(AfpEtteroppgjoerInnhold.SpesieltOmUkrainaUnntak)

                // Inlinet: Ukraina-dokumentasjonslisten — avviker fra PE_AF_04_100.
                // TODO dokumentasjonslisten avviker fra PE_AF_04_100: 04_100 bokmål bruker "inntekter" (ikke "feriepenger") i første punkt, og 04_100 nynorsk har "feriepengane er tent opp" i tidsperiode-punktet. Avklar med fag.
                paragraph {
                    text(
                        bokmal {
                            +"For at NAV skal kunne holde slike inntekter utenfor avkorting, må du sende oss " +
                                "dokumentasjon. Se mer på $AFP_ETTEROPPGJOER_URL om hvordan du sender " +
                                "dokumentasjon. Vi trenger bekreftelse fra arbeidsgiveren din om følgende:"
                        },
                        nynorsk {
                            +"For at NAV skal kunne halde slike inntekter utanfor avkorting, må du sende oss " +
                                "dokumentasjon. Sjå meir på $AFP_ETTEROPPGJOER_URL. Vi treng stadfesting frå " +
                                "arbeidsgivaren din om følgjande:"
                        },
                    )
                    list {
                        item {
                            text(
                                bokmal { +"hvor mye du har hatt i feriepenger fra slikt ekstra arbeid" },
                                nynorsk { +"kor mykje du har hatt i inntekter frå slikt ekstra arbeid" },
                            )
                        }
                        item {
                            text(
                                bokmal { +"i hvilken tidsperiode(-r) dette gjelder" },
                                nynorsk { +"i kva for tidsperiode (-periodar) dette gjeld" },
                            )
                        }
                        item {
                            text(
                                bokmal { +"om utbetalingen er gjort etter særskilt sats for pensjonistavlønning" },
                                nynorsk { +"om utbetalinga er gjort etter særskilt sats for pensjonistavlønning" },
                            )
                        }
                    }
                }
            }


            // Avslutning — rettigheter, plikter og kontaktinformasjon (alltid).
            includePhrase(AfpEtteroppgjoerAvslutning)
        }
    }
}
