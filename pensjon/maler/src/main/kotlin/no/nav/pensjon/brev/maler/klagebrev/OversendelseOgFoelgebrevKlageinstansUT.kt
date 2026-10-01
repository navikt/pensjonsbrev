package no.nav.pensjon.brev.maler.klagebrev

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.klagebrev.OversendelseOgFoelgebrevKlageinstansUT.Forskriften.*
import no.nav.pensjon.brev.maler.klagebrev.tekstNAY.OversendelsesbrevTilKlageinstansTekst
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.saksbehandlervalg
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object OversendelseOgFoelgebrevKlageinstansUT : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val featureToggle = FeatureToggles.brevmalKlageOversendelseOgFoelgebrevKlageinstansUT.toggle

    override val kode = Pesysbrevkoder.Redigerbar.UT_KLAGE_OVERSENDELSE_OG_FOELGEBREV_KLAGEINSTANS
    override val kategori = Brevkategori.KLAGE_OG_ANKE
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Language.Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Klage - oversendelsesbrev til Nav klageinstans (m/følgebrev)",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {

        val forskrift = saksbehandlervalg("forskrift", "Velg forskriften:").enum<Forskriften>()


        title { text(bokmal { +"Oversendelsesbrev til Nav Klageinstans - Uføretrygd" }) }

        outline {

            title1 { text(bokmal { +"Hva klagesaken gjelder" }) }

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

            paragraph { text(bokmal { +"Vedtaket opprettholdes og klagen oversendes til Nav klageinstans for videre behandling." }) }
            paragraph { text(bokmal { +"Klagen har ikke ført til at vedtak blir endret." }) }
        }

        includeAttachment(vedleggFoelgebrevKlageinstansUT)
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





