package no.nav.pensjon.brev.maler.klagebrev

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.OversendelsesbrevTilKlageinstansUTDto
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Felles.fulltNavn
import no.nav.pensjon.brev.maler.klagebrev.tekstNAY.OversendelsesbrevTilKlageinstansTekst
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.ITALIC
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.saksbehandlervalg
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.selectors.brevbakerFelles.bruker
import no.nav.pensjon.brevbaker.api.model.selectors.brevbakerFelles.bruker.foedselsnummer

@TemplateModelHelpers
object OversendelsesbrevTilKlageinstansUT : RedigerbarTemplate<OversendelsesbrevTilKlageinstansUTDto> {

    override val featureToggle = FeatureToggles.brevmalKlageOversendelsesbrevTilKlageinstansUT.toggle

    override val kode = Pesysbrevkoder.Redigerbar.UT_KLAGE_OVERSENDELSESBREV_TIL_KLAGEINSTANS
    override val kategori = Brevkategori.KLAGE_OG_ANKE
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Language.Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Klage - oversendelsesbrev til Nav klageinstans",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        val generisk = saksbehandlervalg("generisk", "Gererisk tekst").bool()
        val medlemskap = saksbehandlervalg("medlemskap", "§12-2 Medlemskap").bool()
        val hensiktsmessigBehandlingOgTiltak = saksbehandlervalg("hensiktsmessigBehandlingOgTiltak", "§ 12-5 Hensiktsmessig behandling og tiltak").bool()
        val kunArbeidsrettedeTiltak = saksbehandlervalg("kunArbeidsrettedeTiltak", "§ 12-5 Kun arbeidsrettede tiltak").bool()
        val hovedAarsakTilSykdom = saksbehandlervalg("hovedAarsakTilSykdom", "§ 12-6 Hovedårsak til sykdom").bool()
        val nedsattInnteksevne = saksbehandlervalg("nedsattInnteksevne", "§ 12-7 Nedsatt innteksevne").bool()
        val kombinasjonNedsattInntektsevne = saksbehandlervalg("kombinasjonNedsattInntektsevne", "§ 12-7 Kombinasjon nedsatt inntektsevne").bool()
        val ufoeretidspunkt = saksbehandlervalg("ufoeretidspunkt", "§ 12-8 Uføretidspunkt").bool()
        val fastsettelseIFU = saksbehandlervalg("fastsettelseIFU", "§ 12-9 Fastsettelse av Inntekt Før Uførhet IFU").bool()
        val fastsettelseIEU = saksbehandlervalg("fastsettelseIEU ", "§ 12-9 Fastsettelse av Inntekt Etter Uførehet IEU").bool()
        val fastsettelseUfoeregrad = saksbehandlervalg("fastsettelseUfoeregrad", "§ 12-10 Fastsettelse av Uføregrad").bool()
        val beregningAvUfoeretrygd = saksbehandlervalg("beregningAvUfoeretrygd", "§ 12-11 Beregning av uføretrygd").bool()
        val trygdetid = saksbehandlervalg("trygdetid", "§ 12-12 Trygdetid").bool()
        val ungufoer = saksbehandlervalg("ungufoer", "§ 12-13 Ung ufør").bool()
        val automatiskInntektsreduksjon = saksbehandlervalg("automatiskInntektsreduksjon", "§ 12-14 Reduksjon på grunn av inntekt - automatisk").bool()
        val etteropgjoer = saksbehandlervalg("etteroppgjoer", "§ 12-14 Reduksjon på grunn av inntekt - etteropgjør").bool()
        val etteroppgjoerBarnetillegg = saksbehandlervalg("etteroppgjoerBarnetillegg", "§ 12-14 Reduksjon på grunn av inntekt - etteroppgjør barnetillegg").bool()
        val barnetillegg = saksbehandlervalg("barnetillegg", "§ 12-15 Barnetillegg").bool()
        val reduksjonAvBarnetillegg = saksbehandlervalg("reduksjonAvBarnetillegg", "§ 12-16 Reduksjon av barnetillegg").bool()
        val yrkesskade = saksbehandlervalg("yrkesskade", "§ 12-17 Yrkesskade").bool()
        val oppholdsinstitusjon = saksbehandlervalg("oppholdsinstitusjon", "§ 12-19 Opphold i institusjon").bool()
        val straffegjennomfoering = saksbehandlervalg("straffegjennomfoering", "§ 12-20 Straffegjennomføring").bool()
        val virkningstidspunkt = saksbehandlervalg("virkningstidspunkt", "§ 22-12/22-13 Virkningstidspunkt").bool()
        val tilbakekreving = saksbehandlervalg("tilbakekreving", "§ 22-15 Tilbakekreving").bool()
        val eosArtikkel57 = saksbehandlervalg("eosArtikkel57", "EØS-trygdeforordningen artikkel 57 Trygdetid eller botid under ett år").bool()
        val oversittetklagefrist = saksbehandlervalg("oversittetklagefrist", "Fvl. § 31 Oversittet klagefrist").bool()


