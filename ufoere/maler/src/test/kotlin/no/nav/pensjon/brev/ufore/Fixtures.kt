package no.nav.pensjon.brev.ufore

import no.nav.brev.brevbaker.*
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.ufore.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.ufore.api.model.maler.info.InfoEndretUTPgaInntektDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.*
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.FeilutbetalingSpesifikkVarselDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.FeilutbetalingVarselDodsboDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.VarselFeilutbetalingPesysData
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate
import java.time.Month
import kotlin.reflect.KClass
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.template.BrevTemplate
import no.nav.pensjon.brev.ufore.maler.feilutbetaling.*
import no.nav.pensjon.brev.ufore.maler.feilutbetaling.varsel.*
import no.nav.pensjon.brev.ufore.maler.hvilenderett.*
import no.nav.pensjon.brev.ufore.maler.info.*
import no.nav.pensjon.brev.ufore.maler.innhentingopplysninger.*
import no.nav.pensjon.brev.ufore.maler.lovendringer2026.*
import no.nav.pensjon.brev.ufore.maler.uforeavslag.*

object Fixtures : LetterDataFactory {

    val felles = FellesFactory.felles

    @Suppress("UNCHECKED_CAST")
    override fun <T : BrevbakerBrevdata> create(templateType: KClass<out BrevTemplate<T, *>>): T =
        when (templateType) {
            InfoEndretUforetrygdPgaInntekt::class -> lagInfoEndretUTPgaInntektDto() as T
            UforeAvslagTestmal::class -> lagUforeAvslagTestmalDto() as T
            UforeAvslagHensiktsmessigBehandling::class -> lagUforeAvslagEnkelDto() as T
            UforegradAvslagManglendeDok::class -> lagUforeAvslagEnkelDto() as T
            UforegradAvslagHensiktsmessigArbTiltakI2::class -> lagUforeAvslagEnkelDto() as T
            UforeAvslagManglendeDok::class -> lagUforeAvslagEnkelDto() as T
            UforeAvslagHensiktsmessigArbTiltakI1::class -> lagUforeAvslagEnkelDto() as T
            UforeAvslagSykdom::class -> lagUforeAvslagEnkelDto() as T
            UforeAvslagUngUforVarig::class -> lagUforeAvslagEnkelDto() as T
            UforegradAvslagHensiktsmessigArbTiltakI1::class -> lagUforeAvslagEnkelDto() as T
            UforeAvslagHensiktsmessigArbTiltakI2::class -> lagUforeAvslagEnkelDto() as T
            UforeAvslagIFUIkkeVarig::class -> lagUforeAvslagEnkelDto() as T
            UforeAvslagIFUOktStilling::class -> lagUforeAvslagEnkelDto() as T
            UforegradAvslagSykdom::class -> lagUforeAvslagEnkelDto() as T
            UforeAvslagUngUfor36::class -> lagUforeAvslagEnkelDto() as T
            UforeAvslagYrkesskadeGodkjent::class -> lagUforeAvslagEnkelDto() as T
            UforeAvslagAlder::class -> lagUforeAvslagEnkelDto() as T
            UforeAvslagUngUfor26::class -> lagUforeAvslagEnkelDto() as T
            UforegradAvslagHensiktsmessigBehandling::class -> lagUforeAvslagEnkelDto() as T
            UforeAvslagMedlemskap::class -> lagUforeAvslagEnkelDto() as T
            UforeAvslagInntektsevne40::class -> lagUforeAvslagInntektDto() as T
            UforeAvslagInntektsevne50::class -> lagUforeAvslagInntektDto() as T
            UforeAvslagInntektsevne30::class -> lagUforeAvslagInntektDto() as T
            UforegradAvslagInntektsevne::class -> lagUforeAvslagInntektDto() as T
            UforeAvslagMedlemskapUtland::class -> lagUforeAvslagUtlandDto() as T
            UforeAvslagMedlemskapMindre12Mnd::class -> lagUforeAvslagDto() as T
            VarselFeilutbetaling::class -> lagVarselFeilutbetalingUforeDto() as T
            VedtakFeilutbetaling::class -> lagVedtakFeilutbetalingUforeDto() as T
            VedtakIngenTilbakekreving::class -> lagVedtakFeilutbetalingUforeIngenTilbakekrevingDto() as T
            VedtakIngenTilbakekrevingForeldelse::class -> lagVedtakFeilutbetalingUforeIngenTilbakekrevingDto() as T
            VarselFeilutbetalingSivilstand12_13_2::class -> lagFeilutbetalingSpesfikkVarsel() as T
            VarselBtBarnUtland12_15::class -> lagFeilutbetalingSpesfikkVarsel() as T
            VarselSoning12_20::class -> lagFeilutbetalingSpesfikkVarsel() as T
            VarselFeilutbetalingSivilstandUngUfor12_13_3::class -> lagFeilutbetalingSpesfikkVarsel() as T
            VarselInstitusjon12_19::class -> lagFeilutbetalingSpesfikkVarsel() as T
            VarselBtBarnetFlytter12_15::class -> lagFeilutbetalingSpesfikkVarsel() as T
            VarselDodsbo::class -> lagFeilutbetalingVarselDodsbo() as T
            Naeringsinntekter::class -> lagInnhentingOpplysningerNaeringsinntekt() as T
            OppgittSamboer::class -> lagInnhentingOpplysningerSamboer() as T
            VarselOmLavereReduksjonsprosentRedigerbar::class -> lagEmptyRedigerbarRedigerbarBrevdata() as T
            VarselOmOktMinsteIFUOgLavereReduksjonsprosentRedigerbar::class -> lagEmptyRedigerbarRedigerbarBrevdata() as T
            VarselOmOktMinsteIFURedigerbar::class -> lagEmptyRedigerbarRedigerbarBrevdata() as T
            VarselOmLavereMinstesatsS::class -> lagEmptyRedigerbarRedigerbarBrevdata() as T
            SoknadBarnetilleggFosterForelder::class -> lagEmptyRedigerbarRedigerbarBrevdata() as T
            FlereOpplysningerGenerell::class -> lagEmptyRedigerbarRedigerbarBrevdata() as T
            SoknadBarnetillegg::class -> lagEmptyRedigerbarRedigerbarBrevdata() as T
            TrukketKlage::class -> lagEmptyRedigerbarRedigerbarBrevdata() as T
            FlereOpplysningerFirmaInntekt::class -> lagEmptyRedigerbarRedigerbarBrevdata() as T
            NySivilstand::class -> lagEmptyRedigerbarRedigerbarBrevdata() as T
            EktefelleUtland::class -> lagEmptyRedigerbarRedigerbarBrevdata() as T
            UtsattKlagefrist::class -> lagEmptyRedigerbarRedigerbarBrevdata() as T
            AntattDod::class -> lagEmptyRedigerbarRedigerbarBrevdata() as T
            SoknadBarnetilleggUtland::class -> lagEmptyRedigerbarRedigerbarBrevdata() as T
            BrukerLegeerklaering::class -> lagEmptyRedigerbarRedigerbarBrevdata() as T
            LegeLegeerklaering::class -> lagEmptyRedigerbarRedigerbarBrevdata() as T
            HvilendeRettMidlertidigOppHoer::class -> lagEmptyRedigerbarRedigerbarBrevdata() as T
            HvilendeRettOppHoer::class -> lagEmptyRedigerbarRedigerbarBrevdata() as T
            HvilendeRettInfo4Aar::class -> lagEmptyRedigerbarRedigerbarBrevdata() as T
            HvilendeRettVarselOpphoer::class -> lagEmptyRedigerbarRedigerbarBrevdata() as T
            UforeAvslagYrkesskadeIkkeGodkjent::class -> lagEmptyRedigerbarRedigerbarBrevdata() as T
            InfoOmReverseringAvMinstesats::class -> EmptyAutobrevdata as T
            VarselOmOktMinsteIFUOgLavereReduksjonsprosent::class -> EmptyAutobrevdata as T
            VarselOmLavereMinstesats::class -> EmptyAutobrevdata as T
            VarselOmLavereReduksjonsprosent::class -> EmptyAutobrevdata as T
            VarselOmOktMinsteIFU::class -> EmptyAutobrevdata as T
            InfobrevLovendringer2026::class -> EmptyAutobrevdata as T
            else -> throw IllegalArgumentException("Don't know how to construct: ${templateType.qualifiedName}")
        }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> createVedlegg(letterDataType: KClass<T>): T = when (letterDataType) {
        OversiktOverFeilutbetalingPEDto::class -> createOversiktOverFeilutbetalingPEDto() as T
        else -> throw IllegalArgumentException("Don't know how to construct: ${letterDataType.qualifiedName}")
    }

