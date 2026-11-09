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
import no.nav.pensjon.brev.maler.klagebrev.tekstNAY.OversendelsesbrevTilKlageinstansTekst.Barnetillegg
import no.nav.pensjon.brev.maler.klagebrev.tekstNAY.OversendelsesbrevTilKlageinstansTekst.BeregningAvUfoeretrygd
import no.nav.pensjon.brev.maler.klagebrev.tekstNAY.OversendelsesbrevTilKlageinstansTekst.EoesArtikkel57
import no.nav.pensjon.brev.maler.klagebrev.tekstNAY.OversendelsesbrevTilKlageinstansTekst.FastsettelseIFUIEU
import no.nav.pensjon.brev.maler.klagebrev.tekstNAY.OversendelsesbrevTilKlageinstansTekst.FastsettelseUfoeregrad
import no.nav.pensjon.brev.maler.klagebrev.tekstNAY.OversendelsesbrevTilKlageinstansTekst.HensiktsmessigBehandlingOgTiltak
import no.nav.pensjon.brev.maler.klagebrev.tekstNAY.OversendelsesbrevTilKlageinstansTekst.HovedAarsakTilSykdom
import no.nav.pensjon.brev.maler.klagebrev.tekstNAY.OversendelsesbrevTilKlageinstansTekst.KombinasjonNedsattInntektsevne
import no.nav.pensjon.brev.maler.klagebrev.tekstNAY.OversendelsesbrevTilKlageinstansTekst.KunArbeidsrettedeTiltak
import no.nav.pensjon.brev.maler.klagebrev.tekstNAY.OversendelsesbrevTilKlageinstansTekst.Medlemskap
import no.nav.pensjon.brev.maler.klagebrev.tekstNAY.OversendelsesbrevTilKlageinstansTekst.NedsattInntektsevne
import no.nav.pensjon.brev.maler.klagebrev.tekstNAY.OversendelsesbrevTilKlageinstansTekst.OppholdIinstitusjon
import no.nav.pensjon.brev.maler.klagebrev.tekstNAY.OversendelsesbrevTilKlageinstansTekst.OversittetKlagefrist
import no.nav.pensjon.brev.maler.klagebrev.tekstNAY.OversendelsesbrevTilKlageinstansTekst.ReduksjonAvBarnetillegg
import no.nav.pensjon.brev.maler.klagebrev.tekstNAY.OversendelsesbrevTilKlageinstansTekst.ReduksjonPaaGrunnAvInntekt
import no.nav.pensjon.brev.maler.klagebrev.tekstNAY.OversendelsesbrevTilKlageinstansTekst.ReduksjonPaaGrunnAvInntekt.ReduksjonPaaGrunnAvInntektValg
import no.nav.pensjon.brev.maler.klagebrev.tekstNAY.OversendelsesbrevTilKlageinstansTekst.Straffegjennomfoering
import no.nav.pensjon.brev.maler.klagebrev.tekstNAY.OversendelsesbrevTilKlageinstansTekst.Tilbakekreving
import no.nav.pensjon.brev.maler.klagebrev.tekstNAY.OversendelsesbrevTilKlageinstansTekst.Trygdetid
import no.nav.pensjon.brev.maler.klagebrev.tekstNAY.OversendelsesbrevTilKlageinstansTekst.Ufoeretidspunkt
import no.nav.pensjon.brev.maler.klagebrev.tekstNAY.OversendelsesbrevTilKlageinstansTekst.UngUfoer
import no.nav.pensjon.brev.maler.klagebrev.tekstNAY.OversendelsesbrevTilKlageinstansTekst.Virkningstidspunkt
import no.nav.pensjon.brev.maler.klagebrev.tekstNAY.OversendelsesbrevTilKlageinstansTekst.Yrkesskade
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

        val avslag = saksbehandlervalg("avslag", "Hvis § 12-17 Yrkesskade, velg:").enum<Avslag>()
        val fastsettelseInntekt = saksbehandlervalg("fastsettelseInntekt", "Velg om inntekt er fastsatt før eller etter uførhet:").enum<FastsettelseIFUIEU.FastsettelseInntektValg>()
        val inntekt = saksbehandlervalg("inntekt", "Velg begrunnelsen for inntektsreduksjon:").enum<ReduksjonPaaGrunnAvInntektValg>()
        val paragraf = saksbehandlervalg("paragraf", "Velg paragraf:").enum<Paragraf>()
        val ufoertidspunkt = saksbehandlervalg("ufoertidspunkt", "Velg uføretidspunkt:").enum<UngUfoer.Ufoertidspunkt>()


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
                includePhrase(OversittetKlagefrist)
            }.orShowIf(paragraf.isOneOf(Paragraf.EosArtikkel57)) {
                includePhrase(EoesArtikkel57)
            }.orShowIf(paragraf.isOneOf(Paragraf.Medlemskap)) {
                includePhrase(Medlemskap)
            }.orShowIf(paragraf.isOneOf(Paragraf.HensiktsmessigBehandlingOgTiltak)) {
                includePhrase(HensiktsmessigBehandlingOgTiltak)
            }.orShowIf(paragraf.isOneOf(Paragraf.KunArbeidsrettedeTiltak)) {
                includePhrase(KunArbeidsrettedeTiltak)
            }.orShowIf(paragraf.isOneOf(Paragraf.HovedAarsakTilSykdom)) {
                includePhrase(HovedAarsakTilSykdom)
            }.orShowIf(paragraf.isOneOf(Paragraf.NedsattInnteksevne)) {
                includePhrase(NedsattInntektsevne)
            }.orShowIf(paragraf.isOneOf(Paragraf.KombinasjonNedsattInntektsevne)) {
                includePhrase(KombinasjonNedsattInntektsevne)
            }.orShowIf(paragraf.isOneOf(Paragraf.Ufoeretidspunkt)) {
                includePhrase(Ufoeretidspunkt)
            }.orShowIf(paragraf.isOneOf(Paragraf.FastsettelseAvInntekt)) {
                includePhrase(FastsettelseIFUIEU(fastsettelseInntekt))
            }.orShowIf(paragraf.isOneOf(Paragraf.FastsettelseUfoeregrad)) {
                includePhrase(FastsettelseUfoeregrad)
            }.orShowIf(paragraf.isOneOf(Paragraf.BeregningAvUfoeretrygd)) {
                includePhrase(BeregningAvUfoeretrygd)
            }.orShowIf(paragraf.isOneOf(Paragraf.Trygdetid)) {
                includePhrase(Trygdetid)
            }.orShowIf(paragraf.isOneOf(Paragraf.Ungufoer)) {
                includePhrase(UngUfoer(ufoertidspunkt))
            }.orShowIf(paragraf.isOneOf(Paragraf.ReduksjonPaaGrunnAvInntekt)) {
                includePhrase(ReduksjonPaaGrunnAvInntekt(inntekt))
            }.orShowIf(paragraf.isOneOf(Paragraf.Barnetillegg)) {
                includePhrase(Barnetillegg)
            }.orShowIf(paragraf.isOneOf(Paragraf.ReduksjonAvBarnetillegg)) {
                includePhrase(ReduksjonAvBarnetillegg)
            }.orShowIf(paragraf.isOneOf(Paragraf.Yrkesskade)) {
                includePhrase(Yrkesskade(avslag))
            }.orShowIf(paragraf.isOneOf(Paragraf.Oppholdsinstitusjon)) {
                includePhrase(OppholdIinstitusjon)
            }.orShowIf(paragraf.isOneOf(Paragraf.Straffegjennomfoering)) {
                includePhrase(Straffegjennomfoering)
            }.orShowIf(paragraf.isOneOf(Paragraf.Virkningstidspunkt)) {
                includePhrase(Virkningstidspunkt)
            }.orShowIf(paragraf.isOneOf(Paragraf.Tilbakekreving)) {
                includePhrase(Tilbakekreving)
            }

            paragraph { text(bokmal { +"Klagen har ikke ført til at vedtak blir endret." }) }
            paragraph { text(bokmal { +"Vedtaket opprettholdes og klagen oversendes til $NAV_KLAGEINSTANS for videre behandling." }) }
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
            FastsettelseAvInntekt("§ 12-9 Fastsettelse av Inntekt", 9),
            FastsettelseUfoeregrad("§ 12-10 Fastsettelse av Uføregrad", 10),
            BeregningAvUfoeretrygd("§ 12-11 Beregning av uføretrygd", 11),
            Trygdetid("§ 12-12 Trygdetid", 12),
            Ungufoer("§ 12-13 Ung ufør", 13),
            ReduksjonPaaGrunnAvInntekt("§ 12-14 Reduksjon på grunn av inntekt", 14),
            Barnetillegg("§ 12-15 Barnetillegg", 15),
            ReduksjonAvBarnetillegg("§ 12-16 Reduksjon av barnetillegg", 16),
            Yrkesskade("§ 12-17 Yrkesskade", 17),
            Oppholdsinstitusjon("§ 12-19 Opphold i institusjon", 18),
            Straffegjennomfoering("§ 12-20 Straffegjennomføring", 19),
            Virkningstidspunkt("§ 22-12/22-13 Virkningstidspunkt", 20),
            Tilbakekreving("§ 22-15 Tilbakekreving", 21),
            EosArtikkel57("EØS-trygdeforordningen artikkel 57 Trygdetid eller botid under ett år", 22),
    }
}





