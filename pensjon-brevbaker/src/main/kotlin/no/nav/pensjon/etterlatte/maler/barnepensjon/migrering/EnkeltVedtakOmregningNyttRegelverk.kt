package no.nav.pensjon.etterlatte.maler.barnepensjon.migrering

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkDTOSelectors.anvendtTrygdetid
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkDTOSelectors.erBosattUtlandet
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkDTOSelectors.erForeldreloes
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkDTOSelectors.erUnder18Aar
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkDTOSelectors.erYrkesskade
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkDTOSelectors.grunnbeloep
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkDTOSelectors.prorataBroek
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkDTOSelectors.utbetaltEtterReform
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkDTOSelectors.utbetaltFoerReform
import no.nav.pensjon.etterlatte.maler.formatBroek
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import no.nav.pensjon.etterlatte.maler.fraser.common.kontakttelefonPensjon
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.dineRettigheterOgPlikter
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.informasjonTilDegSomMottarBarnepensjon
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.utlandInformasjonTilDegSomMottarBarnepensjon
import no.nav.pensjon.etterlatte.maler.vedlegg.informasjonTilDegSomHandlerPaaVegneAvBarnet
import no.nav.pensjon.etterlatte.maler.vedlegg.utlandInformasjonTilDegSomHandlerPaaVegneAvBarnet



