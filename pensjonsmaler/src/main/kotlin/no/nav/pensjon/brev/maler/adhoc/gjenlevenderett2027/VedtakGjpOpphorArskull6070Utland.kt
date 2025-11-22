package no.nav.pensjon.brev.maler.adhoc.gjenlevenderett2027

import no.nav.pensjon.brev.api.model.maler.EmptyVedleggData
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
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.includeAttachment
import no.nav.pensjon.brev.template.includePhrase
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VedtakGjpOpphorArskull6070Utland : AutobrevTemplate<Gjenlevenderett2027Dto> {
    override val kode = Pesysbrevkoder.AutoBrev.GJP_VEDTAK_OPPHOR_60_70_UTLAND

    override val template: LetterTemplate<*, Gjenlevenderett2027Dto> = createTemplate(
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
                bokmal { + "Vedtak – Gjenlevendepensjonen din er tidsbegrenset " },
                english { + "Decision – Your survivor’s pension has been made time-limited " }
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { + "Vi viser til tidligere informasjon om at Stortinget har vedtatt endringer i folketrygdlovens regler om ytelser til etterlatte. Endringene gjelder fra 1. januar 2024.  " },
                    english { + "We refer to previously provided information that the Norwegian Parliament (the Storting) has adopted amendments to the National Insurance Act’s provisions on survivor’s benefits. These amendments were implemented on 01 January 2024.  " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Din rett til gjenlevendepensjon opphører fra 1. januar 2027. " },
                    english { + "Your right to a survivor’s pension will end on 01 January 2027. " }
                )
            }

            title1 {
                text(
                    bokmal { + "Begrunnelse for vedtaket " },
                    english { + "Grounds for the decision " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Opplysninger om inntekten din i perioden 2019–2023, viser at du ikke fyller vilkårene i folketrygdloven § 17 A-3. Din inntekt har vært over inntektsgrensen i denne perioden.  " },
                    english { + "Your income information for the period 2019–2023 shows that you do not meet the conditions established by Section 17 A-3 of the National Insurance Act. Your income has exceeded the income cap for this period.  " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Du beholder retten til gjenlevendepensjon frem til og med desember 2026, under forutsetning av at øvrige vilkår er oppfylt " },
                    english { + "You will retain your right to survivor’s pension until the end of December 2026, provided the other conditions have been met.  " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Vedtaket er gjort etter folketrygdloven § 17 A-3. " },
                    english { + "This decision has been made in accordance with Section 17 A-3 of the National Insurance Act. " }
                )
            }

            title1 {
                text(
                    bokmal { + "Hva er inntektsgrensene? " },
                    english { + "What are the income caps? " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Inntekten må ha vært under tre ganger gjennomsnittlig grunnbeløp i folketrygden (G) i både 2022 og 2023: " },
                    english { + "Your income must not have exceeded three times the average National Insurance basic amount (G) in 2022 or 2023:  " }
                )
            }

            includePhrase(Gjennomsnittlig3GTabell)

            paragraph {
                text(
                    bokmal { + "I tillegg må inntekten din i 2019–2023 ha vært under to ganger grunnbeløpet i folketrygden (G) i gjennomsnitt disse fem årene. " +
                            "Det vil si at inntekten kan overstige to ganger grunnbeløpet i et enkelt år, så lenge gjennomsnittet av de fem årene er lavere. " },
                    english { + "In addition, your income in the period 2019–2023 must not have exceeded two times the National Insurance basic amount (G) on average for this five-year period. " +
                            "This means your income could have exceeded two times the National Insurance basic amount in individual years, provided your average for the five-year period is lower. " }
                )
            }

            includePhrase(Gjennomsnittlig2GTabell)

            title1 {
                text(
                    bokmal { + "Hvilke opplysninger har vi om deg? " },
                    english { + "What information do we have about you? " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Det er dine reelle inntekter i årene 2019–2023 som avgjør om du kan beholde gjenlevendepensjonen.  " },
                    english { + "Your actual income for the period 2019–2023 will determine whether or not you will be able to keep the survivor’s pension.  " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Ifølge registrerte opplysninger vi har om deg, har pensjonen din i årene 2019–2023 vært redusert etter følgende inntekter: " },
                    english { + "According to the information we have registered about you, your pension in the period 2019–2023 has been reduced based on the following income: " }
                )
            }

            includePhrase(DineInntekterTabell(inntekt2019, inntekt2020, inntekt2021, inntekt2022, inntekt2023, gjennomsnittInntektG, inntekt2019G, inntekt2020G, inntekt2021G, inntekt2022G, inntekt2023G))

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
                    bokmal { + "Under forutsetning av at de øvrige vilkårene for gjenlevendepensjon er oppfylt, vil du få utbetalt gjenlevendepensjonen til og med 31. desember 2026.  " },
                    english { + "Provided the other requirements for survivor’s pension have been met, you will continue to receive survivor’s pension until 31 December 2026. " }
                )
            }

            title1 {
                text(
                    bokmal { + "Mulighet for å søke utvidet stønadsperiode " },
                    english { + "Option to apply for extended benefit period " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Hvis du er under nødvendig og hensiktsmessig utdanning eller har behov for tiltak for å komme i arbeid, kan du søke om å få pensjonen forlenget i inntil to år fra 1. januar 2027. " },
                    english { + "If you are currently receiving a necessary and relevant education, or if you need further assistance to find work, you can apply to have the pension extended for a period of up to two years from 01 January 2027. " }
                )
            }

            includePhrase(Felles.RettTilAAKlage)
            includePhrase(Felles.RettTilInnsyn(vedleggGjpDineRettigheterOgPlikter))

            title1 {
                text(
                    bokmal { + "Meld fra om endringer  " },
                    english { + "Report changes  " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Hvis du får endringer i inntekt eller jobbsituasjon, gifter deg eller planlegger å flytte til et annet land, kan det påvirke gjenlevendepensjonen din. I slike tilfeller må du derfor straks melde fra til Nav.  " },
                    english { + "If your income or employment situation changes, if you get married, or if you plan to move to another country, it could affect your survivor’s pension. That is why you must contact Nav immediately if any of these things occur.  " }
                )
            }

            includePhrase(Felles.HarDuSpoersmaal(Constants.GJENLEVENDEPENSJON_URL, Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON))
        }
        includeAttachment(vedleggGjpDineRettigheterOgPlikter, EmptyVedleggData.expr())

    }
}