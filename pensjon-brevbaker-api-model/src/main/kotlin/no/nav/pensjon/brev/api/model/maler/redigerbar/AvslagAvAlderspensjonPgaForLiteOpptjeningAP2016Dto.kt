package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.VedtaksBegrunnelse
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.DisplayText

@Suppress("unused")

data class AvslagAvAlderspensjonPgaForLiteOpptjeningAP2016Dto(
    override val pesysData: PesysData,
    override val saksbehandlerValg: SaksbehandlerValg,
) : RedigerbarBrevdata<AvslagAvAlderspensjonPgaForLiteOpptjeningAP2016Dto.SaksbehandlerValg,
        AvslagAvAlderspensjonPgaForLiteOpptjeningAP2016Dto.PesysData> {

    data class SaksbehandlerValg(
        @DisplayText("ABC")
        val abc: String,
    ) : BrevbakerBrevdata

    data class PesysData(
        val erEOSland: Boolean = false,
        val vedtaksBegrunnelse: VedtaksBegrunnelse,
        val avtalelandNavn: String?,
    ) : BrevbakerBrevdata

}