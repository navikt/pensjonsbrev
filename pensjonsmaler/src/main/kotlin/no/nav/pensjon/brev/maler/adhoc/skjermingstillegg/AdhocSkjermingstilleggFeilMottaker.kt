package no.nav.pensjon.brev.maler.adhoc.skjermingstillegg

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object AdhocSkjermingstilleggFeilMottaker : AutobrevTemplate<EmptyBrevdata> {
    override val kode = Pesysbrevkoder.AutoBrev.PE_AP_ADHOC_2025_SKJERMT_FEIL_MOTTAKER

    override val template = createTemplate(
        name = kode.name,
        letterDataType = EmptyBrevdata::class,
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Nav har sendt deg feil brev",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Vi har ved en feil gitt deg tilgang til noen andres personopplysninger",
            )
        }
        outline {
            paragraph {
                text(
                    Bokmal to "Nav har ved en feil sendt deg et brev som gjelder ektefellen, partneren eller samboeren din."
                )
            }

            title2 {
                text(
                    Bokmal to "Hvordan har feilen oppstått"
                )
            }
            paragraph {
                text(
                    Bokmal to "Vi gjorde en feil da vi satte inn navn og adresse i brevet."
                )
            }

            title2 {
                text(
                    Bokmal to "Hva må du gjøre nå"
                )
            }
            paragraph {
                text(
                    Bokmal to "Ut fra det vi kan se, har Nav sendt deg et brev datert 17. juli 2025, " +
                            "adressert til ektefellen, samboeren eller partneren din som du bodde sammen med da brevet ble sendt. " +
                            "Hvis du fortsatt har brevet, må du slette det så raskt som mulig. Vi minner om at du ikke har lov til å bruke eller dele andres personopplysninger."
                )
            }
            paragraph {
                text(
                    Bokmal to "Nav vil sørge for at riktig person får brevet. Du trenger ikke å dele brevet med vedkommende. " +
                            "Du skal bare slette brevet."
                )
            }

            title2 {
                text(
                    Bokmal to "Hva gjør Nav videre i saken?"
                )
            }
            paragraph {
                text(
                    Bokmal to "Vi har rettet opp i feilen. I fremtiden får du derfor ikke slike brev. " +
                            "Datatilsynet er informert om saken.  Vi varsler ektefellen, partneren eller samboeren om at du har fått tilgang til dennes personopplysninger."
                )
            }

            title2 {
                text(
                    Bokmal to "Vil du vite hvordan Nav håndterer personvernopplysninger?"
                )
            }
            paragraph {
                text(
                    Bokmal to "Du kan lese mer på nav.no/personvernerklaering."
                )
            }

            title2 {
                text(
                    Bokmal to "Har du spørsmål?"
                )
            }
            paragraph {
                text(
                    Bokmal to "På nav.no/kontakt kan du chatte eller skrive til oss. " +
                            "Du kan også ringe oss på telefon 55 55 33 34 hverdager kl.09.00-15.00."
                )
            }

            paragraph {
                text(
                    Bokmal to "Vi beklager feilen."
                )
            }

        }
    }
}