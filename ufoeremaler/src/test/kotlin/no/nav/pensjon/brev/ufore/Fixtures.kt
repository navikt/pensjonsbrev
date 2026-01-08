package no.nav.pensjon.brev.ufore

import no.nav.brev.brevbaker.FellesFactory
import no.nav.brev.brevbaker.LetterDataFactory
import no.nav.brev.brevbaker.vilkaarligDato
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.*
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.FeilutbetalingDodsboSaksbehandlervalg
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.FeilutbetalingSpesifikkVarselDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.FeilutbetalingVarselDodsboDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.VarselFeilutbetalingPesysData
import java.time.LocalDate
import java.time.Month
import kotlin.reflect.KClass

object Fixtures : LetterDataFactory {

    val felles = FellesFactory.felles

    inline fun <reified T : Any> create(): T = create(T::class)

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> create(letterDataType: KClass<T>): T =
        when (letterDataType) {
            UforeAvslagUtenVurderingDto::class -> lagUforeAvslagUtenVurderingDto() as T
            UforeAvslagEnkelDto::class -> lagUforeAvslagEnkelDto() as T
            UforeAvslagInntektDto::class -> lagUforeAvslagInntektDto() as T
            UforeAvslagUforetidspunkt26Dto::class -> lagUforeAvslagUforetidspunkt26Dto() as T
            UforeAvslagForverrelseEtter26Dto::class -> lagUforeAvslagForverrelseEtter26Dto() as T
            UforeAvslagUtlandDto::class -> lagUforeAvslagUtlandDto() as T
            UforeAvslagSupplerendeStonadEnkelDto::class -> lagUforeAvslagSupplerendeStonadEnkelDto() as T
            VarselFeilutbetalingUforeDto::class -> lagVarselFeilutbetalingUforeDto() as T
            VedtakFeilutbetalingUforeDto::class -> lagVedtakFeilutbetalingUforeDto() as T
            VedtakFeilutbetalingUforeIngenTilbakekrevingDto::class -> lagVedtakFeilutbetalingUforeIngenTilbakekrevingDto() as T
            FeilutbetalingSpesifikkVarselDto::class -> lagFeilutbetalingSpesfikkVarsel() as T
            FeilutbetalingVarselDodsboDto::class -> lagFeilutbetalingVarselDodsbo() as T
            EmptyRedigerbarBrevdata::class -> lagEmptyRedigerbarBrevdata() as T
            else -> throw IllegalArgumentException("Don't know how to construct: ${letterDataType.qualifiedName}")
        }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> createVedlegg(letterDataType: KClass<T>): T = when(letterDataType) {
        OversiktOverFeilutbetalingPEDto::class -> createOversiktOverFeilutbetalingPEDto() as T
        else -> throw IllegalArgumentException("Don't know how to construct: ${letterDataType.qualifiedName}")
    }

    private fun lagEmptyRedigerbarBrevdata() = EmptyRedigerbarBrevdata

    private fun lagFeilutbetalingSpesfikkVarsel() = FeilutbetalingSpesifikkVarselDto(
        pesysData = VarselFeilutbetalingPesysData(100),
        saksbehandlerValg = EmptySaksbehandlerValg,
    )

    private fun lagUforeAvslagUtenVurderingDto() = UforeAvslagUtenVurderingDto(
        pesysData = UforeAvslagUtenVurderingDto.UforeAvslagPendata(
            kravMottattDato = vilkaarligDato,
        ), EmptySaksbehandlerValg
    )

    private fun lagUforeAvslagUforetidspunkt26Dto() = UforeAvslagUforetidspunkt26Dto(
        pesysData = UforeAvslagUforetidspunkt26Dto.UforeAvslagPendata(
            kravMottattDato = vilkaarligDato,
            vurdering = "Vurdering 1"
        ),
        saksbehandlerValg = UforeAvslagUforetidspunkt26Dto.Saksbehandlervalg(
            VisVurderingFraVilkarvedtak = true,
            visUforetidspunktEtter26 = true
        )
    )

    private fun lagUforeAvslagForverrelseEtter26Dto() = UforeAvslagForverrelseEtter26Dto(
        pesysData = UforeAvslagForverrelseEtter26Dto.UforeAvslagPendata(
            kravMottattDato = vilkaarligDato,
            vurdering = "Vurdering 1"
        ),
        saksbehandlerValg = UforeAvslagForverrelseEtter26Dto.Saksbehandlervalg(
            VisVurderingFraVilkarvedtak = true,
            visForverrelseEtter26 = true
        )
    )

    private fun lagUforeAvslagEnkelDto() = UforeAvslagEnkelDto(
        pesysData = UforeAvslagEnkelDto.UforeAvslagPendata(
            kravMottattDato = vilkaarligDato,
            vurdering = "Vurdering 1"
        ),
        saksbehandlerValg = UforeAvslagEnkelDto.Saksbehandlervalg(
            VisVurderingFraVilkarvedtak = true
        )
    )

    private fun lagUforeAvslagSupplerendeStonadEnkelDto() = UforeAvslagSupplerendeStonadEnkelDto(
        pesysData = UforeAvslagSupplerendeStonadEnkelDto.UforeAvslagPendata(
            kravMottattDato = vilkaarligDato,
            vurdering = "Vurdering 1"
        ),
        saksbehandlerValg = UforeAvslagSupplerendeStonadEnkelDto.Saksbehandlervalg(
            VisVurderingFraVilkarvedtak = true,
            visSupplerendeStonadUforeFlykninger = true,
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
        saksbehandlerValg = UforeAvslagUtlandDto.Saksbehandlervalg(
            visInnvilgetPensjonEOSLand = true,
            visBrukerIkkeOmfattesAvPersonkretsTrygdeforordning = true,
            visSupplerendeStonadUforeFlykninger = true,
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
        saksbehandlerValg = UforeAvslagInntektDto.SaksbehandlervalgInntekt(
            VisVurderingFraVilkarvedtak = false,
            visVurderingIFU = false
        )
    )

    private fun lagVarselFeilutbetalingUforeDto() = VarselFeilutbetalingUforeDto(
        pesysData = EmptyFagsystemdata,
        saksbehandlerValg = VarselFeilutbetalingUforeDto.Saksbehandlervalg(
            rentetillegg = true
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
        saksbehandlerValg = VedtakFeilutbetalingUforeDto.Saksbehandlervalg(
            sivilstandEndret = true,
            reduksjonForeldelse = true
        )
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
        saksbehandlerValg = EmptySaksbehandlerValg
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
            )),
        feilutbetalingPerArListe = lagFeilutbetalingPerAr(),
    )

    fun lagFeilutbetalingVarselDodsbo() = FeilutbetalingVarselDodsboDto(
        saksbehandlerValg = FeilutbetalingDodsboSaksbehandlervalg(),
        pesysData = VarselFeilutbetalingPesysData(feilutbetaltBrutto = 100)
    )
}