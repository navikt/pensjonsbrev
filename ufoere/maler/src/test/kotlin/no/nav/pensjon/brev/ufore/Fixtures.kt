package no.nav.pensjon.brev.ufore

import no.nav.brev.brevbaker.*
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.ufore.api.model.maler.info.InfoEndretUTPgaInntektDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.*
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.VarselFeilutbetalingPesysData
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate
import java.time.Month
import kotlin.reflect.KClass

object Fixtures : LetterDataFactory {

    val felles = FellesFactory.felles

    inline fun <reified T : Any> create(): T = create(T::class)

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> create(letterDataType: KClass<T>): T =
        when (letterDataType) {
            InfoEndretUTPgaInntektDto::class -> lagInfoEndretUTPgaInntektDto() as T
            UforeAvslagTestmalDto::class -> lagUforeAvslagTestmalDto() as T
            UforeAvslagEnkelDto::class -> lagUforeAvslagEnkelDto() as T
            UforeAvslagInntektDto::class -> lagUforeAvslagInntektDto() as T
            UforeAvslagUtlandDto::class -> lagUforeAvslagUtlandDto() as T
            UforeAvslagDto::class -> lagUforeAvslagDto() as T
            VedtakFeilutbetalingUforeDto::class -> lagVedtakFeilutbetalingUforeDto() as T
            VarselFeilutbetalingPesysData::class -> lagFeilutbetalingSpesfikkVarsel() as T
            EmptyAutobrevdata::class -> EmptyAutobrevdata as T
            EmptyFagsystemdata::class -> EmptyFagsystemdata as T
            else -> throw IllegalArgumentException("Don't know how to construct: ${letterDataType.qualifiedName}")
        }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> createVedlegg(letterDataType: KClass<T>): T = when (letterDataType) {
        OversiktOverFeilutbetalingPEDto::class -> createOversiktOverFeilutbetalingPEDto() as T
        else -> throw IllegalArgumentException("Don't know how to construct: ${letterDataType.qualifiedName}")
    }

    private fun lagFeilutbetalingSpesfikkVarsel() = VarselFeilutbetalingPesysData(100)

    private fun lagUforeAvslagDto() = UforeAvslagDto(
        kravMottattDato = vilkaarligDato,
    )

    private fun lagUforeAvslagEnkelDto() = UforeAvslagEnkelDto(
        kravMottattDato = vilkaarligDato,
        vurdering = "Vurdering 1"
    )

    private fun lagUforeAvslagTestmalDto() = UforeAvslagTestmalDto(
        kravMottattDato = vilkaarligDato,
        vurdering = listOf("Vurdering 1", "Vurdering 2"),
        vurderingsTekst = "Vurdering 3"
    )

    private fun lagUforeAvslagUtlandDto() = UforeAvslagUtlandDto(
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
    )

    private fun lagUforeAvslagInntektDto() = UforeAvslagInntektDto(
        kravMottattDato = vilkaarligDato,
        vurdering = "Vurdering 1",
        uforetidspunkt = vilkaarligDato,
        uforegrad = 50,
        inntektForUforhet = 1,
        inntektEtterUforhet = 2,
        vurderingIFU = "Vurdering IFU",
        vurderingIEU = "Vurdering IEU"
    )

    private fun lagVedtakFeilutbetalingUforeDto() = VedtakFeilutbetalingUforeDto(
            feilutbetaltTotalBelop = 1,
            resultatAvVurderingenForTotalBelop = TilbakekrevingResultat.FULL_TILBAKEKREV,
            sluttPeriodeForTilbakekreving = vilkaarligDato,
            startPeriodeForTilbakekreving = vilkaarligDato,
            sumTilInnkrevingTotalBelop = 2,
            dineRettigheterOgMulighetTilAKlageDto = createDineRettigheterOgMulighetTilAaKlageDto(),
            oversiktOverFeilutbetalingPEDto = createOversiktOverFeilutbetalingPEDto(),
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

    private fun lagInfoEndretUTPgaInntektDto() = InfoEndretUTPgaInntektDto(
        belopsgrense = Kroner(60000)
    )


}
