package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.vilkarsvedtaklist.vilkarsvedtak

import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.vilkarsvedtaklist.vilkarsvedtak.beregningsvilkar.BeregningsVilkar
import java.time.LocalDate

data class VilkarsVedtak(
    val beregningsvilkar: BeregningsVilkar?,
    val vilkar: Vilkar?,
    val vilkarvirkningfom: LocalDate?,
    val vilkarkravlinjekode: String?,
    val vilkarvedtakresultat: String?,
)
