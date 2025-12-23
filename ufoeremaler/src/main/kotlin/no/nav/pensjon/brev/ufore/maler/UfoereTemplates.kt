package no.nav.pensjon.brev.ufore.maler

import no.nav.brev.brevbaker.AllTemplates
import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.template.AlltidValgbartVedlegg
import no.nav.pensjon.brev.ufore.maler.feilutbetaling.VarselFeilutbetaling
import no.nav.pensjon.brev.ufore.maler.feilutbetaling.VedtakFeilutbetaling
import no.nav.pensjon.brev.ufore.maler.feilutbetaling.VedtakIngenTilbakekreving
import no.nav.pensjon.brev.ufore.maler.feilutbetaling.VedtakIngenTilbakekrevingForeldelse
import no.nav.pensjon.brev.ufore.maler.uforeavslag.*
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.ufore.maler.feilutbetaling.varsel.VarselFeilutbetalingSivilstand12_13_2

object UfoereTemplates : AllTemplates {
    override fun hentAutobrevmaler(): Set<AutobrevTemplate<AutobrevData>> = setOf(
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
        VarselFeilutbetaling,
        VedtakFeilutbetaling,
        VedtakIngenTilbakekreving,
        VedtakIngenTilbakekrevingForeldelse,
        UforeAvslagMedlemskap,
        UforeAvslagMedlemskapUtland,
        VarselFeilutbetalingSivilstand12_13_2,
    )

    override fun hentAlltidValgbareVedlegg(): Set<AlltidValgbartVedlegg<*>> = setOf()
}