    private fun lagEmptyRedigerbarRedigerbarBrevdata() =
        EmptyRedigerbarBrevdata(saksbehandlerValg = SaksbehandlervalgIDSLTestImpl())

    private fun lagFeilutbetalingSpesfikkVarsel() = FeilutbetalingSpesifikkVarselDto(
        pesysData = VarselFeilutbetalingPesysData(100),
        saksbehandlerValg = lagSaksbehandlervalg(),
    )

    private fun lagInnhentingOpplysningerNaeringsinntekt() = InnhentingOpplysningerNaeringsinntektDto(
        pesysData = EmptyFagsystemdata,
        saksbehandlerValg = lagSaksbehandlervalg(
            "ikkeMottattInntektsskjema" to true
        )
    )

    private fun lagInnhentingOpplysningerSamboer() = InnhentingOpplysningerSamboerDto(
        pesysData = EmptyFagsystemdata,
        saksbehandlerValg = lagSaksbehandlervalg(
            "ukjentSamboer" to false
        )
    )

    private fun lagUforeAvslagDto() = UforeAvslagDto(
        pesysData = UforeAvslagDto.UforeAvslagPendata(
            kravMottattDato = vilkaarligDato,
        ),
        saksbehandlerValg = lagSaksbehandlervalg(),
    )

