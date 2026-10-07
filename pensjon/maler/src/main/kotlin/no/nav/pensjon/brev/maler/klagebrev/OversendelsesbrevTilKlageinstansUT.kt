package no.nav.pensjon.brev.maler.klagebrev

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Constants.KLAGE_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.KONTAKT_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KLAGEINSTANS
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KONTAKTSENTER_AAPNINGSTID
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KONTAKTSENTER_TELEFON
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
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
        val oversittetklagefrist = saksbehandlervalg("oversittetklagefrist", "Fvl. § 31 Oversittet klagefrist").bool()
        val eoesArtikkel57 = saksbehandlervalg("eosArtikkel57", "Trygdeforordningen EØS-artikkel 57 Trygdetid, eller botid under ett år").bool()
        val medlemskap = saksbehandlervalg("medlemskap", "§ 12-2 Medlemskap").bool()
        val hensiktsmessigBehandlingOgTiltak = saksbehandlervalg("hensiktsmessigBehandlingOgTiltak", "§ 12-5 Hensiktsmessig behandling og tiltak").bool()
        val kunArbeidsrettedeTiltak = saksbehandlervalg("kunArbeidsrettedeTiltak", "§ 12-5 Kun arbeidsrettede tiltak").bool()
        val hovedAarsakTilSykdom = saksbehandlervalg("hovedAarsakTilSykdom", "§ 12-6 Hovedårsak til sykdom").bool()
        val kombinasjonNedsattInntektsevne = saksbehandlervalg("kombinasjonNedsattInntektsevne", "§ 12-7 Kombinasjon nedsatt inntektsevne").bool()
        val nedsattInnteksevne = saksbehandlervalg("nedsattInnteksevne", "§ 12-7 Nedsatt innteksevne").bool()
        val ufoeretidspunkt = saksbehandlervalg("ufoeretidspunkt", "§ 12-8 Uføretidspunkt").bool()
        val fastsettelseIEU = saksbehandlervalg("fastsettelseIEU", "§ 12-9 Fastsettelse av Inntekt Etter Uførhet IEU").bool()
        val fastsettelseIFU = saksbehandlervalg("fastsettelseIFU", "§ 12-9 Fastsettelse av Inntekt Før Uførhet IFU").bool()
        val fastsettelseUfoeregrad = saksbehandlervalg("fastsettelseUfoeregrad", "§ 12-10 Fastsettelse av Uføregrad").bool()
        val beregningAvUfoeretrygd = saksbehandlervalg("beregningAvUfoeretrygd", "§ 12-11 Beregning av uføretrygd").bool()
        val trygdetid = saksbehandlervalg("trygdetid", "§ 12-12 Trygdetid").bool()
        val ungufoer = saksbehandlervalg("ungufoer", "§ 12-13 Ung ufør").bool()
        val automatiskInntektsreduksjon = saksbehandlervalg("automatiskInntektsreduksjon", "§ 12-14 Reduksjon på grunn av inntekt - automatisk").bool()
        val etteropgjoer = saksbehandlervalg("etteropgjoer", "§ 12-14 Reduksjon på grunn av inntekt - etteropgjør").bool()
        val etteroppgjoerBarnetillegg = saksbehandlervalg("etteroppgjoerBarnetillegg", "§ 12-14 Reduksjon på grunn av inntekt - etteroppgjør barnetillegg").bool()
        val barnetillegg = saksbehandlervalg("barnetillegg", "§ 12-15 Barnetillegg").bool()
        val reduksjonAvBarnetillegg = saksbehandlervalg("reduksjonAvBarnetillegg", "§ 12-16 Reduksjon av barnetillegg").bool()
        val yrkesskade = saksbehandlervalg("yrkesskade", "§ 12-17 Yrkesskade").bool()
        val oppholdsinstitusjon = saksbehandlervalg("oppholdsinstitusjon", "§ 12-19 Opphold i institusjon").bool()
        val straffegjennomfoering = saksbehandlervalg("straffegjennomfoering", "§ 12-20 Straffegjennomføring").bool()
        val virkningstidspunkt = saksbehandlervalg("virkningstidspunkt", "§ 22-12/22-13 Virkningstidspunkt").bool()
        val tilbakekreving = saksbehandlervalg("tilbakekreving", "§ 22-15 Tilbakekreving").bool()

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
                    +"Kopi av innstillingen vår er vedlagt."
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
            paragraph { text(bokmal { +"Har du spørsmål? Du finner mer informasjon på $NAV_URL. " }) }
            paragraph { text(bokmal { +"På $KONTAKT_URL kan du chatte eller skrive til oss." }) }
            paragraph { text(bokmal { +"Hvis du ikke finner svar på $NAV_URL, kan du ringe oss på telefon $NAV_KONTAKTSENTER_TELEFON, hverdager $NAV_KONTAKTSENTER_AAPNINGSTID." }) }

            title1 { text(bokmal { +"Innstillingen til $NAV_KLAGEINSTANS - uføretrygd" }) }

            title1 { text(bokmal { +"Hva klagesaken gjelder" }) }

            showIf(oversittetklagefrist.equalTo(true)) {
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

            paragraph{ text(bokmal { +"Vedtaket opprettholdes og klagen oversendes til $NAV_KLAGEINSTANS for videre behandling." }) }
            paragraph{ text(bokmal { +"Klagen har ikke ført til at vedtak blir endret." }) }
        }
    }
}





