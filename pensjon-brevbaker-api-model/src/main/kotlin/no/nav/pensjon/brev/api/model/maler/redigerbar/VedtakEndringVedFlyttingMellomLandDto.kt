package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.BeloepEndring
import no.nav.pensjon.brev.api.model.EksportForbudKode
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.Percent
import java.time.LocalDate

data class VedtakEndringVedFlyttingMellomLandDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<VedtakEndringVedFlyttingMellomLandDto.SaksbehandlerValg, VedtakEndringVedFlyttingMellomLandDto.PesysData> {
    data class SaksbehandlerValg(
        val innvandret: Boolean,
        val reduksjonTilbakeITid: Boolean,
        val endringIPensjonen: Boolean,
        val etterbetaling: Boolean,
    ) : BrevbakerBrevdata

    data class PesysData(
        val krav: Krav,
        val bruker: Bruker,
        val alderspensjonVedVirk: AlderspensjonVedVirk,
        val inngangOgEksportVurdering: InngangOgEksportVurdering,
        val inngangOgEksportVurderingAvdoed: InngangOgEksportVurderingAvdoed?,
        val opphoersbegrunnelseVedVirk: OpphoersbegrunnelseVedVirk,
        val ytelseskomponentInformasjon: YtelseskomponentInformasjon,
        val beregnetpensjonPerMaanedVedVirk: BeregnetPensjonPerMaanedVedVirk,
        val erEtterbetaling1Maaned: Boolean
    ) : BrevbakerBrevdata {
        data class Krav(val virkDatoFom: LocalDate, val aarsak: Aarsak)
        data class Bruker(val faktiskBostedsland: String?, val borIEOES: Boolean, val borIAvtaleland: Boolean)
        data class AlderspensjonVedVirk(
            val erEksportberegnet: Boolean,
            val garantipensjonInnvilget: Boolean,
            val pensjonstilleggInnvilget: Boolean,
            val minstenivaaIndividuellInnvilget: Boolean,
            val minstenivaaPensjonistParInnvilget: Boolean,
            val uforeKombinertMedAlder: Boolean,
            val totalPensjon: Kroner,
            val gjenlevenderettAnvendt: Boolean,
            val uttaksgrad: Percent,
        )

        data class InngangOgEksportVurdering(
            val eksportForbudKode: EksportForbudKode?,
            val minst20AarTrygdetid: Boolean,
            val eksportTrygdeavtaleEOES: Boolean,
            val eksportTrygdeavtaleAvtaleland: Boolean,
        )

        data class InngangOgEksportVurderingAvdoed(
            val eksportForbudKode: EksportForbudKode?,
            val minst20ArTrygdetidKap20: Boolean,
            val minst20ArBotidKap19: Boolean,
        )

        data class OpphoersbegrunnelseVedVirk(
            val begrunnelseET: Opphoersbegrunnelse?,
            val begrunnelseBT: Opphoersbegrunnelse?,
        )

        data class YtelseskomponentInformasjon(val beloepEndring: BeloepEndring?)
        data class BeregnetPensjonPerMaanedVedVirk(val grunnnpensjon: Kroner)
    }

    enum class Aarsak {
        UTVANDRET,
        INNVANDRET
    }

    enum class Opphoersbegrunnelse {
        BRUKER_FLYTTET_IKKE_AVT_LAND,
        ANNET
    }
}