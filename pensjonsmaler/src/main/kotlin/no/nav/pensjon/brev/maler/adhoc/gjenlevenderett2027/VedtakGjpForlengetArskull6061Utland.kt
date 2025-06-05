package no.nav.pensjon.brev.maler.adhoc.gjenlevenderett2027

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027Dto
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.gjennomsnitt
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2019
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2020
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2021
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2022
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2023
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.LocalizedFormatter.CurrencyFormat
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brev.template.includeAttachment
import no.nav.pensjon.brev.template.includePhrase
import no.nav.pensjon.brevbaker.api.model.LetterMetadata


@TemplateModelHelpers
object VedtakGjpForlengetArskull6061Utland : AutobrevTemplate<Gjenlevenderett2027Dto> {
    override val kode = Pesysbrevkoder.AutoBrev.GJP_VEDTAK_FORLENGELSE_60_61_UTLAND

    override val template: LetterTemplate<*, Gjenlevenderett2027Dto> = createTemplate(
        name = kode.name,
        letterDataType = Gjenlevenderett2027Dto::class,
        languages = languages(Bokmal, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - Gjenlevendepensjonen din forlenges",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Vedtak - Gjenlevendepensjonen din forlenges ",
                English to "Decision – Your survivor’s pension is extended "
            )
        }
        outline {
            paragraph {
                text(
                    Bokmal to "Vi viser til tidligere informasjon om at Stortinget har vedtatt endringer i folketrygdlovens regler om ytelser til etterlatte. Endringene gjelder fra 1. januar 2024. ",
                    English to "We refer to previously provided information that the Norwegian Parliament (the Storting) has adopted amendments to the National Insurance Act’s provisions on survivor’s benefits. These amendments were implemented on 01 January 2024. "
                )
            }
            paragraph {
                text(
                    Bokmal to "Du fyller vilkårene for rett til gjenlevendepensjon frem til du fyller 67 år. ",
                    English to "You fulfil the requirements for survivor’s pension until you turn 67 years old. "
                )
            }

            title1 {
                text(
                    Bokmal to "Begrunnelse for vedtaket ",
                    English to "Grounds for the decision "
                )
            }
            paragraph {
                text(
                    Bokmal to "Opplysninger om inntekten din i perioden 2019–2023, viser at du fyller vilkårene i folketrygdloven § 17 A–3. ",
                    English to "Your income information for the period 2019–2023 shows that you fulfil the requirements established by Section 17 A-3 of the National Insurance Act."
                )
            }
            paragraph {
                text(
                    Bokmal to "Du beholder derfor retten til gjenlevendepensjon frem til du fyller 67 år, under forutsetning av at de øvrige vilkårene er oppfylt. ",
                    English to "You will therefore retain the right to survivor’s pension until you turn 67 years old, provided the other conditions have been met. "
                )
            }
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven § 17 A-3. ",
                    English to "This decision has been made in accordance with Section 17 A-3 of the National Insurance Act. "
                )
            }

            title1 {
                text(
                    Bokmal to "Hva er inntektsgrensene? ",
                    English to "What are the income caps? "
                )
            }
            paragraph {
                text(
                    Bokmal to "Inntektene må ha vært under tre ganger gjennomsnittlig grunnbeløp i folketrygden (G) i både 2022 og 2023: ",
                    English to "Your income must not have exceeded three times the average National Insurance basic amount (G) in 2022 or 2023: "
                )
            }

            paragraph {
                table(
                    header = {
                        column(1) {
                            text(
                                Bokmal to "År ",
                                English to "Year "
                            )
                        }
                        column(2) {
                            text(
                                Bokmal to "Gjennomsnittlig grunnbeløp (G) ganger 3 ",
                                English to "Average National Insurance basic amount (G) times 3"
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
                                Bokmal to 329352.expr().format(CurrencyFormat) + " kroner",
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
                                Bokmal to 348717.expr().format(CurrencyFormat) + " kroner",
                                English to "NOK ".expr() + 348717.expr().format(CurrencyFormat)
                            )
                        }
                    }
                }
            }

            paragraph {
                text(
                    Bokmal to "I tillegg må inntekten din i 2019–2023 ha vært under to ganger grunnbeløpet i folketrygden (G) i gjennomsnitt i disse fem årene. " +
                            "Det vil si at inntekten kan overstige to ganger grunnbeløpet i et enkelt år, så lenge gjennomsnittet av de fem årene er lavere. ",

                    English to "In addition, your income in the period 2019–2023 must not have exceeded two times the National Insurance basic amount (G) on average during this five-year period. " +
                            "This means your income could have exceeded two times the National Insurance basic amount in individual years, provided your average for the five-year period is lower. "
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
                                Bokmal to "Gjennomsnittlig grunnbeløp (G) ganger 2 ",
                                English to "Average National Insurance basic amount (G) times 2 "
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
                                Bokmal to 197732.expr().format(CurrencyFormat) + " kroner",
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
                                Bokmal to 201706.expr().format(CurrencyFormat) + " kroner",
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
                                Bokmal to 209432.expr().format(CurrencyFormat) + " kroner",
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
                                Bokmal to 219568.expr().format(CurrencyFormat) + " kroner",
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
                                Bokmal to 232478.expr().format(CurrencyFormat) + " kroner",
                                English to "NOK ".expr() + 232478.expr().format(CurrencyFormat)
                            )
                        }
                    }
                }
            }

            title1 {
                text(
                    Bokmal to "Hvilke opplysninger har vi om deg? ",
                    English to "What information do we have about you? "
                )
            }
            paragraph {
                text(
                    Bokmal to "Det er dine reelle inntekter i årene 2019–2023 som avgjør om du kan beholde gjenlevendepensjon.  ",
                    English to "Your actual income for the period 2019–2023 will determine whether or not you will be able to keep the survivor’s pension. "
                )
            }
            paragraph {
                text(
                    Bokmal to "Ifølge registrerte opplysninger vi har om deg, har pensjonen din i årene 2019–2023 vært redusert etter følgende inntekter: ",
                    English to "According to the information we have registered about you, your pension in the period 2019–2023 has been reduced based on the following income: "
                )
            }

            includePhrase(DineInntekterTabell(inntekt2019, inntekt2020, inntekt2021, inntekt2022, inntekt2023, gjennomsnitt))


            paragraph {
                text(
                    Bokmal to "Din inntekt har ifølge våre opplysninger vært lavere enn inntektsgrensene i årene 2019–2023. ",
                    English to "According to our information, your income has been lower than the income cap in the period 2019–2023. "
                )
            }
            paragraph {
                text(
                    Bokmal to "Under forutsetning av at de øvrige vilkårene for gjenlevendepensjon er oppfylt, vil du få utbetalt gjenlevendepensjonen til og med måneden du fyller 67 år. ",
                    English to "Provided the other requirements for survivor’s pension have been met, you will continue to receive survivor’s pension until you turn 67 years old. "
                )
            }

            title1 {
                text(
                    Bokmal to "Du har rett til å klage ",
                    English to "You have the right to appeal  "
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du mener vedtaket er feil, kan du klage. Fristen for å klage er seks uker fra den datoen du mottok vedtaket. I vedlegget «Dine rettigheter og plikter» får du vite mer om hvordan du går fram. Du finner skjema og informasjon på nav.no/klage. ",
                    English to "If you believe a decision was made in error, you have the right to appeal. The term of appeal is six weeks from the date on which you received notice of the decision. In the attached documents «Your rights and obligations», you can read more about how to proceed. The appeal form and more information can be found at nav.no/klage. "
                )
            }

            title1 {
                text(
                    Bokmal to "Du har rett til innsyn ",
                    English to "You have the right to access your file "
                )
            }
            paragraph {
                text(
                    Bokmal to "Du har rett til å se dokumentene i saken din. Se vedlegg «Dine rettigheter og plikter» for informasjon om hvordan du går fram.   ",
                    English to "You have the right to access all the documents relevant to your case. In the attached «Your rights and obligations», you can read more about how to proceed."
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
                    Bokmal to "Hvis du får endringer i inntekt eller jobbsituasjon, gifter deg eller planlegger å flytte til et annet land, kan det påvirke gjenlevendepensjonen din. I slike tilfeller må du derfor straks melde fra til Nav. ",
                    English to "If your income or employment situation changes, if you get married, or if you plan to move to another country, it could affect your survivor’s pension. That is why you must contact Nav immediately if any of these things occur.  "
                )
            }

            title1 {
                text(
                    Bokmal to "Har du spørsmål? ",
                    English to "Do you have questions?  "
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
                    Bokmal to "På nav.no/kontakt kan du chatte eller skrive til oss. ",
                    English to "You can chat with us or write to us at nav.no/kontakt. "
                )
            }

            paragraph {
                text(
                    Bokmal to "Hvis du ikke finner svar på nav.no, kan du ringe oss på telefon 55 55 33 34, hverdager 09:00-15:00. ",
                    English to "If you can’t find the answers you need on nav.no, you can call us at tel. 55 55 33 34 on weekdays 09:00–15:00.  "
                )
            }
        }
        includeAttachment(vedleggGjpDineRettigheterOgPlikter, EmptyBrevdata.expr())

    }
}