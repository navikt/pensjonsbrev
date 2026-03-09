package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata

import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.BeregningsData
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.etteroppgjorresultat.Etteroppgjoerresultat
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.forrigeetteroppgjor.ForrigeEtteroppgjor
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.kravhode.Kravhode
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.trygdetidavdod.TrygdetidAvdod
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.vilkarsvedtaklist.VilkarsVedtakList
import java.time.LocalDate

data class Vedtaksdata(
    val beregningsdata: BeregningsData?,
    val etteroppgjorresultat: Etteroppgjoerresultat?,
    val faktoromregnet: Boolean?,
    val forrigeetteroppgjor: ForrigeEtteroppgjor?,
    val harLopendealderspensjon: Boolean? = null,
    val kravhode: Kravhode?,
    val reaktiviseringsforskriften: Boolean?,
    val trygdetidavdod: TrygdetidAvdod?,
    val vedtakfattetdatominus1mnd: LocalDate? = null,
    val vilkarsvedtaklist: VilkarsVedtakList?,
    val virkningfom: LocalDate?,
)