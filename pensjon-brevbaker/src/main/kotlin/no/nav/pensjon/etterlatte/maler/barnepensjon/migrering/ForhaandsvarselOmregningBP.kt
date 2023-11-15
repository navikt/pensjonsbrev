package no.nav.pensjon.etterlatte.maler.barnepensjon.migrering

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate



@TemplateModelHelpers
object ForhaandsvarselOmregningBP : EtterlatteTemplate<BarnepensjonOmregnetNyttRegelverkDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_FORHAANDSVARSEL_OMREGNING
    override val template: LetterTemplate<*, BarnepensjonOmregnetNyttRegelverkDTO> = createTemplate(
        name = kode.name,
        letterDataType = BarnepensjonOmregnetNyttRegelverkDTO::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Forhåndsvarsel om økt barnepensjon",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV

        )
    ) {
        title {
            text(
                Language.Bokmal to "Forhåndsvarsel om økt barnepensjon",
                Language.Nynorsk to "",
                Language.English to "",
            )
        }
        outline {
            paragraph {
                text(
                    Language.Bokmal to "Dette er et forhåndsvarsel om at NAV vil fatte nytt vedtak om barnepensjon fordi Stortinget har vedtatt nye regler for barnepensjon. De nye reglene gjelder fra 1. januar 2024.",
                    Language.Nynorsk to "",
                    Language.English to ""
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Du kan se nytt månedsbeløp i vedlegget “Utkast til vedtak- endring av barnepensjon”.",
                    Language.Nynorsk to "",
                    Language.English to ""
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Du trenger ikke å gi beskjed til NAV om du ønsker høyere barnepensjon. Det nye beløpet får du fra januar 2024.",
                    Language.Nynorsk to "",
                    Language.English to ""
                )
            }

            title2 {
                text(
                    Language.Bokmal to "Hva betyr dette forhåndsvarselet?",
                    Language.Nynorsk to "",
                    Language.English to ""
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Vi forhåndsvarsler deg slik at du kan sjekke om beregningen av pensjonen din er korrekt. Dette er en foreløpig beregning som NAV har gjort på bakgrunn av opplysningene vi allerede har om barnepensjonen din.",
                    Language.Nynorsk to "",
                    Language.English to ""
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Beregningen av barnepensjonen din trer i kraft som et vedtak fra fire uker fra du mottar dette forhåndsvarselet. I løpet av denne perioden kan du gi oss nye opplysninger slik at vi kan rette opp eventuelle feil i beregningen.",
                    Language.Nynorsk to "",
                    Language.English to ""
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Du kan se hvordan vi har beregnet ny barnepensjon i vedlegget “Utkast til vedtak – endring av barnepensjon”.",
                    Language.Nynorsk to "",
                    Language.English to ""
                )
            }

            title2 {
                text(
                    Language.Bokmal to "Frister for å sende inn nye opplysninger og klage",
                    Language.Nynorsk to "",
                    Language.English to ""
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Hvis opplysningene ikke stemmer, må du gi beskjed til NAV innen fire uker fra du får dette forhåndsvarselet. Hvis opplysningene gjør at NAV må beregne barnepensjonen din på nytt, får du et nytt vedtak.",
                    Language.Nynorsk to "",
                    Language.English to ""
                )
            }

            paragraph {
                text(
                    Language.Bokmal to "Hvis du ikke har nye opplysninger eller innsigelser til “Utkast til vedtak – endring av barnepensjon” som er vedlagt, vil vedlegget anses som et vedtak fra 1. januar 2024. Du vil altså ikke få et nytt vedtak fordi utkastet da er omgjort til “Vedtak - endring av barnepensjon”.",
                    Language.Nynorsk to "",
                    Language.English to ""
                )
            }

            paragraph {
                text(
                    Language.Bokmal to "Etter at vedtaket har tredd i kraft (altså 1. januar 2024) har du seks ukers klagefrist på vedtaket. Du finner informasjon om hvordan du klager i “Utkast til vedtak – endring av barnepensjon”.",
                    Language.Nynorsk to "",
                    Language.English to ""
                )
            }

            title2 {
                text(
                    Language.Bokmal to "Døde din forelder av yrkesskade eller yrkessykdom?",
                    Language.Nynorsk to "",
                    Language.English to ""
                )
            }

            paragraph {
                text(
                    Language.Bokmal to "Hvis forelderen din døde av yrkesskade eller yrkessykdom, kan det ha betydning for barnepensjonen din. Da må du ta kontakt med NAV slik at vi er sikre på at vi har beregnet barnepensjonen din riktig.",
                    Language.Nynorsk to "",
                    Language.English to ""
                )
            }

            title2 {
                text(
                    Language.Bokmal to "Har du spørsmål?",
                    Language.Nynorsk to "",
                    Language.English to ""
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Du finner mer informasjon på nav.no/barnepensjon. Hvis du ikke finner svar på spørsmålet ditt, kan du ringe oss på telefon 55 55 33 34 hverdager 9-15.",
                    Language.Nynorsk to "",
                    Language.English to ""
                )
            }

            // Vi må jukse litt for å få med "vedlegg" - bare fyller det inn manuelt
            title2 {
                text(
                    Language.Bokmal to "Vedlegg",
                    Language.Nynorsk to "",
                    Language.English to ""
                )
            }
            paragraph {
                list {
                    item {
                        text(
                            Language.Bokmal to "Utkast til vedtak - endring av barnepensjon",
                            Language.Nynorsk to "",
                            Language.English to ""
                        )
                    }
                }
            }

        }
    }

}