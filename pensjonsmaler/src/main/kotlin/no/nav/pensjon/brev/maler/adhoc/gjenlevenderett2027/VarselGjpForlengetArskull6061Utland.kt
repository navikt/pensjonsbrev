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
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2023
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2023G
import no.nav.pensjon.brev.maler.adhoc.gjenlevenderett2027.Tabeller.DineInntekterTabell
import no.nav.pensjon.brev.maler.adhoc.gjenlevenderett2027.Tabeller.Gjennomsnittlig2GTabell
import no.nav.pensjon.brev.maler.adhoc.gjenlevenderett2027.Tabeller.Gjennomsnittlig3GTabell
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.includePhrase
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VarselGjpForlengetArskull6061Utland : AutobrevTemplate<Gjenlevenderett2027Dto> {


    override val kode = Pesysbrevkoder.AutoBrev.GJP_VARSEL_FORLENGELSE_60_61_UTLAND

    override val template: LetterTemplate<*, Gjenlevenderett2027Dto> = createTemplate(
        languages = languages(Bokmal, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Forhåndsvarsel - Gjenlevendepensjonen din kan bli forlenget",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                bokmal { + "Forhåndsvarsel – Gjenlevendepensjonen din kan bli forlenget" },
                english { + "Advance notice – Your survivor’s pension may become extended " }
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
                    bokmal { + "Hva betyr de nye reglene for deg?" },
                    english { + "What do the new rules mean for you? " }
                )

            }

            paragraph {
                text(
                    bokmal { + "Du beholder retten til gjenlevendepensjon. Du får den i tre år fra 1. januar 2024. Det kan bety at siste utbetaling av pensjonen din blir i desember 2026. " },
                    english { + "You retain your right to survivor’s pension. The pension will be paid for a period of three years from 01 January 2024. This could mean that you receive your final pension payment in December 2026. " }
                )

            }

            paragraph {
                text(
                    bokmal { + "Hvis du har hatt lav eller ingen inntekt de siste fem årene før 2024, kan du beholde gjenlevendepensjon til du blir 67 år. " },
                    english { + "If your income has been low or if you have not earned an income in the last five years prior to 2024, you may keep the survivor’s pension until you turn 67 years old. " }
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
                    bokmal { + "Inntekten må ha vært under tre ganger gjennomsnittlig grunnbeløp i folketrygden (G) i både 2022 og 2023: " },
                    english { + "Your income must not have exceeded three times the average National Insurance basic amount (G) in 2022 or 2023: " }
                )
            }

            includePhrase(Gjennomsnittlig3GTabell)

            paragraph {
                text(
                    bokmal { + "I tillegg må inntekten din i 2019–2023 ha vært under to ganger grunnbeløpet i folketrygden (G) i gjennomsnitt disse fem årene. Det vil si at inntekten kan overstige to ganger grunnbeløpet i et enkelt år, så lenge gjennomsnittet av de fem årene er lavere." },
                    english { + "In addition, your income in the period 2019–2023 must not have exceeded two times the National Insurance basic amount (G) on average for this five-year period. This means your income could have exceeded two times the National Insurance basic amount in individual years, provided your average for the five-year period is lower." }
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
                    bokmal { + "Det er din reelle inntekt i årene 2019–2023 som avgjør om du kan beholde gjenlevendepensjonen. " },
                    english { + "Your actual income for the period 2019–2023 will determine whether or not you will be able to keep the survivor’s pension. " }

                )
            }

            paragraph {
                text(
                    bokmal { + "Ifølge registrerte opplysninger vi har om deg, har pensjonen din i årene 2019–2023 vært redusert etter følgende inntekter:" },
                    english { + "According to the information we have registered about you, your pension in the period 2019–2023 has been reduced based on the following income:" }

                )
            }

            includePhrase(DineInntekterTabell(inntekt2019, inntekt2020, inntekt2021, inntekt2022, inntekt2023, gjennomsnittInntektG, inntekt2019G, inntekt2020G, inntekt2021G, inntekt2022G, inntekt2023G))

            paragraph {
                text(
                    bokmal { + "Din inntekt har ifølge våre opplysninger vært lavere enn inntektsgrensene. Dersom det er korrekt, kan du beholde gjenlevendepensjonen til og med måneden du fyller 67 år. " },
                    english { + "According to our information, your income has been lower than the income cap. Provided that is correct, you may keep the survivor’s pension until the month you turn 67 years old. " }
                )
            }

            title1 {
                text(
                    bokmal { + "Hva må du gjøre?" },
                    english { + "What do you need to do?" }
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
                    bokmal { + "Dersom du mener at opplysningene om inntekten din i dette brevet er feil, må du gi oss en tilbakemelding innen fire uker fra du mottok dette forhåndsvarselet. " },
                    english { + "If you believe the information in this letter is incorrect, you must contact us within four weeks of receiving this advance notice. " }
                )
            }

            paragraph {
                text(
                    bokmal { + "Du må da sende oss lønnsslipper fra din arbeidsgiver, eller bekreftelse fra myndighetene der du bor, som viser hvilke inntekter du har hatt i perioden 2019–2023. " },
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
                    bokmal { + "Hvis du får endringer i inntekt eller jobbsituasjon, gifter deg eller planlegger å flytte til et annet land, kan det påvirke gjenlevendepensjonen din. I slike tilfeller må du derfor straks melde fra til Nav. " },
                    english { + "If your income or employment situation changes, if you get married, or if you plan to move to another country, it could affect your survivor’s pension. That is why you must contact Nav immediately if any of these things occur. " }
                )
            }

            includePhrase(Felles.HarDuSpoersmaal(Constants.GJENLEVENDEPENSJON_URL, Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON))
        }
    }
}