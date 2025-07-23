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
object AdhocSkjermingstilleggFeilBeroertBruker : AutobrevTemplate<EmptyBrevdata> {
    override val kode = Pesysbrevkoder.AutoBrev.PE_AP_ADHOC_2025_SKJERMT_FEIL_BEROERT_BRUKER

    override val template = createTemplate(
        name = kode.name,
        letterDataType = EmptyBrevdata::class,
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Nav har sendt ditt brev til feil mottaker",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Vi har ved en feil gitt noen andre tilgang til dine opplysninger.",
            )
        }
        outline {
            paragraph {
                text(
                    Bokmal to "Nav har sendt brev om alderspensjonen din til en person du bodde sammen med da brevet ble sendt. Vi hadde ikke lov til å dele opplysningene i brevet med den personen. \n" +
                            "\n" +
                            "Dette er et brudd på Navs taushetsplikt og er et personvernbrudd. Feilen er rettet opp ved at fremtidige brev sendes til riktig person  \n" +
                            "\n" +
                            "Det kan være du har fått eller lest brevet allerede. Vi vil uansett sende deg riktig brev.  "
                )
            }

            title2 {
                text(
                    Bokmal to "Hvem har hatt tilgang til personopplysningene?"
                )
            }
            paragraph {
                text(
                    Bokmal to "En person du bodde sammen med på tidspunktet da brevet ble sendt, har fått brevet. " +
                            "Personen er nå kontaktet og bedt om å slette brevet. "
                )
            }

            title2 {
                text(
                    Bokmal to "Hvordan har denne feilen oppstått?"
                )
            }
            paragraph {
                text(
                    Bokmal to "Vi gjorde en feil da vi satte inn navn og adresse i brevet."
                )
            }

            title2 {
                text(
                    Bokmal to "Hvilke personopplysninger har en annen person hatt tilgang til?"
                )
            }
            paragraph {
                text(
                    Bokmal to "Personen har fått tilgang til opplysningene som står i brevet fra Nav. " +
                            "Brevet inneholder følgende opplysninger: "
                )
                list {
                    item {
                        text(
                            Bokmal to "navnet ditt"
                        )
                    }
                    item {
                        text(
                            Bokmal to "fødselsnummeret ditt"
                        )
                    }
                    item {
                        text(
                            Bokmal to "adressen din"
                        )
                    }
                    item {
                        text(
                            Bokmal to "opplysninger om beregning av din alderspensjon"
                        )
                    }
                    item {
                        text(
                            Bokmal to "informasjon om at du tidligere har hatt uføretrygd"
                        )
                    }
                }
            }

            title2 {
                text(
                    Bokmal to "Mulige konsekvenser av personvernbruddet"
                )
            }
            paragraph {
                text(
                    Bokmal to "Det en risiko for at denne personen kan ha opplysninger om deg som vedkommende ikke skulle hatt tilgang til. " +
                            "Der personopplysninger er på avveie, kan det i verste fall være en risiko for at vedkommende misbruker disse personopplysningene. " +
                            "Vi beklager konsekvensene dette kan ha fått for deg."
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
                            "Du kan også ringe oss på telefon 55553334 hverdager kl.09.00-15.00."
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