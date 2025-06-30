package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.VedtaksBegrunnelse
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.Trygdetid
import no.nav.pensjon.brevbaker.api.model.DisplayText

@Suppress("unused")

data class AvslagForLiteTrygdetidAPDto(
    override val pesysData: PesysData,
    override val saksbehandlerValg: SaksbehandlerValg,
) : RedigerbarBrevdata<AvslagForLiteTrygdetidAPDto.SaksbehandlerValg,
        AvslagForLiteTrygdetidAPDto.PesysData> {

    data class SaksbehandlerValg(
        @DisplayText("ABC")
        val abc: String,
    ) : BrevbakerBrevdata

    data class PesysData(
        val avtalelandNavn: String?,
        val bostedsland: String?,
        val erEOSland: Boolean,
        val harAvtaleland: Boolean,
        val regelverkType: AlderspensjonRegelverkType,
        val trygdetid: Trygdetid,
        val vedtaksBegrunnelse: VedtaksBegrunnelse,
    ) : BrevbakerBrevdata

}