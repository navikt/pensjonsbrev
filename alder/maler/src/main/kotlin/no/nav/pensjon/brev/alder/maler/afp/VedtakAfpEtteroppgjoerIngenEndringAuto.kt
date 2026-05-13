package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.maler.felles.Constants.AFP_ETTEROPPGJOER_URL
import no.nav.pensjon.brev.alder.maler.felles.Constants.AFP_OFFENTLIG_URL
import no.nav.pensjon.brev.alder.maler.felles.Constants.KLAGE_URL
import no.nav.pensjon.brev.alder.maler.felles.Constants.KONTAKT_URL
import no.nav.pensjon.brev.alder.maler.felles.Constants.NAV_KONTAKTSENTER_AAPNINGSTID
import no.nav.pensjon.brev.alder.maler.felles.Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON
import no.nav.pensjon.brev.alder.maler.felles.Constants.NAV_URL
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
            paragraph {
                text(
                    bokmal {
                        +"Hvert år foretar NAV en kontroll av AFP når resultatet av skatteoppgjøret " +
                            "foreligger. Hvis du har hatt en annen pensjonsgivende inntekt fra arbeid eller " +
                            "næring enn den forventede inntekten som ble lagt til grunn ved utbetalingen, skal " +
                            "vi foreta en ny beregning av pensjonen på grunnlag av inntektsopplysningene fra " +
                            "Skatteetaten."
                    },
                    nynorsk {
                        +"Kvart år gjennomfører NAV ein kontroll av AFP når resultatet av skatteoppgjeret er " +
                            "klart. Dersom du har hatt ei anna pensjonsgivande inntekt frå arbeid eller næring " +
                            "enn den framtidige inntekta som blei lagd til grunn ved utbetalinga, skal vi " +
                            "berekne pensjonen på nytt på grunnlag av inntektsopplysningane frå Skatteetaten."
                    },
                )
            }

            paragraph {
                text(
                    bokmal {
                        +"Vi har ikke funnet grunnlag for å endre beregningen av pensjonen din i " +
                            oppgjoersAar.format() + " på bakgrunn av inntektsopplysningene fra Skatteetaten."
                    },
                    nynorsk {
                        +"Vi har ikkje funne grunnlag for å endre berekninga av pensjonen din i " +
                            oppgjoersAar.format() + " på bakgrunn av inntektsopplysningane frå Skatteetaten."
                    },
                )
            }

            paragraph {
                text(
                    bokmal {
                        +"Vedtaket er gjort etter lov om AFP for medlemmer av Statens pensjonskasse § 3 " +
                            "bokstav d, og tilhørende forskrift om kombinasjon av avtalefestet pensjon for " +
                            "medlemmer av Statens pensjonskasse og arbeidsinntekt (pensjonsgivende inntekt)."
                    },
                    nynorsk {
                        +"Vedtaket er gjort etter lov om AFP for medlemmer av Statens pensjonskasse § 3 " +
                            "bokstav d og tilhøyrande forskrift om kombinasjon av avtalefestet pensjon for " +
                            "medlemmer av Statens pensjonskasse og arbeidsinntekt (pensjonsgivende inntekt)."
                    },
                )
            }

            title1 {
                text(
                    bokmal { +"Inntekten din i " + oppgjoersAar.format() },
                    nynorsk { +"Inntekta di i " + oppgjoersAar.format() },
                )
            }

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

            paragraph {
                text(
                    bokmal { +"Pensjonsberegningen for " + oppgjoersAar.format() + " blir derfor ikke endret." },
                    nynorsk { +"Pensjonsberekninga for " + oppgjoersAar.format() + " blir derfor ikkje endra." },
                )
            }

            // Tilleggstekst når brukeren ikke har fått utbetalt AFP grunnet full inntekt:
            // detaljer om frist, dokumentasjon og særskilte regler for covid-19/Ukraina.
            showIf(scenario.equalTo(Scenario.IKKE_AFP_FULL_INNTEKT)) {
                title1 {
                    text(
                        bokmal { +"Melding om endringer av inntekten" },
                        nynorsk { +"Melding om endringar av inntekta" },
                    )
                }

                paragraph {
                    text(
                        bokmal {
                            +"Hvis du mener at inntekten som ligger til grunn for beregningen er feil, må du " +
                                "melde fra til oss innen fire uker fra du mottok dette brevet."
                        },
                        nynorsk {
                            +"Dersom du meiner at inntekta som ligg til grunn for berekninga, er feil, må du " +
                                "melde frå til oss innan fire veker frå du fekk dette brevet."
                        },
                    )
                }

                paragraph {
                    text(
                        bokmal {
                            +"Inntekt fra arbeid eller virksomhet som er opptjent før første uttak av AFP " +
                                "skal holdes utenfor etteroppgjøret. Dette gjelder også hvis inntekten er " +
                                "utbetalt etter at du tok ut AFP. Med første uttak menes første gang du tok " +
                                "ut AFP, uavhengig av om du har tatt ut gradert eller hel pensjon."
                        },
                        nynorsk {
                            +"Inntekt frå arbeid eller verksemd som er opptent før det første uttaket av AFP, " +
                                "skal haldast utanfor etteroppgjeret. Det gjeld også dersom inntekta er " +
                                "utbetalt etter at du tok ut AFP. Med første uttak meiner vi første gong du " +
                                "tok ut AFP, uavhengig av om du har teke ut gradert eller heil pensjon."
                        },
                    )
                }

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

                paragraph {
                    text(
                        bokmal {
                            +"Annen inntekt som er opptjent samtidig med utbetaling av AFP, vil bli " +
                                "inntektsprøvd mot pensjonen. Vi gjør for ordens skyld oppmerksom på at " +
                                "feriepenger opptjent etter første uttak av AFP skal inntektsprøves mot " +
                                "pensjonen."
                        },
                        nynorsk {
                            +"Anna inntekt som er opptent samtidig med utbetaling av AFP, vil bli " +
                                "inntektsprøvd mot pensjonen. Vi gjer for ordens skuld merksam på at " +
                                "feriepengar som er opptente etter uttak av AFP, skal inntektsprøvast mot " +
                                "pensjonen."
                        },
                    )
                }

                paragraph {
                    // TODO originale bokmål teksten er kjempe-rar, og den er slik i exstream også! Dette må jo bare være feil... dobbelt-sjekket i exstream og det er akkuratt slik innholdet er:
                    /*text (
                        bokmal { + "Du må dokumentere hvilke av inntektene dine som skal holdes utenfor avkorting av AFP.at inntekten er opptjent eller stammer fra arbeid eller virksomhet før uttaket av AFP. Hvis du ikke sender inn ny dokumentasjon innen fristen, benytter vi de foreliggende opplysningene vi har, og etteroppgjøret vil bli regnet vurdert som avsluttet. " },
                        nynorsk { + "Du må dokumentere kva delar av inntektene dine som skal haldast utanfor avkorting av AFP. Dersom du ikkje sender inn ny dokumentasjon innan fristen, nyttar vi dei opplysningane vi har, og etteroppgjeret blir rekna som avslutta. " },
                    )*/

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

                paragraph {
                    text(
                        bokmal {
                            +"På $AFP_ETTEROPPGJOER_URL finner du et skjema som du eller arbeidsgiver kan " +
                                "benytte ved innsending av dokumentasjon."
                        },
                        nynorsk {
                            +"På $AFP_ETTEROPPGJOER_URL finn du eit skjema som du eller arbeidsgivaren kan " +
                                "nytte ved innsending av dokumentasjon."
                        },
                    )
                }

                title1 {
                    text(
                        bokmal { +"Spesielt om inntekter opptjent i forbindelse med covid-19" },
                        nynorsk { +"Spesielt om inntekter opptente i samband med covid-19" },
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"Dersom du har påtatt deg ekstra covid-19-relatert arbeid ut over det du " +
                                "vanligvis jobber, kan det hende at inntekten din fra dette arbeidet ikke " +
                                "skal gi avkorting av AFP. Dette gjelder feriepenger av inntekt fra tjeneste " +
                                "fram til 30. juni 2023 som var nødvendig for å ivareta et ekstraordinært " +
                                "personellbehov ved TISK-tiltak (testing, isolering, smittesporing og " +
                                "karantene) eller vaksinering. Det gjelder bare tjeneste med pensjonistlønn " +
                                "etter særskilt sats."
                        },
                        nynorsk {
                            +"Dersom du har teke på deg ekstra covid-19-relatert arbeid utover det du " +
                                "vanlegvis jobbar, kan det hende at inntekta di frå dette arbeidet ikkje " +
                                "skal gi avkorting av AFP. Dette gjeld feriepengane av inntekt frå teneste " +
                                "fram til 30. juni 2023 som var naudsynt for å ivareta personellbehov ved " +
                                "TISK-tiltak (testing, isolering, smittesporing og karantene) eller " +
                                "vaksinering. Det gjeld berre teneste med pensjonistlønn etter særskilt sats."
                        },
                    )
                }

                paragraph {
                    text(
                        bokmal {
                            +"Reglene om dette finnes i forskrift 19. september 2023 nr. 1446 om endring i " +
                                "forskrift om kombinasjon av avtalefestet pensjon for medlemmer av Statens " +
                                "pensjonskasse og arbeidsinntekt (pensjonsgivende inntekt) kapittel II nr. 2."
                        },
                        nynorsk {
                            +"Reglane om dette finst i forskrift 19. september 2023 nr. 1446 om endring i " +
                                "forskrift om kombinasjon av avtalefestet pensjon for medlemmer av Statens " +
                                "pensjonskasse og arbeidsinntekt (pensjonsgivande inntekt) kapittel II nr. 2."
                        },
                    )
                }
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

                title1 {
                    text(
                        bokmal {
                            +"Spesielt om unntak for pensjonistlønn etter særskilt sats for pensjonister som " +
                                "jobber med fordrevne fra Ukraina"
                        },
                        nynorsk {
                            +"Spesielt om unntak for pensjonistlønn etter særskilt sats for pensjonistar som " +
                                "har arbeidd med fordrivne frå Ukraina"
                        },
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"Dersom du har arbeidet med fordrevne fra Ukraina og mottatt pensjonistlønn etter " +
                                "særskilt sats kan inntekten for dette arbeidet unntas fra inntektsavkorting. " +
                                "Det samme gjelder feriepenger for arbeid utført etter 1. april 2022. Reglene " +
                                "om inntekter fra arbeid med fordrevne fra Ukraina finnes i forskrift om " +
                                "kombinasjon av AFP og arbeidsinntekt § 2 tredje ledd."
                        },
                        nynorsk {
                            +"Dersom du har arbeidd med fordrivne frå Ukraina og motteke pensjonistlønn etter " +
                                "særskilt sats kan inntekta for dette arbeidet unntakas frå inntektsavkorting. " +
                                "Det same gjeld feriepengar for slikt arbeid utført etter 1. april 2022. " +
                                "Reglane om inntekter frå arbeid med fordrivne frå Ukraina finst i forskrift " +
                                "om kombinasjon av AFP og arbeidsinntekt § 2 tredje ledd."
                        },
                    )
                }

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
            title1 {
                text(
                    bokmal { +"Dine plikter" },
                    nynorsk { +"Dine plikter" },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Du har plikt til å melde fra om endringer som har betydning for størrelsen på " +
                            "pensjonen din."
                    },
                    nynorsk {
                        +"Du pliktar å melde frå om endringar som har noko å seie for storleiken på pensjonen " +
                            "din."
                    },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Du må melde fra om endringer i egen inntekt, i familieforhold og ved flytting innen " +
                            "Norge eller til land utenfor Norge. Hvis du mottar grunnpensjon etter sats for " +
                            "enslige, må du melde fra hvis ektefellen, partneren eller samboeren din får en " +
                            "årlig inntekt som er større enn to ganger folketrygdens grunnbeløp eller hvis " +
                            "vedkommende får uføretrygd eller egen pensjon."
                    },
                    nynorsk {
                        +"Du må melde frå om endringar i eiga inntekt eller i familieforhold og ved flytting " +
                            "innanfor Noreg eller til land utanfor Noreg. Dersom du får grunnpensjon etter " +
                            "sats for einslege, må du melde frå dersom ektefellen, partnaren eller sambuaren " +
                            "din får ei årleg inntekt som er større enn to gonger grunnbeløpet i folketrygda, " +
                            "eller dersom vedkommande får eigen pensjon."
                    },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Hvis du får utbetalt for mye pensjon fordi du ikke har meldt fra om endringer, kan " +
                            "vi kreve tilbake det som er for mye utbetalt."
                    },
                    nynorsk {
                        +"Dersom du får utbetalt for mykje pensjon fordi du ikkje har meldt frå om endringar, " +
                            "kan vi krevje tilbake det som er for mykje utbetalt."
                    },
                )
            }

            title1 {
                text(
                    bokmal { +"Du har rett til å klage" },
                    nynorsk { +"Du har rett til å klage" },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Hvis du mener vedtaket er feil, kan du klage innen seks uker fra den datoen du " +
                            "mottok vedtaket. Klagen skal være skriftlig. Du finner skjema og informasjon på " +
                            "$KLAGE_URL."
                    },
                    nynorsk {
                        +"Dersom du meiner at vedtaket er feil, kan du klage innan seks veker frå den datoen " +
                            "då du fekk vedtaket. Klaga skal vere skriftleg. Du finn skjema og informasjon på " +
                            "$KLAGE_URL."
                    },
                )
            }

            title1 {
                text(
                    bokmal { +"Du har rett til innsyn" },
                    nynorsk { +"Du har rett til innsyn" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Du har rett til å se dokumentene i saken din." },
                    nynorsk { +"Du har rett til å sjå dokumenta i saka di." },
                )
            }

            title1 {
                text(
                    bokmal { +"Har du spørsmål?" },
                    nynorsk { +"Har du spørsmål?" },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Du finner mer informasjon på $AFP_OFFENTLIG_URL#etteroppgjor. På $KONTAKT_URL " +
                            "kan du chatte eller skrive til oss. Hvis du ikke finner svar på $NAV_URL, kan du " +
                            "ringe oss på telefon $NAV_KONTAKTSENTER_TELEFON_PENSJON, hverdager " +
                            "$NAV_KONTAKTSENTER_AAPNINGSTID."
                    },
                    nynorsk { // TODO heldigvis leder disse linkene til samme sted, men det må jo ikke være forskjellig til nynorsk/bokmål brukere.
                        +"Du finn meir informasjon på $AFP_ETTEROPPGJOER_URL. På $KONTAKT_URL kan du " +
                            "chatte eller skrive til oss. Om du ikkje finn svar på $NAV_URL, kan du ringe oss " +
                            "på telefon $NAV_KONTAKTSENTER_TELEFON_PENSJON, kvardagar " +
                            "$NAV_KONTAKTSENTER_AAPNINGSTID."
                    },
                )
            }
        }
    }
}
