package no.nav.pensjon.brev.maler.adhoc.skjermingstillegg

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.fraser.common.Constants.KONTAKT_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KONTAKTSENTER_AAPNINGSTID
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON
import no.nav.pensjon.brev.maler.fraser.common.Constants.PERSONVERNERKLAERING_URL
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
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                bokmal { + "Vi har ved en feil gitt noen andre tilgang til dine opplysninger." },
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { + "Nav har sendt brev om alderspensjonen din til en person du bodde sammen med da brevet ble sendt. Vi hadde ikke lov til å dele opplysningene i brevet med den personen. " +
                            "Dette er et brudd på Navs taushetsplikt og er et personvernbrudd. Feilen er rettet opp ved at fremtidige brev sendes til riktig person. " +
                            "Det kan være du har fått eller lest brevet allerede. Vi vil uansett sende deg riktig brev." }
                )
            }

            title2 {
                text(
                    bokmal { + "Hvem har hatt tilgang til personopplysningene?" }
                )
            }
            paragraph {
                text(
                    bokmal { + "En person du bodde sammen med på tidspunktet da brevet ble sendt, har fått brevet. " +
                            "Personen er nå kontaktet og bedt om å slette brevet. " }
                )
            }

            title2 {
                text(
                    bokmal { + "Hvordan har denne feilen oppstått?" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Vi gjorde en feil da vi satte inn navn og adresse i brevet." }
                )
            }

            title2 {
                text(
                    bokmal { + "Hvilke personopplysninger har en annen person hatt tilgang til?" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Personen har fått tilgang til opplysningene som står i brevet fra Nav. " +
                            "Brevet inneholder følgende opplysninger:" }
                )
                list {
                    item {
                        text(
                            bokmal { + "navnet ditt" }
                        )
                    }
                    item {
                        text(
                            bokmal { + "fødselsnummeret ditt" }
                        )
                    }
                    item {
                        text(
                            bokmal { + "adressen din" }
                        )
                    }
                    item {
                        text(
                            bokmal { + "opplysninger om beregning av din alderspensjon" }
                        )
                    }
                    item {
                        text(
                            bokmal { + "informasjon om at du tidligere har hatt uføretrygd" }
                        )
                    }
                }
            }

            title2 {
                text(
                    bokmal { + "Mulige konsekvenser av personvernbruddet" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Det en risiko for at denne personen kan ha opplysninger om deg som vedkommende ikke skulle hatt tilgang til. " +
                            "Der personopplysninger er på avveie, kan det i verste fall være en risiko for at vedkommende misbruker disse personopplysningene. " +
                            "Vi beklager konsekvensene dette kan ha fått for deg." }
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