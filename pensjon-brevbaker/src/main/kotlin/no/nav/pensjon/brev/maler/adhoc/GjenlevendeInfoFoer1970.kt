package no.nav.pensjon.brev.maler.adhoc

import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text

@TemplateModelHelpers
object GjenlevendeInfoFoer1970 : AutobrevTemplate<Unit> {

    override val kode: Brevkode.AutoBrev = Brevkode.AutoBrev.ADHOC_GJENLEVENDEINFOFOER1970

    override val template = createTemplate(
        name = kode.name,
        letterDataType = Unit::class,
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "", //TODO
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) { 
        //TODO legg inn alle tekstene på nytt når det er klart.
        //TODO legg inn nynorsk/engelsk.
        title {
            text(
                Bokmal to "Informasjon om endringer i gjenlevendepensjonen din"
            )
        }
        outline {
            paragraph {
                text(
                    Bokmal to "Dagens gjenlevendepensjon blir erstattet av en tidsbegrenset omstillingsstønad. Dette er en del av pensjonsreformen, som Stortinget har bestemt. Endringen gjelder fra 1. januar 2024.",
                )
            }

            title1 {
                text(
                    Bokmal to "Lav inntekt kan gi utbetaling til du er 67 år ",
                )
            }

            paragraph {
                //TODO uferdig tekst?
                text(
                    Bokmal to "Du kan beholde utbetalingen til du blir 67 år. Dette forutsetter at du har hatt en gjennomsnittlig arbeidsinntekt som ikke er høyere enn to ganger grunnbeløpet i årene 2019-2023 og at arbeidsinntekt i årene 2022 og 2023 ikke er høyere enn tre ganger grunnbeløpet. Les mer om dette i avsnittet «...».",
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis inntektsnivået ditt….. beholde gjenlevendepensjonen som den er i dag frem til 1. januar 2029. Gjenlevendepensjonen vil da bli omregnet til omstillingsstønad, som er en fast sats på 2,25 ganger grunnbeløpet per år. Dette kan føre til at du får en lavere utbetaling. Omstillingsstønaden blir redusert etter arbeidsinntekten din og ved manglende trygdetid, på samme måte som gjenlevendepensjonen blir.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Med dagens grunnbeløp vil en uredusert omstillingsstønad (før reduksjon av eventuell inntekt og trygdetid) utgjøre 250 823 kroner før skatt. Du kan finne grunnbeløpet for ulike år på nav.no/grunnbelopet.",
                )
            }

            title1 {
                text(
                    Bokmal to "Hva må du gjøre hvis du får utbetalt pensjon?",
                )
            }

            paragraph {
                text(
                    Bokmal to "Hvis du ønsker å opprettholde dagens inntektsnivå fra 1. januar 2027, må du selv vurdere om du trenger å komme i jobb eller å jobbe mer. Dette kan ta tid, så du bør komme raskt i gang.",
                )
            }

            paragraph {
                text(
                    Bokmal to "Du finner mer informasjon om hvordan du kan søke eller få hjelp til å komme i jobb eller utdanning på nav.no/arbeid.",
                )
            }

            paragraph {
                text(
                    Bokmal to "På nettsiden arbeidsplassen.no finner du ledige stillinger i ditt område og over hele landet. Her kan du også få varsel på e-post på jobber som passer for deg.",
                )
            }

            paragraph {
                text(
                    Bokmal to "Du finner mange gode tips på nav.no/finn-jobbene.",
                )
            }

            title1 {
                text(
                    Bokmal to "Hva kan vi gjøre for deg?",
                )
            }

            paragraph {
                text(
                    Bokmal to "Du kan få veiledning av oss hvis du trenger hjelp til å komme i jobb. Vi kan fortelle deg om muligheter i arbeidsmarkedet, eller snakke med deg om nødvendig utdanning eller andre tiltak for å komme i arbeid.",
                )
            }

            paragraph {
                text(
                    Bokmal to "Trenger du hjelp for å komme i arbeid, for eksempel utdanning eller arbeidstrening, kan pensjonen forlenges i inntil to år. Dette må i så fall godkjennes av NAV.",
                )
            }

            paragraph {
                text(
                    Bokmal to "Får du godkjent utdanning eller arbeidstrening kan du også søke om skolepenger og tilleggsstønad. Du finner mer informasjon om stønadene på nav.no/gjenlevendepensjon#andre-stonader.",
                )
            }
            //TODO legg inn tabellen når den begynner å bli klar.
            title1 {
                text(
                    Bokmal to "Har du custom spørsmål?",
                )
            }
            paragraph {
                text(
                    Bokmal to "Jeg har custom svar :D",
                )
            }
        }
    }
}

