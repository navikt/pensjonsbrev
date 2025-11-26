package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.BeloepEndring
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025Dto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

data class VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto(
    override val saksbehandlerValg: EmptySaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EmptySaksbehandlerValg, VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto.PesysData> {
    data class PesysData(
        val krav: Krav,
        val alderspensjonVedVirk: AlderspensjonVedVirk,
        val ytelseskomponentInformasjon: YtelseskomponentInformasjon,
        val behandlingKontekst: BehandlingKontekst,
        val etterbetaling: Boolean,
        val orienteringOmRettigheterOgPlikter: OrienteringOmRettigheterOgPlikterDto,
        val maanedligPensjonFoerSkatt: MaanedligPensjonFoerSkattDto?,
        val maanedligPensjonFoerSkattAP2025: MaanedligPensjonFoerSkattAP2025Dto?,
        val opplysningerBruktIBeregningenAlder: OpplysningerBruktIBeregningenAlderDto?,
        val opplysningerBruktIBeregningenAlderAP2025: OpplysningerBruktIBeregningenAlderAP2025Dto?,
    ) : FagsystemBrevdata

    data class Krav(
        val virkDatoFom: LocalDate,
        val arsakErEndretOpptjening: Boolean,
        val erForstegangsbehandling: Boolean,
    )

    data class AlderspensjonVedVirk(
        val totalPensjon: Kroner,
        val uforeKombinertMedAlder: Boolean,
        val regelverkType: AlderspensjonRegelverkType,
        val fullUttaksgrad: Boolean,
    )

    data class YtelseskomponentInformasjon(
        val belopEndring: BeloepEndring
    )

    data class BehandlingKontekst(
        val konteksttypeErKorrigeringopptjening: Boolean
    )
}