package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.BeloepEndring
import no.nav.pensjon.brev.api.model.KravInitiertAv
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.BrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Percent

@Suppress("unused")
data class AvslagPaaGjenlevenderettIAlderspensjonDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: PesysData,
) : BrevdataMedSaksbehandlerValg<AvslagPaaGjenlevenderettIAlderspensjonDto.PesysData> {
    data class PesysData(
        val alderspensjonVedVirk: AlderspensjonVedVirk,
        val krav: Krav,
        val avdoed: Avdoed,
        val ytelseskomponentInformasjon: YtelseskomponentInformasjon,
        val beregnetPensjonPerMaaned: BeregnetPensjonPerManed,
        val avtaleland: Avtaleland?,
        val bruker: Bruker,
        val dineRettigheterOgMulighetTilAaKlage: DineRettigheterOgMulighetTilAaKlageDto,
        val maanedligPensjonFoerSkatt: MaanedligPensjonFoerSkattDto?,
        val maanedligPensjonFoerSkattAP2025: MaanedligPensjonFoerSkattAP2025Dto?,
    ) : FagsystemBrevdata {
        data class AlderspensjonVedVirk(val totalPensjon: Kroner, val uttaksgrad: Percent)
        data class Krav(val kravInitiertAv: KravInitiertAv)
        data class Avdoed(val harTrygdetidNorge: Boolean, val harTrygdetidEOS: Boolean, val harTrygdetidAvtaleland: Boolean)
        data class YtelseskomponentInformasjon(val beloepEndring: BeloepEndring)
        data class BeregnetPensjonPerManed(val antallBeregningsperioderPensjon: Int)
        data class Avtaleland(val erEOSLand: Boolean, val navn: String?)
        data class Bruker(val faktiskBostedsland: String?)
    }
}