@TemplateModelHelpers
object EnkeltVedtakOmregningNyttRegelverk : EtterlatteTemplate<BarnepensjonOmregnetNyttRegelverkDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_VEDTAK_OMREGNING
    override val template: LetterTemplate<*, BarnepensjonOmregnetNyttRegelverkDTO> = createTemplate(
        name = kode.name,
        letterDataType = BarnepensjonOmregnetNyttRegelverkDTO::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Utkast til - endring av barnepensjon",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        title {
            text(
                Language.Bokmal to "Utkast til vedtak - endring av barnepensjon",
                Language.Nynorsk to "Utkast til vedtak – endring av barnepensjon",
                Language.English to "Draft decision – adjustment of children's pension",
            )
        }
        outline {
            paragraph {
                textExpr(
                    Language.Bokmal to "Stortinget har vedtatt nye regler for barnepensjon. Du får høyere barnepensjon. Du har ".expr()
                            + utbetaltFoerReform.format() + " kroner per måned i pensjon til 31. desember 2023. Du får " + utbetaltEtterReform.format()
                            + " kroner før skatt per måned fra 1. januar 2024.",
                    Language.Nynorsk to "Stortinget har vedteke nye reglar for barnepensjon. Du får høgare barnepensjon. Du har ".expr() +
                            utbetaltFoerReform.format() + " kroner per månad i pensjon fram til 31. desember 2023. Du får " + utbetaltEtterReform.format() +
                            " kroner før skatt per månad frå og med 1. januar 2024.",
                    Language.English to "The Norwegian Parliament has adopted new rules for children's pensions. ".expr() +
                            "Your children's pension will now increase. " +
                            "You will receive pension payments amounting to " + utbetaltFoerReform.format() + " kroner per month until 31 December 2023. " +
                            "The gross (pre-tax) amount you will receive is NOK " + utbetaltEtterReform.format() + " per month starting 1 January 2024."
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Vedtaket er gjort etter lov 18. desember 2020 nr. 139 om endringer i folketrygdloven del II nr. 4, jf. folketrygdloven § 18-4 og § 18-5.",
                    Language.Nynorsk to "Vedtaket er gjort etter lov 18. desember 2020 nr. 139 om endringar i folketrygdlova del II nr. 4, jf. folketrygdlova § 18-4 og § 18-5.",
                    Language.English to "The decision was made pursuant to Act #139 of 18 December 2020 concerning Amendments to the National Insurance Act, Part II #4; cf. sections 18-4 and 18-5 of the National Insurance Act."
                )
            }

            title2 {
                text(
                    Language.Bokmal to "Hva betyr de nye reglene for deg?",
                    Language.Nynorsk to "Kva betyr dei nye reglane for deg?",
                    Language.English to "What do the new rules mean to you?"
                )
            }

            paragraph {
                list {
                    showIf(erForeldreloes) {
                        item {
                            text(
                                Language.Bokmal to "Barnepensjonen din øker dersom nye regler gir best beregning.",
                                Language.Nynorsk to "",
                                Language.English to ""
                            )
                        }
                    } orShow {
                        item {
                            text(
                                Language.Bokmal to "Barnepensjonen din øker.",
                                Language.Nynorsk to "Barnepensjonen din aukar.",
                                Language.English to "Your children's pension will increase."
                            )
                        }
                    }
                    item {
                        showIf(erYrkesskade) {
                            text(
                                Language.Bokmal to "Du får barnepensjon til du blir 21 år fordi dødsfallet skyldtes yrkesskade eller yrkessykdom.",
                                Language.Nynorsk to "Du får barnepensjon fram til fylte 21 år fordi dødsfallet var knytt til yrkesskade eller yrkessjukdom.",
                                Language.English to "You will receive a children’s pension until you turn 21 because the death of your parent/guardian was an occupational injury/illness."
                            )
                        } orShow {
                            text(
                                Language.Bokmal to "Du får barnepensjon til du blir 20 år.",
                                Language.Nynorsk to "Du får barnepensjon til du fyller 20 år.",
                                Language.English to "You will receive a children's pension until you turn 20."
                            )
                        }
                    }
                    showIf(erForeldreloes.not()) {
                        item {
                            text(
                                Language.Bokmal to "Barnepensjonen blir ikke justert som følge av søsken.",
                                Language.Nynorsk to "Det er ikkje lenger søskenjustering i barnepensjonen.",
                                Language.English to "Children’s pensions are no longer subject to the sibling adjustment."
                            )
                        }
                    }
                }
            }

            title2 {
                text(
                    Language.Bokmal to "Slik har vi beregnet pensjonen din",
                    Language.Nynorsk to "Slik har vi rekna ut pensjonen din",
                    Language.English to "This is how we calculated your pension"
                )
            }
            showIf(erForeldreloes) {
                paragraph {
                    textExpr(
                        Language.Bokmal to "Når begge foreldrene dine er døde, blir den årlige pensjonen etter nytt regelverk lik 2,25 ganger grunnbeløpet. Grunnbeløpet i januar 2024 er ".expr() +
                                grunnbeloep.format() + " kroner. Dette deles på 12 måneder. I noen tilfeller vil beregning av barnepensjon etter " +
                                "dagens regelverk gi høyere utbetaling av pensjon 2,25 ganger grunnbeløpet. Du får det som gir høyest beløp.",
                        Language.Nynorsk to "".expr(),
                        Language.English to "".expr()
                    )
                }

                paragraph {
                    text(
                        Language.Bokmal to "<Gammelt regelverk - beste beregning>",
                        Language.Nynorsk to "<Gammelt regelverk - beste beregning>",
                        Language.English to "<Gammelt regelverk - beste beregning>"
                    )
                }
                paragraph {
                    text(
                        Language.Bokmal to "Vi har kontrollert barnepensjonen din. Du får høyere pensjon med gammelt regelverk. Pensjonen din er derfor ikke endret fra 1. januar 2024.",
                        Language.Nynorsk to "",
                        Language.English to ""
                    )
                }

                paragraph {
                    text(
                        Language.Bokmal to "<Nytt regelverk - beste beregning>",
                        Language.Nynorsk to "<Nytt regelverk - beste beregning>",
                        Language.English to "<Nytt regelverk - beste beregning>"
                    )
                }
                paragraph {
                    text(
                        Language.Bokmal to "Vi har kontrollert barnepensjonen din. Du får høyere pensjon med nytt regelverk. Pensjonen din er derfor økt fra 1. januar 2024.",
                        Language.Nynorsk to "",
                        Language.English to ""
                    )
                }

            } orShow {
                paragraph {
                    textExpr(
                        Language.Bokmal to "Barnepensjonen din blir lik en ganger folketrygdens grunnbeløp per år. Grunnbeløpet i januar 2024 er ".expr() +
                                grunnbeloep.format() + " kroner. Dette deles på 12 måneder. ",
                        Language.Nynorsk to "Barnepensjonen din blir lik éin gong grunnbeløpet i folketrygda per år. Grunnbeløpet i januar 2024 er ".expr() +
                                grunnbeloep.format() + " kroner. Dette blir fordelt på 12 månader.",
                        Language.English to "Your children's pension will equal one times the National Insurance basic amount (G) per year. ".expr() +
                                "The basic amount in January 2024 is " + grunnbeloep.format() + " kroner. " +
                                "This is divided by 12 months."
                    )
                }
            }

            showIf(prorataBroek.notNull()) {
                paragraph {
                    textExpr(
                        Language.Bokmal to "For å få full pensjon må avdødes trygdetid være beregnet til minst 40 år. Pensjonen din er beregnet etter bestemmelsene i EØS-avtalen fordi vilkårene for rett til pensjon er oppfylt ved sammenlegging av opptjeningstid i flere land. Trygdetiden din er først beregnet etter samlet opptjening i Norge og andre EØS-land. Deretter er trygdetiden ganget med et forholdstall, som angir forholdet mellom faktisk opptjeningstid i Norge og samlet faktisk opptjening i Norge og andre EØS-land. Barnepensjonen din er beregnet med ".expr() + anvendtTrygdetid.format() + " år trygdetid, som ganges med " + prorataBroek.formatBroek() + ". Dette er det samme som tidligere.",
                        Language.Nynorsk to "For å få full pensjon må den utrekna trygdetida til avdøde vere minst 40 år. Pensjonen din er rekna ut etter føresegnene i EØS-avtalen, då vilkåra for rett til pensjon er oppfylte ved samanlegging av oppteningstid i fleire land. Trygdetida di er først rekna ut etter samla opptening i Noreg og andre EØS-land. Deretter er trygdetida gonga med eit forholdstal som angir forholdet mellom faktisk oppteningstid i Noreg og samla faktisk opptening i Noreg og andre EØS-land. Barnepensjonen din er rekna ut med ".expr() + anvendtTrygdetid.format() + " år trygdetid gonga med " + prorataBroek.formatBroek() + ". Dette er det same som tidlegare.",
                        Language.English to "To be entitled to a full pension, the deceased must have accumulated at least 40 years of national insurance coverage. Your pension is calculated according to the provisions in the EEA Agreement because the conditions that entitle you to a pension have been met, compared to the qualifying periods in several countries. Your qualifying time was initially calculated according to pension points earned in Norway and in other EEA countries. Your qualifying time was subsequently multiplied proportionally in comparison with actual qualifying periods in Norway with your overall qualifying periods in Norway and other EEA countries. Your children's pension has been calculated according to a qualifying time of (".expr() + anvendtTrygdetid.format() + ") years, then multiplied by " + prorataBroek.formatBroek() + ". This is the same calculation as in the past."
                    )
                }
            } orShow {
                paragraph {
                    textExpr(
                        Language.Bokmal to "For å få full pensjon må avdødes trygdetid være beregnet til minst 40 år. Trygdetiden tilsvarer det antall år avdøde har vært medlem i folketrygden etter fylte 16 år. Når avdøde var under 67 år ved dødsfallet blir det beregnet framtidig trygdetid. Det er vanligvis fram til og med det året avdøde ville ha fylt 66 år. Barnepensjonen din er beregnet med samme trygdetid som tidligere, ".expr() + anvendtTrygdetid.format() + " år trygdetid.",
                        Language.Nynorsk to "For å få full pensjon må den utrekna trygdetida til avdøde vere minst 40 år. Trygdetida svarer til talet på år avdøde var medlem i folketrygda etter fylte 16 år. Dersom personen døydde før fylte 67 år, blir det rekna ut framtidig trygdetid. Det er vanlegvis fram til og med det året avdøde ville ha fylt 66 år. Barnepensjonen din er rekna ut med same trygdetid som tidlegare, ".expr() + anvendtTrygdetid.format() + " år trygdetid.".expr(),
                        Language.English to "To be entitled to a full pension, the deceased must have accumulated at least 40 years of national insurance coverage. The period of national insurance coverage equals the number of years the deceased has been a member of the Norwegian National Insurance Scheme after reaching the age of 16. If the deceased was less than 67 years old at the time of death, a calculation is made for what would have remained of the future period of national insurance coverage. This is usually calculated up to the year in which the deceased would have turned 66. Your children's pension is calculated based on the same period of national insurance coverage as before of ".expr() + anvendtTrygdetid.format() + " years."
                    )
                }
            }

            paragraph {
                textExpr(
                    Language.Bokmal to "Du vil derfor få ".expr() + utbetaltEtterReform.format() + " kroner per måned før skatt fra 1. januar 2024.",
                    Language.Nynorsk to "Du får såleis ".expr() + utbetaltEtterReform.format() + " kroner per månad før skatt frå og med 1. januar 2024.",
                    Language.English to "You will therefore receive NOK ".expr() + utbetaltEtterReform.format() + " per month (pre-tax) starting 1 January 2024."
                )
            }

            title2 {
                text(
                    Language.Bokmal to "Skattetrekk på barnepensjon",
                    Language.Nynorsk to "Skattetrekk på barnepensjon",
                    Language.English to "Tax deductions on children's pensions"
                )
            }
            showIf(erBosattUtlandet) {
                paragraph {
                    text(
                        Language.Bokmal to "Skatteetaten svarer på spørsmål om skatt på pensjon for deg som ikke er skattemessig bosatt i Norge. Fra utlandet ringer du ${Constants.Utland.KONTAKTTELEFON_SKATT}.",
                        Language.Nynorsk to "Skatteetaten svarer på spørsmål om skatt på pensjon for deg som ikkje er skattemessig busett i Noreg. Frå utlandet ringjer du ${Constants.Utland.KONTAKTTELEFON_SKATT}.",
                        Language.English to "The Norwegian Tax Administration can answer any questions you may have about taxes regarding pension payments for people who are not tax residents in Norway. For calls from abroad: ${Constants.Utland.KONTAKTTELEFON_SKATT}."
                    )
                }
            } orShow {
                paragraph {
                    text(
                        Language.Bokmal to "Barnepensjon er skattepliktig, men vi trekker ikke skatt uten at du gir beskjed om det. Du kan lese mer om skatt på barnepensjon på ${Constants.SKATTETREKK_PENGESTOETTE_URL}. Her finner du også informasjon om frivillig skattetrekk og hvordan du får registrert dette.",
                        Language.Nynorsk to "Barnepensjon er skattepliktig, men vi trekkjer ikkje skatt utan at du gir beskjed om det. Du kan lese meir om skatt på barnepensjon på ${Constants.SKATTETREKK_PENGESTOETTE_URL}. Her finn du også informasjon om frivillig skattetrekk og korleis du får registrert dette.",
                        Language.English to "Children's pensions are taxable, but we do not deduct tax without being notified. You can read more about the taxation of children's pensions online: ${Constants.SKATTETREKK_PENGESTOETTE_URL}. Here you will also find information about voluntary tax withholding and how to register this."
                    )
                }
            }

            title2 {
                text(
                    Language.Bokmal to "Du har rett til å klage",
                    Language.Nynorsk to "Du har rett til å klage",
                    Language.English to "You have the right to appeal"
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Hvis du mener vedtaket er feil, kan du klage innen seks uker fra 1. januar 2024. Klagen skal være skriftlig. Du finner skjema og informasjon på ${Constants.KLAGE_URL}.",
                    Language.Nynorsk to "Dersom du meiner at vedtaket er feil, kan du klage innan seks veker frå 1. januar 2024. Klaga må vere skriftleg. Du finn skjema og informasjon på ${Constants.KLAGE_URL}.",
                    Language.English to "If you believe the decision is incorrect, you can appeal within six weeks after 1 January 2024. The appeal must be in writing. You can find the form and information online: ${Constants.Engelsk.KLAGE_URL}."
                )
            }

            title2 {
                text(
                    Language.Bokmal to "Du må melde fra om endringer",
                    Language.Nynorsk to "Du må melde frå om endringar",
                    Language.English to "You must report any changes"
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Du har plikt til å melde fra til oss om endringer som har betydning for utbetalingen av barnepensjon, eller retten til å få barnepensjon. I vedlegget «Dine rettigheter og plikter» ser du hvilke endringer du må si fra om.",
                    Language.Nynorsk to "Du pliktar å melde frå til oss om endringar som har innverknad på utbetalinga av eller retten på barnepensjon. I vedlegget «Dine rettar og plikter» ser du kva endringar du må seie frå om.",
                    Language.English to "You are obligated to notify us of any changes that affect the payment of a children's pension, or the right to receive a children's pension. You will see which changes you must report in the attachment, Your Rights and Obligations."
                )
            }

            title2 {
                text(
                    Language.Bokmal to "Har du spørsmål?",
                    Language.Nynorsk to "Har du spørsmål?",
                    Language.English to "Any questions?"
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Du finner mer informasjon på ${Constants.BARNEPENSJON_URL}. Hvis du ikke finner svar på spørsmålet ditt, kan du ringe oss på telefon ",
                    Language.Nynorsk to "Les meir på ${Constants.BARNEPENSJON_URL}. Dersom du ikkje finn svar på spørsmålet ditt der, kan du ringje oss på telefon ",
                    Language.English to "For more information, visit us online: ${Constants.Engelsk.BARNEPENSJON_URL}. If you cannot find the answer to your question, you can call us by phone ("
                )
                kontakttelefonPensjon(erBosattUtlandet)
                showIf(erUnder18Aar) {
                    text(
                        Language.Bokmal to " hverdager 9-15. Om du oppgir fødselsnummer til barnet, kan vi lettere gi deg rask og god hjelp.",
                        Language.Nynorsk to ", kvardagar 9–15. Det vil gjere det enklare for oss å gi deg rask og god hjelp om du oppgir fødselsnummeret til barnet.",
                        Language.English to ") weekdays 9-15. If you provide your child's national identity number, we can more easily provide you with quick and good help."
                    )
                }.orShow {
                    text(
                        Language.Bokmal to " hverdager 9-15. Om du oppgir fødselsnummer, kan vi lettere gi deg rask og god hjelp.",
                        Language.Nynorsk to ", kvardagar 9–15. Det vil gjere det enklare for oss å gi deg rask og god hjelp om du oppgir fødselsnummeret.",
                        Language.English to ") weekdays 9-15. If you provide your national identity number, we can more easily provide you with quick and good help."
                    )
                }
            }
        }

        // Over 18 år vedlegg
        includeAttachment(utlandInformasjonTilDegSomMottarBarnepensjon, this.argument, erUnder18Aar.not().and(erBosattUtlandet))
        includeAttachment(informasjonTilDegSomMottarBarnepensjon, this.argument, erUnder18Aar.not().and(erBosattUtlandet.not()))

        // Under 18 år vedlegg
        includeAttachment(utlandInformasjonTilDegSomHandlerPaaVegneAvBarnet, this.argument, erUnder18Aar.and(erBosattUtlandet))
        includeAttachment(informasjonTilDegSomHandlerPaaVegneAvBarnet, this.argument, erUnder18Aar.and(erBosattUtlandet.not()))

        includeAttachment(dineRettigheterOgPlikter, this.argument)
    }
}
