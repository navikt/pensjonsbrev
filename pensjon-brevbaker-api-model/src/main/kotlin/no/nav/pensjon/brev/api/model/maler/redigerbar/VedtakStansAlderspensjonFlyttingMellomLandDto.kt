package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import java.time.LocalDate

@Suppress("unused")
data class VedtakStansAlderspensjonFlyttingMellomLandDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<VedtakStansAlderspensjonFlyttingMellomLandDto.SaksbehandlerValg, VedtakStansAlderspensjonFlyttingMellomLandDto.PesysData> {

    data class SaksbehandlerValg(
        val abc: Boolean
    ) : BrevbakerBrevdata

    data class PesysData(
        val brukersBostedsland: String,
        val garantipensjonInnvilget: Boolean,
        val harEksportForbudAvdod: Boolean,
        val minst20ArTrygdetid: Boolean,
        val regelverkType: AlderspensjonRegelverkType,
        val virkDatoFom: LocalDate,
        ) : BrevbakerBrevdata
}
