package no.nav.pensjon.brev

import no.nav.brev.brevbaker.FellesFactory
import no.nav.brev.brevbaker.LetterDataFactory
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.DineRettigheterOgMulighetTilAKlageDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.OversiktOverFeilutbetalingPEDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.PesysData
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.TilbakekrevingResultat
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.VarselFeilutbetalingUforeDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.VedtakFeilutbetalingUforeDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.VedtakFeilutbetalingUforeIngenTilbakekrevingDto
import java.time.LocalDate
import kotlin.reflect.KClass

object Fixtures : LetterDataFactory {

    val felles = FellesFactory.felles

    inline fun <reified T : Any> create(): T = create(T::class)

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> create(letterDataType: KClass<T>): T =
        when (letterDataType) {
            UforeAvslagEnkelDto::class -> lagUforeAvslagEnkelDto() as T
            UforeAvslagInntektDto::class -> lagUforeAvslagInntektDto() as T
            VarselFeilutbetalingUforeDto::class -> lagVarselFeilutbetalingUforeDto() as T
            VedtakFeilutbetalingUforeDto::class -> lagVedtakFeilutbetalingUforeDto() as T
            VedtakFeilutbetalingUforeIngenTilbakekrevingDto::class -> lagVedtakFeilutbetalingUforeIngenTilbakekrevingDto() as T
            else -> throw IllegalArgumentException("Don't know how to construct: ${letterDataType.qualifiedName}")
        }

    private fun lagUforeAvslagEnkelDto() = UforeAvslagEnkelDto(
        pesysData = UforeAvslagEnkelDto.UforeAvslagPendata(
            kravMottattDato = LocalDate.now(),
            vurdering = "Vurdering 1"
        ),
        saksbehandlerValg = UforeAvslagEnkelDto.Saksbehandlervalg(
            VisVurderingFraVilkarvedtak = true
        )
    )
    
    private fun lagUforeAvslagInntektDto() = UforeAvslagInntektDto(
        pesysData = UforeAvslagInntektDto.UforeAvslagInntektPendata(
            kravMottattDato = LocalDate.now(),
            vurdering = "Vurdering 1",
            inntektForUforhet = 1,
            inntektEtterUforhet = 2,
            vurderingIFU = "Vurdering 2"
        ),
        saksbehandlerValg = UforeAvslagInntektDto.SaksbehandlervalgInntekt(
            VisVurderingFraVilkarvedtak = true,
            visVurderingIFU = true
        )
    )

    private fun lagVarselFeilutbetalingUforeDto() = VarselFeilutbetalingUforeDto(
        pesysData = EmptyBrevdata,
        saksbehandlerValg = VarselFeilutbetalingUforeDto.Saksbehandlervalg(
            rentetillegg = true
        ),
    )

    private fun lagVedtakFeilutbetalingUforeDto() = VedtakFeilutbetalingUforeDto(
        pesysData = PesysData(
            feilutbetaltTotalBelop = 1,
            resultatAvVurderingenForTotalBelop = TilbakekrevingResultat.FULL_TILBAKEKREV,
            sluttPeriodeForTilbakekreving = LocalDate.now(),
            startPeriodeForTilbakekreving = LocalDate.now(),
            sumTilInnkrevingTotalBelop = 2,
            dineRettigheterOgMulighetTilAKlageDto = createDineRettigheterOgMulighetTilAaKlageDto(),
            oversiktOverFeilutbetalingPEDto = createOversiktOverFeilutbetalingPEDto()
        ),
        saksbehandlerValg = VedtakFeilutbetalingUforeDto.Saksbehandlervalg(
            sivilstandEndret = true,
            reduksjonForeldelse = true
        )
    )

    private fun lagVedtakFeilutbetalingUforeIngenTilbakekrevingDto() = VedtakFeilutbetalingUforeIngenTilbakekrevingDto(
        pesysData = PesysData(
            feilutbetaltTotalBelop = 1,
            resultatAvVurderingenForTotalBelop = TilbakekrevingResultat.FULL_TILBAKEKREV,
            sluttPeriodeForTilbakekreving = LocalDate.now(),
            startPeriodeForTilbakekreving = LocalDate.now(),
            sumTilInnkrevingTotalBelop = 2,
            dineRettigheterOgMulighetTilAKlageDto = createDineRettigheterOgMulighetTilAaKlageDto(),
            oversiktOverFeilutbetalingPEDto = createOversiktOverFeilutbetalingPEDto()
        ),
        saksbehandlerValg = EmptySaksbehandlerValg
    )

    fun createDineRettigheterOgMulighetTilAaKlageDto() = DineRettigheterOgMulighetTilAKlageDto(
        sakstype = Sakstype.UFOREP,
        brukerUnder18Ar = false
    )
    
    fun createOversiktOverFeilutbetalingPEDto() = OversiktOverFeilutbetalingPEDto(
        bruttoTilbakekrevdTotalbelop = 1,
        nettoUtenRenterTilbakekrevdTotalbelop = 2,
        rentetilleggSomInnkrevesTotalbelop = 3,
        resultatAvVurderingenForTotalbelop = TilbakekrevingResultat.FULL_TILBAKEKREV,
        skattefradragSomInnkrevesTotalbelop = 4,
        tilbakekrevingPerManed = listOf()
    )
}