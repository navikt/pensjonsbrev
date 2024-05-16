package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import java.time.LocalDate

@Suppress("unused")
data class InformasjonOmSaksbehandlingstidDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: EmptyBrevdata
) : RedigerbarBrevdata<InformasjonOmSaksbehandlingstidDto.SaksbehandlerValg, EmptyBrevdata> {
    data class SaksbehandlerValg(
        val mottattSoeknad: LocalDate,
        val ytelse: String,
        val land: String?,
        val inkluderVenterSvarAFP: InkluderVenterSvarAFP?,
        val svartidUker: Int,
    ) : BrevbakerBrevdata {
        data class InkluderVenterSvarAFP(val uttakAlderspensjonProsent: Int, val uttaksDato: LocalDate)
    }
}