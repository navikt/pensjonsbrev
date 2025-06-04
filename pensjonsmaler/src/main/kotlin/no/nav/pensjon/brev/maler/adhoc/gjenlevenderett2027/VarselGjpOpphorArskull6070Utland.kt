package no.nav.pensjon.brev.maler.adhoc.gjenlevenderett2027

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027Dto
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2019
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2020
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2021
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2022
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2023
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2022Over3g
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2023Over3g
import no.nav.pensjon.brev.template.LocalizedFormatter.CurrencyFormat
import no.nav.pensjon.brev.template.dsl.expression.*

@TemplateModelHelpers
object VarselGjpOpphorArskull6070Utland : AutobrevTemplate<Gjenlevenderett2027Dto> {


    override val kode = Pesysbrevkoder.AutoBrev.GJP_VARSEL_OPPHOR_60_70_UTLAND

    override val template: LetterTemplate<*, Gjenlevenderett2027Dto> = createTemplate(
        name = "GJP_VARSEL_OPPHOR_60_70_UTLAND",
        letterDataType = Gjenlevenderett2027Dto::class,
        languages = languages(Bokmal, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Forhåndsvarsel - Gjenlevendepensjonen din kan bli tidsbegrenset ",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Forhåndsvarsel - Gjenlevendepensjonen din kan bli tidsbegrenset ",
                English to "Advance notice – Your survivor’s pension may become time-limited "
            )
        }
        outline {
            paragraph {
                text(
                    Bokmal to "Vi viser til tidligere informasjon om at Stortinget har vedtatt endringer i folketrygdlovens regler om ytelser til etterlatte. Endringene gjelder fra 1. januar 2024. ",
                    English to "We refer to previously provided information that the Norwegian Parliament (the Storting) has adopted amendments to the National Insurance Act’s provisions on survivor’s benefits. These amendments were implemented on 01 January 2024. "
                )
            }

            title1 {
                text(
                    Bokmal to "Hva betyr de nye reglene for deg? ",
                    English to "What do the new rules mean for you? "
                )

            }

            paragraph {
                text(
                    Bokmal to "Du beholder retten til gjenlevendepensjon. Du får den i tre år fra 1. januar 2024. Det kan bety at siste utbetaling av pensjonen din blir i desember 2026. ",
                    English to "You retain your right to survivor’s pension. The pension will be paid for a period of three years from 01 January 2024. This could mean that you receive your final pension payment in December 2026. "
                )
            }

            title1 {
                text(
                    Bokmal to "Hva er inntektsgrensene?",
                    English to "What are the income caps?"
                )
            }

            paragraph {
                text(
                    Bokmal to "Pensjonsgivende inntekt må ha vært under tre ganger gjennomsnittlig grunnbeløp i folketrygden (G) i både 2022 og 2023: ",
                    English to "Your income must not have exceeded three times the average National Insurance basic amount (G) in 2022 or 2023: "
                )
            }

            paragraph {
                table(
                    header = {
                        column(1) {
                            text(
                                Bokmal to "År",
                                English to "Year"
                            )

                        }
                        column(2) {
                            text(
                                Bokmal to "Gjennomsnittlig grunnbeløp (G) ganger 3",
                                English to "Average National Insurance basic amount (G) times 3:"
                            )

                        }
                    },
                ) {
                    row {
                        cell {
                            text(
                                Bokmal to "2022",
                                English to "2022"
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to 329352.expr().format(CurrencyFormat) + "kroner",
                                English to "NOK ".expr() + 329352.expr().format(CurrencyFormat)
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "2023",
                                English to "2023"
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to 348717.expr().format(CurrencyFormat) + "kroner",
                                English to "NOK ".expr() + 348717.expr().format(CurrencyFormat)
                            )
                        }
                    }
                }
            }

            paragraph {
                text(
                    Bokmal to "I tillegg må inntekten din i 2019–2023 ha vært under to ganger grunnbeløpet i folketrygden (G) i gjennomsnitt i disse fem årene. Det vil si at inntekten kan overstige to ganger grunnbeløpet i et enkelt år, så lenge gjennomsnittet av de fem årene er lavere.",
                    English to "In addition, your income in the period 2019–2023 must not have exceeded two times the National Insurance basic amount (G) on average for this five-year period. This means your income could have exceeded two times the National Insurance basic amount in individual years, provided your average for the five-year period is lower. "
                )
            }

            paragraph {
                table(
                    header = {
                        column(1) {
                            text(
                                Bokmal to "År",
                                English to "Year"
                            )

                        }
                        column(2) {
                            text(
                                Bokmal to "Gjennomsnittlig grunnbeløp (G) ganger 2",
                                English to "Average National Insurance basic amount (G) times 2:"
                            )

                        }
                    },
                ) {
                    row {
                        cell {
                            text(
                                Bokmal to "2019",
                                English to "2019"
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to 197732.expr().format(CurrencyFormat) + "kroner",
                                English to "NOK ".expr() + 197732.expr().format(CurrencyFormat)
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "2020",
                                English to "2020"
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to 201706.expr().format(CurrencyFormat) + "kroner",
                                English to "NOK ".expr() + 201706.expr().format(CurrencyFormat)
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "2021",
                                English to "2021"
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to 209432.expr().format(CurrencyFormat) + "kroner",
                                English to "NOK ".expr() + 209432.expr().format(CurrencyFormat)
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "2022",
                                English to "2022"
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to 219568.expr().format(CurrencyFormat) + "kroner",
                                English to "NOK ".expr() + 219568.expr().format(CurrencyFormat)
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "2023",
                                English to "2023"
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to 232478.expr().format(CurrencyFormat) + "kroner",
                                English to "NOK ".expr() + 232478.expr().format(CurrencyFormat)
                            )
                        }
                    }
                }
            }

            title1 {
                text(
                    Bokmal to "Hvilke opplysninger har vi om deg?",
                    English to "What information do we have about you?"
                )
            }

            paragraph {
                text(
                    Bokmal to "Det er inntekten din i årene 2019–2023 som avgjør om du kan beholde gjenlevendepensjonen din til du blir 67 år. Ifølge registrerte opplysninger vi har om deg, har pensjonen din i årene 2019–2023 vært redusert etter følgende inntekter:",
                    English to "Your income for the period 2019–2023 will determine whether or not you will be able to keep the survivor’s pension until you turn 67 years old. According to the information we have registered about you, your pension in the period 2019–2023 has been reduced based on the following income:"

                )
            }

            paragraph {
                table(
                    header = {
                        column(1) {
                            text(
                                Bokmal to "År",
                                English to ""
                            )

                        }
                        column(2) {
                            text(
                                Bokmal to "Din inntekt",
                                English to ""
                            )

                        }
                    },
                ) {
                    row {
                        cell {
                            text(
                                Bokmal to "2019",
                                English to "2019"
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to inntekt2019.format(CurrencyFormat) + " kroner",
                                English to "NOK ".expr() + inntekt2019.format(CurrencyFormat),
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "2020",
                                English to "2020"
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to inntekt2020.format(CurrencyFormat) + " kroner",
                                English to "NOK ".expr() + inntekt2020.format(CurrencyFormat),
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "2021",
                                English to "2021"
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to inntekt2021.format(CurrencyFormat) + " kroner",
                                English to "NOK ".expr() + inntekt2021.format(CurrencyFormat),
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "2022",
                                English to "2022"
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to inntekt2022.format(CurrencyFormat) + " kroner",
                                English to "NOK ".expr() + inntekt2022.format(CurrencyFormat),
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "2023",
                                English to "2023"
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to inntekt2023.format(CurrencyFormat) + " kroner",
                                English to "NOK ".expr() + inntekt2023.format(CurrencyFormat),
                            )
                        }
                    }
                }
            }

            paragraph {
                text(
                    Bokmal to "Ut fra våre opplysninger fyller du ikke vilkårene i folketrygdloven §17 A-3, for å få forlenget stønadsperiode.",
                    English to ""
                )
            }

            paragraph {
                showIf(inntekt2022Over3g and inntekt2023Over3g) {
                    text(
                        Bokmal to "Din inntekt har ifølge opplysninger fra Skatteetaten vært høyere enn inntektsgrensen i 2022 og 2023 ",
                        English to "According to our information, your income has been higher than the income cap for 2022 and 2023"
                    )
                }.orShowIf(inntekt2022Over3g) {
                    text(
                        Bokmal to "Din inntekt har ifølge opplysninger fra Skatteetaten vært høyere enn inntektsgrensen i 2022 ",
                        English to "According to our information, your income has been higher than the income cap for 2022"
                    )
                }.orShowIf(inntekt2023Over3g) {
                    text(
                        Bokmal to "Din inntekt har ifølge opplysninger fra Skatteetaten vært høyere enn inntektsgrensen i 2023 ",
                        English to "According to our information, your income has been higher than the income cap for 2023"
                    )
                }.orShow {
                    textExpr(
                        Bokmal to "Din gjennomsnittlige inntekt mellom 2019 og 2023 har ifølge våre opplysninger vært høyere enn gjennomsnittlig G de fem årene (".expr() + 212_813.expr().format(CurrencyFormat) + " kroner)",
                        English to "According to our information, your average income between 2019 and 2023 has exceeds the average G these 5 years (NOK ".expr() + 212_813.expr().format(CurrencyFormat)
                    )
                }
            }

            paragraph {
                text(
                    Bokmal to "Under forutsetning av at de øvrige vilkårene for gjenlevendepensjon er oppfylt, vil du få utbetalt gjenlevendepensjonen til og med 31. desember 2026. ",
                    English to "Provided the other requirements for survivor’s pension have been met, you will continue to receive survivor’s pension until 31 December 2026. "
                )
            }

            title1 {
                text(
                    Bokmal to "Mulighet for å søke utvidet stønadsperiode",
                    English to "Option to apply for extended benefit period"
                )
            }

            paragraph {
                text(
                    Bokmal to "Hvis du er under nødvendig og hensiktsmessig utdanning eller har behov for tiltak for å komme i arbeid, kan du søke om å få pensjonen forlenget i inntil to år fra 1. januar 2027. ",
                    English to "If you are currently receiving a necessary and relevant education, or if you need further assistance to find work, you can apply to have the pension extended for a period of up to two years from 01 January 2027."
                )
            }

            title1 {
                text(
                    Bokmal to "Hva må du gjøre?",
                    English to "What do you need to do? "
                )
            }

            paragraph {
                text(
                    Bokmal to "Dersom du mener at opplysningene i dette brevet er korrekt, trenger du ikke å gjøre noe.",
                    English to "If you believe the information in this letter is correct, you do not have to do anything."
                )
            }

            paragraph {
                text(
                    Bokmal to "Dersom du mener at opplysningene om inntekten din i dette brevet er feil, og inntekten din har vært lavere de fem siste årene, må du gi oss en tilbakemelding innen fire uker fra du mottok dette forhåndsvarselet. ",
                    English to "If you believe the information in this letter is incorrect, and your income has been lower in the last five years, you must contact us within four weeks of receiving this advance notice. "
                )
            }

            paragraph {
                text(
                    Bokmal to "Du må da sende oss bekreftelse på at skattemeldingen din er endret. ",
                    English to "You must send us pay slips from your employer, or a certificate issued by the authorities where you live, specifying your income for the period 2019–2023. "
                )
            }


            paragraph {
                text(
                    Bokmal to "Vi vil da vurdere de nye opplysningene før vi gjør vedtak i saken din. ",
                    English to "We will then consider the new information before we make a decision in your case. "
                )
            }

          title1 {
                text(
                    Bokmal to "Meld fra om endringer ",
                    English to "Report changes "
                )
            }


            paragraph {
                text(
                    Bokmal to "Hvis du får endringer i inntekt, familiesituasjon, jobbsituasjon eller planlegger å flytte til et annet land, kan det påvirke gjenlevendepensjonen din. I slike tilfeller må du derfor straks melde fra til Nav. ",
                    English to "If your income or employment situation changes, if you get married, or if you plan to move to another country, it could affect your survivor’s pension. That is why you must contact Nav immediately if any of these things occur. "
                )
            }


            title1 {
                text(
                    Bokmal to "Har du spørsmål? ",
                    English to "Do you have questions? "
                )
            }


            paragraph {
                text(
                    Bokmal to "Du finner mer informasjon på nav.no/gjenlevendepensjon. ",
                    English to "You can read more at nav.no/gjenlevendepensjon. "
                )
            }


            paragraph {
                text(
                    Bokmal to "På nav.no/kontakt kan du chatte eller skrive til oss.",
                    English to "You can chat with us or write to us at nav.no/kontakt. "
                )
            }


            paragraph {
                text(
                    Bokmal to "Hvis du ikke finner svar på nav.no, kan du ringe oss på telefon 55 55 33 34, hverdager 09:00-15:00. ",
                    English to "If you can’t find the answers you need on nav.no, you can call us at tel. 55 55 33 34 on weekdays 09:00–15:00. "
                )
            }
        }
    }
}