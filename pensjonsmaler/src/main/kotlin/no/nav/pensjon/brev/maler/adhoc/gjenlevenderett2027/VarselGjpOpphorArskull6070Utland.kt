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
        languages = languages(Bokmal, Nynorsk, English),
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
                Nynorsk to "",
                English to ""
            )
        }
        outline {
            paragraph {
                text(
                    Bokmal to "Vi viser til tidligere informasjon om at Stortinget har vedtatt endringer i folketrygdlovens regler om ytelser til etterlatte. Endringene gjelder fra 1. januar 2024. ",
                    Nynorsk to "",
                    English to ""
                )

            }

            title1 {
                text(
                    Bokmal to "Hva betyr de nye reglene for deg? ",
                    Nynorsk to "",
                    English to ""
                )

            }

            paragraph {
                text(
                    Bokmal to "Du beholder retten til gjenlevendepensjon. Du får den i tre år fra 1. januar 2024. Det kan bety at siste utbetaling av pensjonen din blir i desember 2026. ",
                    Nynorsk to "",
                    English to ""
                )

            }

            title1 {
                text(
                    Bokmal to "Hva er inntektsgrensene?",
                    Nynorsk to "",
                    English to ""
                )
            }

            paragraph {
                text(
                    Bokmal to "Pensjonsgivende inntekt må ha vært under tre ganger gjennomsnittlig grunnbeløp i folketrygden (G) i både 2022 og 2023: ",
                    Nynorsk to "",
                    English to ""
                )
            }

            paragraph {
                table(
                    header = {
                        column(1) {
                            text(
                                Bokmal to "År",
                                Nynorsk to "",
                                English to ""
                            )

                        }
                        column(2) {
                            text(
                                Bokmal to "Gjennomsnittlig grunnbeløp (G) ganger 3",
                                Nynorsk to "",
                                English to ""
                            )

                        }
                    },
                ) {
                    row {
                        cell {
                            text(
                                Bokmal to "2022",
                                Nynorsk to "",
                                English to ""
                            )
                        }
                        cell {
                            text(
                                Bokmal to "329 352 kroner",
                                Nynorsk to "",
                                English to ""
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "2023",
                                Nynorsk to "",
                                English to ""
                            )
                        }
                        cell {
                            text(
                                Bokmal to "348 717 kroner",
                                Nynorsk to "",
                                English to ""
                            )
                        }
                    }
                }
            }

            paragraph {
                text(
                    Bokmal to "I tillegg må inntekten din i 2019 - 2023 ha vært under to ganger grunnbeløpet i folketrygden (G) i gjennomsnitt i disse fem årene. Det vil si at inntekten kan overstige to ganger grunnbeløpet i et enkelt år, så lenge gjennomsnittet av de fem årene er lavere.",
                    Nynorsk to "",
                    English to ""
                )
            }

            paragraph {
                table(
                    header = {
                        column(1) {
                            text(
                                Bokmal to "År",
                                Nynorsk to "",
                                English to ""
                            )

                        }
                        column(2) {
                            text(
                                Bokmal to "Gjennomsnittlig grunnbeløp (G) ganger 2",
                                Nynorsk to "",
                                English to ""
                            )

                        }
                    },
                ) {
                    row {
                        cell {
                            text(
                                Bokmal to "2019",
                                Nynorsk to "",
                                English to ""
                            )
                        }
                        cell {
                            text(
                                Bokmal to "197 732 kroner",
                                Nynorsk to "",
                                English to ""
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "2020",
                                Nynorsk to "",
                                English to ""
                            )
                        }
                        cell {
                            text(
                                Bokmal to "201 706 kroner",
                                Nynorsk to "",
                                English to ""
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "2021",
                                Nynorsk to "",
                                English to ""
                            )
                        }
                        cell {
                            text(
                                Bokmal to "209 432 kroner",
                                Nynorsk to "",
                                English to ""
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "2022",
                                Nynorsk to "",
                                English to ""
                            )
                        }
                        cell {
                            text(
                                Bokmal to "219 568 kroner",
                                Nynorsk to "",
                                English to ""
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "2023",
                                Nynorsk to "",
                                English to ""
                            )
                        }
                        cell {
                            text(
                                Bokmal to "232 478 kroner",
                                Nynorsk to "",
                                English to ""
                            )
                        }
                    }
                }
            }

            title1 {
                text(
                    Bokmal to "Hvilke opplysninger har vi om deg?",
                    Nynorsk to "",
                    English to ""
                )
            }

            paragraph {
                text(
                    Bokmal to "Ifølge registeropplysninger vi har om deg fra Skatteetaten har du i årene 2019 – 2023 hatt følgende pensjonsgivende inntekt:",
                    Nynorsk to "",
                    English to ""

                )
            }

            paragraph {
                table(
                    header = {
                        column(1) {
                            text(
                                Bokmal to "År",
                                Nynorsk to "",
                                English to ""
                            )

                        }
                        column(2) {
                            text(
                                Bokmal to "Din inntekt",
                                Nynorsk to "",
                                English to ""
                            )

                        }
                    },
                ) {
                    row {
                        cell {
                            text(
                                Bokmal to "2019",
                                Nynorsk to "",
                                English to ""
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to inntekt2019.format(CurrencyFormat) + " kroner",
                                Nynorsk to "".expr(),
                                English to "".expr(),
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "2020",
                                Nynorsk to "",
                                English to ""
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to inntekt2020.format(CurrencyFormat) + " kroner",
                                Nynorsk to "".expr(),
                                English to "".expr(),
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "2021",
                                Nynorsk to "",
                                English to ""
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to inntekt2021.format(CurrencyFormat) + " kroner",
                                Nynorsk to "".expr(),
                                English to "".expr(),
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "2022",
                                Nynorsk to "",
                                English to ""
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to inntekt2022.format(CurrencyFormat) + " kroner",
                                Nynorsk to "".expr(),
                                English to "".expr(),
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "2023",
                                Nynorsk to "",
                                English to ""
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to inntekt2023.format(CurrencyFormat) + " kroner",
                                Nynorsk to "".expr(),
                                English to "".expr(),
                            )
                        }
                    }
                }
            }

            paragraph {
                text(
                    Bokmal to "Det er inntekten din i årene 2019 – 2023 som avgjør om du kan beholde gjenlevendepensjonen din til du blir 67 år. ",
                    Nynorsk to "",
                    English to ""
                )
            }

            paragraph {
                text(
                    Bokmal to "Ut fra våre opplysninger fyller du ikke vilkårene i folketrygdloven § 17 A-3, for å få forlenget stønadsperiode.",
                    Nynorsk to "",
                    English to ""
                )
            }

            paragraph {
                showIf(inntekt2022Over3g and inntekt2023Over3g) {
                    text(
                        Bokmal to "Din inntekt har ifølge opplysninger fra Skatteetaten vært høyere enn inntektsgrensen i 2022 og 2023 ",
                        Nynorsk to "",
                        English to ""
                    )
                }.orShowIf(inntekt2022Over3g) {
                    text(
                        Bokmal to "Din inntekt har ifølge opplysninger fra Skatteetaten vært høyere enn inntektsgrensen i 2022 ",
                        Nynorsk to "",
                        English to ""
                    )
                }.orShowIf(inntekt2023Over3g) {
                    text(
                        Bokmal to "Din inntekt har ifølge opplysninger fra Skatteetaten vært høyere enn inntektsgrensen i 2023 ",
                        Nynorsk to "",
                        English to ""
                    )
                }.orShow {
                    text(
                        Bokmal to "Din gjennomsnittlige inntekt mellom 2019 og 2023 har ifølge opplysninger fra Skatteetaten vært høyere enn 212 813 kroner",
                        Nynorsk to "",
                        English to ""
                    )
                }
            }

            paragraph {
                text(
                    Bokmal to "Under forutsetning av at de øvrige vilkårene for gjenlevendepensjon er oppfylt, vil du få utbetalt gjenlevendepensjonen til og med 31. desember 2026. ",
                    Nynorsk to "",
                    English to ""
                )
            }

            title1 {
                text(
                    Bokmal to "Mulighet for å søke utvidet stønadsperiode",
                    Nynorsk to "",
                    English to ""
                )
            }

            paragraph {
                text(
                    Bokmal to "Hvis du er under nødvendig og hensiktsmessig utdanning eller har behov for tiltak for å komme i arbeid, kan du søke om å få pensjonen forlenget i inntil to år fra 1. januar 2027. ",
                    Nynorsk to "",
                    English to ""
                )
            }

            title1 {
                text(
                    Bokmal to "Hva må du gjøre?",
                    Nynorsk to "",
                    English to ""
                )
            }

            paragraph {
                text(
                    Bokmal to "Dersom du mener at opplysningene i dette brevet er korrekt, trenger du ikke å gjøre noe.",
                    Nynorsk to "",
                    English to ""
                )
            }

            paragraph {
                text(
                    Bokmal to "Dersom du mener at opplysningene om inntekten din i dette brevet er feil, og inntekten din har vært lavere de fem siste årene, må du gi oss en tilbakemelding innen fire uker fra du mottok dette forhåndsvarselet. ",
                    Nynorsk to "",
                    English to ""
                )
            }

            paragraph {
                text(
                    Bokmal to "Du må da sende oss bekreftelse på at skattemeldingen din er endret. ",
                    Nynorsk to "",
                    English to ""
                )
            }


            paragraph {
                text(
                    Bokmal to "Vi vil da vurdere de nye opplysningene før vi gjør vedtak i saken din. ",
                    Nynorsk to "",
                    English to ""
                )
            }


            title1 {
                text(
                    Bokmal to "Har du helseutfordringer? ",
                    Nynorsk to "",
                    English to ""
                )
            }


            paragraph {
                text(
                    Bokmal to "Hvis du har helseutfordringer, kan du undersøke mulighetene for andre ytelser eller støtteordninger ved ditt lokale Nav-kontor og på nav.no/helse. ",
                    Nynorsk to "",
                    English to ""
                )
            }

          title1 {
                text(
                    Bokmal to "Meld fra om endringer ",
                    Nynorsk to "",
                    English to ""
                )
            }


            paragraph {
                text(
                    Bokmal to "Hvis du får endringer i inntekt, familiesituasjon, jobbsituasjon eller planlegger å flytte til et annet land, kan det påvirke gjenlevendepensjonen din. I slike tilfeller må du derfor straks melde fra til Nav. ",
                    Nynorsk to "",
                    English to ""
                )
            }


            title1 {
                text(
                    Bokmal to "Har du spørsmål? ",
                    Nynorsk to "",
                    English to ""
                )
            }


            paragraph {
                text(
                    Bokmal to "Du finner mer informasjon på nav.no/gjenlevendepensjon. ",
                    Nynorsk to "",
                    English to ""
                )
            }


            paragraph {
                text(
                    Bokmal to "På nav.no/kontakt kan du chatte eller skrive til oss.",
                    Nynorsk to "",
                    English to ""
                )
            }


            paragraph {
                text(
                    Bokmal to "Hvis du ikke finner svar på nav.no, kan du ringe oss på telefon 55 55 33 34, hverdager 09:00-15:00. ",
                    Nynorsk to "",
                    English to ""
                )
            }
        }
    }
}