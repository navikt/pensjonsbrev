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
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VedtakGjpForlengetArskull6270 : AutobrevTemplate<Gjenlevenderett2027Dto> {
    override val kode = Pesysbrevkoder.AutoBrev.GJP_VEDTAK_FORLENGELSE_62_70

    override val template: LetterTemplate<*, Gjenlevenderett2027Dto> = createTemplate(
        letterDataType = Gjenlevenderett2027Dto::class,
        languages = languages(Bokmal, Nynorsk),
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
                nynorsk { + "Vedtak – Gjenlevandepensjonen din blir forlenga " }
            )
        }

        outline {
            paragraph {
                text(
                    bokmal { + "Vi viser til tidligere informasjon om at Stortinget har vedtatt endringer i folketrygdlovens regler om ytelser til etterlatte. Endringene gjelder fra 1. januar 2024. " },
                    nynorsk { + "Vi viser til tidlegare informasjon om at Stortinget har vedteke endringar i reglane i folketrygdlova som gjeld ytingar til etterlatne. Endringa gjeld frå 1. januar 2024. " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Du fyller vilkårene for rett til pengestøtte som gjenlevende frem til du fyller 67 år. " },
                    nynorsk { + "Du oppfyller vilkåra for rett til pengestøtte som attlevande fram til du fyller 67 år. " }
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
                    bokmal { + "Opplysninger om inntekten din i perioden 2019–2023, viser at du fyller vilkårene for rett til pengestøtte som gjenlevende frem til du fyller 67 år. " },
                    nynorsk { + "Opplysningar om inntekta di i perioden 2019–2023 viser at du oppfyller vilkåra for rett til pengestøtte som attlevande fram til du fyller 67 år. " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Du beholder retten til gjenlevendepensjon til og med 31. desember 2028. Deretter vil pensjonen omregnes til omstillingsstønad fra 1. januar 2029. " },
                    nynorsk { + "Du beheld retten til gjenlevandepensjon til og med 31. desember 2028. Deretter vil pensjonen blir rekna om til omstillingsstønad frå 1. januar 2029. " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Omstillingsstønaden tilsvarer 2,25 ganger grunnbeløpet per 1. januar 2029. Dersom gjenlevendepensjonen din er beregnet med mindre enn 40 års trygdetid, vil omstillingsstønaden bli redusert tilsvarende. " },
                    nynorsk { + "Omstillingsstønaden tilsvarer 2,25 gongar grunnbeløpet per 1. januar 2029. Dersom gjenlevandepensjonen din er rekna ut med mindre enn 40 års trygdetid, vil omstillingsstønaden bli redusert tilsvarande. " }
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
                    bokmal { + "I tillegg må inntekten din i 2019–2023 ha vært under to ganger grunnbeløpet i folketrygden (G) i gjennomsnitt i disse fem årene. Det vil si at inntekten kan overstige to ganger grunnbeløpet i et enkelt år, så lenge gjennomsnittet av de fem årene er lavere. " },
                    nynorsk { + "I tillegg må inntekta di i 2019–2023 ha vore under to gongar grunnbeløpet i folketrygda (G) i gjennomsnitt i desse fem åra. Det vil seie at inntekta kan overstige to gongar grunnbeløpet i eitt enkelt år, så lenge gjennomsnittet av dei fem åra er lågare." }
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
                    bokmal { + "Det er din inntekt i årene 2019–2023 som avgjør om du kan beholde pengestøtte som gjenlevende til du blir 67 år. " },
                    nynorsk { + "Det er inntekta di i åra 2019–2023 som avgjer om du kan behalde pengestøtte som attlevande til du blir 67 år. " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Ifølge registeropplysninger vi har om deg fra Skatteetaten, har du i årene 2019–2023 hatt følgende inntekter: " },
                    nynorsk { + "Ifølgje registeropplysningar vi har om deg frå Skatteetaten, har du i åra 2019–2023 hatt følgjande inntekter: " }
                )
            }

            includePhrase(DineInntekterTabell(inntekt2019, inntekt2020, inntekt2021, inntekt2022, inntekt2023, gjennomsnittInntektG, inntekt2019G, inntekt2020G, inntekt2021G, inntekt2022G, inntekt2023G))

            paragraph {
                text(
                    bokmal { + "Din inntekt har ifølge opplysninger fra Skatteetaten vært lavere enn inntektsgrensene i disse årene. " },
                    nynorsk { + "Inntekta di har ifølgje opplysningar frå Skatteetaten vore lågare enn inntektsgrensene i desse åra. " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Under forutsetning av at de øvrige vilkårene er oppfylt vil du ha rett til pengestøtte som gjenlevende frem til du fyller 67 år. " },
                    nynorsk { + "Under føresetnad av at dei resterande vilkåra er oppfylte, vil du ha rett til pengestøtte som attlevande fram til du fyller 67 år. " }
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
                    bokmal { + "Hvis du får endringer i inntekt, familiesituasjon, jobbsituasjon eller planlegger å flytte til et annet land, kan det påvirke gjenlevendepensjonen din. I slike tilfeller må du derfor straks melde fra til Nav. " },
                    nynorsk { + "Dersom du planlegg å flytte til eit anna land eller du får endra inntekt, familiesituasjon eller jobbsituasjon, kan det påverke gjenlevandepensjonen din. I slike tilfelle må du difor straks melde frå til Nav. " }
                )
            }
            includePhrase(Felles.HarDuSpoersmaal(Constants.GJENLEVENDEPENSJON_URL, Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON))
        }
        includeAttachment(vedleggGjpDineRettigheterOgPlikter)
    }
}