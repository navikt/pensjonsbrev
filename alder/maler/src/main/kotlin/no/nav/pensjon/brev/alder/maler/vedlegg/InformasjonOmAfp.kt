package no.nav.pensjon.brev.alder.maler.vedlegg

import no.nav.pensjon.brev.api.model.maler.EmptyVedleggData
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text

// PE_AF_informasjon_om_afp_MR71 — statisk vedlegg om AFP og etteroppgjøret.
@TemplateModelHelpers
val vedleggInformasjonOmAfp =
    createAttachment<LangBokmalNynorsk, EmptyVedleggData>(
        title = {
            text(
                bokmal { +"Kort informasjon om AFP og etteroppgjøret" },
                nynorsk { +"Kort informasjon om AFP og etteroppgjeret" },
            )
        },
        includeSakspart = false,
    ) {
        title1 {
            text(
                bokmal { +"Beregning av AFP i forhold til inntekt" },
                nynorsk { +"Berekning av AFP i forhold til inntekt" },
            )
        }
        paragraph {
            text(
                bokmal {
                    +"Hvis du får arbeidsinntekt etter at du har tatt ut AFP, skal pensjonen reduseres. " +
                        "Det skal i utgangspunktet tas hensyn til all arbeidsinntekt, både lønns- og " +
                        "næringsinntekt. Den månedlige utbetalingen må derfor betraktes som en " +
                        "foreløpig utbetaling. Den endelige beregningen er først klar etter at den " +
                        "oppgitte inntekten er kontrollert mot likningen for dette utbetalingsåret."
                },
                nynorsk {
                    +"Dersom du får arbeidsinntekt etter at du har teke ut AFP, skal pensjonen " +
                        "reduserast. Det skal i utgangspunktet takast omsyn til all arbeidsinntekt, " +
                        "både lønns- og næringsinntekt. Den månadlege utbetalinga må ein derfor sjå " +
                        "på som ei foreløpig utbetaling. Den endelege berekninga er først klar etter " +
                        "at den oppgitte inntekta er kontrollert mot likninga for dette utbetalingsåret."
                },
            )
        }

        title1 {
            text(
                bokmal { +"Etteroppgjør" },
                nynorsk { +"Etteroppgjer" },
            )
        }
        paragraph {
            text(
                bokmal {
                    +"Nav vil foreta kontroll av den oppgitte arbeidsinntekten mot skatteetatens " +
                        "opplysninger. Dette vil bli gjort når likningen for de enkelte årene du har " +
                        "mottatt AFP er klar. Hvis det viser seg å være avvik på mer enn 15 000 " +
                        "kroner mellom den tidligere oppgitte arbeidsinntekten og den faktiske " +
                        "arbeidsinntekten, skal pensjonen beregnes på nytt ut fra den faktiske " +
                        "arbeidsinntekten i denne perioden."
                },
                nynorsk {
                    +"Nav vil kontrollere den oppgitte arbeidsinntekta mot skatteetatens " +
                        "opplysningar. Det vil bli gjort når likninga for dei enkelte åra du har " +
                        "fått AFP, er klar. Dersom det viser seg å vere avvik på meir enn 15 000 " +
                        "kroner mellom den tidlegare oppgitte arbeidsinntekta og den faktiske " +
                        "arbeidsinntekta, skal pensjonen bereknast på nytt ut frå den faktiske " +
                        "arbeidsinntekta i denne perioden."
                },
            )
        }

        title1 {
            text(
                bokmal { +"Hvilke inntekter blir AFP beregnet i forhold til?" },
                nynorsk { +"Kva inntekter blir AFP berekna i forhold til?" },
            )
        }
        paragraph {
            text(
                bokmal {
                    +"Både lønnsinntekt, feriepenger, fordel av forsikringspremier betalt av " +
                        "arbeidsgiver, personinntekt fra næring, honorarer osv. skal regnes med."
                },
                nynorsk {
                    +"Både lønnsinntekt, feriepengar, fordel av forsikringspremiar betalte av " +
                        "arbeidsgivar, personinntekt frå næring, honorar osv. skal reknast med."
                },
            )
        }


        paragraph {
            text(
                bokmal { +"Vær oppmerksom på følgende:" },
                nynorsk { +"Ver merksam på følgjande:" },
            )
            newline()
            text(
                bokmal {
                    +"Arbeidsinntekt som stammer fra arbeid eller virksomhet før første uttak av AFP " +
                        "skal ikke inntektsprøves mot pensjonen. Dette gjelder:"
                },
                nynorsk {
                    +"Arbeidsinntekt som stammar frå arbeid eller verksemd før første uttak av AFP, " +
                        "skal ikkje inntektsprøvast mot pensjonen. Det gjeld:"
                },
            )
            list {
                item {
                    text(
                        bokmal { +"feriepenger som er opptjent før uttak av AFP" },
                        nynorsk { +"feriepengar som er opptente før uttak av AFP" },
                    )
                }
                item {
                    text(
                        bokmal { +"eierinntekt fra aksjeselskap som pensjonisten ikke lenger er aktiv eier i" },
                        nynorsk { +"eigarinntekt frå aksjeselskap som pensjonisten ikkje lenger er aktiv eigar i" },
                    )
                }
                item {
                    text(
                        bokmal { +"lønn eller honorar som stammer fra arbeid eller virksomhet før AFP ble tatt ut" },
                        nynorsk { +"lønn eller honorar som stammar frå arbeid eller verksemd før uttak av AFP" },
                    )
                }
                item {
                    text(
                        bokmal { +"royalty eller bonus som stammer fra arbeid eller virksomhet før uttak av AFP" },
                        nynorsk { +"royalty eller bonus som stammar frå arbeid eller verksemd før uttak av AFP" },
                    )
                }
            }
        }
        paragraph {
            text(
                bokmal {
                    +"I forbindelse med etteroppgjøret, der oppgitt inntekt kontrolleres mot " +
                        "skatteetatens inntektsopplysninger, har vi ikke mulighet for å skille mellom " +
                        "arbeidsinntekt som stammer fra arbeid før og etter uttak av AFP i " +
                        "kalenderåret. Det er spesielt viktig at du er påpasselig med å varsle Nav " +
                        "om arbeidsinntekt som helt eller delvis stammer fra arbeid før uttak av " +
                        "AFP, slik at denne inntekten kan holdes utenfor den endelige beregningen."
                },
                nynorsk {
                    +"I samband med etteroppgjeret, der oppgitt inntekt blir kontrollert mot " +
                        "skatteetatens inntektsopplysningar, har vi ikkje høve til å skilje mellom " +
                        "arbeidsinntekt som stammar frå arbeid før og etter uttak av AFP i " +
                        "kalenderåret. Det er spesielt viktig at du passar på å varsle Nav om " +
                        "arbeidsinntekt som heilt eller delvis stammar frå arbeid før uttak av AFP, " +
                        "slik at denne inntekta kan haldast utanfor den endelege berekninga."
                },
            )
        }

        title1 {
            text(
                bokmal { +"Toleransebeløp/avviksbeløp" },
                nynorsk { +"Toleransebeløp/avviksbeløp" },
            )
        }
        paragraph {
            text(
                bokmal {
                    +"15 000 kroner er et avviksbeløp eller toleransebeløp som gir et slingringsmonn " +
                        "i forbindelse med etteroppgjøret. AFP vil ikke bli beregnet på nytt hvis " +
                        "forskjellen mellom tidligere oppgitt inntekt og inntekt som framgår av " +
                        "skatteligningen ikke overstiger dette beløpet."
                },
                nynorsk {
                    +"15 000 kroner er eit avviksbeløp eller toleransebeløp som gir ein " +
                        "slingringsmonn i samband med etteroppgjeret. AFP vil ikkje bli berekna på " +
                        "nytt dersom forskjellen mellom tidlegare oppgitt inntekt og inntekt som " +
                        "går fram av skattelikninga, ikkje overstig dette beløpet."
                },
            )
        }
        paragraph {
            text(
                bokmal {
                    +"Viser det seg at din faktiske arbeidsinntekt overstiger det du tidligere har " +
                        "oppgitt med mer enn 15 000 kroner, vil det bli tatt hensyn til hele " +
                        "arbeidsinntekten ved den nye beregningen av pensjonen. Du har da fått " +
                        "utbetalt for mye pensjon. Det for mye utbetalte vil bli trukket i den " +
                        "framtidige pensjonen din eller du kan, hvis du ønsker det, betale tilbake " +
                        "det du har mottatt for mye med en gang."
                },
                nynorsk {
                    +"Viser det seg at den faktiske arbeidsinntekta di er meir enn 15 000 kroner " +
                        "høgare enn det du tidlegare har oppgitt, vil det bli teke omsyn til heile " +
                        "arbeidsinntekta når pensjonen blir berekna på nytt. Du har då fått " +
                        "utbetalt for mykje pensjon. Det som er for mykje utbetalt, vil bli trekt i " +
                        "den framtidige pensjonen din. Du kan òg betale tilbake det du har fått for " +
                        "mykje, med ein gong dersom du ønskjer det."
                },
            )
        }
        paragraph {
            text(
                bokmal {
                    +"Viser det seg at den faktiske arbeidsinntekten har vært lavere enn det du " +
                        "tidligere har oppgitt og differansen er større enn 15 000 kroner, vil du få " +
                        "etterbetalt pensjon."
                },
                nynorsk {
                    +"Viser det seg at den faktiske arbeidsinntekta har vore lågare enn du tidlegare " +
                        "har oppgitt, og differansen er større enn 15 000 kroner, vil du få " +
                        "etterbetalt pensjon."
                },
            )
        }
    }
