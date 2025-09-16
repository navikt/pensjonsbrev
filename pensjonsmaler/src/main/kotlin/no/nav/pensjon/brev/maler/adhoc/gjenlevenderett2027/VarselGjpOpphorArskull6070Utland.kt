package no.nav.pensjon.brev.maler.adhoc.gjenlevenderett2027

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
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.includePhrase
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VarselGjpOpphorArskull6070Utland : AutobrevTemplate<Gjenlevenderett2027Dto> {


    override val kode = Pesysbrevkoder.AutoBrev.GJP_VARSEL_OPPHOR_60_70_UTLAND

    override val template: LetterTemplate<*, Gjenlevenderett2027Dto> = createTemplate(
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
                bokmal { + "Forhåndsvarsel – Gjenlevendepensjonen din kan bli tidsbegrenset " },
                english { + "Advance notice – Your survivor’s pension may become time-limited " }
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { + "Vi viser til tidligere informasjon om at Stortinget har vedtatt endringer i folketrygdlovens regler om ytelser til etterlatte. Endringene gjelder fra 1. januar 2024. " },
                    english { + "We refer to previously provided information that the Norwegian Parliament (the Storting) has adopted amendments to the National Insurance Act’s provisions on survivor’s benefits. These amendments were implemented on 01 January 2024. " }
                )
            }

            title1 {
                text(
                    bokmal { + "Hva betyr de nye reglene for deg? " },
                    english { + "What do the new rules mean for you? " }
                )

            }

            paragraph {
                text(
                    bokmal { + "Du beholder retten til gjenlevendepensjon. Du får den i tre år fra 1. januar 2024. Det kan bety at siste utbetaling av pensjonen din blir i desember 2026. " },
                    english { + "You retain your right to survivor’s pension. The pension will be paid for a period of three years from 01 January 2024. This could mean that you receive your final pension payment in December 2026. " }
                )
            }

            title1 {
                text(
                    bokmal { + "Hva er inntektsgrensene?" },
                    english { + "What are the income caps?" }
                )
            }

            paragraph {
                text(
                    bokmal { + "Pensjonsgivende inntekt må ha vært under tre ganger gjennomsnittlig grunnbeløp i folketrygden (G) i både 2022 og 2023: " },
                    english { + "Your income must not have exceeded three times the average National Insurance basic amount (G) in 2022 or 2023: " }
                )
            }

            includePhrase(Gjennomsnittlig3GTabell)

            paragraph {
                text(
                    bokmal { + "I tillegg må inntekten din i 2019–2023 ha vært under to ganger grunnbeløpet i folketrygden (G) i gjennomsnitt i disse fem årene. Det vil si at inntekten kan overstige to ganger grunnbeløpet i et enkelt år, så lenge gjennomsnittet av de fem årene er lavere." },
                    english { + "In addition, your income in the period 2019–2023 must not have exceeded two times the National Insurance basic amount (G) on average for this five-year period. This means your income could have exceeded two times the National Insurance basic amount in individual years, provided your average for the five-year period is lower. " }
                )
            }

            includePhrase(Gjennomsnittlig2GTabell)

            title1 {
                text(
                    bokmal { + "Hvilke opplysninger har vi om deg?" },
                    english { + "What information do we have about you?" }
                )
            }

            paragraph {
                text(
                    bokmal { + "Det er inntekten din i årene 2019–2023 som avgjør om du kan beholde gjenlevendepensjonen din til du blir 67 år. Ifølge registrerte opplysninger vi har om deg, har pensjonen din i årene 2019–2023 vært redusert etter følgende inntekter:" },
                    english { + "Your income for the period 2019–2023 will determine whether or not you will be able to keep the survivor’s pension until you turn 67 years old. According to the information we have registered about you, your pension in the period 2019–2023 has been reduced based on the following income:" }

                )
            }

            includePhrase(DineInntekterTabell(inntekt2019, inntekt2020, inntekt2021, inntekt2022, inntekt2023, gjennomsnittInntektG, inntekt2019G, inntekt2020G, inntekt2021G, inntekt2022G, inntekt2023G))

            paragraph {
                text(
                    bokmal { + "Ut fra våre opplysninger fyller du ikke vilkårene i folketrygdloven §17 A-3, for å få forlenget stønadsperiode." },
                    english { + "Based on the information we have about you, you do not meet the requirements for an extended benefit period, as established by Section 17 A-3 of the National Insurance Act." }
                )
            }

            showIf(inntekt2022Over3g and inntekt2023Over3g) {
                paragraph {
                    text(
                        bokmal { + "Din inntekt har ifølge våre opplysninger vært høyere enn inntektsgrensen i 2022 og 2023. " },
                        english { + "According to our information, your income has been higher than the income cap for 2022 and 2023." }
                    )
                }

            }.orShowIf(inntekt2022Over3g) {
                paragraph {
                    text(
                        bokmal { + "Din inntekt har ifølge våre opplysninger vært høyere enn inntektsgrensen i 2022. " },
                        english { + "According to our information, your income has been higher than the income cap for 2022." }
                    )
                }
            }.orShowIf(inntekt2023Over3g) {
                paragraph {
                    text(
                        bokmal { + "Din inntekt har ifølge våre opplysninger vært høyere enn inntektsgrensen i 2023. " },
                        english { + "According to our information, your income has been higher than the income cap for 2023." }
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        bokmal { + "Din inntekt har ifølge våre opplysninger vært høyere enn inntektsgrensen i perioden 2019–2023. " },
                        english { + "According to our information, your income has been higher than the income cap during the period 2019–2023. " },
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Vi har vurdert om gjennomsnittet av din inntekt som antall G for disse årene har vært høyere enn 2 G. Resultatet viser at din gjennomsnittlige inntekt har vært " + gjennomsnittInntektG.format(6) + " G. " +
                                "Kravet om at inntekten må ha vært under to ganger grunnbeløpet i folketrygden (G) i gjennomsnitt i disse fem årene, er derfor ikke oppfylt." },
                        english { + "We have assessed whether the average of your income, expressed as a number of G for these years, has been higher than 2 G. The result shows that your average income has been " + gjennomsnittInntektG.format(6) + " G. " +
                                "The requirement that the income must have been below twice the average National Insurance basic amount (G) on average during these five years is therefore not met." }
                    )
                }
            }


            paragraph {
                text(
                    bokmal { + "Under forutsetning av at de øvrige vilkårene for gjenlevendepensjon er oppfylt, vil du få utbetalt gjenlevendepensjonen til og med 31. desember 2026. " },
                    english { + "Provided the other requirements for survivor’s pension have been met, you will continue to receive survivor’s pension until 31 December 2026. " }
                )
            }

            title1 {
                text(
                    bokmal { + "Mulighet for å søke utvidet stønadsperiode" },
                    english { + "Option to apply for extended benefit period" }
                )
            }

            paragraph {
                text(
                    bokmal { + "Hvis du er under nødvendig og hensiktsmessig utdanning eller har behov for tiltak for å komme i arbeid, kan du søke om å få pensjonen forlenget i inntil to år fra 1. januar 2027. " },
                    english { + "If you are currently receiving a necessary and relevant education, or if you need further assistance to find work, you can apply to have the pension extended for a period of up to two years from 01 January 2027." }
                )
            }

            title1 {
                text(
                    bokmal { + "Hva må du gjøre?" },
                    english { + "What do you need to do? " }
                )
            }

            paragraph {
                text(
                    bokmal { + "Dersom du mener at opplysningene i dette brevet er korrekt, trenger du ikke å gjøre noe." },
                    english { + "If you believe the information in this letter is correct, you do not have to do anything." }
                )
            }

            paragraph {
                text(
                    bokmal { + "Dersom du mener at opplysningene om inntekten din i dette brevet er feil, og inntekten din har vært lavere de fem siste årene, må du gi oss en tilbakemelding innen fire uker fra du mottok dette forhåndsvarselet. " },
                    english { + "If you believe the information in this letter is incorrect, and your income has been lower in the last five years, you must contact us within four weeks of receiving this advance notice. " }
                )
            }

            paragraph {
                text(
                    bokmal { + "Du må da sende oss bekreftelse på at skattemeldingen din er endret. " },
                    english { + "You must send us pay slips from your employer, or a certificate issued by the authorities where you live, specifying your income for the period 2019–2023. " }
                )
            }


            paragraph {
                text(
                    bokmal { + "Vi vil da vurdere de nye opplysningene før vi gjør vedtak i saken din. " },
                    english { + "We will then consider the new information before we make a decision in your case. " }
                )
            }

            title1 {
                text(
                    bokmal { + "Meld fra om endringer " },
                    english { + "Report changes " }
                )
            }


            paragraph {
                text(
                    bokmal { + "Hvis du får endringer i inntekt, familiesituasjon, jobbsituasjon eller planlegger å flytte til et annet land, kan det påvirke gjenlevendepensjonen din. I slike tilfeller må du derfor straks melde fra til Nav. " },
                    english { + "If your income or employment situation changes, if you get married, or if you plan to move to another country, it could affect your survivor’s pension. That is why you must contact Nav immediately if any of these things occur. " }
                )
            }

            includePhrase(Felles.HarDuSpoersmaal(Constants.GJENLEVENDEPENSJON_URL, Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON))
        }
    }
}