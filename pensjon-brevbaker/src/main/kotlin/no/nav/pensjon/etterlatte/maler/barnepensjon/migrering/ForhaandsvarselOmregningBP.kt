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
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants


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
                Language.English to "Advance notice of increase in children's pension",
            )
        }
        outline {
            paragraph {
                text(
                    Language.Bokmal to "Dette er et forhåndsvarsel om at NAV vil fatte nytt vedtak om barnepensjon fordi Stortinget har vedtatt nye regler for barnepensjon. De nye reglene gjelder fra 1. januar 2024.",
                    Language.Nynorsk to "",
                    Language.English to "This is an advance notice that NAV will be considering adjustments to the children's pensions because the Storting has adopted new rules for this type of pension. The new rules will apply from 1 January 2024."
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Du kan se nytt månedsbeløp i vedlegget “Utkast til vedtak- endring av barnepensjon”.",
                    Language.Nynorsk to "",
                    Language.English to "The new monthly amount will appear in the attachment, Draft decision – adjustment of children's pension."
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Du trenger ikke å gi beskjed til NAV om du ønsker høyere barnepensjon. Det nye beløpet får du fra januar 2024.",
                    Language.Nynorsk to "",
                    Language.English to "You do not need to notify NAV if you want a higher children's pension. The new amount will be available starting in January 2024."
                )
            }

            title2 {
                text(
                    Language.Bokmal to "Hva betyr dette forhåndsvarselet?",
                    Language.Nynorsk to "",
                    Language.English to "What does this advance notice mean?"
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Vi forhåndsvarsler deg slik at du kan sjekke om beregningen av pensjonen din er korrekt. Dette er en foreløpig beregning som NAV har gjort på bakgrunn av opplysningene vi allerede har om barnepensjonen din.",
                    Language.Nynorsk to "",
                    Language.English to "We are notifying all children's pension recipients in advance so you can check whether the calculation of your pension is correct. This is a preliminary calculation that NAV has made based on the information we already have about your children's pension."
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Beregningen av barnepensjonen din trer i kraft som et vedtak fra fire uker fra du mottar dette forhåndsvarselet. I løpet av denne perioden kan du gi oss nye opplysninger slik at vi kan rette opp eventuelle feil i beregningen.",
                    Language.Nynorsk to "",
                    Language.English to "The calculation of your children's pension will take effect four weeks after you receive this advance notice. During this period, feel free to provide us with any new information so we can correct any errors in our calculation."
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Du kan se hvordan vi har beregnet ny barnepensjon i vedlegget “Utkast til vedtak – endring av barnepensjon”.",
                    Language.Nynorsk to "",
                    Language.English to "You can see how we have calculated the new children's pension in the attachment, Draft decision – adjustment of children's pension."
                )
            }

            title2 {
                text(
                    Language.Bokmal to "Frister for å sende inn nye opplysninger og klage",
                    Language.Nynorsk to "",
                    Language.English to "Deadlines for submitting new information and appeals"
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Hvis opplysningene ikke stemmer, må du gi beskjed til NAV innen fire uker fra du får dette forhåndsvarselet. Hvis opplysningene gjør at NAV må beregne barnepensjonen din på nytt, får du et nytt vedtak.",
                    Language.Nynorsk to "",
                    Language.English to "If the information is incorrect, you must notify NAV within four weeks of receiving this advance notice. If the information means that NAV has to recalculate your children's pension, you will receive a new decision."
                )
            }

            paragraph {
                text(
                    Language.Bokmal to "Hvis du ikke har nye opplysninger eller innsigelser til “Utkast til vedtak – endring av barnepensjon” som er vedlagt, vil vedlegget anses som et vedtak fra 1. januar 2024. Du vil altså ikke få et nytt vedtak fordi utkastet da er omgjort til “Vedtak - endring av barnepensjon”.",
                    Language.Nynorsk to "",
                    Language.English to "If you have no new information or objections to the information provided in the Draft Decision, the decision will be deemed valid as of 1 January 2024. In other words, you will not receive a new decision because the draft decision will be converted to a formal decision and then entitled Decision – adjustment of children's pension."
                )
            }

            paragraph {
                text(
                    Language.Bokmal to "Etter at vedtaket har tredd i kraft (altså 1. januar 2024) har du seks ukers klagefrist på vedtaket. Du finner informasjon om hvordan du klager i “Utkast til vedtak – endring av barnepensjon”.",
                    Language.Nynorsk to "",
                    Language.English to "After the decision has entered into force (i.e. 1 January 2024), you have six weeks to appeal the decision. You can find information on how to appeal in the attachment, Draft decision – adjustment of children's pension."
                )
            }

            title2 {
                text(
                    Language.Bokmal to "Døde din forelder av yrkesskade eller yrkessykdom?",
                    Language.Nynorsk to "",
                    Language.English to "Did your parent die of an occupational injury or illness?"
                )
            }

            paragraph {
                text(
                    Language.Bokmal to "Hvis forelderen din døde av yrkesskade eller yrkessykdom, kan det ha betydning for barnepensjonen din. Da må du ta kontakt med NAV slik at vi er sikre på at vi har beregnet barnepensjonen din riktig.",
                    Language.Nynorsk to "",
                    Language.English to "If your parent died of an occupational injury or illness, this may affect your children's pension. You must then contact NAV so we can be sure that we calculate your children's pension correctly."
                )
            }

            title2 {
                text(
                    Language.Bokmal to "Har du spørsmål?",
                    Language.Nynorsk to "",
                    Language.English to "Any questions?"
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Du finner mer informasjon på ${Constants.BARNEPENSJON_URL}. Hvis du ikke finner svar på spørsmålet ditt, kan du ringe oss på telefon ${Constants.KONTAKTTELEFON_PENSJON} hverdager 9-15.",
                    Language.Nynorsk to "",
                    Language.English to "For more information, visit us online: ${Constants.Engelsk.BARNEPENSJON_URL}. If you cannot find the answer to your question, you can call us by phone (${Constants.KONTAKTTELEFON_PENSJON}) weekdays 9-15."
                )
            }

            // Vi må jukse litt for å få med "vedlegg" - bare fyller det inn manuelt
            title2 {
                text(
                    Language.Bokmal to "Vedlegg",
                    Language.Nynorsk to "",
                    Language.English to "Attachment"
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