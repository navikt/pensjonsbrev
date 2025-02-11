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
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkDTOSelectors.erBosattUtlandet
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkDTOSelectors.erForeldreloes
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import no.nav.pensjon.etterlatte.maler.fraser.common.kontakttelefonPensjon

@TemplateModelHelpers
object ForhaandsvarselOmregningBP : EtterlatteTemplate<BarnepensjonOmregnetNyttRegelverkDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_FORHAANDSVARSEL_OMREGNING
    override val template: LetterTemplate<*, BarnepensjonOmregnetNyttRegelverkDTO> =
        createTemplate(
            name = kode.name,
            letterDataType = BarnepensjonOmregnetNyttRegelverkDTO::class,
            languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Forhåndsvarsel om økt barnepensjon",
                    isSensitiv = false,
                    distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                    brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
                ),
        ) {

            title {
                showIf(erForeldreloes) {
                    text(
                        Language.Bokmal to "Forhåndsvarsel om mulig endring av barnepensjon",
                        Language.Nynorsk to "Førehandsvarsel om mulig endring av barnepensjon",
                        Language.English to "Advance notice of possible change in children's pension",
                    )
                } orShow {
                    text(
                        Language.Bokmal to "Forhåndsvarsel om økt barnepensjon",
                        Language.Nynorsk to "Førehandsvarsel om auka barnepensjon",
                        Language.English to "Advance notice of increase in children's pension",
                    )
                }
            }
            outline {
                paragraph {
                    text(
                        Language.Bokmal to "Dette er et forhåndsvarsel om at Nav vil fatte nytt vedtak om barnepensjon fordi Stortinget har vedtatt nye regler for barnepensjon. De nye reglene gjelder fra 1. januar 2024.",
                        Language.Nynorsk to "Dette er eit førehandsvarsel om at Nav vil fatte eit nytt vedtak om barnepensjon fordi Stortinget har vedteke nye reglar for barnepensjon. Dei nye reglane gjeld frå og med 1. januar 2024.",
                        Language.English to "This is an advance notice that Nav will be considering adjustments to the children's pensions because the Storting has adopted new rules for this type of pension. The new rules will apply from 1 January 2024.",
                    )
                }
                showIf(erForeldreloes) {
                    paragraph {
                        text(
                            Language.Bokmal to "Beregningen av barnepensjon til foreldreløse barn er annerledes fra 1. januar 2024. Det vil vurderes om pensjonen din vil bli høyere med nytt regelverk enn det du har i dag. Du vil få det som gir høyest utbetaling hver måned. Du trenger ikke å gi beskjed til Nav om hvilken beregning du ønsker.",
                            Language.Nynorsk to "",
                            Language.English to "",
                        )
                    }
                } orShow {
                    paragraph {
                        text(
                            Language.Bokmal to "Du kan se nytt månedsbeløp i vedlegget “Utkast til vedtak – endring av barnepensjon”.",
                            Language.Nynorsk to "Du kan sjå det nye månadsbeløpet i vedlegget «Utkast til vedtak – endring av barnepensjon».",
                            Language.English to "The new monthly amount will appear in the attachment, Draft decision – adjustment of children's pension.",
                        )
                    }
                    paragraph {
                        text(
                            Language.Bokmal to "Du trenger ikke å gi beskjed til Nav om du ønsker høyere barnepensjon. Det nye beløpet får du fra januar 2024.",
                            Language.Nynorsk to "Du treng ikkje å gi beskjed til Nav om at du ønskjer høgare barnepensjon. Du får det nye beløpet frå og med januar 2024.",
                            Language.English to "You do not need to notify Nav if you want a higher children's pension. The new amount will be available starting in January 2024.",
                        )
                    }
                }

                title2 {
                    text(
                        Language.Bokmal to "Hva betyr dette forhåndsvarselet?",
                        Language.Nynorsk to "Kva betyr dette førehandsvarselet?",
                        Language.English to "What does this advance notice mean?",
                    )
                }
                paragraph {
                    text(
                        Language.Bokmal to "Vi forhåndsvarsler deg slik at du kan sjekke om beregningen av pensjonen din er korrekt. Dette er en foreløpig beregning som Nav har gjort på bakgrunn av opplysningene vi allerede har om barnepensjonen din.",
                        Language.Nynorsk to "Vi varslar deg på førehand slik at du kan sjekke at utrekninga av barnepensjonen din stemmer. Dette er ei førebels utrekning som Nav har gjort på bakgrunn av opplysningane som allereie er lagra om barnepensjonen din.",
                        Language.English to "We are notifying all children's pension recipients in advance so you can check whether the calculation of your pension is correct. This is a preliminary calculation that Nav has made based on the information we already have about your children's pension.",
                    )
                }
                paragraph {
                    text(
                        Language.Bokmal to "Beregningen av barnepensjonen din trer i kraft som et vedtak fra 1. januar 2024. Du kan gi oss nye opplysninger slik at vi kan rette opp eventuelle feil i beregningen.",
                        Language.Nynorsk to "Utrekninga av barnepensjonen din trer i kraft som vedtak fire veker frå du får dette førehandsvarselet. I løpet av denne perioden kan du gi oss nye opplysningar slik at vi kan rette opp eventuelle feil i utrekninga.",
                        Language.English to "The calculation of your children's pension will take effect four weeks after you receive this advance notice. During this period, feel free to provide us with any new information so we can correct any errors in our calculation.",
                    )
                }
                paragraph {
                    text(
                        Language.Bokmal to "Du kan se hvordan vi har beregnet ny barnepensjon i vedlegget “Utkast til vedtak – endring av barnepensjon”.",
                        Language.Nynorsk to "Du kan sjå i vedlegget «Utkast til vedtak – endring av barnepensjon» korleis vi har rekna ut den nye barnepensjonen.",
                        Language.English to "You can see how we have calculated the new children's pension in the attachment, Draft decision – adjustment of children's pension.",
                    )
                }

                title2 {
                    text(
                        Language.Bokmal to "Frister for å sende inn nye opplysninger og klage",
                        Language.Nynorsk to "Fristar for å sende inn nye opplysningar og klage",
                        Language.English to "Deadlines for submitting new information and appeals",
                    )
                }
                paragraph {
                    text(
                        Language.Bokmal to "Hvis opplysningene ikke stemmer, må du gi beskjed til Nav snarest mulig. Hvis opplysningene gjør at Nav må beregne barnepensjonen din på nytt, får du et nytt vedtak.",
                        Language.Nynorsk to "Dersom opplysningane ikkje stemmer, må du gi beskjed til Nav innan fire veker frå du får dette førehandsvarselet. Du vil få eit nytt vedtak dersom opplysningane gjer det nødvendig for Nav å rekne ut barnepensjonen din på nytt.",
                        Language.English to "If the information is incorrect, you must notify Nav within four weeks of receiving this advance notice. If the information means that Nav has to recalculate your children's pension, you will receive a new decision.",
                    )
                }

                paragraph {
                    text(
                        Language.Bokmal to "Hvis du ikke har nye opplysninger eller innsigelser til “Utkast til vedtak – endring av barnepensjon” som er vedlagt, vil vedlegget anses som et vedtak etter to uker fra du mottok dette brevet. Du vil ikke få et nytt vedtak fordi utkastet da er omgjort til “Vedtak - endring av barnepensjon”.",
                        Language.Nynorsk to "Dersom du ikkje har nye opplysningar eller innvendingar mot «Utkast til vedtak – endring av barnepensjon» som er lagt ved, vil vedlegget bli rekna som vedtak frå og med 1. januar 2024. Du vil med andre ikkje få eit nytt vedtak, fordi utkastet då har blitt gjort om til «Vedtak – endring av barnepensjon».",
                        Language.English to "If you have no new information or objections to the information provided in the Draft Decision, the decision will be deemed valid as of 1 January 2024. In other words, you will not receive a new decision because the draft decision will be converted to a formal decision and then entitled Decision – adjustment of children's pension.",
                    )
                }

                paragraph {
                    text(
                        Language.Bokmal to "Etter at vedtaket har tredd i kraft har du seks ukers klagefrist på vedtaket. Du finner informasjon om hvordan du klager i “Utkast til vedtak – endring av barnepensjon”.",
                        Language.Nynorsk to "Etter at vedtaket har tredd i kraft (altså 1. januar 2024), har du seks vekers klagefrist. Sjå «Utkast til vedtak – endring av barnepensjon» for meir informasjon om korleis du går fram for å klage.",
                        Language.English to "After the decision has entered into force (i.e. 1 January 2024), you have six weeks to appeal the decision. You can find information on how to appeal in the attachment, Draft decision – adjustment of children's pension.",
                    )
                }

                title2 {
                    text(
                        Language.Bokmal to "Døde din forelder av yrkesskade eller yrkessykdom?",
                        Language.Nynorsk to "Døydde forelderen din av yrkesskade eller yrkessjukdom?",
                        Language.English to "Did your parent die of an occupational injury or illness?",
                    )
                }

                paragraph {
                    text(
                        Language.Bokmal to "Hvis forelderen din døde av yrkesskade eller yrkessykdom, kan det ha betydning for barnepensjonen din. Da må du ta kontakt med Nav slik at vi er sikre på at vi har beregnet barnepensjonen din riktig.",
                        Language.Nynorsk to "Dersom forelderen din døydde av yrkesskade eller yrkessjukdom, kan det ha betydning for barnepensjonen din. Kontakt i dette tilfellet Nav, slik at vi er sikre på at vi har rekna ut barnepensjonen din rett.",
                        Language.English to "If your parent died of an occupational injury or illness, this may affect your children's pension. You must then contact Nav so we can be sure that we calculate your children's pension correctly.",
                    )
                }

                title2 {
                    text(
                        Language.Bokmal to "Har du spørsmål?",
                        Language.Nynorsk to "Har du spørsmål?",
                        Language.English to "Do you have any questions?",
                    )
                }
                paragraph {
                    text(
                        Language.Bokmal to "Du finner mer informasjon på ${Constants.BARNEPENSJON_URL}. Hvis du ikke finner svar på spørsmålet ditt, kan du ringe oss på telefon ",
                        Language.Nynorsk to "Les meir på ${Constants.BARNEPENSJON_URL}. Dersom du ikkje finn svar på spørsmålet ditt der, kan du ringje oss på telefon ",
                        Language.English to "For more information, visit us online: ${Constants.Engelsk.BARNEPENSJON_URL}. If you cannot find the answer to your question, you can call us by phone ",
                    )
                    kontakttelefonPensjon(erBosattUtlandet)
                    text(
                        Language.Bokmal to " hverdager mellom kl. 09.00-15.00.",
                        Language.Nynorsk to ", kvardagar mellom kl. 09.00–15.00. ",
                        Language.English to " weekdays between 09.00-15.00.",
                    )
                }

                // Vi må jukse litt for å få med "vedlegg" - bare fyller det inn manuelt
                title2 {
                    text(
                        Language.Bokmal to "Vedlegg",
                        Language.Nynorsk to "Vedlegg",
                        Language.English to "Attachment",
                    )
                }
                paragraph {
                    list {
                        item {
                            text(
                                Language.Bokmal to "Utkast til vedtak - endring av barnepensjon",
                                Language.Nynorsk to "Utkast til vedtak – endring av barnepensjon",
                                Language.English to "Draft decision – adjustment of children's pension",
                            )
                        }
                    }
                }
            }
        }
}