    private fun lagUforeAvslagEnkelDto() = UforeAvslagEnkelDto(
        pesysData = UforeAvslagEnkelDto.UforeAvslagPendata(
            kravMottattDato = vilkaarligDato,
            vurdering = "Vurdering 1"
        ),
        saksbehandlerValg = lagSaksbehandlervalg(
            "VisVurderingFraVilkarvedtak" to true
        )
    )

    private fun lagUforeAvslagTestmalDto() = UforeAvslagTestmalDto(
        pesysData = UforeAvslagTestmalDto.UforeAvslagPendata(
            kravMottattDato = vilkaarligDato,
            vurdering = listOf("Vurdering 1", "Vurdering 2"),
            vurderingsTekst = "Vurdering 3"
        ),
        saksbehandlerValg = lagSaksbehandlervalg(
            "VisVurderingFraVilkarvedtak" to true
        )
    )

    private fun lagUforeAvslagUtlandDto() = UforeAvslagUtlandDto(
        pesysData = UforeAvslagUtlandDto.UforeAvslagPendata(
            kravMottattDato = LocalDate.now(),
            kravGjelder = UforeAvslagUtlandDto.KravGjelder.MELLOMBH,
            eosNordisk = false,
            avtaletype = "USA",
            artikkel = "8",
            trygdetidListe = listOf(
                UforeAvslagUtlandDto.Trygdetid(
                    land = "Norge",
                    fomDato = LocalDate.of(2000, Month.JANUARY, 1),
                    tomDato = LocalDate.of(2010, Month.DECEMBER, 31)
                ),
                UforeAvslagUtlandDto.Trygdetid(
                    land = "Danmark",
                    fomDato = LocalDate.of(2011, Month.JANUARY, 1),
                    tomDato = LocalDate.of(2020, Month.DECEMBER, 31)
                )
            )
        ),
        saksbehandlerValg = lagSaksbehandlervalg(
            "visInnvilgetPensjonEOSLand" to true,
            "visBrukerIkkeOmfattesAvPersonkretsTrygdeforordning" to true,
            "visSupplerendeStonadUforeFlykninger" to true,
        )
    )

