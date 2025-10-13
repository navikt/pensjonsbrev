package no.nav.pensjon.brev

import no.nav.brev.brevbaker.AllTemplates
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.maler.UforeVarselFeilutbetaling
import no.nav.pensjon.brev.maler.UforeVedtakFeilutbetaling
import no.nav.pensjon.brev.maler.UforeVedtakFeilutbetalingIngenTilbakekreving
import no.nav.pensjon.brev.maler.uforeavslag.*
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.RedigerbarTemplate

object UfoereTemplates : AllTemplates {
    override fun hentAutobrevmaler(): Set<AutobrevTemplate<BrevbakerBrevdata>> = setOf(
    )

    override fun hentRedigerbareMaler(): Set<RedigerbarTemplate<out RedigerbarBrevdata<*, *>>> = setOf(
        UforeAvslagHensiktsmessigBehandling,
        UforeAvslagHensiktsmessigArbTiltakI1,
        UforeAvslagHensiktsmessigArbTiltakI2,
        UforeAvslagAlder,
        UforeAvslagSykdom,
        UforeAvslagInntektsevne50,
        UforeAvslagInntektsevne40,
        UforeAvslagInntektsevne30,
        UforeAvslagUngUfor26,
        UforeAvslagUngUfor36,
        UforeAvslagUngUforVarig,
        UforeAvslagManglendeDok,
        UforeAvslagYrkesskadeGodkjent,
        UforeAvslagYrkesskadeIkkeGodkjent,
        UforeAvslagIFUIkkeVarig,
        UforeAvslagIFUOktStilling,
        UforegradAvslagInntektsevne,
        UforegradAvslagHensiktsmessigBehandling,
        UforegradAvslagHensiktsmessigArbTiltakI1,
        UforegradAvslagHensiktsmessigArbTiltakI2,
        UforegradAvslagSykdom,
        UforegradAvslagManglendeDok,
        UforeVarselFeilutbetaling,
        UforeVedtakFeilutbetaling,
        UforeVedtakFeilutbetalingIngenTilbakekreving,
    )
}