package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import java.time.LocalDate

@Suppress("unused")
data class InformasjonOmSaksbehandlingstidDto(
    val mottattSoeknad: LocalDate,
    val ytelse: String,
    val land: String?,
    val inkluderVenterSvarAFP: InkluderVenterSvarAFP?,
    val svartidUker: Int,
) : BrevbakerBrevdata {
    data class InkluderVenterSvarAFP(val uttakAlderspensjonProsent: Int, val uttaksDato: LocalDate)
}