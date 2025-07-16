package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.EksportForbudKode
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
        val eksportForbudKode: EksportForbudKode, // hentes fra enten v1.InngangOgEksportVurdering, eller hvis avdød finnes - hentes fra v1.InngangOgEksportVurderingAvdod
        val garantipensjonInnvilget: Boolean,
        val harEksportForbud: Boolean,
        val harEksportForbudAvdod: Boolean, //
        val kravVirkDatoFom: LocalDate,
        val minst20ArTrygdetid: Boolean,
        val regelverkType: AlderspensjonRegelverkType,
        ) : BrevbakerBrevdata
}
