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
import no.nav.pensjon.brev.maler.klagebrev.tekstNAY.OversendelsesbrevTilKlageinstansTekst
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
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
z
            showIf(Paragraf.Oversittetklagefrist.equalTo(true)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.OversittetKlagefrist)
            }.orShowIf(eoesArtikkel57.equalTo(true)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.EoesArtikkel57)
            }.orShowIf(medlemskap.equalTo(true)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.Medlemskap)
            }.orShowIf(hensiktsmessigBehandlingOgTiltak.equalTo(true)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.HensiktsmessigBehandlingOgTiltak)
            }.orShowIf(kunArbeidsrettedeTiltak.equalTo(true)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.KunArbeidsrettedeTiltak)
            }.orShowIf(hovedAarsakTilSykdom.equalTo(true)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.HovedAarsakTilSykdom)
            }.orShowIf(nedsattInnteksevne.equalTo(true)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.NedsattInntektsevne)
            }.orShowIf(kombinasjonNedsattInntektsevne.equalTo(true)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.KombinasjonNedsattInntektsevne)
            }.orShowIf(ufoeretidspunkt.equalTo(true)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.Ufoeretidspunkt)
            }.orShowIf(fastsettelseIFU.equalTo(true)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.FastsettelseIFU)
            }.orShowIf(fastsettelseIEU.equalTo(true)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.FastsettelseIEU)
            }.orShowIf(fastsettelseUfoeregrad.equalTo(true)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.FastsettelseUfoeregrad)
            }.orShowIf(beregningAvUfoeretrygd.equalTo(true)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.BeregningAvUfoeretrygd)
            }.orShowIf(trygdetid.equalTo(true)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.Trygdetid)
            }.orShowIf(ungufoer.equalTo(true)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.UngUfoer)
            }.orShowIf(automatiskInntektsreduksjon.equalTo(true)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.AutomatiskInntektsreduksjon)
            }.orShowIf(etteropgjoer.equalTo(true)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.Etteroppgjoer)
            }.orShowIf(etteroppgjoerBarnetillegg.equalTo(true)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.EtteroppgjoerBarnetillegg)
            }.orShowIf(barnetillegg.equalTo(true)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.Barnetillegg)
            }.orShowIf(reduksjonAvBarnetillegg.equalTo(true)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.ReduksjonAvBarnetillegg)
            }.orShowIf(yrkesskade.equalTo(true)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.Yrkesskade)
            }.orShowIf(oppholdsinstitusjon.equalTo(true)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.OppholdIinstitusjon)
            }.orShowIf(straffegjennomfoering.equalTo(true)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.Straffegjennomfoering)
            }.orShowIf(virkningstidspunkt.equalTo(true)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.Virkningstidspunkt)
            }.orShowIf(tilbakekreving.equalTo(true)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.Tilbakekreving)
            }

            paragraph { text(bokmal { +"Vedtaket opprettholdes og klagen oversendes til $NAV_KLAGEINSTANS for videre behandling." }) }
            paragraph { text(bokmal { +"Klagen har ikke ført til at vedtak blir endret." }) }
        }
    }
        enum class Paragraf(override val displayText: String) : SaksbehandlerValgEnum {
            Medlemskap("§ 12-2 Medlemskap"),
            HensiktsmessigBehandlingOgTiltak("§ 12-5 Hensiktsmessig behandling og tiltak"),
            KunArbeidsrettedeTiltak("§ 12-5 Kun arbeidsrettede tiltak"),
            HovedAarsakTilSykdom("§ 12-6 Hovedårsak til sykdom"),
            NedsattInnteksevne("§ 12-7 Nedsatt innteksevne"),
            KombinasjonNedsattInntektsevne("§ 12-7 Kombinasjon nedsatt inntektsevne"),
            Ufoeretidspunkt("§ 12-8 Uføretidspunkt"),
            FastsettelseIFU("§ 12-9 Fastsettelse av Inntekt Før Uførhet IFU"),
            FastsettelseIEU("§ 12-9 Fastsettelse av Inntekt Etter Uførhet IEU"),
            FastsettelseUfoeregrad("§ 12-10 Fastsettelse av Uføregrad"),
            BeregningAvUfoeretrygd("§ 12-11 Beregning av uføretrygd"),
            Trygdetid("§ 12-12 Trygdetid"),
            Ungufoer("§ 12-13 Ung ufør"),
            AutomatiskInntektsreduksjon("§ 12-14 Reduksjon på grunn av inntekt - automatisk"),
            Etteropgjoer("§ 12-14 Reduksjon på grunn av inntekt - etteropgjør"),
            EtteroppgjoerBarnetillegg("§ 12-14 Reduksjon på grunn av inntekt - etteroppgjør barnetillegg"),
            Barnetillegg("§ 12-15 Barnetillegg"),
            ReduksjonAvBarnetillegg("§ 12-16 Reduksjon av barnetillegg"),
            Yrkesskade("§ 12-17 Yrkesskade"),
            Oppholdsinstitusjon("§ 12-19 Opphold i institusjon"),
            Straffegjennomfoering("§ 12-20 Straffegjennomføring"),
            Virkningstidspunkt("§ 22-12/22-13 Virkningstidspunkt"),
            Tilbakekreving("§ 22-15 Tilbakekreving"),
            EosArtikkel57("EØS-trygdeforordningen artikkel 57 Trygdetid eller botid under ett år"),
            Oversittetklagefrist("Fvl. § 31 Oversittet klagefrist"),
    }
}





