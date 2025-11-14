package no.nav.pensjon.brev.ufore.maler

import no.nav.brev.brevbaker.AllTemplates
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.ufore.maler.feilutbetaling.VarselFeilutbetaling
import no.nav.pensjon.brev.ufore.maler.feilutbetaling.VedtakFeilutbetaling
import no.nav.pensjon.brev.ufore.maler.feilutbetaling.VedtakIngenTilbakekreving
import no.nav.pensjon.brev.ufore.maler.feilutbetaling.VedtakIngenTilbakekrevingForeldelse
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.ufore.maler.uforeavslag.UforeAvslagAlder
import no.nav.pensjon.brev.ufore.maler.uforeavslag.UforeAvslagHensiktsmessigArbTiltakI1
import no.nav.pensjon.brev.ufore.maler.uforeavslag.UforeAvslagHensiktsmessigArbTiltakI2
import no.nav.pensjon.brev.ufore.maler.uforeavslag.UforeAvslagHensiktsmessigBehandling
import no.nav.pensjon.brev.ufore.maler.uforeavslag.UforeAvslagIFUIkkeVarig
import no.nav.pensjon.brev.ufore.maler.uforeavslag.UforeAvslagIFUOktStilling
import no.nav.pensjon.brev.ufore.maler.uforeavslag.UforeAvslagInntektsevne30
import no.nav.pensjon.brev.ufore.maler.uforeavslag.UforeAvslagInntektsevne40
import no.nav.pensjon.brev.ufore.maler.uforeavslag.UforeAvslagInntektsevne50
import no.nav.pensjon.brev.ufore.maler.uforeavslag.UforeAvslagManglendeDok
import no.nav.pensjon.brev.ufore.maler.uforeavslag.UforeAvslagMedlemskap
import no.nav.pensjon.brev.ufore.maler.uforeavslag.UforeAvslagSykdom
import no.nav.pensjon.brev.ufore.maler.uforeavslag.UforeAvslagUngUfor26
import no.nav.pensjon.brev.ufore.maler.uforeavslag.UforeAvslagUngUfor36
import no.nav.pensjon.brev.ufore.maler.uforeavslag.UforeAvslagUngUforVarig
import no.nav.pensjon.brev.ufore.maler.uforeavslag.UforeAvslagYrkesskadeGodkjent
import no.nav.pensjon.brev.ufore.maler.uforeavslag.UforeAvslagYrkesskadeIkkeGodkjent
import no.nav.pensjon.brev.ufore.maler.uforeavslag.UforegradAvslagHensiktsmessigArbTiltakI1
import no.nav.pensjon.brev.ufore.maler.uforeavslag.UforegradAvslagHensiktsmessigArbTiltakI2
import no.nav.pensjon.brev.ufore.maler.uforeavslag.UforegradAvslagHensiktsmessigBehandling
import no.nav.pensjon.brev.ufore.maler.uforeavslag.UforegradAvslagInntektsevne
import no.nav.pensjon.brev.ufore.maler.uforeavslag.UforegradAvslagManglendeDok
import no.nav.pensjon.brev.ufore.maler.uforeavslag.UforegradAvslagSykdom

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
        VarselFeilutbetaling,
        VedtakFeilutbetaling,
        VedtakIngenTilbakekreving,
        VedtakIngenTilbakekrevingForeldelse,
        UforeAvslagMedlemskap
    )
}