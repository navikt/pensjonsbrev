package no.nav.pensjon.brev.maler.adhoc

import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

object AdhocInformasjonOpphorGjenlevendepensjon : AutobrevTemplate<EmptyAutobrevdata> {
    override val kode = Pesysbrevkoder.AutoBrev.GJP_INFOBREV_OPPHOR_2027
    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Gjenlevendepensjon – informasjon om forlenget periode",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                bokmal { + "Gjenlevendepensjon – informasjon om forlenget periode" },
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { + "Du har mottatt vedtak om at din gjenlevendepensjon opphører fra 1. januar 2027. " },
                )
            }

            title1 {
                text(
                    bokmal { + "Du kan ha rett til forlenget periode" },
                )
            }
            paragraph {
                text(
                    bokmal { + "Hvis du tar utdanning godkjent av Nav, eller hvis du har arbeidsrettet tiltak via Nav, kan pensjonen forlenges i inntil 2 år. " },
                )
            }
            paragraph {
                text(
                    bokmal { + "Arbeidsrettet tiltak kan for eksempel være arbeidstrening, kurs eller andre tiltak for å komme i jobb. " },
                )
            }
            paragraph {
                text(
                    bokmal { + "For å fylle vilkårene til forlenget periode med gjenlevendepensjon, må du fra 1. januar 2027 være i tiltak eller under utdanning som er nødvendig og hensiktsmessig, med mål om å komme i arbeid. Utdanningen eller tiltaket må utgjøre minst 50 prosent og være godkjent av ditt lokale Nav-kontor. Du finner informasjon om dette på nav.no/gjenlevendepensjon#hvor-lenge. " },
                )
            }
            paragraph {
                text(
                    bokmal { + "Du må ha en plan for tiltak eller utdanning fra 1. januar 2027. Er du ikke i tiltak eller utdanning 1. januar 2027, må du ha en konkret plan med ditt lokale Nav-kontor om oppstart på et senere tidspunkt. Planen må godkjennes av Nav-kontoret innen utgangen av 2026. " },
                )
            }
            paragraph {
                text(
                    bokmal { + "Har du helseproblemer som gjør at du ikke kan stå i arbeid eller være i tiltak eller utdanning for å komme i arbeid, kan du ikke få forlenget gjenlevendepensjon. Trenger du hjelp til andre ytelser, må du kontakte ditt lokale Nav-kontor." },
                )
            }

            title1 {
                text(
                    bokmal { + "Søke om forlenget periode" },
                )
            }
            paragraph {
                text(
                    bokmal { + "Dersom du mener du har krav på forlenget periode med gjenlevendepensjon, må du søke om dette. " },
                )
            }
            paragraph {
                text(
                    bokmal { + "Har du allerede nå en plan for tiltak eller utdanning som du mener gir deg rett til forlenget periode fra 1. januar 2027, søker du ved å sende melding til oss i tjenesten nav.no/velg-gjenlevende. " },
                )
            }
            paragraph {
                text(
                    bokmal { + "Du må også sende inn dokumentasjon på tiltak eller utdanning i tjenesten nav.no/fyllut-ettersending/nav170105/innsendingsvalg. " },
                )
            }
            paragraph {
                text(
                    bokmal { + "Har du ikke mulighet til å bruke nav.no, kan du sende opplysninger og dokumentasjon til adressen: " },
                )
            }
            paragraph {
                text(
                    bokmal { + "Nav familie- og pensjonsytelser" },
                )
                newline()
                text(
                    bokmal { + "Postboks 6600 Etterstad" },
                )
                newline()
                text(
                    bokmal { + "0607 OSLO" },
                )
            }

            title1 {
                text(
                    bokmal { + "Har du spørsmål?" },
                )
            }
            paragraph {
                text(
                    bokmal { + "Du finner mer informasjon på nav.no/gjenlevendepensjon#hvor-lenge. " },
                )
            }
            paragraph {
                text(
                    bokmal { + "På nav.no/kontakt kan du chatte eller skrive til oss. " },
                )
            }
            paragraph {
                text(
                    bokmal { + "Hvis du ikke finner svar på nav.no, kan du ringe oss på telefon 55 55 33 34 hverdager 09.00–15.00. " },
                )
            }
        }
    }
}