    private fun lagUforeAvslagInntektDto() = UforeAvslagInntektDto(
        pesysData = UforeAvslagInntektDto.UforeAvslagInntektPendata(
            kravMottattDato = vilkaarligDato,
            vurdering = "Vurdering 1",
            uforetidspunkt = vilkaarligDato,
            uforegrad = 50,
            inntektForUforhet = 1,
            inntektEtterUforhet = 2,
            vurderingIFU = "Vurdering IFU",
            vurderingIEU = "Vurdering IEU"
        ),
        saksbehandlerValg = lagSaksbehandlervalg(
            "VisVurderingFraVilkarvedtak" to false,
            "visVurderingIFU" to false
        )
    )

    private fun lagVarselFeilutbetalingUforeDto() = VarselFeilutbetalingUforeDto(
        pesysData = VarselFeilutbetalingPesysData(
            feilutbetaltBrutto = 100
        ),
        saksbehandlerValg = lagSaksbehandlervalg(
            "rentetillegg" to true
        ),
    )

    private fun lagVedtakFeilutbetalingUforeDto() = VedtakFeilutbetalingUforeDto(
        pesysData = PesysData(
            feilutbetaltTotalBelop = 1,
            resultatAvVurderingenForTotalBelop = TilbakekrevingResultat.FULL_TILBAKEKREV,
            sluttPeriodeForTilbakekreving = vilkaarligDato,
            startPeriodeForTilbakekreving = vilkaarligDato,
            sumTilInnkrevingTotalBelop = 2,
            dineRettigheterOgMulighetTilAKlageDto = createDineRettigheterOgMulighetTilAaKlageDto(),
            oversiktOverFeilutbetalingPEDto = createOversiktOverFeilutbetalingPEDto(),
        ),
        saksbehandlerValg = lagSaksbehandlervalg(),
    )

    private fun lagFeilutbetalingPerAr(): List<FeilutbetalingPerAr> {
        return listOf(
            FeilutbetalingPerAr(
                ar = 2023,
                feilutbetalingManed = lagFeilutbetalingPerManed()
            ),
            FeilutbetalingPerAr(
                ar = 2024,
                feilutbetalingManed = lagFeilutbetalingPerManed()
            )
        )
    }

    private fun lagFeilutbetalingPerManed(): List<FeilutbetalingManed> {
        return listOf(
            FeilutbetalingManed(
                maned = Month.MAY,
                feilutbetaltBelop = 1000,
                resultat = TilbakekrevingResultat.DELVIS_TILBAKEKREV,
                bruttoBelop = 500,
                nettobelop = 400,
                skatt = 100,
                opprinneligBrutto = 5000
            ),
            FeilutbetalingManed(
                maned = Month.JUNE,
                feilutbetaltBelop = 1000,
                resultat = TilbakekrevingResultat.DELVIS_TILBAKEKREV,
                bruttoBelop = 500,
                nettobelop = 400,
                skatt = 100,
                opprinneligBrutto = 5000
            )
        )
    }

