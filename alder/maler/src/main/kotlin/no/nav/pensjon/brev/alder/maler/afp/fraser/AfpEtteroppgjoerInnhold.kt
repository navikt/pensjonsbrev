package no.nav.pensjon.brev.alder.maler.afp.fraser

import no.nav.pensjon.brev.alder.maler.felles.Constants.AFP_ETTEROPPGJOER_URL
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year

/**
 * Felles innhold som går igjen i AFP etteroppgjør-vedtaksbrev (offentlig sektor /
 * Statens pensjonskasse). Avslutningen (Dine plikter / klage / innsyn / spørsmål)
 * ligger i [AfpEtteroppgjoerAvslutning]; her samler vi de øvrige paragrafene som
 * deles mellom brevene `VedtakAfpEtteroppgjoerIngenEndringAuto` (PE_AF_04_102),
 * `VedtakAfpEtteroppgjoerToleransebeloepAuto` (PE_AF_04_100) m.fl.
 */
object AfpEtteroppgjoerInnhold {

    /**
     * Innledning som forklarer at NAV gjør et årlig etteroppgjør på grunnlag av
     * inntektsopplysninger fra Skatteetaten. Identisk i alle ingen-endring-brev.
     */
    object EtteroppgjoerIntro : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
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
        }
    }

    /**
     * Konklusjonsparagraf: vi har ikke funnet grunnlag for å endre beregningen
     * av pensjonen for {oppgjørsår}.
     */
    data class IkkeFunnetGrunnlagForAaEndre(
        val oppgjoersAar: Expression<Year>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
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
        }
    }

    /**
     * Hjemmelshenvisning til AFP-loven for medlemmer av Statens pensjonskasse.
     */
    object VedtaksgrunnlagAfpSpk : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
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
        }
    }

    /** Title1 «Inntekten din i {oppgjørsår}». */
    data class InntektenDinIAarTittel(
        val oppgjoersAar: Expression<Year>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            title1 {
                text(
                    bokmal { +"Inntekten din i " + oppgjoersAar.format() },
                    nynorsk { +"Inntekta di i " + oppgjoersAar.format() },
                )
            }
        }
    }

    /** Avsluttende konklusjonsparagraf: pensjonsberegningen blir derfor ikke endret. */
    data class PensjonsberegningenBlirIkkeEndret(
        val oppgjoersAar: Expression<Year>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal { +"Pensjonsberegningen for " + oppgjoersAar.format() + " blir derfor ikke endret." },
                    nynorsk { +"Pensjonsberekninga for " + oppgjoersAar.format() + " blir derfor ikkje endra." },
                )
            }
        }
    }

    /**
     * Tittel «Melding om endringer av inntekten» + de to innledende paragrafene
     * som er identiske mellom PE_AF_04_100 og PE_AF_04_102.
     */
    object MeldingOmEndringerInnledning : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
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
                            "melde fra til oss innen "
                    },
                    nynorsk {
                        +"Dersom du meiner at inntekta som ligg til grunn for berekninga, er feil, må du " +
                            "melde frå til oss innan "
                    },
                )
                text(
                    bokmal { +"fire uker" },
                    nynorsk { +"fire veker" },
                    BOLD,
                )
                text(
                    bokmal { +" fra du mottok dette brevet." },
                    nynorsk { +" frå du fekk dette brevet." },
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
        }
    }

    /**
     * Paragrafen «Annen inntekt som er opptjent samtidig med utbetaling av AFP...».
     * Identisk mellom PE_AF_04_100 og PE_AF_04_102.
     */
    object AnnenInntektInntektsproevd : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
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
        }
    }

    /**
     * Paragrafen «På {url} finner du et skjema...». Identisk mellom
     * PE_AF_04_100 og PE_AF_04_102.
     */
    object SkjemaForDokumentasjon : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
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
        }
    }

    /**
     * Title1 «Spesielt om inntekter opptjent i forbindelse med covid-19» + de to
     * påfølgende paragrafene som er identiske mellom PE_AF_04_100 og PE_AF_04_102.
     */
    object SpesieltOmCovidInntekterInnledning : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
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
        }
    }

    /**
     * Title1 «Spesielt om unntak ... fordrevne fra Ukraina» + den påfølgende
     * forklaringsparagrafen. Identisk mellom PE_AF_04_100 og PE_AF_04_102.
     */
    object SpesieltOmUkrainaUnntak : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
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
        }
    }

}
