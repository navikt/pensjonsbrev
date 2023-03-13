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
object GjenlevendeInfoEtter1970 : AutobrevTemplate<Unit> {

    override val kode: Brevkode.AutoBrev = Brevkode.AutoBrev.PE_ADHOC_2023_04_GJENLEVENDEINFOETTER1970

    override val template = createTemplate(
        name = kode.name,
        letterDataType = Unit::class,
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "adhoc - Informasjon om endringer i gjenlevendepensjonen din (født etter 1970)", //TODO midlertidig tittel,
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Informasjon om endringer i gjenlevendepensjonen din",
            )
        }
        outline {
            title1 {
                text(
                    Bokmal to "Brevet er produsert automatisk og derfor ikke underskrevet av saksbehandler.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Dagens gjenlevendepensjon blir erstattet av en tidsbegrenset omstillingsstønad. Denne regelendringen er en del av pensjonsreformen, som Stortinget har bestemt. Endringen gjelder fra 1. januar 2024.",
                )
            }
            title1 {
                text(
                    Bokmal to "Hva betyr endringene for deg?",
                )
            }
            paragraph {
                text(
                    Bokmal to "Du beholder gjenlevendepensjonen som den er i dag, men den varer kun i tre år fra 1. januar 2024. Det betyr at siste utbetaling av pensjonen din blir i desember 2026.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Har du ingen utbetaling av gjenlevendepensjon, vil du ikke merke endringene.",
                )
            }
            title1 {
                text(
                    Bokmal to "Har du utbetalt gjenlevendepensjon?",
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
                    Bokmal to "På nettsiden arbeidsplassen.no finner du ledige stillinger i ditt område og over hele landet. Her kan du også få varsel på e-post om jobber som passer for deg.",
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
                    Bokmal to "Du kan få veiledning av oss hvis du trenger hjelp til å komme i jobb eller jobbe mer. Vi kan fortelle deg om muligheter i arbeidsmarkedet, eller snakke med deg om nødvendig utdanning eller andre tiltak for å komme i arbeid.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Trenger du hjelp for å komme i arbeid, for eksempel utdanning eller arbeidstrening, kan gjenlevendepensjonen forlenges i inntil to år. Dette må i så fall godkjennes av NAV.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Får du godkjent utdanning eller arbeidstrening kan du også søke om skolepenger og tilleggsstønad. Du finner mer informasjon om stønadene på nav.no/gjenlevendepensjon#andre-stonader.",
                )
            }
            title1 {
                text(
                    Bokmal to "Har du spørsmål?",
                )
            }
            paragraph {
                text(
                    Bokmal to "Du finner informasjon på nav.no/gjenlevendepensjon#regelendringer. Du kan også kontakte oss på telefon 55 55 33 34 eller på nav.no/kontaktoss.",
                )
            }
        }
    }
}
