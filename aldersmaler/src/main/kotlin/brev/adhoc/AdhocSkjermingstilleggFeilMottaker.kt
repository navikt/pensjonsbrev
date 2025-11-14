package brev.adhoc

import brev.felles.Constants.KONTAKT_URL
import brev.felles.Constants.NAV_KONTAKTSENTER_AAPNINGSTID
import brev.felles.Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON
import brev.felles.Constants.PERSONVERNERKLAERING_URL
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.model.alder.Aldersbrevkoder
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object AdhocSkjermingstilleggFeilMottaker : AutobrevTemplate<EmptyAutobrevdata> {
    override val kode = Aldersbrevkoder.AutoBrev.PE_AP_ADHOC_2025_SKJERMT_FEIL_MOTTAKER

    override val template = createTemplate(
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
                bokmal { + "Vi har ved en feil gitt deg tilgang til noen andres personopplysninger" },
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { + "Nav har ved en feil sendt deg et brev som gjelder ektefellen, partneren eller samboeren din." }
                )
            }

            title2 {
                text(
                    bokmal { + "Hvordan har feilen oppstått" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Vi gjorde en feil da vi satte inn navn og adresse i brevet." }
                )
            }

            title2 {
                text(
                    bokmal { + "Hva må du gjøre nå" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Ut fra det vi kan se, har Nav sendt deg et brev datert 17. juli 2025, " +
                            "adressert til ektefellen, samboeren eller partneren din som du bodde sammen med da brevet ble sendt. " +
                            "Hvis du fortsatt har brevet, må du slette det så raskt som mulig. Vi minner om at du ikke har lov til å bruke eller dele andres personopplysninger." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Nav vil sørge for at riktig person får brevet. Du trenger ikke å dele brevet med vedkommende. " +
                            "Du skal bare slette brevet." }
                )
            }

            title2 {
                text(
                    bokmal { + "Hva gjør Nav videre i saken?" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Vi har rettet opp i feilen. I fremtiden får du derfor ikke slike brev. " +
                            "Datatilsynet er informert om saken.  Vi varsler ektefellen, partneren eller samboeren om at du har fått tilgang til dennes personopplysninger." }
                )
            }

            title2 {
                text(
                    bokmal { + "Vil du vite hvordan Nav håndterer personvernopplysninger?" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Du kan lese mer på $PERSONVERNERKLAERING_URL." }
                )
            }

            title2 {
                text(
                    bokmal { + "Har du spørsmål?" }
                )
            }
            paragraph {
                text(
                    bokmal { + "På $KONTAKT_URL kan du chatte eller skrive til oss. " +
                            "Du kan også ringe oss på telefon $NAV_KONTAKTSENTER_TELEFON_PENSJON hverdager kl.$NAV_KONTAKTSENTER_AAPNINGSTID." }
                )
            }

            paragraph {
                text(
                    bokmal { + "Vi beklager feilen." }
                )
            }

        }
    }
}