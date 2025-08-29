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
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.includeAttachment
import no.nav.pensjon.brev.template.includePhrase
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VedtakGjpForlengetArskull6270Utland : AutobrevTemplate<Gjenlevenderett2027Dto> {
    override val kode = Pesysbrevkoder.AutoBrev.GJP_VEDTAK_FORLENGELSE_62_70_UTLAND

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
                bokmal { + "Vedtak – Gjenlevendepensjonen din forlenges " },
                english { + "Decision – Your survivor’s pension has been extended " }
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
                    bokmal { + "Du fyller vilkårene for rett til en pengestøtte som gjenlevende frem til du fyller 67 år. " },
                    english { + "You meet the conditions for a survivor’s benefit until you turn 67 years old. " }
                )
            }

            title1 {
                text(
                    bokmal { + "Begrunnelse for vedtaket " },
                    english { +  "Grounds for the decision " }
                )
            }
            paragraph {
                text(
                    bokmal { +  "Opplysninger om inntekten din i perioden 2019–2023, viser at du fyller vilkårene for rett til pengestøtte som gjenlevende frem til du fyller 67 år.  " },
                    english { +  "Your income information for the period 2019–2023 shows that you qualify for a survivor’s benefit until you turn 67 years old.  " }
                )
            }
            paragraph {
                text(
                    bokmal { +  "Du beholder retten til gjenlevendepensjon til og med 31. desember 2028. Deretter vil pensjonen omregnes til omstillingsstønad fra 1. januar 2029.  " },
                    english { +  "You retain your right to a survivor’s pension until 31 December 2028. After this date, your pension is converted into an adjustment allowance from 01 January 2029.  " }
                )
            }
            paragraph {
                text(
                    bokmal { +  "Omstillingsstønaden tilsvarer 2,25 ganger grunnbeløpet per 1. januar 2029. Dersom gjenlevendepensjonen din er beregnet med mindre enn 40 års trygdetid, vil omstillingsstønaden bli redusert tilsvarende. " },
                    english { +  "The adjustment allowance is equivalent to 2.25 times the National Insurance basic amount (G) as at 01 January 2029. If your survivor’s pension is based on a period of national insurance coverage of less than 40 years, your adjustment allowance will be reduced correspondingly. " }
                )
            }
            paragraph {
                text(
                    bokmal { +  "Vedtaket er gjort etter folketrygdloven § 17 A-3. " },
                    english { +  "This decision has been made in accordance with Section 17 A-3 of the National Insurance Act. " }
                )
            }

            title1 {
                text(
                    bokmal { +  "Hva er inntektsgrensene? " },
                    english { +  "What are the income caps? " }
                )
            }
            paragraph {
                text(
                    bokmal { +  "Inntekten må ha vært under tre ganger gjennomsnittlig grunnbeløp i folketrygden (G) i både 2022 og 2023:  " },
                    english { +  "Your income must not have exceeded three times the average National Insurance basic amount (G) in 2022 or 2023:  " }
                )
            }

            includePhrase(Gjennomsnittlig3GTabell)

            paragraph {
                text(
                    bokmal { +  "I tillegg må inntekten din i 2019–2023 ha vært under to ganger grunnbeløpet i folketrygden (G) i gjennomsnitt disse fem årene. " +
                            "Det vil si at inntekten kan overstige to ganger grunnbeløpet i et enkelt år, så lenge gjennomsnittet av de fem årene er lavere. " },
                    english { +  "In addition, your income in the period 2019–2023 must not have exceeded two times the National Insurance basic amount (G) on average for this five-year period. " +
                            "This means your income could have exceeded two times the National Insurance basic amount in individual years, provided your average for the five-year period is lower. " }
                )
            }

            includePhrase(Gjennomsnittlig2GTabell)

            title1 {
                text(
                    bokmal { +  "Hvilke opplysninger har vi om deg? " },
                    english { +  "What information do we have about you? " }
                )
            }
            paragraph {
                text(
                    bokmal { +  "Ifølge registrerte opplysninger vi har om deg, har pensjonen din i årene 2019–2023 vært redusert etter følgende inntekter: " },
                    english { +  "According to the information we have registered about you, your pension in the period 2019–2023 has been reduced based on the following income: " }
                )
            }


            includePhrase(DineInntekterTabell(inntekt2019, inntekt2020, inntekt2021, inntekt2022, inntekt2023, gjennomsnittInntektG, inntekt2019G, inntekt2020G, inntekt2021G, inntekt2022G, inntekt2023G))


            paragraph {
                text(
                    bokmal { +  "Det er dine reelle inntekter i årene 2019–2023 som avgjør om du kan beholde pengestøtte som gjenlevende. " },
                    english { +  "Your actual income for the period 2019–2023 will determine whether or not you will be able to keep the survivor’s benefit. " }
                )
            }
            paragraph {
                text(
                    bokmal { +  "Din inntekt har ifølge våre opplysninger vært lavere enn inntektsgrensene i disse årene. " },
                    english { +  "According to our information, your income has been lower than the income cap during this time period. " }
                )
            }
            paragraph {
                text(
                    bokmal { +  "Under forutsetning av at de øvrige vilkårene for gjenlevendepensjon er oppfylt, vil du få utbetalt gjenlevendepensjonen til og med måneden du fyller 67 år. " },
                    english { +  "Provided the other requirements for survivor’s pension have been met, you will continue to receive the survivor’s pension until you turn 67 years old. " }
                )
            }

            includePhrase(Felles.RettTilAAKlage(vedleggGjpDineRettigheterOgPlikter))
            includePhrase(Felles.RettTilInnsyn(vedleggGjpDineRettigheterOgPlikter))

            title1 {
                text(
                    bokmal { +  "Meld fra om endringer " },
                    english { +  "Report changes " }
                )
            }
            paragraph {
                text(
                    bokmal { +  "Hvis du får endringer i inntekt eller jobbsituasjon, gifter deg eller planlegger å flytte til et annet land, kan det påvirke gjenlevendepensjonen din. I slike tilfeller må du derfor straks melde fra til Nav.  " },
                    english { +  "If your income or employment situation changes, if you get married, or if you plan to move to another country, it could affect your survivor’s pension. That is why you must contact Nav immediately if any of these things occur.  " }
                )
            }

            includePhrase(Felles.HarDuSpoersmaal(Constants.GJENLEVENDEPENSJON_URL, Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON))
        }
        includeAttachment(vedleggGjpDineRettigheterOgPlikter, EmptyBrevdata.expr())
    }
}