    private fun lagVedtakFeilutbetalingUforeIngenTilbakekrevingDto() = VedtakFeilutbetalingUforeIngenTilbakekrevingDto(
        pesysData = PesysData(
            feilutbetaltTotalBelop = 1,
            resultatAvVurderingenForTotalBelop = TilbakekrevingResultat.FULL_TILBAKEKREV,
            sluttPeriodeForTilbakekreving = vilkaarligDato,
            startPeriodeForTilbakekreving = vilkaarligDato,
            sumTilInnkrevingTotalBelop = 2,
            dineRettigheterOgMulighetTilAKlageDto = createDineRettigheterOgMulighetTilAaKlageDto(),
            oversiktOverFeilutbetalingPEDto = createOversiktOverFeilutbetalingPEDto(),
        ),
        saksbehandlerValg = lagSaksbehandlervalg(),
    )

    private fun createDineRettigheterOgMulighetTilAaKlageDto() = DineRettigheterOgMulighetTilAKlageDto(
        sakstype = Sakstype.UFOREP,
        brukerUnder18Ar = false
    )

    private fun createOversiktOverFeilutbetalingPEDto() = OversiktOverFeilutbetalingPEDto(
        bruttoTilbakekrevdTotalbelop = 1,
        nettoUtenRenterTilbakekrevdTotalbelop = 2,
        rentetilleggSomInnkrevesTotalbelop = 3,
        resultatAvVurderingenForTotalbelop = TilbakekrevingResultat.FULL_TILBAKEKREV,
        skattefradragSomInnkrevesTotalbelop = 4,
        tilbakekrevingPerManed = listOf(
            OversiktOverFeilutbetalingPEDto.Tilbakekreving(
                manedOgAr = vilkaarligDato.minusMonths(1).minusYears(1),
                bruttobelopTilbakekrevd = 500,
                feilutbetaltBelop = 1000,
                nettobelopUtenRenterTilbakekrevd = 400,
                resultatAvVurderingen = TilbakekrevingResultat.DELVIS_TILBAKEKREV,
                skattefradragSomInnkreves = 100,
                ytelsenMedFeilutbetaling = KonteringType.UT_ORDINER
            ),
            OversiktOverFeilutbetalingPEDto.Tilbakekreving(
                manedOgAr = vilkaarligDato,
                bruttobelopTilbakekrevd = 500,
                feilutbetaltBelop = 1000,
                nettobelopUtenRenterTilbakekrevd = 400,
                resultatAvVurderingen = TilbakekrevingResultat.DELVIS_TILBAKEKREV,
                skattefradragSomInnkreves = 100,
                ytelsenMedFeilutbetaling = KonteringType.UT_ORDINER
            ),
            OversiktOverFeilutbetalingPEDto.Tilbakekreving(
                manedOgAr = vilkaarligDato.minusMonths(1),
                bruttobelopTilbakekrevd = 500,
                feilutbetaltBelop = 1000,
                nettobelopUtenRenterTilbakekrevd = 400,
                resultatAvVurderingen = TilbakekrevingResultat.DELVIS_TILBAKEKREV,
                skattefradragSomInnkreves = 100,
                ytelsenMedFeilutbetaling = KonteringType.UT_ORDINER
            ),
            OversiktOverFeilutbetalingPEDto.Tilbakekreving(
                manedOgAr = vilkaarligDato,
                bruttobelopTilbakekrevd = 500,
                feilutbetaltBelop = 1000,
                nettobelopUtenRenterTilbakekrevd = 400,
                resultatAvVurderingen = TilbakekrevingResultat.DELVIS_TILBAKEKREV,
                skattefradragSomInnkreves = 100,
                ytelsenMedFeilutbetaling = KonteringType.UT_ORDINER
            )
        ),
        feilutbetalingPerArListe = lagFeilutbetalingPerAr(),
    )

    fun lagFeilutbetalingVarselDodsbo() = FeilutbetalingVarselDodsboDto(
        saksbehandlerValg = lagSaksbehandlervalg(
            "kjentBobestyrer" to true
        ),
        pesysData = VarselFeilutbetalingPesysData(feilutbetaltBrutto = 100)
    )

    private fun lagInfoEndretUTPgaInntektDto() = InfoEndretUTPgaInntektDto(
        belopsgrense = Kroner(60000)
    )


}
