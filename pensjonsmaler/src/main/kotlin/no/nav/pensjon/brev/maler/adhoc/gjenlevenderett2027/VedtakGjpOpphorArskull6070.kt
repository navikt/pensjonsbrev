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
import no.nav.pensjon.brev.maler.fraser.common.Constants.HELSE_URL
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata


@TemplateModelHelpers
object VedtakGjpOpphorArskull6070 : AutobrevTemplate<Gjenlevenderett2027Dto> {
    override val kode = Pesysbrevkoder.AutoBrev.GJP_VEDTAK_OPPHOR_60_70

    override val template: LetterTemplate<*, Gjenlevenderett2027Dto> = createTemplate(
        letterDataType = Gjenlevenderett2027Dto::class,
        languages = languages(Bokmal, Nynorsk),
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
                nynorsk { + "Vedtak – Gjenlevandepensjonen din er tidsavgrensa " },
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { + "Vi viser til tidligere informasjon om at Stortinget har vedtatt endringer i folketrygdlovens regler om ytelser til etterlatte. Endringene gjelder fra 1. januar 2024. " },
                    nynorsk { + "Vi viser til tidlegare informasjon om at Stortinget har vedteke endringar i reglane i folketrygdlova som gjeld ytingar til etterlatne. Endringa gjeld frå 1. januar 2024." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Din rett til gjenlevendepensjon opphører fra 1. januar 2027. " },
                    nynorsk { + "Retten din til gjenlevandepensjon opphøyrer frå 1. januar 2027. " }
                )
            }

