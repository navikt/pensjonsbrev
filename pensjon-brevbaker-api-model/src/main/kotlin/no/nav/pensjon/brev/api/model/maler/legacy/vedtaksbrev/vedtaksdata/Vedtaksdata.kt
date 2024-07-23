package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata

import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.BeregningsData
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.kravhode.Kravhode
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.vilkarsvedtaklist.VilkarsVedtakList
import java.time.LocalDate

data class Vedtaksdata(
    val BeregningsData: BeregningsData?,
    val Kravhode: Kravhode?,
    val VilkarsVedtakList: VilkarsVedtakList?,
    val VirkningFOM: LocalDate?,
    val Faktoromregnet: Boolean?,
)