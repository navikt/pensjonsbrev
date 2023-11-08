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
object ForhaandsvarselMigrering : EtterlatteTemplate<Unit> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.FORHAANDSVARSEL_MIGRERING
    override val template: LetterTemplate<*, Unit> = createTemplate(
        name = kode.name,
        letterDataType = Unit::class,
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
                    Language.Bokmal to "Dette brevet er et forhåndsvarsel om at NAV vil fatte nytt vedtak om barnepensjon fordi Stortinget har vedtatt nye regler for barnepensjon. Du får høyere barnepensjon. ",
                    Language.Nynorsk to "",
                    Language.English to ""
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Du kan se hvordan vi har beregnet barnepensjonen din og nytt månedsbeløp i vedlegget “Vedtak - endring av barnepensjon”.",
                    Language.Nynorsk to "",
                    Language.English to ""
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Du trenger ikke å søke eller gi beskjed til NAV om at du ønsker høyere barnepensjon. Det nye beløpet får du fra januar 2024.",
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
                    Language.Bokmal to "Vi forhåndsvarsler deg slik at du kan sjekke om beregningen av pensjonen din er korrekt. Dette er en foreløpig beregning som NAV har gjort på bakgrunn av opplysningene vi har om barnepensjonen din.",
                    Language.Nynorsk to "",
                    Language.English to ""
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Beregningen trer ikke i kraft som et vedtak før etter fire uker fra du mottar dette forhåndsvarselet. På denne måten har du mulighet til å gi oss nye opplysninger slik at vi kan rette opp eventuelle feil i beregningen.",
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
                    Language.Bokmal to "Hvis du har innsigelser på vedtaket, har du en frist på tre uker til å komme med nye opplysninger. Tre-ukers fristen regnes fra datoen du mottar dette forhåndsvarselet. Om du kommer med nye opplysninger vil du motta et nytt vedtak.",
                    Language.Nynorsk to "",
                    Language.English to ""
                )
            }

            paragraph {
                text(
                    Language.Bokmal to "Hvis du ikke har innsigelser, vil vedlagte vedtak gjelde fra 1. januar 2024.",
                    Language.Nynorsk to "",
                    Language.English to ""
                )
            }

            paragraph {
                text(
                    Language.Bokmal to "Etter at vedtaket er tredd i kraft 1. januar 2024 har du seks ukers klagefrist. Du finner skjema og informasjon på nav.no/klage.",
                    Language.Nynorsk to "",
                    Language.English to ""
                )
            }

            title2 {
                text(
                    Language.Bokmal to "Ta kontakt med NAV hvis dødsfallet skyldes yrkesskade eller yrkessykdom",
                    Language.Nynorsk to "",
                    Language.English to ""
                )
            }

            paragraph {
                text(
                    Language.Bokmal to "Hvis forelderen din døde av yrkesskade eller yrkessykdom kan det ha betydning for barnepensjonen din. Vi ber deg ta kontakt med NAV slik at vi er sikre på at vi har beregnet barnepensjonen din riktig.",
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
                            Language.Bokmal to "Vedtak - endring av barnepensjon",
                            Language.Nynorsk to "",
                            Language.English to ""
                        )
                    }
                }
            }

        }
    }

}