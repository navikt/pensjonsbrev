package no.nav.pensjon.brev.maler.klagebrev

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Felles.fulltNavn
import no.nav.pensjon.brev.maler.klagebrev.OversendelsesbrevTilKlageinstansUT.Forskriften.AutomatiskInntektsreduksjon
import no.nav.pensjon.brev.maler.klagebrev.OversendelsesbrevTilKlageinstansUT.Forskriften.Barnetillegg
import no.nav.pensjon.brev.maler.klagebrev.OversendelsesbrevTilKlageinstansUT.Forskriften.BeregningAvUfoeretrygd
import no.nav.pensjon.brev.maler.klagebrev.OversendelsesbrevTilKlageinstansUT.Forskriften.EosArtikkel57
import no.nav.pensjon.brev.maler.klagebrev.OversendelsesbrevTilKlageinstansUT.Forskriften.Etteropgjoer
import no.nav.pensjon.brev.maler.klagebrev.OversendelsesbrevTilKlageinstansUT.Forskriften.EtteroppgjoerBarnetillegg
import no.nav.pensjon.brev.maler.klagebrev.OversendelsesbrevTilKlageinstansUT.Forskriften.FastsettelseIEU
import no.nav.pensjon.brev.maler.klagebrev.OversendelsesbrevTilKlageinstansUT.Forskriften.FastsettelseIFU
import no.nav.pensjon.brev.maler.klagebrev.OversendelsesbrevTilKlageinstansUT.Forskriften.FastsettelseUfoeregrad
import no.nav.pensjon.brev.maler.klagebrev.OversendelsesbrevTilKlageinstansUT.Forskriften.Generell
import no.nav.pensjon.brev.maler.klagebrev.OversendelsesbrevTilKlageinstansUT.Forskriften.HensiktsmessigBehandlingOgTiltak
import no.nav.pensjon.brev.maler.klagebrev.OversendelsesbrevTilKlageinstansUT.Forskriften.HovedAarsakTilSykdom
import no.nav.pensjon.brev.maler.klagebrev.OversendelsesbrevTilKlageinstansUT.Forskriften.KombinasjonNedsattInntektsevne
import no.nav.pensjon.brev.maler.klagebrev.OversendelsesbrevTilKlageinstansUT.Forskriften.KunArbeidsrettedeTiltak
import no.nav.pensjon.brev.maler.klagebrev.OversendelsesbrevTilKlageinstansUT.Forskriften.Medlemskap
import no.nav.pensjon.brev.maler.klagebrev.OversendelsesbrevTilKlageinstansUT.Forskriften.NedsattInnteksevne
import no.nav.pensjon.brev.maler.klagebrev.OversendelsesbrevTilKlageinstansUT.Forskriften.Oppholdsinstitusjon
import no.nav.pensjon.brev.maler.klagebrev.OversendelsesbrevTilKlageinstansUT.Forskriften.Oversittetklagefrist
import no.nav.pensjon.brev.maler.klagebrev.OversendelsesbrevTilKlageinstansUT.Forskriften.ReduksjonAvBarnetillegg
import no.nav.pensjon.brev.maler.klagebrev.OversendelsesbrevTilKlageinstansUT.Forskriften.Straffegjennomfoering
import no.nav.pensjon.brev.maler.klagebrev.OversendelsesbrevTilKlageinstansUT.Forskriften.Tilbakekreving
import no.nav.pensjon.brev.maler.klagebrev.OversendelsesbrevTilKlageinstansUT.Forskriften.Trygdetid
import no.nav.pensjon.brev.maler.klagebrev.OversendelsesbrevTilKlageinstansUT.Forskriften.Ufoeretidspunkt
import no.nav.pensjon.brev.maler.klagebrev.OversendelsesbrevTilKlageinstansUT.Forskriften.Ungufoer
import no.nav.pensjon.brev.maler.klagebrev.OversendelsesbrevTilKlageinstansUT.Forskriften.Virkningstidspunkt
import no.nav.pensjon.brev.maler.klagebrev.OversendelsesbrevTilKlageinstansUT.Forskriften.Yrkesskade
import no.nav.pensjon.brev.maler.klagebrev.tekstNAY.OversendelsesbrevTilKlageinstansTekst
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.ITALIC
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.or
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

        val forskrift = saksbehandlervalg("forskrift", "Velg forskriften:").enum<Forskriften>()


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

            showIf(forskrift.isOneOf(Generell)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.Generisk)
            }.orShowIf(forskrift.isOneOf(Medlemskap)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.Medlemskap)
            }.orShowIf(forskrift.isOneOf(HensiktsmessigBehandlingOgTiltak)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.HensiktsmessigBehandlingOgTiltak)
            }.orShowIf(forskrift.isOneOf(KunArbeidsrettedeTiltak)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.KunArbeidsrettedeTiltak)
            }.orShowIf(forskrift.isOneOf(HovedAarsakTilSykdom)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.HovedAarsakTilSykdom)
            }.orShowIf(forskrift.isOneOf(NedsattInnteksevne)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.NedsattInntektsevne)
            }.orShowIf(forskrift.isOneOf(KombinasjonNedsattInntektsevne)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.KombinasjonNedsattInntektsevne)
            }.orShowIf(forskrift.isOneOf(Ufoeretidspunkt)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.Ufoeretidspunkt)
            }.orShowIf(forskrift.isOneOf(FastsettelseIFU)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.FastsettelseIFU)
            }.orShowIf(forskrift.isOneOf(FastsettelseIEU)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.FastsettelseIEU)
            }.orShowIf(forskrift.isOneOf(FastsettelseUfoeregrad)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.FastsettelseUfoeregrad)
            }.orShowIf(forskrift.isOneOf(BeregningAvUfoeretrygd)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.BeregningAvUfoeretrygd)
            }.orShowIf(forskrift.isOneOf(Trygdetid)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.Trygdetid)
            }.orShowIf(forskrift.isOneOf(Ungufoer)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.UngUfoer)
            }.orShowIf(forskrift.isOneOf(AutomatiskInntektsreduksjon)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.AutomatiskInntektsreduksjon)
            }.orShowIf(forskrift.isOneOf(Etteropgjoer)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.Etteroppgjoer)
            }.orShowIf(forskrift.isOneOf(EtteroppgjoerBarnetillegg)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.EtteroppgjoerBarnetillegg)
            }.orShowIf(forskrift.isOneOf(Barnetillegg)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.Barnetillegg)
            }.orShowIf(forskrift.isOneOf(ReduksjonAvBarnetillegg)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.ReduksjonAvBarnetillegg)
            }.orShowIf(forskrift.isOneOf(Yrkesskade)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.Yrkesskade)
            }.orShowIf(forskrift.isOneOf(Oppholdsinstitusjon)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.OppholdIinstitusjon)
            }.orShowIf(forskrift.isOneOf(Straffegjennomfoering)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.Straffegjennomfoering)
            }.orShowIf(forskrift.isOneOf(Virkningstidspunkt)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.Virkningstidspunkt)
            }.orShowIf(forskrift.isOneOf(Tilbakekreving)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.Tilbakekreving)
            }.orShowIf(forskrift.isOneOf(EosArtikkel57)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.EoesArtikkel57)
            }.orShowIf(forskrift.isOneOf(Oversittetklagefrist)) {
                includePhrase(OversendelsesbrevTilKlageinstansTekst.OversittetKlagefrist)
            }

            showIf(forskrift.notEqualTo(HovedAarsakTilSykdom) or forskrift.notEqualTo(EosArtikkel57)) {
                paragraph { text(bokmal { +"<Klipp inn fra vedtak eller vilkårsvurdering og svar ut anførslene konkret>" }, ITALIC) }
            }

            paragraph{ text(bokmal { +"Vedtaket opprettholdes og klagen oversendes til Nav klageinstans for videre behandling." }) }
            paragraph{ text(bokmal { +"Klagen har ikke ført til at vedtak blir endret." }) }
        }
    }

    enum class Forskriften(override val displayText: String) : SaksbehandlerValgEnum {
        Medlemskap("§ 12-2 Medlemskap"),
        HensiktsmessigBehandlingOgTiltak("§ 12-5 Hensiktsmessig behandling og tiltak"),
        KunArbeidsrettedeTiltak("§ 12-5 Kun arbeidsrettede tiltak"),
        HovedAarsakTilSykdom("§ 12-6 Hovedårsak til sykdom"),
        NedsattInnteksevne("§ 12-7 Nedsatt innteksevne"),
        KombinasjonNedsattInntektsevne("§ 12-7 Kombinasjon nedsatt inntektsevne"),
        Ufoeretidspunkt("§ 12-8 Uføretidspunkt"),
        FastsettelseIFU("§ 12-9 Fastsettelse av Inntekt Før Uførhet IFU"),
        FastsettelseIEU("§ 12-9 Fastsettelse av Inntekt Etter Uførehet IEU"),
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
        Generell("Generell tekst")
    }
}