            title1 {
                text(
                    bokmal { + "Begrunnelse for vedtaket " },
                    nynorsk { + "Grunngiving for vedtaket " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Opplysninger om inntekten din i perioden 2019-2023, viser at du ikke fyller vilkårene i folketrygdloven § 17 A-3. Din inntekt har vært over inntektsgrensen i denne perioden. " },
                    nynorsk { + "Opplysningar om inntekta di i perioden 2019–2023 viser at du ikkje oppfyller vilkåra i folketrygdlova § 17 A-3. Inntekta di har vore over inntektsgrensa i denne perioden." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Du beholder retten til gjenlevendepensjon frem til og med desember 2026, under forutsetning av at øvrige vilkår er oppfylt" },
                    nynorsk { + "Du beheld retten til gjenlevandepensjon fram til og med desember 2026, under føresetnad av at dei resterande vilkåra er oppfylte. " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Vedtaket er gjort etter folketrygdloven § 17 A-3. " },
                    nynorsk { + "Vedtaket er gjort etter folketrygdlova § 17 A-3. " }
                )
            }

            title1 {
                text(
                    bokmal { + "Hva er inntektsgrensene? " },
                    nynorsk { + "Kva er inntektsgrensene? " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Pensjonsgivende inntekt må ha vært under tre ganger gjennomsnittlig grunnbeløp i folketrygden (G) i både 2022 og 2023: " },
                    nynorsk { + "Pensjonsgivande inntekt må ha vore under tre gongar gjennomsnittleg grunnbeløp i folketrygda (G) i både 2022 og 2023: " }
                )
            }

            includePhrase(Gjennomsnittlig3GTabell)

            paragraph {
                text(
                    bokmal { + "I tillegg må inntekten din i 2019–2023 ha vært under to ganger grunnbeløpet i folketrygden (G) i gjennomsnitt i disse fem årene. " +
                            "Det vil si at inntekten kan overstige to ganger grunnbeløpet i et enkelt år, så lenge gjennomsnittet av de fem årene er lavere. " },
                    nynorsk { + "I tillegg må inntekta di i 2019–2023 ha vore under to gongar grunnbeløpet i folketrygda (G) i gjennomsnitt i desse fem åra. " +
                            "Det vil seie at inntekta kan overstige to gongar grunnbeløpet i eitt enkelt år, så lenge gjennomsnittet av dei fem åra er lågare. " }
                )
            }

            includePhrase(Gjennomsnittlig2GTabell)

            title1 {
                text(
                    bokmal { + "Hvilke opplysninger har vi om deg? " },
                    nynorsk { + "Kva opplysningar har vi om deg? " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Det er inntekten din i årene 2019–2023 som avgjør om du kan beholde gjenlevendepensjonen din til du blir 67 år. " },
                    nynorsk { + "Det er inntekta din i åra 2019–2023 som avgjer om du kan behalde gjenlevandepensjonen din til du blir 67 år. " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Ifølge registeropplysninger vi har om deg fra Skatteetaten har du i årene 2019–2023 hatt følgende inntekter: " },
                    nynorsk { + "Ifølgje registeropplysningar vi har om deg frå Skatteetaten, har du i åra 2019–2023 hatt følgjande inntekter: " }
                )
            }

            includePhrase(DineInntekterTabell(inntekt2019, inntekt2020, inntekt2021, inntekt2022, inntekt2023, gjennomsnittInntektG, inntekt2019G, inntekt2020G, inntekt2021G, inntekt2022G, inntekt2023G))

            showIf(inntekt2022Over3g and inntekt2023Over3g) {
                paragraph {
                    text(
                        bokmal { + "Din inntekt har ifølge opplysninger fra Skatteetaten vært høyere enn inntektsgrensen i 2022 og 2023. " },
                        nynorsk { + "Inntekta di har ifølgje opplysningar frå Skatteetaten vore høgare enn inntektsgrensa i 2022 og 2023." },
                    )
                }
            }.orShowIf(inntekt2022Over3g) {
                paragraph {
                    text(
                        bokmal { + "Din inntekt har ifølge opplysninger fra Skatteetaten vært høyere enn inntektsgrensen i 2022. " },
                        nynorsk { + "Inntekta di har ifølgje opplysningar frå Skatteetaten vore høgare enn inntektsgrensa i 2022." },
                    )
                }
            }.orShowIf(inntekt2023Over3g) {
                paragraph {
                    text(
                        bokmal { + "Din inntekt har ifølge opplysninger fra Skatteetaten vært høyere enn inntektsgrensen i 2023. " },
                        nynorsk { + "Inntekta di har ifølgje opplysningar frå Skatteetaten vore høgare enn inntektsgrensa i 2023." },
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        bokmal { + "Din inntekt har ifølge opplysninger fra Skatteetaten vært høyere enn inntektsgrensen i perioden 2019–2023. " },
                        nynorsk { + "Inntekta di har ifølgje opplysningar frå Skatteetaten vore høgare enn inntektsgrensa i perioden 2019–2023. " },
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Vi har vurdert om gjennomsnittet av din inntekt som antall G for disse årene har vært høyere enn 2 G. Resultatet viser at din gjennomsnittlige inntekt har vært " + gjennomsnittInntektG.format(6) + " G. " +
                                "Kravet om at inntekten må ha vært under to ganger grunnbeløpet i folketrygden (G) i gjennomsnitt i disse fem årene, er derfor ikke oppfylt." },
                        nynorsk { + "Vi har vurdert om gjennomsnittet av inntekta di som talet på G for desse åra har vore høgare enn 2 G. Resultatet viser at den gjennomsnittlege inntekta di har vore " + gjennomsnittInntektG.format(6) + " G. " +
                                "Kravet om at inntekta må ha vore under to gonger grunnbeløpet i folketrygda (G) i gjennomsnitt i desse fem åra, er difor ikkje oppfylt." }
                    )
                }
            }

            paragraph {
                text(
                    bokmal { + "Under forutsetning av at de øvrige vilkårene for gjenlevendepensjon er oppfylt, vil du få utbetalt gjenlevendepensjonen til og med 31. desember 2026. " },
                    nynorsk { + "Under føresetnad av at dei resterande vilkåra for gjenlevandepensjon er oppfylte, vil du få utbetalt gjenlevandepensjonen til og med 31. desember 2026. " }
                )
            }

            title1 {
                text(
                    bokmal { + "Mulighet for å søke utvidet stønadsperiode " },
                    nynorsk { + "Høve til å søkje om utvida stønadsperiode " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Hvis du er under nødvendig og hensiktsmessig utdanning eller har behov for tiltak for å komme i arbeid, kan du søke om å få pensjonen forlenget i inntil to år fra 1. januar 2027. " },
                    nynorsk { + "Dersom du er under nødvendig og føremålstenleg utdanning eller har behov for tiltak for å kome i arbeid, kan du søkje om å få pensjonen forlenga i inntil to år frå 1. januar 2027. " }
                )
            }

            title1 {
                text(
                    bokmal { + "Har du helseutfordringer? " },
                    nynorsk { + "Har du helseutfordringar? " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Hvis du har helseutfordringer, kan du undersøke mulighetene for andre ytelser eller støtteordninger ved ditt lokale Nav-kontor og på $HELSE_URL." },
                    nynorsk { + "Viss du har helseutfordringar, kan du undersøkje moglegheitene for andre ytingar eller støtteordningar ved ditt lokale Nav-kontor og på $HELSE_URL." }
                )
            }

            includePhrase(Felles.RettTilAAKlage(vedleggGjpDineRettigheterOgPlikter))
            includePhrase(Felles.RettTilInnsyn(vedleggGjpDineRettigheterOgPlikter))

            title1 {
                text(
                    bokmal { + "Meld fra om endringer " },
                    nynorsk { + "Meld frå om endringar " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Hvis du får endringer i inntekt eller jobbsituasjon, gifter deg eller planlegger å flytte til et annet land, kan det påvirke gjenlevendepensjonen din. I slike tilfeller må du derfor straks melde fra til Nav. " },
                    nynorsk { + "Dersom du planlegg å flytte til eit anna land eller du får endra inntekt, familiesituasjon eller jobbsituasjon, kan det påverke gjenlevandepensjonen din. I slike tilfelle må du difor straks melde frå til Nav. " }
                )
            }

            includePhrase(Felles.HarDuSpoersmaal(Constants.GJENLEVENDEPENSJON_URL, Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON))
        }
        includeAttachment(vedleggGjpDineRettigheterOgPlikter)
    }
}