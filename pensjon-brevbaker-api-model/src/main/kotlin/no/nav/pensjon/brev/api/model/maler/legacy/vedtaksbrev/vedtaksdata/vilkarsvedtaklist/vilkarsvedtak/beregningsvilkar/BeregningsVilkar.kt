package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.vilkarsvedtaklist.vilkarsvedtak.beregningsvilkar

import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate


data class BeregningsVilkar(
    val ieubegrunnelse: String?,
    val ifubegrunnelse: String?,
    val ifuinntekt: Kroner?,
    val trygdetid: Trygdetid?,
    val uforegrad: Int?,
    val virkningstidpunkt: LocalDate?,
    val ieuinntekt: Kroner?,
    val skadetidspunkt: LocalDate?,
)