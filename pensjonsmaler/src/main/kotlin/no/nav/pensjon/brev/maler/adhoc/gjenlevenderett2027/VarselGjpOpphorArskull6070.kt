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
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VarselGjpOpphorArskull6070 : AutobrevTemplate<Gjenlevenderett2027Dto> {


    override val kode = Pesysbrevkoder.AutoBrev.GJP_VARSEL_OPPHOR_60_70

    override val template: LetterTemplate<*, Gjenlevenderett2027Dto> = createTemplate(
        name = "GJP_VARSEL_FORLENGELSE_62_70_UTLAND",
        letterDataType = Gjenlevenderett2027Dto::class,
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Forhåndsvarsel - Gjenlevendepensjonen din kan bli tidsbegrenset ",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Forhåndsvarsel – Gjenlevendepensjonen din kan bli tidsbegrenset ",
                Nynorsk to "Førehandsvarsel – Gjenlevandepensjonen din kan bli tidsavgrensa ",
            )
        }
        outline {
            paragraph {
                text(
                    Bokmal to "Vi viser til tidligere informasjon om at Stortinget har vedtatt endringer i folketrygdlovens regler om ytelser til etterlatte. Endringene gjelder fra 1. januar 2024. ",
                    Nynorsk to "Vi viser til tidlegare informasjon om at Stortinget har vedteke endringar i reglane i folketrygdlova som gjeld ytingar til etterlatne. Endringa gjeld frå 1. januar 2024. ",
                )

            }

            title1 {
                text(
                    Bokmal to "Hva betyr de nye reglene for deg? ",
                    Nynorsk to "Kva betyr dei nye reglane for deg? ",
                )

            }

            paragraph {
                text(
                    Bokmal to "Du beholder retten til gjenlevendepensjon. Du får den i tre år fra 1. januar 2024. Det kan bety at siste utbetaling av pensjonen din blir i desember 2026. ",
                    Nynorsk to "Du beheld retten til gjenlevandepensjon. Du får denne i tre år frå 1. januar 2024. Det kan bety at siste utbetaling av pensjonen din blir i desember 2026. ",
                )

            }

            title1 {
                text(
                    Bokmal to "Hva er inntektsgrensene?",
                    Nynorsk to "Kva er inntektsgrensene?",
                )
            }

            paragraph {
                text(
                    Bokmal to "Pensjonsgivende inntekt må ha vært under tre ganger gjennomsnittlig grunnbeløp i folketrygden (G) i både 2022 og 2023: ",
                    Nynorsk to "Pensjonsgivande inntekt må ha vore under tre gongar gjennomsnittleg grunnbeløp i folketrygda (G) i både 2022 og 2023: ",
                )
            }

            includePhrase(Gjennomsnittlig3GTabell)

            paragraph {
                text(
                    Bokmal to "I tillegg må inntekten din i 2019–2023 ha vært under to ganger grunnbeløpet i folketrygden (G) i gjennomsnitt i disse fem årene. Det vil si at inntekten kan overstige to ganger grunnbeløpet i et enkelt år, så lenge gjennomsnittet av de fem årene er lavere.",
                    Nynorsk to "I tillegg må inntekta di i 2019–2023 ha vore under to gongar grunnbeløpet i folketrygda (G) i gjennomsnitt i desse fem åra. Det vil seie at inntekta kan overstige to gongar grunnbeløpet i eitt enkelt år, så lenge gjennomsnittet av dei fem åra er lågare.",
                )
            }

            includePhrase(Gjennomsnittlig2GTabell)

            title1 {
                text(
                    Bokmal to "Hvilke opplysninger har vi om deg?",
                    Nynorsk to "Kva opplysningar har vi om deg?",
                )
            }

            paragraph {
                text(
                    Bokmal to "Ifølge registeropplysninger vi har om deg fra Skatteetaten har du i årene 2019–2023 hatt følgende pensjonsgivende inntekt:",
                    Nynorsk to "Ifølgje registeropplysningar vi har om deg frå Skatteetaten, har du i åra 2019–2023 hatt følgjande pensjonsgivande inntekt:",

                    )
            }

            includePhrase(DineInntekterTabell(inntekt2019, inntekt2020, inntekt2021, inntekt2022, inntekt2023, gjennomsnittInntektG, inntekt2019G, inntekt2020G, inntekt2021G, inntekt2022G, inntekt2023G))

            paragraph {
                text(
                    Bokmal to "Det er inntekten din i årene 2019–2023 som avgjør om du kan beholde gjenlevendepensjonen din til du blir 67 år. ",
                    Nynorsk to "Det er inntekta din i åra 2019–2023 som avgjer om du kan behalde gjenlevandepensjonen din til du blir 67 år. ",
                )
            }



            paragraph {
                text(
                    Bokmal to "Ut fra våre opplysninger fyller du ikke vilkårene i folketrygdloven § 17 A-3, for å få forlenget stønadsperiode.",
                    Nynorsk to "Ut frå opplysningane vi har, oppfyller du ikkje vilkåra i folketrygdlova § 17 A-3 for å få forlenga stønadsperiode.",
                )
            }

            showIf(inntekt2022Over3g and inntekt2023Over3g) {
                paragraph {
                    text(
                        Bokmal to "Din inntekt har ifølge opplysninger fra Skatteetaten vært høyere enn inntektsgrensen i 2022 og 2023. ",
                        Nynorsk to "Inntekta di har ifølgje opplysningar frå Skatteetaten vore høgare enn inntektsgrensa i 2022 og 2023.",
                    )
                }
            }.orShowIf(inntekt2022Over3g) {
                paragraph {
                    text(
                        Bokmal to "Din inntekt har ifølge opplysninger fra Skatteetaten vært høyere enn inntektsgrensen i 2022. ",
                        Nynorsk to "Inntekta di har ifølgje opplysningar frå Skatteetaten vore høgare enn inntektsgrensa i 2022.",
                    )
                }
            }.orShowIf(inntekt2023Over3g) {
                paragraph {
                    text(
                        Bokmal to "Din inntekt har ifølge opplysninger fra Skatteetaten vært høyere enn inntektsgrensen i 2023. ",
                        Nynorsk to "Inntekta di har ifølgje opplysningar frå Skatteetaten vore høgare enn inntektsgrensa i 2023.",
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        Bokmal to "Din inntekt har ifølge opplysninger fra Skatteetaten vært høyere enn inntektsgrensen i perioden 2019–2023. ",
                        Nynorsk to "Inntekta di har ifølgje opplysningar frå Skatteetaten vore høgare enn inntektsgrensa i perioden 2019–2023. ",
                    )
                }
                paragraph {
                    textExpr(
                        Bokmal to "Vi har vurdert om gjennomsnittet av din inntekt som antall G for disse årene har vært høyere enn 2 G. Resultatet viser at din gjennomsnittlige inntekt har vært ".expr() + gjennomsnittInntektG.format(6) + " G. " +
                                "Kravet om at inntekten må ha vært under to ganger grunnbeløpet i folketrygden (G) i gjennomsnitt i disse fem årene, er derfor ikke oppfylt.",
                        Nynorsk to "Vi har vurdert om gjennomsnittet av inntekta di som talet på G for desse åra har vore høgare enn 2 G. Resultatet viser at den gjennomsnittlege inntekta di har vore ".expr() + gjennomsnittInntektG.format(6) + " G. " +
                                "Kravet om at inntekta må ha vore under to gonger grunnbeløpet i folketrygda (G) i gjennomsnitt i desse fem åra, er difor ikkje oppfylt."
                    )
                }
            }

            paragraph {
                text(
                    Bokmal to "Under forutsetning av at de øvrige vilkårene for gjenlevendepensjon er oppfylt, vil du få utbetalt gjenlevendepensjonen til og med 31. desember 2026. ",
                    Nynorsk to "Under føresetnad av at dei resterande vilkåra for gjenlevandepensjon er oppfylte, vil du få utbetalt gjenlevandepensjonen til og med 31. desember 2026. ",
                )
            }

            title1 {
                text(
                    Bokmal to "Mulighet for å søke utvidet stønadsperiode",
                    Nynorsk to "Høve til å søkje om utvida stønadsperiode",
                )
            }

            paragraph {
                text(
                    Bokmal to "Hvis du er under nødvendig og hensiktsmessig utdanning eller har behov for tiltak for å komme i arbeid, kan du søke om å få pensjonen forlenget i inntil to år fra 1. januar 2027. ",
                    Nynorsk to "Dersom du er under nødvendig og føremålstenleg utdanning eller har behov for tiltak for å kome i arbeid, kan du søkje om å få pensjonen forlenga i inntil to år frå 1. januar 2027. ",
                )
            }

            title1 {
                text(
                    Bokmal to "Hva må du gjøre?",
                    Nynorsk to "Kva må du gjere?",
                )
            }

            paragraph {
                text(
                    Bokmal to "Dersom du mener at opplysningene i dette brevet er korrekt, trenger du ikke å gjøre noe.",
                    Nynorsk to "Dersom du meiner at opplysningane i dette brevet er korrekte, treng du ikkje å gjere noko.",
                )
            }

            paragraph {
                text(
                    Bokmal to "Dersom du mener at opplysningene om inntekten din i dette brevet er feil, og inntekten din har vært lavere de fem siste årene, må du gi oss en tilbakemelding innen fire uker fra du mottok dette forhåndsvarselet. ",
                    Nynorsk to "Dersom du meiner at opplysningane om inntekta di i dette brevet er feil, og inntekta di har vore lågare dei fem siste åra, må du gi oss tilbakemelding innan fire veker frå du fekk dette førehandsvarselet. ",
                )
            }

            paragraph {
                text(
                    Bokmal to "Du må da sende oss bekreftelse på at skattemeldingen din er endret. ",
                    Nynorsk to "Du må då sende oss stadfesting på at skattemeldinga di er endra. ",
                )
            }


            paragraph {
                text(
                    Bokmal to "Vi vil da vurdere de nye opplysningene før vi gjør vedtak i saken din. ",
                    Nynorsk to "Vi vil då vurdere dei nye opplysningane før vi gjer vedtak i saka di. ",
                )
            }


            title1 {
                text(
                    Bokmal to "Har du helseutfordringer? ",
                    Nynorsk to "Har du helseutfordringar? ",
                )
            }


            paragraph {
                text(
                    Bokmal to "Hvis du har helseutfordringer, kan du undersøke mulighetene for andre ytelser eller støtteordninger ved ditt lokale Nav-kontor og på nav.no/helse. ",
                    Nynorsk to "Viss du har helseutfordringar, kan du undersøkje moglegheitene for andre ytingar eller støtteordningar ved ditt lokale Nav-kontor og på nav.no/helse. ",
                )
            }

            title1 {
                text(
                    Bokmal to "Meld fra om endringer ",
                    Nynorsk to "Meld frå om endringar ",
                )
            }


            paragraph {
                text(
                    Bokmal to "Hvis du får endringer i inntekt, familiesituasjon, jobbsituasjon eller planlegger å flytte til et annet land, kan det påvirke gjenlevendepensjonen din. I slike tilfeller må du derfor straks melde fra til Nav. ",
                    Nynorsk to "Dersom du planlegg å flytte til eit anna land eller du får endra inntekt, familiesituasjon eller jobbsituasjon, kan det påverke gjenlevandepensjonen din. I slike tilfelle må du difor straks melde frå til Nav. ",
                )
            }

            includePhrase(Felles.HarDuSpoersmaal(Constants.GJENLEVENDEPENSJON_URL, Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON))
        }
    }
}