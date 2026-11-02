package no.nav.pensjon.brev.maler.klagebrev

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Constants.KLAGE_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.KONTAKT_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KLAGEINSTANS
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Felles.fulltNavn
import no.nav.pensjon.brev.maler.klagebrev.OversendelsesbrevTilKlageinstansUT.Paragraf.Oversittetklagefrist
import no.nav.pensjon.brev.maler.klagebrev.tekstNAY.OversendelsesbrevTilKlageinstansTekst
import no.nav.pensjon.brev.maler.klagebrev.tekstNAY.OversendelsesbrevTilKlageinstansTekst.Yrkesskade.Avslag
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.saksbehandlervalg
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.selectors.brevbakerFelles.bruker
import no.nav.pensjon.brevbaker.api.model.selectors.brevbakerFelles.bruker.foedselsnummer

@TemplateModelHelpers
object OversendelsesbrevTilKlageinstansUT : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val featureToggle = FeatureToggles.brevmalKlageOversendelsesbrevTilKlageinstansUT.toggle

    override val kode = Pesysbrevkoder.Redigerbar.UT_KLAGE_OVERSENDELSESBREV_TIL_KLAGEINSTANS
    override val kategori = Brevkategori.KLAGE_OG_ANKE
    override val brevkontekst = TemplateDescription.Brevkontekst.ALLE
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Language.Bokmal),
        letterMetadata = LetterMetadata(displayTitle = "Klage - oversendelse til Nav klageinstans",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {

        val paragraf = saksbehandlervalg("paragraf", "Velg paragraf:").enum<Paragraf>()
        val avslag = saksbehandlervalg("avslag", "Avslag").enum<Avslag>()

        title { text(bokmal { +"Oversendelse til $NAV_KLAGEINSTANS - Uføretrygd" }) }

        outline {

            paragraph {
                text(bokmal { +"Klager: " })
                text(bokmal { +felles.bruker.fulltNavn() + " " })
                text(bokmal { +felles.bruker.foedselsnummer.format() })
            }

            paragraph {
                text(bokmal {
                    +"Vi viser til din klage av "
                    +fritekst("dato") + " på vedtak av "
                    +fritekst("dato") + "."
                })
            }
            paragraph { text(bokmal { +"Vi har vurdert vedtaket vårt på nytt, men har ikke endret det." }) }
            paragraph {
                text(bokmal {
                    +"Klagesaken er derfor oversendt til $NAV_KLAGEINSTANS for behandling. "
                    +"Kopi av innstillingen vår er vedlagt under."
                })
            }
            paragraph {
                text(bokmal {
                    +"Klageinstansen vurderer alle sider av saken på selvstendig grunnlag. "
                    +"Resultatet av klagebehandlingen kan bli at vårt vedtak ikke blir endret, eller at det blir endret helt eller delvis. "
                    +"Klageinstansen kan også oppheve vedtaket vårt, og sende saken tilbake til oss for helt eller delvis ny behandling. "
                })
            }
            paragraph { text(bokmal { +"Du får melding fra $NAV_KLAGEINSTANS når de har mottatt saken." }) }
            paragraph {
                text(bokmal {
                    +"Du finner oversikt over saksbehandlingstidene på $NAV_URL/saksbehandlingstider. "
                    +"Du får beskjed fra $NAV_KLAGEINSTANS, dersom de trenger mer tid."
                })
            }
            paragraph {
                text(bokmal {
                    +"Du kan sende merknader og dokumentasjon til $NAV_KLAGEINSTANS. "
                    +"Du kan logge deg inn på $KONTAKT_URL og sende skriftlig melding der. "
                    +"Hvis du ønsker å ettersende dokumentasjon, kan du gå til $KLAGE_URL og trykke på 'Ettersend dokumentasjon' for det saken gjelder."
                })
            }

            includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)

            title1 { text(bokmal { +"Innstillingen til $NAV_KLAGEINSTANS - uføretrygd" }) }

            title1 { text(bokmal { +"Hva klagesaken gjelder" }) }

            showIf(paragraf.isOneOf(Oversittetklagefrist)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.OversittetKlagefrist)
            }.orShowIf(paragraf.isOneOf(Paragraf.EosArtikkel57)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.EoesArtikkel57)
            }.orShowIf(paragraf.isOneOf(Paragraf.Medlemskap)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.Medlemskap)
            }.orShowIf(paragraf.isOneOf(Paragraf.HensiktsmessigBehandlingOgTiltak)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.HensiktsmessigBehandlingOgTiltak)
            }.orShowIf(paragraf.isOneOf(Paragraf.KunArbeidsrettedeTiltak)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.KunArbeidsrettedeTiltak)
            }.orShowIf(paragraf.isOneOf(Paragraf.HovedAarsakTilSykdom)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.HovedAarsakTilSykdom)
            }.orShowIf(paragraf.isOneOf(Paragraf.NedsattInnteksevne)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.NedsattInntektsevne)
            }.orShowIf(paragraf.isOneOf(Paragraf.KombinasjonNedsattInntektsevne)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.KombinasjonNedsattInntektsevne)
            }.orShowIf(paragraf.isOneOf(Paragraf.Ufoeretidspunkt)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.Ufoeretidspunkt)
            }.orShowIf(paragraf.isOneOf(Paragraf.FastsettelseIFU)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.FastsettelseIFU)
            }.orShowIf(paragraf.isOneOf(Paragraf.FastsettelseIEU)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.FastsettelseIEU)
            }.orShowIf(paragraf.isOneOf(Paragraf.FastsettelseUfoeregrad)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.FastsettelseUfoeregrad)
            }.orShowIf(paragraf.isOneOf(Paragraf.BeregningAvUfoeretrygd)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.BeregningAvUfoeretrygd)
            }.orShowIf(paragraf.isOneOf(Paragraf.Trygdetid)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.Trygdetid)
            }.orShowIf(paragraf.isOneOf(Paragraf.Ungufoer)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.UngUfoer)
            }.orShowIf(paragraf.isOneOf(Paragraf.AutomatiskInntektsreduksjon)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.AutomatiskInntektsreduksjon)
            }.orShowIf(paragraf.isOneOf(Paragraf.Etteropgjoer)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.Etteroppgjoer)
            }.orShowIf(paragraf.isOneOf(Paragraf.EtteroppgjoerBarnetillegg)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.EtteroppgjoerBarnetillegg)
            }.orShowIf(paragraf.isOneOf(Paragraf.Barnetillegg)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.Barnetillegg)
            }.orShowIf(paragraf.isOneOf(Paragraf.ReduksjonAvBarnetillegg)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.ReduksjonAvBarnetillegg)
            }.orShowIf(paragraf.isOneOf(Paragraf.Yrkesskade)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.Yrkesskade(avslag))
            }.orShowIf(paragraf.isOneOf(Paragraf.Oppholdsinstitusjon)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.OppholdIinstitusjon)
            }.orShowIf(paragraf.isOneOf(Paragraf.Straffegjennomfoering)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.Straffegjennomfoering)
            }.orShowIf(paragraf.isOneOf(Paragraf.Virkningstidspunkt)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.Virkningstidspunkt)
            }.orShowIf(paragraf.isOneOf(Paragraf.Tilbakekreving)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.Tilbakekreving)
            }

            paragraph { text(bokmal { +"Vedtaket opprettholdes og klagen oversendes til $NAV_KLAGEINSTANS for videre behandling." }) }
            paragraph { text(bokmal { +"Klagen har ikke ført til at vedtak blir endret." }) }
        }
    }
        enum class Paragraf(override val displayText: String, override val rekkefoelge: Int) : SaksbehandlerValgEnum {
            Oversittetklagefrist("Fvl. § 31 Oversittet klagefrist", 1),
            Medlemskap("§ 12-2 Medlemskap", 2),
            HensiktsmessigBehandlingOgTiltak("§ 12-5 Hensiktsmessig behandling og tiltak", 3),
            KunArbeidsrettedeTiltak("§ 12-5 Kun arbeidsrettede tiltak", 4),
            HovedAarsakTilSykdom("§ 12-6 Hovedårsak til sykdom", 5),
            NedsattInnteksevne("§ 12-7 Nedsatt innteksevne", 6),
            KombinasjonNedsattInntektsevne("§ 12-7 Kombinasjon nedsatt inntektsevne", 7),
            Ufoeretidspunkt("§ 12-8 Uføretidspunkt", 8),
            FastsettelseIFU("§ 12-9 Fastsettelse av Inntekt Før Uførhet IFU", 9),
            FastsettelseIEU("§ 12-9 Fastsettelse av Inntekt Etter Uførhet IEU", 10),
            FastsettelseUfoeregrad("§ 12-10 Fastsettelse av Uføregrad", 11),
            BeregningAvUfoeretrygd("§ 12-11 Beregning av uføretrygd", 12),
            Trygdetid("§ 12-12 Trygdetid", 13),
            Ungufoer("§ 12-13 Ung ufør", 14),
            AutomatiskInntektsreduksjon("§ 12-14 Reduksjon på grunn av inntekt - automatisk", 15),
            Etteropgjoer("§ 12-14 Reduksjon på grunn av inntekt - etteropgjør", 16),
            EtteroppgjoerBarnetillegg("§ 12-14 Reduksjon på grunn av inntekt - etteroppgjør barnetillegg", 17),
            Barnetillegg("§ 12-15 Barnetillegg", 18),
            ReduksjonAvBarnetillegg("§ 12-16 Reduksjon av barnetillegg", 19),
            Yrkesskade("§ 12-17 Yrkesskade", 20),
            Oppholdsinstitusjon("§ 12-19 Opphold i institusjon", 21),
            Straffegjennomfoering("§ 12-20 Straffegjennomføring", 22),
            Virkningstidspunkt("§ 22-12/22-13 Virkningstidspunkt", 23),
            Tilbakekreving("§ 22-15 Tilbakekreving", 24),
            EosArtikkel57("EØS-trygdeforordningen artikkel 57 Trygdetid eller botid under ett år", 25),
    }
}