        title { text(bokmal { +"Klage - Uføretrygd - Innstillingen til Nav Klageinstans" }) }

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
                    +"Klagesaken er derfor oversendt til Nav klageinstans for behandling. "
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
            paragraph { text(bokmal { +"Du får melding fra Nav klageinstans når de har mottatt saken." }) }
            paragraph {
                text(bokmal {
                    +"Du finner oversikt over saksbehandlingstidene på nav.no/saksbehandlingstider. "
                    +"Du får beskjed fra Nav klageinstans, dersom de trenger mer tid."
                })
            }
            paragraph {
                text(bokmal {
                    +"Du kan sende merknader og dokumentasjon til Nav klageinstans. "
                    +"Du kan logge deg inn på nav.no/kontakt og sende skriftlig melding der. "
                    +"Hvis du ønsker å ettersende dokumentasjon, kan du gå til nav.no/klage og trykke på 'Ettersend dokumentasjon' for det saken gjelder."
                })
            }
            paragraph { text(bokmal { +"Har du spørsmål? Du finner mer informasjon på nav.no. " }) }
            paragraph { text(bokmal { +"På nav.no/kontakt kan du chatte eller skrive til oss." }) }
            paragraph { text(bokmal { +"Hvis du ikke finner svar på nav.no, kan du ringe oss på telefon 55 55 33 33, hverdager 09.00-15.00." }) }

            title1 { text(bokmal { +"Innstillingen til Nav klageinstans - uføretrygd" }) }

            title2 { text(bokmal { +"Hva klagesaken gjelder" }) }

            showIf(generisk) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.Generisk)
            }.orShowIf(medlemskap) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.Medlemskap)
            }.orShowIf(hensiktsmessigBehandlingOgTiltak) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.HensiktsmessigBehandlingOgTiltak)
            }.orShowIf(kunArbeidsrettedeTiltak) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.KunArbeidsrettedeTiltak)
            }.orShowIf(hovedAarsakTilSykdom) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.HovedAarsakTilSykdom)
            }.orShowIf(nedsattInnteksevne) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.NedsattInntektsevne)
            }.orShowIf(kombinasjonNedsattInntektsevne) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.KombinasjonNedsattInntektsevne)
            }.orShowIf(ufoeretidspunkt) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.Ufoeretidspunkt)
            }.orShowIf(fastsettelseIFU) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.FastsettelseIFU)
            }.orShowIf(fastsettelseIEU) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.FastsettelseIEU)
            }.orShowIf(fastsettelseUfoeregrad) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.FastsettelseUfoeregrad)
            }.orShowIf(beregningAvUfoeretrygd) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.BeregningAvUfoeretrygd)
            }.orShowIf(trygdetid) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.Trygdetid)
            }.orShowIf(ungufoer) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.UngUfoer)
            }.orShowIf(automatiskInntektsreduksjon) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.AutomatiskInntektsreduksjon)
            }.orShowIf(etteropgjoer) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.Etteroppgjoer)
            }.orShowIf(etteroppgjoerBarnetillegg) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.EtteroppgjoerBarnetillegg)
            }.orShowIf(barnetillegg) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.EtteroppgjoerBarnetillegg)
            }.orShowIf(reduksjonAvBarnetillegg) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.ReduksjonAvBarnetillegg)
            }.orShowIf(yrkesskade) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.Yrkesskade)
            }.orShowIf(oppholdsinstitusjon) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.OppholdIinstitusjon)
            }.orShowIf(straffegjennomfoering) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.Straffegjennomfoering)
            }.orShowIf(virkningstidspunkt) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.Virkningstidspunkt)
            }.orShowIf(tilbakekreving) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.Tilbakekreving)
            }.orShowIf(eosArtikkel57) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.EoesArtikkel57)
            }.orShowIf(oversittetklagefrist) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.OversittetKlagefrist)
            }

            showIf(not(hovedAarsakTilSykdom) and not(eosArtikkel57)) {
                paragraph { text(bokmal { +"<Klipp inn fra vedtak eller vilkårsvurdering og svar ut anførslene konkret>" }, ITALIC) }
            }

            paragraph { text(bokmal { +"Vedtaket opprettholdes og klagen oversendes til Nav klageinstans for videre behandling." }) }
            paragraph { text(bokmal { +"Klagen har ikke ført til at vedtak blir endret." }) }
        }
    }
}


