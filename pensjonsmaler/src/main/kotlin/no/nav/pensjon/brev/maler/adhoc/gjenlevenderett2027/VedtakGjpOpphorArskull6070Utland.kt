package no.nav.pensjon.brev.maler.adhoc.gjenlevenderett2027

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027Dto
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.gjennomsnittInntektG
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2019
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2019G
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2020
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2020G
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2021
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2021G
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2022
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2022G
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2022Over3g
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2023
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2023G
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2023Over3g
import no.nav.pensjon.brev.maler.adhoc.gjenlevenderett2027.Tabeller.DineInntekterTabell
import no.nav.pensjon.brev.maler.adhoc.gjenlevenderett2027.Tabeller.Gjennomsnittlig2GTabell
import no.nav.pensjon.brev.maler.adhoc.gjenlevenderett2027.Tabeller.Gjennomsnittlig3GTabell
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.LocalizedFormatter.CurrencyFormat
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brev.template.includeAttachment
import no.nav.pensjon.brev.template.includePhrase
import no.nav.pensjon.brev.template.namedReference
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VedtakGjpOpphorArskull6070Utland : AutobrevTemplate<Gjenlevenderett2027Dto> {
    override val kode = Pesysbrevkoder.AutoBrev.GJP_VEDTAK_OPPHOR_60_70_UTLAND

    override val template: LetterTemplate<*, Gjenlevenderett2027Dto> = createTemplate(
        name = kode.name,
        letterDataType = Gjenlevenderett2027Dto::class,
        languages = languages(Bokmal, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - Gjenlevendepensjonen din er tidsbegrenset",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Vedtak – Gjenlevendepensjonen din er tidsbegrenset ",
                English to "Decision – Your survivor’s pension has been made time-limited "
            )
        }
        outline {
            paragraph {
                text(
                    Bokmal to "Vi viser til tidligere informasjon om at Stortinget har vedtatt endringer i folketrygdlovens regler om ytelser til etterlatte. Endringene gjelder fra 1. januar 2024.  ",
                    English to "We refer to previously provided information that the Norwegian Parliament (the Storting) has adopted amendments to the National Insurance Act’s provisions on survivor’s benefits. These amendments were implemented on 01 January 2024.  "
                )
            }
            paragraph {
                text(
                    Bokmal to "Din rett til gjenlevendepensjon opphører fra 1. januar 2027. ",
                    English to "Your right to a survivor’s pension will end on 01 January 2027. "
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
                    Bokmal to "Opplysninger om inntekten din i perioden 2019–2023, viser at du ikke fyller vilkårene i folketrygdloven § 17 A-3. Din inntekt har vært over inntektsgrensen i denne perioden.  ",
                    English to "Your income information for the period 2019–2023 shows that you do not meet the conditions established by Section 17 A-3 of the National Insurance Act. Your income has exceeded the income cap for this period.  "
                )
            }
            paragraph {
                text(
                    Bokmal to "Du beholder retten til gjenlevendepensjon frem til og med desember 2026, under forutsetning av at øvrige vilkår er oppfylt ",
                    English to "You will retain your right to survivor’s pension until the end of December 2026, provided the other conditions have been met.  "
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
                    Bokmal to "Inntekten må ha vært under tre ganger gjennomsnittlig grunnbeløp i folketrygden (G) i både 2022 og 2023: ",
                    English to "Your income must not have exceeded three times the average National Insurance basic amount (G) in 2022 or 2023:  "
                )
            }

            includePhrase(Gjennomsnittlig3GTabell)

            paragraph {
                text(
                    Bokmal to "I tillegg må inntekten din i 2019–2023 ha vært under to ganger grunnbeløpet i folketrygden (G) i gjennomsnitt disse fem årene. " +
                            "Det vil si at inntekten kan overstige to ganger grunnbeløpet i et enkelt år, så lenge gjennomsnittet av de fem årene er lavere. ",
                    English to "In addition, your income in the period 2019–2023 must not have exceeded two times the National Insurance basic amount (G) on average for this five-year period. " +
                            "This means your income could have exceeded two times the National Insurance basic amount in individual years, provided your average for the five-year period is lower. "
                )
            }

            includePhrase(Gjennomsnittlig2GTabell)

            title1 {
                text(
                    Bokmal to "Hvilke opplysninger har vi om deg? ",
                    English to "What information do we have about you? "
                )
            }
            paragraph {
                text(
                    Bokmal to "Det er dine reelle inntekter i årene 2019–2023 som avgjør om du kan beholde gjenlevendepensjonen.  ",
                    English to "Your actual income for the period 2019–2023 will determine whether or not you will be able to keep the survivor’s pension.  "
                )
            }
            paragraph {
                text(
                    Bokmal to "Ifølge registrerte opplysninger vi har om deg, har pensjonen din i årene 2019–2023 vært redusert etter følgende inntekter: ",
                    English to "According to the information we have registered about you, your pension in the period 2019–2023 has been reduced based on the following income: "
                )
            }

            includePhrase(DineInntekterTabell(inntekt2019, inntekt2020, inntekt2021, inntekt2022, inntekt2023, gjennomsnittInntektG, inntekt2019G, inntekt2020G, inntekt2021G, inntekt2022G, inntekt2023G))

            showIf(inntekt2022Over3g and inntekt2023Over3g) {
                paragraph {
                    text(
                        Bokmal to "Din inntekt har ifølge våre opplysninger vært høyere enn inntektsgrensen i 2022 og 2023. ",
                        English to "According to our information, your income has been higher than the income cap for 2022 and 2023."
                    )
                }

            }.orShowIf(inntekt2022Over3g) {
                paragraph {
                    text(
                        Bokmal to "Din inntekt har ifølge våre opplysninger vært høyere enn inntektsgrensen i 2022. ",
                        English to "According to our information, your income has been higher than the income cap for 2022."
                    )
                }
            }.orShowIf(inntekt2023Over3g) {
                paragraph {
                    text(
                        Bokmal to "Din inntekt har ifølge våre opplysninger vært høyere enn inntektsgrensen i 2023. ",
                        English to "According to our information, your income has been higher than the income cap for 2023."
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        Bokmal to "Din inntekt har ifølge våre opplysninger vært høyere enn inntektsgrensen i perioden 2019–2023. ",
                        English to "According to our information, your income has been higher than the income cap during the period 2019–2023. ",
                    )
                }
                paragraph {
                    textExpr(
                        Bokmal to "Vi har vurdert om gjennomsnittet av din inntekt som antall G for disse årene har vært høyere enn 2 G. Resultatet viser at din gjennomsnittlige inntekt har vært ".expr() + gjennomsnittInntektG.format(6) + " G. " +
                                "Kravet om at inntekten må ha vært under to ganger grunnbeløpet i folketrygden (G) i gjennomsnitt i disse fem årene, er derfor ikke oppfylt.",
                        English to "We have assessed whether the average of your income, expressed as a number of G for these years, has been higher than 2 G. The result shows that your average income has been ".expr() + gjennomsnittInntektG.format(6) + " G. " +
                                "The requirement that the income must have been below twice the average National Insurance basic amount (G) on average during these five years is therefore not met."
                    )
                }
            }

            paragraph {
                text(
                    Bokmal to "Under forutsetning av at de øvrige vilkårene for gjenlevendepensjon er oppfylt, vil du få utbetalt gjenlevendepensjonen til og med 31. desember 2026.  ",
                    English to "Provided the other requirements for survivor’s pension have been met, you will continue to receive survivor’s pension until 31 December 2026. "
                )
            }

            title1 {
                text(
                    Bokmal to "Mulighet for å søke utvidet stønadsperiode ",
                    English to "Option to apply for extended benefit period "
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du er under nødvendig og hensiktsmessig utdanning eller har behov for tiltak for å komme i arbeid, kan du søke om å få pensjonen forlenget i inntil to år fra 1. januar 2027. ",
                    English to "If you are currently receiving a necessary and relevant education, or if you need further assistance to find work, you can apply to have the pension extended for a period of up to two years from 01 January 2027. "
                )
            }

            includePhrase(Felles.RettTilAAKlage(vedleggGjpDineRettigheterOgPlikter))

            title1 {
                text(
                    Bokmal to "Du har rett til innsyn ",
                    English to "You have the right to access your file "
                )
            }
            paragraph {
                text(
                    Bokmal to "Du har rett til å se dokumentene i saken din. Se vedlegg ",
                    English to "You have the right to access all the documents relevant to your case. In the attached "
                )
                namedReference(vedleggGjpDineRettigheterOgPlikter)
                text(
                    Bokmal to " for informasjon om hvordan du går fram.",
                    English to ", you can read more about how to proceed."
                )
            }

            title1 {
                text(
                    Bokmal to "Meld fra om endringer  ",
                    English to "Report changes  "
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du får endringer i inntekt eller jobbsituasjon, gifter deg eller planlegger å flytte til et annet land, kan det påvirke gjenlevendepensjonen din. I slike tilfeller må du derfor straks melde fra til Nav.  ",
                    English to "If your income or employment situation changes, if you get married, or if you plan to move to another country, it could affect your survivor’s pension. That is why you must contact Nav immediately if any of these things occur.  "
                )
            }

            title1 {
                text(
                    Bokmal to "Har du spørsmål?  ",
                    English to "Do you have questions?  "
                )
            }
            paragraph {
                text(
                    Bokmal to "Du finner mer informasjon på nav.no/gjenlevendepensjon.  ",
                    English to "You can read more at nav.no/gjenlevendepensjon.  "
                )
            }
            paragraph {
                text(
                    Bokmal to "På nav.no/kontakt kan du chatte eller skrive til oss.  ",
                    English to "You can chat with us or write to us at nav.no/kontakt.  "
                )
            }

            paragraph {
                text(
                    Bokmal to "Hvis du ikke finner svar på nav.no, kan du ringe oss på telefon 55 55 33 34, hverdager 09:00-15:00.  ",
                    English to "If you can’t find the answers you need on nav.no, you can call us at tel. 55 55 33 34 on weekdays 09:00–15:00.  "
                )
            }
        }
        includeAttachment(vedleggGjpDineRettigheterOgPlikter, EmptyBrevdata.expr())

    }
}