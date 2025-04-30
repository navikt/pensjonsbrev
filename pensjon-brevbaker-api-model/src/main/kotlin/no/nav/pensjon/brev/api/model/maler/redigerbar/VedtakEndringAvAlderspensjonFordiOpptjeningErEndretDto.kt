package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.BelopEndring
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

data class VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto.SaksbehandlerValg, VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto.PesysData> {
    data class SaksbehandlerValg(
        val visUendretOpptjening: Boolean,
        val visOektOpptjening: Boolean,
        val visRedusertOpptjening: Boolean,
    ) : BrevbakerBrevdata

    data class PesysData(
        val krav: Krav,
        val alderspensjonVedVirk: AlderspensjonVedVirk,
        val ytelseskomponentInformasjon: YtelseskomponentInformasjon,
        val behandlingKontekst: BehandlingKontekst
    ) : BrevbakerBrevdata

    data class Krav(
        val virkDatoFom: LocalDate,
        val arsakErEndretOpptjening: Boolean,
        val erForstegangsbehandling: Boolean,
    )

    data class AlderspensjonVedVirk(
        val skjermingstilleggInnvilget: Boolean,
        val totalPensjon: Kroner,
        val skjermingstillegg: Kroner?,
        val uforeKombinertMedAlder: Boolean,
        val regelverkType: AlderspensjonRegelverkType,
        val fullUttaksgrad: Boolean,
    )

    data class YtelseskomponentInformasjon(
        val belopEndring: BelopEndring
    )

    data class BehandlingKontekst(
        val konteksttypeErKorrigeringopptjening: Boolean
    )
}