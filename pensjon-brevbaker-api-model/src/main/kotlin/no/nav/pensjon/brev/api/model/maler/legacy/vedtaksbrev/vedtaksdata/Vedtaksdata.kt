package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata

import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.BeregningsData
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.etteroppgjorresultat.Etteroppgjoerresultat
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.forrigeetteroppgjor.ForrigeEtteroppgjor
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.kravhode.Kravhode
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.vilkarsvedtaklist.VilkarsVedtakList
import java.time.LocalDate

data class Vedtaksdata(
    val beregningsdata: BeregningsData?,
    val kravhode: Kravhode?,
    val vilkarsvedtaklist: VilkarsVedtakList?,
    val virkningfom: LocalDate?,
    val faktoromregnet: Boolean?,
    val etteroppgjorresultat: Etteroppgjoerresultat?,
    val forrigeetteroppgjor: ForrigeEtteroppgjor?,
)